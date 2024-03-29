package com.ryunote.app.activity;
/*
  Nama : Ihsan Ramadhan Nul Hakim
  NIM  : 10120143
  Kelas: IF-4
 */

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ryunote.app.R;
import com.ryunote.app.services.MyFirebaseMessagingService;
import com.ryunote.app.db.FirebaseHelper;
import com.ryunote.app.model.Note;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    ImageButton button;
    EditText editTitle;
    EditText editCategory;
    EditText editDesc;
    Button addButton;
    Button deleteButton;
    TextView titleAdd;
    private static final String TAG = "Firebase Cloud Messaging";
    private FirebaseHelper noteInterface;

    private static final String CHANNEL_ID = "ryunote_notification_channel";
    Note note = null;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().hide();
        note = (Note) getIntent().getSerializableExtra("Note");
        button = findViewById(R.id.back);
        editTitle = findViewById(R.id.title);
        editCategory = findViewById(R.id.category);
        editDesc = findViewById(R.id.descriptionInput);
        addButton = findViewById(R.id.buttonAdd);
        deleteButton = findViewById(R.id.buttonDelete);
        titleAdd = findViewById(R.id.txt_add);
        noteInterface = new FirebaseHelper();

        button.setOnClickListener(v -> {
            finish();
        });

        if (note == null) {
            deleteButton.setVisibility(View.GONE);

            addButton.setOnClickListener(v -> {
                if (editTitle.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Judul Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editCategory.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Kategori Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editDesc.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Isi Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                Date d = new Date();
                CharSequence date = DateFormat.format("EEEE, d MMM yyyy HH:mm", d.getTime());
                Note n = new Note(
                        d.getTime() + "",
                        editTitle.getText().toString(),
                        editCategory.getText().toString(),
                        editDesc.getText().toString(),
                        "di buat pada " + date + ""
                );

                noteInterface.create(n);
                finish();
                MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
                myFirebaseMessagingService.sendNotification("RyuNote", "Catatan berhasil disimpan");
                Toast.makeText(this, "Catatan berhasil disimpan", Toast.LENGTH_SHORT).show();
            });
        } else {
            editTitle.setText(note.getTitle());
            editCategory.setText(note.getCategory());
            editDesc.setText(note.getDesc());

            deleteButton.setVisibility(View.VISIBLE);
            titleAdd.setText("Edit Catatan");

            addButton.setOnClickListener(v -> {
                if (editTitle.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Judul Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editCategory.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Kategori Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editDesc.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Isi Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }

                Date d = new Date();
                CharSequence date = DateFormat.format("EEEE, d MMMM yyyy HH:mm", d.getTime());

                note.setTitle(editTitle.getText().toString());
                note.setCategory(editCategory.getText().toString());
                note.setDesc(editDesc.getText().toString());
                note.setDate("terakhir di edit " + date + "");
                noteInterface.update(note);
                finish();
                MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
                myFirebaseMessagingService.sendNotification("RyuNote", "Catatan berhasil diedit");
                Toast.makeText(this, "Catatan berhasil diedit", Toast.LENGTH_SHORT).show();
            });
        }

        deleteButton.setOnClickListener(v -> {
            noteInterface.delete(note.getId());
            finish();
            MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
            myFirebaseMessagingService.sendNotification("RyuNote", "Catatan berhasil dihapus");
            Toast.makeText(this, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show();
        });


    }
}