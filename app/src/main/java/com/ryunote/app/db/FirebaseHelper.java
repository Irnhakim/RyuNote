package com.ryunote.app.db;

import android.database.Cursor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ryunote.app.model.Note;
/*
  Nama : Ihsan Ramadhan Nul Hakim
  NIM  : 10120143
  Kelas: IF-4
 */
public class FirebaseHelper implements NoteInterface {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(auth.getCurrentUser().getUid());
    }

    @Override
    public Cursor read(){
        return null;
    }

    @Override
    public boolean create(Note note) {
        if (note.getId() != null) {
            databaseReference.child(note.getId()).setValue(note);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Note note) {
        if (note.getId() != null) {
            databaseReference.child(note.getId()).setValue(note);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        if (id != null) {
            databaseReference.child(id).removeValue();
            return true;
        }
        return false;
    }
}
