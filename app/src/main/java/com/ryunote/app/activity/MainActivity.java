package com.ryunote.app.activity;
/*
  Nama : Ihsan Ramadhan Nul Hakim
  NIM  : 10120143
  Kelas: IF-4
 */
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.ryunote.app.R;
import com.ryunote.app.databinding.ActivityMainBinding;
import com.ryunote.app.ui.info.InfoFragment;
import com.ryunote.app.ui.note.NoteFragment;
import com.ryunote.app.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getFragmentPage(new InfoFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.info){
                    fragment = new InfoFragment();
                } else if (itemId == R.id.note) {
                    fragment = new NoteFragment();
                } else if (itemId == R.id.profile ) {
                    fragment = new ProfileFragment();
                } else if (itemId == R.id.exit) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Konfirmasi Logout");
                    builder.setMessage("Apakah anda yakin ingin keluar?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ya", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        GoogleSignIn.getClient(
                                MainActivity.this,
                                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        ).signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    });
                    builder.setNegativeButton("Tidak", (dialog, which) -> {
                        dialog.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                return getFragmentPage(fragment);
            }
        });
    }

    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}