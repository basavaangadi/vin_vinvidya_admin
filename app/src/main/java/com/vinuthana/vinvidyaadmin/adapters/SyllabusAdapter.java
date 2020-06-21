package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 1/22/2018.
 */

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.MyViewHolder> {

    JSONArray sylbsArray;
    Context sylbsContext;
    Activity sylbusActivity;
    OnSyllabusClickListener syllabusListnter;
    View view;
    String strDescription,strSubject,strSyllabus,strClass,strChapterName;
    public SyllabusAdapter(JSONArray sylbsArray, Context sylbsContext,Activity sylbusActivity) {
        this.sylbsArray = sylbsArray;
        this.sylbsContext = sylbsContext;
        this.sylbusActivity=sylbusActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = (LayoutInflater) sylbsContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.syllabus_layout, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MyViewHolder(view);
    }

    public void setOnButtonClickListener(OnSyllabusClickListener syllabusListnter){
        this.syllabusListnter=syllabusListnter;
    }

    @Override
    public void onBindViewHolder(SyllabusAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject object=sylbsArray.getJSONObject(position);
            strSubject=object.getString(sylbsContext.getString(R.string.key_Subject));

            strSyllabus=object.getString(sylbsContext.getString(R.string.key_Syllabus));
            strChapterName=object.getString(sylbsContext.getString(R.string.key_ChapterName));
            holder.tvSylbsChapterName.setText(strChapterName);
            if(strSyllabus.equalsIgnoreCase("0")){
                holder.tvDescriptionMsg.setText("No sub-topics for this chapter");

                holder.tvDescriptionMsg.setTextColor(Color.parseColor("#FF8C00"));
            }else {
                holder.tvDescriptionMsg.setText("Click to get the topics of this chapter");
                holder.tvDescriptionMsg.setTextColor(Color.parseColor("#4CAF50"));
            }

            strClass=object.getString(sylbsContext.getString(R.string.key_Class));


//

        } catch (JSONException e) {
            Toast.makeText(sylbsContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return sylbsArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSylbsSubject, tvSylbsClass, tvSylbsChapterName,tvDescriptionMsg;
        CardView crdviewSyllabus;
        public MyViewHolder(View itemView) {
            super(itemView);
          /* tvSylbsSubject = (TextView) itemView.findViewById(R.id.tvSylbsSubject);
            tvSylbsClass = (TextView) itemView.findViewById(R.id.tvSylbsClass);*/
            tvSylbsChapterName = (TextView) itemView.findViewById(R.id.tvSylbsChapterName);
            tvDescriptionMsg = (TextView) itemView.findViewById(R.id.tvDescriptionMsg);
            crdviewSyllabus =  itemView.findViewById(R.id.crdviewSyllabus);
            crdviewSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        strSyllabus= sylbsArray.getJSONObject(getAdapterPosition()).getString(sylbsContext.getString(R.string.key_Syllabus)).toString();
                        strChapterName=sylbsArray.getJSONObject(getAdapterPosition()).getString(sylbsContext.getString(R.string.key_ChapterName)).toString();;
                       strSubject=sylbsArray.getJSONObject(getAdapterPosition()).getString(sylbsContext.getString(R.string.key_Subject)).toString();;

                       if(strSyllabus.equalsIgnoreCase("0")){
                            Toast.makeText(sylbusActivity, "No sub-topics for this chapter", Toast.LENGTH_SHORT).show();
                        }else {
                            diplayDiscription();
                        }



                    } catch (Exception e) {
                        Toast.makeText(sylbusActivity, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        public void diplayDiscription(){
            AlertDialog.Builder builder = new AlertDialog.Builder(sylbusActivity, R.style.MyAlertDialogStyles);
            LinearLayout layout = new LinearLayout(sylbusActivity);
            TextView tvSubject = new TextView(sylbusActivity);
            TextView tvEventDescription = new TextView(sylbusActivity);
            TextView tvSyllabus = new TextView(sylbusActivity);
            TextView tvChapter = new TextView(sylbusActivity);
            TextView tvEventCreated = new TextView(sylbusActivity);
            tvSubject.setText("Subject: " + strSubject);
            tvSubject.setTextColor(Color.WHITE);
            tvSubject.setPadding(0, 25, 0, 25);
            tvSubject.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            tvChapter.setText("Chapter : " + strChapterName);
            tvChapter.setTextColor(Color.WHITE);
            tvChapter.setPadding(0, 25, 0, 25);
            tvChapter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            tvSyllabus.setText("Syllabus: " + strSyllabus);
            tvSyllabus.setTextColor(Color.WHITE);
            tvSyllabus.setPadding(0, 25, 0, 25);
            tvSyllabus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            /*tvEventDescription.setText("Event Description: " + eventDescription);
            tvEventDescription.setTextColor(Color.WHITE);
            tvEventDescription.setPadding(0, 25, 0, 25);
            tvEventDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);*/
            layout.addView(tvSubject);
            layout.addView(tvChapter);
            layout.addView(tvSyllabus);
            layout.addView(tvEventDescription);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 40);
            builder.setView(layout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        }
    }
    public interface OnSyllabusClickListener{
        public void onSyllabusClick(JSONObject syllabusData,int position);

    }
}
