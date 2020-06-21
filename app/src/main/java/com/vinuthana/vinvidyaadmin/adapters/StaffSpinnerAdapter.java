package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lenovo on 3/1/2018.
 */

public class StaffSpinnerAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    JSONArray array;
    Context context;
    ArrayList<StaffData> data;
    String strStaffId, strStaffName;

    public StaffSpinnerAdapter(JSONArray array, Context context) {
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
            return array.getJSONObject(position).getString(context.getString(R.string.key_StaffDetailsId));
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        JSONObject jsonObject = new JSONObject();
        ViewHolder spinnerHolder;
        if (convertView == null) {
            spinnerHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.spinner_staff_list, viewGroup, false);
            spinnerHolder.tvStaffId = (TextView) convertView.findViewById(R.id.stafId);
            spinnerHolder.tvStaffName = (TextView) convertView.findViewById(R.id.stafName);
            convertView.setTag(spinnerHolder);
        } else {
            spinnerHolder = (ViewHolder) convertView.getTag();
        }

        try {
            jsonObject = array.getJSONObject(position);
            spinnerHolder.tvStaffId.setText(jsonObject.getString(context.getString(R.string.key_StaffId)));
            spinnerHolder.tvStaffName.setText(jsonObject.getString(context.getString(R.string.key_Name)));
            strStaffName = spinnerHolder.tvStaffName.getText().toString();
            strStaffId = spinnerHolder.tvStaffId.getText().toString();
            strStaffName = strStaffId + strStaffName;
            //Toast.makeText(context, strStaffName, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvStaffId, tvStaffName;
    }
}
