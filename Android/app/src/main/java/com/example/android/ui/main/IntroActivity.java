package com.example.android.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android.R;
import com.example.android.ui.user.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class IntroActivity extends AppCompatActivity {

    private final int WRITE_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //파일 접근 권한 허용되지 않았을 때
            //권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        } else {
            //권한 허용되어 있을 때
            startIntro();
        }
    }

    //권한 승인 결과에 따라서 실행
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
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


    private void startIntro() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            //로그인 성공
            if (account != null) {
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
        }, 2000);
    }

    //어플리케이션 종료
    private void exitProgram() {
        // 태스크를 백그라운드로 이동
        moveTaskToBack(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // 액티비티 종료 + 태스크 리스트에서 지우기
            finishAndRemoveTask();
        } else {
            // 액티비티 종료
            finish();
        }
        System.exit(0);
    }
}