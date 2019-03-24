package com.maiajam.calljots.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.CallLogAdapter;
import com.maiajam.calljots.adapter.pageAdapter;
import com.maiajam.calljots.data.model.ContactLogs;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.ui.fragment.AllNotestFrag;
import com.maiajam.calljots.ui.fragment.CallLogFrag;

import java.util.ArrayList;

public class ContactNotes extends AppCompatActivity {

    ImageView ContactPhoto_img, phone_img;
    TextView ContactName_txt, ContactPhNo_txt;
    ViewPager SPviewPager;
    String Name, PhoneNo,Image_uri;
    int tab;

    CallLogFrag callLogFrag = new CallLogFrag();
    private int Contact_Id;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_notes);

        Intent i = getIntent();
        if (i != null) {
            Name = i.getExtras().getString("name");
            PhoneNo = i.getExtras().getString("phoneNo");
            Image_uri =i.getExtras().getString("image_uri");
            Contact_Id = i.getExtras().getInt(getString(R.string.Contact_Id));
            id = i.getExtras().getInt("Id");
            tab = i.getExtras().getInt("tab",0);
        }

        ContactPhoto_img = (ImageView) findViewById(R.id.SpecContPhoto_imgView);
        phone_img = (ImageView) findViewById(R.id.CallNow_imgView);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ContactName_txt = (TextView) findViewById(R.id.SpecContName_txt);
        ContactPhNo_txt = (TextView) findViewById(R.id.SpecContPhoneNo_txt);

        SPviewPager = (ViewPager) findViewById(R.id.ContactNot_viewPager);



        ContactPhoto_img.setImageDrawable(HelperMethodes.getBitmapImage(Image_uri,getBaseContext()));
        ContactName_txt.setText(Name);
        ContactPhNo_txt.setText(PhoneNo);

        pageAdapter adapter = new pageAdapter(getSupportFragmentManager());
        adapter.setContactInfo(getBaseContext(),Name,PhoneNo,Image_uri,id,Contact_Id,Constant.ONE_CONTACT_NOTE);
        adapter.AddFragment(new AllNotestFrag(), "Notes");
        adapter.AddFragment(callLogFrag, "Call Loge");

        SPviewPager.setAdapter(adapter);
        SPviewPager.setCurrentItem(tab);
        tabLayout.setupWithViewPager(SPviewPager);

        SPviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        phone_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ContactNotes.this, new String[]{Manifest.permission.CALL_PHONE},
                            Constant.MY_PERMISSIONS_REQUEST_CALL_PHONE);

                }
              callAction();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                callLogFrag.onRequestPermissionsResult(requestCode,permissions,grantResults);


    }

    private void callAction() {
        Intent CallAction = new Intent(Intent.ACTION_CALL);
        CallAction.setData(Uri.parse("tel:" + PhoneNo));
        startActivity(CallAction);
    }
}
