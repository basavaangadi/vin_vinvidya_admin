package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PeriodWiseAttendanceAdapter extends BaseAdapter {
    private Context context;
    public static JSONArray jsonArray;
    public PeriodWiseAttendanceAdapter(Context context) {
        this.context = context;
    }

    public void setData(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getViewTypeCount() {
        return jsonArray.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.adapter_period_wise_attendance, parent,false);
            holder = new ViewHolder();
            holder.tview_rollno = (TextView) view.findViewById(R.id.tview_rollno);
            holder.tview_st_name = (TextView) view.findViewById(R.id.tview_st_name);
            holder.cb_attendance = (CheckBox) view.findViewById(R.id.cb_attendance);
//            holder.cb_attendance.setButtonDrawable(context.getResources().getDrawable(R.drawable.checkbox_selector));
            view.setTag(holder);

        } else {
            holder = new ViewHolder();
        }
        try {
            holder.tview_rollno.setText(jsonArray.getJSONObject(position).getString(context.getString(R.string.key_RollNo)));
            holder.tview_st_name.setText(jsonArray.getJSONObject(position).getString(context.getString(R.string.key_studentname)));
            if (jsonArray.getJSONObject(position).getString(context.getString(R.string.key_Status)).equals("1"))
                holder.cb_attendance.setChecked(true);
            else
                holder.cb_attendance.setChecked(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.cb_attendance
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            if (isChecked) {
                                try {
                                    holder.tview_st_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                    jsonArray.getJSONObject(position).put("Status","1");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    holder.tview_st_name.setTextColor(context.getResources().getColor(R.color.card_text));

                                    jsonArray.getJSONObject(position).put("Status","0");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
        } catch (Exception e){

        }


        return view;
    }

    class ViewHolder {
        TextView tview_rollno;
        TextView tview_st_name;
        CheckBox cb_attendance;
    }
}
