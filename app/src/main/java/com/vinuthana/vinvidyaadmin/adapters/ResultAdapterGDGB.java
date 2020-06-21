package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultAdapterGDGB extends RecyclerView.Adapter<ResultAdapterGDGB.ViewHolder> {

    JSONArray array;
    Context context;
    int i;
    //String strStatus;

    public ResultAdapterGDGB(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card_layout, parent,false);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.result_card_layout_gdgb, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            i= holder.getAdapterPosition();
            JSONObject object= array.getJSONObject(i);
            String strRollNo=object.getString(context.getString(R.string.key_RollNo));
            holder.txtRollNoGDGB.setText(strRollNo);
            String strStudentName=object.getString(context.getString(R.string.key_Studentname));
            holder.txtStudentNameGDGB.setText(strStudentName);
            String strScholastics=object.getString(context.getString(R.string.key_Scholastics));
            if(!(strScholastics.equalsIgnoreCase("0"))){
                String strObtainedMarks=object.getString(context.getString(R.string.key_MarksObtained));
                //holder.txtObtainedMarks.setText("Marks : "+strObtainedMarks);
                String text = "<font color=#FF8C00>Marks : </font> <font color=#1d252d>"+strObtainedMarks+"</font>";
                holder.txtObtainedMarks.setText(Html.fromHtml(text));
            }

          String strGrade=object.getString(context.getString(R.string.key_Grade));
           // holder.txtGradeGDGB.setText("Grade : "+strGrade);
            String text = "<font color=#FF8C00>Grade : </font> <font color=#1d252d>"+strGrade+"</font>";
            holder.txtGradeGDGB.setText(Html.fromHtml(text));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentNameGDGB, txtClass, txtRollNoGDGB, txtSubject, txtMinMarks, txtMaxMarks, txtObtainedMarks, txtGradeGDGB;

        public ViewHolder(View itemView) {
            super(itemView);
            txtStudentNameGDGB = (TextView) itemView.findViewById(R.id.txtStudentNameGDGB);

            txtRollNoGDGB = (TextView) itemView.findViewById(R.id.txtRollNoGDGB);

            txtObtainedMarks = (TextView) itemView.findViewById(R.id.txtObtainedMarksGDGB);
            txtGradeGDGB = (TextView) itemView.findViewById(R.id.txtGradeGDGB);

        }
    }
}
