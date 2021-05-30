package mainpackage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class User {
	HashMap<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();
	HashMap<String, Integer> nummap = new HashMap<String, Integer>();
	HashMap<String, String> startmap = new HashMap<String, String>();
	int[] enable = {0, 0};
	int count = 0;
	int round = 1;
	
	Latlng lng = new Latlng();
	
	public int getCount() {
		return count;
	}
	
	public String getStartPin(String name) {
		return startmap.get(name);
	}
	
	public int getRound() { return round; }
	
	
	public boolean isGameReady() {
		if(enable[0] == 3 && enable[1] == 3) return true;
		else return false;
	}
	
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
				out.writeUTF("ANSWER:Success");
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
	
	public synchronized void sendStartPin(String name) {
		try {
			if(nummap.get(name) == 0) {
				clientmap.get(name).writeUTF("PIN:rainbow:35.2300507:129.0828376");
				startmap.put(name, "rainbow");
			} else {
				clientmap.get(name).writeUTF("PIN:north:35.2355016:129.0828778");
				startmap.put(name, "north");
			}
			clientmap.get(name).flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendPins(String name) {
		try {
			DataOutputStream out = clientmap.get(name);
			String outBuffer = null;
			for(int i = 0; i < 20; ++i) {
				outBuffer = lng.getPacket(i);
				if(outBuffer != null) {
					out.writeUTF(outBuffer);
					out.flush();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendUnablePin(String name, String pin) {
		try {
			if(pin.equals("rainbow") || pin.equals("north")) {
				clientmap.get(name).writeUTF("UNABLE:"+pin);
				int clientNum = nummap.get(name);
				enable[clientNum] = 3;
			}
			else {
				Iterator<String> keys = clientmap.keySet().iterator();
				while(keys.hasNext()) {
					String clientName = (String)keys.next();
					clientmap.get(clientName).writeUTF("UNABLE:"+pin);
					clientmap.get(clientName).writeUTF("POPUP");
				}
				lng.setPin(pin, 0);
				round++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendAllisReady(String name) {
		try {
			clientmap.get(name).writeUTF("START");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
