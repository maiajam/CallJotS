package com.maiajam.calljots.ui.fragment;

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
import android.widget.SearchView;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.SpecailConAdapter;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class SpecialContactFrag extends Fragment implements SearchView.OnQueryTextListener {


    public ArrayList<AllPhoneContact> search_list;
    public List<AllPhoneContact> phoneList;
    public RecyclerView recyclerView;
    public SpecailConAdapter adapter;
    TextView txtNoItem, txtWelcome;
    List<AllPhoneContact> Spec_contactList;
    ReadDataThread readDataThread;
    Handler handler;
    Unbinder unbinder;
    private FloatingActionButton addNewSpec;
    public View view;

    public void SpecialContactFrag() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.frag_speccont, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.SpecCon_Rec);
        txtNoItem = (TextView) view.findViewById(R.id.NoContact_txt);
        txtWelcome = (TextView) view.findViewById(R.id.welcomSpecial_txt);
        addNewSpec = (FloatingActionButton) view.findViewById(R.id.addNewSpecContact_fab);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (Message.obtain() != null) {
                    if (msg.obj != null) {
                        int type = 1;
                        Spec_contactList = (List<AllPhoneContact>) msg.obj;
                        if (Spec_contactList.isEmpty()) {
                            txtWelcome.setVisibility(View.VISIBLE);
                            txtNoItem.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {

                            recyclerView.setVisibility(View.VISIBLE);
                            txtNoItem.setVisibility(View.GONE);
                            txtWelcome.setVisibility(View.GONE);
                            //
                            adapter = new SpecailConAdapter(getContext(), Spec_contactList, type);
                            recyclerView.setAdapter(adapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                        }
                    }
                }

                super.handleMessage(msg);
            }
        };

        readDataThread = new ReadDataThread(handler, getContext(), Constant.GET_ALL_SPECIAL_CONTACT, null);
        readDataThread.start();


        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.addNewSpecContact_fab)
    public void onViewClicked() {
        HelperMethodes.beginTransAction(getFragmentManager().beginTransaction(),getFragmentManager(),new AllContactFrag(),R.id.frame);
    }
}
