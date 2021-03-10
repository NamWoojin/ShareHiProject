package com.example.android.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.send.SendActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //공유하기
        final Button giveButton = findViewById(R.id.activity_main_go_send_buttion);
        giveButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SendActivity.class);
            startActivity(intent);
        });
        
        //공유받기
        final Button takeButton = findViewById(R.id.activity_main_go_receive_buttion);
        takeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SendActivity.class);
            startActivity(intent);
        });
        
        //계정 설정
        final Button userButton = findViewById(R.id.activity_main_go_user_buttion);
        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SendActivity.class);
            startActivity(intent);
        });
    }
}