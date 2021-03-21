package com.example.android.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.injection.ViewModelInjection;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout passwordCheckTextInputLayout;

    private TextInputEditText nameTextInputEditText;
    private TextInputEditText emailTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private TextInputEditText passwordCheckTextInputEditText;
    private Button signupButton;
    private Button emailCheckButton;
    private SignInButton googleSignInButton;

    private boolean isNameOK = false, isEmailOK = false, isCheckedEmail = false, isPasswordOK = false, isPasswordCheckOK = false;


    private SignUpViewModel mSignUpViewModel;
    private TextInputEditText viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        //ViewModel생성
        mSignUpViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SignUpViewModelImpl.class);
        mSignUpViewModel.setParentContext(this);

        //UserViewModel에 GoogleExecutor, ToastView, LoginView의존성 주입
        injectViewModel(mSignUpViewModel);

        //TextInputLayout 찾기
        nameTextInputLayout = findViewById(R.id.activity_signup_name_text_input_layout);
        emailTextInputLayout= findViewById(R.id.activity_signup_email_text_input_layout);
        passwordTextInputLayout= findViewById(R.id.activity_signup_password_text_input_layout);
        passwordCheckTextInputLayout =  findViewById(R.id.activity_signup_password_check_text_input_layout);;

        //TextInputEditText 찾기
        nameTextInputEditText = viewById;
        emailTextInputEditText = findViewById(R.id.activity_signup_email_text_input_edit_text);
        passwordTextInputEditText = findViewById(R.id.activity_signup_password_text_input_edit_text);
        passwordCheckTextInputEditText = findViewById(R.id.activity_signup_password_check_text_input_edit_text);
        signupButton = findViewById(R.id.activity_signup_button);
        emailCheckButton = findViewById(R.id.activity_signup_email_check_image_view);

        //TextChangedListenr 지정
        nameTextInputEditText.addTextChangedListener(nameTextWatcher);
        emailTextInputEditText.addTextChangedListener(emailTextWatcher);
        passwordTextInputEditText.addTextChangedListener(passwordTextWatcher);
        passwordCheckTextInputEditText.addTextChangedListener(passwordCheckTextWatcher);
        canCheckEmail();
        canSignup();

        //회원가입 버튼 클릭
        signupButton.setOnClickListener(v -> mSignUpViewModel.onRequestedSignUp(nameTextInputEditText.getText().toString(),emailTextInputEditText.getText().toString(),passwordTextInputEditText.getText().toString()));

        //구글 버튼 text변경, 이벤트 추가
        googleSignInButton = findViewById(R.id.activity_signup_google_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Google 이메일로 가입하기");
        googleSignInButton.setOnClickListener(v -> mSignUpViewModel.onRequestedGoogleSignIn());

        //이메일 인증 버튼 클릭
        emailCheckButton.setOnClickListener(v -> {
            mSignUpViewModel.onRenderCheckEmail(emailTextInputEditText.getText().toString());
        });
    }

    //viewModel에 GoogleLoginExecutor의존성 주입
    private void injectViewModel(SignUpViewModel viewModel) {
        viewModel.setGoogleLoginExecutor(ViewModelInjection.provideGoogleLoginExecutor(this));
    }
    

    //구글 로그인 결과 반환
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSignUpViewModel.onActivityResult(requestCode, resultCode, data);
    }

    //이름 체크
    TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > 20){
                //이름 길이 제한 초과
                nameTextInputLayout.setError(getString(R.string.activity_signup_name_error));
                isNameOK = false;
            }
            else if(s.length() > 0){
                //이름 제한 통과
                nameTextInputLayout.setError(null);
                isNameOK = true;
            }
            else{
                //이름 입력 X
                isNameOK = false;
            }
            canSignup();
        }
    };

    //이메일 형식 체크
    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String mailFormat = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
            Pattern pattern = Pattern.compile(mailFormat);
            Matcher matcher = pattern.matcher(s);
            if (s.length()>0 && matcher.matches()) {
                //이메일 형식 통과
                emailTextInputLayout.setError(null);
                isEmailOK = true;
            }else if(s.length() == 0){
                //이메일 입력 X
                emailTextInputLayout.setError(null);
                isEmailOK = false;
            }else{
                //이메일 형식 불일치
                emailTextInputLayout.setError(getString(R.string.activity_signup_email_error));
                isEmailOK = false;
            }

            if(isCheckedEmail){
                //이메일 변경 시 재인증 필요
                isCheckedEmail = false;
            }

            canCheckEmail();
            canSignup();
        }
    };

    //비밀번호 체크
    TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String passwordFormat = "^(?=.*[$@$!%*#?&])[A-Za-z$@$!%*#?&]{8,45}$";
            Pattern pattern = Pattern.compile(passwordFormat);
            Matcher matcher = pattern.matcher(s);
            if (s.length()>0 && matcher.matches()) {
                //비밀번호 형식 통과
                passwordTextInputLayout.setError(null);
                isPasswordOK = true;
            }else if(s.length() == 0){
                //비밀번호 입력 X
                passwordTextInputLayout.setError(null);
                isPasswordOK = false;
            }else{
                //비밀번호 형식 불일치
                passwordTextInputLayout.setError(getString(R.string.activity_signup_password_error));
                isPasswordOK = false;
            }
            canSignup();
        }
    };

    //비밀번호 확인 일치 체크
    TextWatcher passwordCheckTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length()>0) {
                if(passwordTextInputEditText.getText().toString().equals(s.toString())){
                    //비밀번호 일치
                    passwordCheckTextInputLayout.setError(null);
                    isPasswordCheckOK = true;
                }else{
                    passwordCheckTextInputLayout.setError(getString(R.string.activity_signup_password_check_error));
                    isPasswordCheckOK = false;
                }
            }else{
                passwordCheckTextInputLayout.setError(null);
                isPasswordCheckOK = false;
            }
            canSignup();
        }
    };

    //이메일 인증 가능한지 확인
    private void canCheckEmail(){
        if(isEmailOK){
            //이메일 인증 가능
            emailCheckButton.setClickable(true);
            emailCheckButton.setBackgroundColor(Color.rgb(58,197,105));
        }else{
            //이메일 인증 불가
            emailCheckButton.setClickable(false);
            emailCheckButton.setBackgroundColor(Color.rgb(218,219,219));
        }
    }

    //회원가입 버튼 누를 수 있는지 확인
    private void canSignup() {
        if(isNameOK && isEmailOK && isCheckedEmail && isPasswordOK && isPasswordCheckOK){
            //회원가입 가능
            signupButton.setClickable(true);
            signupButton.setBackgroundColor(Color.rgb(58,197,105));
        }else{
            //회원가입 불가
            signupButton.setClickable(false);
            signupButton.setBackgroundColor(Color.rgb(218,219,219));
        }
    }

}