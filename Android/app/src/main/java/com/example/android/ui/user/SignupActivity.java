package com.example.android.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.injection.ViewInjection;
import com.example.android.data.injection.ViewModelInjection;
import com.example.android.data.view.LoginView;
import com.example.android.data.view.SignUpView;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.ui.main.MainActivity;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SignupActivity extends AppCompatActivity {

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private SignUpViewModel mSignUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        if(viewModelFactory == null){
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        }
        //ViewModel생성
        mSignUpViewModel = new ViewModelProvider(this,viewModelFactory).get(SignUpViewModelImpl.class);
        mSignUpViewModel.setParentContext(this);

        //UserViewModel에 GoogleExecutor, ToastView, LoginView의존성 주입
        injectViewModel(mSignUpViewModel);
        injectView(mSignUpViewModel);
    }

    //viewModel에 GoogleLoginExecutor의존성 주입
    private void injectViewModel(SignUpViewModel viewModel) {
        viewModel.setGoogleLoginExecutor(ViewModelInjection.provideGoogleLoginExecutor(this));
    }

    //viewModel에 ToastView, LoginView의존성 주입
    private void injectView(SignUpViewModel viewModel) {
        viewModel.setToastView(ViewInjection.provideToastView());

        SignUpView signUpView = ViewInjection.provideSignUpView(findViewById(R.id.activity_signup_container), this);
        viewModel.setSignUpView(signUpView);
    }

    //구글 로그인 결과 반환
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSignUpViewModel.onActivityResult(requestCode, resultCode, data);
    }

}