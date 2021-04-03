package com.example.android.data.connection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.example.android.data.connection.dto.FileStat;
import com.google.gson.Gson;


public class SocketData {

    private static final int CHUNK_SIZE = 16 * 1024 * 1024;
    private FileStat fs;
    private File file;

    private Socket socket;

    FileOutputStream fileOutput = null;
    DataInputStream dataInput = null;
    byte[] buf = null;
    BufferedInputStream bufferdInput = null;

    public SocketData(FileStat fs) {
        super();
        this.fs = fs;
    }

    public void connect() {
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
                    System.out.println("tmp : " + tmp);
                    fileOutput.flush();
                }
                if (size - tmp <= CHUNK_SIZE) {
                    int i = 0;
                    buf = new byte[(int) (size - tmp)];
                    while (i < size - tmp) {
                        buf[i] = (byte) bufferdInput.read();
                        i++;
                    }
                    fileOutput.write(buf);
                }

                fileOutput.flush();
//                File newFile = new File(fs.getPath() + "/" + fs.getName() + fs.getExt());
//                boolean isSuc = file.renameTo(newFile);
                System.out.println("FILE을 모두 썼습니다.");
//                }
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
