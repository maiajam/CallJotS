package com.maiajam.calljots.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;
import com.maiajam.calljots.ui.activity.ContactNotes;
import com.maiajam.calljots.ui.activity.NewNoteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class SpecailConAdapter extends RecyclerView.Adapter<SpecailConAdapter.Holder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    Context con;
    List<AllPhoneContact> ListCont = new ArrayList<>();
    int Type;

    public SpecailConAdapter(Context context, List<AllPhoneContact> List, int type) {
        con = context;
        ListCont = List;
        Type = type;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allcontact, null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final AllPhoneContact contact = ListCont.get(position);
        final String name = contact.getContName();
        final String phoneNo = contact.getContPhoneNo();
        final int id = contact.getId();
        holder.ContName_txt.setText(name);
        holder.ContPhone_txt.setText(String.valueOf(phoneNo));

        holder.AddToIcon_image.setImageResource(R.drawable.addnote);
        holder.ContName_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethodes.setContactNameInfo(con, name, phoneNo, contact.getContactPhotoUri(), contact.getContId());
                Intent intent = new Intent(con, ContactNotes.class);
                intent.putExtra("name", name);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("image_uri", contact.getContactPhotoUri());
                intent.putExtra(con.getResources().getString(R.string.Contact_Id), contact.getContId());
                intent.putExtra("Id", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                con.startActivity(intent);
            }
        });
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethodes.setContactNameInfo(con, name, phoneNo, contact.getContactPhotoUri(), contact.getContId());
                Intent intent = new Intent(con, ContactNotes.class);
                intent.putExtra("name", name);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("image_uri", contact.getContactPhotoUri());
                intent.putExtra(con.getResources().getString(R.string.Contact_Id), contact.getContId());
                intent.putExtra("Id", id);
                intent.putExtra(con.getResources().getString(R.string.Indecator), Constant.ONE_CONTACT_NOTE);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                con.startActivity(intent);
            }
        });
        holder.AddToIcon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(con, NewNoteActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("image_uri", contact.getContactPhotoUri());
                intent.putExtra("Id", contact.getId());
                intent.putExtra("contact_Id", contact.getContId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                con.startActivity(intent);
            }
        });
        if (contact.getContactPhotoUri() != null) {
            holder.ContPhoto_img.setImageDrawable(HelperMethodes.getBitmapImage(contact.getContactPhotoUri(), con));

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ContPhoto_img.setImageDrawable(con.getDrawable(R.drawable.conphoto));
            } else {
                holder.ContPhoto_img.setImageDrawable(con.getResources().getDrawable(R.drawable.conphoto));
            }
        }

        holder.Editet_txt.setText("Edit");
        viewBinderHelper.bind(holder.swipeRevealLayout, contact.getContPhoneNo());
        holder.binde(contact);
    }


    @Override
    public int getItemCount() {
        return ListCont.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        SwipeRevealLayout swipeRevealLayout;
        View CallLayout, frontLayout;
        ImageView ContPhoto_img, PhoneIcon_img, AddToIcon_image;
        TextView ContName_txt, ContPhone_txt, Editet_txt;
        LinearLayout lin;

        public Holder(View itemView) {
            super(itemView);
            swipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.main_content);
            CallLayout = (View) itemView.findViewById(R.id.delete_layout);
            frontLayout = (View) itemView.findViewById(R.id.front_layout);
            Editet_txt = (TextView) CallLayout.findViewById(R.id.Call_text);
            ContName_txt = (TextView) itemView.findViewById(R.id.ContName_txt);
            ContPhone_txt = (TextView) itemView.findViewById(R.id.ContPhoneNo_txt);

            ContPhoto_img = (ImageView) itemView.findViewById(R.id.ContPhoto_imgView);
            AddToIcon_image = (ImageView) itemView.findViewById(R.id.AddToSpec_img);
            lin = (LinearLayout) itemView.findViewById(R.id.Allconiiitem_Lin);

        }

        public void binde(final AllPhoneContact contact) {

            CallLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });

            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

}



