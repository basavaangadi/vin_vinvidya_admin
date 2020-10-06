package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 1/30/2018.
 */

public class StudentListCard extends RecyclerView.Adapter<StudentListCard.ViewHolder> {
    JSONArray ntcArray;
    Context ntcContext;
    private List<StudentModel> stdList;
    public static ArrayList<String> selectedCategories = new ArrayList<>();
    View view;

    public StudentListCard(List<StudentModel> stdList) {
        this.stdList = stdList;
    }

    @Override
    public StudentListCard.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentListCard.ViewHolder holder, int position) {
        final int pos = position;
        holder.tvStudName.setText(stdList.get(position).getStrName());
        holder.tvRollNo.setText(stdList.get(position).getStrRollNo());
        holder.chkSelected.setChecked(stdList.get(position).isChecked());

        holder.chkSelected.setTag(stdList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                StudentModel studentModel = (StudentModel) checkBox.getTag();

                studentModel.setChecked(checkBox.isChecked());

                stdList.get(pos).setChecked(checkBox.isChecked());

                //Toast.makeText(view.getContext(), "Clicked on Checkbox: " + checkBox.getText()
                        //+ "is " + checkBox.isChecked(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStudName, tvRollNo;
        public CheckBox chkSelected;
        public StudentModel singleStudent;
        public ViewHolder(View itemView) {
            super(itemView);
            tvStudName = (TextView) itemView.findViewById(R.id.tvStudentName);
            tvRollNo = (TextView) itemView.findViewById(R.id.tvRollNo);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);

            tvStudName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)ntcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudName.getText());
                    Toast.makeText(ntcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvRollNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)ntcContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvRollNo.getText());
                    Toast.makeText(ntcContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    public List<StudentModel> getStudent() {
        ArrayList<StudentModel> myList = new ArrayList<>();

        return stdList;
    }
}
