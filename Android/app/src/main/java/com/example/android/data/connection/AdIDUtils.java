package com.example.android.data.connection;

import android.content.Context;
import android.util.Log;


import com.example.android.ui.main.IntroActivity;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;


public class AdIDUtils {

    private Context context;

    public AdIDUtils(Context context){
        this.context = context;
    }

//    public String getAdID(){
//        GetAdID thread = new GetAdID();
//        thread.start();
//        try{
//            thread.join();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//        return thread.getAdvertId();
//    }
//
//    class GetAdID extends Thread{
//
//    }
}
