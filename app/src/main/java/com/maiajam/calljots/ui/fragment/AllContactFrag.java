package com.maiajam.calljots.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.AllConAdapter;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.SwipeControler;
import com.maiajam.calljots.ui.activity.MainNewContactActivity;
import com.maiajam.calljots.util.NewContactObserver;
import com.maiajam.calljots.helper.thread.ReadDataThread;

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
    private String[] CALL_LOG_PERMISSIONS = new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG};
    public View view;

    public void AllContactFrag() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_allcont, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.AllCon_Rec);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All Contact");
        Add_b = (FloatingActionButton) view.findViewById(R.id.addNewContact_fab);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

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

    private void callAction(String PhoneNo) {
        Intent CallAction = new Intent(Intent.ACTION_CALL);
        CallAction.setData(Uri.parse("tel:" + PhoneNo));
        startActivity(CallAction);
    }
    public void revertSwipe(int index) {
        allConAdapter.notifyItemChanged(index);
    }


}

