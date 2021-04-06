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
import com.example.android.data.model.dto.EmailAuth;
import com.example.android.data.model.dto.LoginContent;
import com.example.android.data.model.dto.Member;
import com.example.android.data.model.dto.MemberRequest;
import com.example.android.ui.main.MainActivity;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.ui.user.CheckEmailFragment;
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

import static android.app.Activity.RESULT_OK;

/*
SignUpViewModelImpl : 회원가입과 관련된 데이터를 관리하는 ViewModel
 */
public class SignUpViewModelImpl extends ViewModel implements SignUpViewModel {

    private static final String TAG = "SignUpViewModelImpl";
    private static final int REQ_CODE_SIGN_IN = 1;
    private static final int REQ_CODE_CHECK_EMAIL = 2;
    private WeakReference<Activity> mActivityRef;

    //회원가입
    private MutableLiveData<String> nameLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> emailLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> passwordLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> checkPasswordLiveData = new MutableLiveData<>("");

    private MutableLiveData<Boolean> isOKName = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKEmail = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKPassword = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKCheckPassword = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKCheckEmail = new MutableLiveData<>(false);

    private String advertId = null;

    //이메일 인증
    private MutableLiveData<String> checkEmailLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    //Executor
    private GoogleLoginExecutor mGoogleLoginExecutor;
    private CheckEmailFragment newDialogFragment;

    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }

    //AdID조회
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
                Log.i(TAG, "getAdID: success to get AdID");
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    };

    //이메일 중복 확인
    @Override
    public void checkEmailDuplicate() {
        loadingLiveData.setValue(true);
        APIRequest.request(RetrofitClient.getUserApiService().CheckEmail(emailLiveData.getValue()), objectResponse -> {
            Gson gson = new Gson();
            int code = objectResponse.code();
            String body = gson.toJson(objectResponse.body());
            APIResponse res = gson.fromJson(body, APIResponse.class);
            if (code >= 500) {
                //서버 에러
                loadingLiveData.setValue(false);
            } else if (code >= 400) {
                //클라이언트 에러
                loadingLiveData.setValue(false);
            } else if (code >= 300) {
                loadingLiveData.setValue(false);
            } else if (code >= 200) {
                //성공
                switch (res.getMessage()) {
                    case "SUCCESS":
                        //이메일 중복확인 성공
                        onRenderCheckEmail();
                        break;
                    case "FAIL":
                        if (res.getDetail().equals("DUPLICATE EMAIL")) {
                            //중복된 이메일
                            Toast.makeText(mActivityRef.get(), R.string.toast_check_email_duplicate_message, Toast.LENGTH_SHORT).show();
                            loadingLiveData.setValue(false);
                        }
                        break;
                }
            }
        }, throwable -> {
            loadingLiveData.setValue(false);
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

    //이메일 인증 Activity 열기
    private void onRenderCheckEmail() {
//        이메일 인증 요청 보내기
        APIRequest.request(RetrofitClient.getUserApiService().RequireEmailAuth(new EmailAuth(emailLiveData.getValue())),
                (objectResponse -> {
                    loadingLiveData.setValue(false);
                    Gson gson = new Gson();
                    int code = objectResponse.code();
                    String body = gson.toJson(objectResponse.body());
                    APIResponse res = gson.fromJson(body, APIResponse.class);
                    if (code >= 500) {
                        //서버 에러
                        Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();

                    } else if (code >= 400) {
                        //클라이언트 에러
                    } else if (code >= 300) {

                    } else if (code >= 200) {
                        //성공
                        switch (res.getMessage()) {
                            case "SUCCESS":
                                //이메일 인증 fragment dialog 띄우기
                                checkEmailLiveData.setValue("");
                                newDialogFragment = CheckEmailFragment.newInstance();
                                newDialogFragment.show(mActivityRef.get().getFragmentManager(), "CHECK_EMAIL");
                                break;
                        }
                    }
                }), throwable -> {
                    loadingLiveData.setValue(false);
                    Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
                });
    }

    //이메일 인증 확인
    @Override
    public void checkEmailAuth() {
        loadingLiveData.setValue(true);
        APIRequest.request(RetrofitClient.getUserApiService().CheckEmailAuth(new EmailAuth(emailLiveData.getValue(), checkEmailLiveData.getValue())),
                (objectResponse -> {
                    loadingLiveData.setValue(false);
                    Gson gson = new Gson();
                    int code = objectResponse.code();
                    String body = gson.toJson(objectResponse.body());
                    APIResponse res = gson.fromJson(body, APIResponse.class);
                    if (code >= 500) {
                        //서버 에러
                        switch (res.getMessage()) {
                            case "FAIL":
                                if (res.getDetail().equals("TIMEOUT")) {
                                    //제한시간 종료
                                    Toast.makeText(mActivityRef.get(), R.string.toast_check_email_timeout_message, Toast.LENGTH_SHORT).show();
                                    newDialogFragment.dismiss();
                                }
                                break;
                        }
                    } else if (code >= 400) {
                        //클라이언트 에러
                    } else if (code >= 300) {

                    } else if (code >= 200) {
                        //성공
                        switch (res.getMessage()) {
                            case "SUCCESS":
                                Toast.makeText(mActivityRef.get(), R.string.toast_check_email_success_message, Toast.LENGTH_SHORT).show();
                                isOKCheckEmail.setValue(true);
                                newDialogFragment.dismiss();
                                break;
                            case "FAIL":
                                if (res.getDetail().equals("DIFFERENT AUTHNUM")) {
                                    //인증번호 틀림
                                    Toast.makeText(mActivityRef.get(), R.string.toast_check_email_differentAuthNum_message, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                }), throwable -> {
                    loadingLiveData.setValue(false);
                    Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
                });
    }

    //이메일 인증 창 닫기
    @Override
    public void closeEmailAuth() {
        newDialogFragment.dismiss();
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
        MemberRequest mem  =new MemberRequest();
        mem.setMem_email(emailLiveData.getValue());
        mem.setMem_password(passwordLiveData.getValue());
        mem.setAd_id(advertId);
        APIRequest.request(RetrofitClient.getLoginApiService().Login(mem), objectResponse -> {
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
                        Member member = res.getContent().getMember();
                        saveMemberInfo(member);
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

    //회원가입 요청
    @Override
    public void onRequestedSignUp() {
        MemberRequest mem  =new MemberRequest();
        mem.setMem_name(nameLiveData.getValue());
        mem.setMem_email(emailLiveData.getValue());
        mem.setMem_password(passwordLiveData.getValue());
        APIRequest.request(RetrofitClient.getUserApiService().SignUp(mem), objectResponse -> {
            Gson gson = new Gson();
            int code = objectResponse.code();
            String body = gson.toJson(objectResponse.body());
            APIResponse res = gson.fromJson(body, APIResponse.class);
            if (code >= 500) {
                //서버 에러
                Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();
            } else if (code >= 400) {
                //클라이언트 에러
            } else if (code >= 300) {

            } else if (code >= 200) {
                //성공
                switch (res.getMessage()) {
                    case "SUCCESS":
                        //회원가입 성공
                        Toast.makeText(mActivityRef.get(), R.string.toast_signup_success_message, Toast.LENGTH_SHORT).show();
                        //로그인 시도
                        onRequestedSignIn();
                        break;
                    case "FAIL":
                        if (res.getDetail().equals("DUPLICATE EMAIL")) {
                            //중복된 이메일
                            Toast.makeText(mActivityRef.get(), R.string.toast_check_email_duplicate_message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }, throwable -> {
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
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
                    String name = account.getDisplayName();
                    String email = account.getEmail();
                    Uri imageUri = account.getPhotoUrl();
                    String image = imageUri != null ? imageUri.toString() : null;
                    googleLogin(name,email,image);
                } else {
                    //로그인 실패
                    Toast.makeText(mActivityRef.get(), R.string.toast_social_login_fail_message, Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                //로그인 취소
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

    //구글 로그인 정보로 사용자 정보 조회
    private void googleLogin(String name, String email, String image){
        MemberRequest mem  =new MemberRequest();
        mem.setMem_name(name);
        mem.setMem_email(email);
        mem.setMem_image(image);
        mem.setAd_id(advertId);
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
    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    @Override
    public void setLoadingLiveData(MutableLiveData<Boolean> loadingLiveData) {
        this.loadingLiveData = loadingLiveData;
    }
}
