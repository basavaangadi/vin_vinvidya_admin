package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.HomeWorkActivity;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.GiveHmWrkFbActivity;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewCurrentHomeWork;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KISHAN on 16-09-17.
 */

public class HmWrkRecyclerViewAdapter extends RecyclerView.Adapter<HmWrkRecyclerViewAdapter.MyViewHolder> {


    JSONArray hmwrkArray;
    Context hmwrkContext;
    Fragment currentFragment;
    Activity currentActivity;
    OnHomworkViewClickListener listener;

    public HmWrkRecyclerViewAdapter(JSONArray hmwrkArray, Context hmwrkContext, Fragment currentFragment) {
        this.hmwrkArray = hmwrkArray;
        this.hmwrkContext = hmwrkContext;
        this.currentActivity = (Activity) hmwrkContext;
        this.currentFragment = currentFragment;
    }

    @Override
    public HmWrkRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) hmwrkContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        View view = inflater.inflate(R.layout.hmwrk_card_view_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HmWrkRecyclerViewAdapter.MyViewHolder holder, final int position) {
        try {
            holder.tvSubjectHmCrd.setText("Subject: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_Subject)));
            holder.tvHomeWorkHmCrd.setText("Home Work: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_HomeWork)));
            holder.tvClassHmCrd.setText("Class: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_Clas)));
            holder.tvChptNameHmCrd.setText("Chapt. Name: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_chapterName)));
            holder.tvDateHmCrd.setText("Date: " + hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_Date)));
            holder.tvClassIdHmCrd.setText(hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_ClassId)));
            holder.tvHmWrkIdHmCrd.setText(hmwrkArray.getJSONObject(position).getString(hmwrkContext.getString(R.string.key_HomeworkId)));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnHmwrkDelete.setBackground(hmwrkContext.getResources().getDrawable(R.drawable.button_orange));
                holder.btnHmwrkEdit.setBackground(hmwrkContext.getResources().getDrawable(R.drawable.button_green));
                holder.btnHmwrkFedback.setBackground(hmwrkContext.getResources().getDrawable(R.drawable.button_blue));
            } else {
                holder.btnHmwrkEdit.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.btnHmwrkDelete.setBackgroundColor(Color.parseColor("#FF8C00"));
                holder.btnHmwrkFedback.setBackgroundColor(Color.parseColor("#cd036ce5"));
            }


            holder.btnHmwrkEdit.setOnClickListener(new View.OnClickListener() {
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
            holder.btnHmwrkDelete.setOnClickListener(new View.OnClickListener() {
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
            holder.btnHmwrkFedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int hid = Integer.parseInt(hmwrkArray.getJSONObject(position).getString("HomeworkId"));
                        int classid=Integer.parseInt(hmwrkArray.getJSONObject(position).getString("ClassId"));
                        if(!listener.equals(null)) {
                            listener.onFeed(hid, position,classid);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.tvSubjectHmCrd.setOnClickListener(new View.OnClickListener() {
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
            e.printStackTrace();
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
        TextView tvSubjectHmCrd, tvHomeWorkHmCrd, tvDateHmCrd, tvChptNameHmCrd, tvClassHmCrd, tvHmWrkIdHmCrd, tvClassIdHmCrd;
        CardView crdViewCurHmWrk;
        Button btnHmwrkDelete, btnHmwrkEdit, btnHmwrkFedback;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSubjectHmCrd = itemView.findViewById(R.id.tvSubjectHmCrd);
            tvHomeWorkHmCrd = itemView.findViewById(R.id.tvHomeWorkHmCrd);
            tvDateHmCrd = itemView.findViewById(R.id.tvDateHmCrd);
            tvChptNameHmCrd = itemView.findViewById(R.id.tvChptNameHmCrd);
            tvClassHmCrd = itemView.findViewById(R.id.tvClassHmCrd);
            tvClassIdHmCrd = itemView.findViewById(R.id.tvClassIdHmCrd);
            tvHmWrkIdHmCrd = itemView.findViewById(R.id.tvHmWrkIdHmCrd);
            crdViewCurHmWrk = itemView.findViewById(R.id.crdViewCurHmWrk);
            btnHmwrkEdit = itemView.findViewById(R.id.btnHmwrkEdit);
            btnHmwrkDelete = itemView.findViewById(R.id.btnHmwrkDelete);
            btnHmwrkFedback = itemView.findViewById(R.id.btnHmwrkFedback);

            tvSubjectHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvSubjectHmCrd.getText());
                    Toast.makeText(hmwrkContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvHomeWorkHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvHomeWorkHmCrd.getText());
                    Toast.makeText(hmwrkContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvDateHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvDateHmCrd.getText());
                    Toast.makeText(hmwrkContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvChptNameHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvChptNameHmCrd.getText());
                    Toast.makeText(hmwrkContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvClassHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvClassHmCrd.getText());
                    Toast.makeText(hmwrkContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvClassIdHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvClassIdHmCrd.getText());
                    Toast.makeText(hmwrkContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvHmWrkIdHmCrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvHmWrkIdHmCrd.getText());
                    Toast.makeText(hmwrkContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });




        }
    }

    public interface OnHomworkViewClickListener {
        void onEdit(JSONObject currentData, int position, int hid);
        void onDelete(int hid, int position);
        void onFeed(int hid, int position,int classId);
        void onSubjectSyllabus(JSONObject currentData, int position,int classId,String strSubject,int subjectId);
    }
}

