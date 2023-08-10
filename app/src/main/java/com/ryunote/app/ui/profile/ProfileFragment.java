package com.ryunote.app.ui.profile;
//Ihsan Ramadhan Nul Hakim 10120143 IF-4
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ryunote.app.R;
import com.ryunote.app.activity.LoginActivity;


public class ProfileFragment extends Fragment {
    private static final String TAG = "Firebase Cloud Messaging";
    private Button btnLogout;
    private Button btnNotif;
    private FirebaseAuth auth;
    private ImageButton fb;
    private ImageButton ig;
    private ImageButton github;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();

        btnNotif = view.findViewById(R.id.btnNotif);
        fb = view.findViewById(R.id.fb);
        ig = view.findViewById(R.id.ig);
        github = view.findViewById(R.id.github);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSocialMedia("https://www.facebook.com/irnh4kim");
            }
        });

        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSocialMedia("https://www.instagram.com/irnhakim/");
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSocialMedia("https://github.com/Irnhakim");
            }
        });



        btnNotif.setOnClickListener(View  -> {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, "Token: " + token);
                }
            });
        });


        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        return view;
    }

    private void logout() {
        // End the Firebase session
        auth.signOut();

        GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Redirect ke halaman login
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), "Berhasil Logout", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Gagal Logout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openSocialMedia(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
