package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
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
import org.json.JSONObject;

public class ExamSheduleRecyclerBelowKitKat extends RecyclerView.Adapter<ExamSheduleRecyclerBelowKitKat.MyViewHolder> {
        JSONArray exmschArray;
        Activity exmschActivity;
        int i;
        //int roleId;
        String strDate, strTime, strDateTime, strExamSchdlId,strIsSyllabusSet;
        OnExamSchduleClickListener examSchduleClickListener;

public ExamSheduleRecyclerBelowKitKat(JSONArray prntntcArray, Activity exmschActivity ) {
        this.exmschArray = prntntcArray;
        this.exmschActivity = exmschActivity;
        //this.roleId=roleId;
        }

@Override
public ExamSheduleRecyclerBelowKitKat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmschActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exm_sch_card_view_layout_below_kitkat, parent,false);
        return new MyViewHolder(view);
        }

@Override
public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
        i= holder.getAdapterPosition();

        JSONObject object= exmschArray.getJSONObject(i);

        holder.tvExamExmScheduleBlwKitKat.setText("Exam: " + exmschArray.getJSONObject(i).getString(exmschActivity.getString(R.string.key_Exam)));
        holder.tvSubjectExmScheduleBlwKitKat.setText("Subject: " + exmschArray.getJSONObject(i).getString(exmschActivity.getString(R.string.key_Subject)));
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
        holder.tvDateExmScheduleBlwKitKat.setText("Date: " + strDateTime);
        holder.tvClassExmScheduleBlwKitKat.setText("Class: " + exmschArray.getJSONObject(position).getString(exmschActivity.getString(R.string.key_Clas)));
        /*if(roleId==1){
        holder.lytForExmSchduleBlwKitKat.setVisibility(View.VISIBLE);
        }else {
        holder.lytForExmSchduleBlwKitKat.setVisibility(View.GONE);
        }*/
            if(strIsSyllabusSet.equalsIgnoreCase("1")){
                holder.btnSyllabusExamScduleBlwKitKat.setText("Sylbs Set");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    holder.btnSyllabusExamScduleBlwKitKat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                else{
                    holder.btnSyllabusExamScduleBlwKitKat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
            }else if(strIsSyllabusSet.equalsIgnoreCase("3")){
                holder.btnSyllabusExamScduleBlwKitKat.setText("No Sylbs");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    holder.btnSyllabusExamScduleBlwKitKat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                else{
                    holder.btnSyllabusExamScduleBlwKitKat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
            }
            else{
                holder.btnSyllabusExamScduleBlwKitKat.setText("Set Sylbs");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    holder.btnSyllabusExamScduleBlwKitKat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_blue));
                else{
                    holder.btnSyllabusExamScduleBlwKitKat.setBackgroundColor(Color.parseColor("#cd036ce5"));
                }

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnEditExmScheduleBlwKitKat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_green));
                holder.btnDeleteExmScheduleBlwKitKat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));

            } else {
                holder.btnDeleteExmScheduleBlwKitKat.setBackgroundColor(Color.parseColor("#FF8C00"));
                holder.btnEditExmScheduleBlwKitKat.setBackgroundColor(Color.parseColor("#4CAF50"));

            }
        holder.btnDeleteExmScheduleBlwKitKat.setOnClickListener(new View.OnClickListener() {
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


        holder.btnEditExmScheduleBlwKitKat.setOnClickListener(new View.OnClickListener() {
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
        Log.e("onedit exception", e.toString());
        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
        }
        }
        });



        holder.btnSyllabusExamScduleBlwKitKat.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        try {
        if (!examSchduleClickListener.equals(null)) {
        JSONObject object= exmschArray.getJSONObject(position);
        String strExamScheduleId =object.getString("ExamScheduleId");
        String isSylbusSet =object.getString(exmschActivity.getString(R.string.key_IsSyllabusSet));
        if(isSylbusSet.equalsIgnoreCase("1")){
           Toast toast= Toast.makeText(exmschActivity, "Syllabus already set", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if(isSylbusSet.equalsIgnoreCase("3")){
            Toast toast=Toast.makeText(exmschActivity, "No Syllabus for this subject", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        else{
            examSchduleClickListener.onSyllabus(object,position,strExamScheduleId);
        }
        }
        }catch (Exception e)
        {
        Log.e("onedit exception", e.toString());
        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
        }
        }
        });

        } catch (Exception e) {
       Log.e("onEdit",e.toString());
            Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
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
    TextView tvDateExmScheduleBlwKitKat, tvSubjectExmScheduleBlwKitKat, tvExamExmScheduleBlwKitKat, tvClassExmScheduleBlwKitKat;
    CardView crdExamScdlBlwKitKat;
    LinearLayout lytForExmSchduleBlwKitKat;
    Button btnEditExmScheduleBlwKitKat,btnDeleteExmScheduleBlwKitKat,btnSyllabusExamScduleBlwKitKat;
    public MyViewHolder(View itemView) {
        super(itemView);
        tvDateExmScheduleBlwKitKat = (TextView) itemView.findViewById(R.id.tvDateExmScheduleBlwKitKat);
        tvSubjectExmScheduleBlwKitKat = (TextView) itemView.findViewById(R.id.tvSubjectExmScheduleBlwKitKat);
        tvClassExmScheduleBlwKitKat = (TextView) itemView.findViewById(R.id.tvClassExmScheduleBlwKitKat);
        tvExamExmScheduleBlwKitKat = (TextView) itemView.findViewById(R.id.tvExamExmScheduleBlwKitKat);
        crdExamScdlBlwKitKat = (CardView) itemView.findViewById(R.id.crdExamScdlBlwKitKat);
        lytForExmSchduleBlwKitKat=itemView.findViewById(R.id.lytForExmSchduleBlwKitKat);
        btnEditExmScheduleBlwKitKat=itemView.findViewById(R.id.btnEditExmScheduleBlwKitKat);
        btnDeleteExmScheduleBlwKitKat=itemView.findViewById(R.id.btnDeleteExmScheduleBlwKitKat);
        btnSyllabusExamScduleBlwKitKat=itemView.findViewById(R.id.btnSyllabusExamScduleBlwKitKat);


        tvDateExmScheduleBlwKitKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)exmschActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvDateExmScheduleBlwKitKat.getText());
                Toast.makeText(exmschActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });
        tvSubjectExmScheduleBlwKitKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)exmschActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvSubjectExmScheduleBlwKitKat.getText());
                Toast.makeText(exmschActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });
        tvClassExmScheduleBlwKitKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)exmschActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvClassExmScheduleBlwKitKat.getText());
                Toast.makeText(exmschActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });
        tvExamExmScheduleBlwKitKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)exmschActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvExamExmScheduleBlwKitKat.getText());
                Toast.makeText(exmschActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
public interface OnExamSchduleClickListener{
    public void onEdit(JSONObject schduleData, int position, String examscduleId);
    public void onDelete(int position,String examscduleId);
    public void onSyllabus(JSONObject schduleData ,int position,String examscduleId);

}

}