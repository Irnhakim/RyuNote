package com.ryunote.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ryunote.app.model.Note;

public class NoteImp extends SQLiteOpenHelper implements NoteInterface {

    public NoteImp(Context context) {
        super(context, "db_note", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        sql.execSQL("CREATE TABLE tbl_note (id TEXT, title TEXT, description TEXT, createdtime NUMERIC)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sql, int oldVersion, int newVersion) {
        sql.execSQL(("DROP TABLE tbl_note"));
    }

    @Override
    public Cursor read() {
        SQLiteDatabase sql = getReadableDatabase();
        return sql.rawQuery("SELECT * FROM tbl_note", null);
    }

    @Override
    public boolean create(Note note) {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("INSERT INTO tbl_note VALUES ('"+note.getId()+"','"+note.getTitle()+"','"+note.getDescription()+"','"+note.getCreatedTime()+"')");
        return true;
    }

    @Override
    public boolean update(Note note) {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("UPDATE tbl_note SET tiile='"+note.getTitle()+"', description='"+note.getDescription()+"', createdtime='"+note.getCreatedTime()+"' WHERE id='"+note.getId()+"'");
        return true;
    }

    @Override
    public boolean delete(String id) {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM tbl_note WHERE id='"+id+"'");
        return true;
    }
}
