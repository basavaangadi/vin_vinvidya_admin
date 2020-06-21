package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import java.util.ArrayList;

public class SpinnerGradeDropdownAdapter extends BaseAdapter {
    Context context;
    ArrayList< String> hashMapArrayList;

    public SpinnerGradeDropdownAdapter(Context context, ArrayList<String> hashMapArrayList) {
        this.context = context;
        this.hashMapArrayList = hashMapArrayList;
    }

    @Override
    public int getCount() {
        return hashMapArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return hashMapArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        try {
            if (convertView == null) {

                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.spinner_grade_adapter, null);
                holder = new ViewHolder();
                holder.tViewGrade = (TextView) view.findViewById(R.id.tView_grade);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            if (position!=0) {
                holder.tViewGrade.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.tViewGrade.setTextColor(context.getResources().getColor(R.color.card_text));
            }
            holder.tViewGrade.setText(hashMapArrayList.get(position));
        }  catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    class ViewHolder {
        TextView tViewGrade;
    }
}


