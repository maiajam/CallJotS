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
import com.maiajam.calljots.adapter.pageAdapter;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.ui.fragment.AllNotestFrag;
import com.maiajam.calljots.ui.fragment.CallLogFrag;

public class ContactNotes extends AppCompatActivity {

    ImageView ContactPhoto_img, phone_img;
    TextView ContactName_txt, ContactPhNo_txt;
    ViewPager SPviewPager;
    String Name, PhoneNo,Image_uri;
    int tab;

    CallLogFrag callLogFrag = new CallLogFrag();
    private int Contact_Id;

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
        adapter.setContactInfo(getBaseContext(),Name,PhoneNo,Image_uri,Contact_Id);
        adapter.AddFragment(new AllNotestFrag(), "Notes");
        adapter.AddFragment(callLogFrag, "Call Loge");

        SPviewPager.setAdapter(adapter);
        SPviewPager.setCurrentItem(tab);
        tabLayout.setupWithViewPager(SPviewPager);

        SPviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        phone_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + PhoneNo));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callLogFrag.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RequestCodeCallLog && callLogFrag != null){
            callLogFrag.onActivityResult(requestCode, resultCode, data);
        }
    }
}
