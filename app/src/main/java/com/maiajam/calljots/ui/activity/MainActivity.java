package com.maiajam.calljots.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.helper.SharedPrefHelperMethodes;
import com.maiajam.calljots.ui.fragment.AllContactFrag;
import com.maiajam.calljots.ui.fragment.AllNotestFrag;
import com.maiajam.calljots.ui.fragment.SpecialContactFrag;
import com.maiajam.calljots.util.workmanger.MyWorker;

import java.util.ArrayList;

import androidx.work.OneTimeWorkRequest;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    private Boolean LoadContact = false;
    private BottomNavigationView navigationView;
    private String SearchText;
    int page;

    AllContactFrag allContactFrag = new AllContactFrag();
    private AllContactFrag AlxlContact;
    private RoomManger roomManger;
    private AllPhoneContact contact;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AlertDialog dialog;

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

              //  Fragment NoteFrgment = new NotesFrag();
                //((NotesFrag) NoteFrgment).onQueryTextSubmit(query);

            case R.id.action_AllContact:
                final AllContactFrag AllCont = AlxlContact;
                AllCont.search_list = new ArrayList<>();
                AllCont.search_list.clear();
                final String[] name = new String[1];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        roomManger = RoomManger.getInstance(getBaseContext());
                        RoomDao roomDao = roomManger.roomDao();
                        AllCont.phoneList =  roomDao.getAllPhoneContact();
                        for (int i = 0; i < AllCont.phoneList.size(); i++) {
                            contact = AllCont.phoneList.get(i);
                            name[0] = AllCont.phoneList.get(i).getContName();
                            if (name[0].contains(query)) {
                                AllCont.search_list.add(contact);
                            }
                        }
                        AllCont.recyclerView.setAdapter(AllCont.allConAdapter);
                        AllCont.allConAdapter.notifyDataSetChanged();

                    }
                });
                return true;
            case R.id.action_Spec:
                Fragment Spec = new SpecialContactFrag();
                ((SpecialContactFrag) Spec).onQueryTextSubmit(query);

        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        // Toast.makeText(getBaseContext(),newText,Toast.LENGTH_LONG).show();
        SearchText = newText;

        int id = navigationView.getSelectedItemId();

        switch (id) {
            case R.id.action_MyNotes:

            //    Fragment NoteFrgment = new NotesFrag();
              //  ((NotesFrag) NoteFrgment).onQueryTextChange(newText);

            case R.id.action_AllContact:
                AllContactFrag AllCont = (AllContactFrag) getSupportFragmentManager().findFragmentById(R.id.frame);
                //((AllContactFrag) AllCont).onQueryTextChange(newText);


                return true;
            case R.id.action_Spec:
                Fragment Spec = new SpecialContactFrag();

                ((SpecialContactFrag) Spec).onQueryTextChange(newText);

        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        allContactFrag.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("هل أنت متأكد من الخروج ؟");
        d.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        d.setNegativeButton("كلا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = d.create();
        dialog.show();
    }
}
