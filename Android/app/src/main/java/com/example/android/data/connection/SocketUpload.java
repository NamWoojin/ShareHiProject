package com.example.android.data.connection;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.example.android.data.connection.dto.FileStat;

/*
SocketUpload : 안드로이드 -> 서버 -> 웹의 파일 전송
 */
public class SocketUpload {
    private final String TAG = "SocketUpload";

    private static final int CHUNK_SIZE = 16 * 1024 * 1024;
    private FileStat fs;
    private File file;

    private Socket socket;

    FileInputStream fileInput = null;
    DataInputStream dis = null;
    BufferedInputStream bis = null;

    OutputStream os = null;

    PrintWriter out = null;

    byte[] buf = null;

    public SocketUpload(FileStat fs) {
        super();
        this.fs = fs;
    }

    public void connect() {
        try {
            file = new File(fs.getPath() +"/"+ fs.getName() + fs.getExt());
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(SocketInfo.IP, SocketInfo.PORT);
            socket.connect(socketAddress, 8288);
            socket.setSoTimeout(10000);

            buf = new byte[CHUNK_SIZE];
            fileInput = new FileInputStream(file);
            dis = new DataInputStream(fileInput);
            bis = new BufferedInputStream(fileInput);

            os = socket.getOutputStream();

            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            getIO.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Thread getIO = new Thread() {
        @Override
        public void run() {
            try {
                int size = (int) fs.getSize();
                Log.i(TAG, "run: size - "+ size);
                int tmp = 0;

                while (size - tmp > CHUNK_SIZE) {
                    dis.read(buf);
                    os.write(buf);
                    tmp += CHUNK_SIZE;
                    Log.i(TAG, "run: tmp - "+ tmp);
                    os.flush();
                }
                if (size - tmp <= CHUNK_SIZE) {
                    buf = new byte[(int) (size - tmp)];
                    dis.read(buf);
                    os.write(buf);
                    tmp += (size - tmp);
                    Log.i(TAG, "run: tmp - "+ tmp);
                    os.flush();
                }
                Thread.sleep(10000);
                Log.i(TAG, "run: finish upload file");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null)
                        os.close();
                    if (bis != null)
                        bis.close();
                    if (fileInput != null)
                        fileInput.close();
                    if (dis != null)
                        dis.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
