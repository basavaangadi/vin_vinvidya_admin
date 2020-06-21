package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AllSubjectExamMarksActivity extends AppCompatActivity {

    CheckConnection connection = new CheckConnection();
    String strStatus,strClassId,strSchoolId,strExamId,strExam,strClassSection;
    JSONArray studentArray=new JSONArray();
    JSONArray subjectArray=new JSONArray();
    JSONArray resultArray=new JSONArray();
    JSONArray exmArray=new JSONArray();
    ArrayList<StudentData>studList= new ArrayList<StudentData>();
    ArrayList<String>subjectList= new ArrayList<>();
    ArrayList<HashMap<String,String>>resultList= new ArrayList<HashMap<String,String>>();

    GetResponse response=new GetResponse();
    TextView tvAllsubEmresClsRVK, tvAllSubEmresExamRVK;
    TableLayout tblLytAllSubjectExamMarksRVK;
    ProgressDialog progressDialog;
    LinearLayout lynrlytAllSubExamStudentDetailsRVK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_subject_exam_marks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Marks List");
        if (!connection.netInfo(AllSubjectExamMarksActivity.this)) {
            connection.buildDialog(AllSubjectExamMarksActivity.this).show();
        } else {
            try {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle = intent.getExtras();
                String bunData = bundle.toString();
                Log.e("oncreate", bunData);
                strSchoolId = bundle.getString(getString(R.string.key_SchoolId));

                strClassId = bundle.getString(getString(R.string.key_ClassId), strClassId);

                strExamId = bundle.getString(getString(R.string.key_ExamId));

                strExam = bundle.getString(getString(R.string.key_Exam));

                strClassSection = bundle.getString(getString(R.string.key_Cls_Section));

                new GetStudentList().execute();
            } catch (Exception e) {
                Toast.makeText(AllSubjectExamMarksActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("examMarksList ", e.toString());
            }
        }

        initialisation();

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

    public void initialisation() {
        tvAllsubEmresClsRVK = findViewById(R.id.tvAllsubEmresClsRVK);
        tvAllSubEmresExamRVK = findViewById(R.id.tvAllSubEmresExamRVK);
        tblLytAllSubjectExamMarksRVK = findViewById(R.id.tblLytAllSubjectExamMarksRVK);
        lynrlytAllSubExamStudentDetailsRVK = findViewById(R.id.lynrlytAllSubExamStudentDetailsRVK);
    }
    class GetStudentList extends AsyncTask<String,JSONArray,Void> {
        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllSubjectExamMarksActivity.this);
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for(int i=0;i<studentArray.length();i++){
                try {
                    JSONObject object = studentArray.getJSONObject(i);
                    StudentData studentData= new StudentData();
                    studentData.setRollNo(object.getString(getString(R.string.key_RollNo)));
                    studentData.setName(object.getString(getString(R.string.key_studentname)));
                    studentData.setStudentId(object.getString(getString(R.string.key_StudentDetailsId)));
                    studentData.setChecked(true);
                    studList.add(studentData);

                }catch (Exception e){
                    Log.e("onPostStudList",e.toString());
                    Toast.makeText(AllSubjectExamMarksActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            new GetExamwiseSubjectList().execute();
        }
    }

    class GetExamwiseSubjectList extends AsyncTask<String,JSONArray,Void>{
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... strings) {

                try{
                    JSONObject outObject= new JSONObject();
                    outObject.put(getString(R.string.key_OperationName),getString(R.string.web_getStaffExamwiseSubjectList));
                    JSONObject examData= new JSONObject();
                    examData.put(getString(R.string.key_ClassId),strClassId);
                    examData.put(getString(R.string.key_ExamId),strExamId);
                    outObject.put(getString(R.string.key_examData),examData);
                    Log.e("ExamWiseSub","outObject = "+outObject.toString());
                    String responseText=response.getServerResopnse(url,outObject.toString());
                    Log.e("ExamWiseSub","RespText "+responseText);
                    JSONObject inObject=new JSONObject(responseText);
                    strStatus= inObject.getString(getString(R.string.key_Status));
                    if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
                        publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                        subjectArray= new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                    }

                }catch (Exception e){
                    Log.e("ExamSubDoinBackExe",e.toString());
                }
                return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                for(int i=0;i<subjectArray.length();i++){
                    try {
                        JSONObject object = subjectArray.getJSONObject(i);
                        String strSubject= object.getString(getString(R.string.key_Subject));
                        subjectList.add(i,strSubject);

                    }catch (Exception e){
                        Log.e("ExamSubOnPost",e.toString());
                        Toast.makeText(AllSubjectExamMarksActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                new GetExamwiseResultList().execute();
        }
    }
    class GetExamwiseResultList extends AsyncTask<String,JSONArray,Void>{
        String url = AD.url.base_url + "examsectionOperation.jsp";
        protected Void doInBackground(String... strings) {
            try{
                JSONObject outObject=new JSONObject();
                outObject.put(getString(R.string.key_OperationName),getString(R.string.web_getStaffExamResultRVK));
                //outObject.put(getString(R.string.key_OperationName),getString(R.string.web_getStaffResultDisplayExamwise));
                JSONObject examData= new JSONObject();
                examData.put(getString(R.string.key_ClassId),strClassId);
                examData.put(getString(R.string.key_ExamId),strExamId);
                outObject.put(getString(R.string.key_examData),examData);
                Log.e("ExamWiseRes","outObject = "+outObject.toString());
                String responseText=response.getServerResopnse(url,outObject.toString());
                Log.e("ExamWiseRes","RespText "+responseText);
                JSONObject inObject=new JSONObject(responseText);
                strStatus= inObject.getString(getString(R.string.key_Status));
                if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    resultArray= new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }

            }catch (Exception e){
                Log.e("ExamResDoinBackExe",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                for(int i=0;i<resultArray.length();i++){
                    JSONObject object=resultArray.getJSONObject(i);
                    HashMap<String,String>hashMap= new HashMap<String,String>();
                    Log.e("onPostObject","at "+String.valueOf(i)+" "+object.toString());
                   String strName= object.getString(getString(R.string.key_Name));
                   String strRollNo=object.getString(getString(R.string.key_RollNo));
                   hashMap.put(getString(R.string.key_RollNo),strRollNo);
                   hashMap.put(getString(R.string.key_Name),strName);

                   for(int j=0;j<subjectList.size();j++){
                       String strSubjectMarks=object.getString(subjectList.get(j));
                       hashMap.put(subjectList.get(j),strSubjectMarks);
                   }
                   String strTotal= object.getString(getString(R.string.key_Total));
                   String strMaxTotal=object.getString(getString(R.string.key_MaxTotal));
                   String strPercentage= object.getString(getString(R.string.key_Percentage));
                   String strTotalGrade=object.getString(getString(R.string.key_TotalGrade));

                    hashMap.put(getString(R.string.key_Total),strTotal);
                    hashMap.put(getString(R.string.key_MaxTotal),strMaxTotal);
                    hashMap.put(getString(R.string.key_Percentage),strPercentage);
                    hashMap.put(getString(R.string.key_TotalGrade),strTotalGrade);
                    resultList.add(hashMap);
                }

            }catch (Exception e){
                Log.e("resultListonPostExe",e.toString());
                Toast.makeText(AllSubjectExamMarksActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            addHeader();
            addData();
        }

        private  TextView getTextView(String strTitle,int color,int typeface,int bgcolor){
            TextView tv;
            tv=new TextView(AllSubjectExamMarksActivity.this);
            tv.setText(strTitle.toUpperCase());
            tv.setTextColor(color);
            tv.setPadding(20,20,20,20);
            tv.setTypeface(Typeface.DEFAULT,typeface);
            tv.setBackgroundColor(bgcolor);


            return tv;
        }

        public void addHeader(){
            TableRow tableRow;
            tableRow = new TableRow(AllSubjectExamMarksActivity.this);
            tableRow.addView(getTextView("Roll No", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.card_text)));
            tableRow.addView(getTextView("Student Name", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.card_text)));
            for(int i=0;i<subjectList.size();i++){
                tableRow.addView(getTextView(subjectList.get(i),Color.WHITE,Typeface.BOLD,ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.card_text)));
            }
            tableRow.addView(getTextView("Max Total",Color.WHITE,Typeface.BOLD,ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.card_text)));
            tableRow.addView(getTextView("Total",Color.WHITE,Typeface.BOLD,ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.card_text)));
            tableRow.addView(getTextView("Per",Color.WHITE,Typeface.BOLD,ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.card_text)));
            tableRow.addView(getTextView("Grade",Color.WHITE,Typeface.BOLD,ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.card_text)));
            tblLytAllSubjectExamMarksRVK.addView(tableRow);
            Log.e("addHeader", resultList.toString());
        }

        public  void addData() {
            Log.e("addData", resultList.toString());
            for (int i = 0; i < resultList.size(); i++) {
                TableRow tableRow;
                Log.e("Row of ", String.valueOf(i));
                tableRow = new TableRow(AllSubjectExamMarksActivity.this);
                tblLytAllSubjectExamMarksRVK.addView(tableRow);
                if (i % 2 == 0) {
                    tableRow.addView(getTextView(resultList.get(i).get("RollNo"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabEvenColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("Name"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabEvenColor)));
                    for (int j = 0; j < subjectList.size(); j++) {
                        tableRow.addView(getTextView(resultList.get(i).get(subjectList.get(j)),
                                Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabEvenColor)));
                    }
                    tableRow.addView(getTextView(resultList.get(i).get("MaxTotal"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabEvenColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("Total"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabEvenColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("Percentage"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabEvenColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("TotalGrade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabEvenColor)));
                } else {
                    tableRow.addView(getTextView(resultList.get(i).get("RollNo"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabOddColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("Name"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabOddColor)));
                    for (int j = 0; j < subjectList.size(); j++) {
                        tableRow.addView(getTextView(resultList.get(i).get(subjectList.get(j)),
                                Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabOddColor)));
                    }
                    tableRow.addView(getTextView(resultList.get(i).get("MaxTotal"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabOddColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("Total"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabOddColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("Percentage"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabOddColor)));
                    tableRow.addView(getTextView(resultList.get(i).get("TotalGrade"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(AllSubjectExamMarksActivity.this, R.color.tabOddColor)));
                }
            }
            tvAllsubEmresClsRVK.setText(strClassSection);
            tvAllSubEmresExamRVK.setText(strExam);
            lynrlytAllSubExamStudentDetailsRVK.setVisibility(View.VISIBLE);
        }
    }


        /*public void showDataInTable(JSONArray array) {
            tblLytAllSubjectExamMarksRVK.removeAllViews();
            ArrayList<ArrayList<String>> headerNData = new ArrayList<ArrayList<String>>();

            try {
                ArrayList<String> headers = new ArrayList<>();
                Iterator<String> headerList = array.getJSONObject(0).keys();
                while (headerList.hasNext()) {
                    headers.add(headerList.next());
                }
                Log.e("Header", headers.toString());
                headerNData.add(headers);
                for (int i = 0; i < array.length(); i++) {
                    ArrayList<String> data = new ArrayList<>();
                    JSONObject object = array.getJSONObject(i);
                    for (String key : headers) {
                        data.add(object.getString(key));
                    }
                    headerNData.add(data);
                }
                Log.e("headerNData", headerNData.toString());
                generateTableRowAnsColumn(headerNData);
            } catch (Exception e) {
                Log.e("showTAbledata", e.toString());
            }
        }


        public void generateTableRowAnsColumn(ArrayList<ArrayList<String>> headerNData) {
        int rowCount = 0;
        for (ArrayList<String> rows : headerNData) {
            TableRow row = new TableRow(AllSubjectExamMarksActivity.this);

            if (rowCount % 2 == 0) {
                row.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                row.setBackgroundColor(Color.parseColor("#d7d7d7"));
            }
            for (String column : rows) {

                TextView textViewColumn = new TextView(AllSubjectExamMarksActivity.this);
                textViewColumn.setText(column);
                textViewColumn.setPadding(10, 10, 10, 10);
                row.addView(textViewColumn);
            }
            tblLytAllSubjectExamMarksRVK.addView(row);
            rowCount++;
        }
    }

        class GetAllSubjectResult extends AsyncTask<String, JSONArray, Void> {
            ProgressDialog progressDialog;
            String url = AD.url.base_url + "examsectionOperation.jsp";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AllSubjectExamMarksActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Fetching Data...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(String... params) {
                GetResponse response = new GetResponse();
                JSONObject outObject = new JSONObject();

                try {
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamResultTabularGDGB));
                    JSONObject examData = new JSONObject();
                    examData.put(getString(R.string.key_ClassId), strClassId);
                    examData.put(getString(R.string.key_ExamId), strExamId);
                    outObject.put(getString(R.string.key_examData), examData);
                    Log.e("Tag", "outObject =" + outObject.toString());
                    String responseText = response.getServerResopnse(url, outObject.toString());
                    JSONObject inObject = new JSONObject(responseText);

                    Log.e("Tag", "responseText is =" + responseText);
                    String strStatus = inObject.getString(getString(R.string.key_Status));
                    if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                        publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                        // showDataInTable();

                    } else {
                        AllSubjectExamMarksActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AllSubjectExamMarksActivity.this);
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
                    Log.e("DoinBGKEMARKS", ex.toString());
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(JSONArray... values) {
                super.onProgressUpdate(values);
                exmArray = values[0];
                showDataInTable(exmArray);

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                try {
                    JSONObject object = exmArray.getJSONObject(0);

                    tvAllsubEmresClsRVK.setText(strClassSection);
                    tvAllSubEmresExamRVK.setText(strExam);


                    if (strExam.length() > 0) {
                        lynrlytAllSubExamStudentDetailsRVK.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    Log.e("onPost", e.toString());
                }
            }
        }*/

}
