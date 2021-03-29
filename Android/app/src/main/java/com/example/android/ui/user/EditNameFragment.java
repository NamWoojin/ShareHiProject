package com.example.android.ui.user;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.viewmodel.SettingViewModel;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.SettingViewModelImpl;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.example.android.databinding.FragmentUserCheckEmailBinding;
import com.example.android.databinding.FragmentUserEditNameBinding;
import com.example.android.ui.main.BackdropActivity;

public class EditNameFragment extends DialogFragment {

//    private FragmentUserEditNameBinding binding;
//    private SettingViewModel mSettingViewModel;

    public EditNameFragment() {
        // Required empty public constructor
    }

    public static EditNameFragment newInstance() {
        EditNameFragment fragment = new EditNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_user_edit_name);


        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_edit_name, container, false);
//        View view = binding.getRoot();
//
//        mSettingViewModel = new ViewModelProvider((BackdropActivity)getActivity()).get(SettingViewModelImpl.class);
//        binding.setViewModel(mSettingViewModel);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_edit_name, container, false);
    }
}