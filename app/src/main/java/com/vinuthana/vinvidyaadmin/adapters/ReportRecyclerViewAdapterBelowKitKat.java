package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
public class ReportRecyclerViewAdapterBelowKitKat extends RecyclerView.Adapter<ReportRecyclerViewAdapterBelowKitKat.MyViewHolder> {

    JSONArray reportArray;
    Context reportContext;
    Fragment currentFragment;
    int roleId;
    Activity currentActivity;
    OnReportClickListener listener;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) reportContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.report_card_view_below_kitkat, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.tvReportCardViewSubjectBelowKitKat.setText( reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Subject)));
            holder.tvReportCardViewClassBelowKitKat.setText(reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Clas)));
            holder.tvReportCardViewPeriodBelowKitKat.setText(reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Period)));
            holder.tvReportCardViewReportBelowKitKat.setText(reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Report)));
            if(roleId==1){
                holder.LnyrLytReportBelowKitKat.setVisibility(View.VISIBLE);
            }else{
                holder.LnyrLytReportBelowKitKat.setVisibility(View.GONE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnReportCardViewDeleteBelowKitKat.setBackground(reportContext.getResources().getDrawable(R.drawable.button_orange));
                holder.btnReportCardViewEditBelowKitKat.setBackground(reportContext.getResources().getDrawable(R.drawable.button_green));
            } else {
                holder.btnReportCardViewEditBelowKitKat.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.btnReportCardViewDeleteBelowKitKat.setBackgroundColor(Color.parseColor("#FF8C00"));
            }


            holder.btnReportCardViewEditBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!listener.equals(null)) {
                        try {
                            String strReportId = reportArray.getJSONObject(position).getString("StaffReportId");
                            listener.onEdit(reportArray.getJSONObject(position), position, strReportId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(reportContext, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            holder.btnReportCardViewDeleteBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String strReportId = reportArray.getJSONObject(position).getString("StaffReportId");
                        if (!listener.equals(null)) {
                            listener.onDelete(strReportId, position);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(reportContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(reportContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public ReportRecyclerViewAdapterBelowKitKat(JSONArray reportArray,Context reportContext,int roleId ){
        this.reportArray=reportArray;
        this.reportContext=reportContext;
        this.roleId=roleId;
    }

    @Override
    public int getItemCount() {
        return reportArray.length();
    }
    public void setOnButtonClickListener(OnReportClickListener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvReportCardViewSubjectBelowKitKat, tvReportCardViewPeriodBelowKitKat, tvReportCardViewReportBelowKitKat, tvReportCardViewClassBelowKitKat ;
        Button btnReportCardViewDeleteBelowKitKat, btnReportCardViewEditBelowKitKat;
        CardView crdViewReportBelowKitKat;
        LinearLayout LnyrLytReportBelowKitKat;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvReportCardViewSubjectBelowKitKat = itemView.findViewById(R.id.tvReportCardViewSubjectBelowKitKat);
            tvReportCardViewPeriodBelowKitKat = itemView.findViewById(R.id.tvReportCardViewPeriodBelowKitKat);
            tvReportCardViewClassBelowKitKat = itemView.findViewById(R.id.tvReportCardViewClassBelowKitKat);
            tvReportCardViewReportBelowKitKat = itemView.findViewById(R.id.tvReportCardViewReportBelowKitKat);
            LnyrLytReportBelowKitKat=itemView.findViewById(R.id.LnyrLytReportBelowKitKat);
            crdViewReportBelowKitKat = itemView.findViewById(R.id.crdViewReportBelowKitKat);

            btnReportCardViewEditBelowKitKat = itemView.findViewById(R.id.btnReportCardViewEditBelowKitKat);
            btnReportCardViewDeleteBelowKitKat = itemView.findViewById(R.id.btnReportCardViewDeleteBelowKitKat);

            tvReportCardViewSubjectBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewSubjectBelowKitKat.getText());
                    Toast.makeText(reportContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvReportCardViewPeriodBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewPeriodBelowKitKat.getText());
                    Toast.makeText(reportContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvReportCardViewClassBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewClassBelowKitKat.getText());
                    Toast.makeText(reportContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvReportCardViewReportBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewReportBelowKitKat.getText());
                    Toast.makeText(reportContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
    public interface OnReportClickListener {
        void onEdit(JSONObject currentData, int position, String strReportId);
        void onDelete(String strReportId, int position);

    }
}
