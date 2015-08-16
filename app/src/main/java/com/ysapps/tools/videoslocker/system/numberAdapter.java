package com.ysapps.tools.videoslocker.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysapps.tools.videoslocker.R;


/**
 * Created by B.E.L on 05/11/2014.
 */
public class numberAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context ctx;
    GridView.LayoutParams params;

    public numberAdapter(Context ctx, GridView.LayoutParams params){
        this.inflater = LayoutInflater.from(ctx);;
        this.ctx = ctx;
        this.params = params;

    }
    public int getCount() {
        return 12;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                position++;
                TextView v = (TextView) inflater.inflate(R.layout.number_cell, null);
                convertView = v;
                if (position < 10)
                    v.setText(Integer.toString(position));
                else if (position == 10)
                    v.setText(" ");
                else if (position == 11)
                    v.setText("0");
                else {
                    ImageView view = new ImageView(ctx);
                    view.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_input_delete));
                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    view.setPadding(1,1,1,1);
                    convertView = view;
                }
                convertView.setLayoutParams(params);
            } else
                return convertView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return  convertView;
    }
}
