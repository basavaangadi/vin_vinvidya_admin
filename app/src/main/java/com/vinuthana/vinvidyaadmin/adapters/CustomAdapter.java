package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<StudentModel> {

    private ArrayList<StudentModel> dataSet;
    Context mContext;

    // View lookup cache


    public CustomAdapter(ArrayList<StudentModel> data, Context context) {
        super(context, R.layout.unchecked_list, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public StudentModel getItem(int position) {
        return dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.unchecked_list, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvStudName);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.chkStatus);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        StudentModel item = getItem(position);


        /*viewHolder.txtName.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked_list);*/


        return result;
    }
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }
}
