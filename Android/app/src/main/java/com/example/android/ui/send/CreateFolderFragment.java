package com.example.android.ui.send;

import android.app.DialogFragment;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.databinding.FragmentSendCreateFolderBinding;
import com.example.android.ui.main.BackdropActivity;

/*
CreateFolderFragment : 폴더 목록에서 새 폴더 생성을 위한 DialogFragment
 */
public class CreateFolderFragment extends DialogFragment {

    private FragmentSendCreateFolderBinding binding;
    private SendViewModel mSendViewModel;

    public CreateFolderFragment() {
        // Required empty public constructor
    }

    public static CreateFolderFragment newInstance() {
        CreateFolderFragment fragment = new CreateFolderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_create_folder, container, false);
        View view = binding.getRoot();

        mSendViewModel = new ViewModelProvider((BackdropActivity) getActivity()).get(SendViewModelImpl.class);
        binding.setViewModel(mSendViewModel);

        return view;
    }
}