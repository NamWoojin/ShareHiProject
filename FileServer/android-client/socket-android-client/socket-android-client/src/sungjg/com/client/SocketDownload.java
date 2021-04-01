package sungjg.com.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	DataOutputStream dataOutput = null;
	byte[] buf = null;
	BufferedOutputStream bufferdOutput = null;

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
			dataOutput = new DataOutputStream(socket.getOutputStream());
			bufferdOutput = new BufferedOutputStream(dataOutput);
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
				long tmp = 0;
				while (size - tmp > CHUNK_SIZE) {
					int i = 0;
					while (i < CHUNK_SIZE) {
						buf[i] = (byte) fileInput.read();
						i++;
					}
					tmp += (CHUNK_SIZE);
					i = 0;
					bufferdOutput.write(buf);
					System.out.println("tmp : " + tmp);
					bufferdOutput.flush();
				}
				if (size - tmp <= CHUNK_SIZE) {
					int i = 0;
					buf = new byte[(int) (size - tmp)];
					while (i < size - tmp) {
						buf[i] = (byte) fileInput.read();
						i++;
					}
					bufferdOutput.write(buf);
					tmp += (size - tmp);
				}

				bufferdOutput.flush();

				System.out.println("FILE을 모두 전송했습니다..");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferdOutput != null)
						bufferdOutput.close();
					if (dataOutput != null)
						dataOutput.close();
					if (fileInput != null)
						fileInput.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
}
