package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lenovo on 2/28/2018.
 */

public class AtndRecyclerAdapter extends RecyclerView.Adapter<AtndRecyclerAdapter.MyViewHolder> {

    JSONArray atndArray;
    Context mContext;


    public AtndRecyclerAdapter(JSONArray atndArray, Context mContext) {
        this.atndArray = atndArray;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.attendance_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AtndRecyclerAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject object = atndArray.getJSONObject(position);
            holder.tvAtndStdName.setText(object.getString(mContext.getString(R.string.key_StudentName)));
            holder.tvAndRollNo.setText(object.getString(mContext.getString(R.string.key_RollNo))+". ");
            holder.tvAtndClass.setText(object.getString(mContext.getString(R.string.key_Clas)));
            holder.tvAtndDate.setText(object.getString(mContext.getString(R.string.key_Date)));
            String strStatus = object.getString(mContext.getString(R.string.key_Status));
            if (strStatus.equalsIgnoreCase("Present")) {
                holder.tvAtndStatus.setTextColor(Color.parseColor("#00b300"));
                //holder.lynrLyt.setBackgroundColor(Color.parseColor("#00ff00"));
            } else {
                holder.tvAtndStatus.setTextColor(Color.parseColor("#ff0000"));
                //holder.lynrLyt.setBackgroundColor(Color.parseColor("#ff0000"));
            }
            holder.tvAtndStatus.setText(atndArray.getJSONObject(position).getString(mContext.getString(R.string.key_Status)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return atndArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAtndStdName, tvAndRollNo, tvAtndClass, tvAtndDate, tvAtndStatus;
        LinearLayout lynrLyt;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAtndStdName = (TextView) itemView.findViewById(R.id.tvAtndStdName);
            tvAndRollNo = (TextView) itemView.findViewById(R.id.tvAndRollNo);
            tvAtndClass = (TextView) itemView.findViewById(R.id.tvAtndClass);
            tvAtndDate = (TextView) itemView.findViewById(R.id.tvAtndDate);
            tvAtndStatus = (TextView) itemView.findViewById(R.id.tvAtndStatus);
            //lynrLyt=(LinearLayout) itemView.findViewById(R.id.lynrLyt);
        }
    }
}
