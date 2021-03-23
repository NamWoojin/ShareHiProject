package com.example.android.data.viewimpl;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.lifecycle.LifecycleOwner;

import com.example.android.R;
import com.example.android.data.view.LoginView;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;


public class LoginViewImpl implements LoginView {

    private TextInputEditText emailTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private Button loginButton;
    private SignInButton googleSignInButton;
    private TextView signUpTextView;
    private TextView findPasswordTextView;


    private View mMainView;
    private ActionListener mActionListener;
    private LifecycleOwner mLifecycleOwner;

    public LoginViewImpl(View view, LifecycleOwner lifecycleOwner) {
        mMainView = view;
        mLifecycleOwner = lifecycleOwner;

        emailTextInputEditText = mMainView.findViewById(R.id.activity_login_email_text_input_edit_text);
        passwordTextInputEditText = mMainView.findViewById(R.id.activity_login_password_text_input_edit_text);
        loginButton = mMainView.findViewById(R.id.activity_login_button);
        googleSignInButton = mMainView.findViewById(R.id.activity_login_google_button);
        signUpTextView = mMainView.findViewById(R.id.activity_login_signup_text_view);
        findPasswordTextView = mMainView.findViewById(R.id.activity_login_find_password_text_view);

        //로그인 버튼 입력 가능 여부 처리(이메일, 비밀번호 전부 입력해야 버튼 누를 수 있도록)
        emailTextInputEditText.addTextChangedListener(checkInputWatcher);
        passwordTextInputEditText.addTextChangedListener(checkInputWatcher);
        canLogin();

        //로그인 버튼
        loginButton.setOnClickListener(v -> mActionListener.onRequestedSignIn(emailTextInputEditText.getText().toString(),passwordTextInputEditText.getText().toString()));

        //구글 로그인 버튼
        SignInButton googleSignInButton = mMainView.findViewById(R.id.activity_login_google_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(v -> mActionListener.onRequestedGoogleSignIn());
        //구글 로그인 버튼 문구 변경
        TextView textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Google 이메일로 로그인하기");



        //회원가입 이동
        signUpTextView.setOnClickListener(v -> {
            mActionListener.onRenderSignUp();
        });

        //비밀번호 찾기
        findPasswordTextView.setOnClickListener(v -> {
            //웹으로 이동
            mActionListener.onMoveFindPassword();
        });
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

    //ActionListener 초기화
    @Override
    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


}