package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/31/2018.
 */

public class StudentAdapterList extends ArrayAdapter<StudentModel> {

    JSONArray array;
    Context mContext;
    View view;
    ArrayList<StudentModel> stdList;

    public StudentAdapterList(Context mContext, int resource, ArrayList<StudentModel> stdList) {
        super(mContext, resource, stdList);
        this.mContext = mContext;
        this.stdList = stdList;
    }

    static class ViewHolder {
        protected TextView studName;
        protected TextView rollNo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, null);
            viewHolder = new ViewHolder();

            viewHolder.studName = (TextView) convertView.findViewById(R.id.tvStudName);
            viewHolder.rollNo = (TextView) convertView.findViewById(R.id.tvRollNo);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvStudName, viewHolder.studName);
            convertView.setTag(R.id.tvStudName, viewHolder.rollNo);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.studName.setText(stdList.get(position).getStrName());
        viewHolder.rollNo.setText(stdList.get(position).getStrRollNo());
        return convertView;
    }
}
