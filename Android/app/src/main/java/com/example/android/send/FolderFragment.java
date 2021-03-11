package com.example.android.send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.android.R;
import com.example.android.main.BackgroundActivity;

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
            ((BackgroundActivity)getActivity()).replaceFragment(PrepareFragment.newInstance(),false);
        });

        return view;
    }
}