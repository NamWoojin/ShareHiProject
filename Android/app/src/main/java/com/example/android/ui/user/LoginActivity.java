package com.example.android.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.injection.ViewModelInjection;
import com.example.android.data.model.entity.User;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.data.viewmodelimpl.LoginViewModelImpl;
import com.example.android.databinding.ActivityUserLoginBinding;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private Button loginButton;
    private SignInButton googleSignInButton;
    private TextView signUpTextView;
    private TextView findPasswordTextView;

    private ActivityUserLoginBinding binding;
    private LoginViewModel mLoginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //ViewModel 요청
        mLoginViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LoginViewModelImpl.class);
        mLoginViewModel.setParentContext(this);

        //UserViewModel에 GoogleExecutor의존성 주입
        injectViewModel(mLoginViewModel);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_login);
        binding.setUser(mLoginViewModel.getuserLiveData().getValue());

        emailTextInputEditText = findViewById(R.id.activity_login_email_text_input_edit_text);
        passwordTextInputEditText = findViewById(R.id.activity_login_password_text_input_edit_text);
        loginButton = findViewById(R.id.activity_login_button);
        googleSignInButton = findViewById(R.id.activity_login_google_button);
        signUpTextView = findViewById(R.id.activity_login_signup_text_view);
        findPasswordTextView = findViewById(R.id.activity_login_find_password_text_view);

        //로그인 버튼 입력 가능 여부 처리(이메일, 비밀번호 전부 입력해야 버튼 누를 수 있도록)
        emailTextInputEditText.addTextChangedListener(checkInputWatcher);
        passwordTextInputEditText.addTextChangedListener(checkInputWatcher);
        canLogin();

        //로그인 버튼
        loginButton.setOnClickListener(v ->{
            Log.i("TAG", "onCreate: "+binding.getUser());
            mLoginViewModel.onRequestedSignIn(binding.getUser());
        });

        //구글 로그인 버튼
        googleSignInButton = findViewById(R.id.activity_login_google_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(v -> mLoginViewModel.onRequestedGoogleSignIn());
        //구글 로그인 버튼 문구 변경
        TextView textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Google 이메일로 로그인하기");

        //회원가입 이동
        signUpTextView.setOnClickListener(v -> {
            mLoginViewModel.onRenderSignUp();
        });

        //비밀번호 찾기
        findPasswordTextView.setOnClickListener(v -> {
            //웹으로 이동
            mLoginViewModel.onMoveFindPassword();
        });


    }

    private void setUserData(){
        mLoginViewModel.getuserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

            }
        });
    }

    //viewModel에 GoogleLoginExecutor의존성 주입
    private void injectViewModel(LoginViewModel viewModel) {
        viewModel.setGoogleLoginExecutor(ViewModelInjection.provideGoogleLoginExecutor(this));
    }

    //EditText 변화에 따른 TextWatcher
    TextWatcher checkInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            canLogin();
        }
    };

    //로그인 버튼 누를 수 있는지 확인
    private void canLogin() {
        String email = emailTextInputEditText.getText().toString();
        String password = passwordTextInputEditText.getText().toString();
        if (email.length() > 0 && password.length() > 0) {
            loginButton.setClickable(true);
            loginButton.setBackgroundColor(Color.rgb(58, 197, 105));
        } else {
            loginButton.setClickable(false);
            loginButton.setBackgroundColor(Color.rgb(218, 219, 219));
        }
    }



    //구글 로그인 결과 반환
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginViewModel.onActivityResult(requestCode, resultCode, data);
    }


}