package com.example.android.ui.send;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.databinding.FragmentSendFolderBinding;
import com.example.android.ui.main.BackdropActivity;

/*
FolderFragment : 폴더 목록을 보여주는 Fragment
 */
public class FolderFragment extends Fragment {

    private FragmentSendFolderBinding binding;
    private SendViewModel mSendViewModel;


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