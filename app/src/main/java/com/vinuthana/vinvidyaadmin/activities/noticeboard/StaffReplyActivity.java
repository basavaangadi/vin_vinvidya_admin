package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.GetSoapResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

public class StaffReplyActivity extends AppCompatActivity {
    TextView tvStaffReplyParentMessage;
    EditText edtStaffReply;
    Button btnStaffReply;
    JSONObject currentObject;
    String strStaffReply,base64StaffReply,strParentMessageId,strCurrentObject,strStaffId;
    CheckConnection connection = new CheckConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_reply);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Staff Reply");
        if (!connection.netInfo(StaffReplyActivity.this)) {
            connection.buildDialog(StaffReplyActivity.this).show();
        } else {
            Bundle bundle = getIntent().getExtras();
            strParentMessageId = bundle.getString("SchoolId");
            strStaffId = bundle.getString("StaffId");
            strCurrentObject = bundle.getString("NoticeData");

            init();
            allEvents();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void init(){

        tvStaffReplyParentMessage=findViewById(R.id.tvStaffReplyParentMessage);
        edtStaffReply=findViewById(R.id.edtStaffReply);
        btnStaffReply=findViewById(R.id.btnStaffReply);

    }

    public void allEvents(){
        btnStaffReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 strStaffReply=edtStaffReply.getText().toString();
                 if(strStaffReply.length()<=3){
                     Toast toast= Toast.makeText(StaffReplyActivity.this,"Enter valid Reply",Toast.LENGTH_SHORT);
                     toast.setGravity(Gravity.CENTER,0,0);
                     toast.show();
                 }else{
                     strStaffReply=edtStaffReply.getText().toString();
                     base64StaffReply= StringUtil.textToBase64(strStaffReply);
                     new SendStaffReply().execute();
                 }
            }
        });
    }
    public  class SendStaffReply extends AsyncTask<String, JSONArray, Void>{
        ProgressDialog progressDialog;

        String url= AD.url.asmx_url+"";
        String methodName="Insert_StaffReply";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        progressDialog.setMessage("Please wait Sending the reply");
        progressDialog.setCancelable(false);
        progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetSoapResponse soapResponse = new GetSoapResponse();
            JSONObject outObject = new JSONObject();

            try {
                JSONObject parentMessageObject = new JSONObject();
                parentMessageObject.put(getString(R.string.key_ParentMessageId),strParentMessageId);
                parentMessageObject.put(getString(R.string.key_Staff_Reply),strStaffReply);
                parentMessageObject.put(getString(R.string.key_Addedby),strStaffId);
                parentMessageObject.put(getString(R.string.key_Input),"1");
                List<PropertyInfo>param=new ArrayList<PropertyInfo>();
                PropertyInfo staffReplyData= new PropertyInfo();
                staffReplyData.setName("Insert_ParentMessage");
                staffReplyData.setValue(parentMessageObject.toString());
                param.add(staffReplyData);

                String responseText = soapResponse.getSOAPStringResponse(url,methodName,param);
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage=inObject.getString(getString(R.string.key_Message));

                showAlert(strStatus,strMessage);


            } catch (Exception ex) {

                StaffReplyActivity.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast toast=Toast.makeText(StaffReplyActivity.this,"Something went Wrong, while sending the reply try again",Toast.LENGTH_SHORT);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();
                     }
               });


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        public void showAlert(String strStatus,String strMessage){

            StaffReplyActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StaffReplyActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                    builder.setTitle(strStatus);
                    builder.setMessage(strMessage   );
                    builder.show();
                }
            });

        }
    }
}
