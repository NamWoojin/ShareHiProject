package com.example.android.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.viewmodel.SettingViewModel;
import com.example.android.data.viewmodelimpl.LoginViewModelImpl;
import com.example.android.data.viewmodelimpl.SettingViewModelImpl;

public class SettingFragment extends Fragment {

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
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
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);

        if(viewModelFactory == null){
//            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(inflater);
        }
        //ViewModel생성
//        mSettingViewModel = new ViewModelProvider(this,viewModelFactory).get(SettingViewModelImpl.class);
//        mSettingViewModel.setParentContext(this);


        return view;
    }


    // [END onActivityResult]

}