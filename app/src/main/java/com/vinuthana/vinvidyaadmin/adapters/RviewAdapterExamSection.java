package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.examsection.ExamMarksActivity;
import com.vinuthana.vinvidyaadmin.activities.examsection.ExamScheduleActivity;
import com.vinuthana.vinvidyaadmin.activities.examsection.ExamStatusActivity;
import com.vinuthana.vinvidyaadmin.activities.examsection.ExamSyllabusActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/4/2018.
 */

public class RviewAdapterExamSection extends RecyclerView.Adapter<RviewAdapterExamSection.MyViewHolder> {
    ArrayList<String> titleName;
    ArrayList<Integer> images;
    Activity context;
    View view;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null, false);
        RviewAdapterExamSection.MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    public RviewAdapterExamSection(Activity context, ArrayList<String> titleName, ArrayList<Integer> images) {
        this.titleName = titleName;
        this.images = images;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(RviewAdapterExamSection.MyViewHolder holder, final int position) {

        /*try {
            strStudentId = context.getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Context mContext;
        mContext = holder.itemView.getContext();
        holder.tvText.setText(titleName.get(position));
        holder.imageView.setImageResource(images.get(position));

        holder.cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Position " + position, Toast.LENGTH_SHORT).show();

                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(context, ExamScheduleActivity.class);
                        break;
                    case 1:
                        intent = new Intent(context, ExamSyllabusActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, ExamMarksActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, ExamStatusActivity.class);
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
