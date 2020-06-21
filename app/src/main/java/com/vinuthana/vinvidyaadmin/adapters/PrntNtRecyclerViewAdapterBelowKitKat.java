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
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrntNtRecyclerViewAdapterBelowKitKat extends RecyclerView.Adapter<PrntNtRecyclerViewAdapterBelowKitKat.MyViewHolder> {

    JSONArray prntNtArray;
    Context prnNtContext;
    OnNoticeViewClickListener parentsNoteListner;

    public PrntNtRecyclerViewAdapterBelowKitKat(JSONArray prntNtArray, Context prnNtContext) {
        this.prntNtArray = prntNtArray;
        this.prnNtContext = prnNtContext;
    }

    @Override
    public PrntNtRecyclerViewAdapterBelowKitKat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) prnNtContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.parent_note_crdvw_layout_below_kitkat, null);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(PrntNtRecyclerViewAdapterBelowKitKat.MyViewHolder holder, int position) {
        try {
            holder.tvPrtNtNoteTitleBelowKitKat.setText(prntNtArray.getJSONObject(position).getString("NoteTitle"));
            holder.tvPrtNtNoteBelowKitKat.setText(prntNtArray.getJSONObject(position).getString("Notice"));
            holder.tvPrtNtNoteSentDateBelowKitKat.setText("Sent Date: " + prntNtArray.getJSONObject(position).getString("NoticeCreatedDate"));
            holder.tvPrtNtNoteOnDateBelowKitKat.setText("Note On Date: " + prntNtArray.getJSONObject(position).getString("NoticeDate"));
            Log.e("tag", holder.tvPrtNtNoteTitleBelowKitKat.toString() + " " + holder.tvPrtNtNoteBelowKitKat.toString() + " " + holder.tvPrtNtNoteSentDateBelowKitKat.toString() + " " + holder.tvPrtNtNoteOnDateBelowKitKat.toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnParentNoteEditBelowKitKat.setBackground(prnNtContext.getResources().getDrawable(R.drawable.button_green));
            } else {
                holder.btnParentNoteEditBelowKitKat.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            holder.btnParentNoteEditBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!parentsNoteListner.equals(null)){

                            JSONObject object = prntNtArray.getJSONObject(position);
                            Log.e("Parents note OnEdit", "noticeId ");
                            String noticeId =object.getString("NoticeId");
                            parentsNoteListner.onEdit(object,position,noticeId);

                        }

                        else {
                            Toast.makeText(prnNtContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Toast.makeText(prnNtContext, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnParentNoteDeleteBelowKitKat.setBackground(prnNtContext.getResources().getDrawable(R.drawable.button_orange));
            } else {
                holder.btnParentNoteDeleteBelowKitKat.setBackgroundColor(Color.parseColor("#FF8C00"));
            }
            holder.btnParentNoteDeleteBelowKitKat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!parentsNoteListner.equals(null)) {
                            JSONObject object= prntNtArray.getJSONObject(position);
                            String noticeId =object.getString("NoticeId");
                            parentsNoteListner.onDelete(position,noticeId);

                        }else {
                            Toast.makeText(prnNtContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(prnNtContext,e.toString() , Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setOnButtonClickListener(OnNoticeViewClickListener noticeListnter){
        this.parentsNoteListner=noticeListnter;
    }
    @Override
    public int getItemCount() {
        return prntNtArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrtNtNoteTitleBelowKitKat, tvPrtNtNoteBelowKitKat, tvPrtNtNoteSentDateBelowKitKat, tvPrtNtNoteOnDateBelowKitKat;
        Button btnParentNoteEditBelowKitKat,btnParentNoteDeleteBelowKitKat;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPrtNtNoteTitleBelowKitKat = (TextView) itemView.findViewById(R.id.tvPrtNtNoteTitleBelowKitKat);
            tvPrtNtNoteBelowKitKat = (TextView) itemView.findViewById(R.id.tvPrtNtNoteBelowKitKat);
            tvPrtNtNoteSentDateBelowKitKat = (TextView) itemView.findViewById(R.id.tvPrtNtNoteSentDateBelowKitKat);
            tvPrtNtNoteOnDateBelowKitKat = (TextView) itemView.findViewById(R.id.tvPrtNtNoteOnDateBelowKitKat);
            btnParentNoteEditBelowKitKat=itemView.findViewById(R.id.btnParentNoteEditBelowKitKat);
            btnParentNoteDeleteBelowKitKat=itemView.findViewById(R.id.btnParentNoteDeleteBelowKitKat);
        }
    }
    public interface OnNoticeViewClickListener{
        public void onEdit(JSONObject noticeData, int position, String noticeId);
        public void onDelete(int position,String noticeId);
    }
}