package com.maiajam.calljots.ui.fragment;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.CallLogAdapter;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class NewContact extends Fragment{


    String Name, FirstPhone, SecPhone, Email;
    String imagePath = "";
    private static final String[] READ_CONTACT_PERMISSIONS =
            new String[]{Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    int SELECT_PHOTO = 1;
    @BindView(R.id.ContactPhoto_newContact_img)
    ImageView ContactPhotoNewContactImg;
    @BindView(R.id.ContactName_newContact_ed)
    EditText ContactNameNewContactEd;
    @BindView(R.id.ContactFirstPhone_newContact_ed)
    EditText ContactFirstPhoneNewContactEd;
    @BindView(R.id.ContactSecPhone_newContact_ed)
    EditText ContactSecPhoneNewContactEd;
    @BindView(R.id.ContactEmail_newContact_ed)
    EditText ContactEmailNewContactEd;
    @BindView(R.id.AddasSpec_NewContact_ch)
    CheckBox AddasSpecNewContactCh;
    @BindView(R.id.AddasSpec_NewContact_b)
    Button AddasSpecNewContactB;
    Unbinder unbinder;
    private Handler handler;
    private ReadDataThread readThread;
    private String PhoneNO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_contact, container, false);
        unbinder = ButterKnife.bind(this, view);
        ContactFirstPhoneNewContactEd.setText(PhoneNO);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
        ContactPhotoNewContactImg.setImageDrawable(HelperMethodes.getBitmapImage(imagePath, getContext()));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ContactPhoto_newContact_img, R.id.AddasSpec_NewContact_b})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ContactPhoto_newContact_img:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            case R.id.AddasSpec_NewContact_b:
                AddNewContact();
                break;
        }
    }

    private void AddNewContact() {

        Name = ContactNameNewContactEd.getText().toString();
        FirstPhone = ContactFirstPhoneNewContactEd.getText().toString();
        SecPhone = ContactNameNewContactEd.getText().toString();
        Email = ContactEmailNewContactEd.getText().toString();

        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(getContext(), getResources().getString(R.string.Name), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(FirstPhone)) {
            Toast.makeText(getContext(), getResources().getString(R.string.phone), Toast.LENGTH_LONG).show();
            return;
        }
        AllPhoneContact Newcontact = new AllPhoneContact();
        Newcontact.setContName(Name);
        // Newcontact.setI(imagePath);
        Newcontact.setContPhoneNo(FirstPhone);
        int contId = new Random().nextInt(500);
        Newcontact.setContId(contId);

        if (AddasSpecNewContactCh.isChecked()) {
            // add as special contact
            Newcontact.setContIsSpec(1);

            final AddSpecialContactFrag f = new AddSpecialContactFrag();
            f.setcontactInfo(Name, FirstPhone, imagePath,contId);
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (Message.obtain() != null) {
                        if (msg.arg1 == 1) {
                            // start fragment add special contact info after add the contact
                            Toast.makeText(getContext(), getResources().getString(R.string.AddDone), Toast.LENGTH_LONG).show();
                            AddToPhoneContact();
                            HelperMethodes.beginTransAction(getFragmentManager().beginTransaction(), f, R.id.frame_newContact);

                        }
                    }
                    super.handleMessage(msg);
                }
            };
        } else {
            Newcontact.setContIsSpec(0);
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (Message.obtain() != null) {
                        if (msg.arg1 == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.AddDone), Toast.LENGTH_LONG).show();
                            AddToPhoneContact();
                            // return back to the All contact tab
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("tab2", 1);
                            startActivity(intent);
                        }
                    }
                    super.handleMessage(msg);
                }
            };
        }

        readThread = new ReadDataThread(handler, getActivity(), Constant.ADD_NEW_CONTACT, null);
        readThread.setContactInfo(Newcontact);
        readThread.start();

    }



    private void AddToPhoneContact() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(READ_CONTACT_PERMISSIONS, Constant.REQUEST_CODE_READ_WRITE);

        } else {
            AddTheContact();
        }
    }

    private void AddTheContact() {
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
            getContext().getContentResolver().
                    applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.REQUEST_CODE_READ_WRITE) {
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
                AddTheContact();

            }
        }

    }

    public void setPhoneNo(String firstPhone) {
        PhoneNO = firstPhone ;
    }
}
