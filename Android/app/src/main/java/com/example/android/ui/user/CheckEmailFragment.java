package com.example.android.ui.user;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.example.android.databinding.FragmentUserCheckEmailBinding;

/*
CheckEmailFragment : 이메일 인증번호를 입력받는 DialogFragment
 */
public class CheckEmailFragment extends DialogFragment {

    private FragmentUserCheckEmailBinding binding;
    private SignUpViewModel mSignUpViewModel;

    private TextView infoTextView;
    private TextView timeTextView;
    private Button okButton;


    public static CheckEmailFragment newInstance(){
        CheckEmailFragment frag = new CheckEmailFragment();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_user_check_email);


        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_check_email, container, false);
        View view = binding.getRoot();

        mSignUpViewModel = new ViewModelProvider((SignupActivity)getActivity()).get(SignUpViewModelImpl.class);
        binding.setViewModel(mSignUpViewModel);

        okButton = (Button)view.findViewById(R.id.fragment_check_email_ok_button);
        infoTextView = (TextView)view.findViewById(R.id.fragment_check_email_info_textView);
        timeTextView = (TextView)view.findViewById(R.id.fragment_check_email_time_textView);

        mSignUpViewModel.getCheckEmailLiveData().observe((SignupActivity)getActivity(),s -> canOK());
        //확인버튼 클릭 가능 여부 처리
        canOK();

        infoTextView.setText(mSignUpViewModel.getEmailLiveData().getValue()+"으로\n인증번호가 발송되었습니다.");

        //남은 시간
        CountDownTimer countDownTimer = new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int)(millisUntilFinished / 60000);
                int seconds =(int)(millisUntilFinished % 60000 / 1000);
                timeTextView.setText(minutes+"분 "+seconds+"초");
            }
            @Override
            public void onFinish() {
                dismiss();
            }
        };
        countDownTimer.start();

        return view;
    }

    //확인 버튼 누를 수 있는지 확인
    private void canOK(){
        String text = mSignUpViewModel.getCheckEmailLiveData().getValue();
        if (text != null  && text.length() > 0) {
            okButton.setEnabled(true);
            okButton.setBackgroundColor(Color.rgb(58,197,105));
        } else {
            okButton.setEnabled(false);
            okButton.setBackgroundColor(Color.rgb(218,219,219));
        }
    }


}