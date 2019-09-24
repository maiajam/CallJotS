package com.maiajam.calljots.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.thread.ReadDataThread;
import com.maiajam.calljots.ui.activity.NewNoteActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by maiAjam on 5/7/2018.
 */
public class ContNotesAdapter extends RecyclerView.Adapter<ContNotesAdapter.Holder>{

    Context con;
    List<ContactNoteEnitiy> ListNotes;
    String NoteDate ;
    android.support.v7.widget.CardView CardView ;
    String Name,PhoneNo ;
    int Type;
   String img_uri;
    private RoomManger roomManger;
    private ReadDataThread myThread;
    private Handler handler;
    public ContNotesAdapter(Context context, List<ContactNoteEnitiy> List, int type)
    {
        con = context ;
        ListNotes = List ;
        Type = type;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contactsnote,parent,false);
        return new Holder(v);
    }
    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final String NoteTitle,Note ;
        final int NoteId,stuts ;
        final ContactNoteEnitiy contactNote = ListNotes.get(position);
        final int parentNote_Id = contactNote.getNote_Parent_Id();
        final int contactId = contactNote.getContact_Id();
        NoteId = contactNote.getId();
        NoteTitle = contactNote.getContact_NoteTitle();
        Note = contactNote.getContact_Note();
        if(!TextUtils.isEmpty(NoteTitle)) {
            Name = contactNote.getContact_Name();
            stuts = contactNote.getContact_NoteStuts();
            NoteDate = new SimpleDateFormat("EEE dd/MM/yyyy", Locale.getDefault()).format(contactNote.getContact_LastCallTime());

                if (Type == 1) {
                    // this fragment has been opend from the main activivty
                    holder.NoteDetials.setText(Name);
                    holder.contact_img.setVisibility(View.VISIBLE);
                } else {
                    holder.NoteDetials.setText(Note);
                    holder.contact_img.setVisibility(View.GONE);
                }
                holder.NoteTitle_txt.setText(NoteTitle);
                holder.NoteDate_txt.setText(NoteDate);

                if (stuts == 0) {
                    holder.check_done_img.setVisibility(View.INVISIBLE);
                } else {
                    holder.check_done_img.setVisibility(View.VISIBLE);
                }

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(con, NewNoteActivity.class);
                        i.putExtra("Note_Id", NoteId);
                        i.putExtra("NoteFragment", 1);
                        i.putExtra("name", Name);
                        i.putExtra("Id", contactNote.getNote_Parent_Id());
                        i.putExtra("contact_Id", contactId);
                        i.putExtra("phoneNo", PhoneNo);
                        i.putExtra("image_uri", img_uri);
                        con.startActivity(i);
                    }
                });
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.arg1 == 1) {
                        // the note is updated by the thread
                        contactNote.setContact_NoteStuts(1);
                        ListNotes.set(holder.getAdapterPosition(), contactNote);
                        notifyDataSetChanged();
                    } else {
                        // the note is deleted
                        ListNotes.remove(contactNote);
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                    super.handleMessage(msg);
                }
            };
            final PopupMenu pop = new PopupMenu(con, holder.menu_img);
            pop.getMenuInflater().inflate(R.menu.menu_pop_note, pop.getMenu());

            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    switch (id) {
                        case R.id.action_delete:
                            myThread = new ReadDataThread(handler, con, Constant.DELETE_NOTE_BY_time, null);
                            myThread.setNoteDate(contactNote.getContact_LastCallTime());
                            myThread.start();
                            break;
                        case R.id.action_edit:
                            Intent i = new Intent(con, NewNoteActivity.class);
                            i.putExtra("Id", parentNote_Id);
                            i.putExtra("NoteFragment", 1);
                            i.putExtra("name", Name);
                            i.putExtra("contact_Id",contactId);
                            i.putExtra(con.getString(R.string.phoneNoExtra), PhoneNo);
                            i.putExtra("Note_Id",NoteId);
                            i.putExtra("image_uri", img_uri);
                            con.startActivity(i);
                            break;
                        case R.id.action_markComplete:
                            myThread = new ReadDataThread(handler, con, Constant.UPDATE_NOTE_IS_DONE, null);
                            myThread.setNoteId(NoteId);
                            myThread.start();
                            break;
                    }
                    return false;
                }
            });
                holder.menu_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop.show();
                    }
                });
            }else {
            holder.cardView.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return ListNotes.size();
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
