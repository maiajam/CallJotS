package com.maiajam.calljots.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.AllNotesAdapter;
import com.maiajam.calljots.adapter.ContNotesAdapter;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.ReadDataThread;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class AllNotestFrag extends Fragment {


    private String Name ;
    ArrayList<ContactNoteEnitiy> Allnote;
    AllNotesAdapter allNoteadapter;
    ContNotesAdapter contNotesAdapter;
    @BindView(R.id.ContNote_Rec)
    RecyclerView ContNoteRec;
    @BindView(R.id.NoPermission_txt)
    TextView NoPermissionTxt;
    @BindView(R.id.addNewNote_fab)
    FloatingActionButton addNewNoteFab;
    Unbinder unbinder;
    private RoomManger roomManger;
    private Handler handler;
    private int ContactNoteIndecator;
    private ReadDataThread readThread;
    private ContactNoteEnitiy allNotes;

    public void AllNotesFrag() {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        unbinder = ButterKnife.bind(this, view);
        Allnote = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ContNoteRec.setLayoutManager(layoutManager);
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(Message.obtain()!= null)
                    {
                        if(msg.obj != null )
                        {
                            allNotes = (ContactNoteEnitiy) msg.obj;
                            if(ContactNoteIndecator == Constant.ONE_CONTACT_NOTE)
                            {
                                contNotesAdapter = new ContNotesAdapter(getContext(), Allnote,0);
                                ContNoteRec.setAdapter(contNotesAdapter);
                                contNotesAdapter.notifyDataSetChanged();
                            }else
                            {
                                allNoteadapter = new AllNotesAdapter(getContext(), Allnote);
                                ContNoteRec.setAdapter(allNoteadapter);
                               allNoteadapter.notifyDataSetChanged();
                            }
                        }
                    }
                    super.handleMessage(msg);
                }
            };

        if(ContactNoteIndecator == Constant.ONE_CONTACT_NOTE)
        {
            // view the contact notes
            readThread = new ReadDataThread(handler,getContext(),Constant.GET_CONTACT_NOTES,Name);
        }else {
            // view all the notes for all contact
            readThread = new ReadDataThread(handler,getContext(),Constant.GET_ALL_NOTES,Name);
        }
        readThread.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.addNewNote_fab)
    public void onViewClicked() {
    }

    public void SetFromWhere(String name)
    {
        ContactNoteIndecator = Constant.ONE_CONTACT_NOTE;
        Name = name ;
    }
}

