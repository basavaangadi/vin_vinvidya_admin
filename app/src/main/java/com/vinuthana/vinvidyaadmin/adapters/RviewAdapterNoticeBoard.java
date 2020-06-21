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
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ClassNoticeActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.NoteActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ParentMessageActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ParentNoteActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ReminderActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.TeacherNoticeActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/4/2018.
 */

public class RviewAdapterNoticeBoard extends RecyclerView.Adapter<RviewAdapterNoticeBoard.MyViewHolder> {

    Activity context;
    ArrayList<String> titleName;
    ArrayList<Integer> images;
    View view;
    /*StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId;*/

    public RviewAdapterNoticeBoard(Activity context, ArrayList<String> titleName, ArrayList<Integer> images) {
        this.context = context;
        this.titleName = titleName;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RviewAdapterNoticeBoard.MyViewHolder holder, final int position) {

        /*try {
            strStudentId = context.getIntent().getExtras().getString("studentId");
            //Toast.makeText(this, strStudId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        holder.tvText.setText(titleName.get(position));
        holder.imageView.setImageResource(images.get(position));

        holder.cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Position " + position, Toast.LENGTH_SHORT).show();

                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(context, NoteActivity.class);
                        break;
                    case 1:
                        intent = new Intent(context, ParentNoteActivity.class);
                        break;
                    case 2: intent = new Intent(context, ClassNoticeActivity.class);
                        break;

                    case 3:intent = new Intent(context, TeacherNoticeActivity.class);
                        break;

                    case 4:intent = new Intent(context, ReminderActivity.class);
                        break;
                    case 5:intent = new Intent(context, ParentMessageActivity.class);
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
        Context mContext;
        CardView cardList;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            cardList = (CardView) itemView.findViewById(R.id.cardList);
            mContext = itemView.getContext();
        }
    }
}
