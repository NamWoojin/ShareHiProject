package com.example.android.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.android.R;

public class DownloadNotification {
    private static final String CHANNEL_ID = "DownloadNotification";
    private int notificationId = 0;
    private static final int PROGRESS_MAX = 100;
    private String fileName, path;
    private NotificationCompat.Builder builder;
    NotificationManagerCompat notificationManager;

    public DownloadNotification(Context context,String fileName, String path){
        notificationId = NotificationID.getID();
        this.fileName = fileName;
        this.path = path;
        notificationManager = NotificationManagerCompat.from(context);
        builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(path+"에 "+fileName+"을(를) 받는 중입니다."))
                .setContentTitle(fileName + "을(를) 다운받는 중입니다.")
                .setProgress(0,0, true);
        notificationManager.notify(notificationId, builder.build());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Oreo 이상 버전은 notification channel 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        Log.i("TAG", "makeNotification: "+notificationManager);
    }

    public void startNotification(int PROGRESS_CURRENT){
        Log.i("TAG", "startNotification: "+PROGRESS_CURRENT);
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        if(PROGRESS_CURRENT == PROGRESS_MAX){
            Log.i("TAG", "startNotification: "+PROGRESS_CURRENT);
            builder .setContentTitle(fileName + "을(를) 다운받았습니다.")
                    .setContentText("")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(path+"에 "+fileName+"이(가) 저장되었습니다."));
        }else{
            builder
                    .setContentText(PROGRESS_CURRENT+"/"+PROGRESS_MAX);

        }
        notificationManager.notify(notificationId, builder.build());
    }
}
