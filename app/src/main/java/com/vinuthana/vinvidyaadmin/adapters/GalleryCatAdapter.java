package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Krish on 02-12-2017.
 */

public class GalleryCatAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    JSONArray array;
    Context context;

    public GalleryCatAdapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return array.getJSONObject(position).getString("eventTitle");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.gallery_cate_item, parent, false);
            viewHolder.tView_cate_name = (TextView) convertView.findViewById(R.id.tView_cate_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = array.getJSONObject(position);
            viewHolder.tView_cate_name.setText(jsonObject.getString("eventTitle"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView tView_cate_name;
    }
}

