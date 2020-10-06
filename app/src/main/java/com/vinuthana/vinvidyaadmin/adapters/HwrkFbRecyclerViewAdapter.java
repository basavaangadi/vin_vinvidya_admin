package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 12/9/2017.
 */

public class HwrkFbRecyclerViewAdapter extends RecyclerView.Adapter<HwrkFbRecyclerViewAdapter.MyViewHolder> {
    JSONArray hmwrkFbArray;
    Context hmwrkFbContext;

    public HwrkFbRecyclerViewAdapter(JSONArray hmwrkFbArray, Context hmwrkFbContext) {
        this.hmwrkFbArray = hmwrkFbArray;
        this.hmwrkFbContext = hmwrkFbContext;
    }

    @Override
    public HwrkFbRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) hmwrkFbContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.hw_feedback_layout, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HwrkFbRecyclerViewAdapter.MyViewHolder holder, final int position) {
        try {
            JSONObject object = hmwrkFbArray.getJSONObject(position);
            holder.tvCurHWFbStdtName.setText(object.getString(hmwrkFbContext.getString(R.string.key_StudentName)));
            holder.tvCurHwFbRollNo.setText(object.getString(hmwrkFbContext.getString(R.string.key_RollNo)));
            String strStatus = object.getString(hmwrkFbContext.getString(R.string.key_Status));
            if (strStatus.equalsIgnoreCase("Done")) {
                holder.tvHWFbStatus.setTextColor(Color.parseColor("#00b300"));
            } else {
                holder.tvHWFbStatus.setTextColor(Color.parseColor("#ff0000"));
            }
            holder.tvHWFbStatus.setText(object.getString(hmwrkFbContext.getString(R.string.key_Status)));
            //holder.tvCurHWFbHmClass.setText(hmwrkFbArray.getJSONObject(position).getString(hmwrkFbContext.getString(R.string.key_ClassId)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hmwrkFbArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHWFbStatus, tvCurHWFbHmDt, tvCurHwFbDt, tvCurHWFbSub, tvCurHWFbStdtName,
                tvCurHwFbRollNo, tvCurHWFbHmClass, tvCurHWFbHmChapterName, tvCurHWFbHomWork;
        CardView crdViewHmWrkFb;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvHWFbStatus = (TextView) itemView.findViewById(R.id.tvHWFbStatus);
            tvCurHWFbStdtName = (TextView) itemView.findViewById(R.id.tvCurHWFbStdtName);
            tvCurHwFbRollNo = (TextView) itemView.findViewById(R.id.tvCurHwFbRollNo);

            tvHWFbStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkFbContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvHWFbStatus.getText());
                    Toast.makeText(hmwrkFbContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvCurHWFbStdtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkFbContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvCurHWFbStdtName.getText());
                    Toast.makeText(hmwrkFbContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvCurHwFbRollNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)hmwrkFbContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvCurHwFbRollNo.getText());
                    Toast.makeText(hmwrkFbContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}