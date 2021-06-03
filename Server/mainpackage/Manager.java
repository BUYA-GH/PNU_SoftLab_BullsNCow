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
			user.setAnswer(name, answer);
			user.sendEnable(name);
			
			buffer = in.readUTF();
			
			// Start Game
			System.out.println(name + " : Set Pins");
			user.sendStartPin(name);
			
			buffer = in.readUTF();
			String [] set = buffer.split(":");
			if(set[0].equals("ARRIVE") && set[1].equals( user.getStartPin(name) )) {
				user.sendUnablePin(name, user.getStartPin(name));
			}
			System.out.println(name + " : has arrive at start pin");
			
			// Server는 두개의 client가 시작핀에 있기 전까지 while 안에서 돔
			while(!user.isGameReady());
			
			System.out.println(name + " : All is ready");
			user.sendAllisReady(name);
			user.sendPins(name);
			while(user.getRound() <= 9) {
				buffer = in.readUTF();
				set = buffer.split(":");
				
				System.out.println(name + " : is arrive at " + set[1]);
				if(set[0].equals("ARRIVE")) {
					user.sendUnablePin(name, set[1]);
				}


				while(true) {
					buffer = in.readUTF();
					//System.out.println(buffer + "is answer?");
					System.out.println(name + " : said " + buffer + " will be answer");
					int a = Integer.parseInt(buffer);
					for(int i = 0; i < 3; ++i ) {
						answer[2-i] = a % 10;
						a /= 10;
	                }
					if(answer[0] == 0 || answer[0] == answer[1] || answer[1] == answer[2] || answer[2] == answer[0]) {
						user.sendToast(name, "Not correct Answer, Try Again!");
					}			
					else {
						user.matchAnswer(name, answer);
						break;
					}	
				}			
			}
			
			user.sendToast(name, "Game is End");
			while(true);
			
		} catch(Exception e) {
			user.RemoveClient(this.name);
		}
		
	}
}

