package com.example.android.ui.send;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.R;
import com.example.android.data.connection.SocketInfo;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.example.android.databinding.FragmentSendShareBinding;
import com.example.android.ui.main.BackdropActivity;
import com.example.android.ui.user.SignupActivity;

public class ShareFragment extends DialogFragment {

    private FragmentSendShareBinding binding;
    private SendViewModel mSendViewModel;

    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_send_share);
        dialog.setCancelable(false);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_send_share, container, false);
        View view = binding.getRoot();

        mSendViewModel = new ViewModelProvider((BackdropActivity)getActivity()).get(SendViewModelImpl.class);
        binding.setViewModel(mSendViewModel);

        
        return view;
    }
}