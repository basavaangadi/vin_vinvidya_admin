package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.MyViewHolder> {

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
        View view = inflater.inflate(R.layout.report_card_view, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.tvReportCardViewSubject.setText( reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Subject)));
            holder.tvReportCardViewClass.setText(reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Clas)));
            holder.tvReportCardViewPeriod.setText(reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Period)));
            holder.tvReportCardViewReport.setText(reportArray.getJSONObject(position).getString(reportContext.getString(R.string.key_Report)));
            if(roleId==1){
               holder.LnyrLytReport.setVisibility(View.VISIBLE);
            }else{
                holder.LnyrLytReport.setVisibility(View.GONE);
            }
            holder.btnReportCardViewEdit.setOnClickListener(new View.OnClickListener() {
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
            holder.btnReportCardViewDelete.setOnClickListener(new View.OnClickListener() {
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
    public ReportRecyclerViewAdapter(JSONArray reportArray,Context reportContext,int roleId ){
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
        TextView tvReportCardViewSubject, tvReportCardViewPeriod, tvReportCardViewReport, tvReportCardViewClass ;
        Button btnReportCardViewDelete, btnReportCardViewEdit;
         CardView crdViewReport;
        LinearLayout LnyrLytReport;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvReportCardViewSubject = itemView.findViewById(R.id.tvReportCardViewSubject);
            tvReportCardViewPeriod = itemView.findViewById(R.id.tvReportCardViewPeriod);
            tvReportCardViewClass = itemView.findViewById(R.id.tvReportCardViewClass);
            tvReportCardViewReport = itemView.findViewById(R.id.tvReportCardViewReport);
            LnyrLytReport=itemView.findViewById(R.id.LnyrLytReport);
            crdViewReport = itemView.findViewById(R.id.crdViewReport);

            btnReportCardViewEdit = itemView.findViewById(R.id.btnReportCardViewEdit);
            btnReportCardViewDelete = itemView.findViewById(R.id.btnReportCardViewDelete);

            tvReportCardViewSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewSubject.getText());
                    Toast.makeText(reportContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvReportCardViewPeriod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewPeriod.getText());
                    Toast.makeText(reportContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvReportCardViewClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewClass.getText());
                    Toast.makeText(reportContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvReportCardViewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)reportContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvReportCardViewReport.getText());
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
