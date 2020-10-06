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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExamSyllabusRecyclerBelowKitKat extends RecyclerView.Adapter<ExamSyllabusRecyclerBelowKitKat.MyViewHolder> {
    JSONArray exmsylArray;
    Activity exmsylcContext;
    //String strRoleId;
    String strDate, strTime, strDateTime, strExamSchdlId,strIsMarksSet;
    OnExamSyllabusClickListener examsyllabusClickListener;

    public ExamSyllabusRecyclerBelowKitKat(JSONArray exmsylArray, Activity exmsylcContext) {
        this.exmsylArray = exmsylArray;
        this.exmsylcContext = exmsylcContext;
        //this.strRoleId=strRoleId;
    }

    @Override
    public ExamSyllabusRecyclerBelowKitKat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmsylcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exm_sylbs_card_view_layout_below_kitkat, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamSyllabusRecyclerBelowKitKat.MyViewHolder holder, int position) {
        try {
            int pos=holder.getAdapterPosition();
            JSONObject object= exmsylArray.getJSONObject(pos);
            holder.tvExamExmSyllabusBlwKitKat.setText("Exam: " + exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_Exam)));
            strDate = exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_Date));
            strTime = exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_ExamTime));
            strIsMarksSet=exmsylArray.getJSONObject(position).getString(exmsylcContext.getString(R.string.key_IsMarksSet));

            strDateTime = strDate + " - " + strTime;
            holder.tvExamExmDtTimeBlwKitKat.setText("Exam Time: " + strDateTime);
            holder.tvSubjectExmSyllabusBlwKitKat.setText("Subject: " + exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Subject)));
            holder.tvClassExmSyllabusBlwKitKat.setText("Class: " + exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Clas)));
            holder.tvSyllabusExmSyllabusBlwKitKat.setText("Syllabus: " + exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_Syllabus)));
            strExamSchdlId = exmsylArray.getJSONObject(pos).getString(exmsylcContext.getString(R.string.key_ExamScheduleId));
            /*if(strRoleId.equalsIgnoreCase("1")){
                holder.lytForExmSyllabusBlwKitKat.setVisibility(View.VISIBLE);
            }else{
                holder.lytForExmSyllabusBlwKitKat.setVisibility(View.GONE);
            }*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if(strIsMarksSet.equalsIgnoreCase("0")){
                    holder.btnEnteMarksSyllabusBlwKitKat.setText(" Set Mrk");
                    holder.btnEnteMarksSyllabusBlwKitKat.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_blue));
                }else if(strIsMarksSet.equalsIgnoreCase("1")){
                    holder.btnEnteMarksSyllabusBlwKitKat.setText(" Set Mrk");
                    holder.btnEnteMarksSyllabusBlwKitKat.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_green));
                }else {
                    holder.btnEnteMarksSyllabusBlwKitKat.setText("Mrk Set");
                    holder.btnEnteMarksSyllabusBlwKitKat.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_orange));
                }


                holder.btnEditExmSyllabusBlwKitKat.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_green));
                holder.btnDeleteExmSyllabusBlwKitKat.setBackground(exmsylcContext.getResources().getDrawable(R.drawable.button_orange));


            } else {
                if(strIsMarksSet.equalsIgnoreCase("0")){
                    holder.btnEnteMarksSyllabusBlwKitKat.setText(" Set Mrk");
                    holder.btnEnteMarksSyllabusBlwKitKat.setBackgroundColor(Color.parseColor("#cd036ce5"));
                }else if(strIsMarksSet.equalsIgnoreCase("1")){
                    holder.btnEnteMarksSyllabusBlwKitKat.setText(" Set Mrk");
                    holder.btnEnteMarksSyllabusBlwKitKat.setBackgroundColor(Color.parseColor("#4CAF50"));
                }else {
                    holder.btnEnteMarksSyllabusBlwKitKat.setText("Mrk Set");
                    holder.btnEnteMarksSyllabusBlwKitKat.setBackgroundColor(Color.parseColor("#FF8C00"));
                }
                holder.btnEditExmSyllabusBlwKitKat.setBackgroundColor(Color.parseColor("#4CAF50"));

                holder.btnDeleteExmSyllabusBlwKitKat.setBackgroundColor(Color.parseColor("#FF8C00"));
            }
            holder.btnDeleteExmSyllabusBlwKitKat.setOnClickListener(new View.OnClickListener() {
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

            holder.btnEditExmSyllabusBlwKitKat.setOnClickListener(new View.OnClickListener() {
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

            holder.btnEnteMarksSyllabusBlwKitKat.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(exmsylcContext, e.toString(), Toast.LENGTH_SHORT).show();
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
        TextView tvExamExmSyllabusBlwKitKat, tvSubjectExmSyllabusBlwKitKat, tvClassExmSyllabusBlwKitKat;
        TextView tvSyllabusExmSyllabusBlwKitKat, tvExamExmDtTimeBlwKitKat;
        Button btnEditExmSyllabusBlwKitKat,btnDeleteExmSyllabusBlwKitKat,btnEnteMarksSyllabusBlwKitKat;
        LinearLayout lytForExmSyllabusBlwKitKat;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvExamExmSyllabusBlwKitKat = (TextView) itemView.findViewById(R.id.tvExamExmSyllabusBlwKitKat);
            tvSubjectExmSyllabusBlwKitKat = (TextView) itemView.findViewById(R.id.tvSubjectExmSyllabusBlwKitKat);
            tvClassExmSyllabusBlwKitKat = (TextView) itemView.findViewById(R.id.tvClassExmSyllabusBlwKitKat);
            tvSyllabusExmSyllabusBlwKitKat = (TextView) itemView.findViewById(R.id.tvSyllabusExmSyllabusBlwKitKat);
            tvExamExmDtTimeBlwKitKat = (TextView) itemView.findViewById(R.id.tvExamExmDtTimeBlwKitKat);
            lytForExmSyllabusBlwKitKat=itemView.findViewById(R.id.lytForExmSyllabusBlwKitKat);
            btnEditExmSyllabusBlwKitKat=itemView.findViewById(R.id.btnEditExmSyllabusBlwKitKat);
            btnDeleteExmSyllabusBlwKitKat=itemView.findViewById(R.id.btnDeleteExmSyllabusBlwKitKat);
            btnEnteMarksSyllabusBlwKitKat=itemView.findViewById(R.id.btnEnteMarksSyllabusBlwKitKat);

            tvExamExmSyllabusBlwKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExamExmSyllabusBlwKitKat.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvSubjectExmSyllabusBlwKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSubjectExmSyllabusBlwKitKat.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvClassExmSyllabusBlwKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvClassExmSyllabusBlwKitKat.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvSyllabusExmSyllabusBlwKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSyllabusExmSyllabusBlwKitKat.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvExamExmDtTimeBlwKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmsylcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExamExmDtTimeBlwKitKat.getText());
                    Toast.makeText(exmsylcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
    public interface OnExamSyllabusClickListener{
        public void onEdit(JSONObject syllabusData, int position, String examsylbusId);
        public void onSetMarks(JSONObject syllabusData, int position, String examScheduleId);
        public void onDelete(int position,String examsylbusId);


    }
}
