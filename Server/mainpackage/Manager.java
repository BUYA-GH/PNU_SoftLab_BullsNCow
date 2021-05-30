package mainpackage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Manager implements Runnable {
	Socket socket;
	DataInputStream in;
	User user = null;
	
	String name, buffer;
	int [] answer = new int[3];
	
	public Manager(Socket socket, User user) throws IOException {
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
			// Ready for Game : Save Client Name n Answer
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
			
			buffer = in.readUTF();
			
			// Start Game
			user.sendStartPin(name);
			user.sendPins(name);
			
			buffer = in.readUTF();
			String [] set = buffer.split(":");
			if(set[0].equals("ARRIVE") && set[1].equals( user.getStartPin(name) )) {
				user.sendUnablePin(name, user.getStartPin(name));
			}
			
			while(!user.isGameReady());
			
			user.sendAllisReady(name);
			
			while(user.getRound() <= 9) {
				buffer = in.readUTF();
				set = buffer.split(":");
				if(set[0].equals("ARRIVE")) {
					user.sendUnablePin(name, set[1]);
				}
			}
			
			while(true);
			
		} catch(Exception e) {
			user.RemoveClient(this.name);
		}
		
	}
}

