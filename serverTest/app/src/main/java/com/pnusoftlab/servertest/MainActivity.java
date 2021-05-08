package com.pnusoftlab.servertest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    EditText addressInput; // Host IP inputBox
    EditText dataInput; // inputBox about data which send to server
    Button socketConnectBtn; // button for Send n Connect
    String str;
    String addr;
    String response;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressInput = findViewById(R.id.addressInput);
        dataInput = findViewById(R.id.dataInput);
        socketConnectBtn = findViewById(R.id.socketConnectBtn);

        socketConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addr = addressInput.getText().toString().trim();
                str = dataInput.getText().toString();
                SocketThread thread = new SocketThread(addr, str);
                thread.start();
            }
        });
    }

    class SocketThread extends Thread {
        String host; // Server IP
        String data;

        public SocketThread(String host, String data) {
            this.host = host;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                int port = 5555;
                Socket socket = new Socket(host, port);
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(data);
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
    }
}