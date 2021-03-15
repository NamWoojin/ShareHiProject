package com.example.android.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.receive.DeviceFragment;
import com.example.android.send.PrepareFragment;
import com.example.android.user.SettingFragment;

public class BackdropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_backdrop);

        TextView actionBarTextView = (TextView)findViewById(R.id.activity_backdrop_action_bar_text);
        
        Intent intent = getIntent();
        String page = intent.getStringExtra("page");
        switch (page){
            case "send":
                replaceFragment(PrepareFragment.newInstance(),true);
                actionBarTextView.setText("공유하기");
                break;
            case "receive":
                replaceFragment(DeviceFragment.newInstance(),true);
                actionBarTextView.setText("공유받기");
                break;
            case "user":
                replaceFragment(SettingFragment.newInstance(),true);
                actionBarTextView.setText("계정설정");
                break;

        }

        //상단바 상세메뉴 여닫기
        View fragment = (View)findViewById(R.id.activity_backdrop_fragment);
        View actionBarDetail = (View)findViewById(R.id.activity_backdrop_action_bar_detail);
        ImageView actionBarMenu = (ImageView)findViewById(R.id.activity_backdrop_action_bar_menu);
        actionBarMenu.setOnClickListener(v -> {
            if(fragment.isEnabled()){
                fragment.animate().translationY(actionBarDetail.getHeight());
            }else{
                fragment.animate().translationY(0);
            }
            fragment.setEnabled(!fragment.isEnabled());
        });
    }

    //프래그먼트 화면이동
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_backdrop_fragment, fragment);
        if(!addToBackStack)  //뒤로가기 미적용
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}