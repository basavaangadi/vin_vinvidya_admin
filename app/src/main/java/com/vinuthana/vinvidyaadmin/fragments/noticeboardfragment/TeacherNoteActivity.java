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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StaffAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StaffCheckboxBaseAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StaffData;
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

public class TeacherNoteActivity extends AppCompatActivity {

    ListView lstSetTchrNt;
    Button btnInsertStaffNote;
    CheckBox cbSelectAllStaff, cb;
    String strSchoolId, strClassId, strClass, strStaffId, strAcademicYearId, strMsg, strStatus;
    String strNoticeId, strNoteOnDate, strTime, strSetDate, strNote, strNoteTitle;
    private Session session;
    private ArrayList<StaffData> staffData;
    //private List<StaffData> staffData1;
    JSONArray jsonArray = new JSONArray();
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    StaffAdapter adapter;
    JSONArray js_array = new JSONArray();
    ArrayList<String> staffNameList = new ArrayList<String>();
    ArrayList<String> staffIdList = new ArrayList<String>();
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();
    LinearLayout itemLayout;
    StaffCheckboxBaseAdapter listViewDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStaffNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Teacher Note");
        if (!connection.netInfo(TeacherNoteActivity.this)) {
            connection.buildDialog(TeacherNoteActivity.this).show();
        } else {
            init();
            allEvents();
            new GetStaffList().execute();
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

            strNoteTitle = bundle.getString("NoteTitle");
            strNoticeId = bundle.getString("NoteTitleId");
            strNote = bundle.getString("Note");
            strNoteOnDate = bundle.getString("NoteOnDate");
            strTime = bundle.getString("NoteOnTime");
        }

        //staffData1 = (List<StaffData>)
        //new GetStaffList().execute();
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
        session = new Session(TeacherNoteActivity.this);
        staffData = new ArrayList<StaffData>();
        lstSetTchrNt = findViewById(R.id.lstSetTchrNt);
        btnInsertStaffNote = findViewById(R.id.btnInsertStaffNote);
        //cbSelectAllStaff = findViewById(R.id.cbSelectAllStaff);
    }

    public void allEvents() {
        btnInsertStaffNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.mCheckStates.get(i)) {
                            JSONObject jObj = new JSONObject();
                            jObj.put(getString(R.string.key_SchoolId), strSchoolId);
                            jObj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                            jObj.put(getString(R.string.key_SentTo), staffData.get(i).getStaffDetailsId());
                            staffNameList.add(staffData.get(i).getName());
                            staffIdList.add(staffData.get(i).getStaffId());
                            jObj.put(getString(R.string.key_EnteredBy), strStaffId);
                            jObj.put(getString(R.string.key_NoteSentDate), strSetDate);
                            jObj.put(getString(R.string.key_NoteTitleId), strNoticeId);
                            if(strNoticeId.equalsIgnoreCase("1")){

                                jObj.put(getString(R.string.key_OtherNoteTitle), strNoteTitle);
                            }
                           // String base64OtherTitle= StringUtil.textToBase64(strNoteTitle);
                            jObj.put(getString(R.string.key_NoteTitleName), strNoteTitle);
                            String baseNote= StringUtil.textToBase64(strNote);
                            jObj.put(getString(R.string.key_Note), strNote);
                            jObj.put(getString(R.string.key_NoteOnDate), strNoteOnDate);
                            if(strTime.equalsIgnoreCase("")||strTime.equalsIgnoreCase(null)){
                                strTime="9999";
                            }
                            jObj.put(getString(R.string.key_NoticeTime), strTime);
                            js_array.put(jObj);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherNoteActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(TeacherNoteActivity.this);
                            View view = inflater.inflate(R.layout.custom_alert_dialog, null);
                            TextView textView = (TextView) view.findViewById(R.id.tview_message);
                            builder.setView(view);
                            builder.setCancelable(true);
                            builder.setTitle("Staff list");

                            String strStaffList = "";
                            for (int i = 0; i < staffNameList.size(); i++) {
                                strStaffList += staffIdList.get(i) + " " + staffNameList.get(i) + "\n";
                            }
                            textView.setText(strStaffList);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new InsertNote().execute();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    js_array = new JSONArray();
                                    staffNameList.clear();
                                    staffIdList.clear();
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


    class GetStaffList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TeacherNoteActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffListBySchoolId));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherNoteActivity.this);
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
            //jsonArray = new JSONArray();
            for (int i = 0; i <= jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    StaffData studData = new StaffData();
                    studData.setName(object.getString(getString(R.string.key_Name)));
                    studData.setStaffDetailsId(object.getString(getString(R.string.key_StaffDetailsId)));
                    studData.setStaffId(object.getString(getString(R.string.key_StaffId)));
                    studData.setChecked(false);
                    staffData.add(studData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new StaffAdapter(TeacherNoteActivity.this, R.layout.unchecked_list, staffData);
                lstSetTchrNt.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
    }

    class InsertNote extends AsyncTask<String, JSONArray, Void> {

        String responseText1;
       // String url = AD.url.base_url + "noticeBoardOperation.jsp";
        String asmxUrl=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_TeachersNote";
        String methodName="Insert_TeachersNote";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TeacherNoteActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding Teacher's note...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertTeacherNote)); //,"EnteredBy":"2","StudentId": "7", "Status":"0","Date":"18-01-2018"}}
                List<PropertyInfo>param= new ArrayList<PropertyInfo>();
                PropertyInfo tecahersNoteData= new PropertyInfo();
                tecahersNoteData.setName("Insert_TeachersNoteData");
                tecahersNoteData.setValue(js_array.toString());
                param.add(tecahersNoteData);
                Log.e("outObject",js_array.toString());
                GetSoapResponse soapResponse= new GetSoapResponse();
                String responseText=soapResponse.getSOAPStringResponse(asmxUrl,methodName,param);

                //js_array = new JSONArray();

                /*JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_TeacherNoteArray), js_array);
                //final json object
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());*/
                responseText1 = responseText;
                JSONObject inObject = new JSONObject(responseText);
                String strResId=inObject.getString("resId");
                int resID=Integer.parseInt(strResId);
                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (resID>0) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (resID==-1) {
                            alertMessage = strMsg;
                            alertTitle = "Alert";
                        } else {
                            alertTitle = "Fail";
                            alertMessage = "Teacher's note couldn't be added because of error..";
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
            staffNameList.clear();
            staffIdList.clear();
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            //Toast.makeText(TeacherNoteActivity.this, responseText1, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherNoteActivity.this);
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
