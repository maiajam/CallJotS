package com.maiajam.calljots.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.CallLogAdapter;
import com.maiajam.calljots.data.model.CalLog;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.util.Constant;
import com.maiajam.calljots.util.PermissionsUtils;
import com.maiajam.calljots.util.Util;

import java.util.ArrayList;
import java.util.List;


public class CallLogFrag extends Fragment {

    String Name ;
    int PhoneNo ;
    RecyclerView callog_Rec;
    CallLogAdapter callLogAdapter;
    List<CalLog> lisLog ;
    CallLogAdapter adapter ;
    TextView noPermesioin_txt;
    private Cursor cursor;
    private LinearLayoutManager Laymang;
    private int Contact_Id;
    private String Contact_Number;


    public CallLogFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_call_log, container, false);

        callog_Rec =(RecyclerView)v.findViewById(R.id.ContNote_Rec);
        noPermesioin_txt =(TextView)v.findViewById(R.id.NoPermission_txt);

        Name = getArguments().getString("name");
        Contact_Id = getArguments().getInt(getString(R.string.Contact_Id));
        Contact_Number = getArguments().getString("phoneNo");

        if(!PermissionsUtils.isCallLogPermissionsGranted(getActivity()))
        {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},
                    Constant.RequestCodeCallLog);
        }else {

            lisLog = new ArrayList<>();
            lisLog =  Util.getCallLogsList(getActivity(),Contact_Id,Contact_Number);

            if(lisLog == null || lisLog.isEmpty())
            {
                noPermesioin_txt.setVisibility(View.VISIBLE);
                callog_Rec.setVisibility(View.GONE);
            }else {
                Laymang = new LinearLayoutManager(getContext());
                callLogAdapter = new CallLogAdapter(getActivity(),lisLog);
                callog_Rec.setLayoutManager(Laymang);
                callog_Rec.setAdapter(callLogAdapter);
                callLogAdapter.notifyDataSetChanged();
            }
        }
        return v ;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.RequestCodeCallLog) {
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {
                // permission was granted 🙂

                ArrayList<CalLog> callLogArrayList = new ArrayList<>();
               callLogArrayList = Util.getCallLogsList(getActivity(),Contact_Id,Contact_Number);
                if (callLogArrayList != null) {
                    // app has permissions and we get call logs
                    callLogAdapter = new CallLogAdapter(getActivity(),callLogArrayList);
                    callog_Rec.setAdapter(callLogAdapter);
                    callLogAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
