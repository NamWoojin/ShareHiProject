package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.connection.APIRequest;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.dto.Event;
import com.example.android.data.model.dto.User;
import com.example.android.ui.main.MainActivity;
import com.example.android.ui.user.CheckEmailActivity;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SignUpViewModelImpl extends ViewModel implements SignUpViewModel {

    private static final String TAG = "SignUpViewModelImpl";
    private static final int REQ_CODE_SIGN_IN = 1;
    private static final int REQ_CODE_CHECK_EMAIL = 2;
    private WeakReference<Activity> mActivityRef;

    //회원가입
    private MutableLiveData<String> nameLiveData = new MutableLiveData<>();
    private MutableLiveData<String> emailLiveData = new MutableLiveData<>();
    private MutableLiveData<String> passwordLiveData = new MutableLiveData<>();
    private MutableLiveData<String> checkPasswordLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isOKName = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKEmail = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKPassword = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKCheckPassword = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKCheckEmail = new MutableLiveData<>(false);

    //이메일 인증
    private MutableLiveData<String> infoLiveData = new MutableLiveData<>();
    private MutableLiveData<String> timeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> checkEmailLiveData = new MutableLiveData<>();

    private MutableLiveData<Event<Boolean>> clickOKLiveData = new MutableLiveData<>(new Event<>(false));
    private MutableLiveData<Event<Boolean>> clickCancelLiveData = new MutableLiveData<>(new Event<>(false));
    private MutableLiveData<Event<Integer>> goCheckEmailLiveData = new MutableLiveData<>(new Event<>(0));
    private MutableLiveData<Event<Boolean>> loginSuccessLiveData = new MutableLiveData<>(new Event<>(false));

    //Executor
    private GoogleLoginExecutor mGoogleLoginExecutor;


    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }

    //회원가입 조건 확인
    @Override
    public int canSignUp() {
        int result = 0;
        if (!isOKName.getValue()) {
            result = 1;
        } else if (!isOKEmail.getValue()) {
            result = 2;
        } else if (!isOKCheckEmail.getValue()) {
            result = 3;
        } else if (!isOKPassword.getValue()) {
            result = 4;
        } else if (!isOKCheckPassword.getValue()) {
            result = 5;
        }

        return result;
    }


    //로그인 시도
    @Override
    public void onRequestedSignIn() {
        Call<Object> doLogin = RetrofitClient.getLoginApiService().Login(new User(emailLiveData.getValue(), passwordLiveData.getValue()));
        doLogin.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Toast.makeText(mActivityRef.get(), "환영합니다!", Toast.LENGTH_SHORT).show();
                updateUI();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "onRequestedSignIn: " + t);
                Toast.makeText(mActivityRef.get(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //회원가입 요청
    @Override
    public void onRequestedSignUp() {

        User user = new User(nameLiveData.getValue(), emailLiveData.getValue(), passwordLiveData.getValue());
        Call<Object> doLogin = RetrofitClient.getUserApiService().SignUp(user);
        doLogin.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
//                response.code() : 200
//                response.message() : OK
//                response.body() : 내용
                Log.i(TAG, "onResponse: " + call.request());
                int code = response.code();
                String message = response.message();
                String body = new Gson().toJson(response.body());
                Log.i(TAG, "onResponse: " + code + " " + message + " " + body);

                if (code >= 500) {
                    //서버 오류
                    Toast.makeText(mActivityRef.get(), R.string.toast_signup_success_message, Toast.LENGTH_SHORT).show();
                } else if (code >= 400) {
                    //요청오류
                    Toast.makeText(mActivityRef.get(), R.string.toast_signup_success_message, Toast.LENGTH_SHORT).show();
                } else if (code >= 300) {

                } else if (code >= 200) {
                    //성공
                    Toast.makeText(mActivityRef.get(), R.string.toast_signup_success_message, Toast.LENGTH_SHORT).show();
                    //로그인 시도
                    onRequestedSignIn();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "onRequestedSignIn: " + t);
                Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //구글로그인 요청
    @Override
    public void onRequestedGoogleSignIn() {
        Intent signInIntent = mGoogleLoginExecutor.getSignInIntent();
        if (mActivityRef.get() != null) {
            mActivityRef.get().startActivityForResult(signInIntent, REQ_CODE_SIGN_IN);
        }
    }

    //이메일 인증 Activity 열기
    @Override
    public void onRenderCheckEmail() {
        Intent intent = new Intent(mActivityRef.get(), CheckEmailActivity.class);
        mActivityRef.get().startActivityForResult(intent, REQ_CODE_CHECK_EMAIL);
        APIRequest.request(RetrofitClient.getUserApiService().requireEmailAuth(new User(emailLiveData.getValue())),
                (objectResponse -> {
                    int code = objectResponse.code();
                    Log.i(TAG, "onRenderCheckEmail: " + code);
                }), throwable -> {
                    Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
                });
    }

    //이메일 인증 확인
    @Override
    public void checkEmailAuth() {
        if (!clickOKLiveData.getValue().isHandled()) {
            //이메일 인증 확인
        }
        clickOKLiveData.setValue(new Event<>(true));
    }

    //이메일 인증 창 닫기
    @Override
    public void closeEmailAuth() {

        clickCancelLiveData.setValue(new Event<>(true));
    }


    //Intent 요청에 따른 반환(mActivityRef에서 여기로 전달)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SIGN_IN) {
            //구글 로그인
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    updateUI();
                } else {
                    //로그인 실패
                    Toast.makeText(mActivityRef.get(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                //로그인 실패
                Toast.makeText(mActivityRef.get(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQ_CODE_CHECK_EMAIL) {
            //이메일 인증
            if (resultCode == RESULT_OK) {
                //이메일 인증 성공
                isOKCheckEmail.setValue(true);
            } else {
                //이메일 인증 실패
                isOKCheckEmail.setValue(false);
            }
        }
    }


    //화면 전환
    private void updateUI() { //update ui code here
        Intent intent = new Intent(mActivityRef.get(), MainActivity.class);
        mActivityRef.get().startActivity(intent);
        mActivityRef.get().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //다시 돌아오지 않도록 끝내기
        mActivityRef.get().finish();
    }


    //LiveData Getter & Setter
    @Override
    public MutableLiveData<String> getNameLiveData() {
        return nameLiveData;
    }

    @Override
    public void setNameLiveData(MutableLiveData<String> nameLiveData) {
        this.nameLiveData = nameLiveData;
    }

    @Override
    public MutableLiveData<String> getEmailLiveData() {
        return emailLiveData;
    }

    @Override
    public void setEmailLiveData(MutableLiveData<String> emailLiveData) {
        this.emailLiveData = emailLiveData;
    }

    @Override
    public MutableLiveData<String> getPasswordLiveData() {
        return passwordLiveData;
    }

    @Override
    public void setPasswordLiveData(MutableLiveData<String> passwordLiveData) {
        this.passwordLiveData = passwordLiveData;
    }

    @Override
    public MutableLiveData<String> getCheckPasswordLiveData() {
        return checkPasswordLiveData;
    }

    @Override
    public void setCheckPasswordLiveData(MutableLiveData<String> checkPasswordLiveData) {
        this.checkPasswordLiveData = checkPasswordLiveData;
    }

    @Override
    public MutableLiveData<Boolean> getIsOKName() {
        return isOKName;
    }

    @Override
    public void setIsOKName(MutableLiveData<Boolean> isOKName) {
        this.isOKName = isOKName;
    }

    @Override
    public MutableLiveData<Boolean> getIsOKEmail() {
        return isOKEmail;
    }

    @Override
    public void setIsOKEmail(MutableLiveData<Boolean> isOKEmail) {
        this.isOKEmail = isOKEmail;
    }

    @Override
    public MutableLiveData<Boolean> getIsOKPassword() {
        return isOKPassword;
    }

    @Override
    public void setIsOKPassword(MutableLiveData<Boolean> isOKPassword) {
        this.isOKPassword = isOKPassword;
    }

    @Override
    public MutableLiveData<Boolean> getIsOKCheckPassword() {
        return isOKCheckPassword;
    }

    @Override
    public void setIsOKCheckPassword(MutableLiveData<Boolean> isOKCheckPassword) {
        this.isOKCheckPassword = isOKCheckPassword;
    }

    @Override
    public MutableLiveData<Boolean> getIsOKCheckEmail() {
        return isOKCheckEmail;
    }

    @Override
    public void setIsOKCheckEmail(MutableLiveData<Boolean> isOKCheckEmail) {
        this.isOKCheckEmail = isOKCheckEmail;
    }

    @Override
    public MutableLiveData<String> getCheckEmailLiveData() {
        return checkEmailLiveData;
    }

    @Override
    public void setCheckEmailLiveData(MutableLiveData<String> checkEmailLiveData) {
        this.checkEmailLiveData = checkEmailLiveData;
    }

    @Override
    public MutableLiveData<String> getInfoLiveData() {
        return infoLiveData;
    }

    @Override
    public void setInfoLiveData(MutableLiveData<String> infoLiveData) {
        this.infoLiveData = infoLiveData;
    }

    @Override
    public MutableLiveData<String> getTimeLiveData() {
        return timeLiveData;
    }

    @Override
    public void setTimeLiveData(MutableLiveData<String> timeLiveData) {
        this.timeLiveData = timeLiveData;
    }

    @Override
    public MutableLiveData<Event<Integer>> getGoCheckEmailLiveData() {
        return goCheckEmailLiveData;
    }

    @Override
    public void setGoCheckEmailLiveData(MutableLiveData<Event<Integer>> goCheckEmailLiveData) {
        this.goCheckEmailLiveData = goCheckEmailLiveData;
    }

    @Override
    public MutableLiveData<Event<Boolean>> getLoginSuccessLiveData() {
        return loginSuccessLiveData;
    }

    @Override
    public void setLoginSuccessLiveData(MutableLiveData<Event<Boolean>> loginSuccessLiveData) {
        this.loginSuccessLiveData = loginSuccessLiveData;
    }

    @Override
    public MutableLiveData<Event<Boolean>> getClickOKLiveData() {
        return clickOKLiveData;
    }

    @Override
    public void setClickOKLiveData(MutableLiveData<Event<Boolean>> clickOKLiveData) {
        this.clickOKLiveData = clickOKLiveData;
    }

    @Override
    public MutableLiveData<Event<Boolean>> getClickCancelLiveData() {
        return clickCancelLiveData;
    }

    @Override
    public void setClickCancelLiveData(MutableLiveData<Event<Boolean>> clickCancelLiveData) {
        this.clickCancelLiveData = clickCancelLiveData;
    }
}
