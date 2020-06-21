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
 * Created by Lenovo on 1/16/2018.
 */

public class ExamSpinnerDataAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    JSONArray array;
    Context context;

    public ExamSpinnerDataAdapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int i) {
        try {

            return array.getJSONObject(i).getString(context.getString(R.string.key_ExamId));
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
    }*/

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder spinnerHolder;
        if (view == null) {
            spinnerHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.spinner_list, viewGroup, false);
            spinnerHolder.spinnerItemList = (TextView) view.findViewById(R.id.list);
            view.setTag(spinnerHolder);
        } else {
            spinnerHolder = (ViewHolder) view.getTag();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = array.getJSONObject(i);
            spinnerHolder.spinnerItemList.setText(jsonObject.getString(context.getString(R.string.key_Exam)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;

    }

    class ViewHolder {
        TextView spinnerItemList;
    }
}
