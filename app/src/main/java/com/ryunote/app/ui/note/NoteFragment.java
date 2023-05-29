package com.ryunote.app.ui.note;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ryunote.app.AddNote;
import com.ryunote.app.MainActivity;
import com.ryunote.app.R;
import com.ryunote.app.adapter.NoteAdapter;
import com.ryunote.app.db.NoteImp;
import com.ryunote.app.db.NoteInterface;
import com.ryunote.app.model.Note;

import java.util.ArrayList;
import java.util.List;


public class NoteFragment extends Fragment {
    private RecyclerView note_list;
    private List<Note> noteList;
    private NoteInterface noteInterface;
    private NoteAdapter noteAdapter;
    private Button btnTambah;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);

    }
    //kalau misal bug tampilan list, pindah kesini dari main activity
}