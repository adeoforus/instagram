package mapper_reducer;

import java.io.BufferedReader;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatagramSender {

	private File file_to_send;
	private InetAddress host_adress;
	private int port;

	public DatagramSender(File file_to_send, InetAddress host_adress, int port) {
		this.file_to_send = file_to_send;
		this.host_adress = host_adress;
		this.port = port;
	}

	public void sendFile() {
		BufferedReader in;
		try {
			// Read file in array of bites
			Path path = Paths.get(file_to_send.getAbsolutePath());
			byte[] message = Files.readAllBytes(path);
			
			// Create Datagram packet and socket
			DatagramPacket packet = new DatagramPacket(message, message.length, host_adress, port);
			DatagramSocket socket = new DatagramSocket();
			
			// Send the packet throw it
			socket.send(packet);
			// Close it
			socket.close();
			System.out.println("Send!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
