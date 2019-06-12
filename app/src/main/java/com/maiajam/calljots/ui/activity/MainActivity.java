package com.maiajam.calljots.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.AllConAdapter;
import com.maiajam.calljots.adapter.AllNotesAdapter;
import com.maiajam.calljots.adapter.SpecailConAdapter;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.ui.fragment.AllContactFrag;
import com.maiajam.calljots.ui.fragment.AllNotestFrag;
import com.maiajam.calljots.ui.fragment.SpecialContactFrag;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private BottomNavigationView navigationView;
    private String SearchText;
    AllContactFrag allContactFrag = new AllContactFrag();
    private AllContactFrag AlxlContact;
    private AllPhoneContact contact;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AlertDialog dialog;
    private ReadDataThread readDataThread;
    private Handler h;
    private ContactNoteEnitiy NoteItem;
    private boolean isStartup = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Special Contact");

        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        HelperMethodes.beginTransAction(getSupportFragmentManager().beginTransaction(),getSupportFragmentManager(), new SpecialContactFrag(), R.id.frame);

        sp = getBaseContext().getSharedPreferences("FirstVisit", Context.MODE_PRIVATE);
        editor = sp.edit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(isStartup) {
                    ((FrameLayout) findViewById(R.id.frame)).removeAllViews();
                    isStartup = false;
                }
                switch (id) {
                    case R.id.action_MyNotes:
                        Bundle bundle = new Bundle();
                        bundle.putString("text", SearchText);
                        Fragment AllNotes = new AllNotestFrag();
                        AllNotes.setArguments(bundle);
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.frame, AllNotes);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        toolbar.setTitle("My Note");
                        return true;
                    case R.id.action_AllContact:
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("text", SearchText);
                        AlxlContact = new AllContactFrag();
                        AlxlContact.setArguments(bundle2);
                        FragmentManager xmanager = getSupportFragmentManager();
                        FragmentTransaction xtransaction = xmanager.beginTransaction();
                        xtransaction.replace(R.id.frame, AlxlContact);
                        xtransaction.addToBackStack(null);
                        xtransaction.commit();
                        toolbar.setTitle("Phone Contact");
                        return true;

                    case R.id.action_Spec:
                        Fragment AlxxlContact = new SpecialContactFrag();
                        FragmentManager xxmanager = getSupportFragmentManager();
                        FragmentTransaction xxtransaction = xxmanager.beginTransaction();
                        xxtransaction.replace(R.id.frame, AlxxlContact);
                        xxtransaction.addToBackStack(null);
                        xxtransaction.commit();
                        toolbar.setTitle("Special Contact");
                        return true;

                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {


        int id = navigationView.getSelectedItemId();

        switch (id) {
            case R.id.action_MyNotes:
              getSelectedNote(query);
                return true;
            case R.id.action_AllContact:
               getContact(query);
                return true;
            case R.id.action_Spec:
               getSpecialContact(query);
                return true;
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {

        switch (navigationView.getSelectedItemId()) {
            case R.id.action_MyNotes:
                getSelectedNote(newText);
                return true ;
            case R.id.action_AllContact:
                getContact(newText);
                return true;
            case R.id.action_Spec:
                getSpecialContact(newText);
                return true ;
        }
        return true;
    }
    private void getSelectedNote(final String query) {
        final AllNotestFrag AllNotefrag = new AllNotestFrag();
        AllNotefrag.search_list = new ArrayList<>();
        AllNotefrag.search_list.clear();
        AllNotefrag.ContNoteRec = AllNotefrag.view.findViewById(R.id.ContNote_Rec);
        final String[] noteTitle = new String[1];
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                AllNotefrag.noteList = (List<ContactNoteEnitiy>) msg.obj;
                for (int i = 0; i < AllNotefrag.noteList.size(); i++) {
                    NoteItem =  AllNotefrag.noteList.get(i);
                    noteTitle[0] = NoteItem.getContact_NoteTitle();
                    if(noteTitle[0] != null)
                    {
                        if (noteTitle[0].contains(query)) {
                            AllNotefrag.search_list.add(NoteItem);
                        }
                    }
                }
                AllNotefrag.allNoteadapter = new AllNotesAdapter(getBaseContext(),AllNotefrag.search_list);
                AllNotefrag.ContNoteRec.setAdapter(AllNotefrag.allNoteadapter);
                AllNotefrag.allNoteadapter.notifyDataSetChanged();

            }
        };
        readDataThread = new ReadDataThread(h,getBaseContext(),Constant.GET_ALL_NOTES,null);
        readDataThread.start();
    }

    private void getContact(final String newText) {
        final AllContactFrag AllCont = AlxlContact;
        AllCont.search_list = new ArrayList<>();
        AllCont.search_list.clear();
        AllCont.recyclerView = AllCont.view.findViewById(R.id.AllCon_Rec);
        final String[] name = new String[1];
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                AllCont.phoneList = (List<AllPhoneContact>) msg.obj;
                for (int i = 0; i < AllCont.phoneList.size(); i++) {
                    contact = AllCont.phoneList.get(i);
                    name[0] = AllCont.phoneList.get(i).getContName();
                    if (name[0].contains(newText)) {
                        AllCont.search_list.add(contact);
                    }
                }
                AllCont.allConAdapter = new AllConAdapter(getBaseContext(),  AllCont.search_list,0);
                AllCont.recyclerView.setAdapter(AllCont.allConAdapter);
                AllCont.allConAdapter.notifyDataSetChanged();

            }
        };
        readDataThread = new ReadDataThread(h,getBaseContext(),Constant.GET_ALL_PHONE_CONTACT,null);
        readDataThread.start();
    }

    private void getSpecialContact(final String newText) {
        final SpecialContactFrag specialContactFrag = new SpecialContactFrag();
        specialContactFrag.search_list = new  ArrayList<>();
        specialContactFrag.search_list.clear();
        specialContactFrag.recyclerView = specialContactFrag.view.findViewById(R.id.SpecCon_Rec);
        final String[] Specname = new String[1];
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                specialContactFrag.phoneList = (List<AllPhoneContact>) msg.obj;
                for (int i = 0; i < specialContactFrag.phoneList.size(); i++) {
                    contact = specialContactFrag.phoneList.get(i);
                    Specname[0] = specialContactFrag.phoneList.get(i).getContName();
                    if (Specname[0].contains(newText)) {
                        specialContactFrag.search_list.add(contact);
                    }
                }
                specialContactFrag.adapter = new SpecailConAdapter(getBaseContext(),specialContactFrag.search_list,0);
                specialContactFrag.recyclerView.setAdapter( specialContactFrag.adapter);
                specialContactFrag.adapter.notifyDataSetChanged();

            }
        };
        readDataThread = new ReadDataThread(h,getBaseContext(),Constant.GET_ALL_SPECIAL_CONTACT,null);
        readDataThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        allContactFrag.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage(getString(R.string.exit));
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        d.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = d.create();
        dialog.show();
    }

    public void callPhone(String contPhoneNo) {

    }
}
