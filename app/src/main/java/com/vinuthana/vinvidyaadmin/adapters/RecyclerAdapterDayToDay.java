package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.AttendanceActivity;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.DayReportActivity;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.HomeWorkActivity;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.HomeWorkFeedbackActivity;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.TimeTableActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/2/2018.
 */

public class RecyclerAdapterDayToDay extends RecyclerView.Adapter<RecyclerAdapterDayToDay.MyViewHolder> {

    ArrayList<String> titleName;
    ArrayList<Integer> images;
    View view;
    Activity context;

    /*StudentSPreference studentSPreference;
    HashMap<String,String> hashMap = new HashMap<String, String>();
    String strStudentId;*/


    public RecyclerAdapterDayToDay(Activity context, ArrayList<String> titleName, ArrayList<Integer> images) {
        this.titleName = titleName;
        this.images = images;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterDayToDay.MyViewHolder holder, final int position) {

        /*try {
            strStudentId = context.getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentSPreference = new StudentSPreference(context);
        hashMap = studentSPreference.getStudentDataFromSP(strStudentId);*/

        holder.tvText.setText(titleName.get(position));
        holder.imageView.setImageResource(images.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, titleName.get(position), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(context,NewActivity.class);
                context.startActivity(intent);*/
            }
        });
        holder.cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(context, AttendanceActivity.class);
                        break;
                    case 1:
                        intent = new Intent(context, HomeWorkActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, HomeWorkFeedbackActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, DayReportActivity.class);
                        break;
                    case 4:
                        intent = new Intent(context, TimeTableActivity.class);
                        break;
                    default:
                        break;
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        ImageView imageView;
        CardView cardList;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            cardList = (CardView) itemView.findViewById(R.id.cardList);
        }
    }
}
