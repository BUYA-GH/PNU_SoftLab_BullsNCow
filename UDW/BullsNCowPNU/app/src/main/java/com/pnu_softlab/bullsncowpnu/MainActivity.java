package com.pnu_softlab.bullsncowpnu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
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

        pin lib1 = new pin(35.233903,129.078703);
        Pins.put("lib1", lib1);//중앙
        pin lib2 = new pin(35.235681,129.0816332);
        Pins.put("lib2", lib2);//새벽별
        pin playground = new pin(35.231735, 129.082916);
        Pins.put("Playground", playground);//넉터
        pin gonguri = new pin(35.231510, 129.080128);
        Pins.put("gonguri", gonguri);//건설관
        pin tesla  = new pin(35.233170,129.08377);
        Pins.put("tesla", tesla);//항공
        pin inmoon = new pin(35.231859, 129.081144);
        Pins.put("inmoon", inmoon);//인문
        pin hall1 = new pin(35.235318, 129.080495);
        Pins.put("hall1", hall1);//금정회관
        pin hall2 = new pin(35.235325,129.076707);
        Pins.put("hall2", hall2);//학생회관
        pin hall3 = new pin(35.234214, 129.079518);
        Pins.put("hall3", hall3);//샛별회관
        pin hall4 = new pin(35.233923, 129.081941);
        Pins.put("hall4", hall4);//문창회관
        pin art = new pin(35.232564, 129.077607);
        Pins.put("art", art);//예술관
        pin bio = new pin(35.234542, 129.081106);
        Pins.put("bio", bio);//생물관
        pin out_Glo = new pin(35.235861, 129083604);
        Pins.put("out_Glo",out_Glo);//언어교육원
        pin law = new pin(35.236746, 129.078729);
        Pins.put("law", law);//법학관
        pin tower = new pin(35.230972,129.0813167);
        Pins.put("tower", tower);//웅비의 탑
        pin slave = new pin(35.234987,129.082492);
        Pins.put("slave", slave);//실험동및 전산원
        pin heo_polit = new pin(35.236312,129.079920);
        Pins.put("polit",heo_polit);//경영관
        pin chem = new pin(35.235166,129.077992);
        Pins.put("chem", chem);//화학관
        pin drug = new pin(35.232568,129.078467);
        Pins.put("drug", drug);//약학관
        pin bio_en = new pin(35.2337672137374, 129.08083534111876);
        Pins.put("bio_en", bio_en);//자연과학관
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

        naverMap.setMinZoom(14.0);
        naverMap.setMaxZoom(18.0);
        naverMap.setExtent(new LatLngBounds(new LatLng(35.230684, 129.075435), new LatLng(35.237382,129.084256)));

        Iterator<Map.Entry<String, pin>> entries = Pins.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry<String, pin> entry = entries.next();
            Marker marker = new Marker();
            marker.setPosition(new LatLng(entry.getValue().latitude, entry.getValue().longitude));
            marker.setWidth(70);
            marker.setHeight(70);
            marker.setIconTintColor(Color.RED);
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