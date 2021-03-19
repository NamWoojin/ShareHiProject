package com.example.android.ui.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.android.R;
import com.example.android.data.injection.ModelInjection;
import com.example.android.data.injection.ViewInjection;
import com.example.android.data.injection.ViewModelInjection;
import com.example.android.data.view.LoginView;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.data.viewmodelimpl.LoginViewModelImpl;
import com.example.android.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class LoginActivity extends AppCompatActivity {

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if(viewModelFactory == null){
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        }
        //ViewModel생성
        mLoginViewModel = new ViewModelProvider(this,viewModelFactory).get(LoginViewModelImpl.class);
        mLoginViewModel.setParentContext(this);

        //UserViewModel에 GoogleExecutor, ToastView, LoginView의존성 주입
        injectViewModel(mLoginViewModel);
        injectView(mLoginViewModel);

    }

    //viewModel에 GoogleLoginExecutor의존성 주입
    private void injectViewModel(LoginViewModel viewModel) {
        viewModel.setGoogleLoginExecutor(ViewModelInjection.provideGoogleLoginExecutor(this));
    }

    //viewModel에 ToastView, LoginView의존성 주입
    private void injectView(LoginViewModel viewModel) {
        viewModel.setToastView(ViewInjection.provideToastView());

        LoginView loginView = ViewInjection.provideLoginView(findViewById(R.id.activity_login_container), this);
        viewModel.setLoginView(loginView);
    }


    //구글 로그인 결과 반환
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginViewModel.onActivityResult(requestCode, resultCode, data);
    }



}