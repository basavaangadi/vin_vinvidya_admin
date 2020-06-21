package com.vinuthana.vinvidyaadmin.adapters;

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
import com.vinuthana.vinvidyaadmin.activities.otheractivities.SubjectSyllabusActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.SyllabusActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Krish on 04-10-2017.
 */

public class SubjectRecyclerAdapter extends RecyclerView.Adapter<SubjectRecyclerAdapter.MyViewHolder> {
    JSONArray subArray;
    Context subContext;

    String subTitle, strsubheading, strSubjectId, strClassId,strClassSection;
    ArrayList<String> strSubjectsList = new ArrayList<>();
    SyllabusActivity sybActivity = new SyllabusActivity();

    public SubjectRecyclerAdapter(JSONArray subArray, Context subContext,String strClassSection) {
        this.subArray = subArray;
        this.subContext = subContext;
        this.strClassSection=strClassSection;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.syllabus_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectRecyclerAdapter.MyViewHolder holder, int position) {

        try {
            strsubheading = subArray.getJSONObject(position).getString(subContext.getString(R.string.key_Subject)).toString();
            //strSubjectId = AssignmentArray.getJSONObject(position).getString("SubjectId").toString();
            strSubjectsList.add(new String(strsubheading));
            if (strsubheading.contains(" ")) {
                String[] twoChars = strsubheading.split(" ");
                String fLetter = Character.toString(twoChars[0].charAt(0));
                String secLetter = Character.toString(twoChars[1].charAt(0));
                subTitle = fLetter + " " + secLetter;
            } else {
                subTitle = Character.toString(strsubheading.charAt(0));
            }
            holder.tvSubjectSyl.setText(subTitle);
            holder.syllabusTitleSyl.setText(subArray.getJSONObject(position).getString(subContext.getString(R.string.key_Subject)));
        } catch (Exception e) {
            Toast.makeText(subContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return subArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectSyl, syllabusTitleSyl;
        ImageView imageView;
        CardView cardSyllabus;
        String strPosition;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSubjectSyl = (TextView) itemView.findViewById(R.id.tvSubjectSyl);
            syllabusTitleSyl = (TextView) itemView.findViewById(R.id.syllabusTitleSyl);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewSyl);
            cardSyllabus = (CardView) itemView.findViewById(R.id.cardSyllabus);

            cardSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(subContext, SubjectSyllabusActivity.class);
                    try {
                        intent.putExtra(subContext.getString(R.string.key_Subject),subArray.getJSONObject(getAdapterPosition()).getString(subContext.getString(R.string.key_Subject)).toString());
                        intent.putExtra(subContext.getString(R.string.key_SubjectId),subArray.getJSONObject(getAdapterPosition()).getString(subContext.getString(R.string.key_SubjectId)).toString());
                        intent.putExtra(subContext.getString(R.string.key_ClassId),subArray.getJSONObject(getAdapterPosition()).getString(subContext.getString(R.string.key_ClassId)).toString());
                        intent.putExtra("Class",strClassSection);
                    } catch (JSONException e) {
                        Toast.makeText(subContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    subContext.startActivity(intent);
                }
            });
        }
    }
}
