package com.example.android.data.connection;

import android.content.Context;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.example.android.data.connection.dto.FileStat;
import com.example.android.notification.DownloadNotification;

/*
SocketDownload : 웹 -> 서버 -> 안드로이드의 파일 전송
 */
public class SocketDownload {

    private final String TAG = "SocketDownload";

    private static final int CHUNK_SIZE = 16 * 1024 * 1024;
    private FileStat fs;
    private File file;

    private Socket socket;
    private DownloadNotification downloadNotification;
    FileOutputStream fileOutput = null;
    DataInputStream dataInput = null;
    byte[] buf = null;
    BufferedInputStream bufferdInput = null;

    public SocketDownload(FileStat fs) {
        super();
        this.fs = fs;
    }

    public void connect(Context context) {
        try {
            file = new File(fs.getPath() + "/" + fs.getName() + fs.getExt());
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(SocketInfo.IP, SocketInfo.PORT);
            socket.connect(socketAddress, 8288);
            socket.setSoTimeout(10000);
            buf = new byte[CHUNK_SIZE];
            fileOutput = new FileOutputStream(file, true);
            dataInput = new DataInputStream(socket.getInputStream());
            bufferdInput = new BufferedInputStream(dataInput);
            downloadNotification = new DownloadNotification(context,fs.getName(),fs.getPath());
            getIO.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Thread getIO = new Thread() {
        @Override
        public void run() {
            try {
                long size = fs.getSize();
                long tmp = fs.getTmpfileSize();
                while (size - tmp > CHUNK_SIZE) {
                    int i = 0;
                    while (i < CHUNK_SIZE) {
                        buf[i] = (byte) bufferdInput.read();
                        i++;
                    }
                    tmp += (CHUNK_SIZE);
                    fileOutput.write(buf);
                    downloadNotification.startNotification((int)(((float)tmp/size)*100));
                    fileOutput.flush();
                }
                if (size - tmp <= CHUNK_SIZE) {
                    int i = 0;
                    buf = new byte[(int) (size - tmp)];
                    while (i < size - tmp) {
                        buf[i] = (byte) bufferdInput.read();
                        i++;
                    }
                    tmp += (size-tmp);
                    fileOutput.write(buf);
                }
                downloadNotification.startNotification((int)(((float)tmp/size)*100));
                fileOutput.flush();
                Log.i(TAG, "run: finish download file");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferdInput != null)
                        bufferdInput.close();
                    if (dataInput != null)
                        dataInput.close();
                    if (fileOutput != null)
                        fileOutput.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
