package com.pnu_softlab.bullsncowpnu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.geometry.Utmk;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final int STATUS_DISCONNECTED = 0;
    final int STATUS_CONNECTED = 1;
    final int STATUS_READY = 2;

    //String ip = "211.109.68.18";//전승윤
    String ip = "192.168.1.109";//유동운
    String name = null;
    String otherName = null;
    String answer = null;
    SocketManager manager = null;

    EditText nameInput;
    EditText answerInput;

    HashMap<String, TextView> textNameMap = new HashMap<String, TextView>();
    HashMap<String, TextView> textEnableMap = new HashMap<String, TextView>();
    HashMap<String, Integer> clientNumMap = new HashMap<String, Integer>();
    int[] enable = {0, 0};

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
        Log.i("MainActivity", "connectToServer()");
        name = nameInput.getText().toString();
        manager.setSocket(ip);
        manager.connect(name);

        textNameMap.put(name, (TextView) findViewById(R.id.myTextName));
        textEnableMap.put(name, (TextView) findViewById(R.id.myTextEnable));
        clientNumMap.put(name, 0);
        textNameMap.get(name).setText("Name : " + name);
        textEnableMap.get(name).setText("Enable : ");
        Toast.makeText(MainActivity.this, "Connected With Server", Toast.LENGTH_SHORT).show();
    }

    public void sendAnswer(View v) throws RemoteException {
        Log.i("MainActivity", "sendAnswer()");
        if (manager.getStatus() == STATUS_CONNECTED) {
            answer = answerInput.getText().toString();
            manager.send(answer);

        } else {
            Toast.makeText(this, "not connected to server", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoMapActivity(View v)  {
        Log.i("MainActivity", "gotoMapActivity()");
        Log.d("receiver", "Enable is : " + enable[0] + " " + enable[1]);

        if(enable[0] == 2 && enable[1] == 2) {
            try {
                manager.send("GO");
            } catch(RemoteException e) {
                e.printStackTrace();
            }

            // go to MapActivity
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.putExtra("answer",answer);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "All player is not ready!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            String buffer = intent.getStringExtra("Receive");
            Log.d("receiver", "Receive data : " + buffer);
            String[] set = buffer.split(":");

            if (set[0].equals("OTHER")) {
                otherName = set[1];
                textNameMap.put(otherName, (TextView) findViewById(R.id.otherTextName));
                textEnableMap.put(otherName, (TextView) findViewById(R.id.otherTextEnable));
                clientNumMap.put(otherName, 1);
                textNameMap.get(otherName).setText("Name : " + otherName);
                textEnableMap.get(otherName).setText("Enable : ");
                Log.d("receiver", "Enable is : " + enable[0] + " " + enable[1]);
            } else if (set[0].equals("ANSWER")) {
                if (set[1].equals("Fail"))
                    Toast.makeText(MainActivity.this, "Wrong Answer, Try Again", Toast.LENGTH_SHORT).show();
                else if (set[1].equals("Success"))
                    Toast.makeText(MainActivity.this, "Correct Answer, Please wait", Toast.LENGTH_SHORT).show();
            } else if (set[0].equals("ENABLE")) {
                textEnableMap.get(set[1]).setText("Enable : " + set[2]);
                enable[clientNumMap.get(set[1])] = Integer.parseInt(set[2]);
                Log.d("receiver", "Enable is : " + enable[0] + " " + enable[1]);
            }
        }

    }
}