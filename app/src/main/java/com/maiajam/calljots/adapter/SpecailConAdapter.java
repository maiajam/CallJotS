package com.maiajam.calljots.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.SpecialContactInfo;
import com.maiajam.calljots.helper.HelperMethodes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class SpecailConAdapter extends RecyclerView.Adapter<SpecailConAdapter.Holder> {

    Context con;
    List<AllPhoneContact> ListCont = new ArrayList<>();
    Bitmap photo ;
    int Type  ;

   public SpecailConAdapter(Context context, List<AllPhoneContact> List, int type)
    {
        con = context ;
        ListCont = List ;
        Type = type ;
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
            holder.ContName_txt.setText(name);
            holder.ContPhone_txt.setText(String.valueOf(phoneNo));

            holder.AddToIcon_image.setImageResource(R.drawable.addnote);
            holder.ContName_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*
                    Intent intent = new Intent(con, ContactNotes.class);
                    intent.putExtra("name",name);
                    intent.putExtra("phoneNo",phoneNo);
                    intent.putExtra("image_uri",contact.getContactPhotoUri());
                    intent.putExtra(con.getResources().getString(R.string.Contact_Id),contact.getContId());
                    con.startActivity(intent);
                    */
                }
            });

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
    }


    @Override
    public int getItemCount() {
        return ListCont.size();
    }

    class  Holder extends RecyclerView.ViewHolder{

        ImageView ContPhoto_img,PhoneIcon_img,AddToIcon_image;
        TextView ContName_txt,ContPhone_txt;

        public Holder(View itemView) {
            super(itemView);
            ContName_txt =(TextView)itemView.findViewById(R.id.ContName_txt);
            ContPhone_txt = (TextView)itemView.findViewById(R.id.ContPhoneNo_txt);

            ContPhoto_img =(ImageView)itemView.findViewById(R.id.ContPhoto_imgView);
            AddToIcon_image =(ImageView)itemView.findViewById(R.id.AddToSpec_img);
        }
    }
}
