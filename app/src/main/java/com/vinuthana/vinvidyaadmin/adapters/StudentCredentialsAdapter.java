package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

public class StudentCredentialsAdapter extends RecyclerView.Adapter<StudentCredentialsAdapter.MyViewHolder> {

    JSONArray prntNtArray;
    Context prnNtContext;
    onCredentialsClickListner onCredentialsClickListner;

    public StudentCredentialsAdapter(JSONArray prntNtArray, Context prnNtContext) {
        this.prntNtArray = prntNtArray;
        this.prnNtContext = prnNtContext;
    }

    @Override
    public StudentCredentialsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) prnNtContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.student_credential_layout, parent,false);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(StudentCredentialsAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject object= prntNtArray.getJSONObject(position);
            String strPhNo=object.getString("PhoneNumber");
            String strStudName=object.getString("StudentName");
            String strClass=object.getString("Class");
            //String strStudentId=object.getString("StudentId");
            String strPassword=object.getString("Password");
            if (strPassword.equals("-9999"))
                strPassword = "Unregistered student";
            if (strPhNo.equals("-9999"))
                strPhNo = "Phone number not entered";
            String strRollNo = "";
            if (object.getString("RollNo")==null ||object.getString("RollNo").equals("null"))
                strRollNo = strStudName;
            else
                strRollNo=object.getString("RollNo")+" "+strStudName;
            String htmPhNo = "<font color=#FF8C00>Ph.No : </font>"+strPhNo;
            String htmClass = "<font color=#FF8C00>Class : </font> "+strClass;
            String htmPassword = "<font color=#FF8C00>Password : </font> "+strPassword;

            holder.tvStudentCredentialsByStudPhno.setText(Html.fromHtml(htmPhNo));
            holder.tvStudentCredentialsByStudClass.setText(Html.fromHtml(htmClass));
            holder.tvStudentCredentialsByStudRollNo.setText(strRollNo);
            holder.tvStudentCredentialsByStudStudentName.setText(strStudName);
            holder.tvStudentCredentialsByPassword.setText(Html.fromHtml(htmPassword));




           /* holder.tvStudentCredentialsByStudPhno.setText(prntNtArray.getJSONObject(position).getString("NoteTitle"));
            holder.tvStudentCredentialsByStudClass.setText(prntNtArray.getJSONObject(position).getString("Notice"));
            holder.tvStudentCredentialsByStudRollNo.setText("Sent Date: " + prntNtArray.getJSONObject(position).getString("NoticeCreatedDate"));
            holder.tvStudentCredentialsByStudStudentName.setText("Note On Date: " + prntNtArray.getJSONObject(position).getString("NoticeDate"));
            holder.tvStudentCredentialsByPassword.setText("Note On Date: " + prntNtArray.getJSONObject(position).getString("NoticeDate"));*/
            Log.e("tag", holder.tvStudentCredentialsByStudPhno.toString() + " " + holder.tvStudentCredentialsByStudClass.toString() + " " + holder.tvStudentCredentialsByStudRollNo.toString() + " " + holder.tvStudentCredentialsByStudStudentName.toString());
            holder.btnGetStudentCredentialsByStudEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!onCredentialsClickListner.equals(null)) {

                            JSONObject object = prntNtArray.getJSONObject(position);
                            Log.e("onCredentials", "StudentId ");
                            String strStudentId = object.getString("StudentId");
                            onCredentialsClickListner.onEdit(object, position, strStudentId);

                        } else {
                            Toast.makeText(prnNtContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(prnNtContext, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
            /*holder.btnParentNoteDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!onCredentialsClickListner.equals(null)) {
                            JSONObject object= prntNtArray.getJSONObject(position);
                            String noticeId =object.getString("NoticeId");
                            onCredentialsClickListner.onDelete(position,noticeId);

                        }else {
                            Toast.makeText(prnNtContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        Log.e("onDelete exception", e.toString());
                        Toast.makeText(prnNtContext,e.toString() , Toast.LENGTH_SHORT).show();

                    }
                }
            });*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
    }
    public void setOnButtonClickListener(onCredentialsClickListner noticeListnter){
        this.onCredentialsClickListner=noticeListnter;
    }
    @Override
    public int getItemCount() {
        return prntNtArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentCredentialsByStudPhno, tvStudentCredentialsByStudClass, tvStudentCredentialsByStudRollNo;
        TextView tvStudentCredentialsByPassword, tvStudentCredentialsByStudStudentName;
        Button btnGetStudentCredentialsByStudEdt,btnParentNoteDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvStudentCredentialsByStudPhno = (TextView) itemView.findViewById(R.id.tvStudentCredentialsByStudPhno);
            tvStudentCredentialsByPassword = (TextView) itemView.findViewById(R.id.tvStudentCredentialsByPassword);
            tvStudentCredentialsByStudClass = (TextView) itemView.findViewById(R.id.tvStudentCredentialsByStudClass);
            tvStudentCredentialsByStudRollNo = (TextView) itemView.findViewById(R.id.tvStudentCredentialsByStudRollNo);
            tvStudentCredentialsByStudStudentName = (TextView) itemView.findViewById(R.id.tvStudentCredentialsByStudStudentName);
            btnGetStudentCredentialsByStudEdt =itemView.findViewById(R.id.btnGetStudentCredentialsByStudEdt);
            btnParentNoteDelete=itemView.findViewById(R.id.btnParentNoteDelete);


            tvStudentCredentialsByStudPhno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudentCredentialsByStudPhno.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvStudentCredentialsByPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudentCredentialsByPassword.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvStudentCredentialsByStudClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudentCredentialsByStudClass.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvStudentCredentialsByStudRollNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudentCredentialsByStudRollNo.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvStudentCredentialsByStudStudentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)prnNtContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudentCredentialsByStudStudentName.getText());
                    Toast.makeText(prnNtContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
    public interface onCredentialsClickListner{
        public void onEdit(JSONObject credntialsData, int position, String strStudentId);
        
    }
}
