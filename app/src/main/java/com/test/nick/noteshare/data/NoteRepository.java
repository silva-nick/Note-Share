package com.test.nick.noteshare.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao dao;
    private LiveData<List<Note>> allNotes;


    public NoteRepository(Application application){
        AppDatabase database = AppDatabase.getDatabase(application);
        dao = database.noteDao();
        allNotes = dao.loadAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public void insert(Note note){
        new insertAsyncTask(dao).execute(note);
    }

    public void delete(Note note) {
        new deleteAsyncTask(dao).execute(note);
    }

    public void update(Note note) { new updateAsyncTask(dao).execute(note); }


    private static class insertAsyncTask extends AsyncTask<Note, Void, Void>{
        private NoteDao asyncDao;

        public insertAsyncTask(NoteDao nDao){
            asyncDao = nDao;
        }

        @Override
        protected Void doInBackground(final Note... params){
            asyncDao.insertNote(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Note, Void, Void>{
        private NoteDao asyncDao;

        public deleteAsyncTask(NoteDao nDao){
            asyncDao = nDao;
        }

        @Override
        protected Void doInBackground(final Note... params){
            asyncDao.deleteNote(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Note, Void, Void>{
        private NoteDao asyncDao;

        public updateAsyncTask(NoteDao nDao){
            asyncDao = nDao;
        }

        @Override
        protected Void doInBackground(final Note... params){
            asyncDao.updateNote(params[0]);
            return null;
        }
    }

}
