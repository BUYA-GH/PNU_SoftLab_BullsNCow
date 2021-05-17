package com.pnusoftlab.clienttest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectionService extends Service {
    final int STATUS_DISCONNECTED = 0;
    final int STATUS_CONNECTED = 1;

    final int TIME_OUT = 5000;

    private int status = STATUS_DISCONNECTED;
    private Socket socket = null;
    private SocketAddress socketAddress = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    final int port = 7777;

    IConnectionService.Stub binder = new IConnectionService.Stub() {
        @Override
        public int getStatus() throws RemoteException {
            return status;
        }

        @Override
        public void setSocket(String ip) throws RemoteException {
            mySetSocket(ip);
        }

        @Override public void connect() throws RemoteException {
            myConnect();
        }

        @Override public void disconnect() throws RemoteException {
            myDisconnect();
        }

        @Override public void send(String buffer) throws RemoteException {
            mySend(buffer);
        }

        @Override public void receive() throws RemoteException {
            myReceive();
        }
    };

    public ConnectionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ConnectionService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ConnectionService", "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ConnectionService", "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("ConnectionService", "onBind()");
        return binder;
    }

    @Override public boolean onUnbind(Intent intent) {
        Log.i("ConnectionService", "onUnbind()");
        return super.onUnbind(intent);
    }

    void mySetSocket(String ip) {
        socketAddress = new InetSocketAddress(ip, port);
        Log.i("ConnectionService", "mySetSocket()");
    }

    void myConnect() {
        Log.i("ConnectionService", "myConnect1()");
        socket = new Socket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.connect(socketAddress, TIME_OUT);
                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                    Log.i("ConnectionService", "myConnect2()");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                status = STATUS_CONNECTED;
            }
        }).start();
    }

    void myDisconnect() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = STATUS_DISCONNECTED;
    }

    void mySend(String outBuffer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeUTF(outBuffer);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void myReceive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String inBuffer = in.readUTF();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}