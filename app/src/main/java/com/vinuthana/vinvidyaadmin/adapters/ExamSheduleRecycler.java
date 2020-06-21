package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.ClassWiseExamSchedule;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.SetExamSyllabusActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KISHAN on 16-09-17.
 */

public class ExamSheduleRecycler extends RecyclerView.Adapter<ExamSheduleRecycler.MyViewHolder> {
    JSONArray exmschArray;
    Activity exmschActivity;
    int i;

    String strDate, strTime, strDateTime, strExamSchdlId,strIsSyllabusSet;
    OnExamSchduleClickListener examSchduleClickListener;

    public ExamSheduleRecycler(JSONArray prntntcArray, Activity exmschActivity ) {
        this.exmschArray = prntntcArray;
        this.exmschActivity = exmschActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmschActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exm_sch_card_view_layout, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
           i= holder.getAdapterPosition();

            JSONObject object= exmschArray.getJSONObject(i);

            holder.tvExamExmSchedule.setText("Exam: " + exmschArray.getJSONObject(i).getString(exmschActivity.getString(R.string.key_Exam)));
            holder.tvSubjectExmSchedule.setText("Subject: " + exmschArray.getJSONObject(i).getString(exmschActivity.getString(R.string.key_Subject)));
            /*strDate = exmschArray.getJSONObject(position).getString(exmschActivity.getString(R.string.key_Date));
            strTime = exmschArray.getJSONObject(position).getString(exmschActivity.getString(R.string.key_ExamTime));
            strExamSchdlId = exmschArray.getJSONObject(position).getString(exmschActivity.getString(R.string.key_ExamScheduleId));
            strDateTime = strDate + " " + strTime;*/
            strIsSyllabusSet=object.getString(exmschActivity.getString(R.string.key_IsSyllabusSet));
            strDate = object.getString(exmschActivity.getString(R.string.key_Date));
            strTime = object.getString(exmschActivity.getString(R.string.key_ExamTime));
            strExamSchdlId = object.getString(exmschActivity.getString(R.string.key_ExamScheduleId));
            strDateTime = strDate + " - " + strTime;
            Toast.makeText(exmschActivity ,strExamSchdlId,Toast.LENGTH_LONG);
            holder.tvDateExmSchedule.setText("Date: " + strDateTime);
            holder.tvClassExmSchedule.setText("Class: " + exmschArray.getJSONObject(position).getString(exmschActivity.getString(R.string.key_Clas)));


            holder.btnDeleteExmSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Delete","clicked");
                    try {
                        if (!examSchduleClickListener.equals(null)) {
                            JSONObject object= exmschArray.getJSONObject(position);
                            String strExamScheduleId =object.getString("ExamScheduleId");
                            examSchduleClickListener.onDelete(position,strExamScheduleId);
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
                });
            holder.btnEditExmSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!examSchduleClickListener.equals(null)) {
                            JSONObject object= exmschArray.getJSONObject(position);
                            String strExamScheduleId =object.getString("ExamScheduleId");
                            examSchduleClickListener.onEdit(object,position,strExamScheduleId);
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if(strIsSyllabusSet.equalsIgnoreCase("1")){
                holder.btnSyllabusExamScdule.setText("Sylbs Set");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
                holder.btnSyllabusExamScdule.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                else
                    holder.btnSyllabusExamScdule.setBackgroundColor(Color.parseColor("#FF8C00"));
            }else if(strIsSyllabusSet.equalsIgnoreCase("3")){
                holder.btnSyllabusExamScdule.setText("No Sylbs");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
                    holder.btnSyllabusExamScdule.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                else
                    holder.btnSyllabusExamScdule.setBackgroundColor(Color.parseColor("#FF8C00"));
            }
            else{
                holder.btnSyllabusExamScdule.setText("Sylbs");
            }
            holder.btnSyllabusExamScdule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!examSchduleClickListener.equals(null)) {
                            JSONObject object= exmschArray.getJSONObject(position);
                            String strExamScheduleId =object.getString("ExamScheduleId");
                            String strsylbSet =object.getString("IsSyllabusSet");
                            if(strsylbSet.equalsIgnoreCase("1")){
                                Toast toast=Toast.makeText(exmschActivity, "Syllabus already set", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else if(strsylbSet.equalsIgnoreCase("3")){
                                Toast toast=Toast.makeText(exmschActivity, "No Syllabus for this subject", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }else{
                            examSchduleClickListener.onSyllabus(object,position,strExamScheduleId);
                        }}
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnButtonClickListener(OnExamSchduleClickListener examSchduleClickListener){
        this.examSchduleClickListener=examSchduleClickListener;
    }
    @Override
    public int getItemCount() {
        return exmschArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateExmSchedule, tvSubjectExmSchedule, tvExamExmSchedule, tvClassExmSchedule;
        CardView crdExamScdl;
        LinearLayout lytForExmSchdule;
        Button btnEditExmSchedule,btnDeleteExmSchedule,btnSyllabusExamScdule;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvDateExmSchedule = (TextView) itemView.findViewById(R.id.tvDateExmSchedule);
            tvSubjectExmSchedule = (TextView) itemView.findViewById(R.id.tvSubjectExmSchedule);
            tvClassExmSchedule = (TextView) itemView.findViewById(R.id.tvClassExmSchedule);
            tvExamExmSchedule = (TextView) itemView.findViewById(R.id.tvExamExmSchedule);
            crdExamScdl = (CardView) itemView.findViewById(R.id.crdExamScdl);
            lytForExmSchdule=itemView.findViewById(R.id.lytForExmSchdule);
            btnEditExmSchedule=itemView.findViewById(R.id.btnEditExmSchedule);
            btnDeleteExmSchedule=itemView.findViewById(R.id.btnDeleteExmSchedule);
            btnSyllabusExamScdule=itemView.findViewById(R.id.btnSyllabusExamScdule);

        }
    }
    public interface OnExamSchduleClickListener{
        public void onEdit(JSONObject schduleData,int position,String examscduleId);
        public void onDelete(int position,String examscduleId);
        public void onSyllabus(JSONObject schduleData ,int position,String examscduleId);

    }

}
