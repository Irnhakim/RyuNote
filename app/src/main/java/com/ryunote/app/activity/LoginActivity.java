package com.ryunote.app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.ryunote.app.R;
import com.ryunote.app.model.User;

public class LoginActivity extends AppCompatActivity {

    SignInButton btnSignIn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

    btnSignIn = findViewById(R.id.btnSignIn);

    auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    database = FirebaseDatabase.getInstance();

    progressDialog = new ProgressDialog(LoginActivity.this);
    progressDialog.setTitle("Creating account");
    progressDialog.setMessage("We're creating your account");


    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    btnSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            requestNotificationPermission();
        }
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
            // Izin notifikasi sudah diaktifkan, lanjutkan dengan Sign In
            signIn();
        }
    }

    int RC_SIGN_IN = 40;

    private void signIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuth(account.getIdToken());

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }

        }else if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // Setelah pengguna memberikan izin notifikasi, lanjutkan dengan Sign In
            signIn();
        }
    }

    public void firebaseAuth(String idToken) {

        AuthCredential credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            User users = new User();
                            users.setUserId(user.getUid());
                            users.setName(user.getDisplayName());
                            users.setProfile(user.getPhotoUrl().toString());

                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}