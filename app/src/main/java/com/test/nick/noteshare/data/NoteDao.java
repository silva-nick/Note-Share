package com.test.nick.noteshare.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface NoteDao {

    @Query("SELECT * FROM notes")
    List<Note> loadAllNotes();

    @Query("SELECT type, title, body FROM notes")


    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);
}
