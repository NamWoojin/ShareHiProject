package com.example.android.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.android.R;
import com.example.android.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class SettingFragment extends Fragment {



    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        
        //로그아웃
        Button signOutButton = (Button) view.findViewById(R.id.fragment_setting_signout_button);
        signOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GoogleLoginActivity.class);
            intent.putExtra("action", "SIGN_OUT");
            startActivity(intent);
        });
        
        //회원탈퇴
        Button userDeleteButton = (Button) view.findViewById(R.id.fragment_setting_user_delete_button);
        userDeleteButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GoogleLoginActivity.class);
            intent.putExtra("action", "REVOKE_ACCESS");
            startActivity(intent);
        });

        return view;
    }


    // [END onActivityResult]

}