package com.ryunote.app.ui.info;
/*
  Nama : Ihsan Ramadhan Nul Hakim
  NIM  : 10120143
  Kelas: IF-4
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryunote.app.R;

public class InfoFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_info_fragment1, container, false);
        return rootView;
    }
}