package com.example.android.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.injection.ViewModelInjection;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.SignUpViewModelImpl;
import com.example.android.databinding.ActivityUserSignupBinding;
import com.example.android.ui.main.LoadingFragment;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
SignupActivity : 회원가입 Activity
 */
public class SignupActivity extends AppCompatActivity {

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout passwordCheckTextInputLayout;

    private TextView beforeSignUpView;
    private Button signupButton;
    private Button emailCheckButton;
    private SignInButton googleSignInButton;


    private ActivityUserSignupBinding binding;

    private SignUpViewModel mSignUpViewModel;

    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        //ViewModel생성
        mSignUpViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SignUpViewModelImpl.class);
        mSignUpViewModel.setParentContext(this);
        mSignUpViewModel.getAdID();

        //바인딩 객체 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_signup);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mSignUpViewModel);

        //observe 등록
        mSignUpViewModel.getNameLiveData().observe(this, this::checkNameFormat);
        mSignUpViewModel.getEmailLiveData().observe(this, this::checkEmailFormat);
        mSignUpViewModel.getPasswordLiveData().observe(this, this::checkPasswordFormat);
        mSignUpViewModel.getCheckPasswordLiveData().observe(this, this::checkCheckPasswordFormat);
        mSignUpViewModel.getLoadingLiveData().observe(this, this::doLoading);

        //회원가입 가능 여부 확인
        mSignUpViewModel.getIsOKCheckEmail().observe(this, aBoolean -> {
            canCheckEmail();
            canSignup();
        });

        //UserViewModel에 GoogleExecutor, ToastView, LoginView의존성 주입
        injectViewModel(mSignUpViewModel);

        //TextInputLayout 찾기
        nameTextInputLayout = findViewById(R.id.activity_signup_name_text_input_layout);
        emailTextInputLayout = findViewById(R.id.activity_signup_email_text_input_layout);
        passwordTextInputLayout = findViewById(R.id.activity_signup_password_text_input_layout);
        passwordCheckTextInputLayout = findViewById(R.id.activity_signup_password_check_text_input_layout);
        beforeSignUpView = findViewById(R.id.activitiy_signup_textView);

        //Button 찾기
        signupButton = findViewById(R.id.activity_signup_button);
        emailCheckButton = findViewById(R.id.activity_signup_email_check_image_view);

        canCheckEmail();
        canSignup();

        //구글 버튼 text변경, 이벤트 추가
        googleSignInButton = findViewById(R.id.activity_signup_google_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Google 이메일로 가입하기");
        googleSignInButton.setOnClickListener(v -> mSignUpViewModel.onRequestedGoogleSignIn());

        loadingFragment = LoadingFragment.newInstance();
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
    private void checkNameFormat(String s) {
        if (s.length() > 20) {
            //이름 길이 제한 초과
            nameTextInputLayout.setError(getString(R.string.activity_signup_name_error));
            mSignUpViewModel.setIsOKName(new MutableLiveData<>(false));
        } else if (s.length() > 0) {
            //이름 제한 통과
            nameTextInputLayout.setError(null);
            mSignUpViewModel.setIsOKName(new MutableLiveData<>(true));
        } else {
            //이름 입력 X
            mSignUpViewModel.setIsOKName(new MutableLiveData<>(false));
        }
        canSignup();
    }


    //이메일 형식 체크
    private void checkEmailFormat(String s) {
        String mailFormat = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(mailFormat);
        Matcher matcher = pattern.matcher(s);
        if (s.length() > 0 && matcher.matches()) {
            //이메일 형식 통과
            emailTextInputLayout.setError(null);
            mSignUpViewModel.setIsOKEmail(new MutableLiveData<>(true));
        } else if (s.length() == 0) {
            //이메일 입력 X
            emailTextInputLayout.setError(null);
            mSignUpViewModel.setIsOKEmail(new MutableLiveData<>(false));
        } else {
            //이메일 형식 불일치
            emailTextInputLayout.setError(getString(R.string.activity_signup_email_error));
            mSignUpViewModel.setIsOKEmail(new MutableLiveData<>(false));
        }

        if (mSignUpViewModel.getIsOKCheckEmail().getValue()) {
            //이메일 변경 시 재인증 필요
            mSignUpViewModel.setIsOKCheckEmail(new MutableLiveData<>(false));
        }
        canCheckEmail();
        canSignup();
    }

    //비밀번호 체크
    private void checkPasswordFormat(String s) {
        String passwordFormat = "^(?=.*[$@$!%*#?&])[0-9A-Za-z$@$!%*#?&]{8,45}$";
        Pattern pattern = Pattern.compile(passwordFormat);
        Matcher matcher = pattern.matcher(s);
        if (s.length() > 0 && matcher.matches()) {
            //비밀번호 형식 통과
            passwordTextInputLayout.setError(null);
            mSignUpViewModel.setIsOKPassword(new MutableLiveData<>(true));
        } else if (s.length() == 0) {
            //비밀번호 입력 X
            passwordTextInputLayout.setError(null);
            mSignUpViewModel.setIsOKPassword(new MutableLiveData<>(false));
        } else {
            //비밀번호 형식 불일치
            passwordTextInputLayout.setError(getString(R.string.activity_signup_password_error));
            mSignUpViewModel.setIsOKPassword(new MutableLiveData<>(false));
        }
        canSignup();
    }

    //비밀번호 확인 일치 체크
    private void checkCheckPasswordFormat(String s) {
        if (s.length() > 0) {
            if (mSignUpViewModel.getPasswordLiveData().getValue().equals(s)) {
                //비밀번호 일치
                passwordCheckTextInputLayout.setError(null);
                mSignUpViewModel.setIsOKCheckPassword(new MutableLiveData<>(true));
            } else {
                passwordCheckTextInputLayout.setError(getString(R.string.activity_signup_password_check_error));
                mSignUpViewModel.setIsOKCheckPassword(new MutableLiveData<>(false));
            }
        } else {
            passwordCheckTextInputLayout.setError(null);
            mSignUpViewModel.setIsOKCheckPassword(new MutableLiveData<>(false));
        }
        canSignup();
    }

    //이메일 인증 가능한지 확인
    private void canCheckEmail() {
        boolean okCheckEmail = mSignUpViewModel.getIsOKCheckEmail().getValue();
        boolean okEmail = mSignUpViewModel.getIsOKEmail().getValue();
        //이메일 인증을 했는지
        if (okCheckEmail) {
            emailCheckButton.setEnabled(false);
            emailCheckButton.setBackgroundColor(Color.rgb(207, 240, 218));
        }
        //지금 입력된 이메일이 인증을 안했으며 형식에 맞게 입력했는지
        else if (okEmail) {
            //이메일 인증 가능
            emailCheckButton.setEnabled(true);
            emailCheckButton.setBackgroundColor(Color.rgb(58, 197, 105));
        }
        //이메일 형식에 맞게 입력 안함
        else {
            //이메일 인증 불가
            emailCheckButton.setEnabled(false);
            emailCheckButton.setBackgroundColor(Color.rgb(218, 219, 219));
        }
    }

    //회원가입 버튼 누를 수 있는지 확인
    private void canSignup() {
        int result = mSignUpViewModel.canSignUp();

        if (result == 0) {
            //회원가입 가능
            signupButton.setEnabled(true);
            signupButton.setBackgroundColor(Color.rgb(58, 197, 105));
            beforeSignUpView.setText(R.string.activity_signup_signUp_agree);
        } else {
            //회원가입 불가
            signupButton.setEnabled(false);
            signupButton.setBackgroundColor(Color.rgb(218, 219, 219));
            switch (result) {
                case 1:
                    beforeSignUpView.setText(getString(R.string.activity_signup_name_notOK));
                    break;
                case 2:
                    beforeSignUpView.setText(getString(R.string.activity_signup_email_notOK));
                    break;
                case 3:
                    beforeSignUpView.setText(getString(R.string.activity_signup_email_check_notOK));
                    break;
                case 4:
                    beforeSignUpView.setText(getString(R.string.activity_signup_password_notOK));
                    break;
                case 5:
                    beforeSignUpView.setText(getString(R.string.activity_signup_password_check_notOK));
                    break;
            }
        }
    }

    //로딩 표시
    private void doLoading(Boolean b) {
        if (b) {
            if (!loadingFragment.isAdded()) {
                loadingFragment.show(getFragmentManager(), "loading");
            }
        } else {
            loadingFragment.dismiss();
        }
    }

}