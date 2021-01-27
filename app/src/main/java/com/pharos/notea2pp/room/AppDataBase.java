package com.pharos.notea2pp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pharos.notea2pp.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public  abstract NoteDao noteDao();
}