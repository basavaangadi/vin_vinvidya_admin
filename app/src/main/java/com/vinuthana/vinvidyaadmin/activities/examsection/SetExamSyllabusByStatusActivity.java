package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.SetExamSyllabus;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetExamSyllabusByStatusActivity extends AppCompatActivity {
    String strCurrentObject,strStaffId,strExamId,strExam,strSetDate,strSyllabus,base64Syllabus;
    String strSubject,strSubjectId,strClass,strClassId,strDate,strExamTime,strExamScheduleId;
    String strStatus;
    TextView tvExamSylbsThroStatusClass,tvExamSylbsThroStatusSubject,tvExamSylbsThroStatusExam;
    TextView tvExamSylbsThroStatusExamTime,tvExamSylbsThroStatusDate;
    EditText edtExamSylbsThroStatusSyllabus;
    Button btnSetExamScheduleTroughStatus;
    JSONObject currentObject;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_exam_syllabus_by_status);
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Exam Syllabus");

        strCurrentObject=bundle.getString("ExamStatusData");

        strStaffId=bundle.getString("StaffId");
        strExamId=bundle.getString("ExamId");
        strExam=bundle.getString("Exam");

        init();
        allEvents();

        setData();
        
    }
    public  void init(){
        tvExamSylbsThroStatusDate=findViewById(R.id.tvExamSylbsThroStatusDate);
        btnSetExamScheduleTroughStatus=findViewById(R.id.btnSetExamScheduleTroughStatus);
        tvExamSylbsThroStatusExam=findViewById(R.id.tvExamSylbsThroStatusExam);
        edtExamSylbsThroStatusSyllabus=findViewById(R.id.edtExamSylbsThroStatusSyllabus);

        tvExamSylbsThroStatusExamTime=findViewById(R.id.tvExamSylbsThroStatusExamTime);

        tvExamSylbsThroStatusSubject=findViewById(R.id.tvExamSylbsThroStatusSubject);
        tvExamSylbsThroStatusClass=findViewById(R.id.tvExamSylbsThroStatusClass);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        strSetDate = df.format(c.getTime());

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
    public void allEvents(){



        btnSetExamScheduleTroughStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strSyllabus = edtExamSylbsThroStatusSyllabus.getText().toString();

                //Toast.makeText(getActivity(), startFromTime, Toast.LENGTH_SHORT).show();
                if (strSyllabus.equalsIgnoreCase("") ||strSyllabus.equalsIgnoreCase(null)) {
                    Toast.makeText(SetExamSyllabusByStatusActivity.this, "Date, Start Time and End Time can't be blank", Toast.LENGTH_SHORT).show();
                } else {
                    new setExamSyllabus().execute();
                }
            }
        });
    }
    public void setData(){
        try{

            currentObject=new JSONObject(strCurrentObject);
            strClass=currentObject.getString("Class");
            strClassId=currentObject.getString("ClassId");

            //strExam=currentObject.getString("Exam");
            strSubject=currentObject.getString("Subject");

            strSubjectId=currentObject.getString("SubjectId");

            strExamScheduleId= currentObject.getString("ExamScheduleId");


            strExamTime=currentObject.getString("ExamTime");
            strDate=currentObject.getString("Date");
            
            

            //strExamId=currentObject.getString("ExamId");
            String strCardclass = getColoredSpanned("Class  ", "#FF8C00");
            String strTextClass = getColoredSpanned(strClass,"#ffffff");
            tvExamSylbsThroStatusClass.setText(Html.fromHtml(strCardclass+" : "+strTextClass));

            String strCardSubject = getColoredSpanned("Subject ", "#FF8C00");
            String strTextSubject = getColoredSpanned(strSubject,"#ffffff");
            tvExamSylbsThroStatusSubject.setText(Html.fromHtml(strCardSubject+" : "+strTextSubject));
            String strCardExam = getColoredSpanned("Exam ", "#FF8C00");
            String strTextExam = getColoredSpanned(strExam,"#ffffff");
            tvExamSylbsThroStatusExam.setText(Html.fromHtml(strCardExam+" : "+strTextExam));
            String strCardDate = getColoredSpanned("Date", "#FF8C00");
            String strTextDate = getColoredSpanned(strDate,"#ffffff");
            tvExamSylbsThroStatusDate.setText(Html.fromHtml(strCardDate+" : "+strTextDate));
           //tvExamSylbsThroStatusDate.setText("Date : "+strDate);
            String strCardExamTime = getColoredSpanned("ExamTime  ", "#FF8C00");
            String strTextExamTime = getColoredSpanned(strExamTime,"#ffffff");
            tvExamSylbsThroStatusExamTime.setText(Html.fromHtml(strCardExamTime+" : "+strTextExamTime));
            //tvExamSylbsThroStatusExamTime.setText(strExamTime);
        }catch(Exception e){
            Toast.makeText(SetExamSyllabusByStatusActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    class setExamSyllabus extends AsyncTask<String,JSONArray,Void> {
        ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SetExamSyllabusByStatusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Setting Exam Schedule...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_setStaffExamSyllabus));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_SetBy), strStaffId);
                examData.put(getString(R.string.key_SetDate), strSetDate);
                examData.put(getString(R.string.key_ExamSceduleId), strExamScheduleId);
                //examData.put(getString(R.string.key_ExamId), strExamId);
                String base64Syllabus= StringUtil.textToBase64(strSyllabus);
                examData.put(getString(R.string.key_Syllabus), base64Syllabus);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);

                strMsg = inObject.getString(getString(R.string.key_Message));
                strStatus=inObject.getString(getString(R.string.key_Status));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        try {
                            alertTitle = strStatus;
                            alertMessage=strMsg;
                            if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))){
                                showAlert(alertTitle,alertMessage);
                            }else{
                                showAlert("Error","Something went while setting the syllabus");
                            }

                        }catch (Exception e){
                            Log.e("setSyllabusDoInBgk",e.toString());
                            Toast.makeText(SetExamSyllabusByStatusActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public void showAlert(String strTitle,String strMessage){
            SetExamSyllabusByStatusActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetExamSyllabusByStatusActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(true);
                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
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
