package com.example.application.send;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.application.R;

public class PrepareFragment extends Fragment {



    public PrepareFragment() {
        // Required empty public constructor
    }


    public static PrepareFragment newInstance() {
        PrepareFragment fragment = new PrepareFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_prepare, container, false);
        Button goFolderButton = (Button) view.findViewById(R.id.fragment_prepare_go_folder_Button);

        goFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SendActivity)getActivity()).replaceFragment(FolderFragment.newInstance());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}