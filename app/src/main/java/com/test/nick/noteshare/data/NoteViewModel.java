package com.test.nick.noteshare.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private List<Note> allNotes;

    public NoteViewModel(Application application){
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public List<Note> getAllNotes(){
        return allNotes;
    }

    public void insert(Note note){
        repository.insert(note);
    }
}
