package com.classyinc.noteit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classyinc.noteit.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }
}