package com.maiajam.calljots.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
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
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.ReadDataThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllNotesAdapter extends RecyclerView.Adapter<AllNotesAdapter.holder> implements View.OnClickListener {


    Context context ;
    ArrayList<ContactNoteEnitiy> AllNotes ;
    ContactNoteEnitiy contactNote ;
    String contactName,NoteTitle;
    Date NoteDate;
    int stuts,Id;
    holder h ;
    private RoomManger roomManger;
    private Handler handler;
    private ReadDataThread readDataThreaD;

    public AllNotesAdapter(Context context, ArrayList<ContactNoteEnitiy> allNotes)
    {
        this.context = context ;
        this.AllNotes = allNotes ;
    }
    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allnote,parent,false);
        h = new holder(v);
        return h;

    }

    @Override
    public void onBindViewHolder(holder holder, int position)
    {

        contactNote = AllNotes.get(position);
        contactName = contactNote.getContact_Name();
        NoteTitle = contactNote.getContact_NoteTitle();
        NoteDate = contactNote.getContact_LastCallTime();
        stuts = contactNote.getContact_NoteStuts();
        Id= contactNote.getId();
        if(contactName.equals("Personal"))
        {
            //holder.contactName.setText(contactName);
            holder.contactName.setTextColor(context.getResources().getColor(R.color.new_y));
        }
        holder.contactName.setText(contactName);
        holder.noteTitle_txt.setText(NoteTitle);
        String date = new SimpleDateFormat("ddd dd/MM/YYYY hh:mm a").format(NoteDate).toString();
        holder.NoteDate_text.setText(date);

        if(stuts == 1)
        {
            holder.check_done_img.setVisibility(View.VISIBLE);
        }else
        {
            holder.check_done_img.setVisibility(View.GONE);
        }
        holder.menuAll_img.setOnClickListener(this);
        holder.cardView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return AllNotes.size();
    }

    @Override
    public void onClick(View view) {

        if(view == h.menuAll_img)
        {
            PopupMenu pop = new PopupMenu(context,h.menuAll_img);
            pop.getMenuInflater().inflate(R.menu.menu_pop_note,pop.getMenu());

            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    roomManger = RoomManger.getInstance(context);
                    RoomDao roomDao = roomManger.roomDao();
                    switch (id){
                        case R.id.action_delete :
                         //   roomDao.deleteNote(new SimpleDateFormat("ddd dd/MM/YYYY hh:mm a").format(NoteDate).toString());
                            AllNotes.remove(h.getAdapterPosition());
                            notifyItemRemoved(h.getAdapterPosition());
                            break;
                        case R.id.action_edit :
                          /*  Intent i = new Intent(context, NewNoteActivity.class);
                            i.putExtra("id",Id);
                            i.putExtra("NoteFragment",1);
                            i.putExtra("name", contactName);
                            context.startActivity(i);
                            */
                            break;
                        case R.id.action_markComplete :
                            contactNote.setContact_NoteStuts(1);
                            readDataThreaD = new ReadDataThread(handler,context,Constant.UPDATE_NOTE_BY_ID,contactName);
                            readDataThreaD.start();
                        //    roomDao.update(contactNote);
                            AllNotes.set(h.getAdapterPosition(),contactNote);
                            notifyItemChanged(h.getAdapterPosition());
                            break;

                    }
                    return true;
                }
            });
            pop.show();

        }else if(view == h.cardView)
        {
           /* Intent ViewNote_Int = new Intent(context,ViewNote.class);
            ViewNote_Int.putExtra("name",contactName );
            ViewNote_Int.putExtra("noteTitle",NoteTitle);
            context.startActivity(ViewNote_Int);
            */
        }
        }


    class holder extends RecyclerView.ViewHolder{

        TextView noteTitle_txt,NoteDate_text,contactName,NoteTaken_txt;
        ImageView menuAll_img,check_done_img;
        CardView cardView ;

        public holder(View itemView) {
            super(itemView);
            cardView =(CardView)itemView.findViewById(R.id.ContactNotAll_CardView);
            noteTitle_txt = (TextView)itemView.findViewById(R.id.NoteTitleAll_row_txt);
            NoteDate_text=(TextView)itemView.findViewById(R.id.NoteDateAllNote_row_txt);
            menuAll_img = (ImageView)itemView.findViewById(R.id.menuAll_row_imageView);
            check_done_img =(ImageView)itemView.findViewById(R.id.checkDoneAll_row_img);
            NoteTaken_txt=(TextView)itemView.findViewById(R.id.NoteTokenAll_row_txt);
            contactName = (TextView)itemView.findViewById(R.id.ContactNoteName_txt);
        }
    }
}
