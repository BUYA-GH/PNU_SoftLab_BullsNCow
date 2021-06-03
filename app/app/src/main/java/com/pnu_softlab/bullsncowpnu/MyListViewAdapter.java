package com.pnu_softlab.bullsncowpnu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {

    ArrayList<listitem> items;
    Context context;

    public MyListViewAdapter(ArrayList<listitem> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listview_custom, viewGroup, false);

        TextView tv_name = view.findViewById(R.id.textview_name);
        tv_name.setText(items.get(i).round);

        TextView tv_gender = view.findViewById(R.id.textview_gender);
        tv_gender.setText(items.get(i).answer);

        TextView tv_phone = view.findViewById(R.id.textview_phone);
        tv_phone.setText(items.get(i).result);

        return view;
    }

}