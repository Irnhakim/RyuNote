package com.ryunote.app.activity;
//Ihsan Ramadhan Nul Hakim 10120143 IF-4

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ryunote.app.NoteInterface;
import com.ryunote.app.R;
import com.ryunote.app.db.DatabaseHelper;
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
                showNotification("Catatan berhasil di tambah");
                Toast.makeText(this, "Catatan berhasil di tambah", Toast.LENGTH_SHORT).show();
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
                showNotification("Catatan berhasil di edit");
                Toast.makeText(this, "Catatan berhasil di edit", Toast.LENGTH_SHORT).show();
            });
        }

        deleteButton.setOnClickListener(v -> {
            noteInterface.delete(note.getId());
            finish();
            showNotification("Catatan berhasil di hapus");
            Toast.makeText(this, "Catatan berhasil di hapus", Toast.LENGTH_SHORT).show();
        });


    }

    private void showNotification(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("RyuNote")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }
        notificationManager.notify(0, builder.build());
    }
}