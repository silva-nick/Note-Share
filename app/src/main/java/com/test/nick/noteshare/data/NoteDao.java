package com.test.nick.noteshare.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes")
    List<Note> loadAllNotes();

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);
}