package com.pnusoftlab.otherclient;

import android.os.RemoteException;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketManager {
    final int STATUS_DISCONNECTED = 0;
    final int STATUS_CONNECTED = 1;

    private int status = STATUS_DISCONNECTED;
    Socket socket = null;
    DataOutputStream out = null;
    DataInputStream in = null;

    String ip = "211.109.68.18";
    int port = 7777;

    private static final SocketManager instance = new SocketManager();

    public SocketManager() { Log.i("SocketManager", "SocketManager()"); }

    public static SocketManager getInstance() {
        return instance;
    }

    public void connect() {
        Log.i("SocketManager", "connect()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        socket = new Socket(ip, port);
                        if(socket != null) {
                            in = new DataInputStream(socket.getInputStream());
                            out = new DataOutputStream(socket.getOutputStream());
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                status = STATUS_CONNECTED;
            }
        }).start();
    }

    public void disConnect() {
        Log.i("SocketManager", "disConnect()");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = STATUS_DISCONNECTED;
    }

    public void send(String buf) {
        Log.i("SocketManager", "send()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    out.writeUTF(buf);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String receive() {
        Log.i("SocketManager", "receive()");
        String buf = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String buf = in.readUTF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return buf;
    }
}