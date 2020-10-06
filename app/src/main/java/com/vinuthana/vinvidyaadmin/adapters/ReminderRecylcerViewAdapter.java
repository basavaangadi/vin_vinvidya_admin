package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
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

/**
 * Created by KISHAN on 07-09-17.
 */

public class ReminderRecylcerViewAdapter extends RecyclerView.Adapter<ReminderRecylcerViewAdapter.MyViewHolder> {

    JSONArray rmndrArray;
    Context rmdrContext;
    int roleId;
    OnReminderViewClickListener reminderListner;

    public ReminderRecylcerViewAdapter(JSONArray rmndrArray, Context rmdrContext,int roleId) {
        this.rmndrArray = rmndrArray;
        this.rmdrContext = rmdrContext;
        this.roleId=roleId;
    }

    @Override
    public ReminderRecylcerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) rmdrContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.reminder_cardview_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReminderRecylcerViewAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvRmndrNoteTitle.setText(rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_ReminderTitle)));
            holder.tvRmndrNote.setText(rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_Reminder)));
            holder.tvRmndrOnDate.setText("Rem. On Date: " + rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_ReminderDate)));
            holder.tvRmndrSentDate.setText("Sent Date: " + rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_ReminderSetDate)));
            holder.btnRmdrEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(!reminderListner.equals(null)){
                            JSONObject object = rmndrArray.getJSONObject(position);
                            Log.e("Parents note OnEdit", "ReminderId ");
                            String strReminderId =object.getString("ReminderId");
                            reminderListner.onEdit(object,position,strReminderId);
                        }else {
                            Toast.makeText(rmdrContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(rmdrContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.btnRmdrDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(!reminderListner.equals(null)){
                            JSONObject object = rmndrArray.getJSONObject(position);
                            Log.e("Staff Reminder onDelete", String.valueOf(position));
                            String strReminderId =object.getString("ReminderId");
                            reminderListner.onDelete(position,strReminderId);
                        }else {
                            Toast.makeText(rmdrContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(rmdrContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (roleId == 1) {
                holder.lytForReminderOperation.setVisibility(View.VISIBLE);

            } else {
                holder.lytForReminderOperation.setVisibility(View.GONE);
            }
            //holder.tvRmndrAcademicYrId.setText("Academic Year ID: " + rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_AcademicYearId)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
 public void setOnButtonClickListner(OnReminderViewClickListener reminderListner){
        this.reminderListner=reminderListner;
 }
    @Override
    public int getItemCount() {
        return rmndrArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRmndrNoteTitle, tvRmndrNote, tvRmndrSentDate, tvRmndrOnDate, tvRmndrAcademicYrId;
        LinearLayout lytForReminderOperation;
        Button btnRmdrEdit,btnRmdrDelete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvRmndrNoteTitle = (TextView) itemView.findViewById(R.id.tvRmndrNoteTitle);
            tvRmndrNote = (TextView) itemView.findViewById(R.id.tvRmndrNote);
            tvRmndrSentDate = (TextView) itemView.findViewById(R.id.tvRmndrSntDate);
            tvRmndrOnDate = (TextView) itemView.findViewById(R.id.tvRmndrOnDate);
            lytForReminderOperation =  itemView.findViewById(R.id.lytForReminderOperation);
            btnRmdrDelete=itemView.findViewById(R.id.btnRmdrDelete);
            btnRmdrEdit=itemView.findViewById(R.id.btnRmdrEdit);

            tvRmndrNoteTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrNoteTitle.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvRmndrNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrNote.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvRmndrSentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrSentDate.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvRmndrOnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)rmdrContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRmndrOnDate.getText());
                    Toast.makeText(rmdrContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });


        }

    }
    public interface OnReminderViewClickListener{
        public void onEdit(JSONObject ReminderData, int position, String reminderId);
        public void onDelete(int position,String reminderId);
    }
}
