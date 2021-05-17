package com.pnusoftlab.clienttest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    final int STATUS_DISCONNECTED = 0;
    final int STATUS_CONNECTED = 1;

    String ip = "192.168.1.109";
    String name = null;
    SocketManager manager = null;

    EditText nameInput;
    EditText answerInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameInput = findViewById(R.id.nameInput);
        answerInput = findViewById(R.id.answerNumInput);
        Log.i("MainActivity", "onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume()");
        // get SocketManager instance
        manager = SocketManager.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void connectToServer(View v) throws RemoteException {
        manager.setSocket(ip);
        manager.connect();

        name = nameInput.getText().toString();
        manager.send(name);
    }

    public void sendAnswer(View v) throws RemoteException {
        if(manager.getStatus() == STATUS_CONNECTED){
            String answer = answerInput.getText().toString();
            manager.send(answer);
        } else {
            Toast.makeText(this, "not connected to server", Toast.LENGTH_SHORT).show();
        }
    }


    /*
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
                            socket = new Socket("192.168.1.109",7777);
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
    }*/
}