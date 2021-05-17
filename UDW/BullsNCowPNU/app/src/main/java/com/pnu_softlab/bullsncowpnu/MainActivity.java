package com.pnu_softlab.bullsncowpnu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    public TextView eText;

    //pin 관련
    HashMap<String,pin> Pins = new HashMap<>();
    HashMap<String,Marker> Markers = new HashMap<>();
    HashMap<Integer, String> map = new HashMap<>();

    class pin{
        double latitude;
        double longitude;

        pin(double latitude,  double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
    //이하 pin관련해서 받아서 생성
    //
    //아무튼 받아서
    //Utmk utmk = Utmk.valueOf(new LatLng(받은거위도, 받은거경도));
    //넣을때 pin.latitude = utmk.x;
    // pin.longitude = utmk.y;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //위에서 받아서 스트링만들고 그거 출력
        eText = (TextView)findViewById(R.id.textView);
        //eText.setText();


        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(35.230974, 129.082301),
                0,
                0,
                0
        );

        NaverMapOptions options = new NaverMapOptions()
                .camera(cameraPosition)
                .locationButtonEnabled(true);


        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        pin rainbow = new pin(35.2300507,129.0828376);
        Pins.put("rainbow", rainbow);
        pin north = new pin(35.2355016, 129.0828778);
        Pins.put("north", north);
        pin lib1 = new pin(35.23568843,129.0813636);
        Pins.put("lib1", lib1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        naverMap.setMinZoom(15.0);
        naverMap.setMaxZoom(18.0);
        naverMap.setExtent(new LatLngBounds(new LatLng(35.230684, 129.075435), new LatLng(35.237382,129.084256)));

        Iterator<Map.Entry<String, pin>> entries = Pins.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry<String, pin> entry = entries.next();
            Marker marker = new Marker();
            marker.setPosition(new LatLng(entry.getValue().latitude, entry.getValue().longitude));
            marker.setMap(naverMap);
            Markers.put(entry.getKey(), marker);
        }
        //Marker marker = new Marker();
        //marker.setPosition(new LatLng(37.5670135, 126.9783740));
        //marker.setMap(naverMap);


        //gps로 위치추적하는거 만들어야되
        //이거 핀찾는데에 쓸수있을거 같음
        naverMap.addOnLocationChangeListener(location ->
        {
            naverMap.setLocationSource(locationSource);
            Utmk locationUtmk = Utmk.valueOf(new LatLng(location.getLatitude(),location.getLongitude()));
            Iterator<Map.Entry<String, pin>> entriesA = Pins.entrySet().iterator();
            while(entriesA.hasNext()){
                Map.Entry<String, pin> entry = entriesA.next();
                Utmk entryUtmk = Utmk.valueOf(new LatLng(entry.getValue().latitude, entry.getValue().longitude));
                if(Math.pow(entryUtmk.x - locationUtmk.x, 2) + Math.pow(entryUtmk.y - locationUtmk.y, 2) < 100) {
                    //공격권주고 서버로 처리
                    //갔다와서 이거 치우기
                    //Pins.remove(entry.getKey());
                    //Markers.get(entry.getKey()).setMap(null);
                    //Markers.remove(entry.getKey());
                    //
                    Toast.makeText(MainActivity.this, "Correct Answer, Please wait", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}