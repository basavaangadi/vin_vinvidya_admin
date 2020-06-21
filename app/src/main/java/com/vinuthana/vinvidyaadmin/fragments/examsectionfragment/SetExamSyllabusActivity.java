package com.vinuthana.vinvidyaadmin.fragments.examsectionfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SetExamSyllabusActivity extends AppCompatActivity {

    TextView tvClassExmSylbs, tvSubjectExmSylbs, tvExamExmSylbs, tvTimeExmSylbs;
    EditText edtExmSyllabus;
    Button btnSetExmSyllabus;
    private Session session;
    String strSetDate, strStaffId, strClass, strSylabus, strClassId, strSubjectId, strExam, strSchoolId, strExmSchedule,
            strExamSchdlId, strDate, strSubject,strScheduleDate,strTime ,strAcademicYearId,strCurrentObject,strStatus;
    JSONObject currentObject;
    View view;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_exam_syllabus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Exam Syllabus");
        if (!connection.netInfo(SetExamSyllabusActivity.this)) {
            connection.buildDialog(SetExamSyllabusActivity.this).show();
        } else {


            session = new Session(SetExamSyllabusActivity.this);
            HashMap<String, String> user = session.getUserDetails();

            //ArrayList<String> strUser = new ArrayList<>();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            Bundle bundle = getIntent().getExtras();
            strCurrentObject=bundle.getString("SchduleData");
            strClassId= bundle.getString("classId");
            init();
            allEvents();
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());






            //new GetExamSchduleDetails().execute();
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

    public void init() {

        btnSetExmSyllabus = findViewById(R.id.btnSetExmSyllabus);
        tvClassExmSylbs = findViewById(R.id.tvClassExmSylbs);
        tvSubjectExmSylbs = findViewById(R.id.tvSubjectExmSylbs);
        tvExamExmSylbs = findViewById(R.id.tvExamExmSylbs);
        tvTimeExmSylbs = findViewById(R.id.tvTimeExmSylbs);
        edtExmSyllabus = findViewById(R.id.edtExmSyllabus);

        try {
            currentObject = new JSONObject(strCurrentObject);
            strExamSchdlId=currentObject.getString("ExamScheduleId");
            strClass=currentObject.getString("Class");
            strDate=currentObject.getString("Date");
            strExam=currentObject.getString("Exam");
            strSubject=currentObject.getString("Subject");
            //strTime=currentObject.getString("Time");



        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
            /*strClassId = bundle.getString("classId");
            strSubjectId = bundle.getString("subject");
            strExam = bundle.getString("exam");
            strExmSchedule = bundle.getString("exmSchedule");
            strExamSchdlId = bundle.getString("strExamSchdlId");*/

        tvClassExmSylbs.setText(strClass);
        tvSubjectExmSylbs.setText(strSubject);
        tvExamExmSylbs.setText(strExam);
        tvTimeExmSylbs.setText(strDate);

    }

    private void allEvents() {

        btnSetExmSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtExmSyllabus.getText().toString().length() < 3 || edtExmSyllabus.getText().toString().length() > 1000) {
                    Toast.makeText(SetExamSyllabusActivity.this, "Syllabus should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                } else {
                    strSylabus = edtExmSyllabus.getText().toString();
                    //startFromTime = edtExmSyllabusDate.getText().toString() + " " + edtExmSyllabusTime.getText().toString();
                /*if (edtExmSyllabusDate.getText().toString().equals("") && edtExmSyllabusTime.getText().toString().equals("") && edtExmSyllabus.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Fields can't be blank ", Toast.LENGTH_SHORT).show();
                } else {*/
                    new SetExamSylbs().execute();
                }

                //}
            }
        });
    }

    class SetExamSylbs extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SetExamSyllabusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding Exam Syllabus...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_setStaffExamSyllabus));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_SetBy), strStaffId);
                examData.put(getString(R.string.key_SetDate), strSetDate);
                examData.put(getString(R.string.key_ExamSceduleId), strExamSchdlId);
                strSylabus = edtExmSyllabus.getText().toString();
                String base64Syllabus= StringUtil.textToBase64(strSylabus);
                examData.put(getString(R.string.key_Syllabus), base64Syllabus);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                       alertMessage=strMsg;
                       try {
                           alertTitle = inObject.getString(getString(R.string.key_status));
                           showAlert(alertMessage, alertTitle);
                       }catch (Exception e){
                           Log.e("doinBgk",e.toString());
                           Toast.makeText(SetExamSyllabusActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                       }

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
            /*ExamSheduleRecycler recyclerAdapter = new ExamSheduleRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewSetExamSylbs.setLayoutManager(layoutManager);
            recyclerViewSetExamSylbs.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSetExamSylbs.setAdapter(recyclerAdapter);*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            edtExmSyllabus.setText("");
            /*edtExmSyllabusDate.setText("");
            edtExmSyllabusTime.setText("");*/
        }
    }

    class GetExamSchduleDetails extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffexamSchduleDetailsById));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_Clas), strClassId);
                examData.put(getString(R.string.key_Subject), strSubjectId);
                examData.put(getString(R.string.key_Exam), strExam);
                examData.put(getString(R.string.key_ExamTime), strExmSchedule);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                }/* else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", null);
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }*/
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            progressDialog.dismiss();
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetExamSyllabusActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
                    onBackPressed();
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
