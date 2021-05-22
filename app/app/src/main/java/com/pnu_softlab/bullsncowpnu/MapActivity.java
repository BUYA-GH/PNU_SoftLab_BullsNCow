package com.pnu_softlab.bullsncowpnu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    SocketManager manager = null;

    public TextView eText;

    //pin 관련 나중에 pin지우고 LatLng로 교체하고 밑에 고치는게 나을듯
    public HashMap<String,pin> Pins = new HashMap<>();
    public HashMap<String, Marker> Markers = new HashMap<>();

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
        Log.i("MapActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //위에서 받아서 스트링만들고 그거 출력
        eText = (TextView)findViewById(R.id.textView);
        //eText.setText();


        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(35.230974, 129.082301),
                16,
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
    }

    @Override
    protected void onResume() {
        Log.i("MapActivity", "onResume()");
        super.onResume();
        // get SocketManager instance
        manager = SocketManager.getInstance();
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


        //gps로 위치추적하는거 만들어야되
        //이거 핀찾는데에 쓸수있을거 같음
        naverMap.addOnLocationChangeListener(location ->
        {
            naverMap.setLocationSource(locationSource);

            String tmp = new String();
            Utmk locationUtmk = Utmk.valueOf(new LatLng(location.getLatitude(),location.getLongitude()));
            Iterator<Map.Entry<String, pin>> entriesA = Pins.entrySet().iterator();
            while(entriesA.hasNext()){
                Map.Entry<String, pin> entry = entriesA.next();
                Utmk entryUtmk = Utmk.valueOf(new LatLng(entry.getValue().latitude, entry.getValue().longitude));
                if((Math.pow(entryUtmk.x - locationUtmk.x, 2) + Math.pow(entryUtmk.y - locationUtmk.y, 2)) <= Math.pow(15,2)) {
                    //공격권주고 서버로 처리


                    //
                    Intent intent = new Intent(this, PopupActivity.class);
                    intent.putExtra("data", "Test Popup");
                    startActivityForResult(intent, 1);
                    Markers.get(entry.getKey()).setMap(null);
                    Markers.remove(entry.getKey());
                    tmp = entry.getKey();
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
                String result = data.getStringExtra("result");
                eText.setText(result);
            }
        }
    }
}
