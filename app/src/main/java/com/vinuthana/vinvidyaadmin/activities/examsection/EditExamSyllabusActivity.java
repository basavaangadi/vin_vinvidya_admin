package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditExamSyllabusActivity extends AppCompatActivity {
    String strExamSyllabusId,strClassId,strStaffId,strSyllabusData,strExamScheduleId;
    String strSyllabus,base64Syllabus,strScduleData,strEditDate,strSubject,strExamTime;
    String strDate,strClass,strExam;
    JSONObject sylabusData;
    EditText edtExmSyllabus;
    Button btnEditExamSyllabus;
    TextView tvEditExamSyllabusClass,tvEditExamSyllabusExam,tvEditExamSyllabusSubject;
    TextView tvEditExamSyllabusDate,tvEditExamSyllabusExamTime,tvEditExamSyllabusExamEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exam_syllabus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit exam Syllabus");
        Bundle bundle = getIntent().getExtras();
        strExamSyllabusId=bundle.getString("ExamSyllabusId");
        strClassId=bundle.getString("ClassId");
        strStaffId=bundle.getString("StaffId");
        strSyllabusData=bundle.getString("SyllabusData");

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        strEditDate = df.format(c.getTime());

        try{

            sylabusData=new JSONObject(strSyllabusData);
            strExamScheduleId= sylabusData.getString("ExamScheduleId");
            strSyllabus=sylabusData.getString("Syllabus");
            strSubject=sylabusData.getString("Subject");
            strExamTime=sylabusData.getString("ExamTime");
            strDate=sylabusData.getString("Date");
            strClass=sylabusData.getString("Class");
            strExam=sylabusData.getString("Exam");

        }catch (Exception e){

            Log.e("init",e.toString());
        }

        init();
        allEvents();
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

        tvEditExamSyllabusClass=findViewById(R.id.tvEditExamSyllabusClass);
        tvEditExamSyllabusExam=findViewById(R.id.tvEditExamSyllabusExam);
        tvEditExamSyllabusSubject=findViewById(R.id.tvEditExamSyllabusSubject);
        tvEditExamSyllabusDate=findViewById(R.id.tvEditExamSyllabusDate);
       // tvEditExamSyllabusExamTime=findViewById(R.id.tvEditExamSyllabusExamTime);
       // tvEditExamSyllabusExamEnd=findViewById(R.id.tvEditExamSyllabusExamEnd);
        edtExmSyllabus=findViewById(R.id.edtExmSyllabus);
        btnEditExamSyllabus=findViewById(R.id.btnEditExamSyllabus);


        tvEditExamSyllabusClass.setText(strClass);
        tvEditExamSyllabusSubject.setText(strSubject);
        //tvEditExamSyllabusExamTime.setText(strExamTime);
        tvEditExamSyllabusDate.setText(strDate);
        edtExmSyllabus.setText(strSyllabus);
        tvEditExamSyllabusExam.setText(strExam);
    }

    public void allEvents(){
        btnEditExamSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSyllabus= edtExmSyllabus.getText().toString();
                if(!strSyllabus.equalsIgnoreCase("")) {
                    new EditExamSyllabus().execute();
                }else{
                    Toast.makeText(EditExamSyllabusActivity.this, "Enter the Syllabus to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class EditExamSyllabus extends AsyncTask<String,JSONArray,Void>{
        ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditExamSyllabusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Editing Exam Syllabus...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editExamSyllabus));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ExamSyllabusId),strExamSyllabusId);
                examData.put(getString(R.string.key_SetBy), strStaffId);

                examData.put(getString(R.string.key_EditDate), strEditDate);
                examData.put(getString(R.string.key_ExamSceduleId), strExamScheduleId);
                base64Syllabus= StringUtil.textToBase64(strSyllabus);
                examData.put(getString(R.string.key_Syllabus), base64Syllabus);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        try {
                            alertTitle = inObject.getString(getString(R.string.key_Status));
                            alertMessage=strMsg;
                            showAlert(alertTitle,alertMessage);
                        }catch (Exception e){
                            Log.e("EditSyllabusDoInBgk",e.toString());
                            Toast.makeText(EditExamSyllabusActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        /*if (strMsg.equals("Syllabus added sucessfully")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equals("Syllabus has been already added")) {
                            alertMessage = strMsg;
                            alertTitle = "fail";
                        } else {
                            alertTitle = "fail";
                            alertMessage = "Syllabus could not added";
                        }*/

                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public void showAlert(String strTitle,String strMessage){
            EditExamSyllabusActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditExamSyllabusActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(true);
                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(strTitle.equalsIgnoreCase(getString(R.string.key_Success))){
                                onBackPressed();
                            }

                        }
                    });
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}
