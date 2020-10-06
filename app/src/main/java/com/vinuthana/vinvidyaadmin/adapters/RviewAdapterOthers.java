package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.AdmissionFeesActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.AssignmentActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.EventsActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.GalleryActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.StudentCredentialsActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.SyllabusActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/4/2018.
 */

public class RviewAdapterOthers extends RecyclerView.Adapter<RviewAdapterOthers.MyViewHolder> {
    Activity context;
    ArrayList<String> titleName;
    ArrayList<Integer> images;
    View view;

    /*StudentSPreference studentSPreference;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    String strStudentId;*/

    public RviewAdapterOthers(Activity context, ArrayList<String> titleName, ArrayList<Integer> images) {
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
    public void onBindViewHolder(RviewAdapterOthers.MyViewHolder holder, final int position) {

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
                        intent = new Intent(context, EventsActivity.class);
                        break;
                    case 1:
                        intent = new Intent(context, GalleryActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, SyllabusActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, AssignmentActivity.class);
                        break;
                    case 4:
                        intent = new Intent(context, StudentCredentialsActivity.class);
                        break;
                    case 5:
                        intent=new Intent(context, AdmissionFeesActivity.class);
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

            tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvText.getText());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
