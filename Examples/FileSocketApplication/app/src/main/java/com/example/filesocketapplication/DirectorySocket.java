package com.example.filesocketapplication;

import android.util.Log;

import org.json.JSONObject;

public class DirectorySocket {

    private JSONObject jsonObject;

    public DirectorySocket(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        directorySendThread.start();
    }

    private Thread directorySendThread = new Thread(){
        @Override
        public void run() {
            super.run();
            SocketInfo s = new SocketInfo();

            s.connect();
        }
    };
}
