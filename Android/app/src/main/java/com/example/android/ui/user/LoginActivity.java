package com.example.android.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.injection.ViewModelInjection;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.data.viewmodelimpl.LoginViewModelImpl;
import com.example.android.databinding.ActivityUserLoginBinding;
import com.example.android.ui.main.BackPressHandler;
import com.example.android.ui.main.LoadingFragment;
import com.google.android.gms.common.SignInButton;

/*
LoginActivity : 로그인 Activity
 */
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private SignInButton googleSignInButton;

    private ActivityUserLoginBinding binding;
    private LoginViewModel mLoginViewModel;
    private LoadingFragment loadingFragment;

    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //ViewModel 요청
        mLoginViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LoginViewModelImpl.class);
        mLoginViewModel.setParentContext(this);
        mLoginViewModel.getAdID();

        //observe 등록
        mLoginViewModel.getEmailLivedata().observe(this, s -> canLogin());
        mLoginViewModel.getPasswordLivedata().observe(this, s -> canLogin());
        mLoginViewModel.getLoadingLiveData().observe(this,this::doLoading);

        //UserViewModel에 GoogleExecutor의존성 주입
        injectViewModel(mLoginViewModel);

        //바인딩 객체 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mLoginViewModel);

        loginButton = findViewById(R.id.activity_login_button);
        googleSignInButton = findViewById(R.id.activity_login_google_button);

        //로그인 버튼 입력 가능 여부 처리(이메일, 비밀번호 전부 입력해야 버튼 누를 수 있도록)
        canLogin();

        //구글 로그인 버튼
        googleSignInButton = findViewById(R.id.activity_login_google_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(v -> mLoginViewModel.onRequestedGoogleSignIn());
        //구글 로그인 버튼 문구 변경
        TextView textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Google 이메일로 로그인하기");

        loadingFragment = LoadingFragment.newInstance();
    }


    //viewModel에 GoogleLoginExecutor의존성 주입
    private void injectViewModel(LoginViewModel viewModel) {
        viewModel.setGoogleLoginExecutor(ViewModelInjection.provideGoogleLoginExecutor(this));
    }


    //로그인 버튼 누를 수 있는지 확인
    private void canLogin() {
        String email = mLoginViewModel.getEmailLivedata().getValue();
        String password = mLoginViewModel.getPasswordLivedata().getValue();
        if (email != null && email.length() > 0 && password != null &&  password.length() > 0) {
            loginButton.setEnabled(true);
            loginButton.setBackgroundColor(Color.rgb(58, 197, 105));
        } else {
            loginButton.setEnabled(false);
            loginButton.setBackgroundColor(Color.rgb(218, 219, 219));
        }
    }

    //구글 로그인 결과 반환
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginViewModel.onActivityResult(requestCode, resultCode, data);
    }
    //로딩 표시
    private void doLoading(Boolean b){
        if(b){
            if(!loadingFragment.isAdded()) {
                loadingFragment.show(getFragmentManager(), "loading");
            }
        }else{
            loadingFragment.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}