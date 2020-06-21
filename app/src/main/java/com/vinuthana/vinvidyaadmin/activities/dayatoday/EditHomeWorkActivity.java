package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SubjectSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewCurrentHomeWork;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class EditHomeWorkActivity extends AppCompatActivity {

    String strSchoolId , strAcademicYearId, strClass, strClassId, strSubjectId="", strHomeworkId,strSubject;
    String strClassList,strSectionList,strSubjectList,strClassListBun,strSectionListBun,strSubjectListBun;
    String strSelectedclass, strSelectedSection, strSelectedSubject,strSelectedChpater,strSelectedHomework,strSelectedDate;
    String strCurrentObject,strStaffId;

    EditText edtTxtBxHomeworkFeedbackHomeworkDate,edtTxtHomeWorkFeedbackDiscription,edtTxtHomeWorkFeedbackChapterName;
    AutoCompleteTextView autoCompleteHomeworkFeedbackSection, autoCompleteHomeworkFeedbackClass, autoCompleteHomeworkFeedbackSubject;
    TextView tvHomeworkFeedbackHomeworkDate;
    TextView tvHomeworkFeedbackClass;
    Button btnHomeworkFeedbackEdit;

    Spinner spnrHomeworkFeedbackSubject;
    ArrayList<String> subjectArrayList= new ArrayList<String>();
    ArrayList<String>sectionArrayList = new ArrayList<String>();
    ArrayList<String>classArrayList = new ArrayList<String>();

    JSONObject object;
    JSONArray array, result,classArray,subjectArray,sectionArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_home_work);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit homework");
        Bundle bundle = getIntent().getExtras();

        init();
        strHomeworkId = bundle.getString("HomeworkId");
        strSchoolId = bundle.getString("SchoolId");
        strStaffId = bundle.getString("StaffId");
        strAcademicYearId = bundle.getString("AcademicYearId");
        strSubjectListBun=bundle.getString("SubjectList");
       // strClassListBun=bundle.getString("ClassList");
       // strSectionListBun=bundle.getString("SectionList");
        strCurrentObject=bundle.getString("currentData");


        try {
            object= new JSONObject(strCurrentObject);
            strClassId=object.getString("ClassId");
            strClass=object.getString("Class");
            strSubject = object.getString("Subject");
         //   classArray = new JSONArray(strClassListBun);
            subjectArray = new JSONArray(strSubjectListBun);
         //   sectionArray = new JSONArray(strSectionListBun);

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONObject currentObject=new JSONObject(strCurrentObject);
           // classArray=new JSONArray(strClassListBun);
            subjectArray=new JSONArray(strSubjectListBun);
            //sectionArray= new JSONArray(strSectionListBun);
            SubjectSpinnerDataAdapter subjectSpnrAdapter= new SubjectSpinnerDataAdapter(subjectArray,EditHomeWorkActivity.this);
            spnrHomeworkFeedbackSubject.setAdapter(subjectSpnrAdapter);
            String strHomeworkDate=currentObject.getString("Date");
            String strHomeworkChapter=currentObject.getString("ChapterName");
            String strDiscription=currentObject.getString("HomeWork");
            strSelectedclass=currentObject.getString("Class");
            String  strTmpclass=strSelectedclass.substring(0,strSelectedclass.length()-1);
            strSelectedSection=strSelectedclass.substring(strSelectedclass.length()-1,strSelectedclass.length());
            strSelectedSubject=currentObject.getString("Subject");
            allEvents();
            int subjectPosition=getSubjectPosition();
            spnrHomeworkFeedbackSubject.setSelection(subjectPosition);
            tvHomeworkFeedbackHomeworkDate.setText(strHomeworkDate);
            edtTxtHomeWorkFeedbackChapterName.setText(strHomeworkChapter);
            edtTxtHomeWorkFeedbackDiscription.setText(strDiscription);
            tvHomeworkFeedbackClass.setText(strClass);

           } catch (JSONException e) {
            e.printStackTrace();
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
    public int getSubjectPosition(){
        int pos=0;

        for( int i=0;i<strSubjectListBun.length();i++)
        { try {
            JSONObject object = subjectArray.getJSONObject(i);
            String objectSubject=object.getString("Subject");
            if(objectSubject.equalsIgnoreCase(strSelectedSubject)){
                pos=i;
                break;

            }

        }catch (Exception e){
            e.printStackTrace();

        }

        }
        return pos;
    }


    public void init() {
        tvHomeworkFeedbackHomeworkDate = findViewById(R.id.tvHomeworkFeedbackHomeworkDate);
        edtTxtHomeWorkFeedbackChapterName = findViewById(R.id.edtTxtHomeWorkFeedbackChapterName);
        edtTxtHomeWorkFeedbackDiscription = findViewById(R.id.edtTxtHomeWorkFeedbackDiscription);
        tvHomeworkFeedbackClass=findViewById(R.id.tvHomeworkFeedbackClass);

        spnrHomeworkFeedbackSubject=findViewById(R.id.spnrHomeworkFeedbackSubject);

        btnHomeworkFeedbackEdit = findViewById(R.id.btnHomeworkFeedbackEdit);



    }

    public void allEvents() {


        spnrHomeworkFeedbackSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrHomeworkFeedbackSubject.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "Subject selected  " +strSubjectId.toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnHomeworkFeedbackEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int subPos=spnrHomeworkFeedbackSubject.getSelectedItemPosition();

                if(subPos==0){
                    Toast toast=Toast.makeText(EditHomeWorkActivity.this,"",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{
                    new EditHomeworkWithDeatils().execute();
                }

            }
        });

    }





    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditHomeWorkActivity.this);
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


    class EditHomeworkWithDeatils extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String url = AD.url.base_url + "homeworkOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(EditHomeWorkActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait, editing the homework..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editHomework));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_HomeworkId),strHomeworkId);
                homeworkData.put(getString(R.string.key_Date), tvHomeworkFeedbackHomeworkDate.getText().toString());
                homeworkData.put(getString(R.string.key_StaffId), strStaffId);
                homeworkData.put(getString(R.string.key_SubjectId), strSubjectId);
                homeworkData.put(getString(R.string.key_ClassId), strClassId);
                String planeChapter=edtTxtHomeWorkFeedbackChapterName.getText().toString();
                String planeDiscription=edtTxtHomeWorkFeedbackDiscription.getText().toString();
               String base64Chapter= StringUtil.textToBase64(planeChapter);
               String base64Discription= StringUtil.textToBase64(planeDiscription);
                homeworkData.put(getString(R.string.key_Chapter),base64Chapter );
                homeworkData.put(getString(R.string.key_Description),base64Discription);
                outObject.put(getString(R.string.key_homeworkData), homeworkData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
               String strMsg = inObject.getString(getString(R.string.key_Message));

                EditHomeWorkActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (strMsg.equalsIgnoreCase("Homework edited sucessfully")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equalsIgnoreCase("Homework was not edited")) {
                            alertMessage = strMsg;
                            alertTitle = "Failure";
                        } else {
                            alertTitle = "Error";
                            alertMessage = "Something went wrong While editing Home Work..";
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}

