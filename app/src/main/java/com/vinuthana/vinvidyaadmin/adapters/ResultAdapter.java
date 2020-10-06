package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 3/  3/2018.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    JSONArray array;
    Context context;
    String strStatus="",strSchoolId;

    public ResultAdapter(JSONArray array,String strSchoolId ,Context context) {
        this.array = array;
        this.context = context;
        this.strSchoolId=strSchoolId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card_layout, parent,false);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.result_card_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            int pos=holder.getAdapterPosition();
            JSONObject object=array.getJSONObject(pos);
            holder.txtStudentName.setText(object.getString(context.getString(R.string.key_Studentname)));

           holder.txtRollNo.setText( object.getString(context.getString(R.string.key_RollNo)));
            String strScholastics=object.getString(context.getString(R.string.key_Scholastics));

            if(!(strScholastics.equalsIgnoreCase("0"))){
                if(strSchoolId.equalsIgnoreCase("5")){
                    String strObtainedMarks=object.getString(context.getString(R.string.key_MarksObtained));
                    String text = "<font color=#FF8C00>Marks : </font> <font color=#1d252d>"+strObtainedMarks+"</font>";
                    holder.txtObtainedMarks.setText(Html.fromHtml(text));
                    strStatus= object.getString(context.getString(R.string.key_Status));

                }else{
                   // strS=object.getString(context.getString(R.string.key_Status));
                    String strObtainedMarks=object.getString(context.getString(R.string.key_MarksObtained));
                    String text = "<font color=#FF8C00>Marks : </font> <font color=#1d252d>"+strObtainedMarks+"</font>";
                    holder.txtObtainedMarks.setText(Html.fromHtml(text));

                    int minMarks=0,obtMarks=0;
                    String strMinMarks=object.getString(context.getString(R.string.key_MinMarks));;
                    //String strObtainedMarks=object.getString(context.getString(R.string.key_MarksObtained));

                    if(!(strObtainedMarks==null||strObtainedMarks.isEmpty())){
                        obtMarks=Integer.parseInt(strObtainedMarks);
                        if(!(strMinMarks==null||strMinMarks.isEmpty())){

                            minMarks=Integer.parseInt(strMinMarks);

                            if(obtMarks>=minMarks){
                                strStatus="Pass";
                                holder.txtResultStatus.setTextColor(Color.rgb(0, 200, 0));
                                /*String strStatusText = "<font color=#FF8C00>Grade : </font> <font color=#1d252d>"+strStatus+"</font>";
                                holder.txtResultStatus.setText(Html.fromHtml(strStatusText));*/
                            }else {
                                strStatus="Fail";
                                holder.txtResultStatus.setTextColor(Color.rgb(200, 0, 0));
                            }
                        }
                    }





                }

               // holder.txtObtainedMarks.setText("Marks : "+strObtainedMarks);
            }else{

                if(strSchoolId.equalsIgnoreCase("5")){
                    String strObtainedMarks=object.getString(context.getString(R.string.key_MarksObtained));
                    String text = "<font color=#FF8C00>Marks : </font> <font color=#1d252d>"+strObtainedMarks+"</font>";
                    holder.txtObtainedMarks.setText(Html.fromHtml(text));
                    strStatus= object.getString(context.getString(R.string.key_Status));
                }else{
                    String strObtainedMarks=object.getString(context.getString(R.string.key_Status));
                    String text = "<font color=#FF8C00>Marks : </font> <font color=#1d252d>"+strObtainedMarks+"</font>";
                    holder.txtObtainedMarks.setText(Html.fromHtml(text));
                    strStatus= array.getJSONObject(position).getString(context.getString(R.string.key_Status));
                    JSONObject statusObject=array.getJSONObject(position);


                }

                /*String strMarks=object.getString(context.getString(R.string.key_MarksObtained));
                String text = "<font color=#FF8C00>Marks : </font> <font color=#1d252d>"+strMarks+"</font>";
                holder.txtObtainedMarks.setText(Html.fromHtml(text));*/
           // holder.txtObtainedMarks.setText( "Obt.marks : "+object.getString(context.getString(R.string.key_MarksObtained)));
            }


            //if(!strStatus.equalsIgnoreCase("-9999")) {
                /*if (strStatus.equalsIgnoreCase("Pass") || strStatus.equalsIgnoreCase("pass")) {
                    holder.txtResultStatus.setTextColor(Color.rgb(0, 200, 0));
                } else if (strStatus.equalsIgnoreCase("Fail") || strStatus.equalsIgnoreCase("fail")) {
                    holder.txtResultStatus.setTextColor(Color.rgb(200, 0, 0));
                }*/

                if(!(strSchoolId.equalsIgnoreCase("3"))){
                    if(!(strStatus.isEmpty()||strStatus==null||strStatus.equalsIgnoreCase(""))){

                        String text = "<font color=#FF8C00>Grade : </font> <font color=#1d252d>"+strStatus+"</font>";
                        holder.txtResultStatus.setText(Html.fromHtml(text));
                    }
                }else{
                   holder.txtResultStatus.setText(strStatus);
                }

            //}

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentName, txtClass, txtRollNo, txtSubject, txtMinMarks, txtMaxMarks, txtObtainedMarks, txtResultStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtStudentName = (TextView) itemView.findViewById(R.id.txtStudentName);

            txtRollNo = (TextView) itemView.findViewById(R.id.txtRollNo);

           txtObtainedMarks = (TextView) itemView.findViewById(R.id.txtObtainedMarks);
            txtResultStatus = (TextView) itemView.findViewById(R.id.txtResultStatus);

            txtStudentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(txtStudentName.getText());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            txtRollNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(txtRollNo.getText());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            txtObtainedMarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(txtObtainedMarks.getText());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            txtResultStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(txtResultStatus.getText());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
