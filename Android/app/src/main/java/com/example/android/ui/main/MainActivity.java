package com.example.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;

/*
MainActivity : 파일 전송, 계정 설정 선택하는 메인 화면
 */
public class MainActivity extends AppCompatActivity {

    private Button sendButton;
    private Button userButton;

    private BackPressHandler  backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.activity_main_go_send_button);
        userButton = findViewById(R.id.activity_main_go_user_button);

        Intent intent = new Intent(MainActivity.this, BackdropActivity.class);

        //공유하기
        sendButton.setOnClickListener(v -> {
            intent.putExtra("page","send");
            startActivity(intent);
        });
        
        //계정 설정
        userButton.setOnClickListener(v -> {
            intent.putExtra("page","user");
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}