package com.example.android.ui.send;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.databinding.FragmentSendShareNameBinding;
import com.example.android.ui.main.BackdropActivity;

/*
ShareNameFragment : 저장공간 공유 전 기기 별명을 설정하는 DialogFragment
 */
public class ShareNameFragment extends DialogFragment {

    private FragmentSendShareNameBinding binding;
    private SendViewModel mSendViewModel;

    private Button startShareButton;

    public ShareNameFragment() {
        // Required empty public constructor
    }

    public static ShareNameFragment newInstance() {
        ShareNameFragment fragment = new ShareNameFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_send_share_name, container, false);
        View view = binding.getRoot();

        mSendViewModel = new ViewModelProvider((BackdropActivity)getActivity()).get(SendViewModelImpl.class);
        binding.setViewModel(mSendViewModel);

        startShareButton = view.findViewById(R.id.fragment_share_name_ok_button);
        mSendViewModel.getShareNameLiveData().observe((BackdropActivity) getActivity(), this::canStartShareButton);

        return view;
    }

    //별명 입력에 따른 버튼 활성화
    private void canStartShareButton(String s) {
        int length = s.length();
        if (length > 0 && length <= 20) {
            //공유 시작 가능
            startShareButton.setEnabled(true);
            startShareButton.setBackgroundColor(Color.rgb(58, 197, 105));
        } else {
            //공유 시작 불가
            startShareButton.setEnabled(false);
            startShareButton.setBackgroundColor(Color.rgb(218, 219, 219));
        }
    }
}