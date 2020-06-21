package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.ViewGroup.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.SingleViewImageAdapter;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Krish on 02-12-2017.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    JSONArray jsonArray;
    int[] rainbow;

    public GridViewAdapter(Context c, JSONArray jsonArray) {
        mContext = c;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
        //  Resources r = mContext.getResources();
        //int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics()); // i have bottom tabbar so yf you dont have any thing like this just leave 150 to 0.I think in your case height of image view an your top(Pifer)
        //this change height of rcv
        DisplayMetrics displaymetrics = mContext.getResources().getDisplayMetrics();
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
       /* LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = ((21 * height) / 100);
        params.width = (width) / 3;//height recycleviewer (there are 5 rows so divide by 5 but i think in your case there are 4 rows so divide by 4)
        //params.setMargins(0,50,0,50);
        convertView.setLayoutParams(params);*/
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);
        try {
            Picasso.with(mContext).load(jsonArray.getJSONObject(position).getString(mContext.getString(R.string.key_Image))).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,SingleViewImageActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("array_list",jsonArray.toString());
                mContext.startActivity(intent);
            }
        });


        //  textView.setTextColor(rainbow[position]);


     return convertView;
       // return rowView;
    }
}