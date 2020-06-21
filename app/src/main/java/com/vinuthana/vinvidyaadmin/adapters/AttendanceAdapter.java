package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2/12/2018.
 */
public class AttendanceAdapter extends ArrayAdapter<StudentData> implements CompoundButton.OnCheckedChangeListener {
    public SparseBooleanArray mCheckStates;
    AppInfoHolder holder;
    //OnCheckBoxClickListener onCheckBoxClickListener;
    Context context;
    int layoutResourceId;
    ArrayList<StudentData> data;
    JSONArray jsonArray;
    ArrayList<String> selectedStrings = new ArrayList<String>();

    public AttendanceAdapter(Context context, int layoutResourceId, ArrayList<StudentData> data,JSONArray jsonArray) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.jsonArray = jsonArray;
        mCheckStates = new SparseBooleanArray(data.size());
        for (int i = 0; i < data.size(); i++) {
            //default true
            StudentData appinfo = data.get(i);
            Boolean boolPresent=true;
            for (int j=0;j<jsonArray.length();j++) {
                try {
                    if (appinfo.getRollNo().equalsIgnoreCase(jsonArray.getJSONObject(j).getString("RollNo"))) {
                        boolPresent = false;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            mCheckStates.put(i, boolPresent);
        }
    }
   /* public void setOnButtonClickListener(OnCheckBoxClickListener onCheckBoxClickListener){
        this.onCheckBoxClickListener=onCheckBoxClickListener;
    }*/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //AppInfoHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, null);
            holder = new AppInfoHolder();
            holder.rollNo = (TextView) convertView.findViewById(R.id.tvRollNo);
            holder.studentName = (TextView) convertView.findViewById(R.id.tvStudName);
            holder.chkBxStatus = (CheckBox) convertView.findViewById(R.id.chkStatus);
            convertView.setTag(holder);
        } else {
            holder = (AppInfoHolder) convertView.getTag();
        }

        StudentData appinfo = data.get(position);
        holder.studentName.setText(appinfo.getName());
        holder.rollNo.setText(appinfo.getRollNo());
        // holder.chkSelect.setChecked(true);
        holder.chkBxStatus.setTag(position);
        holder.chkBxStatus.setChecked(mCheckStates.get(position));
        holder.chkBxStatus.setOnCheckedChangeListener(this);



        return convertView;
    }

    public boolean isChecked(int position) {

        boolean isChecked=mCheckStates.get(position, true);

        return isChecked;

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        mCheckStates.put((Integer) compoundButton.getTag(), isChecked);

    }
    /*public interface OnCheckBoxClickListener{
        public void checkBoxStatusChanged( int position, boolean noticeId);
        public void onDelete(int position,String noticeId);
    }*/

    static class AppInfoHolder {
        protected TextView studentName;
        protected TextView rollNo;
        protected CheckBox chkBxStatus;
    }
}
