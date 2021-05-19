package com.pnusoftlab.otherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    SocketManager manager = null;

    EditText nameInput;
    EditText answerInput;
    HashMap<String, TextView> textNameMap = new HashMap<String, TextView>();
    HashMap<String, TextView> textEnableMap = new HashMap<String, TextView>();
    HashMap<String, Integer> enableMap = new HashMap<String, Integer>();
    Button gotoNextBtn;

    String name;
    String otherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Log in!");

        nameInput = (EditText)findViewById(R.id.nameInput);
        answerInput = (EditText)findViewById(R.id.answerNumInput);
        gotoNextBtn = (Button)findViewById(R.id.goNextActivity);
        //gotoNextBtn.setEnabled(false);
        Log.i("MainActivity", "onCreate()");

        gotoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enableMap.size() == 2 && enableMap.get(name) == 2 && enableMap.get(otherName) == 2) {
                    Toast.makeText(MainActivity.this, "EveryOne is Ready", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Not Ready", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume()");

        manager = SocketManager.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "onPause()");
    }

    public void connectToServer(View v) throws RemoteException {
        Log.i("MainActivity", "connectToServer()");
        manager.connect();

        name = nameInput.getText().toString();
        manager.send(name);
        textNameMap.put(name, (TextView)findViewById(R.id.myTextName));
        textEnableMap.put(name, (TextView)findViewById(R.id.myTextEnable));
        textNameMap.get(name).setText("Name : " + name);
        textEnableMap.get(name).setText("Enable : ");
        enableMap.put(name, (Integer)0);
        Toast.makeText(MainActivity.this, "Connected With Server", Toast.LENGTH_SHORT).show();
        receive();
    }

    public void sendAnswer(View v) throws RemoteException {
        Log.i("MainActivity", "connectToServer()");

        String answer = answerInput.getText().toString();
        manager.send(answer);
        receive();
    }

    public void myReceive(View v) throws RemoteException {
        Log.i("MainActivity", "myReceive()");
        receive();
    }

    public void receive() {
        Log.i("MainActivity", "receive()");

        String buffer = manager.receive();
        if(buffer != null) {
            String [] set = buffer.split(":");
            if(set[0].equals("OTHER")){
                otherName = set[1];
                textNameMap.put(otherName, (TextView)findViewById(R.id.otherTextName));
                textEnableMap.put(otherName, (TextView)findViewById(R.id.otherTextEnable));
                textNameMap.get(otherName).setText("Name : " + otherName);
                textEnableMap.get(otherName).setText("Enable : ");
                enableMap.put(otherName, (Integer)0);

            } else if(set[0].equals("ANSWER")) {
                if(set[1].equals("Fail"))
                    Toast.makeText(MainActivity.this, "Wrong Answer, Try Again", Toast.LENGTH_SHORT).show();
                else if(set[1].equals("Success"))
                    Toast.makeText(MainActivity.this, "Correct Answer, Please wait", Toast.LENGTH_SHORT).show();

            } else if(set[0].equals("ENABLE")) {
                textEnableMap.get(set[1]).setText("Enable : " + set[2]);
                enableMap.put(set[1], Integer.parseInt(set[2]));
            }
        }
    }


}