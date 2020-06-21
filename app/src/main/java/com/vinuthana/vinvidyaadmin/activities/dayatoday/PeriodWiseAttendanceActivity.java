package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.PeriodWiseAttendanceAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PeriodWiseAttendanceActivity extends AppCompatActivity {
    ListView listViewPWAttendance;
    Button btnSubmit;
    public static JSONArray jsonStudentAttendanceArray;
    String strPeriod,strClassId,strStatus,strSchoolId,strStaffId,strAcademicYearId,strDate;
    PeriodWiseAttendanceAdapter periodWiseAttendanceAdapter;
    JSONArray jsonInsertStudentAttendance,jsonStudentLeaveArray;
    private Session session;
    ArrayList<String> absenteeNameList = new ArrayList<String>();
    ArrayList<String> absenteeRollList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_wise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Attendance));
        listViewPWAttendance = findViewById(R.id.listViewPWAttendance);
        jsonStudentLeaveArray = new JSONArray();
        btnSubmit = findViewById(R.id.submit);
        Bundle extras = getIntent().getExtras();
        strPeriod = extras.getString(getString(R.string.key_Period));
        strClassId = extras.getString("classId");
        session = new Session(PeriodWiseAttendanceActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
        strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        strDate = df.format(c);
        new getStudentLeaveDetails().execute();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jsonInsertStudentAttendance = new JSONArray();
                    for (int j = 0; j < jsonStudentAttendanceArray.length(); j++) {
                        JSONObject json_obj = new JSONObject();
                        json_obj.put(getString(R.string.key_SchoolId), strSchoolId);
                        json_obj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                        json_obj.put(getString(R.string.key_StudentId), jsonStudentAttendanceArray.getJSONObject(j).getString(getString(R.string.key_StudentDetailsId)));
                        json_obj.put(getString(R.string.key_EnteredBy), strStaffId);
                        json_obj.put(getString(R.string.key_Period), strPeriod);
                        json_obj.put(getString(R.string.key_Date), strDate);
                        json_obj.put(getString(R.string.key_ClassId), strClassId);
                        json_obj.put(getString(R.string.key_ReasonId), "0");
                        json_obj.put(getString(R.string.key_OtherReasonTitle), "no");
                        if (jsonStudentAttendanceArray.getJSONObject(j).getString("Status").equals("1"))
                            json_obj.put(getString(R.string.key_Status), "1");
                        else {
                            absenteeNameList.add(jsonStudentAttendanceArray.getJSONObject(j).getString(getString(R.string.key_studentname)));
                            absenteeRollList.add(jsonStudentAttendanceArray.getJSONObject(j).getString(getString(R.string.key_RollNo)));
                            json_obj.put(getString(R.string.key_Status), "0");
                        }
                        jsonInsertStudentAttendance.put(json_obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PeriodWiseAttendanceActivity.this);
                            /*LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.list_layout, null);*/
                        LayoutInflater inflater = LayoutInflater.from(PeriodWiseAttendanceActivity.this);
                        View view = inflater.inflate(R.layout.custom_alert_dialog, null);
                        TextView textView = (TextView) view.findViewById(R.id.tview_message);
                        builder.setView(view);
                        builder.setCancelable(true);
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
                                new InsertPeriodWiseAttendance().execute();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Don't do anything
                                jsonInsertStudentAttendance = new JSONArray();
                                absenteeNameList.clear();
                                absenteeRollList.clear();
                            }
                        });
                        builder.create().show();
                    }
                });

            }
        });
    }

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PeriodWiseAttendanceActivity.this);
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseStudentsList));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                jsonStudentAttendanceArray = inObject.getJSONArray("Result");
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                progressDialog.dismiss();
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonStudentAttendanceArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PeriodWiseAttendanceActivity.this);
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
                Toast.makeText(PeriodWiseAttendanceActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
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

            for (int j = 0; j < jsonStudentAttendanceArray.length(); j++) {
                try {
                    jsonStudentAttendanceArray.getJSONObject(j).put("Status", "1");
                    if (jsonStudentLeaveArray.length()>0) {
                        for (int k=0;k<jsonStudentLeaveArray.length();k++) {
                            if (jsonStudentAttendanceArray.getJSONObject(j).getString(getString(R.string.key_RollNo)).equalsIgnoreCase(jsonStudentLeaveArray.getJSONObject(k).getString(getString(R.string.key_RollNo)))) {
                                jsonStudentAttendanceArray.getJSONObject(j).put("Status", "0");
                                break;
                            }
                        }
                     }
                } catch (Exception e) {
                    Toast.makeText(PeriodWiseAttendanceActivity.this,e.toString(),Toast.LENGTH_SHORT).show();

                }
            }
            progressDialog.dismiss();
            periodWiseAttendanceAdapter = new PeriodWiseAttendanceAdapter(PeriodWiseAttendanceActivity.this);
            periodWiseAttendanceAdapter.setData(jsonStudentAttendanceArray);
            listViewPWAttendance.setAdapter(periodWiseAttendanceAdapter);
        }
    }

    class InsertPeriodWiseAttendance extends AsyncTask<String, JSONArray, Void> {
        GetResponse response = new GetResponse();
        String responseText1;
        String alertMessage, alertTitle;
        String url = AD.url.base_url + "studentAttendanceOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PeriodWiseAttendanceActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait Adding Attendance...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertStudentAttendancePeriodWise)); //,"EnteredBy":"2","StudentId": "7", "Status":"0","Date":"18-01-2018"}}
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_AttendanceArray), jsonInsertStudentAttendance);
                //final json object
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                responseText1 = responseText;
                Log.e("request : ",responseText);
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                /*strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }*/
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    alertTitle = "Success";
                    alertMessage = "Student attendance added sucessfully";
                } else {
                    alertMessage = "Cannot able to add attendance";
                    alertTitle = "Fail";
                }


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
            //Toast.makeText(PeriodWiseAttendanceActivity.this, responseText1, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            showAlert(alertMessage, alertTitle);
        }

    }

    class getStudentLeaveDetails extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PeriodWiseAttendanceActivity.this);
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
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String currentDate = sdf.format(date.getTime());

                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getclasswiseStudentRequestList));
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_ClassId), strClassId);
                studAttendanceData.put(getString(R.string.key_Date), currentDate);
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
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

    private void ShowDialog(String stritem) {

        final Dialog UpgradeDialog = new Dialog(PeriodWiseAttendanceActivity.this);
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
    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PeriodWiseAttendanceActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
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
