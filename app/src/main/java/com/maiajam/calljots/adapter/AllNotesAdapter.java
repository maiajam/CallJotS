package com.maiajam.calljots.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
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
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.ui.activity.NewNoteActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllNotesAdapter extends RecyclerView.Adapter<AllNotesAdapter.holder> implements View.OnClickListener {


    Context context;
    ArrayList<ContactNoteEnitiy> AllNotes;


    private RoomManger roomManger;
    private Handler handler;
    private ReadDataThread readDataThreaD;

    public AllNotesAdapter(Context context, ArrayList<ContactNoteEnitiy> allNotes) {
        this.context = context;
        this.AllNotes = allNotes;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allnote, parent, false);
        return new holder(v);

    }

    @Override
    public void onBindViewHolder(final holder holder, int position) {
        final ContactNoteEnitiy contactNote;
        final String contactName, NoteTitle;
        Date NoteDate;
        final int stuts, Id;
        contactNote = AllNotes.get(position);
        contactName = contactNote.getContact_Name();
        NoteTitle = contactNote.getContact_NoteTitle();
        NoteDate = contactNote.getContact_LastCallTime();
        stuts = contactNote.getContact_NoteStuts();
        final int contactId= contactNote.getContact_Id();
        final int parentNote_Id = contactNote.getNote_Parent_Id();
        if (!TextUtils.isEmpty(NoteTitle)) {
            Id = contactNote.getId();
            if (contactName.equals("Personal")) {
                //holder.contactName.setText(contactName);
                holder.contactName.setTextColor(context.getResources().getColor(R.color.colorSecondAccent));
            }
            holder.contactName.setText(contactName);
            holder.noteTitle_txt.setText(NoteTitle);
            String date = new SimpleDateFormat("EEE dd/MM/yyyy hh:mm a").format(NoteDate).toString();
            holder.NoteDate_text.setText(date);

            if (stuts == 1) {
                holder.check_done_img.setVisibility(View.VISIBLE);
            } else {
                holder.check_done_img.setVisibility(View.GONE);
            }
            final PopupMenu pop = new PopupMenu(context,holder.menuAll_img);
            pop.getMenuInflater().inflate(R.menu.menu_pop_note, pop.getMenu());

            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.action_delete:
                            handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    AllNotes.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(),AllNotes.size());
                                }
                            };
                            readDataThreaD = new ReadDataThread(handler, context, Constant.DELETE_NOTE_BY_time, null);
                            readDataThreaD.setNoteDate(contactNote.getContact_LastCallTime());
                            readDataThreaD.start();

                            break;
                        case R.id.action_edit:
                            Intent i = new Intent(context, NewNoteActivity.class);
                            i.putExtra("Id", parentNote_Id);
                            i.putExtra("NoteFragment", 1);
                            i.putExtra("name",contactName );
                            i.putExtra("contact_Id",contactId);
                            i.putExtra("Note_Id",contactNote.getId());
                            context.startActivity(i);
                            break;
                        case R.id.action_markComplete:
                            contactNote.setContact_NoteStuts(1);
                            handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    contactNote.setContact_NoteStuts(1);
                                    AllNotes.set(holder.getAdapterPosition(), contactNote);
                                    notifyItemChanged(holder.getAdapterPosition());
                                }
                            };
                            readDataThreaD = new ReadDataThread(handler, context, Constant.UPDATE_NOTE_BY_ID, contactName);
                            readDataThreaD.setNote(contactNote);
                            readDataThreaD.start();
                            break;
                    }
                    return false;
                }
            });
            holder.menuAll_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop.show();
                }
            });
            holder.cardView.setOnClickListener(this);
        } else {
            holder.cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return AllNotes.size();
    }

    @Override
    public void onClick(View view) {
    }


    class holder extends RecyclerView.ViewHolder {

        TextView noteTitle_txt, NoteDate_text, contactName, NoteTaken_txt;
        ImageView menuAll_img, check_done_img;
        CardView cardView;

        public holder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.ContactNotAll_CardView);
            noteTitle_txt = (TextView) itemView.findViewById(R.id.NoteTitleAll_row_txt);
            NoteDate_text = (TextView) itemView.findViewById(R.id.NoteDateAllNote_row_txt);
            menuAll_img = (ImageView) itemView.findViewById(R.id.menuAll_row_imageView);
            check_done_img = (ImageView) itemView.findViewById(R.id.checkDoneAll_row_img);
            NoteTaken_txt = (TextView) itemView.findViewById(R.id.NoteTokenAll_row_txt);
            contactName = (TextView) itemView.findViewById(R.id.ContactNoteName_txt);
        }
    }
}
