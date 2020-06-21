package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2/21/2018.
 */

public class StaffAdapter extends ArrayAdapter<StaffData> implements CompoundButton.OnCheckedChangeListener {

    public SparseBooleanArray mCheckStates;
    Context context;
    int layoutResourceId;
    ArrayList<StaffData> data;

    public StaffAdapter(Context context, int layoutResourceId, ArrayList<StaffData> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        mCheckStates = new SparseBooleanArray(data.size());
        for (int i = 0; i < data.size(); i++) {
            //default true
            mCheckStates.put(i, false);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View row = convertView;
        StaffHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, null);
            holder = new StaffHolder();
            holder.staffId = (TextView) convertView.findViewById(R.id.tvRollNo);
            holder.staffName = (TextView) convertView.findViewById(R.id.tvStudName);
            holder.chkBxStatus = (CheckBox) convertView.findViewById(R.id.chkStatus);
            convertView.setTag(holder);
        } else {
            holder = (StaffHolder) convertView.getTag();
        }
        StaffData appinfo = data.get(position);
        holder.staffName.setText(appinfo.getName());
        holder.staffId.setText(appinfo.getStaffId());
        // holder.chkSelect.setChecked(true);
        holder.chkBxStatus.setTag(position);
        holder.chkBxStatus.setChecked(mCheckStates.get(position));
        //holder.chkBxStatus.setChecked(mCheckStates.get(position, true));
        holder.chkBxStatus.setOnCheckedChangeListener(this);
        return convertView;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        mCheckStates.put((Integer) compoundButton.getTag(), isChecked);
    }

    static class StaffHolder {
        protected TextView staffName;
        protected TextView staffId;
        protected CheckBox chkBxStatus;
    }
}
