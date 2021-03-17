package com.example.android.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.injection.ViewInjection;
import com.example.android.injection.ViewModelInjection;
import com.example.android.main.MainActivity;
import com.example.android.view.LoginView;
import com.example.android.viewmodel.UserViewModel;
import com.example.android.viewmodelimpl.UserViewModelImpl;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class LoginActivity extends AppCompatActivity {
    //로그인 코드
    private static final int SIGN_IN = 1;

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if(viewModelFactory == null){
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        }
        //ViewModel생성
        mUserViewModel = new ViewModelProvider(this,viewModelFactory).get(UserViewModelImpl.class);
        mUserViewModel.setParentContext(this);

        //UserViewModel에 GoogleExecutor, ToastView, LoginView의존성 주입
        injectViewModel(mUserViewModel);
        injectView(mUserViewModel);


//        googleSignInButton.setOnClickListener(v -> {
//            Intent intent = new Intent(mMainView.getContext(), GoogleLoginActivity.class);
//            intent.putExtra("action", "SIGN_IN");
//            startActivityForResult(intent, SIGN_IN);
//        });
//
//        //회원가입
//        signUpTextView.setOnClickListener(v -> {
//            Intent intent = new Intent(mMainView.getContext(),SignupActivity.class);
//            startActivity(intent);
//        });
//
//        //비밀번호 찾기
//        findPasswordTextView.setOnClickListener(v -> {
//            //웹으로 이동
//        });

    }

    //viewModel에 GoogleLoginExecutor의존성 주입
    private void injectViewModel(UserViewModel viewModel) {
        viewModel.setGoogleLoginExecutor(ViewModelInjection.provideGoogleLoginExecutor(this));
    }

    //viewModel에 ToastView, LoginView의존성 주입
    private void injectView(UserViewModel viewModel) {
        viewModel.setToastView(ViewInjection.provideToastView());

        LoginView loginView = ViewInjection.provideLoginView(findViewById(R.id.activity_login_container), this);
        viewModel.setLoginView(loginView);
    }


    //구글 로그인 결과 반환
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "onActivityResult: 여기들어옴!!");
        updateUI(mUserViewModel.onActivityResult(requestCode, resultCode, data));
    }

    //구글 로그인 결과에 따른 화면 전환
    private void updateUI(GoogleSignInAccount account) { //update ui code here
        if (account != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            //다시 돌아오지 않도록 끝내기
            finish();
        }
    }


}