package com.maiajam.calljots.ui.activity;

import android.app.DatePickerDialog;
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
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.helper.helperMethodes.SharedPrefHelperMethodes;
import com.maiajam.calljots.util.workmanger.ReminerSchudleWorker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.work.BackoffPolicy;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class NewNoteActivity extends AppCompatActivity {


    TextView ContactName_txt, ContactPhone_txt;
    EditText NoteTitle_ed, Note_ed;
    ImageView ContactPhoto;
    ContactNoteEnitiy contact_obj, contNot;
    String noteTitle, Note, Name, PhoneNo, Image_Uri;
    int Id, NoteFrag;
    Calendar Mycalendar = Calendar.getInstance();
    Boolean RemindeMe = false;
    Calendar current_Calender;
    Date current_date;
    DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int MofYear, int dayOfMounth) {

            Mycalendar.set(year, MofYear, dayOfMounth);
        }
    };
    TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int min) {

            if (hourOfDay > 12) {
                Mycalendar.set(Calendar.AM_PM, Calendar.PM);
            } else {
                Mycalendar.set(Calendar.AM_PM, Calendar.AM);
            }

            Mycalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            Mycalendar.set(Calendar.MINUTE, min);
          //  remindeMe(contact_obj.getContact_Name(),contact_obj.getContact_Note(),contact_obj.getContact_NoteTitle());
        }
    };
    private Handler handler;
    private ReadDataThread myReadThread;
    private Handler getNotehandler;
    private ReadDataThread getNoteThread,getIdThread;
    private OneTimeWorkRequest scheduleReq;
    private int Contact_Id;
    private int NoteId;
    private Handler getIdHanler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Name = extra.getString("name");
            PhoneNo = extra.getString(getString(R.string.phoneNoExtra));
            Contact_Id = extra.getInt("contact_Id");
            NoteFrag = extra.getInt("NoteFragment");
            NoteId = extra.getInt("Note_Id");
            Image_Uri = extra.getString("image_uri");
        }
        // current time of this note
        current_Calender = Calendar.getInstance();
        ContactName_txt = (TextView) findViewById(R.id.ContName_NewNote_txt);
        ContactPhone_txt = (TextView) findViewById(R.id.ContPhone_NewNote_txt);
        ContactPhoto = (ImageView) findViewById(R.id.conPhonto_NewNote_img);
        NoteTitle_ed = (EditText) findViewById(R.id.NoteTitle_newNote_ed);
        Note_ed = (EditText) findViewById(R.id.Note_newNote_ed);

        contact_obj = new ContactNoteEnitiy();
        if (NoteFrag == 1) {
            // indicate that this activivty have been opend to update the note
            getNotehandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (Message.obtain() != null) {
                        if (msg.obj != null) {
                            contact_obj = (ContactNoteEnitiy) msg.obj;
                            NoteTitle_ed.setText(contact_obj.getContact_NoteTitle().toString());
                            Note_ed.setText(contact_obj.getContact_Note().toString());
                        }
                    }
                    super.handleMessage(msg);
                }
            };
            getNoteThread = new ReadDataThread(getNotehandler, getBaseContext(), Constant.GET_NOTE_BY_ID, null);
            getNoteThread.setNoteId(NoteId);
            getNoteThread.start();
        }

        if (Contact_Id == 0) {
            // personal note
            ContactName_txt.setVisibility(View.GONE);
            ContactPhone_txt.setVisibility(View.GONE);
            ContactPhoto.setVisibility(View.GONE);
        } else {
            getIdForTheContact(Name);
            ContactName_txt.setText(Name);
            ContactPhone_txt.setText(PhoneNo);
            ContactPhoto.setImageDrawable(HelperMethodes.getBitmapImage(Image_Uri, getBaseContext()));
        }
    }

    private void getIdForTheContact(String name) {

        getIdHanler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(msg.obj != null)
                {
                    Id = (int) msg.obj;
                }
            }
        };
        getIdThread = new ReadDataThread(getIdHanler,getBaseContext(),Constant.GET_ID_FOR_CONTACT,name);
        getIdThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newnote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                try {
                    if (TextUtils.isEmpty(NoteTitle_ed.getText().toString())) {
                        Toast.makeText(getBaseContext(), getString(R.string.EnterNoteTitle), Toast.LENGTH_LONG).show();
                    } else {
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
            case R.id.action_Reminder:
                ShowDatePicker();

                break;
        }
        return true;
    }

    private void ShowDatePicker() {

        int Year, month, DayofMounth;
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog picker = new DatePickerDialog(NewNoteActivity.this, setDate, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        picker.setButton(DialogInterface.BUTTON_POSITIVE, "Set Time", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new TimePickerDialog(NewNoteActivity.this, setTime, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                RemindeMe = true;
            }
        });
        picker.show();
    }

    private void remindeMe(String ContactName,String Note,String NoteTitle ) {

        int IntilaDelay = (int) (Mycalendar.getTimeInMillis()- Calendar.getInstance().getTimeInMillis());
        Data noteData = new Data.Builder()
                .putString(getString(R.string.ContactName_Extra),ContactName)
                .putString(getString(R.string.Note_Extra),Note)
                .putString(getString(R.string.NoteTitle_Extra),NoteTitle)
                .putString(getString(R.string.ContactImgUrl_Extra),Image_Uri)
                .putString(getString(R.string.ContactPhoneNumber_Extra),PhoneNo)
                .putInt(getString(R.string.ContactId_Extra),Contact_Id)
                .build();

        scheduleReq = new OneTimeWorkRequest.Builder(ReminerSchudleWorker.class)
                .setInitialDelay(IntilaDelay,TimeUnit.MILLISECONDS)
                .setInputData(noteData)
                .build();
        WorkManager.getInstance().enqueue(scheduleReq);
    }

    private void AddNote() throws ParseException {
        {
            noteTitle = NoteTitle_ed.getText().toString();
            Note = Note_ed.getText().toString();
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            final ContactNoteEnitiy contact_obj = new ContactNoteEnitiy();
            contact_obj.setContact_Note(Note);
            contact_obj.setContact_NoteTitle(noteTitle);
            contact_obj.setContact_Id(Contact_Id);
            contact_obj.setId(NoteId);
            contact_obj.setNote_Parent_Id(Id);
            current_date = current_Calender.getTime();
            contact_obj.setContact_LastCallTime(current_date);

            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (Message.obtain() != null) {
                        if (msg.arg1 == 1) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.NoteAdded), Toast.LENGTH_LONG).show();
                            if (Contact_Id == 0) {
                                finish();
                            } else {
                                // after update the note return back to contact note activity
                                Intent intent = new Intent(NewNoteActivity.this, ContactNotes.class);
                                intent.putExtra("tab", 0);
                                intent.putExtra("name", Name);
                                String phoneNo = HelperMethodes.getContactInfo(getBaseContext()).getContPhoneNo();
                                intent.putExtra("phoneNo", phoneNo);
                                intent.putExtra("image_uri", Image_Uri);
                                intent.putExtra(getString(R.string.Contact_Id), contact_obj.getContact_Id());
                                intent.putExtra("Id", contact_obj.getNote_Parent_Id());
                                startActivity(intent);
                            }
                        }
                    }
                    super.handleMessage(msg);
                }
            };
            if (NoteFrag == 1) {// update the note
                if (RemindeMe) {
                    remindeMe(contact_obj.getContact_Name(),contact_obj.getContact_Note(),contact_obj.getContact_NoteTitle());
                }
                if (Contact_Id == 0) { // means this is a personal note not contact note and this page open from All notes page
                    contact_obj.setContact_Name("Personal");
                    Name = "Personal";
                } else {
                    contact_obj.setContact_Name(Name);
                    contact_obj.setId(NoteId);
                }
                myReadThread = new ReadDataThread(handler, getBaseContext(), Constant.UPDATE_NOTE_BY_ID, Name);
                myReadThread.setNoteId(NoteId);
                myReadThread.setNote(contact_obj);
                myReadThread.start();

            } else {
                // new note
                if (Contact_Id == Constant.Personal_Note) { // means this is a personal note not contact note and this page open from All notes page
                    contact_obj.setContact_Name("Personal");
                    contact_obj.setNote_Parent_Id(SharedPrefHelperMethodes.getParntIdNoteForPernol(getBaseContext()));
                } else {
                    contact_obj.setContact_Name(Name);
                    contact_obj.setNote_Parent_Id(Id);
                    Intent intent = new Intent(NewNoteActivity.this, ContactNotes.class);
                    intent.putExtra("tab", 0);
                    intent.putExtra("name", Name);
                    intent.putExtra("phoneNo", PhoneNo);
                    intent.putExtra("image_uri", Image_Uri);
                    intent.putExtra(getString(R.string.Contact_Id), contact_obj.getContact_Id());
                    intent.putExtra("Id", contact_obj.getNote_Parent_Id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                if (RemindeMe) {
                    remindeMe(contact_obj.getContact_Name(),contact_obj.getContact_Note(),contact_obj.getContact_NoteTitle());
                }
                myReadThread = new ReadDataThread(handler, getBaseContext(), Constant.ADD_NEW_NOTE, Name);
                myReadThread.setNote(contact_obj);
                myReadThread.start();
            }
        }
    }
}



