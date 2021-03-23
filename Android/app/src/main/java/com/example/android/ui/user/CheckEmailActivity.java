package com.example.android.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.example.android.databinding.ActivityUserCheckEmailBinding;
import com.google.android.material.textfield.TextInputEditText;

public class CheckEmailActivity extends AppCompatActivity {

    private Button okButton;

    private SignUpViewModel mSignUpViewModel;
    private ActivityUserCheckEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_check_email);

        okButton = (Button)findViewById(R.id.activity_check_email_ok_button);

        //ViewModel생성
        mSignUpViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SignUpViewModelImpl.class);

        //바인딩 객체 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_check_email);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mSignUpViewModel);

        //observe 설정
        mSignUpViewModel.getCheckEmailLiveData().observe(this, s -> canOK());
        mSignUpViewModel.getClickOKLiveData().observe(this,s -> returnOK());
        mSignUpViewModel.getClickCancelLiveData().observe(this,s -> returnCANCEL());

        canOK();

        //안내정보
        mSignUpViewModel.setInfoLiveData(new MutableLiveData<>(mSignUpViewModel.getEmailLiveData().getValue() + "으로\n인증번호가 발송되었습니다."));

        //남은 시간
        CountDownTimer countDownTimer = new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int)(millisUntilFinished / 60000);
                int seconds =(int)(millisUntilFinished % 60000 / 1000);
                mSignUpViewModel.setTimeLiveData(new MutableLiveData<>(minutes+"분 "+seconds+"초"));
            }

            @Override
            public void onFinish() {
                returnCANCEL();
            }
        };
        countDownTimer.start();

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

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 클릭
        returnCANCEL();
        return;
    }
    
    private void returnOK(){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);

        //액티비티(팝업) 닫기
        finish();
    }

    private void returnCANCEL(){
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);

        //액티비티(팝업) 닫기
        finish();
    }
}