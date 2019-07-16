package com.test.nick.noteshare.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context c){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(c.getApplicationContext(),
                            AppDatabase.class, "test").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){
            @Override
            public void onOpen (@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db); new PopulateDbAsync(INSTANCE).execute();
            }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NoteDao dao;

        PopulateDbAsync(AppDatabase db) {
            dao = db.noteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Note note = new Note(39813742, "sticky", "Please work", "This wont work", "");
            //dao.insertNote(note);
            return null;
        }
    }
}