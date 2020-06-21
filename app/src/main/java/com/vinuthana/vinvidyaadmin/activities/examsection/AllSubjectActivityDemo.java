package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StudentData;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.transform.Result;

public class AllSubjectActivityDemo extends AppCompatActivity {
    String strStatus, strClassId, strSchoolId, strExamId, strGradeVisible;
    String strClass, strExam, strClassSection;
    JSONArray studentArray = new JSONArray();
    JSONArray subjectArray = new JSONArray();
    JSONArray resultArray = new JSONArray();
    GetResponse response = new GetResponse();
    ArrayList<StudentData> studList = new ArrayList<StudentData>();
    ArrayList<String> subjectList = new ArrayList<>();
    ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
    LinearLayout lynrlytAllSubExamStudentDetailsDemo;
    TableLayout tblLytAllSubjectExamMarksDemo;
    ProgressDialog progressDialog;
    TextView tvAllsubEmresClsDemo, tvAllSubEmresExamDemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subject_demo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Marks List");
        init();
        new GetStudentList().execute();

    }

    public void init() {
        Bundle bundle = getIntent().getExtras();

        strSchoolId = bundle.getString(getString(R.string.key_SchoolId));
        strClassId = bundle.getString(getString(R.string.key_ClassId));
        strClass = bundle.getString(getString(R.string.key_Class));
        strExam = bundle.getString(getString(R.string.key_Exam));
        strExamId = bundle.getString(getString(R.string.key_ExamId));
        strClassSection = bundle.getString(getString(R.string.key_Cls_Section));
        lynrlytAllSubExamStudentDetailsDemo = findViewById(R.id.lynrlytAllSubExamStudentDetailsDemo);
        tblLytAllSubjectExamMarksDemo = findViewById(R.id.tblLytAllSubjectExamMarksDemo);
        tvAllsubEmresClsDemo = findViewById(R.id.tvAllsubEmresClsDemo);
        tvAllSubEmresExamDemo = findViewById(R.id.tvAllSubEmresExamDemo);
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

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllSubjectActivityDemo.this);
            progressDialog.setMessage("Fetching result please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            JSONObject outObject = new JSONObject();
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseStudentsList));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    studentArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
                }
            } catch (Exception ex) {
                Log.e("doInBackground EXE", ex.toString());
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
            for (int i = 0; i < studentArray.length(); i++) {
                try {

                    JSONObject object = studentArray.getJSONObject(i);
                    StudentData studentData = new StudentData();
                    studentData.setRollNo(object.getString(getString(R.string.key_RollNo)));
                    studentData.setName(object.getString(getString(R.string.key_studentname)));
                    studentData.setStudentId(object.getString(getString(R.string.key_StudentDetailsId)));
                    studentData.setChecked(true);
                    studList.add(studentData);

                } catch (Exception e) {
                    Log.e("onPostExecute Exe", e.toString());
                }
            }
            new GetExamwiseSubjectList().execute();
        }
    }

    class GetExamwiseSubjectList extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamwiseSubjectList));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("ExamWiseSub", "outObject = " + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("ExamWiseSub", "RespText " + responseText);
                JSONObject inObject = new JSONObject(responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    subjectArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                } else {
                       /*runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                               LayoutInflater inflater = getLayoutInflater();
                               View convertView = (View) inflater.inflate(R.layout.custom, null);
                               builder.setView(convertView);
                               builder.setCancelable(true);
                               builder.setNegativeButton("Ok", null);
                               builder.setTitle("Alert");
                               builder.setMessage("Marks not entered for this exam");
                               builder.show();
                           }
                       });*/
                }

            } catch (Exception e) {
                Log.e("ExamSubDoinBackExe", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (subjectArray.length() != 0) {
                for (int i = 0; i < subjectArray.length(); i++) {
                    try {
                        JSONObject object = subjectArray.getJSONObject(i);
                        String strSubject = object.getString(getString(R.string.key_Subject));
                        subjectList.add(i, strSubject);

                    } catch (Exception e) {
                        Log.e("ExamWiseSubEXE", e.toString());
                    }
                }
                new GetExamwiseResultList().execute();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(AllSubjectActivityDemo.this, "No Marks for this exam", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                progressDialog.dismiss();
                onBackPressed();
            }

        }
    }

    class GetExamwiseResultList extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                if(strSchoolId.equalsIgnoreCase("2")){
                      outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamResultTabularGDGB));
                }else if(strSchoolId.equalsIgnoreCase("13")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamResultTabularMNJT));
                }else if(strSchoolId.equalsIgnoreCase("5")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamResultTabularHBBN));
                }

                //outObject.put(getString(R.string.key_OperationName),getString(R.string.web_getStaffResultDisplayExamwise));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("ExamWiseRes", "outObject = " + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("ExamWiseRes", "RespText " + responseText);
                JSONObject inObject = new JSONObject(responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    resultArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }

            } catch (Exception e) {
                Log.e("ExamResDoinBackExe", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject object = resultArray.getJSONObject(i);
                    HashMap<String, String> hashMap = new HashMap<String, String>();

                    String strStudentName = object.getString(getString(R.string.key_Name));
                    String strRollNo = object.getString(getString(R.string.key_RollNo));
                    hashMap.put(getString(R.string.key_RollNo), strRollNo);
                    hashMap.put(getString(R.string.key_StudentName), strStudentName);
                    Double dblTotal = 0.0;
                    for (int j = 0; j < subjectList.size(); j++) {
                        String strSubjectMarks = object.getString(subjectList.get(j));
                        hashMap.put(subjectList.get(j), strSubjectMarks);
                            /*Double dblMarks=getDoubleValue(strSubjectMarks);
                            dblTotal+=dblMarks;
                            hashMap.put(getString(R.string.key_Total),String.valueOf(dblTotal));*/
                    }
                    if(strSchoolId.equalsIgnoreCase("2")){
                        if (!(strClass.length() > 2)) {
                            int cls = Integer.parseInt(strClass);
                            if ((strExamId.equalsIgnoreCase("31") || strExamId.equalsIgnoreCase("32")) && (cls > 5)) {
                                String strGrade = object.getString(getString(R.string.key_Overall_Grade));
                                strGradeVisible = strGrade;
                                hashMap.put(getString(R.string.key_Grade), strGrade);
                            }
                        }
                    }else if(strSchoolId.equalsIgnoreCase("5")){
                        String strGrade = object.getString(getString(R.string.key_Grade));
                        hashMap.put(getString(R.string.key_Grade), strGrade);
                    }else if(strSchoolId.equalsIgnoreCase("13")){

                    }

                    /*if (strExamId.equalsIgnoreCase("31") || strExamId.equalsIgnoreCase("32") ) {
                        String strGrade = object.getString(getString(R.string.key_Overall_Grade));
                        strGradeVisible = strGrade;
                        hashMap.put(getString(R.string.key_Grade), strGrade);

                    }*/

                    resultList.add(hashMap);
                }

            } catch (Exception e) {
                Log.e("ExamResOnPost", e.toString());
            }
            progressDialog.dismiss();
            addHeaders();
            addData();
        }
    }

    Double getDoubleValue(String strSubjectMarks) {
        if (strSubjectMarks != null && !strSubjectMarks.equalsIgnoreCase("") && !strSubjectMarks.equalsIgnoreCase("AB")) {
            return Double.parseDouble(strSubjectMarks);
        } else {
            return 0.0;
        }
    }

    private TextView getTextView(String strTitle, int color, int typeface, int bgcolor) {
        TextView tv;
        tv = new TextView(AllSubjectActivityDemo.this);
        tv.setText(strTitle.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(20, 20, 20, 20);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgcolor);


        return tv;
    }

    public void addHeaders() {
        TableRow tableRow;
        tableRow = new TableRow(AllSubjectActivityDemo.this);
        tableRow.addView(getTextView("Roll No", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.card_text)));
        tableRow.addView(getTextView("Student Name", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.card_text)));
        for (int i = 0; i < subjectList.size(); i++) {
            tableRow.addView(getTextView(subjectList.get(i), Color.WHITE, Typeface.BOLD, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.card_text)));
        }
        if(strSchoolId.equalsIgnoreCase("2")) {
            if (!(strClass.length() > 2)) {
                int cls = Integer.parseInt(strClass);
                if ((strExamId.equalsIgnoreCase("31") || strExamId.equalsIgnoreCase("32")) && (cls > 5)) {
                    tableRow.addView(getTextView("Over All Grade", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.card_text)));
                }
            }
        }else if(strSchoolId.equalsIgnoreCase("5")){
            tableRow.addView(getTextView("Over All Grade", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.card_text)));
        }

        tblLytAllSubjectExamMarksDemo.addView(tableRow);
    }

    public void addData() {
        Log.e("addData", resultList.toString());
        for (int i = 0; i < resultList.size(); i++) {
            TableRow tableRow;
            Log.e("Row of ", String.valueOf(i));
            tableRow = new TableRow(AllSubjectActivityDemo.this);
            tblLytAllSubjectExamMarksDemo.addView(tableRow);
            if (i % 2 == 0) {
                tableRow.addView(getTextView(resultList.get(i).get("RollNo"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.tabEvenColor)));
                tableRow.addView(getTextView(resultList.get(i).get("StudentName"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.tabEvenColor)));
                for (int j = 0; j < subjectList.size(); j++) {
                    tableRow.addView(getTextView(resultList.get(i).get(subjectList.get(j)),
                            Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabEvenColor)));
                }if(strSchoolId.equalsIgnoreCase("2")){
                    if (!(strClass.length() > 2)) {
                        int cls = Integer.parseInt(strClass);
                        if ((strExamId.equalsIgnoreCase("31") || strExamId.equalsIgnoreCase("32")) && (cls > 5)) {
                            tableRow.addView(getTextView(resultList.get(i).get("Grade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabEvenColor)));
                        }
                    }
                }else if(strSchoolId.equalsIgnoreCase("5")){
                    tableRow.addView(getTextView(resultList.get(i).get("Grade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabEvenColor)));
                }

                        /*if((strExamId.equalsIgnoreCase("31")||strExamId.equalsIgnoreCase("32"))&&(!strGradeVisible.equalsIgnoreCase("0"))) {
                                tableRow.addView(getTextView(resultList.get(i).get("Grade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabEvenColor)));
                            }*/
            } else {
                tableRow.addView(getTextView(resultList.get(i).get(getString(R.string.key_RollNo)), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabOddColor)));
                tableRow.addView(getTextView(resultList.get(i).get("StudentName"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabOddColor)));
                for (int j = 0; j < subjectList.size(); j++) {
                    tableRow.addView(getTextView(resultList.get(i).get(subjectList.get(j)),
                            Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabOddColor)));
                }
                if(strSchoolId.equalsIgnoreCase("2")){
                    if (!(strClass.length() > 2)) {
                        int cls = Integer.parseInt(strClass);
                        if ((strExamId.equalsIgnoreCase("31") || strExamId.equalsIgnoreCase("32")) && (cls > 5)) {
                            tableRow.addView(getTextView(resultList.get(i).get("Grade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabOddColor)));
                        }
                    }
                }else if(strSchoolId.equalsIgnoreCase("5")){
                    tableRow.addView(getTextView(resultList.get(i).get("Grade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabOddColor)));
                }

                        /*if((strExamId.equalsIgnoreCase("31")||strExamId.equalsIgnoreCase("32"))&&(!strGradeVisible.equalsIgnoreCase("0"))) {

                                    tableRow.addView(getTextView(resultList.get(i).get("Grade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectActivityDemo.this, R.color.tabOddColor)));
                                }
                            }*/
            }
            tvAllsubEmresClsDemo.setText(strClassSection);
            tvAllSubEmresExamDemo.setText(strExam);
            lynrlytAllSubExamStudentDetailsDemo.setVisibility(View.VISIBLE);
        }
    }
}