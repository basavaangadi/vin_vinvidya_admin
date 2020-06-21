package com.vinuthana.vinvidyaadmin.adapters.DayToDayAdpater;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.MyViewHolder> {
    JSONArray studLeaveArray;
    Context studLeaveContext;

    String strRollNo,strStudentName;
    public LeaveRequestAdapter(JSONArray studLeaveArray, Context studLeaveContext) {
        this.studLeaveArray = studLeaveArray;
        this.studLeaveContext = studLeaveContext;
    }

    @Override
    public LeaveRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) studLeaveContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.leave_details_layout, parent,false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(LeaveRequestAdapter.MyViewHolder holder, int position) {
        try {




            JSONObject object=studLeaveArray.getJSONObject(position);
            strRollNo=object.getString(studLeaveContext.getString(R.string.key_RollNo));
            strStudentName=object.getString(studLeaveContext.getString(R.string.key_StudentName));
            strStudentName=strRollNo+" "+strStudentName;
            holder.tvStudLeaveDispTitle.setText(object.getString(studLeaveContext.getString(R.string.key_ReasonTitle)));
            holder.tvStudLeaveDispDisc.setText(object.getString(studLeaveContext.getString(R.string.key_Description)));
            holder.tvStudLeaveStudentName.setText(strStudentName);
            holder.tvStudLeaveDispToDate.setText("To : " + object.getString(studLeaveContext.getString(R.string.key_ToDate)));
            holder.tvStudLeaveDispFromDate.setText("From : " +object.getString(studLeaveContext.getString(R.string.key_FromDate)));






        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return studLeaveArray.length();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudLeaveDispTitle, tvStudLeaveDispDisc, tvStudLeaveDispToDate;
        TextView tvStudLeaveStudentName, tvStudLeaveDispFromDate;
        CardView crdviewStudLeaveDisp;


        public MyViewHolder(View myView) {
            super(myView);
            tvStudLeaveDispTitle =  myView.findViewById(R.id.tvStudLeaveDispTitle);
            tvStudLeaveDispDisc = myView.findViewById(R.id.tvStudLeaveDispDisc);
            tvStudLeaveStudentName = myView.findViewById(R.id.tvStudLeaveStudentName);
            tvStudLeaveDispToDate = myView.findViewById(R.id.tvStudLeaveDispToDate);
            tvStudLeaveDispFromDate = myView.findViewById(R.id.tvStudLeaveDispFromDate);
            crdviewStudLeaveDisp =  myView.findViewById(R.id.crdviewStudLeaveDisp);


        }
    }


}