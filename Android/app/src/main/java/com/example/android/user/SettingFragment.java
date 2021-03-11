package com.example.android.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.android.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

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
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        
        //로그아웃
        Button signOutButton = (Button) view.findViewById(R.id.fragment_setting_signout_button);
        signOutButton.setOnClickListener(v -> {
            mAuth.signOut();

            // Google sign out
//            mGoogleSignInClient.signOut().addOnCompleteListener() {
//                restartApplication();
//            }
        });
        
        //회원탈퇴
        Button userDeleteButton = (Button) view.findViewById(R.id.fragment_setting_user_delete_button);
        userDeleteButton.setOnClickListener(v -> {
            mAuth.getCurrentUser().delete();
//            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this) {
//                restartApplication();
//            }
        });

        return view;
    }
    
    private void restartApplication(){
        System.runFinalization();

        //해당 앱의 루트 액티비티 종료
        getActivity().finishAffinity();

        //재실행
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        
        //현재 액티비티 종료
        System.exit(0);
    }
}