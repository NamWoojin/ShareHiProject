package com.example.android.data.viewimpl;

import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.example.android.R;
import com.example.android.data.view.LoginView;
import com.example.android.data.view.SettingView;

public class SettingViewImpl implements SettingView {

    private Button signOutButton;
    private Button userDeleteButton;

    private View mMainView;
    private SettingView.ActionListener mActionListener;
    private LifecycleOwner mLifecycleOwner;

    public SettingViewImpl(View view, LifecycleOwner lifecycleOwner) {
        mMainView = view;
        mLifecycleOwner = lifecycleOwner;

        signOutButton = mMainView.findViewById(R.id.fragment_setting_signout_button);
        userDeleteButton = (Button) view.findViewById(R.id.fragment_setting_user_delete_button);

        //로그아웃
        signOutButton.setOnClickListener(v -> {
            mActionListener.onRequestedSignOut();
//            Intent intent = new Intent(getActivity(), GoogleLoginActivity.class);
//            intent.putExtra("action", "SIGN_OUT");
//            startActivity(intent);
        });

        //회원탈퇴
        userDeleteButton.setOnClickListener(v -> {
            mActionListener.onRequestedRevokeAccess();
//            Intent intent = new Intent(getActivity(), GoogleLoginActivity.class);
//            intent.putExtra("action", "REVOKE_ACCESS");
//            startActivity(intent);
        });
    }

    @Override
    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }
}
