package sungjg.com.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SocketClientV1 {

	private static final String IP = "localhost";
	private static final int PORT = 8888;

	public Thread checkStart = new Thread() {
		
		private Socket socket;
		OutputStream os;
		InputStream is;
		byte[] bytes;
		int size = 0;
		boolean socketConnected = false;
		
		
		@Override
		public void run() {
			try {
				socket = new Socket();
				SocketAddress socketAddress = new InetSocketAddress(IP, PORT);
				socket.connect(socketAddress, 8288);
				os = socket.getOutputStream();
				String output = "message test";
				bytes = output.getBytes();
				ByteBuffer buffer = ByteBuffer.allocate(4);
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				buffer.putInt(bytes.length);
				os.write(buffer.array(),0,4);
				os.write(bytes);
				os.flush();
				socketConnected = true;
			} catch (Exception e) {
				e.printStackTrace();
				socketConnected = false;
			}
			
			try {
				if(socketConnected) {
					while(true) {
						is = socket.getInputStream();
						bytes = new byte[4];
						System.out.println("start");
						size = is.read(bytes, 0, 4);
						System.out.println("size : " + size);
						ByteBuffer buffer = ByteBuffer.wrap(bytes);
						buffer.order(ByteOrder.LITTLE_ENDIAN);
						int length = buffer.getInt();
						System.out.println("length : " + length);
						bytes = new byte[length];
						size = is.read(bytes, 0, length);
						System.out.println("next size : " + size);
						int bytesRead = 0;
						
						while(size < length) {
							bytesRead = size;
							System.out.println("here?");
							size += is.read(bytes, bytesRead, length-bytesRead);
							System.out.println("hi");
						}
						
						String input = new String(bytes, "UTF-8");
						System.out.println("input : " + input);
						if(size > 0) {
							break;
						}
						
					}
				}
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
