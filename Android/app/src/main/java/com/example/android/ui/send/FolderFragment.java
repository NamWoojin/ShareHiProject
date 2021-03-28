package com.example.android.ui.send;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.databinding.FragmentSendFolderBinding;
import com.example.android.ui.main.BackdropActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {

    private FragmentSendFolderBinding binding;
    private SendViewModel mSendViewModel;

    private String mFileName;
    private RecyclerView mFolderRecyclerView;

    private List<String> lItem = null;
    private List<String> lPath = null;


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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_folder, container, false);
        View view = binding.getRoot();

        mSendViewModel = new ViewModelProvider((BackdropActivity) getActivity()).get(SendViewModelImpl.class);
        binding.setViewModel(mSendViewModel);
        binding.setLifecycleOwner(this);

        return view;
    }

}