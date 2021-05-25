package com.pnu_softlab.bullsncowpnu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    SocketManager manager = null;

    public TextView eText;

    public HashMap<String, Utmk> Pins = new HashMap<String, Utmk>();
    public HashMap<String, Marker> Markers = new HashMap<>();
    private int count = 1;
    private String s = null;
    private String answer = null;

    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MapActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("지도") ;
        tabHost1.addTab(ts1)  ;

        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("전적") ;
        tabHost1.addTab(ts2) ;

        //위에서 받아서 스트링만들고 그거 출력
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        eText = (TextView)findViewById(R.id.textView);
        answer = intent.getStringExtra("answer");
        s = "Round: " + count + "\n" + "Your num is: " + answer;
        eText.setText(s);

        manager = SocketManager.getInstance();

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap)  {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        naverMap.setMinZoom(14.0);
        naverMap.setMaxZoom(18.0);
        naverMap.setExtent(new LatLngBounds(new LatLng(35.230009, 129.074514), new LatLng(35.237382,129.084256)));


        //나중에 서버에서 받은것들만 처리
        Iterator<Map.Entry<String, Utmk>> entries = Pins.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry<String, Utmk> entry = entries.next();
            Marker marker = new Marker();
            LatLng latLng = entry.getValue().toLatLng();
            marker.setPosition(latLng);
            marker.setWidth(70);
            marker.setHeight(70);
            marker.setIconTintColor(Color.RED);

            marker.setMap(naverMap);
            Markers.put(entry.getKey(), marker);
        }


        //gps로 위치추적하는거 만들어야되
        //이거 핀찾는데에 쓸수있을거 같음
        naverMap.addOnLocationChangeListener(location ->
        {
            naverMap.setLocationSource(locationSource);

            String tmp = new String();
            Utmk locationUtmk = Utmk.valueOf(new LatLng(location.getLatitude(),location.getLongitude()));
            Iterator<Map.Entry<String, Utmk>> entriesA = Pins.entrySet().iterator();
            while(entriesA.hasNext()){
                Map.Entry<String, Utmk> entry = entriesA.next();
                if((Math.pow(entry.getValue().x - locationUtmk.x, 2) + Math.pow(entry.getValue().y - locationUtmk.y, 2)) <= Math.pow(15,2)) {
                    Intent intent = new Intent(this, PopupActivity.class);
                    intent.putExtra("data", "Test Popup");
                    startActivityForResult(intent, 1);
                    tmp = entry.getKey();
                    Markers.get(tmp).setMap(null);
                    Markers.remove(entry.getKey());

                    Toast.makeText(MapActivity.this, "Correct Answer, Please wait", Toast.LENGTH_SHORT).show();
                    break;
                }

            }
            Pins.remove(tmp);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                count++;
                s = "Round: " + count + "\n" + "Your num is: " + answer;
                eText.setText(s);
            }
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

            if(set[0].equals("PIN")) {
                double lat = Double.parseDouble(set[2]);
                double longt = Double.parseDouble(set[3]);
                String name = set[1];
                LatLng latLng = new LatLng(lat,longt);
                Utmk utmk = Utmk.valueOf(latLng);

                Pins.put(name, utmk);
            }
        }

    }
}
