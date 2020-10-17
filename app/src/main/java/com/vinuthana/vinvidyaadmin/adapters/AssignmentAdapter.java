package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.vinuthana.vinvidyaadmin.R;
import org.json.JSONArray;
import org.json.JSONObject;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {
    JSONArray AssignmentArray;
    Activity subContext;
    OnAssignmentClickListener onAssignmentClickListener;

    public AssignmentAdapter(JSONArray AssignmentArray, Activity subContext, String strClassSection) {
        this.AssignmentArray = AssignmentArray;
        this.subContext = subContext;
    }

    @Override
    public AssignmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // LayoutInflater inflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // View view = inflater.inflate(R.layout.assignment_list, parent,false);
       // return new MyViewHolder(view);

        LayoutInflater inflater = (LayoutInflater) subContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.assignment_list, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssignmentAdapter.MyViewHolder holder, int position) {

        try {
            String Subject = "<font color=#FF8C00>Sub : </font>"+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Subject));
            String title = "<font color=#FF8C00>Title : </font> "+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Title));
            String filename = "<font color=#FF8C00>File : </font> "+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Filename));
            String description = "<font color=#FF8C00>Description : </font> "+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Description));
            holder.tViewSubject.setText(Html.fromHtml(Subject));
            holder.tViewTitle.setText(Html.fromHtml(title));
            holder.tViewFilename.setText(Html.fromHtml(filename));
            holder.tViewDescription.setText(Html.fromHtml(description));
            /*holder.tViewSubject.setText("Sub : "+AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Subject)));
            holder.tViewTitle.setText(AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Title)));
            holder.tViewFilename.setText(AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Filename)));
            holder.tViewDescription.setText(AssignmentArray.getJSONObject(position).getString(subContext.getString(R.string.key_Description)));*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnDownload.setBackground(subContext.getResources().getDrawable(R.drawable.btnshape));
                holder.btnEdit.setBackground(subContext.getResources().getDrawable(R.drawable.button_green));
            } else {
                holder.btnDownload.setBackgroundColor(Color.parseColor("#039be5"));
                holder.btnEdit.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!onAssignmentClickListener.equals(null)) {
                            JSONObject object= AssignmentArray.getJSONObject(position);
                            onAssignmentClickListener.onDownload(object, position);

                        }
                    }catch (Exception e)
                    {
                        Log.e("onDownload exception", e.toString());
                        Toast toast=Toast.makeText(subContext,e.toString() , Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                    }
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!onAssignmentClickListener.equals(null)) {
                            JSONObject object= AssignmentArray.getJSONObject(position);

                                onAssignmentClickListener.onEdit(object, position);

                        }
                    }catch (Exception e)
                    {
                        Log.e("onEdit exception", e.toString());
                        Toast toast=Toast.makeText(subContext,e.toString() , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(subContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void setOnButtonClickListener(OnAssignmentClickListener onAssignmentClickListener){
        this.onAssignmentClickListener=onAssignmentClickListener;
    }

    @Override
    public int getItemCount() {
        return AssignmentArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tViewSubject,tViewTitle,tViewFilename,tViewDescription;
        Button btnDownload,btnEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            tViewSubject = (TextView) itemView.findViewById(R.id.tViewSubject);
            tViewTitle = (TextView) itemView.findViewById(R.id.editTitle);
            tViewFilename = (TextView) itemView.findViewById(R.id.tViewFilename);
            tViewDescription = (TextView) itemView.findViewById(R.id.editDescription);
            btnDownload = (Button) itemView.findViewById(R.id.btnDownload);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);

            tViewSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)subContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tViewSubject.getText());
                    Toast.makeText(subContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tViewTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)subContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tViewTitle.getText());
                    Toast.makeText(subContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tViewFilename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)subContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tViewFilename.getText());
                    Toast.makeText(subContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tViewDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)subContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tViewDescription.getText());
                    Toast.makeText(subContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

    public interface OnAssignmentClickListener{
        public void onDownload(JSONObject downloadData, int position);
        public void onEdit(JSONObject EditData,int position);

    }
}
