package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.connection.APIRequest;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.dto.APIResponse;
import com.example.android.data.model.dto.LoginContent;
import com.example.android.data.model.dto.Member;
import com.example.android.data.model.dto.MemberRequest;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.ui.main.MainActivity;
import com.example.android.ui.user.SignupActivity;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

public class LoginViewModelImpl extends ViewModel implements LoginViewModel {

    private static final String TAG = "LoginViewModelImpl";
    private static final int REQ_CODE_SIGN_IN = 1;
    private static final int REQ_CODE_GOOGLE_SIGN_IN = 2;

    private WeakReference<Activity> mActivityRef;

    private String advertId = null;

    //LiveData
    private MutableLiveData<String> emailLiveData = new MutableLiveData<>();
    private MutableLiveData<String> passwordLiveData = new MutableLiveData<>();


    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    //GoogleLoginExecutor
    private GoogleLoginExecutor mGoogleLoginExecutor;


    //activity지정
    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void getAdID(){
        getadid.start();
    }

    private Thread getadid = new Thread(){
        @Override
        public void run() {
            super.run();
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(mActivityRef.get().getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                advertId = idInfo.getId();
                Log.i("TAG", "run: "+advertId);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    };


    //GoogleLoginExecutor지정
    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }

    //로그인 시도
    @Override
    public void onRequestedSignIn() {
        loadingLiveData.setValue(true);
        MemberRequest member  =new MemberRequest();
        member.setMem_email(emailLiveData.getValue());
        member.setMem_password(passwordLiveData.getValue());
        member.setAd_id(advertId);
        APIRequest.request(RetrofitClient.getLoginApiService().Login(member), objectResponse -> {
            loadingLiveData.setValue(false);
            Gson gson = new Gson();
            int code = objectResponse.code();
            String body = gson.toJson(objectResponse.body());

            if (code >= 500) {
                //서버 에러
                Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();
            } else if (code >= 400) {
                //클라이언트 에러
            } else if (code >= 300) {

            } else if (code >= 200) {
                //성공
                Type listType = new TypeToken<APIResponse<LoginContent>>() {
                }.getType();
                APIResponse<LoginContent> res = gson.fromJson(body, listType);
                switch (res.getMessage()) {
                    case "SUCCESS":
                        //로그인 성공
                        Toast.makeText(mActivityRef.get(), R.string.toast_login_success_message, Toast.LENGTH_SHORT).show();
                        String token = res.getContent().getToken();
                        Member mem = res.getContent().getMember();
                        Log.i(TAG, "onRequestedSignIn: " + res.getContent().getToken());
                        saveMemberInfo(mem);
                        saveLoginToken(token);
                        saveAutoLoginInfo();
                        updateUI();
                        break;
                    case "FAIL":
                        //아이디 또는 이메일 틀림
                        Toast.makeText(mActivityRef.get(), R.string.toast_login_fail_message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, throwable -> {
            loadingLiveData.setValue(false);
            Log.e(TAG, "onRequestedSignIn: " + throwable);
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

    //사용자 정보 저장
    private void saveMemberInfo(Member member) {
        SharedPreferences tokenInformation = mActivityRef.get().getSharedPreferences("member", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenInformation.edit();
        editor.putInt("mem_id", member.getMem_id());
        editor.putString("dev_id",member.getDev_id());
        editor.apply();
    }

    //JWT 토큰 저장
    private void saveLoginToken(String value) {
        SharedPreferences tokenInformation = mActivityRef.get().getSharedPreferences("token", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenInformation.edit();
        editor.putString("token", value);
        editor.apply();
    }

    //일반로그인 정보 저장
    private void saveAutoLoginInfo() {
        SharedPreferences loginInformation = mActivityRef.get().getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginInformation.edit();
        editor.putString("login","basic");
        editor.putString("email", emailLiveData.getValue());
        editor.putString("password", passwordLiveData.getValue());
        editor.apply();
    }

    //구글 로그인 요청
    @Override
    public void onRequestedGoogleSignIn() {
        Intent signInIntent = mGoogleLoginExecutor.getSignInIntent();
        if (mActivityRef.get() != null) {
            mActivityRef.get().startActivityForResult(signInIntent, REQ_CODE_GOOGLE_SIGN_IN);
        }
    }

    //구글 로그인 요청에 따른 반환(mActivityRef에서 여기로 전달)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    String name = account.getDisplayName();
                    String email = account.getEmail();
                    Uri imageUri = account.getPhotoUrl();
                    String image = imageUri != null ? imageUri.toString() : null;
                    googleLogin(name,email,image);
                } else {
                    //로그인 실패
                    Toast.makeText(mActivityRef.get(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                //로그인 취소
//                Toast.makeText(mActivityRef.get(), "로그인을 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //구글 로그인 정보로 사용자 정보 조회
    private void googleLogin(String name, String email, String image){
        MemberRequest mem = new MemberRequest();
        mem.setMem_name(name);
        mem.setMem_email(email);
        mem.setMem_image(image);
        mem.setAd_id(advertId);
        Log.i(TAG, "googleLogin: "+mem);
        APIRequest.request(RetrofitClient.getLoginApiService().SocialLogin(mem), objectResponse -> {
            Gson gson = new Gson();
            int code = objectResponse.code();
            String body = gson.toJson(objectResponse.body());
            if (code >= 500) {
                //서버 에러
                Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();
            } else if (code >= 400) {
                //클라이언트 에러
            } else if (code >= 300) {

            } else if (code >= 200) {
                //성공
                Type listType = new TypeToken<APIResponse<LoginContent>>() {
                }.getType();
                APIResponse<LoginContent> res = gson.fromJson(body, listType);
                switch (res.getMessage()) {
                    case "SUCCESS":
                        //로그인 성공
                        String token = res.getContent().getToken();
                        Member member = res.getContent().getMember();
                        Log.i(TAG, "onRequestedSignIn: " + res.getContent().getMember().toString());
                        saveMemberInfo(member);
                        saveLoginToken(token);
                        saveGoogleLoginInfo();
                        updateUI();
                        break;
                    case "FAIL":
                        //아이디 또는 이메일 틀림
                        Toast.makeText(mActivityRef.get(), R.string.toast_login_fail_message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, throwable -> {
            Log.e(TAG, "onRequestedSignIn: " + throwable);
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

    //구글 로그인 정보 SharedPreference에 저장
    private void saveGoogleLoginInfo() {
        SharedPreferences loginInformation = mActivityRef.get().getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginInformation.edit();
        editor.putString("login", "google");
        editor.apply();
    }


    //화면 전환
    private void updateUI() { //update ui code here
        Intent intent = new Intent(mActivityRef.get(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivityRef.get().startActivity(intent);
        mActivityRef.get().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //다시 돌아오지 않도록 끝내기
        mActivityRef.get().finish();
    }


    //회원가입으로 이동
    @Override
    public void onRenderSignUp() {
        Intent intent = new Intent(mActivityRef.get(), SignupActivity.class);
        mActivityRef.get().startActivity(intent);
    }

    //비밀번호 찾기로 이동
    @Override
    public void onMoveFindPassword() {

    }

    //LiveData getter setter
    @Override
    public MutableLiveData<String> getEmailLivedata() {
        return emailLiveData;
    }

    @Override
    public void setEmailLivedata(MutableLiveData<String> emailLivedata) {
        this.emailLiveData = emailLivedata;
    }

    @Override
    public MutableLiveData<String> getPasswordLivedata() {
        return passwordLiveData;
    }

    @Override
    public void setPasswordLivedata(MutableLiveData<String> passwordLivedata) {
        this.passwordLiveData = passwordLivedata;
    }

    @Override
    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    @Override
    public void setLoadingLiveData(MutableLiveData<Boolean> loadingLiveData) {
        this.loadingLiveData = loadingLiveData;
    }
}
