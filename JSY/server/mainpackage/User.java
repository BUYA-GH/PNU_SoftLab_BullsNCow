package mainpackage;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class User {
	HashMap<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();
	HashMap<String, Integer> nummap = new HashMap<String, Integer>();
	int[] enable = {0, 0};
	int count = 0;
	
	public synchronized void sendPrev(String name) {
		Iterator<String> keys = clientmap.keySet().iterator();
		try {
			while(keys.hasNext() ) {
				String otherName = (String)keys.next();
				if(! otherName.equals(name) ) {
					clientmap.get(otherName).writeUTF("OTHER:" + name);
					clientmap.get(name).writeUTF("OTHER:" + otherName);
					clientmap.get(otherName).writeUTF("ENABLE:" + name + ":" + enable[nummap.get(name)]);
					clientmap.get(name).writeUTF("ENABLE:" + otherName + ":" + enable[nummap.get(otherName)]);
					
					clientmap.get(otherName).flush();
					clientmap.get(name).flush();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void AddName(String name, Socket socket) {
		try {
            clientmap.put(name, new DataOutputStream(socket.getOutputStream()));
            nummap.put(name, count);
            enable[count] = 1;
            clientmap.get(name).writeUTF("ENABLE:" + name + ":" + enable[nummap.get(name)]);
            System.out.println(name + " has successfully Connected!");
            System.out.println("Num of Client : " + clientmap.size());
            count++;
        } catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	 public synchronized void RemoveClient(String name)  {
		 try {
			 clientmap.remove(name);
			 nummap.remove(name);
			 System.out.println(name + " has successfully UnConnected!");
	         System.out.println("Num of Client : " + clientmap.size());
	         count--;
	     } catch(Exception e){
	    	 e.printStackTrace();
	     }
	}
	
	public synchronized void sendAnswer(String name, int i) {
		try {
			DataOutputStream out = clientmap.get(name);
			int clientNum = nummap.get(name);
			if(i == 0) {
				System.out.println(name + " 's answer is Fail");
				out.writeUTF("ANSWER:Fail");
				out.flush();
			}
			else {
				System.out.println(name + " 's answer is Correct");
				enable[clientNum] = 2;
				out.writeUTF("ANSWER Success");
				out.flush();
			}
		} catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public synchronized void sendEnable(String name) {
		Iterator<String> keys = clientmap.keySet().iterator();
		try {
			while(keys.hasNext()) {
				String otherName = (String)keys.next();
				//clientmap.get(otherName).writeUTF("OTHER:" + name);
				clientmap.get(otherName).writeUTF("ENABLE:" + name + ":" + enable[nummap.get(name)]);
				
				clientmap.get(otherName).flush();
			}
		} catch(Exception e){
        	e.printStackTrace();
        }
	}
}
