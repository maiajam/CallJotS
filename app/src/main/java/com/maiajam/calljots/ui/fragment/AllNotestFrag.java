package com.maiajam.calljots.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.AllNotesAdapter;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;

import java.util.ArrayList;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class AllNotestFrag extends Fragment {




    ArrayList<ContactNoteEnitiy> Allnote ;
    AllNotesAdapter adapter ;
    private RoomManger roomManger;

    public void AllNotesFrag()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes,container,false);
        Allnote = new ArrayList<>();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roomManger = RoomManger.getInstance(getActivity());
                RoomDao roomDao = roomManger.roomDao();
                Allnote = (ArrayList<ContactNoteEnitiy>) roomDao.getAllContactsNotes();
                adapter = new AllNotesAdapter(getContext(),Allnote);
            }
        });
        return view ;
    }





}

