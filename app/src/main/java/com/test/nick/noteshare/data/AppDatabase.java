package com.test.nick.noteshare.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context c){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(c.getApplicationContext(),
                            AppDatabase.class, "test").build();
                }
            }
        }
        return INSTANCE;
    }
}