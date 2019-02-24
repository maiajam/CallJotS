package com.maiajam.calljots.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.SpecailConAdapter;
import com.maiajam.calljots.data.local.entity.SpecialContactInfo;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class SpecialContactFrag extends Fragment implements SearchView.OnQueryTextListener {


    RecyclerView recyclerView ;
    SpecailConAdapter adapter ;
    TextView txtNoItem,txtWelcome;
    List<SpecialContactInfo> Spec_contactList ,searchList;
    private RoomManger roomManger;

    public void SpecialContactFrag()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_speccont,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.SpecCon_Rec);
        txtNoItem = (TextView) view.findViewById(R.id.NoContact_txt);
        txtWelcome = (TextView) view.findViewById(R.id.welcomSpecial_txt);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    roomManger = RoomManger.getInstance(getContext());
                    RoomDao roomDao = roomManger.roomDao();
                    Spec_contactList =roomDao.getAllSpecContact();
                    int  type = 1;
                    if(Spec_contactList.isEmpty())
                    {
                        txtWelcome.setVisibility(View.VISIBLE);
                        txtNoItem.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {

                        recyclerView.setVisibility(View.VISIBLE);
                        txtNoItem.setVisibility(View.GONE);
                        txtWelcome.setVisibility(View.GONE);
                        //
                        adapter = new SpecailConAdapter(getContext(),Spec_contactList,type);
                        recyclerView.setAdapter(adapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                    }
                }
        });
        return view ;
    }


    @Override
    public boolean onQueryTextSubmit(String s)
    {
       /* searchList = new ArrayList<>();
        String search ;
         searchList.clear();
        for(int i= 0 ; i<Spec_contactList.size();i++)
        {
         //   search = Spec_contactList.get(i).getName();
           // if(search.contains(s))
            //{
              //  searchList.add(Spec_contactList.get(i));
            //}
        //}

        adapter = new AllConAdapter(getContext(),searchList,1);

        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();
        */
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {

       /* searchList = new ArrayList<>();
        String search ;
        searchList.clear();
        for(int i= 0 ; i<Spec_contactList.size();i++)
        {
            search = Spec_contactList.get(i).getName();
            if(search.contains(s))
            {
                searchList.add(Spec_contactList.get(i));
            }
        }

        adapter = new AllConAdapter(getContext(),searchList,1);

        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();
        */
        return true;
    }
}
