package com.example.android.data.viewmodelimpl;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.example.android.data.model.dto.MemberRequest;
import com.example.android.data.viewmodel.IntroViewModel;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

public class IntroViewModelImpl extends ViewModel implements IntroViewModel {

    private final String TAG = "IntroViewModelImpl";

    private final int PERMISSON_CODE = 1;
    private final int EXTERNAL_STORAGE_MANAGER = 2;

    private String advertId = null;

    private WeakReference<Activity> mActivityRef;

    private MutableLiveData<Event<Boolean>> checkAutoLoginLiveData = new MutableLiveData<>();

    //activity지정
    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void getAdID() {
        GetAdidThread getAdidThread = new GetAdidThread();
        getAdidThread.start();
    }

    //AdID 조회
    public class GetAdidThread extends Thread {
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
            try {
                advertId = idInfo.getId();
                Log.i(TAG, "GetAdidThread: success to get AdID");
                mActivityRef.get().runOnUiThread(() -> {    //현재 스레드가 UI스레드가 아니니 UI스레드에서 실행하도록 runOnUiThread설정
                    // TODO.
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        getExternalStoragePermission();
                    }
                    else{
                        getPermissionAndLogin();
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
                mActivityRef.get().runOnUiThread(() -> {
                    // TODO.
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        getExternalStoragePermission();
                    }
                    else{
                        getPermissionAndLogin();
                    }
                });

            }
        }
    };

    //모든 파일에 대한 접근 권한 확인
    private void getExternalStoragePermission(){
        if(Environment.isExternalStorageManager()){
            getPermissionAndLogin();
        }else{
            Toast.makeText(mActivityRef.get(), R.string.toast_intro_getPermission, Toast.LENGTH_SHORT).show();
            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            mActivityRef.get().startActivityForResult(permissionIntent,EXTERNAL_STORAGE_MANAGER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(Environment.isExternalStorageManager()){
            getPermissionAndLogin();
        }else{
            exitProgram();
        }
    }

    //파일 읽기, 쓰기 권한 확인
    private void getPermissionAndLogin() {

        if (ContextCompat.checkSelfPermission(mActivityRef.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mActivityRef.get(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            //파일 접근 권한 허용되지 않았을 때
            //권한 요청
            ActivityCompat.requestPermissions(mActivityRef.get(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSON_CODE);
        } else {
            //권한 허용되어 있을 때
            startIntro();
        }
    }

    //권한 승인 결과에 따라서 실행
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSON_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
        String login = loginInformation.getString("login", "none");
        //기본 로그인인지 판단
        if (login.equals("basic")) {
            String email = loginInformation.getString("email", "");
            String password = loginInformation.getString("password", "");
            login(email, password);
        } else if (login.equals("google")) {
            //구글로그인이거나, 로그인 정보가 없는지 판단
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivityRef.get());

            if (account != null) {
                String name = account.getDisplayName();
                String email = account.getEmail();
                Uri imageUri = account.getPhotoUrl();
                String image = imageUri != null ? imageUri.toString() : null;
                googleLogin(name, email, image);
            } else {
                checkAutoLoginLiveData.setValue(new Event<>(false));
            }
        } else {
            checkAutoLoginLiveData.setValue(new Event<>(false));
        }
    }

    //일반 로그인
    private void login(String email, String password) {
        MemberRequest member = new MemberRequest();
        member.setMem_email(email);
        member.setMem_password(password);
        member.setAd_id(advertId);
        APIRequest.request(RetrofitClient.getLoginApiService().Login(member), objectResponse -> {
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
                        Member mem = res.getContent().getMember();
                        saveMemberInfo(mem);
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
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
            checkAutoLoginLiveData.setValue(new Event<>(false));
        });
    }

    //구글 계정으로 사용자 정보 얻어오기
    private void googleLogin(String name, String email, String image) {
        MemberRequest member = new MemberRequest();
        member.setMem_name(name);
        member.setMem_email(email);
        member.setMem_image(image);
        member.setAd_id(advertId);
        APIRequest.request(RetrofitClient.getLoginApiService().SocialLogin(member), objectResponse -> {
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
                        Member mem = res.getContent().getMember();
                        saveMemberInfo(mem);
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
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
            checkAutoLoginLiveData.setValue(new Event<>(false));
        });
    }

    //사용자 정보 저장
    private void saveMemberInfo(Member member) {
        SharedPreferences tokenInformation = mActivityRef.get().getSharedPreferences("member", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenInformation.edit();
        editor.putInt("mem_id", member.getMem_id());
        editor.putString("dev_id", member.getDev_id());
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

    //LiveData getter & setter
    @Override
    public MutableLiveData<Event<Boolean>> getCheckAutoLoginLiveData() {
        return checkAutoLoginLiveData;
    }

    @Override
    public void setCheckAutoLoginLiveData(MutableLiveData<Event<Boolean>> checkAutoLoginLiveData) {
        this.checkAutoLoginLiveData = checkAutoLoginLiveData;
    }
}
