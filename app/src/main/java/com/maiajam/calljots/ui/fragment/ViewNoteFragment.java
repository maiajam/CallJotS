package com.maiajam.calljots.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.thread.ReadDataThread;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewNoteFragment extends Fragment {

    @BindView(R.id.conPhonto_NewNote_img)
    ImageView conPhontoNewNoteImg;
    @BindView(R.id.ContName_NewNote_txt)
    TextView ContNameNewNoteTxt;
    @BindView(R.id.ContPhone_NewNote_txt)
    TextView ContPhoneNewNoteTxt;
    @BindView(R.id.NoteTitle_newNote_ed)
    EditText NoteTitleNewNoteEd;
    @BindView(R.id.Note_newNote_ed)
    EditText NoteNewNoteEd;
    @BindView(R.id.newnote_card)
    CardView newnoteCard;
    Unbinder unbinder;
    private Handler getNotehandler;
    private ReadDataThread getNoteThread;
    private int NoteId;
    private int ContactId;

    public ViewNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_note, container, false);
        unbinder = ButterKnife.bind(this, view);
        getNoteById();
        if(ContactId == 0)
        {
            //personal Note
            ContNameNewNoteTxt.setVisibility(View.GONE);

        }else {
            //contact Note

        }
        return view;
    }
    public void setNoteInfo(int noteId,int contactId)
    {
        NoteId = noteId ;
        ContactId = contactId ;
    }
    private void getNoteById() {
        getNoteThread = new ReadDataThread(getNotehandler,getContext(),Constant.GET_NOTE_BY_ID,null);
        getNotehandler = new Handler(){
            private ContactNoteEnitiy contact_obj;
            @Override
            public void handleMessage(Message msg) {
                if(Message.obtain()!= null)
                {
                    if(msg.obj != null)
                    {
                        contact_obj = (ContactNoteEnitiy) msg.obj;
                        NoteTitleNewNoteEd.setText(contact_obj.getContact_NoteTitle().toString());
                        NoteNewNoteEd.setText(contact_obj.getContact_Note().toString());
                    }
                }
                super.handleMessage(msg);
            }
        };
        getNoteThread.setNoteId(NoteId);
        getNoteThread.start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
