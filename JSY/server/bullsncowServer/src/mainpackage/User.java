package mainpackage;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class User {
	HashMap<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();
	
	public synchronized void AddName(String name, Socket socket) {
		try {
            clientmap.put(name, new DataOutputStream(socket.getOutputStream()));
            System.out.println(name + " has successfully Connected!");
            System.out.println("Num of Client : " + clientmap.size());
        } catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	 public synchronized void RemoveClient(String name)  {
		 try {
			 clientmap.remove(name);
			 System.out.println(name + " has successfully UnConnected!");
	         System.out.println("Num of Client : " + clientmap.size());
	     } catch(Exception e){
	    	 e.printStackTrace();
	     }
	}
	
	public synchronized void sendAnswer(String name, int i) {
		try {
			DataOutputStream out = clientmap.get(name);
			if(i == 0) {
				System.out.println(name + " 's answer is Fail");
				out.writeUTF("FailAnswer");
			}
			else {
				System.out.println(name + " 's answer is Correct");
				out.writeUTF("SuccessAnswer");
			}
		} catch(Exception e){
        	e.printStackTrace();
        }
	}
}
