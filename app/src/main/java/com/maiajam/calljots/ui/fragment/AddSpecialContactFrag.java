package com.maiajam.calljots.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.ui.activity.MainActivity;
import com.maiajam.calljots.helper.ReadDataThread;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddSpecialContactFrag extends Fragment implements View.OnClickListener {

    String img_uri;
    String name, phoneNo;

    int F_clciked, B_clicked, Fr_clicked;
    // defult value = 1 which means family catg
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
    private ReadDataThread addThread;
    private Handler handler;
    private String CompanyName;
    private String CompanyAdress;

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

        SpecContNameTxt.setText(name);
        SpecContPhotoImgView.setImageDrawable(HelperMethodes.getBitmapImage(img_uri,getActivity()));
       SpecContPhoneNoTxt.setText(phoneNo);
        FamilyIconBtn.setBackground(getResources().getDrawable(R.drawable.clcikedborder));

        AddToSpecB.setOnClickListener(this);
        BusIconBtn.setOnClickListener(this);
        FriendIconImg.setOnClickListener(this);
        FamilyIconBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

        if (view == AddToSpecB) {
            FirstClassfication = FirstClassifcationEd.getText().toString();
            SecClassification = SecClassifcationEd.getText().toString();
            if (TextUtils.isEmpty(FirstClassfication)) {
                Toast.makeText(getContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(FirstClassfication)) {
                Toast.makeText(getContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();
                return;
            }
            AllPhoneContact newContact = new AllPhoneContact();
            newContact.setContName(name);
            newContact.setContPhoneNo(phoneNo);
            newContact.setContFirstClassf(FirstClassfication);
            newContact.setContPrimaryClassf(CatType);
            newContact.setContSecClassF(SecClassification);
            newContact.setContactPhotoUri(img_uri);
            newContact.setContId(contact_id);
            if (CatType == 3) {

                CompanyName = CompanyNameEd.getText().toString();
                CompanyAdress = AddressEd.getText().toString();

                if (TextUtils.isEmpty(CompanyAdress)) {
                    Toast.makeText(getContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(CompanyName)) {
                    Toast.makeText(getContext(), "Please Enter the First Classifaction for This Contact", Toast.LENGTH_LONG).show();
                    return;
                }
                newContact.setContCompanyName(CompanyName);
                newContact.setContAddress(CompanyAdress);

            }
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (Message.obtain() != null) {
                        if (msg.arg1 == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.AddDone), Toast.LENGTH_LONG).show();
                            // return back to the specail contact tab
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("tab2", 2);
                            startActivity(intent);
                        }
                    }
                    super.handleMessage(msg);
                }
            };
            addThread = new ReadDataThread(handler, getContext(), Constant.ADD_TO_SPECIAL_CONTACT, null);
            addThread.setSpecialContactInfo(newContact);
            addThread.start();


        } else if (view == BusIconBtn) {
            B_clicked = 1;
            CatType = 3;
            CompanyNameEd.setVisibility(View.VISIBLE);
            AddressEd.setVisibility(View.VISIBLE);
            FamilyIconBtn.setBackground(getResources().getDrawable(R.drawable.border));
            BusIconBtn.setBackground(getResources().getDrawable(R.drawable.clcikedborder));
            FriendIconImg.setBackground(getResources().getDrawable(R.drawable.border));

        } else if (view == FamilyIconBtn) {
            CatType = 1;
            CompanyNameEd.setVisibility(View.GONE);
            AddressEd.setVisibility(View.GONE);
            FamilyIconBtn.setBackground(getResources().getDrawable(R.drawable.clcikedborder));
            BusIconBtn.setBackground(getResources().getDrawable(R.drawable.border));
            FriendIconImg.setBackground(getResources().getDrawable(R.drawable.border));
        } else if (view == FriendIconImg) {
            CatType = 2;
            CompanyNameEd.setVisibility(View.GONE);
            AddressEd.setVisibility(View.GONE);
            FamilyIconBtn.setBackground(getResources().getDrawable(R.drawable.border));
            BusIconBtn.setBackground(getResources().getDrawable(R.drawable.border));
            FriendIconImg.setBackground(getResources().getDrawable(R.drawable.clcikedborder));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setcontactInfo(String Name, String FirstPhone, String imagePath,int contact_id) {

        name = Name;
        this.contact_id = contact_id ;
        phoneNo = FirstPhone;
        img_uri = imagePath ;

    }
}
