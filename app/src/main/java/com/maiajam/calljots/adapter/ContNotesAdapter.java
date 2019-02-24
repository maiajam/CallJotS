package com.maiajam.calljots.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class ContNotesAdapter extends RecyclerView.Adapter<ContNotesAdapter.Holder> implements View.OnClickListener{

    Context con;
    List<ContactNoteEnitiy> ListNotes;
    Holder holder;
    String NoteTitle,Note,Name,PhoneNo;
    int Type,Id,stuts ;
    String NoteDate ;
    android.support.v7.widget.CardView CardView ;
   ContactNoteEnitiy contactNote ;

   String img_uri;
    private RoomManger roomManger;


    public ContNotesAdapter(Context context, List<ContactNoteEnitiy> List, int type)
    {
        con = context ;
        ListNotes = List ;
        Type = type;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contactsnote,parent,false);
        holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

       contactNote = ListNotes.get(position);
        Id = contactNote.getId();
        NoteTitle = contactNote.getContact_NoteTitle();
        Note = contactNote.getContact_Note();
        Name = contactNote.getContact_Name();
        stuts = contactNote.getContact_NoteStuts();
        NoteDate = new SimpleDateFormat("EEE dd/MM/yyyy",Locale.getDefault()).format(contactNote.getContact_LastCallTime());

        if(Type == 1)
        {
            // this fragment has been opend from the main activivty
            holder.NoteDetials.setText(Name);
            holder.contact_img.setVisibility(View.VISIBLE);
        }else
        {
            holder.NoteDetials.setText(Note);
            holder.contact_img.setVisibility(View.GONE);
        }
        holder.NoteTitle_txt.setText(NoteTitle);
        holder.NoteDate_txt.setText(NoteDate);

        if(stuts == 0)
        {
            holder.check_done_img.setVisibility(View.INVISIBLE);
        }else
        {
            holder.check_done_img.setVisibility(View.VISIBLE);
        }

        holder.cardView.setOnClickListener(this);
        holder.menu_img.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return ListNotes.size();
    }

    @Override
    public void onClick(View view) {


        if(view == holder.menu_img)
        {

            PopupMenu pop = new PopupMenu(con,holder.menu_img);
            pop.getMenuInflater().inflate(R.menu.menu_pop_note,pop.getMenu());

            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    switch (id){
                        case R.id.action_delete :
                            roomManger = RoomManger.getInstance(con);
                            RoomDao roomDao = roomManger.roomDao();
                            roomDao.deleteNote( new SimpleDateFormat("EEE dd/MM/yyyy",Locale.getDefault()).format(contactNote.getContact_LastCallTime()));
                            ListNotes.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            break;
                        case R.id.action_edit :
                           /* Intent i = new Intent(con, NewNoteActivity.class);
                            i.putExtra("id",Id);
                            i.putExtra("NoteFragment",1);
                            i.putExtra("contactName", Name);
                            i.putExtra("phoneNo",PhoneNo);
                            i.putExtra("image_uri",img_uri);
                            con.startActivity(i);
                            */
                            break;
                        case R.id.action_markComplete :
                            roomManger = RoomManger.getInstance(con);
                            RoomDao roomDao2 = roomManger.roomDao();
                            contactNote.setContact_NoteStuts(1);
                            roomDao2.update(contactNote);
                            ListNotes.set(holder.getAdapterPosition(),contactNote);
                            notifyItemChanged(holder.getAdapterPosition());
                            break;

                    }
                    return true;
                }
            });
            pop.show();

        }else if(view == holder.cardView)
        {
           /* Intent ViewNote_Int = new Intent(con,ViewNote.class);
            ViewNote_Int.putExtra("name",Name);
            ViewNote_Int.putExtra("noteTitle",NoteTitle);
            ViewNote_Int.putExtra("date",NoteDate.toString());

            con.startActivity(ViewNote_Int);
            */
        }
    }


    class  Holder extends RecyclerView.ViewHolder{


        ImageView menu_img,check_done_img,contact_img;
        TextView NoteTitle_txt,NoteDate_txt,NoteDetials,NoteTaken;
        android.support.v7.widget.CardView cardView ;

        public Holder(View itemView) {
            super(itemView);



            cardView =(android.support.v7.widget.CardView)itemView.findViewById(R.id.ContactNot_CardView);
            NoteTitle_txt = (TextView)itemView.findViewById(R.id.NoteTitle_row_txt);
            NoteDetials =(TextView)itemView.findViewById(R.id.NoteDetials_txt);
            NoteDate_txt=(TextView)itemView.findViewById(R.id.NoteDate_row_txt);
            menu_img = (ImageView)itemView.findViewById(R.id.menu_row_image);
            check_done_img =(ImageView)itemView.findViewById(R.id.checkDone_row_img);
            NoteTaken=(TextView)itemView.findViewById(R.id.NoteToken_row_txt);
            contact_img=(ImageView)itemView.findViewById(R.id.ContactPhoto_all_imageView);


        }


    }
}
