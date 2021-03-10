package com.example.application.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.application.R;
import com.example.application.send.SendActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //공유하기
        final Button giveButton = findViewById(R.id.activity_main_go_send_buttion);
        giveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });
        
        //공유받기
        final Button takeButton = findViewById(R.id.activity_main_go_receive_buttion);
        takeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });
        
        //계정 설정
        final Button userButton = findViewById(R.id.activity_main_go_user_buttion);
        userButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });
    }
}