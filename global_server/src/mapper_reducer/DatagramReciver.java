package mapper_reducer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class DatagramReciver {
	
	private int port;
	private String path_out;
	private DatagramSocket socket;
	
	public DatagramReciver(int port, String pathout) {
		this.port = port;
		this.path_out = pathout;
	}

	public void closeReceiver(){
		socket.close();
	}
	
	public void receiveData(){
		System.out.println("Step 1 RECIEVER");
		try {
			socket = new DatagramSocket(port);
			
			byte[] buffer = new byte[1024];
			boolean recieved = false;
			while(!recieved){
				FileOutputStream fos = new FileOutputStream(path_out, true); // true == append data
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				byte[] data = packet.getData();
				
				System.out.println(data.length);
				
				if (data.length != 0){
					fos.write(data);
					recieved = true;
				}
				if (data.length == 0 && recieved){
					fos.close();
					recieved = false;
					socket.close();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
