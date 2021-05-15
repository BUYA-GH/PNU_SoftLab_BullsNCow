package com.pnusoftlab.clienttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText nameInput;
    EditText answerInput;
    Button socketConnectBtn;
    Button answerSendBtn;

    HashMap<String, TextView> textNameMap = new HashMap<String, TextView>();
    HashMap<String, TextView> textEnableMap = new HashMap<String, TextView>();

    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;

    String name, answer, buffer = null;
    String otherName;

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
                                    textNameMap.put(name, (TextView)findViewById(R.id.myTextName));
                                    textEnableMap.put(name, (TextView)findViewById(R.id.myTextEnable));
                                    textNameMap.get(name).setText("Name : " + name);
                                    textEnableMap.get(name).setText("Enable : ");
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
                                String [] set = buffer.split(":");

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(set[0].equals("OTHER")){
                                            otherName = set[1];
                                            textNameMap.put(otherName, (TextView)findViewById(R.id.otherTextName));
                                            textEnableMap.put(otherName, (TextView)findViewById(R.id.otherTextEnable));
                                            textNameMap.get(otherName).setText("Name : " + otherName);
                                            textEnableMap.get(otherName).setText("Enable : ");
                                        } else if(set[0].equals("ANSWER")) {
                                            if(set[1].equals("Fail"))
                                                Toast.makeText(MainActivity.this, "Wrong Answer, Try Again", Toast.LENGTH_SHORT).show();
                                            else if(set[1].equals("Success"))
                                                Toast.makeText(MainActivity.this, "Correct Answer, Please wait", Toast.LENGTH_SHORT).show();
                                        } else if(set[0].equals("ENABLE")) {
                                            textEnableMap.get(set[1]).setText("Enable : " + set[2]);
                                        }
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