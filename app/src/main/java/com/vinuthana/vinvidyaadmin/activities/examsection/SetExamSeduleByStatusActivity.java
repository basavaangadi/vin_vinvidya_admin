package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ExamSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetExamSeduleByStatusActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    Calendar hmFbByDt = Calendar.getInstance();
    Button btnSetExamScheduleTroughStatus;

    private Session session;
    EditText edtSetExamScduleThroStatusDate, edtSetExamScduleThroStatusEndTime, edtSetExamScduleThroStatusStartTime;
    String strSetDate, strStaffId, strClass,strExam, strClassId, strSubjectId;
    String strExamId,strSubject,strStatus;

    private int mYear, mMonth, mDay;
    Calendar startTime = Calendar.getInstance();
    Calendar endTime = Calendar.getInstance();
    int startHour = startTime.get(Calendar.HOUR_OF_DAY);
    int startMinute = startTime.get(Calendar.MINUTE);
    TextView tvSetExamScduleThroStatusClass,tvSetExamScduleThroStatusSubject;
    TextView tvSetExamScduleThroStatusExam;
    int endHour = endTime.get(Calendar.HOUR_OF_DAY);
    int endMinute = endTime.get(Calendar.MINUTE);
    TimePickerDialog startTimePicker, endTimePicker;
    String startFromTime, strDate, strCurrentObject, endToTime;
    String strFrmMinute = "";
    String strToMinute = "";
    JSONObject currentObject;


    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetExamScduleThroStatusDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_exam_sedule_by_status);
        
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Exam Schedule");
        
        strCurrentObject=bundle.getString("ExamStatusData");
       
        strStaffId=bundle.getString("StaffId");
        strExamId=bundle.getString("ExamId");
        strExam=bundle.getString("Exam");
        mYear = hmFbByDt.get(Calendar.YEAR);
        mMonth = hmFbByDt.get(Calendar.MONTH);
        mDay = hmFbByDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(SetExamSeduleByStatusActivity.this, mlistner, mYear, mMonth, mDay);
        init();
        allEvents();

        setData();
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
    public  void init(){
        edtSetExamScduleThroStatusDate=findViewById(R.id.edtSetExamScduleThroStatusDate);
        btnSetExamScheduleTroughStatus=findViewById(R.id.btnSetExamScheduleTroughStatus);
        tvSetExamScduleThroStatusExam=findViewById(R.id.tvSetExamScduleThroStatusExam);
        
        edtSetExamScduleThroStatusEndTime=findViewById(R.id.edtSetExamScduleThroStatusEndTime);
        edtSetExamScduleThroStatusStartTime=findViewById(R.id.edtSetExamScduleThroStatusStartTime);
        tvSetExamScduleThroStatusSubject=findViewById(R.id.tvSetExamScduleThroStatusSubject);
        tvSetExamScduleThroStatusClass=findViewById(R.id.tvSetExamScduleThroStatusClass);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        strSetDate = df.format(c.getTime());
        
    }
    public void allEvents(){
        edtSetExamScduleThroStatusDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtSetExamScduleThroStatusDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        /*edtExmSchdlTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtExmSchdlTime) {

                }
                return false;
            }
        });*/

        edtSetExamScduleThroStatusStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimePicker = new TimePickerDialog(SetExamSeduleByStatusActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM = " AM";
                        if (selectedHour >= 12) {
                            AM_PM = " PM";
                            if (selectedHour >= 13 && selectedHour < 24) {
                                selectedHour -= 12;
                            } else {
                                selectedHour = 12;
                            }
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        strFrmMinute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            strFrmMinute = "0" + strFrmMinute;

                        }

                        edtSetExamScduleThroStatusStartTime.setText(selectedHour + ":" + strFrmMinute + AM_PM);
                    }
                }, startHour, startMinute, false);
                startTimePicker.setTitle("Select Time");
                startTimePicker.show();
            }
        });

        edtSetExamScduleThroStatusEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimePicker = new TimePickerDialog(SetExamSeduleByStatusActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM = " AM";
                        if (selectedHour >= 12) {
                            AM_PM = " PM";
                            if (selectedHour >= 13 && selectedHour < 24) {
                                selectedHour -= 12;
                            } else {
                                selectedHour = 12;
                            }
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        strToMinute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            strToMinute = "0" + strToMinute;

                        }
                        edtSetExamScduleThroStatusEndTime.setText(selectedHour + ":" + strToMinute + AM_PM);
                    }
                }, endHour, endMinute, false);
                endTimePicker.setTitle("Select Time");
                endTimePicker.show();
            }
        });




       


        btnSetExamScheduleTroughStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate = edtSetExamScduleThroStatusDate.getText().toString();
                startFromTime = edtSetExamScduleThroStatusStartTime.getText().toString();
                endToTime = edtSetExamScduleThroStatusEndTime.getText().toString();
                //Toast.makeText(getActivity(), startFromTime, Toast.LENGTH_SHORT).show();
                if (edtSetExamScduleThroStatusDate.getText().toString().equalsIgnoreCase("") ||edtSetExamScduleThroStatusStartTime.getText().toString().equalsIgnoreCase("") || edtSetExamScduleThroStatusEndTime.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SetExamSeduleByStatusActivity.this, "Date, Start Time and End Time can't be blank", Toast.LENGTH_SHORT).show();
                } else {
                    new setExamSedule().execute();
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
            //strExamId=currentObject.getString("ExamId");

            tvSetExamScduleThroStatusClass.setText(strClass);
            tvSetExamScduleThroStatusSubject.setText(strSubject);
            tvSetExamScduleThroStatusExam.setText(strExam);
          
        }catch(Exception e){
            Toast.makeText(SetExamSeduleByStatusActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    class setExamSedule extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SetExamSeduleByStatusActivity.this);
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_setStaffexamSchdule));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ExamScheduleId), "0");
                examData.put(getString(R.string.key_SubjectId), strSubjectId);
                examData.put(getString(R.string.key_SetBy), strStaffId);
                examData.put(getString(R.string.key_SetDate), strSetDate);
                examData.put(getString(R.string.key_ExamOnDate), strDate);
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_ExamStartTime), startFromTime);
                examData.put(getString(R.string.key_ExamEndTime), endToTime);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));
                 strStatus=inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    showAlert(strStatus,strMsg);
                } else {
                    showAlert("Error","Error Occured..");
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
        }
        public void showAlert(String strTitle,String strMessage){
            SetExamSeduleByStatusActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetExamSeduleByStatusActivity.this);
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
    }
}
