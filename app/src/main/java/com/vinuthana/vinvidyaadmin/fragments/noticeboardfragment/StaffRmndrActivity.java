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

public class StaffRmndrActivity extends AppCompatActivity {

    Button btnStfRmndr;
    ListView lstStfRmndr;
    CheckBox cbSelectAllStafdRmd;
    String strSchoolId, strClass, strStaffId, strMsg, strStatus, strRmndrId,  strSetDate;
    String strRmndr, strReminderTitle,strAcademicYearId, strTime, strDateTime,strDate;
    private Session session;
    private ArrayList<StaffData> staffData;
    private List<StaffData> staffData1;
    JSONArray jsonArray;
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    StaffAdapter adapter;
    JSONArray js_array = new JSONArray();
    ArrayList<String> staffNameList = new ArrayList<String>();
    ArrayList<String> staffIdList = new ArrayList<String>();
    CheckConnection connection = new CheckConnection();
    ProgressDialog progressDialog;
    StaffCheckboxBaseAdapter listViewDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_rmndr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Staff Reminder");
        if (!connection.netInfo(StaffRmndrActivity.this)) {
            connection.buildDialog(StaffRmndrActivity.this).show();
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

            strReminderTitle = bundle.getString("ReminderTitle");
            strRmndrId = bundle.getString("ReminderTitleId");
            strRmndr = bundle.getString("Reminder");
            strDate = bundle.getString("ReminderDate");
            strTime = bundle.getString("ReminderTime");
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

    private void allEvents() {
        btnStfRmndr.setOnClickListener(new View.OnClickListener() {
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
                            jObj.put(getString(R.string.key_SentBy), strStaffId);
                            jObj.put(getString(R.string.key_ReminderSetDate), strSetDate);
                            jObj.put(getString(R.string.key_ReminderTitleId), strRmndrId);

                           // String base64ReminderTitle=StringUtil.textToBase64(strReminderTitle);
                            if(strRmndrId.equalsIgnoreCase("1")){

                                jObj.put(getString(R.string.key_ReminderOtherTitle),strReminderTitle);

                            }
                            jObj.put(getString(R.string.key_ReminderTitleName),strReminderTitle);
                           // String base64strReminder= StringUtil.textToBase64(strRmndr);
                            jObj.put(getString(R.string.key_Reminder), strRmndr);
                            jObj.put(getString(R.string.key_ReminderDate), strDate);
                            if(!(strTime.length()>1)){
                                strTime="9999";
                            }
                            jObj.put(getString(R.string.key_ReminderTime), strTime);
                            js_array.put(jObj);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StaffRmndrActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(StaffRmndrActivity.this);
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
                                    new InsertStaffRmndr().execute();
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

        /*cbSelectAllStafdRmd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int size = staffData1.size();
                for (int i = 0; i < size; i++) {
                    StaffData data = staffData1.get(i);
                    if (isChecked == true) {
                        data = staffData1.get(i);
                        data.setChecked(true);
                    } else {
                        data = staffData1.get(i);
                        data.setChecked(false);
                    }
                }
                listViewDataAdapter.notifyDataSetChanged();
            }
        });*/
    }

    public void init() {
        session = new Session(StaffRmndrActivity.this);
        staffData = new ArrayList<StaffData>();
        btnStfRmndr = findViewById(R.id.btnStfRmndr);
        lstStfRmndr = findViewById(R.id.lstStfRmndr);
        //cbSelectAllStafdRmd = findViewById(R.id.cbSelectAllStafdRmd);
    }

    class GetStaffList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StaffRmndrActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(StaffRmndrActivity.this);
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

                adapter = new StaffAdapter(StaffRmndrActivity.this, R.layout.unchecked_list, staffData);
                lstStfRmndr.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
    }

    class InsertStaffRmndr extends AsyncTask<String, JSONArray, Void> {

        String responseText1;
       // String url = AD.url.base_url + "noticeBoardOperation.jsp";
        String asmx_url=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_StaffReminder";
        String methodName="Insert_StaffReminder";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StaffRmndrActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                GetSoapResponse soapResponse= new GetSoapResponse();
                JSONObject outObject = new JSONObject();
                List<PropertyInfo>param= new ArrayList<PropertyInfo>();
                PropertyInfo staffReminderData= new PropertyInfo();
                staffReminderData.setName("Insert_StaffReminderData");
                staffReminderData.setValue(js_array.toString());
                param.add(staffReminderData);
                Log.e("Tag", "outObject =" + js_array.toString());
                String responseText=soapResponse.getSOAPStringResponse(asmx_url,methodName,param);

                JSONObject inObject = new JSONObject(responseText);
                String strMessage=inObject.getString(getString(R.string.key_Message));
                String strStatus=inObject.getString(getString(R.string.key_status));

                int resId=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));

                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (resId>0||resId==-1) {
                            alertMessage = strMessage;
                            alertTitle = strStatus;
                        } else {
                            alertTitle = "Fail";
                            alertMessage = "Couldn't add the reminder because of some error..";
                    }
                        showAlert(alertMessage, alertTitle);
                    }
                });
            } catch (Exception e) {
                Log.e("Execption staffreminder",e.toString());
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
            //Toast.makeText(StaffRmndrActivity.this, responseText1, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StaffRmndrActivity.this);
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
