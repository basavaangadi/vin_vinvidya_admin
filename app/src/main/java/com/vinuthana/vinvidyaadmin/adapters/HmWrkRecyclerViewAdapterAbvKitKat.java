package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HmWrkRecyclerViewAdapterAbvKitKat extends RecyclerView.Adapter<HmWrkRecyclerViewAdapterAbvKitKat.MyViewHolder> {


    JSONArray hmwrkArray;
    Context hmwrkContext;
    Fragment currentFragment;
    Activity currentActivity;
    OnHomworkViewClickListener listener;

    public HmWrkRecyclerViewAdapterAbvKitKat(JSONArray hmwrkArray, Context hmwrkContext, Fragment currentFragment) {
        this.hmwrkArray = hmwrkArray;
        this.hmwrkContext = hmwrkContext;
        this.currentActivity = (Activity) hmwrkContext;
        this.currentFragment = currentFragment;
    }

    @Override
    public HmWrkRecyclerViewAdapterAbvKitKat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) hmwrkContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        View view = inflater.inflate(R.layout.hmwrk_card_view_layout_abv_kitkat, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HmWrkRecyclerViewAdapterAbvKitKat.MyViewHolder holder, int position) {
        try {
            holder.tvSubjectHmCrdAbvKitKat.setText("Subject: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_Subject)));
            holder.tvHomeWorkHmCrdAbvKitKat.setText("Home Work: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_HomeWork)));
            holder.tvClassHmCrdAbvKitKat.setText("Class: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_Clas)));
            holder.tvChptNameHmCrdAbvKitKat.setText("Chapt. Name: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_chapterName)));
            holder.tvDateHmCrdAbvKitKat.setText("Date: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_Date)));






            holder.btnHmwrkEditAbvKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!listener.equals(null)) {
                        try {
                            int hid = Integer.parseInt(hmwrkArray.getJSONObject(position).getString("HomeworkId"));
                            listener.onEdit(hmwrkArray.getJSONObject(position), position, hid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            holder.btnHmwrkDeleteAbvKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int hid = Integer.parseInt(hmwrkArray.getJSONObject(position).getString("HomeworkId"));
                        if(!listener.equals(null)) {
                            listener.onDelete(hid, position);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.btnHmwrkFedbackAbvKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int hid = Integer.parseInt(hmwrkArray.getJSONObject(position).getString("HomeworkId"));
                        int classid=Integer.parseInt(hmwrkArray.getJSONObject(position).getString("ClassId"));
                        if(!listener.equals(null)) {
                            listener.onFeed(hid, position,classid);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(hmwrkContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.tvSubjectHmCrdAbvKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int subjectId = Integer.parseInt(hmwrkArray.getJSONObject(position).getString("SubjectId"));
                        int classid=Integer.parseInt(hmwrkArray.getJSONObject(position).getString("ClassId"));
                        String strSubject=hmwrkArray.getJSONObject(position).getString("Subject");
                        if(!listener.equals(null)) {
                            listener.onSubjectSyllabus(hmwrkArray.getJSONObject(position),position,classid,strSubject,subjectId);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(hmwrkContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(hmwrkContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        return hmwrkArray.length();
    }


    public void setOnButtonClickListener(OnHomworkViewClickListener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectHmCrdAbvKitKat, tvHomeWorkHmCrdAbvKitKat, tvDateHmCrdAbvKitKat;
        TextView tvChptNameHmCrdAbvKitKat, tvClassHmCrdAbvKitKat, tvHmWrkIdHmCrd, tvClassIdHmCrd;
        CardView crdViewCurHmWrkAbvKitKat;
        Button btnHmwrkDeleteAbvKitKat, btnHmwrkEditAbvKitKat, btnHmwrkFedbackAbvKitKat;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSubjectHmCrdAbvKitKat = itemView.findViewById(R.id.tvSubjectHmCrdAbvKitKat);
            tvHomeWorkHmCrdAbvKitKat = itemView.findViewById(R.id.tvHomeWorkHmCrdAbvKitKat);
            tvDateHmCrdAbvKitKat = itemView.findViewById(R.id.tvDateHmCrdAbvKitKat);
            tvChptNameHmCrdAbvKitKat = itemView.findViewById(R.id.tvChptNameHmCrdAbvKitKat);
            tvClassHmCrdAbvKitKat = itemView.findViewById(R.id.tvClassHmCrdAbvKitKat);

            crdViewCurHmWrkAbvKitKat = itemView.findViewById(R.id.crdViewCurHmWrkAbvKitKat);
            btnHmwrkEditAbvKitKat = itemView.findViewById(R.id.btnHmwrkEditAbvKitKat);
            btnHmwrkDeleteAbvKitKat = itemView.findViewById(R.id.btnHmwrkDeleteAbvKitKat);
            btnHmwrkFedbackAbvKitKat = itemView.findViewById(R.id.btnHmwrkFedbackAbvKitKat);
        }
    }

    public interface OnHomworkViewClickListener {
        void onEdit(JSONObject currentData, int position, int hid);
        void onDelete(int hid, int position);
        void onFeed(int hid, int position,int classId);
        void onSubjectSyllabus(JSONObject currentData, int position,int classId,String strSubject,int subjectId);
    }
}
