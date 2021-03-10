package com.example.android.send;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.R;

public class SendActivity extends AppCompatActivity {

    private SendViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        replaceFragment(PrepareFragment.newInstance(),true);


        //상단바 상세메뉴 여닫기
        View fragment = (View)findViewById(R.id.activity_send_fragment);
        View actionBarDetail = (View)findViewById(R.id.activity_send_action_bar_detail);
        ImageView actionBarMenu = (ImageView)findViewById(R.id.activity_send_action_bar_menu);
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
    public void replaceFragment(Fragment fragment,boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_send_fragment, fragment);
        if(!addToBackStack)  //뒤로가기 미적용
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}