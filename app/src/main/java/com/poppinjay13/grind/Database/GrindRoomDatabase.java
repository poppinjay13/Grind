package com.poppinjay13.grind.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.poppinjay13.grind.DAO.EventDAO;
import com.poppinjay13.grind.Entities.Event;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class GrindRoomDatabase extends RoomDatabase {

    public abstract EventDAO EventDAO();

    private static GrindRoomDatabase INSTANCE;

    public static GrindRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GrindRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            GrindRoomDatabase.class, "grind_database")
                                    .allowMainThreadQueries()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}