package com.maiajam.calljots.data.local.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.entity.SpecialContactInfo;


@Database(entities = {AllPhoneContact.class,ContactNoteEnitiy.class,SpecialContactInfo.class}, version = 1, exportSchema = false)
@TypeConverters({DataTypeConverter.class})
public abstract class RoomManger extends RoomDatabase {

    private static RoomManger roomManger;

    public abstract RoomDao roomDao();

    public static RoomManger getInstance(Context context) {
        if (roomManger == null) {
            roomManger = Room.databaseBuilder(context.getApplicationContext(), RoomManger.class,
                    "callJots_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomManger;
    }

}
