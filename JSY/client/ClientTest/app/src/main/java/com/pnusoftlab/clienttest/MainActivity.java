package com.pnusoftlab.clienttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    EditText nameInput;
    EditText answerInput;
    Button socketConnectBtn;
    Button answerSendBtn;

    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;

    String name, answer, buffer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Log in!");

        nameInput = (EditText)findViewById(R.id.nameInput);
        answerInput = (EditText)findViewById(R.id.answerNumInput);
        socketConnectBtn = (Button)findViewById(R.id.socketConnectBtn);
        answerSendBtn = (Button)findViewById(R.id.inputAnswerBtn);
        answerSendBtn.setEnabled(false);

        socketConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            socket = new Socket("211.109.68.18",7777);
                            in = new DataInputStream(socket.getInputStream());
                            out = new DataOutputStream(socket.getOutputStream());

                            out.writeUTF(name);

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Connected With Server", Toast.LENGTH_SHORT).show();
                                    answerSendBtn.setEnabled(true);
                                }
                            });
                            out.flush();

                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        while(true) {
                            try{
                                buffer = in.readUTF();

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(buffer.equals("FailAnswer"))
                                            Toast.makeText(MainActivity.this, "Wrong Answer, Try Again", Toast.LENGTH_SHORT).show();
                                        else if(buffer.equals("SuccessAnswer"))
                                            Toast.makeText(MainActivity.this, "Correct Answer, Please wait", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        answerSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = answerInput.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            out.writeUTF(answer);
                            out.flush();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}