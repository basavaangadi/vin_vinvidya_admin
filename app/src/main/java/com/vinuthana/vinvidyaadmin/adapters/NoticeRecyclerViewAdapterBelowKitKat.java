package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class NoticeRecyclerViewAdapterBelowKitKat extends
        RecyclerView.Adapter<NoticeRecyclerViewAdapterBelowKitKat.MyViewHolder> {

    JSONArray ntcArray;
    Context ntcContext;
    OnNoticeViewClickListener noticeListnter;
    int roleId;


    public NoticeRecyclerViewAdapterBelowKitKat(JSONArray ntcArray, Context ntcContext,int roleId ) {
        this.ntcArray = ntcArray;
        this.ntcContext = ntcContext;
        this.roleId=roleId;
    }

    @Override
    public NoticeRecyclerViewAdapterBelowKitKat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) ntcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notice_cardview_layout_below_kitkat, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            holder.tvNtcNoteTitle.setText(ntcArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_NoticeTitle)));
            holder.tvNtcNote.setText(ntcArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_Notice)));
            holder.tvNtcNoteOnDate.setText("Note On Date : " + ntcArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_NoticeDate)));
            holder.tvNtcNoteSntDate.setText("Sent Date : " + ntcArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_NoticeCreatedDate)));
            Log.e("adapter ",String.valueOf(roleId));
            //Toast.makeText(ntcContext, roleId, Toast.LENGTH_SHORT).show();
            if (roleId == 1) {
                holder.lytForNoticeOperation.setVisibility(View.VISIBLE);
            } else {
                holder.lytForNoticeOperation.setVisibility(View.GONE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnNoticeDelete.setBackground(ntcContext.getResources().getDrawable(R.drawable.button_orange));
                holder.btnNoticeEdit.setBackground(ntcContext.getResources().getDrawable(R.drawable.button_green));
            } else {
                holder.btnNoticeDelete.setBackgroundColor(Color.parseColor("#FF8C00"));
                holder.btnNoticeEdit.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            holder.btnNoticeEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Edit","clicked");
                    try {
                        if (!noticeListnter.equals(null)) {
                            JSONObject object= ntcArray.getJSONObject(position);
                            String noticeId =object.getString("NoticeId");
                            noticeListnter.onEdit(object,position,noticeId);
                        }
                    }catch (Exception e)
                    {
                        Log.e("onEdit exception", e.toString());
                        Toast.makeText(ntcContext,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            });


            holder.btnNoticeDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("delete","clicked");
                    try {
                        if (!noticeListnter.equals(null)) {
                            JSONObject object= ntcArray.getJSONObject(position);
                            String noticeId =object.getString("NoticeId");
                            noticeListnter.onDelete(position,noticeId);

                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(ntcContext,e.toString() , Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setOnButtonClickListener(OnNoticeViewClickListener noticeListnter){
        this.noticeListnter=noticeListnter;
    }

    @Override
    public int getItemCount() {
        return ntcArray.length();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNtcNoteTitle, tvNtcNote, tvNtcNoteOnDate, tvNtcNoteSntDate;
        LinearLayout lytForNoticeOperation;
        Button btnNoticeEdit,btnNoticeDelete;

        public MyViewHolder(View myView) {
            super(myView);
            tvNtcNoteTitle = (TextView) myView.findViewById(R.id.tvNtcNoteTitle);
            tvNtcNote = (TextView) myView.findViewById(R.id.tvNtcNote);
            tvNtcNoteOnDate = (TextView) myView.findViewById(R.id.tvNtcNoteOnDate);
            tvNtcNoteSntDate = (TextView) myView.findViewById(R.id.tvNtcNoteSntDate);
            lytForNoticeOperation=myView.findViewById(R.id.lytForNoticeOperation);
            btnNoticeDelete=myView.findViewById(R.id.btnNoticeDelete);
            btnNoticeEdit=myView.findViewById(R.id.btnNoticeEdit);


            tvNtcNoteTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)ntcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvNtcNoteTitle.getText());
                    Toast.makeText(ntcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvNtcNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)ntcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvNtcNote.getText());
                    Toast.makeText(ntcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvNtcNoteOnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)ntcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvNtcNoteOnDate.getText());
                    Toast.makeText(ntcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvNtcNoteSntDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)ntcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvNtcNoteSntDate.getText());
                    Toast.makeText(ntcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
    public interface OnNoticeViewClickListener{
        public void onEdit(JSONObject noticeData, int position, String noticeId);
        public void onDelete(int position,String noticeId);
    }

}
