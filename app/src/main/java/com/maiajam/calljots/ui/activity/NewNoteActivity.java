package com.maiajam.calljots.ui.activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.helper.SharedPrefHelperMethodes;
import com.maiajam.calljots.util.workmanger.ReminerSchudleWorker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class NewNoteActivity extends AppCompatActivity {


    TextView ContactName_txt,ContactPhone_txt;
    EditText NoteTitle_ed,Note_ed;
    ImageView ContactPhoto ;
    ContactNoteEnitiy contact_obj ,contNot;
    String noteTitle,Note,Name,PhoneNo,Image_Uri;
    int Id,NoteFrag;

    Calendar Mycalendar = Calendar.getInstance();
    Boolean RemindeMe = false ;
    Calendar current_Calender ;
    Date current_date ;
    private Handler handler;
    private ReadDataThread myReadThread;
    private Handler getNotehandler;
    private ReadDataThread getNoteThread;

    private OneTimeWorkRequest OneTimeReq;
    private int Contact_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Bundle extra = getIntent().getExtras();
        if (extra != null)
        {
            Name = extra.getString(getString(R.string.NameExtra));
            PhoneNo = extra.getString(getString(R.string.phoneNoExtra));
            Id = extra.getInt("Id");
            Contact_Id = extra.getInt("contact_Id");
            NoteFrag = extra.getInt("NoteFragment");
            Image_Uri = extra.getString("image_uri");
        }
        // current time of this note
        current_Calender = Calendar.getInstance();
        ContactName_txt = (TextView)findViewById(R.id.ContName_NewNote_txt);
        ContactPhone_txt =(TextView)findViewById(R.id.ContPhone_NewNote_txt);
        ContactPhoto =(ImageView)findViewById(R.id.conPhonto_NewNote_img);
        NoteTitle_ed =(EditText)findViewById(R.id.NoteTitle_newNote_ed);
        Note_ed =(EditText)findViewById(R.id.Note_newNote_ed);

        contact_obj = new ContactNoteEnitiy();
        if(NoteFrag == 1)
        {
            // indicate that this activivty have been opend to update the note
            getNotehandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(Message.obtain()!= null)
                    {
                        if(msg.obj != null)
                        {
                            contact_obj = (ContactNoteEnitiy) msg.obj;
                            NoteTitle_ed.setText(contact_obj.getContact_NoteTitle().toString());
                            Note_ed.setText(contact_obj.getContact_Note().toString());
                        }
                    }
                    super.handleMessage(msg);
                }
            };
            getNoteThread = new ReadDataThread(getNotehandler,getBaseContext(),Constant.GET_NOTE_BY_ID,null);
            getNoteThread.setNoteId(Id);
            getNoteThread.start();
        }
        if(Contact_Id == 0)
        {
            // personal note
            ContactName_txt.setVisibility(View.GONE);
            ContactPhone_txt.setVisibility(View.GONE);
            ContactPhoto.setVisibility(View.GONE);
        }else  {
            ContactName_txt.setText(Name);
            ContactPhone_txt.setText(PhoneNo);
            ContactPhoto.setImageDrawable(HelperMethodes.getBitmapImage(Image_Uri,getBaseContext()));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_newnote,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId() ;
        switch (id) {
            case R.id.action_save :
                try {

                    if(TextUtils.isEmpty(NoteTitle_ed.getText().toString()))
                    {
                        Toast.makeText(getBaseContext(),getString(R.string.EnterNoteTitle),Toast.LENGTH_LONG).show();
                    }else {
                        if (TextUtils.isEmpty(Note_ed.getText().toString())) {

                            Toast.makeText(getBaseContext(), getString(R.string.EnterNote), Toast.LENGTH_LONG).show();
                        } else {
                            AddNote();
                            finish();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.action_Reminder :
                RemindeMe = true ;
                ShowDatePicker();
                remindeMe(3);
                break;
        }
        return true ;
    }

    private void ShowDatePicker() {

        DatePickerDialog picker =  new DatePickerDialog(NewNoteActivity.this,setDate,Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);

        picker.setButton(DialogInterface.BUTTON_POSITIVE, "Set Time", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new TimePickerDialog(NewNoteActivity.this,setTime,Calendar.HOUR_OF_DAY,Calendar.MINUTE,true).show();
            }
        });

        picker.show();

    }

    DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int MofYear, int dayOfMounth) {

            Mycalendar.set(year,MofYear,dayOfMounth);
        }
    };

    TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int min) {

            if(hourOfDay > 12)
            {
                Mycalendar.set(Calendar.AM_PM,Calendar.PM);
            }else
            {
                Mycalendar.set(Calendar.AM_PM,Calendar.AM);
            }

            Mycalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            Mycalendar.set(Calendar.MINUTE,min);
        }
    };
    private void remindeMe(int time) {

        OneTimeReq = new OneTimeWorkRequest.Builder(ReminerSchudleWorker.class)
                .setInitialDelay(time,TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance().enqueue(OneTimeReq);
        
    }

    
    private void AddNote() throws ParseException {
            {
                noteTitle = NoteTitle_ed.getText().toString();
                Note = Note_ed.getText().toString();
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                contact_obj = new ContactNoteEnitiy();
                contact_obj.setContact_Note(Note);
                contact_obj.setContact_NoteTitle(noteTitle);
                contact_obj.setContact_Id(Contact_Id);
                contact_obj.setNote_Parent_Id(Id);
                current_date = current_Calender.getTime();
                contact_obj.setContact_LastCallTime(current_date);

                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(Message.obtain()!= null)
                        {
                            if(msg.arg1 == 1)
                            {
                                Toast.makeText(getBaseContext(),getResources().getString(R.string.NoteAdded),Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                        super.handleMessage(msg);
                    }
                };
                if(NoteFrag == 1)
                {// update the note
                    if (RemindeMe) {
                        remindeMe(((int) Calendar.getInstance().getTimeInMillis()));
                    }
                    if(Contact_Id == 0)
                    { // means this is a personal note not contact note and this page open from All notes page
                        contact_obj.setContact_Name("Personal");
                        Name = "Personal";
                    }else {
                        contact_obj.setContact_Name(Name);
                        contact_obj.setId(Id);
                    }
                    myReadThread = new ReadDataThread(handler,getBaseContext(),Constant.UPDATE_NOTE_BY_ID,Name);
                    myReadThread.setNoteId(Id);
                    myReadThread.setNote(contact_obj);
                    myReadThread.start();
                    if(Contact_Id == 0)
                    {
                        finish();
                    }else
                    {
                        // after update the note return back to contact note activity
                        Intent intent = new Intent(NewNoteActivity.this, ContactNotes.class);
                        intent.putExtra("tab", 0);
                        intent.putExtra("name", Name);
                        String phoneNo = HelperMethodes.getContactInfo(getBaseContext()).getContPhoneNo();
                        intent.putExtra("phoneNo", phoneNo);
                        intent.putExtra("image_uri", Image_Uri);
                        startActivity(intent);
                    }
                }else{
                    // new note
                    if(Contact_Id == 0)
                    { // means this is a personal note not contact note and this page open from All notes page
                        contact_obj.setContact_Name("Personal");
                        contact_obj.setNote_Parent_Id(SharedPrefHelperMethodes.getParntIdNoteForPernol(getBaseContext()));
                    }else {
                        contact_obj.setContact_Name(Name);
                        Intent intent = new Intent(NewNoteActivity.this, ContactNotes.class);
                        intent.putExtra("tab", 0);
                        intent.putExtra("name", Name);
                        intent.putExtra("phoneNo", PhoneNo);
                        intent.putExtra("image_uri", Image_Uri);
                        startActivity(intent);
                        finish();
                    }
                    myReadThread = new ReadDataThread(handler, getBaseContext(), Constant.ADD_NEW_NOTE, Name);
                    myReadThread.setNote(contact_obj);
                    myReadThread.start();
                }
            }
        }
    }



