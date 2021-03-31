package com.example.serversampleapplication;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class AdUtil {
    private Context context;
    String advertId = null;

    public AdUtil(Context context){
        this.context = context;

    }

    public String getAdID(){
        GetAdID getAdID = new GetAdID();
        getAdID.start();
        synchronized (getAdID) {
            System.out.println("syncThread 가 완료될 때까지 기다린다.");
            try {
                getAdID.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Total Sum : " + advertId);
        }
        return advertId;
    }

    class GetAdID extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (this){
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try{
                    advertId = idInfo.getId();
//                    advertId="들들";
                    Log.i("TAG", "runnnn1: "+advertId);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                notify();
            }
        }
    }
}
