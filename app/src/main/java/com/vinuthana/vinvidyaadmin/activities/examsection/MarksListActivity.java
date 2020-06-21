package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StudentMarksAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentMarksDropdownAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MarksListActivity extends AppCompatActivity {
    ListView listView;
    Button btnSubmit;
    TextView tViewMarks;
    StudentMarksAdapter studentMarksAdapter;
    StudentMarksDropdownAdapter studentMarksDropdownAdapter;
    JSONArray insertArray;
    JSONArray jsonStudentArray,jsonGradeArray;
    JSONObject insertStudentMarks, getStudentList,syllabusDataObject;
    int max;
    float min;
    Boolean boolIsGradeVise,boolStudentFound;
    ArrayList<String>strGradelist= new ArrayList<String>();
    ArrayList<String>strGradeValuelist= new ArrayList<String>();
    HashMap<String,String> HashMapGradeValuelist = new HashMap<String,String>();
    Session session;
    TextView tvMarksListMaxMarks,tvMarksListMinMarks,tvMarksListClass,tvMarksListSubject;
    String strClassId,strExamScheduleId,strExamResultId,strStaffId;
    String strSubject,strScholastics,strSyllabusData;
    String strStatus,strSchoolId,message,strIsMarksSet,strClass,strAcademicYearId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_list);
        listView = findViewById(R.id.lv_st_marks);
        tViewMarks = findViewById(R.id.tView_marks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSubmit = findViewById(R.id.submit);
        studentMarksAdapter = new StudentMarksAdapter(MarksListActivity.this);
       // studentMarksDropdownAdapter = new StudentMarksDropdownAdapter(MarksListActivity.this,jsonGradeArray);
        Bundle extras = getIntent().getExtras();
        /*max = extras.getInt("max");
        min = extras.getInt("min");*/
        try {
            strClassId = extras.getString("ClassId");
            strExamScheduleId = extras.getString("ExamScheduleId");
            strIsMarksSet = extras.getString(getString(R.string.key_IsMarksSet));
            strExamResultId = extras.getString("ExamResultId");
            strStaffId = extras.getString("StaffId");
            strSyllabusData = extras.getString("SyllabusData");
            if(strIsMarksSet.equalsIgnoreCase("0")){
                strSubject=extras.getString("Subject");
                strClass=extras.getString("Class");
            }else if(strIsMarksSet.equalsIgnoreCase("1")) {
                syllabusDataObject = new JSONObject(strSyllabusData);
                strSubject=syllabusDataObject.getString(getString(R.string.key_Subject));
                strClass=syllabusDataObject.getString(getString(R.string.key_Clas));
            }


            session = new Session(MarksListActivity.this);
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strScholastics = extras.getString("Scholastics");
            if(!(strSchoolId.equalsIgnoreCase("5"))){
                if (strScholastics.equals("1"))
                    boolIsGradeVise = true;
                else {
                    boolIsGradeVise = false;
                     new GetGrades().execute();
                }
            }else{
                boolIsGradeVise = true;
            }

       /* Boolean bolScholastics = Boolean.valueOf(strScholastics);
        boolIsGradeVise = bolScholastics;*/
            //params
            //strClassId = "5";
        }catch (Exception e){
            Log.e("marksListOncreate",e.toString());
        }
        initialisation();
        AllEvents();
    }

    public void initialisation(){
        tvMarksListSubject=findViewById(R.id.tvMarksListSubject);
        tvMarksListMaxMarks=findViewById(R.id.tvMarksListMaxMarks);
        tvMarksListMinMarks=findViewById(R.id.tvMarksListMinMarks);
        tvMarksListClass=findViewById(R.id.tvMarksListClass);
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
    public void AllEvents() {
        try {


            JSONObject examData = new JSONObject();
            getStudentList = new JSONObject();

            examData.put(getString(R.string.key_ClassId),strClassId);
            examData.put(getString(R.string.key_ExamResultId),strExamResultId);


            getStudentList.put(getString(R.string.key_OperationName), getString(R.string.web_getLeftOutStudentsForMarksEntry));

            getStudentList.put(getString(R.string.key_examData),examData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new getStudentAssync().execute();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray;
                if (!boolIsGradeVise) {
                    tViewMarks.setText("Grade");
                    jsonArray = StudentMarksDropdownAdapter.jsonArray;
                //    new GetGrades().execute();

                } else {
                    tViewMarks.setText("Marks");
                    jsonArray = StudentMarksAdapter.jsonArray;
                }

                insertArray = new JSONArray();
                Boolean boolData = true;
                try {
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            //if (jsonArray.getJSONObject(i).getString("marks") == null || jsonArray.getJSONObject(i).getString("marks").equals("")||jsonArray.getJSONObject(i).getString("marks").equalsIgnoreCase("A")) {
                            if (jsonArray.getJSONObject(i).getString("marks") == null || jsonArray.getJSONObject(i).getString("marks").equals("")) {
                                Toast.makeText(MarksListActivity.this, "Enter marks for all students", Toast.LENGTH_SHORT).show();
                                boolData = false;
                                break;
                            }else if(jsonArray.getJSONObject(i).getString("marks").equalsIgnoreCase("A")){
                                Toast.makeText(MarksListActivity.this, "Marks should be integer", Toast.LENGTH_SHORT).show();
                                boolData = false;
                                break;
                            } else {
                                jsonObject.put("ExamResultId",strExamResultId);
                                jsonObject.put("AddedBy",strStaffId);
                                jsonObject.put("StudentId", jsonArray.getJSONObject(i).getString("StudentDetailsId"));
                                jsonObject.put("ObtainedMarks",jsonArray.getJSONObject(i).getString("marks"));
                                SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//dd/MM/yyyy
                                Date now = new Date();
                                String strDate = sdfDate.format(now);
                                jsonObject.put("SetDate",strDate);
                                insertArray.put(jsonObject);

                            }
                        }
                        new insertStudentMarksAssync().execute();
                    } else {
                        Toast.makeText(MarksListActivity.this, "Enter marks for all students", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(MarksListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("marksActivitybtnclick",e.toString());
                }
                if (boolData) {
                    insertStudentMarks = new JSONObject();
                    JSONObject marksArray = new JSONObject();
                    try {
                        marksArray.put("MarksArray",insertArray);
                        insertStudentMarks.put("OperationName", "insertExamResult");
                        insertStudentMarks.put("examData",marksArray);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                  //  new insertStudentMarksAssync().execute();
                }
            }
        });
    }

    /*{"OperationName":"getClasswiseStudentsList","classSubjectData":{"ClassId":"5"}}*/

    /*{"OperationName":"insertGDGBExamResult","examData":{"MarksArray":[{"ExamResultId":"144","AddedBy":"5","StudentId":"3","ObtainedMarks":"5","SetDate":"30-01-2019"},
        {"ExamResultId":"144","AddedBy":"5","StudentId":"11","ObtainedMarks":"5","SetDate":"30-01-2019"},
        {"ExamResultId":"144","AddedBy":"5","StudentId":"13","ObtainedMarks":"5","SetDate":"30-01-2019"}]}}*/


    class getStudentAssync extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;



      String url = AD.url.base_url + "examsectionOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progressDialog = new ProgressDialog(MarksListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching student details...");
            progressDialog.show();




        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            try {
                JSONObject outObject = getStudentList;
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                JSONObject inObject = new JSONObject(responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String message = inObject.getString(getString(R.string.key_Message));

                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    boolStudentFound= true;
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonStudentArray=inObject.getJSONArray(getString(R.string.key_Result));

                } else {
                    boolStudentFound = false;
                    showFailureAlert(strStatus,message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            try {
                JSONObject object= values[0].getJSONObject(0);
                max =Integer.parseInt(object.getString(getString(R.string.key_MaxMarks)));
                min =Float.parseFloat(object.getString(getString(R.string.key_MinMarks)));

            }catch (Exception e){
               Log.e("MarkList onProgress",e.toString());
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (boolStudentFound) {
                if (!boolIsGradeVise) {
                    //studentMarksDropdownAdapter = new StudentMarksDropdownAdapter(MarksListActivity.this,strGradelist, HashMapGradeValuelist);
                    //new GetGrades().execute();
                    studentMarksDropdownAdapter = new StudentMarksDropdownAdapter(MarksListActivity.this,strGradelist, strGradeValuelist);
                    //studentMarksDropdownAdapter = new StudentMarksDropdownAdapter(MarksListActivity.this,jsonGradeArray);

                    studentMarksDropdownAdapter.setData(jsonStudentArray, max, min,strSchoolId);
                    listView.setAdapter(studentMarksDropdownAdapter);
                } else {
                    studentMarksAdapter = new StudentMarksAdapter(MarksListActivity.this);
                    studentMarksAdapter.setData(jsonStudentArray, max, min);
                    listView.setAdapter(studentMarksAdapter);
                }
            }else{
                tvMarksListMaxMarks.setVisibility(View.GONE);
                tvMarksListMinMarks.setVisibility(View.GONE);
            }

            MarksListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String classText = "<font color=#FF8C00>Class : </font> <font color=#ffffff>"+strClass+"</font>";
                    tvMarksListClass.setText(Html.fromHtml(classText));
                    String SubjectText = "<font color=#FF8C00>Subject : </font> <font color=#ffffff>"+strSubject+"</font>";
                    tvMarksListSubject.setText(Html.fromHtml(SubjectText));
                    String strMax=String.valueOf(max);
                    String strMin=String.valueOf(min);
                    String maxText = "<font color=#FF8C00>max : </font> <font color=#ffffff>"+strMax+"</font>";
                    tvMarksListMaxMarks.setText(Html.fromHtml(maxText));
                    String minText = "<font color=#FF8C00>min : </font> <font color=#ffffff>"+strMin+"</font>";
                    tvMarksListMinMarks.setText(Html.fromHtml(minText));



                    /*tvMarksListClass.setText(strClass);
                    tvMarksListSubject.setText(strSubject);
                    tvMarksListMaxMarks.setText(String.valueOf(max));
                    tvMarksListMinMarks.setText(String.valueOf(min));*/

                }
            });
            progressDialog.dismiss();


        }


    }


    class GetGrades extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String strStatus,strMessage;
        String url = AD.url.base_url + "examsectionOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MarksListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching student details...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName),getString(R.string.web_getGradesBySchoolId));
                JSONObject examData= new JSONObject();
                examData.put(getString(R.string.key_SchoolId),strSchoolId);
                examData.put(getString(R.string.key_AcademicYearId),strAcademicYearId);
                outObject.put(getString(R.string.key_examData),examData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                JSONObject inObject = new JSONObject(responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                strMessage = inObject.getString(getString(R.string.key_Message));

                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    boolStudentFound= true;
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonGradeArray=inObject.getJSONArray(getString(R.string.key_Result));

                } else {
                    boolStudentFound = false;
                    showFailureAlert(strStatus,message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
           /* try {
                JSONObject object= values[0].getJSONObject(0);
                max =Integer.parseInt(object.getString(getString(R.string.key_MaxMarks)));
                min =Float.parseFloat(object.getString(getString(R.string.key_MinMarks)));

            }catch (Exception e){
                Log.e("MarkList onProgress",e.toString());
            }*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
             for(int i=0;i<jsonGradeArray.length();i++){
                 try {
                     JSONObject object= jsonGradeArray.getJSONObject(i);
                     strGradelist.add(object.getString(getString(R.string.key_Grade)));
                     String strGradValue=object.getString(getString(R.string.key_GradingValue));
                    // HashMapGradeValuelist.put(String.valueOf(i),strGradValue );
                     strGradeValuelist.add(strGradValue );

                 } catch (Exception e) {
                     Log.e("Exeception at","Execption at getGrades onPostExecute "+e.toString());
                 }
             }
            }

            /*if (boolStudentFound) {
                if (!boolIsGradeVise) {
                    studentMarksDropdownAdapter = new StudentMarksDropdownAdapter(MarksListActivity.this,jsonGradeArray);

                    studentMarksDropdownAdapter.setData(jsonStudentArray, max, min,strSchoolId);
                    listView.setAdapter(studentMarksDropdownAdapter);
                } else {
                    studentMarksAdapter = new StudentMarksAdapter(MarksListActivity.this);
                    studentMarksAdapter.setData(jsonStudentArray, max, min);
                    listView.setAdapter(studentMarksAdapter);
                }
            }else{
                tvMarksListMaxMarks.setVisibility(View.GONE);
                tvMarksListMinMarks.setVisibility(View.GONE);
            }
            MarksListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String classText = "<font color=#FF8C00>Class : </font> <font color=#ffffff>"+strClass+"</font>";
                    tvMarksListClass.setText(Html.fromHtml(classText));
                    String SubjectText = "<font color=#FF8C00>Subject : </font> <font color=#ffffff>"+strSubject+"</font>";
                    tvMarksListSubject.setText(Html.fromHtml(SubjectText));
                    String strMax=String.valueOf(max);
                    String strMin=String.valueOf(min);
                    String maxText = "<font color=#FF8C00>max : </font> <font color=#ffffff>"+strMax+"</font>";
                    tvMarksListMaxMarks.setText(Html.fromHtml(maxText));
                    String minText = "<font color=#FF8C00>min : </font> <font color=#ffffff>"+strMin+"</font>";
                    tvMarksListMinMarks.setText(Html.fromHtml(minText));



                    *//*tvMarksListClass.setText(strClass);
                    tvMarksListSubject.setText(strSubject);
                    tvMarksListMaxMarks.setText(String.valueOf(max));
                    tvMarksListMinMarks.setText(String.valueOf(min));*//*

                }
            });
            */
            progressDialog.dismiss();


        }


    }




    class insertStudentMarksAssync extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "examsectionOperation.jsp";
       // String url =  "http://192.168.0.101:8080/vinvidyawebapi/operation/examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MarksListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Inserting marks please wait...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();


            try {
                JSONObject outObject = insertStudentMarks;
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                JSONObject inObject = new JSONObject(responseText);


              strStatus = inObject.getString(getString(R.string.key_Status));
             message = inObject.getString(getString(R.string.key_Message));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    showAlert(strStatus,message);
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
        MarksListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MarksListActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                builder.setView(convertView);
                builder.setCancelable(true);
                builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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

    public void showFailureAlert(final String strTitle, final String strMessage){
        MarksListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MarksListActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                builder.setView(convertView);
                builder.setCancelable(true);
                builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(strTitle.equalsIgnoreCase(getString(R.string.key_Failure))){
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