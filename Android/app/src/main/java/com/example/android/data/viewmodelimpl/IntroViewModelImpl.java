package com.example.android.data.viewmodelimpl;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.connection.APIRequest;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.dto.APIResponse;
import com.example.android.data.model.dto.Event;
import com.example.android.data.model.dto.LoginContent;
import com.example.android.data.model.dto.Member;
import com.example.android.data.model.dto.User;
import com.example.android.data.viewmodel.IntroViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class IntroViewModelImpl extends ViewModel implements IntroViewModel {

    private final int WRITE_EXTERNAL_STORAGE = 1;

    private static final String TAG = "IntroViewModelImpl";

    private WeakReference<Activity> mActivityRef;

    private MutableLiveData<Event<Boolean>> checkAutoLoginLiveData = new MutableLiveData<>();

    //activity지정
    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void getPermissionAndLogin() {
        if (ContextCompat.checkSelfPermission(mActivityRef.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //파일 접근 권한 허용되지 않았을 때
            //권한 요청
            ActivityCompat.requestPermissions(mActivityRef.get(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        } else {
            //권한 허용되어 있을 때
            startIntro();
        }
    }


    //권한 승인 결과에 따라서 실행
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 승인
                    startIntro();
                } else {
                    exitProgram();
                }
                return;
            }

        }
    }

    //자동로그인 판단
    private void startIntro() {
        SharedPreferences loginInformation = mActivityRef.get().getSharedPreferences("login", Activity.MODE_PRIVATE);
        Boolean isBagicLogin = loginInformation.getBoolean("basicLogin", false);
        //기본 로그인인지 판단
        if (isBagicLogin) {
            String email = loginInformation.getString("email", "");
            String password = loginInformation.getString("password", "");
            login(email, password);
        } else {
            //구글로그인이거나, 로그인 정보가 없는지 판단
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivityRef.get());
            checkAutoLoginLiveData.setValue(new Event<>(account != null));
            if(account != null) {
                String name = account.getDisplayName();
                String email = account.getEmail();
                Uri imageUri = account.getPhotoUrl();
                String image = imageUri != null ? imageUri.toString() : null;
                googleLogin(name,email,image);
            }
        }
    }

    //일반 로그인
    private void login(String email, String password) {
        APIRequest.request(RetrofitClient.getLoginApiService().Login(new User(email, password)), objectResponse -> {
            Gson gson = new Gson();
            int code = objectResponse.code();
            String body = gson.toJson(objectResponse.body());

            if (code >= 500) {
                //서버 에러
                Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();
                checkAutoLoginLiveData.setValue(new Event<>(false));
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
                        checkAutoLoginLiveData.setValue(new Event<>(true));
                        break;
                    case "FAIL":
                        //아이디 또는 이메일 틀림
                        Toast.makeText(mActivityRef.get(), R.string.toast_login_fail_message, Toast.LENGTH_SHORT).show();
                        checkAutoLoginLiveData.setValue(new Event<>(false));
                        break;
                }
            }
        }, throwable -> {
            Log.e(TAG, "onRequestedSignIn: " + throwable);
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
            checkAutoLoginLiveData.setValue(new Event<>(false));
        });
    }

    //구글 계정으로 사용자 정보 얻어오기
    private void googleLogin(String name, String email, String image){
        User user = new User();
        user.setMem_name(name);
        user.setMem_email(email);
        user.setMem_image(image);
        APIRequest.request(RetrofitClient.getLoginApiService().socialLogin(user), objectResponse -> {
            Gson gson = new Gson();
            int code = objectResponse.code();
            String body = gson.toJson(objectResponse.body());
            if (code >= 500) {
                //서버 에러
                Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();
                checkAutoLoginLiveData.setValue(new Event<>(false));
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
                        checkAutoLoginLiveData.setValue(new Event<>(true));
                        break;
                    case "FAIL":
                        //아이디 또는 이메일 틀림
                        Toast.makeText(mActivityRef.get(), R.string.toast_login_fail_message, Toast.LENGTH_SHORT).show();
                        checkAutoLoginLiveData.setValue(new Event<>(false));
                        break;
                }
            }
        }, throwable -> {
            Log.e(TAG, "onRequestedSignIn: " + throwable);
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
            checkAutoLoginLiveData.setValue(new Event<>(false));
        });
    }

    //사용자 정보 저장
    private void saveMemberInfo(Member member) {
        SharedPreferences tokenInformation = mActivityRef.get().getSharedPreferences("member", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenInformation.edit();
        String memberJson = new Gson().toJson(member);
        editor.putString("member", memberJson);
        editor.apply();
    }

    //JWT 토큰 저장
    private void saveLoginToken(String value) {
        SharedPreferences tokenInformation = mActivityRef.get().getSharedPreferences("token", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenInformation.edit();
        editor.putString("token", value);
        editor.apply();
    }

    //어플리케이션 종료
    private void exitProgram() {
        // 태스크를 백그라운드로 이동
        mActivityRef.get().moveTaskToBack(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // 액티비티 종료 + 태스크 리스트에서 지우기
            mActivityRef.get().finishAndRemoveTask();
        } else {
            // 액티비티 종료
            mActivityRef.get().finish();
        }
        System.exit(0);
    }

    @Override
    public MutableLiveData<Event<Boolean>> getCheckAutoLoginLiveData() {
        return checkAutoLoginLiveData;
    }

    @Override
    public void setCheckAutoLoginLiveData(MutableLiveData<Event<Boolean>> checkAutoLoginLiveData) {
        this.checkAutoLoginLiveData = checkAutoLoginLiveData;
    }
}
