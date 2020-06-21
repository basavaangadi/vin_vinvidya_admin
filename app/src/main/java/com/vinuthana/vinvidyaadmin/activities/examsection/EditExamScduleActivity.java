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
import android.view.Gravity;
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

public class EditExamScduleActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    Calendar hmFbByDt = Calendar.getInstance();
    Button btnEditExmSchdl;
  Spinner spnrEditExmSchdlExam;
    private Session session;
    EditText edtEditExmSchdlDate, edtEditExmSchdlEndTime, edtEditExmSchdlStartTime;
    String strSetDate, strStaffId, strClass, strExam, strClassId, strSubjectId, strExamId,strSubject;
    String strSchoolId,strExamScduleId,strStartTime,strExamScheduleId,strExamTime,strEndTime;
    private int mYear, mMonth, mDay;
    Calendar startTime = Calendar.getInstance();
    Calendar endTime = Calendar.getInstance();
    int startHour = startTime.get(Calendar.HOUR_OF_DAY);
    int startMinute = startTime.get(Calendar.MINUTE);
    TextView tvEditExamScduleClass,tvEditExamScduleSubject;
    int endHour = endTime.get(Calendar.HOUR_OF_DAY);
    int endMinute = endTime.get(Calendar.MINUTE);
    TimePickerDialog startTimePicker, endTimePicker;
    String startFromTime, strDate, strExamList,strCurrentObject,strAcademicYearId, endToTime;
    String strFrmMinute = "";
    String strToMinute = "";
    JSONObject currentObject;
    JSONArray examList;

    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtEditExmSchdlDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exam_scdule);
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit exam Schedule");
        strExamList= bundle.getString("ExamListArray");
        strCurrentObject=bundle.getString("SchduleData");
        strClassId=bundle.getString("ClassId");
        strStaffId=bundle.getString("StaffId");
        mYear = hmFbByDt.get(Calendar.YEAR);
        mMonth = hmFbByDt.get(Calendar.MONTH);
        mDay = hmFbByDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(EditExamScduleActivity.this, mlistner, mYear, mMonth, mDay);
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
    public void init(){
        edtEditExmSchdlDate=findViewById(R.id.edtEditExmSchdlDate);
        btnEditExmSchdl=findViewById(R.id.btnEditExmSchdl);
        spnrEditExmSchdlExam=findViewById(R.id.spnrEditExmSchdlExam);
        edtEditExmSchdlEndTime=findViewById(R.id.edtEditExmSchdlEndTime);
        edtEditExmSchdlStartTime=findViewById(R.id.edtEditExmSchdlStartTime);
        tvEditExamScduleSubject=findViewById(R.id.tvEditExamScduleSubject);
        tvEditExamScduleClass=findViewById(R.id.tvEditExamScduleClass);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        strSetDate = df.format(c.getTime());
        try{
        examList=new JSONArray(strExamList);
            ExamSpinnerDataAdapter adapter = new ExamSpinnerDataAdapter(examList, EditExamScduleActivity.this);
            spnrEditExmSchdlExam.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void allEvents() {
        edtEditExmSchdlDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtEditExmSchdlDate) {
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

        edtEditExmSchdlStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimePicker = new TimePickerDialog(EditExamScduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

                        edtEditExmSchdlStartTime.setText(selectedHour + ":" + strFrmMinute + AM_PM);
                    }
                }, startHour, startMinute, false);
                startTimePicker.setTitle("Select Time");
                startTimePicker.show();
            }
        });

        edtEditExmSchdlEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimePicker = new TimePickerDialog(EditExamScduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        edtEditExmSchdlEndTime.setText(selectedHour + ":" + strToMinute + AM_PM);
                    }
                }, endHour, endMinute, false);
                endTimePicker.setTitle("Select Time");
                endTimePicker.show();
            }
        });




        spnrEditExmSchdlExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*String selected = (String) adapterView.getItemAtPosition(i);
                if (i > 0){
                    Toast.makeText(getActivity(), "Selected " + selected, Toast.LENGTH_SHORT).show();
                }*/
                TextView tmpView = (TextView) spnrEditExmSchdlExam.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strExamId);
                //Toast.makeText(getActivity(), strExam + " You Clicked on", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnEditExmSchdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate = edtEditExmSchdlDate.getText().toString();
                startFromTime = edtEditExmSchdlStartTime.getText().toString();
                endToTime = edtEditExmSchdlEndTime.getText().toString();
                //Toast.makeText(getActivity(), startFromTime, Toast.LENGTH_SHORT).show();
                int exmPos=spnrEditExmSchdlExam.getSelectedItemPosition();
                if(exmPos==-1||exmPos==0){
                    Toast toast=Toast.makeText(EditExamScduleActivity.this, "select Exam", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
               else if (edtEditExmSchdlDate.getText().toString().equals("") && edtEditExmSchdlStartTime.getText().toString().equals("") && edtEditExmSchdlEndTime.getText().toString().equals("")) {
                    Toast.makeText(EditExamScduleActivity.this, "Date, Start Time and End Time can't be blank", Toast.LENGTH_SHORT).show();
                } else {
                    new EditExamScdule().execute();
                }
            }
        });

    }
    public  void setData(){
        try{

            currentObject=new JSONObject(strCurrentObject);
            strClass=currentObject.getString("Class");
            strDate=currentObject.getString("Date");
            strExam=currentObject.getString("Exam");
            strSubject=currentObject.getString("Subject");
            strExamTime=currentObject.getString("ExamTime");
            strExamScheduleId=currentObject.getString("ExamScheduleId");
            strEndTime=currentObject.getString("EndTime");
            strStartTime=currentObject.getString("StartTime");
            strSubjectId=currentObject.getString("SubjectId");
            strExamId=currentObject.getString("ExamId");

            tvEditExamScduleClass.setText(strClass);
            tvEditExamScduleSubject.setText(strSubject);
            edtEditExmSchdlDate.setText(strDate);
            edtEditExmSchdlStartTime.setText(strStartTime);
            edtEditExmSchdlEndTime.setText(strEndTime);
            int pos=getSpinnerPosition();
            spnrEditExmSchdlExam.setSelection(pos);
        }catch(Exception e){
            Toast.makeText(EditExamScduleActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public int getSpinnerPosition(){
        int pos=0;

        for( int i=0;i<examList.length();i++)
        { try {
            String strItem = examList.getJSONObject(i).getString("ExamId");

            if(strItem.equalsIgnoreCase(strExamId)){
                pos=i;
                break;
            }

        }catch (Exception e){
            e.printStackTrace();

        }

        }
        return pos;
    }
    class EditExamScdule extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditExamScduleActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Editing Exam Schedule...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editExamSchdule));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ExamScheduleId), strExamScheduleId);
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
                String strStatus=inObject.getString(getString(R.string.key_Status));
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
            EditExamScduleActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditExamScduleActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(true);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
    }

}
