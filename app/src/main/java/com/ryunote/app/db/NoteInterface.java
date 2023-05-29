package com.ryunote.app.db;

import android.database.Cursor;

import com.ryunote.app.model.Note;

public interface NoteInterface {

    public Cursor read();
    public boolean create(Note note);
    public boolean update(Note note);
    public boolean delete(String id);
}
