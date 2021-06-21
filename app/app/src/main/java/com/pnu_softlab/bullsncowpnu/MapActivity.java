package com.pnu_softlab.bullsncowpnu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap Mymap;

    SocketManager manager = null;

    public TextView eText;

    public HashMap<String, Utmk> Pins = new HashMap<String, Utmk>();
    public HashMap<String, Marker> Markers = new HashMap<>();
    private int count = 1;
    private String infor = null;
    private String answer = null;
    private int Token = 0;
    ListView listView = null;
    MyListViewAdapter adapter = null;
    ArrayList<listitem> items = new ArrayList<>();
    InfoWindow infoWindow = new InfoWindow();

    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MapActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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

        infor = "Round : Tutorial\n" + "Your num is : " + answer;

        eText.setText(infor);

        manager = SocketManager.getInstance();

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        listView = findViewById(R.id.history);

        items.add(new listitem("Round", "Answer", "Result"));//대체할까?
        items.add(new listitem("Round1", "---", "---"));
        items.add(new listitem("Round2", "---", "---"));
        items.add(new listitem("Round3", "---", "---"));
        items.add(new listitem("Round4", "---", "---"));
        items.add(new listitem("Round5", "---", "---"));
        items.add(new listitem("Round6", "---", "---"));
        items.add(new listitem("Round7", "---", "---"));
        items.add(new listitem("Round8", "---", "---"));
        items.add(new listitem("Round9", "---", "---"));
        adapter = new MyListViewAdapter(items, getApplicationContext());

        listView.setAdapter(adapter);
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
        Mymap = naverMap;
        try {
            manager.send("Go");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);


        naverMap.setMinZoom(14.0);
        naverMap.setMaxZoom(18.0);
        naverMap.setExtent(new LatLngBounds(new LatLng(35.230009, 129.074514), new LatLng(35.237382,129.084256)));


        //gps로 위치추적하는거 만들어야되
        //이거 핀찾는데에 쓸수있을거 같음
        naverMap.addOnLocationChangeListener(location ->
        {
            //naverMap.setLocationSource(locationSource);

            Utmk locationUtmk = Utmk.valueOf(new LatLng(location.getLatitude(),location.getLongitude()));
            Iterator<Map.Entry<String, Utmk>> entriesA = Pins.entrySet().iterator();
            while(entriesA.hasNext()) {
                Map.Entry<String, Utmk> entry = entriesA.next();
                if ((Math.pow(entry.getValue().x - locationUtmk.x, 2) + Math.pow(entry.getValue().y - locationUtmk.y, 2)) <= Math.pow(15, 2)) {
                    String activePin = "ARRIVE:"+entry.getKey();
                    try {
                        manager.send(activePin);
                        RemoveMarker(entry.getKey());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK);
        }

        try {
            manager.send("NEXT");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
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

                Marker marker = new Marker();
                marker.setPosition(latLng);
                marker.setWidth(70);
                marker.setHeight(70);
                marker.setIconTintColor(Color.RED);
                marker.setMap(Mymap);
                marker.setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        if(marker.getInfoWindow() ==null){
                            ViewGroup rootView = (ViewGroup)findViewById(R.id.map);
                            MyinforAdapter adapter = new MyinforAdapter(MapActivity.this, rootView, name);

                            infoWindow.setAdapter(adapter);
                            infoWindow.open(marker);
                        }
                        else{
                            infoWindow.close();
                        }

                        return false;
                    }
                });
                Pins.put(name, utmk);
                Markers.put(name, marker);
            }
            else if(set[0].equals("UNABLE")){
                RemoveMarker(set[1]);
            }
            else if(set[0].equals("POPUP")){
                Intent popup = new Intent(this, PopupActivity.class);
                popup.putExtra("Token", parseInt(set[1]));
                startActivityForResult(popup, 1);
            }
            else if(set[0].equals("START")) {
                Toast.makeText(MapActivity.this, "Now Game Start!!", Toast.LENGTH_SHORT).show();

                infor = "Round: " + count + "\n" + "Your num is: " + answer;
                eText.setText(infor);

            }
            else if(set[0].equals("RESULT")) {
                Toast.makeText(MapActivity.this, set[1], Toast.LENGTH_SHORT).show();
                items.get(count).setter(set[2],set[1]);
                listView.setAdapter(adapter);
                count++;
                infor = "Round: " + count + "\n" + "Your num is: " + answer;
                eText.setText(infor);
            }
            else if(set[0].equals("END")) {
                Toast.makeText(MapActivity.this, "Game has end", Toast.LENGTH_SHORT).show();

                Intent endGame = new Intent();
                endGame.putExtra("result", set[1]);
                setResult(RESULT_OK, endGame);
                // 종료하는거 넣어야됨
                finish();
            }

        }

    }

    private void RemoveMarker(String s)
    {
        Markers.get(s).setMap(null);
        Markers.remove(s);
        Pins.remove(s);
    }
}
