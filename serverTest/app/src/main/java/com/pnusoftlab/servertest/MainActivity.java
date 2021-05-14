package com.pnusoftlab.servertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    EditText portInput; // Host IP inputBox
    EditText dataInput; // inputBox about data which send to server
    EditText answerInput;
    Button socketConnectBtn; // button for Send n Connect
    String response;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Log in!");

        portInput = findViewById(R.id.portInput);
        dataInput = findViewById(R.id.dataInput);
        answerInput = findViewById(R.id.answerNumInput);
        socketConnectBtn = findViewById(R.id.socketConnectBtn);

        socketConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Where1");
                Intent intent = new Intent(getApplicationContext(), SendNumActivity.class);
                //System.out.println("Where2");
                String port =  portInput.getText().toString();
                String answer = answerInput.getText().toString();

                intent.putExtra("Port", Integer.parseInt(port));
                intent.putExtra("Name", dataInput.getText().toString());
                intent.putExtra("Answer", Integer.parseInt(answer));
                startActivity(intent);
            }
        });
    }


    /*
    class SocketThread extends Thread {
        String host; // Server IP
        String name; // name
        DataOutputStream out;


        public SocketThread(String host, String data) {
            this.host = host;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                int port = 5555;
                Socket socket = new Socket(host, port);
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(name);
                outstream.flush();

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                response = (String) instream.readObject();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "서버 응답 : " + response, Toast.LENGTH_LONG).show();
                    }
                });

                socket.close();

            } catch(Exception e) {
                e.printStackTrace();
            }


        }
    }*/
}