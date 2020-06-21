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

public class PeriodSpinnerDataAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    JSONArray array;
    Context context;

    public PeriodSpinnerDataAdapter(JSONArray array, Context context) {
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
            return array.getJSONObject(position).getString(context.getString(R.string.key_Period));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject jsonObject = new JSONObject();
        ViewHolder spinnerHolder;
        if (convertView == null) {
            spinnerHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.spinner_list, parent, false);
            spinnerHolder.spinnerItemList = (TextView) convertView.findViewById(R.id.list);
            convertView.setTag(spinnerHolder);
        } else {
            spinnerHolder = (ViewHolder) convertView.getTag();
        }

        try {
            jsonObject = array.getJSONObject(position);
            spinnerHolder.spinnerItemList.setText(jsonObject.getString(context.getString(R.string.key_Period)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return convertView;
    }
    class ViewHolder {
        TextView spinnerItemList;
    }
}
