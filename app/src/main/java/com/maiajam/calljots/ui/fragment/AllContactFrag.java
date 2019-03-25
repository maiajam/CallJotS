package com.maiajam.calljots.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.AllConAdapter;
import com.maiajam.calljots.adapter.CallLogAdapter;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.data.model.ContactLogs;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.SwipeContrlloerActions;
import com.maiajam.calljots.helper.SwipeControler;
import com.maiajam.calljots.ui.activity.ContactNotes;
import com.maiajam.calljots.ui.activity.MainNewContactActivity;
import com.maiajam.calljots.util.NewContactObserver;
import com.maiajam.calljots.helper.ReadDataThread;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class AllContactFrag extends Fragment {

    ProgressBar progressBar;
    Cursor Cr_phonesNo;
    public RecyclerView recyclerView;
    public List<AllPhoneContact> phoneList;
    public ArrayList<AllPhoneContact> search_list;
    FloatingActionButton Add_b;
    public AllConAdapter allConAdapter;
    List<AllPhoneContact> allPhoneContact;
    private RoomManger roomManger;
    private Handler handler;
    private ReadDataThread myThread;
    private SwipeControler swipeControler;
    private String PhonNo;

    public void AllContactFrag() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_allcont, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.AllCon_Rec);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All Contact");
        Add_b = (FloatingActionButton) view.findViewById(R.id.addNewContact_fab);
        swipeControler = new SwipeControler(new SwipeContrlloerActions() {
            @Override
            public void onLeftClicked(int position) {
            }
            @Override
            public void onRightClicked(int position) {
                //ACTION CALL
                PhonNo = allPhoneContact.get(position).getContPhoneNo();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                            Constant.MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }else {
                    callAction(allPhoneContact.get(position).getContPhoneNo());
                }

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeControler);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
              swipeControler.onDraw();
            }
        });
            NewContactObserver observer = new NewContactObserver(new Handler(), getContext());
            getContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, observer);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    roomManger = RoomManger.getInstance(getActivity());
                    RoomDao roomDao = roomManger.roomDao();
                    allPhoneContact = roomDao.getAllPhoneContact();
                    allConAdapter = new AllConAdapter(getActivity(), allPhoneContact, 0);

                    handler.sendEmptyMessage(0);
                        }
            });

            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {

                    if(allConAdapter != null)
                    {
                        recyclerView.setAdapter(allConAdapter);
                        allConAdapter.notifyDataSetChanged();
                    }
                    super.handleMessage(msg);
                }
            };

        Add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             startActivity(new Intent(getActivity(), MainNewContactActivity.class));
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(Message.obtain()!= null)
                {
                    if(msg.obj != null)
                    {
                        allPhoneContact = (List<AllPhoneContact>) msg.obj;
                        allConAdapter = new AllConAdapter(getActivity(), allPhoneContact, 0);
                        if(allConAdapter != null)
                        {
                            recyclerView.setAdapter(allConAdapter);
                            allConAdapter.notifyDataSetChanged();
                        }
                    }
                }

                super.handleMessage(msg);
            }
        };
        myThread = new ReadDataThread(handler,getContext(),Constant.GET_ALL_PHONE_CONTACT,"");
        myThread.start();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            boolean allgranted = false;
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                allgranted = true ;
            }else {
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_PHONE_STATE);
            }
            if (allgranted) {
                // permission was granted 🙂
              callAction(PhonNo);
            }
        }

    }

    private void callAction(String PhoneNo) {
        Intent CallAction = new Intent(Intent.ACTION_CALL);
        CallAction.setData(Uri.parse("tel:" + PhoneNo));
        startActivity(CallAction);
    }


}

