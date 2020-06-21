package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditDailyReportActivity extends AppCompatActivity {
    TextView tvEditDailyReportClass,tvEditDailyReportSubject,tvEditDailyReportPeriod,tvEditDailyReportDate;
    EditText edtxbEditDailyReport;
    Button btnEditDailyReport;
    JSONObject curentObject;
    String strClassId,strCurrentData,strStaffId,strSubjectId,strBase64Report,strReportSentDate;
    String strStaffReportId,strReportOnDate,strClass,strPeriod,strSubject,strReport;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_report);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.atndToolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit daily report");
       // setTitle("Edit daily report");
        init();
        allevents();
        setValues();
    }
    public void init(){
    tvEditDailyReportClass=findViewById(R.id.tvEditDailyReportClass);
    tvEditDailyReportSubject=findViewById(R.id.tvEditDailyReportSubject);
    tvEditDailyReportPeriod=findViewById(R.id.tvEditDailyReportPeriod);
    tvEditDailyReportDate=findViewById(R.id.tvEditDailyReportDate);
        edtxbEditDailyReport=findViewById(R.id.edtxbEditDailyReport);
        btnEditDailyReport=findViewById(R.id.btnEditDailyReport);
        Bundle bundle= getIntent().getExtras();

        strCurrentData=bundle.getString("CurrentData");
        strStaffId=bundle.getString("StaffId");

        try {
            curentObject= new JSONObject(strCurrentData);
            strStaffReportId=curentObject.getString("StaffReportId");
            strReportOnDate=curentObject.getString("ReportOnDate");
            strClass=curentObject.getString("Class");
            strPeriod=curentObject.getString("Period");
            strSubject=curentObject.getString("Subject");
            strReport=curentObject.getString("Report");
            strClassId=curentObject.getString("ClassId");
            strSubjectId=curentObject.getString("SubjectId");
          } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void allevents(){
btnEditDailyReport.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       String strReport= edtxbEditDailyReport.getText().toString();
        if(strReport.length()<3){
            Toast toast =Toast.makeText(EditDailyReportActivity.this,"enter valid Report",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else{
            new EditDailyReport().execute();
        }

    }

});
    edtxbEditDailyReport.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            strReport=edtxbEditDailyReport.getText().toString();
            strBase64Report= StringUtil.textToBase64(strReport);
        }
    });
    }
    public void setValues(){
        tvEditDailyReportClass.setText(strClass);
        tvEditDailyReportSubject.setText(strSubject);
        tvEditDailyReportPeriod.setText(strPeriod);
        tvEditDailyReportDate.setText(strReportOnDate);
        edtxbEditDailyReport.setText(strReport);
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
    class EditDailyReport extends AsyncTask<String,JSONArray,Void>{

        String strMsg = "";
        String url = AD.url.base_url + "reportingOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditDailyReportActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Editing Daily report...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editStaffDailyReport));
                JSONObject reportingData = new JSONObject();
                reportingData.put(getString(R.string.key_StaffReportId), strStaffReportId);
                reportingData.put(getString(R.string.key_StaffId), strStaffId);
                reportingData.put(getString(R.string.key_ClassId), strClassId);
                reportingData.put(getString(R.string.key_Date), strReportOnDate);
                reportingData.put(getString(R.string.key_SubjectId), strSubjectId);
                reportingData.put(getString(R.string.key_Report), strBase64Report);
                reportingData.put(getString(R.string.key_Period), strPeriod);
                reportingData.put(getString(R.string.key_ReportSentDate), strReportSentDate);
                outObject.put(getString(R.string.key_reportingData), reportingData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));
                int resid=inObject.getInt(getString(R.string.key_resId));
                String strStatus=inObject.getString(getString(R.string.key_Status));

                EditDailyReportActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (strStatus.equalsIgnoreCase("Success")||strStatus.equalsIgnoreCase("Fail")) {
                            alertMessage = strMsg;
                            alertTitle = strStatus;

                        } else {
                            alertTitle = "Error";
                            alertMessage = "Error Occured While editing report..";
                            Toast.makeText(EditDailyReportActivity.this, String.valueOf(resid), Toast.LENGTH_SHORT).show();
                        }
                        showAlert(alertMessage, alertTitle);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
        private void showAlert(String alertMessage, String alertTitle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditDailyReportActivity.this);
            builder.setMessage(alertMessage);
            builder.setTitle(alertTitle);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onBackPressed();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }



}
