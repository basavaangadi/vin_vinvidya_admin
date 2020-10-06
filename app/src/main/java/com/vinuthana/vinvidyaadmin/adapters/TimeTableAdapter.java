package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Krish on 14-10-2017.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.MyViewHolder> {
    JSONArray timeTableArray;
    Context timeTableContext;

    public TimeTableAdapter(JSONArray timeTableArray, Context timeTableContext) {
        this.timeTableArray = timeTableArray;
        this.timeTableContext = timeTableContext;
    }

    @Override
    public TimeTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_card_layout, null);*/
        LayoutInflater inflater = (LayoutInflater) timeTableContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_table_card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeTableAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvTimeTableSTime.setText(timeTableArray.getJSONObject(position).getString(timeTableContext.getString(R.string.key_StartTime)));
            holder.tvTimeTablePeriod.setText(timeTableArray.getJSONObject(position).getString(timeTableContext.getString(R.string.key_Period)));
            holder.tvTimeTableSubject.setText(timeTableArray.getJSONObject(position).getString(timeTableContext.getString(R.string.key_Subject)));
            holder.tvTimeTableClass.setText(timeTableArray.getJSONObject(position).getString(timeTableContext.getString(R.string.key_Clas)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return timeTableArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeTableSTime, tvTimeTablePeriod, tvTimeTableSubject, tvTimeTableClass;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTimeTableSTime = (TextView) itemView.findViewById(R.id.tvTimeTableSTime);
            tvTimeTableSubject = (TextView) itemView.findViewById(R.id.tvTimeTableSubject);
            tvTimeTableClass = (TextView) itemView.findViewById(R.id.tvTimeTableClass);
            tvTimeTablePeriod = (TextView) itemView.findViewById(R.id.tvTimeTablePeriod);

            tvTimeTableSTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)timeTableContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvTimeTableSTime.getText());
                    Toast.makeText(timeTableContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvTimeTableSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)timeTableContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvTimeTableSubject.getText());
                    Toast.makeText(timeTableContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvTimeTableClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)timeTableContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvTimeTableClass.getText());
                    Toast.makeText(timeTableContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvTimeTablePeriod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)timeTableContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvTimeTablePeriod.getText());
                    Toast.makeText(timeTableContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
