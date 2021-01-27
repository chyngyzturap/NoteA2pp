package com.pharos.notea2pp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pharos.notea2pp.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Insert
    void insert (Note note);


    @Delete
    void delete (Note note);

    @Update
    void update (Note note);

    @Query("SELECT * FROM 'note' ORDER BY title ASC")
    List<Note> sortAll();

    @Query("SELECT * FROM 'note' ORDER BY date ASC")
    List<Note> sortTime();
}
