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
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.AttendanceActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.StudentReminderActivity;
import com.vinuthana.vinvidyaadmin.adapters.StudAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudCheckboxBaseAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentData;
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
import java.util.HashMap;
import java.util.List;

public class StudReminderActivity extends AppCompatActivity {

    ListView lstStudRmndr;
    Button btnStudRmndr;
    CheckBox cbSelectAllStudRmd;
    String strSchoolId, strClassId, strClass, strStaffId, strMsg, strStatus, strMessage,strRmndrId;
    String strDateTime, strSetDate, strRmndr, strReminderTitle,strAcademicYearId, strDate, strTime;
    private Session session;
    //private ArrayList<StaffData> staffData;
    JSONArray jsonArray = new JSONArray();
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    //StaffAdapter adapter;
    JSONArray js_array = new JSONArray();
    ProgressDialog progressDialog;
    ArrayList<StudentData> studList = new ArrayList<StudentData>();
    List<StudentData> studList1 = new ArrayList<StudentData>();
    StudAdapter adapter;
    ArrayList<String> studNameList = new ArrayList<String>();
    ArrayList<String> studRollList = new ArrayList<String>();
    CheckConnection connection = new CheckConnection();
    StudCheckboxBaseAdapter listViewDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Reminder");
        if (!connection.netInfo(StudReminderActivity.this)) {
            connection.buildDialog(StudReminderActivity.this).show();
        } else {
            init();
            allEvents();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            Log.e("tag", "Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());

            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle = intent.getExtras();

            strReminderTitle = bundle.getString("ReminderTitle");
            strRmndrId = bundle.getString("ReminderTitleId");
            strRmndr = bundle.getString("Reminder");
            strDate = bundle.getString("ReminderDate");
            strClassId = bundle.getString("ClassId");
            strTime = bundle.getString("ReminderTime");

            //Toast.makeText(StudReminderActivity.this, strTime, Toast.LENGTH_SHORT).show();

            new GetStudentList().execute();
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
        session = new Session(StudReminderActivity.this);
        studList1 = new ArrayList<StudentData>();
        lstStudRmndr = findViewById(R.id.lstStudRmndr);
        btnStudRmndr = findViewById(R.id.btnStudRmndr);
        //cbSelectAllStudRmd = findViewById(R.id.cbSelectAllStudRmd);
    }

    public void allEvents() {
        btnStudRmndr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(StudReminderActivity.this, strReminderTitle + " " +
                        strRmndrId + " " + strRmndr + " " + strDate + " " + strClassId , Toast.LENGTH_SHORT).show();*/
                try {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.mCheckStates.get(i)) {
                            JSONObject jObj = new JSONObject();
                            jObj.put(getString(R.string.key_SchoolId), strSchoolId);
                            jObj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                            jObj.put(getString(R.string.key_StudentId), studList.get(i).getStudentId());
                            studNameList.add(studList.get(i).getName());
                            studRollList.add(studList.get(i).getRollNo());
                            jObj.put(getString(R.string.key_SentBy), strStaffId);
                            jObj.put(getString(R.string.key_ClassId), strClassId);
                            jObj.put(getString(R.string.key_ReminderSetDate), strSetDate);
                            jObj.put(getString(R.string.key_ReminderTitleId), strRmndrId);
                            jObj.put(getString(R.string.key_ReminderTitleName), strReminderTitle);
                            if(strRmndrId.equalsIgnoreCase("1")){

                                jObj.put(getString(R.string.key_ReminderOtherTitle), strReminderTitle);
                            }


                           // String base64Rmndr= StringUtil.textToBase64(strRmndr);
                            jObj.put(getString(R.string.key_Reminder),strRmndr );
                            jObj.put(getString(R.string.key_ReminderDate), strDate);
                            if(!(strTime.length()>1)){
                                strTime="9999";
                            }
                            jObj.put(getString(R.string.key_ReminderTime), strTime);
                            js_array.put(jObj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StudReminderActivity.this);
                            /*LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.list_layout, null);*/
                        LayoutInflater inflater = LayoutInflater.from(StudReminderActivity.this);
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
                                new InsertStudRmndr().execute();
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


            }
        });

        /*lstStudRmndr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemObject = parent.getAdapter().getItem(position);

                StudentData data = (StudentData) itemObject;

                if (data.isChecked()) {
                    cbSelectAllStudRmd.setChecked(false);
                    data.setChecked(false);
                } else {
                    cbSelectAllStudRmd.setChecked(true);
                    data.setChecked(true);
                }
            }
        });*/

        /*cbSelectAllStudRmd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int size = studList1.size();
                for (int i = 0; i < size; i++) {
                    StudentData data = studList1.get(i);
                    if (isChecked == true) {
                        data = studList1.get(i);
                        data.setChecked(true);
                    } else {
                        data = studList1.get(i);
                        data.setChecked(false);
                    }
                }
                listViewDataAdapter.notifyDataSetChanged();
            }
        });*/
    }

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StudReminderActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding data...");
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudReminderActivity.this);
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
                adapter = new StudAdapter(StudReminderActivity.this, R.layout.unchecked_list, studList);
                lstStudRmndr.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
        public void showRollNoStatus(String strTitle, String strMessage){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudReminderActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(false);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent= new Intent(StudReminderActivity.this, StudentReminderActivity.class);
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

    class InsertStudRmndr extends AsyncTask<String, JSONArray, Void> {

        String responseText1;
        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        String asmx_url=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_StudentReminder";
        String methodName="Insert_StudentReminder";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StudReminderActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertStudentReminder)); //,"EnteredBy":"2","StudentId": "7", "Status":"0","Date":"18-01-2018"}}

                //js_array = new JSONArray();

                JSONObject noticeBoardData = new JSONObject();
                List<PropertyInfo>param= new ArrayList<PropertyInfo>();
                PropertyInfo studentReminderData= new PropertyInfo();
                studentReminderData.setName("Insert_StudentReminderData");
                studentReminderData.setValue(js_array.toString());
                param.add(studentReminderData);
                noticeBoardData.put(getString(R.string.key_StudentReminderArray), js_array);
                //final json object
                Log.e("Request studReminder :",js_array.toString());

                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                GetSoapResponse soapResponse= new GetSoapResponse();


                String responseText =soapResponse.getSOAPStringResponse(asmx_url,methodName,param);
                responseText1 = responseText;
                JSONObject inObject = new JSONObject(responseText);
                int resId=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));
                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));
                String strStatus=inObject.getString(getString(R.string.key_Status));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (resId>0||resId==-1) {
                            alertMessage = strMsg;
                            alertTitle = strStatus;
                        } else {
                            alertTitle = "Fail";
                            alertMessage = "Reminder couldn't be added because of some error..";
                        }
                        showAlert(alertMessage, alertTitle);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            js_array = new JSONArray();
            studNameList.clear();
            studRollList.clear();
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            //Toast.makeText(StudReminderActivity.this, responseText1, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudReminderActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
