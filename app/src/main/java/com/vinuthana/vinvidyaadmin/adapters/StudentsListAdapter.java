package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/29/2018.
 */

public class StudentsListAdapter extends BaseAdapter {

    private Context context;
    public static ArrayList<StudentModel> student_list = new ArrayList<StudentModel>();
    boolean[] tg_btn_state;
    JSONArray array;

    public StudentsListAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<StudentModel> studentList) {
        this.student_list = studentList;
        notifyDataSetChanged();
        tg_btn_state = new boolean[student_list.size()];
    }

    @Override
    public int getCount() {
        return student_list.size();
    }

    @Override
    public Object getItem(int postion) {
        return student_list.get(postion);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        private TextView tViewStudentName;
        private TextView tViewRollNo;
        private CheckBox checkBox;
    }

    /*@Override
    public int getViewTypeCount() {
        return student_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }*/

    @Override
    public View getView(int postion, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item, null);
            viewHolder = new ViewHolder();
            try {
                viewHolder.tViewStudentName = (TextView) view.findViewById(R.id.tvStudName);
                viewHolder.tViewRollNo = (TextView) view.findViewById(R.id.tvRollNo);
                viewHolder.checkBox = (CheckBox) view.findViewById(R.id.chkStatus);
                view.setTag(viewHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            if (student_list.get(postion).getStrName() == null || student_list.get(postion).getStrName().equals("null")) {
                viewHolder.tViewStudentName.setText("-");
            } else {
                viewHolder.tViewStudentName.setText(student_list.get(postion).getStrName());
            }

            if (student_list.get(postion).getStrRollNo() == null || student_list.get(postion).getStrRollNo().equals("null")) {
                viewHolder.tViewRollNo.setText("-");
            } else {
                viewHolder.tViewRollNo.setText(student_list.get(postion).getStrRollNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tg_btn_state[postion] = student_list.get(postion).isChecked();
        final int pos = postion;

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    tg_btn_state[pos] = true;
                    student_list.get(pos).setChecked(true);
                } else {
                    tg_btn_state[pos] = false;
                    student_list.get(pos).setChecked(false);
                }
            }
        });
        viewHolder.checkBox.setChecked(tg_btn_state[postion]);
        return view;
    }
}