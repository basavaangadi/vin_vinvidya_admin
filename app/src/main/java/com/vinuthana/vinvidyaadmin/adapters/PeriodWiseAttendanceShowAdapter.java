package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class PeriodWiseAttendanceShowAdapter extends RecyclerView.Adapter<PeriodWiseAttendanceShowAdapter.MyViewHolder> {

    JSONArray atndArray;
    Context mContext;


    public PeriodWiseAttendanceShowAdapter(JSONArray atndArray, Context mContext) {
        this.atndArray = atndArray;
        this.mContext = mContext;
    }

    @Override
    public PeriodWiseAttendanceShowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_attendance_period_wise_display, null);
        return new PeriodWiseAttendanceShowAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeriodWiseAttendanceShowAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject object = atndArray.getJSONObject(position);
            holder.tvAtndStdName.setText(object.getString(mContext.getString(R.string.key_StudentName)));
            holder.tvAndRollNo.setText(object.getString(mContext.getString(R.string.key_RollNo))+". ");
            holder.tvAtndClass.setText(object.getString(mContext.getString(R.string.key_Clas)));
            holder.tvAtndDate.setText(object.getString(mContext.getString(R.string.key_Date)));
            holder.tvAtndPeriod.setText("Per: "+object.getString((mContext.getString(R.string.key_Period))));
            String strStatus = object.getString(mContext.getString(R.string.key_Status));
            if (strStatus.equalsIgnoreCase("Present")) {
                holder.tvAtndStatus.setTextColor(Color.parseColor("#00b300"));
                strStatus="P";
                //holder.lynrLyt.setBackgroundColor(Color.parseColor("#00ff00"));
            } else {
                strStatus="A";
                holder.tvAtndStatus.setTextColor(Color.parseColor("#ff0000"));
                //holder.lynrLyt.setBackgroundColor(Color.parseColor("#ff0000"));
            }

            holder.tvAtndStatus.setText(strStatus);
        } catch (Exception e) {
            Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return atndArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAtndStdName, tvAndRollNo, tvAtndClass, tvAtndDate, tvAtndStatus,tvAtndPeriod;
        LinearLayout lynrLyt;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAtndStdName = (TextView) itemView.findViewById(R.id.tvAtndStdName);
            tvAndRollNo = (TextView) itemView.findViewById(R.id.tvAndRollNo);
            tvAtndClass = (TextView) itemView.findViewById(R.id.tvAtndClass);
            tvAtndDate = (TextView) itemView.findViewById(R.id.tvAtndDate);
            tvAtndStatus = (TextView) itemView.findViewById(R.id.tvAtndStatus);
            tvAtndPeriod = (TextView) itemView.findViewById(R.id.tvAtndPeriod);
            //lynrLyt=(LinearLayout) itemView.findViewById(R.id.lynrLyt);
        }
    }
}
