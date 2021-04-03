package sungjg.com.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.google.gson.Gson;

import sugjg.com.dto.FileStat;

public class SocketDownload {
	private static final int CHUNK_SIZE = 16 * 1024 * 1024;
	private FileStat fs;
	private File file;

	private Socket socket;
	Gson gson = new Gson();

	FileInputStream fileInput = null;
	DataInputStream dis = null;
	BufferedInputStream bis = null;

	//BufferedOutputStream bos = null;
	OutputStream os = null;
	
	PrintWriter out = null;

	byte[] buf = null;

	public SocketDownload(FileStat fs) {
		super();
		this.fs = fs;
	}

	public void connect() {
		try {
			file = new File(fs.getPath() + fs.getName() + fs.getExt());
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(Client.IP, Client.PORT);
			socket.connect(socketAddress, 8288);
			socket.setSoTimeout(10000);

			buf = new byte[CHUNK_SIZE];
			fileInput = new FileInputStream(file);
			dis = new DataInputStream(fileInput);
			bis = new BufferedInputStream(fileInput);

			//bos = new BufferedOutputStream(socket.getOutputStream());
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
				System.out.println("size : " + size);
				int tmp = 0;

				while (size - tmp > CHUNK_SIZE) {
					dis.read(buf);
					os.write(buf);
					tmp += CHUNK_SIZE;
					System.out.println("tmp : " + tmp);
					os.flush();
				}
				if (size - tmp <= CHUNK_SIZE) {
					buf = new byte[(int) (size - tmp)];
					dis.read(buf);
					os.write(buf);
					tmp += (size - tmp);
					System.out.println("tmp : " + tmp);
					os.flush();
				}

				System.out.println("FILE을 모두 전송했습니다..");
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
