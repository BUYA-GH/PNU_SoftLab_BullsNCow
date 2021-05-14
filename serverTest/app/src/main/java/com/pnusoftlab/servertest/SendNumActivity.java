package com.pnusoftlab.servertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendNumActivity extends Activity {
    TextView myView;
    TextView otherView;

    String name;
    int port;
    int answer;
    String otherName;
    String ip = "211.109.68.18";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendnum);
        setTitle("Input Answer");

        myView = findViewById(R.id.You);
        otherView = findViewById(R.id.Other);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        port = intent.getIntExtra("Port", 0);
        answer = intent.getIntExtra("Answer", 0);

        myView.setText("Your name : " + name);

        try {
            Socket socket = new Socket(ip, port);

            Thread sender = new Thread(new ClientSender(socket, name, answer));
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start();
        } catch(IOException ie){
            ie.printStackTrace();
        }

    }

    class ClientSender extends Thread {
        Socket socket;
        DataOutputStream out;
        String name;
        int answer;

        public ClientSender(Socket socket, String name, int answer){
            this.socket = socket;
            this.name = name;
            this.answer = answer;
            try{
                out = new DataOutputStream(socket.getOutputStream());
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                out.writeUTF(name);
                out.writeInt(answer);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream in;

        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (in != null) {
                try {
                    otherName = in.readUTF();
                    otherView.setText("Other's name : " + otherName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
