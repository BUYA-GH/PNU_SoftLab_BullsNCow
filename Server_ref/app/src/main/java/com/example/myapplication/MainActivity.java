package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText addressInput; //호스트 IP 입력상자
    EditText dataInput; //서버로 전송할 데이터 입력상자

    String str;
    String addr;

    String response; //서버 응답

    Handler handler = new Handler(); // 토스트를 띄우기 위한 메인스레드 핸들러 객체 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressInput = findViewById(R.id.addressInput);
        dataInput = findViewById(R.id.dataInput);
        Button socketConnectBtn = findViewById(R.id.socketConnectBtn);

        /*
        버튼을 클릭했을 때
        1. 입력상자의 서버 IP 주소와 전송할 데이터 가져오기
        2. 소켓통신을 위한 스레드의 매개변수로 넣어주어 스레드 객체 생성
        3. 스레드 시작
        */
        socketConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addr = addressInput.getText().toString().trim();
                str = dataInput.getText().toString();
                SocketThread thread = new SocketThread(addr, str);
                thread.start();
            }
        });
    }

    class SocketThread extends Thread{

        String host; // 서버 IP
        String data; // 전송 데이터

        public SocketThread(String host, String data){
            this.host = host;
            this.data = data;
        }

        @Override
        public void run() {

            try{
                int port = 5555; //포트 번호는 서버측과 똑같이
                Socket socket = new Socket(host, port); // 소켓 열어주기
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream()); //소켓의 출력 스트림 참조
                outstream.writeObject(data); // 출력 스트림에 데이터 넣기
                outstream.flush(); // 출력

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream()); // 소켓의 입력 스트림 참조
                response = (String) instream.readObject(); // 응답 가져오기

                /* 토스트로 서버측 응답 결과 띄워줄 러너블 객체 생성하여 메인스레드 핸들러로 전달 */
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "서버 응답 : " + response, Toast.LENGTH_LONG).show();
                    }
                });

                socket.close(); // 소켓 해제

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}