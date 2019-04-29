package com.maiajam.calljots.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;
import com.maiajam.calljots.ui.activity.MainNewContactActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class AllConAdapter extends RecyclerView.Adapter<AllConAdapter.Holder> {

    Context con;
    List<AllPhoneContact> ListCont = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    Holder holder ;
    int Type  ;

   public AllConAdapter(Context context, List<AllPhoneContact> List, int type)
    {
        con = context ;
        ListCont = List ;
        viewBinderHelper.setOpenOnlyOne(true);

    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allcontact,null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final AllPhoneContact contact = ListCont.get(position);
        final String name = contact.getContName();
       final String phoneNo = contact.getContPhoneNo();
       int Isspecail = contact.getContIsSpec();
       final int id = contact.getId();
       final int contactId= contact.getContId();

        holder.ContName_txt.setText(name);
        holder.ContPhone_txt.setText(String.valueOf(phoneNo));
        //
            if(contact.getContactPhotoUri()!= null)
            {
                holder.ContPhoto_img.setImageDrawable( HelperMethodes.getBitmapImage(contact.getContactPhotoUri(),con));

            }else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.ContPhoto_img.setImageDrawable( con.getDrawable(R.drawable.conphoto));
                }else {
                    holder.ContPhoto_img.setImageDrawable( con.getResources().getDrawable(R.drawable.conphoto));
                }
            }

            // all contact tab
            if(Isspecail == 1)
            {
             holder.AddToIcon_image.setVisibility(View.INVISIBLE);
            }

            holder.AddToIcon_image.setImageResource(R.drawable.addspec);
            if(contact.getContactPhotoUri()!=null)
            {
                holder.ContPhoto_img.setImageDrawable(HelperMethodes.getBitmapImage(contact.getContactPhotoUri(),con));
            }else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.ContPhoto_img.setImageDrawable( con.getDrawable(R.drawable.conphoto));
                }else {
                    holder.ContPhoto_img.setImageDrawable( con.getResources().getDrawable(R.drawable.conphoto));
                }
            }


        holder.AddToIcon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con,MainNewContactActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("phoneNo",phoneNo);
                intent.putExtra("id",id);
                intent.putExtra(con.getResources().getString(R.string.imageUrl),contact.getContactPhotoUri());
                intent.putExtra(con.getResources().getString(R.string.Contact_Id),contactId);
                con.startActivity(intent);
            }
        });

        viewBinderHelper.bind(holder.swipeRevealLayout,contact.getContPhoneNo());
        holder.binde(contact);
    }

    @Override
    public int getItemCount() {
        return ListCont.size();
    }

    class  Holder extends RecyclerView.ViewHolder{

       SwipeRevealLayout swipeRevealLayout ;
       View CallLayout ,frontLayout ;
        ImageView ContPhoto_img,PhoneIcon_img,AddToIcon_image;
        TextView ContName_txt,ContPhone_txt;

        public Holder(View itemView) {
            super(itemView);

            swipeRevealLayout = (SwipeRevealLayout)itemView.findViewById(R.id.main_content);
            CallLayout =(View)itemView.findViewById(R.id.delete_layout);
            frontLayout =(View)itemView.findViewById(R.id.front_layout);
            ContName_txt =(TextView)itemView.findViewById(R.id.ContName_txt);
            ContPhone_txt = (TextView)itemView.findViewById(R.id.ContPhoneNo_txt);
            ContPhoto_img =(ImageView)itemView.findViewById(R.id.ContPhoto_imgView);
            AddToIcon_image =(ImageView)itemView.findViewById(R.id.AddToSpec_img);

        }

        public void binde(final AllPhoneContact contact) {

            CallLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCallPhonePerm(contact);
                }
            });

            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private void requestCallPhonePerm(AllPhoneContact contact) {
        if (ContextCompat.checkSelfPermission(con,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) con,
                    new String[]{Manifest.permission.CALL_PHONE},
                    Constant.MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            HelperMethodes.callAction(con, contact.getContPhoneNo());
        }

    }

    public void resultMakeCall(boolean b)
    {
        if(b)
        {

        }
    }
}
