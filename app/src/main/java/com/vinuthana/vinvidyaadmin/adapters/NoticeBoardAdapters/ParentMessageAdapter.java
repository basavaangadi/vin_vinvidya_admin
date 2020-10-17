package com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParentMessageAdapter extends RecyclerView.Adapter<ParentMessageAdapter.MyViewHolder> {

    JSONArray prntNtArray;
    Context prnNtContext;
    onParentMessageClickListener parentMessageListener;

    public ParentMessageAdapter(JSONArray prntNtArray, Context prnNtContext) {
        this.prntNtArray = prntNtArray;
        this.prnNtContext = prnNtContext;
    }

    @Override
    public ParentMessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) prnNtContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.parent_message_layout, null);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(ParentMessageAdapter.MyViewHolder holder, int position) {
        try {

            holder.tvParentMessageResponse.setText(prntNtArray.getJSONObject(position).getString("Reply"));
            holder.tvParentMessageClass.setText(prntNtArray.getJSONObject(position).getString("Class"));
            holder.tvParentMessageStudentName.setText(prntNtArray.getJSONObject(position).getString("StudentName"));
            holder.tvParentMeassageSentOn.setText("Sent Date: " + prntNtArray.getJSONObject(position).getString("AddedDate"));
            holder.tvParentMessageStaffRepliedDate.setText("Replied Date: " + prntNtArray.getJSONObject(position).getString("ReplyDate"));
            Log.e("tag", holder.tvParentMessageResponse.toString() + " " + holder.tvParentMeassageSentOn.toString() + " " + holder.tvParentMessageStaffRepliedDate.toString());
            holder.btnParentNoteEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!parentMessageListener.equals(null)) {

                            JSONObject object = prntNtArray.getJSONObject(position);
                            Log.e("Parents Message onReply", "noticeId ");
                            String parentMessageId = object.getString("ParentMessageId");
                            parentMessageListener.onReply(object, position, parentMessageId);

                        } else {
                            Toast.makeText(prnNtContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(prnNtContext, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOnButtonClickListener(onParentMessageClickListener parentMessageListener) {
        this.parentMessageListener = parentMessageListener;
    }

    @Override
    public int getItemCount() {
        if(prntNtArray!=null){
            return prntNtArray.length();

        }else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  tvParentMessageResponse,tvParentMessageClass,tvParentMessageMessage ,tvParentMeassageSentOn, tvParentMessageStaffRepliedDate,tvParentMessageStudentName;
        Button btnParentNoteEdit, btnParentNoteDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvParentMessageResponse = (TextView) itemView.findViewById(R.id.tvParentMessageResponse);
            tvParentMeassageSentOn = (TextView) itemView.findViewById(R.id.tvParentMeassageSentOn);
            tvParentMessageMessage = (TextView) itemView.findViewById(R.id.tvParentMessageMessage);
            tvParentMessageClass = (TextView) itemView.findViewById(R.id.tvParentMessageClass);
            tvParentMessageStudentName = (TextView) itemView.findViewById(R.id.tvParentMessageStudentName);
            tvParentMessageStaffRepliedDate = (TextView) itemView.findViewById(R.id.tvParentMessageStaffRepliedDate);
            btnParentNoteEdit = itemView.findViewById(R.id.btnParentNoteEdit);

            tvParentMessageResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvParentMessageResponse.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvParentMeassageSentOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvParentMeassageSentOn.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvParentMessageMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvParentMessageMessage.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvParentMessageClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvParentMessageClass.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvParentMessageStudentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvParentMessageStudentName.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvParentMessageStaffRepliedDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvParentMessageStaffRepliedDate.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });



        }
    }

    public interface onParentMessageClickListener {
        public void onReply(JSONObject parentMessageData, int position, String parentMessageId);


    }
    
    
}
