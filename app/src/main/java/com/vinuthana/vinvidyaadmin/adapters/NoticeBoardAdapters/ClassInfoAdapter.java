package com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters;

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

public class ClassInfoAdapter extends ArrayAdapter<ClassData> implements CompoundButton.OnCheckedChangeListener  {
    public SparseBooleanArray mCheckStates;
    Activity context;
    int layoutResourceId;
    ArrayList<ClassData> data;
    boolean truOrFalse;
    public ClassInfoAdapter( Activity context, int layoutResourceId,ArrayList<ClassData> data, boolean truOrFalse) {
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.truOrFalse = truOrFalse;
        mCheckStates= new SparseBooleanArray(data.size());
        for (int i = 0; i < data.size(); i++) {
            //default true
            mCheckStates.put(i, truOrFalse);
        }

    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassInfoHolder holder = new ClassInfoHolder();
        if(convertView==null){
            LayoutInflater inflater=((Activity)context).getLayoutInflater();
            convertView=inflater.inflate(layoutResourceId,null);
            //  holder= new ClassInfoHolder();
            holder.SlNo=convertView.findViewById(R.id.tvSlno);
            holder.classes=convertView.findViewById(R.id.tvClassName);
            holder.chkBxStatus=convertView.findViewById(R.id.chkStatus);
            convertView.setTag(holder);

        }else {
            holder = (ClassInfoHolder) convertView.getTag();
        }
        ClassData classInfo=data.get(position);
        int slno=position+1;
        holder.SlNo.setText(String.valueOf(slno));
        String strClass=classInfo.getClasses();
        holder.classes.setText(strClass);
        holder.chkBxStatus.setTag(position);

        holder.chkBxStatus.setChecked(mCheckStates.get(position));
        holder.chkBxStatus.setOnCheckedChangeListener(this);


        //return super.getView(position, convertView, parent);
        return convertView;
    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        mCheckStates.put((Integer) compoundButton.getTag(), isChecked);
    }
    static class ClassInfoHolder{
        protected TextView classes;
        protected TextView SlNo;
        protected CheckBox chkBxStatus;
    }
}
