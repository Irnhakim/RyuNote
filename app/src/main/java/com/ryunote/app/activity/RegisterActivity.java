package com.ryunote.app.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ryunote.app.R;

public class RegisterActivity extends AppCompatActivity {

    private Button btnDaftar, btnMasuk;
    private EditText txtNamaRegister, txtEmailRegister, txtPasswordRegister, txtKonfirmasiPasswordRegister;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        btnDaftar = findViewById(R.id.btnDaftar);
        btnMasuk = findViewById(R.id.btnMasuk);
        txtNamaRegister = findViewById(R.id.txtNamaRegister);
        txtEmailRegister = findViewById(R.id.txtEmailRegister);
        txtPasswordRegister = findViewById(R.id.txtPasswordRegister);
        txtKonfirmasiPasswordRegister = findViewById(R.id.txtKonfirmasiPasswordRegister);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);

        btnMasuk.setOnClickListener(v -> {
            finish();
        });

        btnDaftar.setOnClickListener(v -> {
            requestNotificationPermission();
        });
    }

    private void requestNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Izin Notifikasi");
            builder.setMessage("Izin notifikasi diperlukan untuk memberikan pemberitahuan penting.");
            builder.setPositiveButton("Izinkan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Buka halaman pengaturan notifikasi
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("Tidak Izinkan", null);
            builder.show();
        } else {
            registerUser();
        }
    }

    private void registerUser() {
        String nama = txtNamaRegister.getText().toString();
        String email = txtEmailRegister.getText().toString();
        String password = txtPasswordRegister.getText().toString();
        String konfirmasiPassword = txtKonfirmasiPasswordRegister.getText().toString();

        if (nama.isEmpty()) {
            txtNamaRegister.setError("Nama tidak boleh kosong");
            txtNamaRegister.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            txtEmailRegister.setError("Email tidak boleh kosong");
            txtEmailRegister.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            txtPasswordRegister.setError("Password tidak boleh kosong");
            txtPasswordRegister.requestFocus();
            return;
        }
        if (konfirmasiPassword.isEmpty()) {
            txtKonfirmasiPasswordRegister.setError("Konfirmasi Password tidak boleh kosong");
            txtKonfirmasiPasswordRegister.requestFocus();
            return;
        }
        if (!password.equals(konfirmasiPassword)) {
            txtKonfirmasiPasswordRegister.setError("Konfirmasi Password tidak sama");
            txtKonfirmasiPasswordRegister.requestFocus();
            return;
        }

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nama)
                                        .build();
                                firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Pendaftaran Berhasil", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Gagal Mengupdate Profil Pengguna", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal Membuat Akun", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal Mendaftarkan Pengguna: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
