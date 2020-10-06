package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;

/**
 * Created by KISHAN on 16-09-17.
 */

public class ExmMarksRecycler extends RecyclerView.Adapter<ExmMarksRecycler.MyViewHolder> {
    JSONArray exmmrklArray;
    Context exmmrkcContext;

    public ExmMarksRecycler(JSONArray exmmrklArray, Context exmmrkcContext) {
        this.exmmrklArray = exmmrklArray;
        this.exmmrkcContext = exmmrkcContext;
    }

    @Override
    public ExmMarksRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) exmmrkcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exam_mrks_card_view_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExmMarksRecycler.MyViewHolder holder, int position) {
        try {
            holder.tvExmMarksSubject.setText(exmmrklArray.getJSONObject(position).getString("Subject"));
            holder.tvMaxExmMarks.setText(exmmrklArray.getJSONObject(position).getString("MaxMarks"));
            holder.tvExmMarks.setText(exmmrklArray.getJSONObject(position).getString("MarksObtained"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return exmmrklArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvExmMarks, tvMaxExmMarks, tvExmMarksSubject;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvExmMarksSubject = (TextView) itemView.findViewById(R.id.tvExmMarksSubject);
            tvMaxExmMarks = (TextView) itemView.findViewById(R.id.tvMaxExmMarks);
            tvExmMarks = (TextView) itemView.findViewById(R.id.tvExmMarks);

            tvExmMarksSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExmMarksSubject.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvMaxExmMarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvMaxExmMarks.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvExmMarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)exmmrkcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvExmMarks.getText());
                    Toast.makeText(exmmrkcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}
