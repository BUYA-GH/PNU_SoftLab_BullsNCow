package mainpackage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receiver implements Runnable {
	Socket socket;
	DataInputStream in;
	User user = null;
	
	String name, buffer;
	int [] answer = new int[3];
	
	public Receiver(Socket socket, User user) throws IOException {
		this.socket = socket;
		this.user = user;
		in = new DataInputStream(socket.getInputStream());
		
		
		name = in.readUTF();
		user.AddName(name, socket);
		user.sendPrev(name);
	}
	
	@Override
	public void run() {
		try {		
			while(true) {
				buffer = in.readUTF();
				//System.out.println(buffer + "is answer?");
				
				int a = Integer.parseInt(buffer);
				for(int i = 0; i < 3; ++i ) {
					answer[2-i] = a % 10;
					a /= 10;
                }
				if(answer[0] == 0 || answer[0] == answer[1] || answer[1] == answer[2] || answer[2] == answer[0])
					user.sendAnswer(name, 0);
				else {
					user.sendAnswer(name, 1);
					break;
				}	
			}
			
			user.sendEnable(name);
			while(true);
			
		} catch(Exception e) {
			user.RemoveClient(this.name);
		}
		
	}
}

