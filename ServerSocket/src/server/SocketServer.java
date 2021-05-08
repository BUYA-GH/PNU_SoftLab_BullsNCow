package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	public static void main(String[] args) {
		int portNumber = 5555;
		
		try {
			System.out.println("Start Server...");
			ServerSocket serverSocket = new ServerSocket(portNumber);
			System.out.println("Port " + portNumber + " is wait for Connect...");
			
			while(true) {
				Socket socket = serverSocket.accept();
				InetAddress clientHost = socket.getLocalAddress();
				int clientPort = socket.getPort();
				 System.out.println("Client Connect Complete : " + clientHost + ", Port : " + clientPort);
				 
				 ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
				 Object obj = instream.readObject();
				 System.out.println("Data from Client : " + obj);
				 
				 ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
				 outstream.writeObject(obj + " from server");
				 outstream.flush();
				 socket.close(); 
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
