package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReminderRecylcerViewAdapterBelowKitkat extends RecyclerView.Adapter<ReminderRecylcerViewAdapterBelowKitkat.MyViewHolder> {

    JSONArray rmndrArray;
    Context rmdrContext;
    int roleId;
    OnReminderViewClickListener reminderListner;

    public ReminderRecylcerViewAdapterBelowKitkat(JSONArray rmndrArray, Context rmdrContext,int roleId) {
        this.rmndrArray = rmndrArray;
        this.rmdrContext = rmdrContext;
        this.roleId=roleId;
    }

    @Override
    public ReminderRecylcerViewAdapterBelowKitkat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) rmdrContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.reminder_cardview_layout_below_kitkat, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReminderRecylcerViewAdapterBelowKitkat.MyViewHolder holder, int position) {
        try {
            holder.tvRmndrNoteTitleBelowKitkat.setText(rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_ReminderTitle)));
            holder.tvRmndrNoteBelowKitkat.setText(rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_Reminder)));
            holder.tvRmndrOnDateBelowKitkat.setText("Rem. On Date: " + rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_ReminderDate)));
            holder.tvRmndrSntDateBelowKitkat.setText("Sent Date: " + rmndrArray.getJSONObject(position).getString(rmdrContext.getString(R.string.key_ReminderSetDate)));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnRmdrDeleteBelowKitkat.setBackground(rmdrContext.getResources().getDrawable(R.drawable.button_orange));
                holder.btnRmdrEditBelowKitkat.setBackground(rmdrContext.getResources().getDrawable(R.drawable.button_green));
            } else {
                holder.btnRmdrDeleteBelowKitkat.setBackgroundColor(Color.parseColor("#FF8C00"));
                holder.btnRmdrEditBelowKitkat.setBackgroundColor(Color.parseColor("#4CAF50"));
            }

            holder.btnRmdrEditBelowKitkat.setOnClickListener(new View.OnClickListener() {
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


            holder.btnRmdrDeleteBelowKitkat.setOnClickListener(new View.OnClickListener() {
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
                holder.lytForReminderOperationBelowKitkat.setVisibility(View.VISIBLE);

            } else {
                holder.lytForReminderOperationBelowKitkat.setVisibility(View.GONE);
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
        TextView tvRmndrNoteTitleBelowKitkat, tvRmndrNoteBelowKitkat, tvRmndrSntDateBelowKitkat, tvRmndrOnDateBelowKitkat, tvRmndrAcademicYrId;
        LinearLayout lytForReminderOperationBelowKitkat;
        Button btnRmdrEditBelowKitkat,btnRmdrDeleteBelowKitkat;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvRmndrNoteTitleBelowKitkat = (TextView) itemView.findViewById(R.id.tvRmndrNoteTitleBelowKitkat);
            tvRmndrNoteBelowKitkat = (TextView) itemView.findViewById(R.id.tvRmndrNoteBelowKitkat);
            tvRmndrSntDateBelowKitkat = (TextView) itemView.findViewById(R.id.tvRmndrSntDateBelowKitkat);
            tvRmndrOnDateBelowKitkat = (TextView) itemView.findViewById(R.id.tvRmndrOnDateBelowKitkat);
            lytForReminderOperationBelowKitkat =  itemView.findViewById(R.id.lytForReminderOperationBelowKitkat);
            btnRmdrDeleteBelowKitkat=itemView.findViewById(R.id.btnRmdrDeleteBelowKitkat);
            btnRmdrEditBelowKitkat=itemView.findViewById(R.id.btnRmdrEditBelowKitkat);

        }

    }
    public interface OnReminderViewClickListener{
        public void onEdit(JSONObject ReminderData, int position, String reminderId);
        public void onDelete(int position,String reminderId);
    }
}
