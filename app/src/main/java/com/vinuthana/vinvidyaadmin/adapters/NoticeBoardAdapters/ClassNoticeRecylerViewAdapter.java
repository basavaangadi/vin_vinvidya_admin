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

public class ClassNoticeRecylerViewAdapter extends RecyclerView.Adapter<ClassNoticeRecylerViewAdapter.MyViewHolder> {

    JSONArray prntNtArray;
    Context prnNtContext;
    OnNoticeViewClickListener ClassNoticeListner;

    public ClassNoticeRecylerViewAdapter(JSONArray prntNtArray, Context prnNtContext) {
        this.prntNtArray = prntNtArray;
        this.prnNtContext = prnNtContext;
    }

    @Override
    public ClassNoticeRecylerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) prnNtContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.parent_note_crdvw_layout, null);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(ClassNoticeRecylerViewAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvPrtNtNoteTitle.setText(prntNtArray.getJSONObject(position).getString("NoticeTitle"));
            holder.tvPrtNtNote.setText(prntNtArray.getJSONObject(position).getString("Notice"));
            holder.tvPrtNtNoteSentDate.setText("Sent Date: " + prntNtArray.getJSONObject(position).getString("NoticeCreatedDate"));
            holder.tvPrtNtNoteOnDate.setText("Note On Date: " + prntNtArray.getJSONObject(position).getString("NoticeDate"));
            Log.e("tag", holder.tvPrtNtNoteTitle.toString() + " " + holder.tvPrtNtNote.toString() + " " + holder.tvPrtNtNoteSentDate.toString() + " " + holder.tvPrtNtNoteOnDate.toString());
            holder.btnParentNoteEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!ClassNoticeListner.equals(null)) {

                            JSONObject object = prntNtArray.getJSONObject(position);
                            Log.e("Parents note OnEdit", "noticeId ");
                            String noticeId = object.getString("NoticeId");
                            ClassNoticeListner.onEdit(object, position, noticeId);

                        } else {
                            Toast.makeText(prnNtContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(prnNtContext, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
            holder.btnParentNoteDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!ClassNoticeListner.equals(null)) {
                            JSONObject object = prntNtArray.getJSONObject(position);
                            String noticeId = object.getString("NoticeId");
                            ClassNoticeListner.onDelete(position, noticeId);

                        } else {
                            Toast.makeText(prnNtContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(prnNtContext, e.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOnButtonClickListener(OnNoticeViewClickListener noticeListnter) {
        this.ClassNoticeListner = noticeListnter;
    }

    @Override
    public int getItemCount() {
        return prntNtArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrtNtNoteTitle, tvPrtNtNote, tvPrtNtNoteSentDate, tvPrtNtNoteOnDate;
        Button btnParentNoteEdit, btnParentNoteDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPrtNtNoteTitle = (TextView) itemView.findViewById(R.id.tvPrtNtNoteTitle);
            tvPrtNtNote = (TextView) itemView.findViewById(R.id.tvPrtNtNote);
            tvPrtNtNoteSentDate = (TextView) itemView.findViewById(R.id.tvPrtNtNoteSentDate);
            tvPrtNtNoteOnDate = (TextView) itemView.findViewById(R.id.tvPrtNtNoteOnDate);
            btnParentNoteEdit = itemView.findViewById(R.id.btnParentNoteEdit);
            btnParentNoteDelete = itemView.findViewById(R.id.btnParentNoteDelete);
            tvPrtNtNoteTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPrtNtNoteTitle.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvPrtNtNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPrtNtNote.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvPrtNtNoteSentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPrtNtNoteSentDate.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

            tvPrtNtNoteOnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPrtNtNoteOnDate.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });



        }
    }

    public interface OnNoticeViewClickListener {
        public void onEdit(JSONObject noticeData, int position, String noticeId);

        public void onDelete(int position, String noticeId);
    }
}
