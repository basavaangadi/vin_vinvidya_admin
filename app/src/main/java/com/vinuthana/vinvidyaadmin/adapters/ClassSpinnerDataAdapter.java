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
 * Created by KISHAN on 06-09-17.
 */

public class ClassSpinnerDataAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    JSONArray array;
    Context context;

    public ClassSpinnerDataAdapter(JSONArray array, Context context) {
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
            return array.getJSONObject(position).getString(context.getString(R.string.key_Clas));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == 0){
            tv.setTextColor(Color.GRAY);
        } else {
            tv.setTextColor(Color.BLACK);
        }
        return view;
    }
*/
    @Override
    public long getItemId(int position) {
        return position;
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
            spinnerHolder.spinnerItemList.setText(jsonObject.getString(context.getString(R.string.key_Clas)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView spinnerItemList;
    }
}
