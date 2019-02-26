package com.maiajam.calljots.ui.activity;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.db.dbhandler;
import com.maiajam.calljots.data.model.contact;
import com.maiajam.calljots.util.Util;

import java.util.ArrayList;

public class NewContact extends AppCompatActivity implements View.OnClickListener {

    ImageView contactphoto_img;
    EditText ContactName_ed,ContFirstPhone_ed,ContactSecPhone_ed,ContactEmail_ed;
    CheckBox AddAsSpec_ch;
    Button AddContact_b;
    String Name,FirstPhone,SecPhone,Email;
    String imagePath = "" ;


    int SELECT_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        contactphoto_img =(ImageView)findViewById(R.id.ContactPhoto_newContact_img);

        ContactName_ed =(EditText) findViewById(R.id.ContactName_newContact_ed);
        ContactEmail_ed =(EditText) findViewById(R.id.ContactEmail_newContact_ed);
        ContactSecPhone_ed =(EditText) findViewById(R.id.ContactSecPhone_newContact_ed);
        ContFirstPhone_ed =(EditText) findViewById(R.id.ContactFirstPhone_newContact_ed);

        AddAsSpec_ch =(CheckBox)findViewById(R.id.AddasSpec_NewContact_ch);
        AddContact_b =(Button) findViewById(R.id.AddasSpec_NewContact_b);


        contactphoto_img.setOnClickListener(this);
        AddContact_b.setOnClickListener(this);


        setFont();
    }

  

    @Override
    public void onClick(View view) {


        if(view == contactphoto_img) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);

        }
        else if(view == AddContact_b) {
            db = new dbhandler(getBaseContext());
            Name = ContactName_ed.getText().toString();
            FirstPhone = ContFirstPhone_ed.getText().toString();
            SecPhone = ContactSecPhone_ed.getText().toString();
            Email = ContactEmail_ed.getText().toString();


            if (TextUtils.isEmpty(Name)) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.Name), Toast.LENGTH_LONG).show();
            }
                else
                {
                    if (TextUtils.isEmpty(FirstPhone)) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.phone), Toast.LENGTH_LONG).show();
                    } else {
                        if (AddAsSpec_ch.isChecked()) {
                            // add as specailContact

                            contact Newcontact = new contact();
                            Newcontact.setName(Name);
                            Newcontact.setImage_uri(imagePath);
                            Newcontact.setPhoneN0(FirstPhone);
                            Newcontact.setIsSpec(1);

                            db.AddNewContact(Newcontact);
                            db.close();

                            Intent i = new Intent(NewContact.this, AddSpecialContact.class);
                            i.putExtra("name", Name);
                            i.putExtra("phoneNo", FirstPhone);
                            i.putExtra("imageUri", imagePath);
                            startActivity(i);
                            finish();

                            AddToPhoneContact();

                        } else {
                            contact Newcontact = new contact();
                            Newcontact.setName(Name);
                            Newcontact.setImage_uri(imagePath);
                            Newcontact.setPhoneN0(FirstPhone);
                            Newcontact.setIsSpec(0);

                            db.AddNewContact(Newcontact);
                            db.close();
                            finish();

                            AddToPhoneContact();
                        }

                    }
                }


            }

        }



    private void AddToPhoneContact() {
        ArrayList contentProviderOperations = new ArrayList();
        //insert raw contact using RawContacts.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        //insert contact display name using Data.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, Name).build());
        //insert mobile number using Data.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, FirstPhone).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            getApplicationContext().getContentResolver().
                    applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     
        if(resultCode == RESULT_OK)
        {
            if(requestCode == SELECT_PHOTO)
            {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);
                    cursor.close();
                    
                    setImage(imagePath);
                }

            }
        }
    }

    
    
    
    /////
    private void setImage(String imagePath) {
        
        contactphoto_img.setImageDrawable(Util.getBitmapImage(imagePath,getBaseContext()));
    }

    private void setFont() {

        ContactName_ed.setTypeface(Util.GetTypeFace(getBaseContext(),"fonts/regulaer.ttf"));
        ContFirstPhone_ed.setTypeface(Util.GetTypeFace(getBaseContext(),"fonts/regulaer.ttf"));
        ContactSecPhone_ed.setTypeface(Util.GetTypeFace(getBaseContext(),"fonts/regulaer.ttf"));
        ContactEmail_ed.setTypeface(Util.GetTypeFace(getBaseContext(),"fonts/regulaer.ttf"));

        AddContact_b.setTypeface(Util.GetTypeFace(getBaseContext(),"fonts/semibold.ttf"));
    }
}
