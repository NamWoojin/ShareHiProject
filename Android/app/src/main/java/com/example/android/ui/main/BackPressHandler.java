package com.example.android.ui.main;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

/*
BackPressHandler : 뒤로 가기 두번 눌러 종료 구현
 */
public class BackPressHandler {
    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;

    private Activity activity;

    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    public BackPressHandler(Activity activity){
        this.activity = activity;
    }

    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            exitProgram();
        }

    }

    //어플리케이션 종료
    private void exitProgram() {
        // 태스크를 백그라운드로 이동
        activity.moveTaskToBack(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // 액티비티 종료 + 태스크 리스트에서 지우기
            activity.finishAndRemoveTask();
        } else {
            // 액티비티 종료
            activity.finish();
        }
        System.exit(0);
    }

}
