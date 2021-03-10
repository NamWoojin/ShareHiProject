package com.example.application.send;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.application.R;

public class FolderFragment extends Fragment {


    public FolderFragment() {
        // Required empty public constructor
    }


    public static FolderFragment newInstance() {
        FolderFragment fragment = new FolderFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_folder, container, false);
        Button folderSelectButton = (Button) view.findViewById(R.id.fragment_folder_select_button);

        folderSelectButton.setOnClickListener(v -> {
            ((SendActivity)getActivity()).replaceFragment(PrepareFragment.newInstance(),false);
        });

        return view;
    }
}