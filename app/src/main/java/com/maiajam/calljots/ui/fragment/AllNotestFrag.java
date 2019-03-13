package com.maiajam.calljots.ui.fragment;

import android.content.Intent;
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
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.ui.activity.NewNoteActivity;

import java.util.ArrayList;
import java.util.List;

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
    private List<ContactNoteEnitiy> allNotes;
    private String PhoneNo;
    private String ImageUrl;
    private int Id;
    private int ContId;

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
                            allNotes = (List<ContactNoteEnitiy>) msg.obj;
                            if(ContactNoteIndecator == Constant.ONE_CONTACT_NOTE)
                            {
                                contNotesAdapter = new ContNotesAdapter(getContext(), allNotes,0);
                                ContNoteRec.setAdapter(contNotesAdapter);
                                contNotesAdapter.notifyDataSetChanged();
                            }else
                            {
                                allNoteadapter = new AllNotesAdapter(getContext(), (ArrayList<ContactNoteEnitiy>) allNotes);
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
            readThread = new ReadDataThread(handler,getContext(),Constant.GET_ALL_NOTES,null);
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
        if(ContactNoteIndecator == Constant.ONE_CONTACT_NOTE)
        {
            //add new note for a specail contact
            startActivity(new Intent(getActivity(),NewNoteActivity.class)
                    .putExtra(getString(R.string.NameExtra),Name)
                    .putExtra(getString(R.string.phoneNoExtra),PhoneNo)
                    .putExtra("ImageUrl",ImageUrl)
                    .putExtra("Id",Id)
                    .putExtra("contact_Id",ContId))
                    ;
        }else {
            //add new personal note for the user
            startActivity(new Intent(getActivity(),NewNoteActivity.class));
        }
    }

    public void SetFromWhere(String name, String phoneNo, String imageUrl, int mId, int mContId)
    {
        ContactNoteIndecator = Constant.ONE_CONTACT_NOTE;
        Name = name ;
        PhoneNo = phoneNo ;
        ImageUrl = imageUrl ;
        Id = mId;
        ContId = mContId ;
    }


}

