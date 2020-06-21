package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.AttendanceActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ParentNoteActivity;
import com.vinuthana.vinvidyaadmin.adapters.AppInfoAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudCheckboxBaseAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentData;
//import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.StudentListActivity;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.GetSoapResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    //TextView tvNoticeId, tvDate, tvClassId, tvStaffId;
    String strStaffId, strClassId, strDate, strNoteSentDate, strNoticeId,
            strStatus,strMessage ,strSchoolId, strMsg, strAcademicYearId, strNoteTitle,strOtherTitle;
    Button btnWriteParentNt, btnSelectAll;
    CheckBox cbSelectAll, cb;
    JSONArray jsonArray = new JSONArray();
    private Session session;
    String strNote, strDateTime, strTime,base64OtherNoticeTitle,base64Notice;
    private ArrayList<StudentData> studentModels;
    ListView lstWriteParentNt;
    EditText edtWriteParentNt, edtWriteParentNtDt;
    ArrayList<StudentData> studList = new ArrayList<StudentData>();
    public AppInfoAdapter adapter;
    JSONObject inObject;
    ProgressDialog progressDialog;
    JSONArray js_array = new JSONArray();
    ArrayList<String> studNameList = new ArrayList<String>();
    ArrayList<String> studRollList = new ArrayList<String>();
    CheckConnection connection = new CheckConnection();
    LinearLayout itemLayout;
    int resId;
    StudCheckboxBaseAdapter listViewDataAdapter;

    /*DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetPrntNoteDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    private int year, month, day;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStudList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Send Parent note");
        if (!connection.netInfo(StudentListActivity.this)) {
            connection.buildDialog(StudentListActivity.this).show();
        } else {
            init();
            allEvents();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

        /*year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(StudentListActivity.this, mlistner, year, month, day);*/

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


            new GetStudentList().execute();

            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle = intent.getExtras();

            //strStaffId = bundle.getString("staffId");
            strClassId = bundle.getString("classId");
            strDate = bundle.getString("strDate");
            strNoticeId = bundle.getString("noticeTitleId");
            strOtherTitle = bundle.getString("otherTitle");
            strNoteTitle = bundle.getString("NoteTitle");
            strNote = bundle.getString("note");
            strTime = bundle.getString("strTime");
            strNoteSentDate = df.format(c);

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
        session = new Session(getApplicationContext());
        studentModels = new ArrayList<StudentData>();
        lstWriteParentNt = findViewById(R.id.lstWriteParentNt);
        btnWriteParentNt = findViewById(R.id.btnWriteParentNt);
        //btnSelectAll = findViewById(R.id.btnSelectAll);
        edtWriteParentNt = findViewById(R.id.edtWriteParentNt);
        //cbSelectAll = findViewById(R.id.cbSelectAll);


    }

    public void allEvents() {
        btnWriteParentNt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        //student is selected then Chekstatus of adapter will be 1
                        if (adapter.mCheckStates.get(i) == true) {
                            JSONObject json_obj = new JSONObject();
                            json_obj.put(getString(R.string.key_SchoolId), strSchoolId);
                            json_obj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                            json_obj.put(getString(R.string.key_StudentId), studList.get(i).getStudentId());
                            studNameList.add(studList.get(i).getName());
                            studRollList.add(studList.get(i).getRollNo());
                            json_obj.put(getString(R.string.key_EnteredBy), strStaffId);
                            json_obj.put(getString(R.string.key_NoteSentDate), strNoteSentDate);
                            json_obj.put(getString(R.string.key_ClassId), strClassId);
                            json_obj.put(getString(R.string.key_NoteTitleName),strNoteTitle);
                            json_obj.put(getString(R.string.key_NoteTitleId), strNoticeId);
                            json_obj.put(getString(R.string.key_OtherNoteTitle), strOtherTitle);
                            json_obj.put(getString(R.string.key_Note), strNote);
                            json_obj.put(getString(R.string.key_NoteOnDate), strDate);
                            json_obj.put(getString(R.string.key_NoticeTime), strTime);
                            js_array.put(json_obj);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentListActivity.this);
                            /*LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.list_layout, null);*/
                            LayoutInflater inflater = LayoutInflater.from(StudentListActivity.this);
                            View view = inflater.inflate(R.layout.custom_alert_dialog, null);
                            TextView textView = (TextView) view.findViewById(R.id.tview_message);
                            builder.setView(view);
                            builder.setCancelable(true);
                            builder.setTitle("Student list");

                            String strStudentNames = "";
                            for (int i = 0; i < studNameList.size(); i++) {
                                strStudentNames += studRollList.get(i) + " " + studNameList.get(i) + "\n";
                            }
                            textView.setText(strStudentNames);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new InsertParentsNote().execute();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Don't do anything
                                    js_array = new JSONArray();
                                    studNameList.clear();
                                    studRollList.clear();
                                }
                            });
                            builder.create().show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }



    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StudentListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
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
                strMessage=inObject.getString(getString(R.string.key_Message));
                String strRollNotSet=inObject.getString(getString(R.string.key_Roll_Set));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }else if(strRollNotSet.equalsIgnoreCase("1")){
                    showRollNoStatus(strStatus,strMessage);
                } 
                
                
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentListActivity.this);
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
                ex.printStackTrace();
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
            for (int i = 0; i <= jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    StudentData studData = new StudentData();
                    studData.setName(object.getString(getString(R.string.key_studentname)));
                    studData.setRollNo(object.getString(getString(R.string.key_RollNo)));
                    studData.setStudentId(object.getString(getString(R.string.key_StudentDetailsId)));
                    studData.setChecked(false);
                    studList.add(studData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter = new AppInfoAdapter(StudentListActivity.this, R.layout.unchecked_list, studList,false);
                lstWriteParentNt.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
        public void showRollNoStatus(String strTitle, String strMessage){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentListActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(false);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent= new Intent(StudentListActivity.this, ParentNoteActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });
        }
    }

    class InsertParentsNote extends AsyncTask<String, JSONArray, Void> {
        //GetResponse response = new GetResponse();
        GetSoapResponse soapResponse= new GetSoapResponse();
        String responseText1;
        //String url = AD.url.base_url + "noticeBoardOperation.jsp";
        String asmx_url=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_ParentNote";
        String alertMessage, alertTitle;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StudentListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
               // outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertParentNote));
                //outObject.put("OperationName", "");//,"EnteredBy":"2","StudentId": "7", "Status":"0","Date":"18-01-2018"}}


                JSONObject noticeBoardData = new JSONObject();
                //noticeBoardData.put(getString(R.string.key_ParentNoteArray), js_array);
                String methodName="Insert_ParentNote";
                List<PropertyInfo>param=new ArrayList<PropertyInfo>();
                PropertyInfo parentsNoteInsertData= new PropertyInfo();
                parentsNoteInsertData.setName("Insert_ParentNoteData");
                parentsNoteInsertData.setValue(js_array.toString());
                param.add(parentsNoteInsertData);
                //final json object
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("Tag", "outObject =" +js_array.toString());
                //String responseText = response.getServerResopnse(url, outObject.toString());
                responseText1=soapResponse.getSOAPStringResponse(asmx_url,methodName,param);

                inObject = new JSONObject(responseText1);
                resId=0;

                Log.e("Tag", "responseText is =" + responseText1);
                strMsg = inObject.getString(getString(R.string.key_Message));
                strStatus=inObject.getString(getString(R.string.key_Status));
                resId=Integer.parseInt(inObject.getString("resId"));
               if (strStatus.equals(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                   //resId=Integer.parseInt(inObject.getString("resId"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                       /* if (strMsg.equals("Parent's note inserted sucessfully")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equals("Parent's note has been already added")) {
                            alertMessage = strMsg;
                            alertTitle = "Fail";
                        } else {
                            alertTitle = "Fail";
                            alertMessage = "Parents note couldn't be added because of some error";
                        }*/
                       if(resId>0||resId==-1){
                           showAlert(strMsg, strStatus);
                       }else{
                           showAlert("Error", "Something went wrong");
                       }

                    }
                });

                /*strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }*/
            } catch (Exception e) {
                e.printStackTrace();
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
            js_array = new JSONArray();
            studNameList.clear();
            studRollList.clear();
            progressDialog.dismiss();
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentListActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(resId>0||resId==-1){
                    onBackPressed();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
