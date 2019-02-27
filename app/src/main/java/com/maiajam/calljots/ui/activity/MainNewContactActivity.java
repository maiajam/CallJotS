package com.maiajam.calljots.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maiajam.calljots.R;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.ui.fragment.AddSpecialContactFrag;
import com.maiajam.calljots.ui.fragment.NewContact;

public class MainNewContactActivity extends AppCompatActivity {

    private String Name,FirstPhone,imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_contact);

        Bundle extra = getIntent().getExtras();
        if(extra!= null)
        {
            // add one of the phone contact to speail contact
            Name = extra.getString(getString(R.string.NameExtra));
            FirstPhone =  extra.getString(getString(R.string.FirstPhone));
            imagePath = extra.getString(getString(R.string.imageUrl));
            final AddSpecialContactFrag f = new AddSpecialContactFrag();
            f.setcontactInfo(Name, FirstPhone, imagePath);
            HelperMethodes.beginTransAction(getSupportFragmentManager().beginTransaction(),new NewContact(),R.id.frame_newContact);
        }else
        {
            //add new contact
            HelperMethodes.beginTransAction(getSupportFragmentManager().beginTransaction(),new NewContact(),R.id.frame_newContact);
        }

    }
}
