package com.ryunote.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryunote.app.R;
import com.ryunote.app.model.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private ArrayList<Note> notes;

    public NotesAdapter(ArrayList<Note> notes){
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
    Note note = getNote(position);
    if (note != null){
        holder.noteText.setText(note.getNoteText());
        holder.noDate.setText();
    }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private Note getNote (int position){
        return notes.get(position);
    }
    class NoteHolder extends RecyclerView.ViewHolder{
        TextView noteText, noDate;

        public NoteHolder(View itemView){
            super(itemView);
        }
    }
}
