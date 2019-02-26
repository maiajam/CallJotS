package com.maiajam.calljots.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.db.dbhandler;
import com.maiajam.calljots.data.model.contact;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.util.GlobalVariable;
import com.maiajam.calljots.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddSpecialContactFrag extends Fragment implements View.OnClickListener {


    ImageView ContactPhotot_img;
    Button BussinesCat_img, FamilyCat_img, FriendCat_img;
    TextView ContNameSpec_txt, ContPhoneSpec_txt, PrimClass_txt;
    EditText FirstClass_ed, SecClass_ed, CompName_ed, Address_ed;
    Button AddTo_b;
    String img_uri;
    String name, phoneNo;
    // defult value = 1 which means family catg
    int F_clciked, B_clicked, Fr_clicked;
    int CatType = 1;
    int id;

    String FirstClassfication, SecClassification;

    @BindView(R.id.SpecContPhoto_imgView)
    ImageView SpecContPhotoImgView;
    @BindView(R.id.SpecContName_txt)
    TextView SpecContNameTxt;
    @BindView(R.id.SpecContPhoneNo_txt)
    TextView SpecContPhoneNoTxt;
    @BindView(R.id.chosePrimClass_txt)
    TextView chosePrimClassTxt;
    @BindView(R.id.BusIcon_btn)
    Button BusIconBtn;
    @BindView(R.id.FamilyIcon_btn)
    Button FamilyIconBtn;
    @BindView(R.id.FriendIcon_img)
    Button FriendIconImg;
    @BindView(R.id.FirstClassifcation_ed)
    EditText FirstClassifcationEd;
    @BindView(R.id.SecClassifcation_ed)
    EditText SecClassifcationEd;
    @BindView(R.id.CompanyName_ed)
    EditText CompanyNameEd;
    @BindView(R.id.Address_ed)
    EditText AddressEd;
    @BindView(R.id.AddToSpec_b)
    Button AddToSpecB;
    Unbinder unbinder;
    private int contact_id;

    public AddSpecialContactFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_add_special_contact, container, false);
        unbinder = ButterKnife.bind(this, view);

        ContNameSpec_txt.setText(name);
        ContactPhotot_img.setImageDrawable(HelperMethodes.getBitmapImage(img_uri,getActivity()));
        ContPhoneSpec_txt.setText(phoneNo);

        FamilyCat_img.setBackground(getResources().getDrawable(R.drawable.clcikedborder));

        return view;
    }

    @Override
    public void onClick(View view) {

        if (view == AddTo_b) {
            if (CatType == 1) {

                // bussiness type
                FirstClassfication = FirstClass_ed.getText().toString();
                SecClassification = SecClass_ed.getText().toString();
                if (TextUtils.isEmpty(FirstClassfication)) {
                    Toast.makeText(getContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(SecClassification)) {
                    Toast.makeText(getContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();

                } else {

                    contact newContact = new contact();
                    newContact.setName(name);
                    newContact.setPhoneN0(phoneNo);
                    newContact.setFirstClassififaction(FirstClassfication);
                    newContact.setPrimaryClassfication(0);
                    newContact.setSecClassifaction(SecClassification);
                    newContact.setImage_uri(img_uri);
                    newContact.setContactId(contact_id);

                    db.AddContact(newContact);
                    db.SetIsSpecialContact(id);

                    Intent intent = new Intent(AddSpecialContact.this, MainActivity.class);
                    intent.putExtra("tab2", 2);

                    startActivity(intent);

                    // to indcate that this contact has been added to speacail contact table
                    GlobalVariable.Specialcontact_v = 1;

                }


            } else if (CatType == 2) {
                FirstClassfication = FirstClass_ed.getText().toString();
                SecClassification = SecClass_ed.getText().toString();

                if (TextUtils.isEmpty(FirstClassfication)) {
                    Toast.makeText(getBaseContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();
                } else {
                    contact newContact = new contact();
                    newContact.setName(name);
                    newContact.setPhoneN0(phoneNo);
                    newContact.setFirstClassififaction(FirstClassfication);
                    newContact.setPrimaryClassfication(CatType);
                    newContact.setSecClassifaction(SecClassification);
                    newContact.setImage_uri(img_uri);

                    db.AddContact(newContact);
                    db.close();
                    // to indcate that this contact has been added to speacail contact table
                    GlobalVariable.Specialcontact_v = 1;
                    finish();


                    // Intent intent = new Intent(AddSpecialContact.this,MainActivity.class);
                    Intent intent = new Intent(AddSpecialContact.this, MainActivity.class);
                    intent.putExtra("tab2", 2);

                    startActivity(intent);

                }
            } else if (CatType == 3) {

                if (TextUtils.isEmpty(FirstClassfication)) {
                    Toast.makeText(getBaseContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();
                } else {
                    contact newContact = new contact();
                    newContact.setName(name);
                    newContact.setPhoneN0(phoneNo);
                    newContact.setFirstClassififaction(FirstClassfication);
                    newContact.setPrimaryClassfication(CatType);
                    newContact.setSecClassifaction(SecClassification);
                    newContact.setImage_uri(img_uri);
                    db.AddContact(newContact);
                    // to indcate that this contact has been added to speacail contact table
                    GlobalVariable.Specialcontact_v = 1;
                    db.close();
                    finish();
                    Intent intent = new Intent(AddSpecialContact.this, MainActivity.class);
                    intent.putExtra("tab2", 2);

                    startActivity(intent);

                }
            }

        } else if (view == BussinesCat_img) {
            B_clicked = 1;
            CatType = 1;
            CompName_ed.setVisibility(View.VISIBLE);
            Address_ed.setVisibility(View.VISIBLE);


            FamilyCat_img.setBackground(getDrawable(R.drawable.border));
            BussinesCat_img.setBackground(getDrawable(R.drawable.clcikedborder));
            FriendCat_img.setBackground(getDrawable(R.drawable.border));


        } else if (view == FamilyCat_img) {
            CatType = 2;
            CompName_ed.setVisibility(View.GONE);
            Address_ed.setVisibility(View.GONE);

            FamilyCat_img.setBackground(getDrawable(R.drawable.clcikedborder));
            BussinesCat_img.setBackground(getDrawable(R.drawable.border));
            FriendCat_img.setBackground(getDrawable(R.drawable.border));


        } else if (view == FriendCat_img) {
            CatType = 3;
            CompName_ed.setVisibility(View.GONE);
            Address_ed.setVisibility(View.GONE);

            FamilyCat_img.setBackground(getDrawable(R.drawable.border));
            BussinesCat_img.setBackground(getDrawable(R.drawable.border));
            FriendCat_img.setBackground(getDrawable(R.drawable.clcikedborder));


        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
