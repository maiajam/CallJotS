package com.maiajam.calljots.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.maiajam.calljots.R;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.ui.fragment.AddSpecialContactFrag;
import com.maiajam.calljots.ui.fragment.NewContact;

public class MainNewContactActivity extends AppCompatActivity {

    private String Name,FirstPhone,imagePath;
    NewContact newContactFrg = new NewContact();
    private int contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_contact);

        Bundle extra = getIntent().getExtras();
        if(extra!= null)
        {
            Name = extra.getString("name");
            if(TextUtils.isEmpty(Name))
            { // add new contact
                FirstPhone =  extra.getString("phoneNo");
                final NewContact f = new NewContact();
                f.setPhoneNo(FirstPhone);
                HelperMethodes.beginTransAction(getSupportFragmentManager().beginTransaction(),f,R.id.frame_newContact);
            }else{
                // add one of the phone contact to speail contact
                FirstPhone =  extra.getString("phoneNo");
                imagePath = extra.getString(getString(R.string.imageUrl));
                contactId = extra.getInt(getString(R.string.Contact_Id));
                final AddSpecialContactFrag f = new AddSpecialContactFrag();
                f.setcontactInfo(Name, FirstPhone, imagePath,contactId);
                HelperMethodes.beginTransAction(getSupportFragmentManager().beginTransaction(),f,R.id.frame_newContact);
            }

        }else
        {//add new contact
            HelperMethodes.beginTransAction(getSupportFragmentManager().beginTransaction(),new NewContact(),R.id.frame_newContact);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        newContactFrg.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
