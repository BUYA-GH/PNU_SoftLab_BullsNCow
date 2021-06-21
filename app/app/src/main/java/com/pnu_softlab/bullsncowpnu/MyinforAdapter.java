package com.pnu_softlab.bullsncowpnu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import org.w3c.dom.Text;

public class MyinforAdapter extends InfoWindow.DefaultViewAdapter {


    private final Context context;
    private final ViewGroup viewGroup;
    private  String makerName;

    public MyinforAdapter(@NonNull Context context, ViewGroup viewGroup, String markerName) {
        super(context);
        this.context = context;
        this.viewGroup = viewGroup;
        this.makerName = markerName;
    }


    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow) {
        View view = (View) LayoutInflater.from(context).inflate(R.layout.info_custom, viewGroup, false);

        TextView textView = view.findViewById(R.id.infoTitle);
        ImageView imageView = view.findViewById(R.id.infoImage);

        textView.setText(makerName);
        if(makerName.equals("rainbow"))
            imageView.setImageResource(R.drawable.rainbow);
        else if(makerName.equals("north"))
            imageView.setImageResource(R.drawable.north);
        else if(makerName.equals("mainlib"))
            imageView.setImageResource(R.drawable.mainlib);
        else if(makerName.equals("seclib"))
            imageView.setImageResource(R.drawable.seclib);
        else if(makerName.equals("playground"))
            imageView.setImageResource(R.drawable.playground);
        else if(makerName.equals("architecture"))
            imageView.setImageResource(R.drawable.architecture);
        else if(makerName.equals("aviation"))
            imageView.setImageResource(R.drawable.aviation);
        else if(makerName.equals("humanities"))
            imageView.setImageResource(R.drawable.humanities);
        else if(makerName.equals("geumjung"))
            imageView.setImageResource(R.drawable.geumjung);
        else if(makerName.equals("student"))
            imageView.setImageResource(R.drawable.student);
        else if(makerName.equals("lucifer"))
            imageView.setImageResource(R.drawable.lucifer);
        else if(makerName.equals("munchang"))
            imageView.setImageResource(R.drawable.munchang);
        else if(makerName.equals("art"))
            imageView.setImageResource(R.drawable.art);
        else if(makerName.equals("biology"))
            imageView.setImageResource(R.drawable.biology);
        else if(makerName.equals("language"))
            imageView.setImageResource(R.drawable.language);
        else if(makerName.equals("law"))
            imageView.setImageResource(R.drawable.law);
        else if(makerName.equals("tower"))
            imageView.setImageResource(R.drawable.tower);
        else if(makerName.equals("studies"))
            imageView.setImageResource(R.drawable.studies);
        else if(makerName.equals("business"))
            imageView.setImageResource(R.drawable.business);
        else if(makerName.equals("chemical"))
            imageView.setImageResource(R.drawable.chemical);
        else if(makerName.equals("industry"))
            imageView.setImageResource(R.drawable.industry);
        else if(makerName.equals("nature"))
            imageView.setImageResource(R.drawable.nature);

        return view;
    }


}
