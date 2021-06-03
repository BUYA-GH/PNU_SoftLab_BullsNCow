package com.pnu_softlab.bullsncowpnu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PopupActivity extends Activity {

    SocketManager manager = null;

    TextView txtText;
    EditText editText;
    Button close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        manager = SocketManager.getInstance();
        //UI 객체생성
        txtText = (TextView) findViewById(R.id.txtText);
        editText = (EditText) findViewById(R.id.etext);
        close = (Button) findViewById(R.id.closeBtn);

        //데이터 가져오기
        Intent intent = getIntent();

        if (intent.getIntExtra("Token", 0) == 1) {
            editText.setVisibility(View.VISIBLE);
            txtText.setVisibility(View.GONE);
            close.setVisibility(View.VISIBLE);

            /*
            여기서부터 숫자야구관련
             */
        } else {
            editText.setVisibility(View.GONE);
            txtText.setVisibility(View.VISIBLE);
            close.setVisibility(View.GONE);
            //서버에서 라운드종료 신호를 받으면  이거 무한루프돌리는게 맞나?
            //어떤조건문 들어갈거임
            //close.performClick();

        }
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        try {
            String myanswer = editText.getText().toString();
            manager.send(myanswer);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
    //그냥 서버로 보내고 형식에 맞으면 activity 종료만 시킬까? 확인->서버에서 이상무->종료->map에서 결과가지고 list에 저장
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            String buffer = intent.getStringExtra("Receive");
            String[] set = buffer.split(":");

            if (set[0].equals("ROUNDF")) {
                Intent closePopup = new Intent();
                setResult(RESULT_OK, closePopup);

                //액티비티(팝업) 닫기
                finish();
            }
            else if(set[0].equals("TOAST")) {
                Toast.makeText(this, set[1], Toast.LENGTH_SHORT).show();
            }
        }
    }
}