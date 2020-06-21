package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

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
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.AttendanceActivity;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.HomeWorkActivity;
import com.vinuthana.vinvidyaadmin.adapters.AppInfoAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentData;
import com.vinuthana.vinvidyaadmin.adapters.StudentModel;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GiveHmWrkFbActivity extends AppCompatActivity {

    String strStaffId, strClassId, strDate, strHomeWorkId, strStatus, strSchoolId, strWriteNote, strMsg;
    Button btnFeedback;
    JSONArray jsonArray = new JSONArray();
    // JSONArray js_array = new JSONArray();
    private Session session;
    private ArrayList<StudentModel> studentModels;
    ArrayList<StudentData> studList = new ArrayList<StudentData>();
    AppInfoAdapter adapter;
    ListView lstFeedback;
    String alertMessage, alertTitle,strMessage;
    TextView textView, textView2;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_hm_wrk_fb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFeedback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Give_Home_Work_feedback));

        if (!connection.netInfo(GiveHmWrkFbActivity.this)) {
            connection.buildDialog(GiveHmWrkFbActivity.this).show();
        } else {
            init();
            allEvents();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);

            new GetStudentList().execute();

            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle = intent.getExtras();

            strClassId = bundle.getString("ClassId");
            strHomeWorkId = bundle.getString("HomeworkId");

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strDate = df.format(c);
        }
    }

    public void init() {
        session = new Session(getApplicationContext());
        studentModels = new ArrayList<StudentModel>();
        lstFeedback = findViewById(R.id.lstFeedback);
        btnFeedback = findViewById(R.id.btnFeedback);
    }

    public void allEvents() {
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GiveHmWrkFbActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                builder.setView(convertView);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new HomeworkOperation().execute();
                    }
                });
                builder.setNegativeButton("No",null);
                builder.setTitle("confirm");
                builder.setMessage("Do you want add feedback for this homework");
                builder.show();

            }
        });
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
            progressDialog = new ProgressDialog(GiveHmWrkFbActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Student List...");
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
                }else {
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
                    studData.setChecked(true);
                    studList.add(studData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter = new AppInfoAdapter(GiveHmWrkFbActivity.this, R.layout.checked_list, studList,true);
                lstFeedback.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
        public void showRollNoStatus(String strTitle, String strMessage){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GiveHmWrkFbActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(false);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent= new Intent(GiveHmWrkFbActivity.this, HomeWorkActivity.class);
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

    class HomeworkOperation extends AsyncTask<String, JSONArray, Void> {
        GetResponse response = new GetResponse();
        String responseText1;
        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GiveHmWrkFbActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertHomworkFeedBackWithJSONArray));
                //outObject.put("OperationName", "");//,"EnteredBy":"2","StudentId": "7", "Status":"0","Date":"18-01-2018"}}
                JSONArray js_array = new JSONArray();
                try {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        //student is selected then Chekstatus of adapter will be 1
                        if (adapter.mCheckStates.get(i) == true) {
                            JSONObject json_obj = new JSONObject();
                            json_obj.put(getString(R.string.key_HomeworkId), strHomeWorkId);
                            json_obj.put(getString(R.string.key_EnteredBy), strStaffId);
                            json_obj.put(getString(R.string.key_StudentId), studList.get(i).getStudentId());
                            json_obj.put(getString(R.string.key_Status), "1");
                            json_obj.put(getString(R.string.key_Date), strDate);
                            js_array.put(json_obj);
                        } else {
                            JSONObject json_obj = new JSONObject();
                            json_obj.put(getString(R.string.key_HomeworkId), strHomeWorkId);
                            json_obj.put(getString(R.string.key_EnteredBy), strStaffId);
                            json_obj.put(getString(R.string.key_StudentId), studList.get(i).getStudentId());
                            json_obj.put(getString(R.string.key_Status), "0");
                            json_obj.put(getString(R.string.key_Date), strDate);
                            js_array.put(json_obj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_HomworkArray), js_array);
                //final json object
                outObject.put(getString(R.string.key_homeworkData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                responseText1 = responseText;
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
           strStatus = inObject.getString(getString(R.string.key_Status));
              /*  if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }*/
                strMsg = inObject.getString(getString(R.string.key_Message));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                            alertMessage = strMsg;
                            alertTitle = strStatus;
                        } /*else if (strMsg.equals("Homework feedback has been already added")) {
                            alertMessage = strMsg;
                            alertTitle = "Fail";
                        }*/ else {
                            alertTitle = "Fail";
                            alertMessage = "Something went wrong";
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
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(GiveHmWrkFbActivity.this, responseText1, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GiveHmWrkFbActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                   onBackPressed();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
