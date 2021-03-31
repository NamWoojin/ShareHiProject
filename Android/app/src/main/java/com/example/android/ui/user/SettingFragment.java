package com.example.android.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.data.viewmodel.SettingViewModel;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.LoginViewModelImpl;
import com.example.android.data.viewmodelimpl.SettingViewModelImpl;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.example.android.databinding.FragmentUserCheckEmailBinding;
import com.example.android.databinding.FragmentUserSettingBinding;
import com.example.android.ui.main.BackdropActivity;

public class SettingFragment extends Fragment {

    private FragmentUserSettingBinding binding;
    private SettingViewModel mSettingViewModel;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_setting, container, false);
        View view = binding.getRoot();

        mSettingViewModel = new ViewModelProvider((BackdropActivity)getActivity()).get(SettingViewModelImpl.class);
        mSettingViewModel.getMemberInformation();
        binding.setViewModel(mSettingViewModel);
        binding.setLifecycleOwner(this);

        return view;
    }
}