package com.maiajam.calljots.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.model.ContactLogs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.Holder> implements View.OnClickListener{



    Context con;
    List<ContactLogs> ListCallLog;
    Holder holder;
    String NoteTitle,Note,Name;
    int Type,Id,stuts ;
    Date NoteDate ;



   public CallLogAdapter(Context context, List<ContactLogs> List)
    {
        con = context ;
        ListCallLog = List ;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calllog,null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        ContactLogs calLog = ListCallLog.get(position);

        String date = new SimpleDateFormat("dd/MM/yy").format(new Date(Long.parseLong(calLog.getDate())));
        String Time = new SimpleDateFormat("hh:mm a").format(new Date(Long.parseLong(calLog.getDate())));
        String Duration = calLog.getCallDuration();
        String dir = calLog.getDir();
        int duration = Integer.valueOf(Duration);
        int callduration;
        int noOfMin,noOgHour,noOfSec;
        if(duration >= 60 && duration<3600)
        {
           noOgHour = 0;
            noOfMin = duration/60 ;
            noOfSec = duration % 60 ;
            holder.CallDuration_txt.setText(String.format("%02d",noOgHour) +":" +String.format("%02d", noOfMin) +":" + String.format("%02d", noOfSec));
        }else if (duration < 60 && duration != 0)
        {
            noOgHour = 0;
            noOfMin = 0 ;
            noOfSec = duration ;
            holder.CallDuration_txt.setText(String.format("%02d", noOgHour) +":" +String.format("%02d", noOfMin) +":" +String.format("%02d", noOfSec));
        }else if(duration >= 3600)
        {
            noOgHour = duration /3600;
            int reminder = duration % 3600;
            noOfMin = reminder /60;
            noOfSec = reminder % 60 ;
            holder.CallDuration_txt.setText(String.format("%02d", noOgHour) +":" +String.format("%02d", noOfMin) +":" +String.format("%02d",  noOfSec));
        }else if (duration == 0)
        {
            noOgHour = 0;
            noOfMin = 0 ;
            noOfSec = 0 ;
            if (dir.equals("MISSED"))
            {
                holder.Call_txt.setText(con.getString(R.string.missidCall));
                holder.CallDuration_txt.setVisibility(View.INVISIBLE);
            }

        }
        holder.Date_txt.setText(date + " , " +Time );



        if(dir.equals("OUTGOING"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.callType_img.setImageDrawable(con.getDrawable(R.drawable.out));
            }else
            {
                holder.callType_img.setImageDrawable(con.getResources().getDrawable(R.drawable.out));
            }
        }else if (   dir.equals("INCOMING") )
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.callType_img.setImageDrawable(con.getDrawable(R.drawable.income_call));
            }else
            {
                holder.callType_img.setImageDrawable(con.getResources().getDrawable(R.drawable.income_call));
            }

        }else if (dir.equals("MISSED"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.callType_img.setImageDrawable(con.getDrawable(R.drawable.missed));
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.callType_img.setImageDrawable(con.getDrawable(R.drawable.missed));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return ListCallLog.size();
    }

    @Override
    public void onClick(View view) {


    }


    class  Holder extends RecyclerView.ViewHolder{
        ImageView callType_img;
        TextView Date_txt,Call_txt,CallDuration_txt,Permission_txt;

        public Holder(View itemView) {
            super(itemView);
            CallDuration_txt = (TextView)itemView.findViewById(R.id.callDurationVa_row_Txt);
            Call_txt =(TextView)itemView.findViewById(R.id.callDuration_row_Txt);
           Date_txt=(TextView)itemView.findViewById(R.id.Date_row_Txt);
            callType_img =(ImageView)itemView.findViewById(R.id.callType_row_img);
        }
    }

}
