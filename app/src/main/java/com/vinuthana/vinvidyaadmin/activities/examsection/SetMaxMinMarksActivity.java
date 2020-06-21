package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ExamAdapter.DecimalDigitsInputFilter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetMaxMinMarksActivity extends AppCompatActivity {
    EditText edtMin,edtMax;
    TextView tvclasses,tvexam,tvdate,tvsubject;
    Button btnProceed;
    String strMax="",strMin="",strIsMarksSet;
    int intMax;
    float fltMin;
    JSONObject outObject;
    String strExamScheduleId,strClassId,strStaffId,syllabusData,strDate,strExamSyllabusId,strExamTime,strClass,strSyllabus;
    String strScholastics,strExam,strSubject,strExamResultId;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_max_min_marks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Max Min Marks");
        tvclasses = findViewById(R.id.tvclasses);
        tvexam = findViewById(R.id.tvexam);
        tvdate = findViewById(R.id.tvdate);
        tvsubject = findViewById(R.id.tvsubject);

        edtMax = findViewById(R.id.edit_max);
        edtMin = findViewById(R.id.edit_min);
        Bundle bundle = getIntent().getExtras();
        strExamScheduleId =bundle.getString("ExamSceduleId");
        strClassId=bundle.getString("ClassId");
        strStaffId=bundle.getString("StaffId");
        strIsMarksSet= bundle.getString(getString(R.string.key_IsMarksSet));
        syllabusData=bundle.getString("SyllabusData");
        try {
            JSONObject sylObject = new JSONObject(syllabusData);
            strDate = sylObject.getString(getString(R.string.key_Date));
            if(!(strDate.equalsIgnoreCase("9999"))){

                tvdate.setText(strDate);
            }else{
                strDate=" ";
                tvdate.setVisibility(View.GONE);
            }
            /*strExamTime = sylObject.getString(getString(R.string.key_ExamTime));
            if(!(strExamTime.equalsIgnoreCase("9999"))){

               strExamTime=" ";
            }*/
            strExamSyllabusId = sylObject.getString(getString(R.string.key_ExamSyllabusId));

            strClass = sylObject.getString(getString(R.string.key_Clas));
            tvclasses.setText(strClass);
            //strSyllabus = sylObject.getString(getString(R.string.key_Syllabus));
            strExam = sylObject.getString(getString(R.string.key_Exam));
            tvexam.setText(strExam);
            strSubject = sylObject.getString(getString(R.string.key_Subject));
            tvsubject.setText(strSubject);
            strScholastics = sylObject.getString(getString(R.string.key_Scholastics));
            allEvents();




        }  catch (Exception e) {
            Log.e("oncreate_MaxMin",e.toString());
        }
        btnProceed = findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strMax = edtMax.getText().toString();
                strMin = edtMin.getText().toString();
                try {
                    intMax = Integer.parseInt(strMax);
                    fltMin = Float.parseFloat(strMin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ((strMax==null||strMax.equals(""))||(strMin==null||strMin.equals(""))) {
                    Toast.makeText(SetMaxMinMarksActivity.this,"enter all fields ",Toast.LENGTH_LONG).show();
                } else if (fltMin>intMax) {
                    Toast.makeText(SetMaxMinMarksActivity.this,"wrong data",Toast.LENGTH_LONG).show();
                }else if(strClassId==null||strClassId.equalsIgnoreCase("")){
                    Toast.makeText(SetMaxMinMarksActivity.this, "couldnt get class", Toast.LENGTH_SHORT).show();
                }else if(strStaffId==null||strStaffId.equalsIgnoreCase("")){
                    Toast.makeText(SetMaxMinMarksActivity.this, "couldnt get staff", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        outObject = new JSONObject();
                        JSONObject examData = new JSONObject();
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//dd/MM/yyyy
                        Date now = new Date();
                        String strDate = sdfDate.format(now);
                        examData.put("ExamScheduleId", strExamScheduleId);
                        examData.put("SetBy", strStaffId);
                        examData.put("MaxMarks", strMax);
                        examData.put("MinMarks", strMin);
                        examData.put("SetDate", strDate);
                        outObject.put("OperationName", "insertExamResultMaxMin");
                        outObject.put("examData", examData);
                        new insertExamResultMaxMin().execute();
                        //new GetExamScheduleByExam().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //{"OperationName":"outObject","examData":{"ExamScheduleId":"653","SetBy":"5","MaxMarks":"10","MinMarks":"2","SetDate":"30-01-2019 10:30:31",}}


                }
            }

        });
    }
    public  void allEvents(){
        edtMin.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
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



    class insertExamResultMaxMin extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "examsectionOperation.jsp";
       // String url =  "http://192.168.0.101:8080/vinvidyawebapi/operation/examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SetMaxMinMarksActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching class wise exam schedule...");
            progressDialog.show();




        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();


            try {

                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                JSONObject inObject = new JSONObject(responseText);


                String strStatus = inObject.getString(getString(R.string.key_Status));
                int resId = inObject.getInt(getString(R.string.key_resultId));
                strExamResultId = String.valueOf(resId);
                String message = inObject.getString(getString(R.string.key_Message));
                //showAlert(strStatus,message);
                if (resId>0) {
                    Intent newIntent = new Intent(SetMaxMinMarksActivity.this,MarksListActivity.class);
                    newIntent.putExtra("max",intMax);
                    newIntent.putExtra("min",fltMin);
                    newIntent.putExtra("ExamScheduleId", strExamScheduleId);
                    newIntent.putExtra("ClassId",strClassId);
                    newIntent.putExtra("ExamResultId",strExamResultId);
                    newIntent.putExtra(getString(R.string.key_IsMarksSet),strIsMarksSet);
                    newIntent.putExtra("StaffId",strStaffId);
                    newIntent.putExtra("Class",strClass);
                    newIntent.putExtra("Subject",strSubject);

                    newIntent.putExtra("Scholastics",strScholastics);
                    startActivity(newIntent);
                } else {
                    showAlert(strStatus,message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

            /*outString=values[0].toString();
            Log.e("Tag Outstring",outString);*/

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
    }

    public void showAlert(final String strTitle, final String strMessage){
        SetMaxMinMarksActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetMaxMinMarksActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                builder.setView(convertView);
                builder.setCancelable(true);
                builder.setNegativeButton("ok", null);
                builder.setTitle(strTitle);
                builder.setMessage(strMessage);
                builder.show();
            }
        });
    }
}