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

    String ip = "192.168.35.105";//유동운
    String name = null;
    String otherName = null;
    SocketManager manager = null;

    EditText nameInput;
    EditText answerInput;

    HashMap<String, TextView> textNameMap = new HashMap<String, TextView>();
    HashMap<String, TextView> textEnableMap = new HashMap<String, TextView>();

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
        name = nameInput.getText().toString();
        manager.setSocket(ip);
        manager.connect(name);

        textNameMap.put(name, (TextView) findViewById(R.id.myTextName));
        textEnableMap.put(name, (TextView) findViewById(R.id.myTextEnable));
        textNameMap.get(name).setText("Name : " + name);
        textEnableMap.get(name).setText("Enable : ");
        Toast.makeText(MainActivity.this, "Connected With Server", Toast.LENGTH_SHORT).show();
    }

    public void sendAnswer(View v) throws RemoteException {
        if (manager.getStatus() == STATUS_CONNECTED) {
            String answer = answerInput.getText().toString();
            manager.send(answer);
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("data", "Test Popup");
            startActivityForResult(intent, 1);

        } else {
            Toast.makeText(this, "not connected to server", Toast.LENGTH_SHORT).show();
        }
    }

    public void receiveData(View v) throws RemoteException {
        if (manager.getStatus() == STATUS_CONNECTED) {
            manager.receive();
        } else {
            Toast.makeText(this, "not connected to server", Toast.LENGTH_SHORT).show();
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
            String[] set = buffer.split(":");

            if (set[0].equals("OTHER")) {
                otherName = set[1];
                textNameMap.put(otherName, (TextView) findViewById(R.id.otherTextName));
                textEnableMap.put(otherName, (TextView) findViewById(R.id.otherTextEnable));
                textNameMap.get(otherName).setText("Name : " + otherName);
                textEnableMap.get(otherName).setText("Enable : ");
            } else if (set[0].equals("ANSWER")) {
                if (set[1].equals("Fail"))
                    Toast.makeText(MainActivity.this, "Wrong Answer, Try Again", Toast.LENGTH_SHORT).show();
                else if (set[1].equals("Success"))
                    Toast.makeText(MainActivity.this, "Correct Answer, Please wait", Toast.LENGTH_SHORT).show();
            } else if (set[0].equals("ENABLE")) {
                textEnableMap.get(set[1]).setText("Enable : " + set[2]);
            }
        }

    }
}