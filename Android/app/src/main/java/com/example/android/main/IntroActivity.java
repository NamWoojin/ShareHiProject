package com.example.android.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.user.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            //로그인 성공
            if (mAuth.getCurrentUser() != null) {
                intent = new Intent(IntroActivity.this, MainActivity.class);
            }
            //로그인 실패
            else{
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