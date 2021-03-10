package com.example.application.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.application.R;
import com.example.application.user.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            boolean login = true;
            Intent intent;
            //로그인 성공
            if (login) {
                intent = new Intent(IntroActivity.this, MainActivity.class);
            }
            //로그인 실패
            else{
                intent = new Intent(IntroActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            //화면 전환 animation 효과 적용
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        },3000);
    }
}