package com.maiajam.calljots.data.local.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.helper.GetAllPhoneContactThread;
import com.maiajam.calljots.ui.fragment.AllContactFrag;


@Database(entities = {AllPhoneContact.class,ContactNoteEnitiy.class}, version = 1, exportSchema = false)
@TypeConverters({DataTypeConverter.class})
public abstract class RoomManger extends RoomDatabase {

    private static RoomManger roomManger;
    private static Context mcontext;

    public abstract RoomDao roomDao();


    public static RoomManger getInstance(Context context) {

        mcontext = context ;
        if (roomManger == null) {
            roomManger = Room.databaseBuilder(context.getApplicationContext(), RoomManger.class,
                    "callJots_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomMangerCallBacck)
                    .build();
        }
        return roomManger;
    }

    private static RoomManger.Callback roomMangerCallBacck = new RoomManger.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new GetAllPhoneContactThread(mcontext).start();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
