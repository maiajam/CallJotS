package com.maiajam.calljots.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.AllNotesAdapter;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class AllNotestFrag extends Fragment {


    ArrayList<ContactNoteEnitiy> Allnote;
    AllNotesAdapter adapter;
    @BindView(R.id.ContNote_Rec)
    RecyclerView ContNoteRec;
    @BindView(R.id.NoPermission_txt)
    TextView NoPermissionTxt;
    @BindView(R.id.addNewNote_fab)
    FloatingActionButton addNewNoteFab;
    Unbinder unbinder;
    private RoomManger roomManger;
    private Handler handler;

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

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                roomManger = RoomManger.getInstance(getActivity());
                RoomDao roomDao = roomManger.roomDao();
                Allnote = (ArrayList<ContactNoteEnitiy>) roomDao.getAllContactsNotes();
                adapter = new AllNotesAdapter(getContext(), Allnote);
                handler.sendEmptyMessage(0);
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (adapter != null) {
                    ContNoteRec.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                super.handleMessage(msg);
            }
        };

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
}

