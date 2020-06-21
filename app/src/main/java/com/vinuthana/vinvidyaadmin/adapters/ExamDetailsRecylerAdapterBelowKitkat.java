package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class ExamDetailsRecylerAdapterBelowKitkat extends RecyclerView.Adapter<ExamDetailsRecylerAdapterBelowKitkat.MyViewHolder> {
    JSONArray exmschArray;
    Activity exmschActivity;
    int i;

    String strDate, strTime, strDateTime, strExamSchdlId;
    OnExamDetailsClickListener examSchduleClickListener;

    public ExamDetailsRecylerAdapterBelowKitkat(JSONArray prntntcArray, Activity exmschActivity ) {
        this.exmschArray = prntntcArray;
        this.exmschActivity = exmschActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmschActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exam_status_details_below_kitkat, null);
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
                holder.btnScheduleStatusBelowKitkat.setText("Schedule Set");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnScheduleStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                } else {
                    holder.btnScheduleStatusBelowKitkat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
               /* holder.btnScheduleStatusBelowKitkat.setClickable(false);
                holder.btnScheduleStatusBelowKitkat.setEnabled(false);*/
            }else if(strIsScheduleSet.equalsIgnoreCase("-11")){
                holder.btnScheduleStatusBelowKitkat.setText("Schedule Deleted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnScheduleStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.red_btn_shape));
                } else {
                    holder.btnScheduleStatusBelowKitkat.setBackgroundColor(Color.parseColor("#ff1919"));
                }
               /* holder.btnScheduleStatusBelowKitkat.setClickable(false);
                holder.btnScheduleStatusBelowKitkat.setEnabled(false);*/
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnScheduleStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_blue));
                } else {
                    holder.btnScheduleStatusBelowKitkat.setBackgroundColor(Color.parseColor("#cd036ce5"));
                }
                holder.btnScheduleStatusBelowKitkat.setText("Set Schedule");
            }

            if(strIsSyllabusSet.equalsIgnoreCase("1")){
                holder.btnSyllabusStatusBelowKitkat.setText("Syllabus Set");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                } else {
                    holder.btnSyllabusStatusBelowKitkat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
              }else if(strIsSyllabusSet.equalsIgnoreCase("3")){
                holder.btnSyllabusStatusBelowKitkat.setText("No Syllabus");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                } else {
                    holder.btnSyllabusStatusBelowKitkat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
            }else if(strIsSyllabusSet.equalsIgnoreCase("-11")){
                holder.btnSyllabusStatusBelowKitkat.setText("Syllabus deleted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.red_btn_shape));
                } else {
                    holder.btnSyllabusStatusBelowKitkat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
            }else{
                holder.btnSyllabusStatusBelowKitkat.setText("Set Syllabus");
                final int sdk = Build.VERSION.SDK_INT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnSyllabusStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_blue));
                } else {
                    holder.btnSyllabusStatusBelowKitkat.setBackgroundColor(Color.parseColor("#cd036ce5"));
                }

            }

            if(strIsResultSet.equalsIgnoreCase("2")){
                holder.btnMarksStatusBelowKitkat.setText("Marks \n Set");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnMarksStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_orange));
                }
                else {
                    holder.btnMarksStatusBelowKitkat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
                /*holder.btnMarksStatusBelowKitkat.setClickable(false);
                holder.btnMarksStatusBelowKitkat.setEnabled(false);*/


            }else if(strIsResultSet.equalsIgnoreCase("1")){
                holder.btnMarksStatusBelowKitkat.setText("Set \n Marks");
                final int sdk = Build.VERSION.SDK_INT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnMarksStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_green));
                } else {
                    holder.btnMarksStatusBelowKitkat.setBackgroundColor(Color.parseColor("#4CAF50"));
                }
            }
            else{
                holder.btnMarksStatusBelowKitkat.setText("Set \n Marks");
                final int sdk = Build.VERSION.SDK_INT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.btnMarksStatusBelowKitkat.setBackground(exmschActivity.getResources().getDrawable(R.drawable.button_blue));
                } else {
                    holder.btnMarksStatusBelowKitkat.setBackgroundColor(Color.parseColor("#cd036ce5"));
                }                /*if(strIsScheduleSet.equalsIgnoreCase("0")||strIsSyllabusSet.equalsIgnoreCase("0")){
                    holder.btnMarksStatusBelowKitkat.setClickable(false);
                    holder.btnMarksStatusBelowKitkat.setEnabled(false);
                }*/
            }


            holder.tvSubjectBelowKitkat.setText("Subject : " +exmschArray.getJSONObject(i).getString(exmschActivity.getString(R.string.key_Subject)));


            Toast.makeText(exmschActivity ,strExamSchdlId,Toast.LENGTH_LONG);

            holder.tvClassBelowKitkat.setText("Class : " + exmschArray.getJSONObject(position).getString(exmschActivity.getString(R.string.key_Clas)));


            holder.btnSyllabusStatusBelowKitkat.setOnClickListener(new View.OnClickListener() {
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
                            }else if(syllabusSet.equalsIgnoreCase("1") ){
                                Toast toast= Toast.makeText(exmschActivity,"Syllabus already set",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else if(syllabusSet.equalsIgnoreCase("-11")|| scheduleSet.equalsIgnoreCase("-11")){
                                Toast toast= Toast.makeText(exmschActivity,"Syllabus or schedule is deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                            else if(syllabusSet.equalsIgnoreCase("3") ){
                                Toast toast= Toast.makeText(exmschActivity,"No Syllabus for this subject",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                            else {
                                String strExamScheduleId =object.getString("ExamScheduleId");

                                examSchduleClickListener.onSetSyllabus(object,position);
                            }

                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.btnScheduleStatusBelowKitkat.setOnClickListener(new View.OnClickListener() {
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
                            }else if(scheduleSet.equalsIgnoreCase("-11")){
                                 Toast toast=Toast.makeText(exmschActivity,"Schedule is deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                                 toast.setGravity(Gravity.CENTER,0,0);
                                 toast.show();
                             }else if(strISWorkBookExam.equalsIgnoreCase("1")){
                                Toast toast=Toast.makeText(exmschActivity,"Schedule for this exam cannot be set here ",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else {
                                String strExamScheduleId = object.getString("ExamScheduleId");
                                examSchduleClickListener.onSetSchedule(object, position);
                            }
                            }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmschActivity,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.btnMarksStatusBelowKitkat.setOnClickListener(new View.OnClickListener() {
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
                            }if(scheduleSet.equalsIgnoreCase("-11")||syllabusSet.equalsIgnoreCase("-11")){
                                Toast toast= Toast.makeText(exmschActivity,"Schedule or Syllabus deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                            else if(marksSet.equalsIgnoreCase("2") ){
                                Toast toast= Toast.makeText(exmschActivity,"Marks already set",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }else {

                                String strExamScheduleId = object.getString("ExamScheduleId");
                                examSchduleClickListener.onSetMarks(strExamScheduleId, object, position);
                            }
                            }
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

    public void setOnButtonClickListener(OnExamDetailsClickListener examSchduleClickListener){
        this.examSchduleClickListener=examSchduleClickListener;
    }
    @Override
    public int getItemCount() {
        return exmschArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectBelowKitkat, tvClassBelowKitkat, tvExamExmSchedule, tvClassExmSchedule;
        CardView crdExamScdl;
        LinearLayout lytForExmSchdule;
        Button btnScheduleStatusBelowKitkat,btnSyllabusStatusBelowKitkat,btnMarksStatusBelowKitkat;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvSubjectBelowKitkat = (TextView) itemView.findViewById(R.id.tvSubjectBelowKitkat);
            tvClassBelowKitkat = (TextView) itemView.findViewById(R.id.tvClassBelowKitkat);
            tvClassExmSchedule = (TextView) itemView.findViewById(R.id.tvClassExmSchedule);
            tvExamExmSchedule = (TextView) itemView.findViewById(R.id.tvExamExmSchedule);
            crdExamScdl = (CardView) itemView.findViewById(R.id.crdExamScdl);
            lytForExmSchdule=itemView.findViewById(R.id.lytForExmSchdule);
            btnScheduleStatusBelowKitkat=itemView.findViewById(R.id.btnScheduleStatusBelowKitkat);
            btnSyllabusStatusBelowKitkat=itemView.findViewById(R.id.btnSyllabusStatusBelowKitkat);
            btnMarksStatusBelowKitkat=itemView.findViewById(R.id.btnMarksStatusBelowKitkat);

        }
    }
    public interface OnExamDetailsClickListener{
        public void onSetSchedule(JSONObject schduleData, int position);
        public void onSetSyllabus(JSONObject syllabusData,int position);
        public void onSetMarks(String strExamScheduleId,JSONObject marksData ,int position);

    }

}
