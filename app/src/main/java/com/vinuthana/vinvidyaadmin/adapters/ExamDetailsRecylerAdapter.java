package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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

public class ExamDetailsRecylerAdapter extends RecyclerView.Adapter<ExamDetailsRecylerAdapter.MyViewHolder> {
    JSONArray exmschArray;
    Activity exmschActivity;
    int i;

    String strDate, strTime, strDateTime, strExamSchdlId;
    OnExamDetailsClickListener examSchduleClickListener;

    public ExamDetailsRecylerAdapter(JSONArray prntntcArray, Activity exmschActivity ) {
        this.exmschArray = prntntcArray;
        this.exmschActivity = exmschActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmschActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exam_status_details, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            i= holder.getAdapterPosition();
            String strIsScheduleSet="",strIsSyllabusSet="",strIsResultSet="";
            JSONObject object= exmschArray.getJSONObject(i);
            String strSubject=exmschArray.getJSONObject(i).getString(exmschActivity.getString(R.string.key_Subject));
            strIsScheduleSet=object.getString("ScheduleSet");
            strIsSyllabusSet=object.getString("SyllabusSet");
            strIsResultSet=object.getString("ResultSet");
            if(strIsScheduleSet.equalsIgnoreCase("1")){
                holder.btnScheduleStatus.setText("Schedule Set");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnScheduleStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.delete_btn_shape) );
                } else {
                    holder.btnScheduleStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.delete_btn_shape));
                }
                /*holder.btnScheduleStatus.setClickable(false);
                holder.btnScheduleStatus.setEnabled(false);*/
            } else if(strIsScheduleSet.equalsIgnoreCase("-11")){
                holder.btnScheduleStatus.setText("Schedule Deleted");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnScheduleStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.red_btn_shape) );
                } else {
                    holder.btnScheduleStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.red_btn_shape));
                }
                /*holder.btnScheduleStatus.setClickable(false);
                holder.btnScheduleStatus.setEnabled(false);*/
            }else{
                holder.btnScheduleStatus.setText("Schedule Not Set");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnScheduleStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.feedback_btn_shape) );
                } else {
                    holder.btnScheduleStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.feedback_btn_shape));
                }
            }

            if(strIsSyllabusSet.equalsIgnoreCase("1")){
                holder.btnSyllabusStatus.setText("Syllabus Set");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.delete_btn_shape) );
                } else {
                    holder.btnSyllabusStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.delete_btn_shape));
                }

            } else if(strIsSyllabusSet.equalsIgnoreCase("3")){
                holder.btnSyllabusStatus.setText(" No Syllabus");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.delete_btn_shape) );
                } else {
                    holder.btnSyllabusStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.delete_btn_shape));
                }

            }else if(strIsSyllabusSet.equalsIgnoreCase("-11")){
                holder.btnSyllabusStatus.setText("Syllabus deleted");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.red_btn_shape) );
                } else {
                    holder.btnSyllabusStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.red_btn_shape));
                }
                /*holder.btnScheduleStatus.setClickable(false);
                holder.btnScheduleStatus.setEnabled(false);*/
            }

                else{
                holder.btnSyllabusStatus.setText("Syllabus Not Set");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.feedback_btn_shape) );
                } else {
                   // holder.btnMarksStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.feedback_btn_shape) );
                    holder.btnSyllabusStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.feedback_btn_shape));
                }

            }

            if(strIsResultSet.equalsIgnoreCase("2")) {
                holder.btnMarksStatus.setText("Marks Set");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnMarksStatus.setBackgroundColor(Color.parseColor("#FF8C00"));

                } else {
                    ;//orange
                    holder.btnMarksStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.delete_btn_shape));
                }
            }else if(strIsResultSet.equalsIgnoreCase("1")) {
                holder.btnMarksStatus.setText("Marks not Set");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnMarksStatus.setBackgroundColor(Color.parseColor("#4CAF50"));//green
                } else {

                    holder.btnMarksStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.edit_btn_shape));
                }
            }else{
                holder.btnMarksStatus.setText("Marks Not Set");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnMarksStatus.setBackgroundColor(Color.parseColor("#cd036ce5"));//blue

                } else {
                   // holder.btnMarksStatus.setBackgroundDrawable(ContextCompat.getDrawable(exmschActivity, R.drawable.feedback_btn_shape) );
                    holder.btnMarksStatus.setBackground(ContextCompat.getDrawable(exmschActivity, R.drawable.feedback_btn_shape));
                }

            }


            holder.tvExamStatusSubject.setText("Subject : " +exmschArray.getJSONObject(i).getString(exmschActivity.getString(R.string.key_Subject)));


            Toast.makeText(exmschActivity ,strExamSchdlId,Toast.LENGTH_LONG);

            holder.tvExamStatusClass.setText("Class : " + exmschArray.getJSONObject(position).getString(exmschActivity.getString(R.string.key_Clas)));


            holder.btnSyllabusStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Delete","clicked");
                    try {
                        if (!examSchduleClickListener.equals(null)) {
                            JSONObject object= exmschArray.getJSONObject(position);
                            String scheduleSet=object.getString("ScheduleSet");
                            String syllabusSet=object.getString("SyllabusSet");
                            if(scheduleSet.equalsIgnoreCase("0")){
                                Toast toast= Toast.makeText(exmschActivity,"Set Schedule before you set the Syllabus",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else if(syllabusSet.equalsIgnoreCase("-11")||scheduleSet.equalsIgnoreCase("-11")){
                                Toast toast= Toast.makeText(exmschActivity,"Syllabus or schedule is deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else if(syllabusSet.equalsIgnoreCase("1") ){
                                Toast toast= Toast.makeText(exmschActivity,"Syllabus already set",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else if(syllabusSet.equalsIgnoreCase("3") ){
                                Toast toast= Toast.makeText(exmschActivity,"No Syllabus For this subject",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else {
                                String strExamScheduleId =object.getString("ExamScheduleId");

                                examSchduleClickListener.onSetSyllabus(object,position);
                            }
                            /*String strExamScheduleId =object.getString("ExamScheduleId");
                            examSchduleClickListener.onSetSyllabus(object,position);*/
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.btnScheduleStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!examSchduleClickListener.equals(null)) {

                            JSONObject object= exmschArray.getJSONObject(position);
                            String scheduleSet=object.getString("ScheduleSet");
                            String strISWorkBookExam=object.getString(exmschActivity.getString(R.string.key_IsWorkBookExam));
                            if(scheduleSet.equalsIgnoreCase("1")){
                                Toast toast=Toast.makeText(exmschActivity,"Schedule already set",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else if(strISWorkBookExam.equalsIgnoreCase("1")){
                                Toast toast=Toast.makeText(exmschActivity,"Schedule for this exam cannot be set here ",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            } else if(scheduleSet.equalsIgnoreCase("-11")){
                                Toast toast=Toast.makeText(exmschActivity,"Schedule is deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else {
                                String strExamScheduleId = object.getString("ExamScheduleId");
                                examSchduleClickListener.onSetSchedule(object, position);
                            }
                            /*String strExamScheduleId =object.getString("ExamScheduleId");
                            examSchduleClickListener.onSetSchedule(object,position);*/
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.btnMarksStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!examSchduleClickListener.equals(null)) {
                            JSONObject object= exmschArray.getJSONObject(position);
                                String scheduleSet=object.getString("ScheduleSet");
                                String syllabusSet=object.getString("SyllabusSet");
                                String marksSet=object.getString("ResultSet");
                                if(scheduleSet.equalsIgnoreCase("0")||syllabusSet.equalsIgnoreCase("0")){
                                    Toast toast= Toast.makeText(exmschActivity,"Set Schedule and Syllabus before you set the Marks",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }else if(scheduleSet.equalsIgnoreCase("-11")||syllabusSet.equalsIgnoreCase("-11") ){
                                    Toast toast= Toast.makeText(exmschActivity,"schedule or Syllabus is deleted",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }else if(marksSet.equalsIgnoreCase("2") ){
                                    Toast toast= Toast.makeText(exmschActivity,"Marks already set",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }/*else if(scheduleSet.equalsIgnoreCase("-11")||syllabusSet.equalsIgnoreCase("-11") ){
                                    Toast toast= Toast.makeText(exmschActivity,"schedule or Syllabus is deleted",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }*/else {

                                    String strExamScheduleId = object.getString("ExamScheduleId");
                                    examSchduleClickListener.onSetMarks(strExamScheduleId, object, position);
                                }
                            /*String strExamScheduleId =object.getString("ExamScheduleId");
                            examSchduleClickListener.onSetMarks(strExamScheduleId,object,position);*/
                        }
                    }catch (Exception e)
                    {
                        Log.e("onmarks exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnButtonClickListener(OnExamDetailsClickListener examSchduleClickListener){
        this.examSchduleClickListener=examSchduleClickListener;
    }
    @Override
    public int getItemCount() {
        return exmschArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  tvExamStatusSubject, tvExamStatusClass;

        Button btnScheduleStatus,btnSyllabusStatus, btnMarksStatus;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvExamStatusSubject = (TextView) itemView.findViewById(R.id.tvExamStatusSubject);
            tvExamStatusClass = (TextView) itemView.findViewById(R.id.tvExamStatusClass);



            btnScheduleStatus=itemView.findViewById(R.id.btnScheduleStatus);
            btnSyllabusStatus=itemView.findViewById(R.id.btnSyllabusStatus);
            btnMarksStatus =itemView.findViewById(R.id.btnMarksStatus);

        }
    }
    public interface OnExamDetailsClickListener{
        public void onSetSchedule(JSONObject schduleData, int position);
        public void onSetSyllabus(JSONObject syllabusData,int position);
        public void onSetMarks(String strExamScheduleId,JSONObject marksData ,int position);

    }

}