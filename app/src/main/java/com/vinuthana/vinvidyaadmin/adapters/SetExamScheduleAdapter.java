package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
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

public class SetExamScheduleAdapter  extends
        RecyclerView.Adapter<SetExamScheduleAdapter.MyViewHolder> {

    JSONArray ntcArray;
    Context ntcContext;
    OnNoticeViewClickListener noticeListnter;
    int roleId;


    public SetExamScheduleAdapter(JSONArray ntcArray, Context ntcContext, int roleId) {
        this.ntcArray = ntcArray;
        this.ntcContext = ntcContext;
        this.roleId = roleId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) ntcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.schdule_to_be_set_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            holder.tvScduleToBeSetSlNO.setText(String.valueOf(position));
            holder.tvScduleToBeSetclass.setText(ntcArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_Notice)));
            holder.tvScduleToBeSetSubject.setText("Note On Date : " + ntcArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_NoticeDate)));
            holder.tvNtcNoteSntDate.setText("Sent Date : " + ntcArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_NoticeCreatedDate)));
            Log.e("adapter ", String.valueOf(roleId));
            //Toast.makeText(ntcContext, roleId, Toast.LENGTH_SHORT).show();

                holder.lytForNoticeOperation.setVisibility(View.VISIBLE);

            holder.btnSetExmSchdlAdpter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Edit", "clicked");
                    try {
                        if (!noticeListnter.equals(null)) {
                            JSONObject object = ntcArray.getJSONObject(position);
                            String noticeId = object.getString("NoticeId");
                            noticeListnter.onEdit(object, position, noticeId);
                        }
                    } catch (Exception e) {
                        Log.e("onEdit exception", e.toString());
                        Toast.makeText(ntcContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.btnNoticeDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("delete", "clicked");
                    try {
                        if (!noticeListnter.equals(null)) {
                            JSONObject object = ntcArray.getJSONObject(position);
                            String noticeId = object.getString("NoticeId");
                            noticeListnter.onDelete(position, noticeId);

                        }
                    } catch (Exception e) {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(ntcContext, e.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOnButtonClickListener(OnNoticeViewClickListener noticeListnter) {
        this.noticeListnter = noticeListnter;
    }

    @Override
    public int getItemCount() {
        return ntcArray.length();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvScduleToBeSetSlNO, tvScduleToBeSetclass, tvScduleToBeSetSubject, tvNtcNoteSntDate;
        LinearLayout lytForNoticeOperation;
        Button btnSetExmSchdlAdpter, btnNoticeDelete;

        public MyViewHolder(View myView) {
            super(myView);
            tvScduleToBeSetSlNO = (TextView) myView.findViewById(R.id.tvScduleToBeSetSlNO);
            tvScduleToBeSetclass = (TextView) myView.findViewById(R.id.tvScduleToBeSetclass);
            tvScduleToBeSetSubject = (TextView) myView.findViewById(R.id.tvScduleToBeSetSubject);
            tvNtcNoteSntDate = (TextView) myView.findViewById(R.id.tvNtcNoteSntDate);
            lytForNoticeOperation = myView.findViewById(R.id.lytForNoticeOperation);
            btnNoticeDelete = myView.findViewById(R.id.btnNoticeDelete);
            btnSetExmSchdlAdpter = myView.findViewById(R.id.btnSetExmSchdlAdpter);
        }
    }

    public interface OnNoticeViewClickListener {
        public void onEdit(JSONObject noticeData, int position, String noticeId);

        public void onDelete(int position, String noticeId);
    }
}