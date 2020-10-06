package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by KISHAN on 16-09-17.
 */

public class ExamSyllabusRecycler extends RecyclerView.Adapter<ExamSyllabusRecycler.MyViewHolder> {
    JSONArray exmsylArray;
    Activity exmsylcContext;
   // String strRoleId;
    String strDate, strTime, strDateTime, strExamSchdlId,strIsMarksSet;
    OnExamSyllabusClickListener examsyllabusClickListener;

    public ExamSyllabusRecycler(JSONArray exmsylArray, Activity exmsylcContext) {
        this.exmsylArray = exmsylArray;
        this.exmsylcContext = exmsylcContext;
     //   this.strRoleId=strRoleId;
    }

    @Override
    public ExamSyllabusRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmsylcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exm_sylbs_card_view_layout, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamSyllabusRecycler.MyViewHolder holder, int position) {
        try {
          int  pos= holder.getAdapterPosition();
            holder.tvExamExmSyllabus.setText("Exam: " + exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Exam)));

            strDate = exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Date));
            strTime = exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_ExamTime));
            strIsMarksSet = exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_IsMarksSet));
            strDateTime = strDate + " - " + strTime;
            holder.tvExamExmDtTime.setText("Exam Time: " + strDateTime);
            holder.tvSubjectExmSyllabus.setText("Subject: " + exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Subject)));
            holder.tvClassExmSyllabus.setText("Class: " + exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Clas)));
            holder.tvSyllabusExmSyllabus.setText("Syllabus: " + exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Syllabus)));
            strExamSchdlId = exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_ExamScheduleId));
            /*if(strIsMarksSet.equalsIgnoreCase("2")){
                holder.btnEnteMarksSyllabus.setText("Mrk Set");
            }else{
                holder.btnEnteMarksSyllabus.setText("set Mrk");
            }*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if(strIsMarksSet.equalsIgnoreCase("0")){
                holder.btnEnteMarksSyllabus.setText(" Set Mrk");
                holder.btnEnteMarksSyllabus.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_blue));
            }else if(strIsMarksSet.equalsIgnoreCase("1")){
                holder.btnEnteMarksSyllabus.setText(" Set Mrk");
                holder.btnEnteMarksSyllabus.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_green));
            }else {
                holder.btnEnteMarksSyllabus.setText("Mrk Set");
                holder.btnEnteMarksSyllabus.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_orange));
            }
            }else{
                if(strIsMarksSet.equalsIgnoreCase("0")){
                    holder.btnEnteMarksSyllabus.setText(" Set Mrk");
                    holder.btnEnteMarksSyllabus.setBackgroundColor(Color.parseColor("#cd036ce5"));
                }else if(strIsMarksSet.equalsIgnoreCase("1")){
                    holder.btnEnteMarksSyllabus.setText(" Set Mrk");
                    holder.btnEnteMarksSyllabus.setBackgroundColor(Color.parseColor("#4CAF50"));
                }else {
                    holder.btnEnteMarksSyllabus.setText("Mrk Set");
                    holder.btnEnteMarksSyllabus.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
                holder.btnEditExmSyllabus.setBackgroundColor(Color.parseColor("#4CAF50"));

                holder.btnDeleteExmSyllabus.setBackgroundColor(Color.parseColor("#FF8C00"));
            }


            /* if(strRoleId.equalsIgnoreCase("1")){
                holder.lytForExmSyllabus.setVisibility(View.VISIBLE);
            }else{
                holder.lytForExmSyllabus.setVisibility(View.GONE);
            }*/
            holder.btnDeleteExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Delete","clicked");
                    try {
                        if (!examsyllabusClickListener.equals(null)) {
                            JSONObject object= exmsylArray.getJSONObject(position);
                            String strExamSyllabusId =object.getString("ExamSyllabusId");
                            examsyllabusClickListener.onDelete(position,strExamSyllabusId);
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmsylcContext,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.btnEditExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!examsyllabusClickListener.equals(null)) {
                            JSONObject object= exmsylArray.getJSONObject(position);
                            String strExamSyllabusId=object.getString("ExamSyllabusId");
                            examsyllabusClickListener.onEdit(object,position,strExamSyllabusId);
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmsylcContext,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.btnEnteMarksSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!examsyllabusClickListener.equals(null)) {
                            JSONObject object= exmsylArray.getJSONObject(position);
                            String strExamScheduleId=object.getString("ExamScheduleId");

                            String  isMrksSet=object.getString(exmsylcContext.getString(R.string.key_IsMarksSet));
                            if(isMrksSet.equalsIgnoreCase("2")){
                               Toast toast= Toast.makeText(exmsylcContext, "Marks already Set", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }else {
                                examsyllabusClickListener.onSetMarks(object, position, strExamScheduleId);
                            }
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(exmsylcContext,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnButtonClickListener(OnExamSyllabusClickListener examsyllabusClickListener){
        this.examsyllabusClickListener=examsyllabusClickListener;
    }

    @Override
    public int getItemCount() {
        return exmsylArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamExmSyllabus, tvSubjectExmSyllabus, tvClassExmSyllabus, tvSyllabusExmSyllabus, tvExamExmDtTime;
        Button btnEditExmSyllabus,btnDeleteExmSyllabus,btnEnteMarksSyllabus;
        LinearLayout lytForExmSyllabus;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvExamExmSyllabus = (TextView) itemView.findViewById(R.id.tvExamExmSyllabus);
            tvSubjectExmSyllabus = (TextView) itemView.findViewById(R.id.tvSubjectExmSyllabus);
            tvClassExmSyllabus = (TextView) itemView.findViewById(R.id.tvClassExmSyllabus);
            tvSyllabusExmSyllabus = (TextView) itemView.findViewById(R.id.tvSyllabusExmSyllabus);
            tvExamExmDtTime = (TextView) itemView.findViewById(R.id.tvExamExmDtTime);
            lytForExmSyllabus=itemView.findViewById(R.id.lytForExmSyllabus);
            btnEditExmSyllabus=itemView.findViewById(R.id.btnEditExmSyllabus);
            btnDeleteExmSyllabus=itemView.findViewById(R.id.btnDeleteExmSyllabus);
            btnEnteMarksSyllabus=itemView.findViewById(R.id.btnEnteMarksSyllabus);


            tvExamExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExamExmSyllabus.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvSubjectExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSubjectExmSyllabus.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvClassExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvClassExmSyllabus.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvSyllabusExmSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSyllabusExmSyllabus.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvExamExmDtTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExamExmDtTime.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
    public interface OnExamSyllabusClickListener{
        public void onEdit(JSONObject syllabusData, int position, String examsylbusId);
        public void onDelete(int position,String examsylbusId);
        public void onSetMarks(JSONObject syllabusData,int position,String examScheduleId);


    }
}
