package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Created by Kishan on 25-02-2018.
 */

public class StudAdapter extends ArrayAdapter<StudentData> implements CompoundButton.OnCheckedChangeListener {
    public SparseBooleanArray mCheckStates;

    Context context;
    int layoutResourceId;
    ArrayList<StudentData> data;

    public StudAdapter(Context context, int layoutResourceId, ArrayList<StudentData> data) {
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        mCheckStates.put((Integer) compoundButton.getTag(), isChecked);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        StudHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId,  null);
            holder = new StudHolder();
            holder.studId = (TextView) convertView.findViewById(R.id.tvRollNo);
            holder.studName = (TextView) convertView.findViewById(R.id.tvStudName);
            holder.chkBxStatus = (CheckBox) convertView.findViewById(R.id.chkStatus);
            convertView.setTag(holder);
        } else {
            holder = (StudHolder) convertView.getTag();
        }
        StudentData appinfo = data.get(position);
        holder.studName.setText(appinfo.getName());
        holder.studId.setText(appinfo.getRollNo());
        // holder.chkSelect.setChecked(true);
        holder.chkBxStatus.setTag(position);
        holder.chkBxStatus.setChecked(mCheckStates.get(position));
        //holder.chkBxStatus.setChecked(mCheckStates.get(position, true));
        holder.chkBxStatus.setOnCheckedChangeListener(this);
        return convertView;
    }

    static class StudHolder {
        protected TextView studName;
        protected TextView studId;
        protected CheckBox chkBxStatus;
    }
}
