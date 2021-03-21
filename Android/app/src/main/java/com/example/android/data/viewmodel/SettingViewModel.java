package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;


public interface SettingViewModel{

    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
