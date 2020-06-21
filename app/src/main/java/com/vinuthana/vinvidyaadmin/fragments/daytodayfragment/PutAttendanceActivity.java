package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.AttendanceActivity;
import com.vinuthana.vinvidyaadmin.adapters.AttendanceAdapter;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PutAttendanceActivity extends AppCompatActivity {

    ListView lstPutAtnd;
    private ProgressDialog progressDialog;
    private Session session;
    String strStaffId, strDate, strSchoolId, strAcademicYearId, strReason, strClassId;
    String strClass, strStatus, strReasonId, strMsg;
    JSONArray jsonArray = new JSONArray();
    private ArrayList<StudentData> studList;
    AttendanceAdapter adapter;
    //Spinner spnrReason;
    Button btnPutAttendance;
    int absentCount=0;
    String strAbsentStudentIDs=null,strFinalAbsentList=null;
    ArrayList<String> absenteeNameList = new ArrayList<String>();
    ArrayList<String> absenteeRollList = new ArrayList<String>();
    JSONArray js_array = new JSONArray();
    JSONArray jsonStudentLeaveArray = new JSONArray();
    CheckConnection connection = new CheckConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Put_Attendance));

        if (!connection.netInfo(PutAttendanceActivity.this)) {
            connection.buildDialog(PutAttendanceActivity.this).show();
        } else {
            init();
            allEvents();
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

            //new GetReason().execute();
            new getStudentLeaveDetails().execute();

            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle = intent.getExtras();
            strClassId = bundle.getString("classId");
            strClass = bundle.getString("class");
            //strAcademicYearId = bundle.getString("academicYearId");

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strDate = df.format(c);
        }
    }

    public void init() {
        session = new Session(PutAttendanceActivity.this);
        lstPutAtnd = findViewById(R.id.lstPutAtnd);
        //spnrReason = findViewById(R.id.spnrReason);
        btnPutAttendance = findViewById(R.id.btnPutAttendance);
        studList = new ArrayList<StudentData>();
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

    public void allEvents() {
        /*spnrReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrReason.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strReasonId = adapterView.getItemAtPosition(i).toString();
                strReason = tmpView.getText().toString();
                Log.e("Tag", "" + strReasonId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        btnPutAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PutAttendanceActivity.this, strSchoolId + "\n" + strStaffId + "\n" + strClassId + "\n" + strAcademicYearId + "\n" + strReasonId + "\n" + strReason, Toast.LENGTH_SHORT).show();
                //
                try {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        //student is selected then Chekstatus of adapter will be 1
                        // this is the code which was removed on 07-03-2020 to faclitate the insert operation
                        //this was uncommented on 29-03-2020 to restore the attendance operation
                        if (adapter.mCheckStates.get(i)) {
                            JSONObject json_obj = new JSONObject();

                            json_obj.put(getString(R.string.key_StudentAttendenceId), "0");
                            json_obj.put(getString(R.string.key_SchoolId), strSchoolId);
                            String base64StudName= StringUtil.textToBase64(studList.get(i).getName());
                            json_obj.put(getString(R.string.key_StudentName),base64StudName);
                            json_obj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                            json_obj.put(getString(R.string.key_StudentId), studList.get(i).getStudentId());
                            json_obj.put(getString(R.string.key_EnteredBy), strStaffId);
                            json_obj.put(getString(R.string.key_Status), "1");
                            json_obj.put(getString(R.string.key_Date), strDate);
                            json_obj.put(getString(R.string.key_ClassId), strClassId);
                            json_obj.put(getString(R.string.key_Class), strClass);
                            json_obj.put(getString(R.string.key_ReasonId), "0");
                            json_obj.put(getString(R.string.key_ReasonTitleId), "2");
                            json_obj.put(getString(R.string.key_OtherReasonTitle), "no");
                            js_array.put(json_obj);
                        } else {
                            JSONObject json_obj = new JSONObject();
                            json_obj.put(getString(R.string.key_StudentAttendenceId), "0");
                            json_obj.put(getString(R.string.key_SchoolId), strSchoolId);
                            json_obj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                            json_obj.put(getString(R.string.key_StudentId), studList.get(i).getStudentId());
                            absenteeNameList.add(studList.get(i).getName());
                            String base64StudName=StringUtil.textToBase64(studList.get(i).getName());
                            json_obj.put(getString(R.string.key_StudentName),base64StudName);
                            absenteeRollList.add(studList.get(i).getRollNo());
                            json_obj.put(getString(R.string.key_EnteredBy), strStaffId);
                            json_obj.put(getString(R.string.key_Status), "0");
                            json_obj.put(getString(R.string.key_Date), strDate);
                            json_obj.put(getString(R.string.key_ClassId), strClassId);
                            json_obj.put(getString(R.string.key_Clas), strClass);
                            json_obj.put(getString(R.string.key_ReasonId), "0");
                            json_obj.put(getString(R.string.key_ReasonTitleId), "2");
                            json_obj.put(getString(R.string.key_OtherReasonTitle), "no");
                            js_array.put(json_obj);
                        }
                        if(!(adapter.mCheckStates.get(i))){
                            absentCount++;
                            strAbsentStudentIDs+=studList.get(i).getStudentId()+",";
                        }
                    }
                    //this was commented by basu for restoring the attendance

                    /*if ((strAbsentStudentIDs != null) && (strAbsentStudentIDs.length() > 0)) {
                        strFinalAbsentList = strAbsentStudentIDs.substring(0, strAbsentStudentIDs.length() - 1);
                    }

                    JSONObject json_obj = new JSONObject();
                    json_obj.put(getString(R.string.key_StudentAttendenceId), "0");
                    json_obj.put(getString(R.string.key_SchoolId), strSchoolId);
                    json_obj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                    json_obj.put(getString(R.string.key_EnteredBy), strStaffId);
                    json_obj.put(getString(R.string.key_Date), strDate);
                    json_obj.put(getString(R.string.key_ClassId), strClassId);
                    json_obj.put(getString(R.string.key_Absentees_cnt),absentCount);
                    json_obj.put(getString(R.string.key_StudentId),strFinalAbsentList);*/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PutAttendanceActivity.this);
                            /*LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.list_layout, null);*/
                            LayoutInflater inflater = LayoutInflater.from(PutAttendanceActivity.this);
                            View view = inflater.inflate(R.layout.custom_alert_dialog, null);
                            TextView textView = (TextView) view.findViewById(R.id.tview_message);
                            builder.setView(view);
                            builder.setCancelable(true);
                            builder.setTitle("Absentee list");
                            String strAbsntStudentNames = "";
                            if (absenteeNameList.size()>0) {
                                for (int i = 0; i < absenteeNameList.size(); i++) {
                                    strAbsntStudentNames += absenteeRollList.get(i) + " " + absenteeNameList.get(i) + "\n";
                                }
                                builder.setTitle("Absentee list");
                                textView.setText(strAbsntStudentNames);
                            }
                            else {
                                builder.setTitle("Confirm !");
                                textView.setText("Do you want to mark the attendance with ZERO absentee ?");
                            }
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new InsertAttendance().execute();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Don't do anything
                                    js_array = new JSONArray();
                                    absenteeNameList.clear();
                                    absenteeRollList.clear();
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
        lstPutAtnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object clickItemObj = parent.getAdapter().getItem(position);
            }
        });
    }

    private void ShowDialog(String stritem) {

        final Dialog UpgradeDialog = new Dialog(PutAttendanceActivity.this);
        UpgradeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        UpgradeDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        UpgradeDialog.setContentView(R.layout.popup_student_leave_details);

        TextView tview_item,tview_date;
        Button btnOk;

        tview_item = (TextView) UpgradeDialog.findViewById(R.id.tview_item);
        tview_date = (TextView) UpgradeDialog.findViewById(R.id.tview_date);
        Date date = new Date();
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String currentDate = sdf.format(date.getTime());
        tview_date.setText(currentDate);
        tview_item.setText(stritem);
        btnOk = (Button) UpgradeDialog.findViewById(R.id.btn_ok);
        UpgradeDialog.setCancelable(false);
        try {
            UpgradeDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new GetStudentList().execute();
                UpgradeDialog.dismiss();
            }
        });


    }

    class getStudentLeaveDetails extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PutAttendanceActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                Date date = new Date();
                String myFormat = "dd-MM-yyyy hh:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String currentDate = sdf.format(date.getTime());

                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseStudentLeaveList));
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_ClassId), strClassId);
                studAttendanceData.put(getString(R.string.key_Date), currentDate);
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                Log.e("Tag", "outObject for getStudentLeaveDetails =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    jsonStudentLeaveArray = inObject.getJSONArray("Result");
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
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
            String stritem="";
            try {
                for (int i = 0; i < jsonStudentLeaveArray.length(); i++) {
                    JSONObject jsonObject = jsonStudentLeaveArray.getJSONObject(i);
                    stritem += jsonObject.getString("RollNo") + ". " + jsonObject.getString("StudentName") + " : " + jsonObject.getString("Description") + "\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonStudentLeaveArray.length()!=0) {
                ShowDialog(stritem);
            } else {
                new GetStudentList().execute();
            }

            progressDialog.dismiss();
        }
    }

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        String strMessage="";
        ProgressDialog progressDialog = new ProgressDialog(PutAttendanceActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressDialog = new ProgressDialog(PutAttendanceActivity.this);
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
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());

                JSONObject inObject = new JSONObject(responseText);

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
                            AlertDialog.Builder builder = new AlertDialog.Builder(PutAttendanceActivity.this);
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
                adapter = new AttendanceAdapter(PutAttendanceActivity.this, R.layout.checked_list, studList,jsonStudentLeaveArray);
                lstPutAtnd.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
        public void showRollNoStatus(String strTitle, String strMessage){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PutAttendanceActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(false);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent= new Intent(PutAttendanceActivity.this, AttendanceActivity.class);
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


    class InsertAttendance extends AsyncTask<String, JSONArray, Void> {
        GetResponse response = new GetResponse();
        String responseText1;
        String alertMessage, alertTitle;
        String url = AD.url.base_url + "studentAttendanceOperation.jsp";
        String asmx_url=AD.url.asmx_url+"WebService_DayToDayActivity.asmx?op=Insert_StudentsAttendance";
        String methodName="Insert_StudentsAttendance";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PutAttendanceActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait Adding Attendance...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                GetSoapResponse soapResponse= new GetSoapResponse();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertStudentAttendance)); //,"EnteredBy":"2","StudentId": "7", "Status":"0","Date":"18-01-2018"}}
                JSONObject studAttendanceData = new JSONObject();





                studAttendanceData.put(getString(R.string.key_AttendanceArray), js_array);
                //final json object
                List<PropertyInfo> param = new ArrayList<PropertyInfo>();
                PropertyInfo propertyInfo = new PropertyInfo();


                // reqObejct.put(getString(R.string.key_NoticeId),strNoteId);

                //homeworkData.put(getString(R.string.key_Date), strDate);

                propertyInfo.setName("Insert_StudentsAttendanceData");
                propertyInfo.setValue(js_array.toString());
                //schoolId.setType(Long.class);
                param.add(propertyInfo);
                Log.e("Request ",js_array.toString());
                // Log.e("asmx_url",asmx_url);
               // outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                final String responseText = soapResponse.getSOAPStringResponse(asmx_url, methodName, param);






                //String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "outObject =" + outObject.toString());
                responseText1 = responseText;
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                /*strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }*/
                int resId=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));
                if(resId>0||resId==-1){
                    alertTitle=inObject.getString(getString(R.string.key_Status));
                    alertMessage = inObject.getString(getString(R.string.key_Message));
                }else{
                    alertTitle="Error";
                    alertMessage = "Something went wrong while marking attendance";
                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /*if (strMsg.equals("Student attendance added sucessfully")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equals("Student attendance has been already added")) {
                            alertMessage = strMsg;
                            alertTitle = "Fail";
                        } else {
                            alertTitle = "Fail";
                            alertMessage = "Something went wrong";
                        }*/
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
            js_array = new JSONArray();
            absenteeNameList.clear();
            absenteeRollList.clear();
            //Toast.makeText(PutAttendanceActivity.this, responseText1, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PutAttendanceActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent= new Intent(PutAttendanceActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
