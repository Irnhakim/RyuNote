package com.ryunote.app.ui.profile;
//Ihsan Ramadhan Nul Hakim 10120143 IF-4
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.ryunote.app.R;
import com.ryunote.app.activity.LoginActivity;


public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();

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
}
