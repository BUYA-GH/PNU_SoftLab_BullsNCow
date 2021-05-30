package mainpackage;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	final static int SERVER_PORT = 7777;
	User user = new User();
	Socket socket;
		
	public static void main(String[] args) {
		new Server();
	}
	
	public Server() {
		ServerSocket server_socket = null;
		int count = 0;
		Thread thread[] = new Thread[2];
		
		try {
			server_socket = new ServerSocket(SERVER_PORT);
			System.out.println("Server Open Complete!");
			
			while(true) {
				socket = server_socket.accept();
				if(user.getCount() >= 2) {
					System.out.println("Other client has connecet, but cannot join the game");
					socket.close();
				} else {					
					thread[count] = new Thread(new Manager(socket, user));
					thread[count].start();
					count++;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
