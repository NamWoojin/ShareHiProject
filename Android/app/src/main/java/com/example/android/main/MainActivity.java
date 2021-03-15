package com.example.android.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(MainActivity.this, BackdropActivity.class);

        //공유하기
        final Button giveButton = findViewById(R.id.activity_main_go_send_button);
        giveButton.setOnClickListener(v -> {
            intent.putExtra("page","send");
            startActivity(intent);
        });
        
        //공유받기
//        final Button receiveButton = findViewById(R.id.activity_main_go_receive_button);
//        receiveButton.setOnClickListener(v -> {
//            intent.putExtra("page","receive");
//            startActivity(intent);
//        });
        
        //계정 설정
        final Button userButton = findViewById(R.id.activity_main_go_user_button);
        userButton.setOnClickListener(v -> {
            intent.putExtra("page","user");
            startActivity(intent);
        });
    }
}