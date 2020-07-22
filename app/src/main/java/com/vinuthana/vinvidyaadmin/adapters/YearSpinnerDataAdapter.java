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

public class YearSpinnerDataAdapter extends BaseAdapter {

    JSONArray array;
    Context context;
    LayoutInflater layoutInflater;
    public YearSpinnerDataAdapter(JSONArray array, Context context){
        this.array=array;
        this.context=context;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        return array.length();
    }



    @Override
    public Object getItem(int position) {
        try {
            JSONObject object=array.getJSONObject(position);
            return object.getString("AcademicYearId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView== null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.spinner_list,parent,false);
            viewHolder.spinnerList= convertView.findViewById(R.id.list);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject=array.getJSONObject(position);
            String strClass=jsonObject.getString("AcademicYearRange");
            viewHolder.spinnerList.setText(strClass);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }
    class  ViewHolder{
        TextView spinnerList;
    }}
