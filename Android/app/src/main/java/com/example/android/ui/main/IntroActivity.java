package com.example.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.model.dto.Event;
import com.example.android.data.viewmodel.IntroViewModel;
import com.example.android.data.viewmodelimpl.IntroViewModelImpl;
import com.example.android.ui.user.LoginActivity;

import java.util.UUID;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity";

    private IntroViewModel mIntroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);

        mIntroViewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(IntroViewModelImpl.class);
        mIntroViewModel.setParentContext(this);
        mIntroViewModel.getPermissionAndLogin();
        mIntroViewModel.getCheckAutoLoginLiveData().observe(this,this::setHandler);

        String useruuid = null;
        useruuid = UUID.randomUUID().toString();
        Log.i(TAG, "onCreate: "+useruuid);
    }

//    private String GetDevicesUUID(Context mContext){
//        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        final String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String deviceId = deviceUuid.toString();
//        return deviceId;
//    }

    
    //권한 허용에 대한 결과 전달
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mIntroViewModel.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    //자동로그인 확인 결과에 따른 처리
    private void setHandler(Event<Boolean> event){
        boolean canLogin = event.getContentIfNotHandled();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            //로그인 성공
            if (canLogin) {
                intent = new Intent(IntroActivity.this, MainActivity.class);
            }
            //로그인 실패
            else {
                intent = new Intent(IntroActivity.this, LoginActivity.class);
            }
            startActivity(intent);

            //화면 전환 animation 효과 적용
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            //인트로로 다시 돌아오지 못하도록 끝내기
            finish();
        },2000);
    }
}