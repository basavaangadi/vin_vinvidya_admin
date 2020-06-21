package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StaffSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class EditStaffReminderActvity extends AppCompatActivity {
    String strSchoolId,strStaffId,strCurrentObject,strAcademicYearId,strStaffList,strStaffNameId,strStaff;
    String strSentTo,strReminderTitle,strReminder,strReminderDate,strReminderId;
    String strReminderTitleId,strReminderEditDate,strReminderCreatedDate,strReminderTime;
    TextView tvStaffRemindertitle;
    EditText edtStaffReminder,edtStaffReminderDate,edtStaffReminderTime;
    Button btnStaffReminderEdit;
    TimePickerDialog mTimePicker;
    DatePickerDialog reminderDatePicker;
    Calendar reminderDate = Calendar.getInstance();
    int hour = reminderDate.get(Calendar.HOUR_OF_DAY);
    int minute = reminderDate.get(Calendar.MINUTE);
    int rmdYear, rmdMonth, rmdDay;
    Spinner spnrStaffReminderSentTo;
    JSONArray staffArrray;
    JSONObject currentObject;
    String strMinute = "";
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtStaffReminderDate.setText(dayOfMonth + "/" + String.valueOf(month + 1) + "/" + year);
            reminderDatePicker.dismiss();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff_reminder_actvity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Staff Reminder");
        Bundle bundle = getIntent().getExtras();
        strSchoolId = bundle.getString("SchoolId");
        strStaffId = bundle.getString("StaffId");
        strCurrentObject=bundle.getString("ReminderData");
        strAcademicYearId=bundle.getString("AcademicYearId");
        strStaffList=bundle.getString("SatffList");
        init();
        allEvents();
        setData();
        rmdYear = reminderDate.get(Calendar.YEAR);
        rmdMonth = reminderDate.get(Calendar.MONTH);
        rmdDay = reminderDate.get(Calendar.DAY_OF_MONTH);
        reminderDatePicker = new DatePickerDialog(EditStaffReminderActvity.this, nlistner, rmdYear, rmdMonth, rmdDay);
        reminderDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
    public  void init(){
        tvStaffRemindertitle= findViewById(R.id.tvStaffRemindertitle);
        spnrStaffReminderSentTo= findViewById(R.id.spnrStaffReminderSentTo);
        edtStaffReminder= findViewById(R.id.edtStaffReminder);
        edtStaffReminderDate= findViewById(R.id.edtStaffReminderDate);
        edtStaffReminderTime= findViewById(R.id.edtStaffReminderTime);
        btnStaffReminderEdit= findViewById(R.id.btnStaffReminderEdit);

     }
    public void allEvents(){
        spnrStaffReminderSentTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvStaffId = (TextView) spnrStaffReminderSentTo.getSelectedView().findViewById(R.id.stafId);
                TextView tvStaffName = (TextView) spnrStaffReminderSentTo.getSelectedView().findViewById(R.id.stafName);
                tvStaffId.setTextColor(Color.WHITE);
                tvStaffName.setTextColor(Color.WHITE);
                strStaffNameId = tvStaffId.getText().toString() + tvStaffName.getText().toString();
                strSentTo = parent.getItemAtPosition(position).toString();
                strStaff = tvStaffId.getText().toString();
                Log.e("Tag", "" + strStaffNameId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtStaffReminderDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtStaffReminderDate) {
                    reminderDatePicker.show();
                    return true;
                }
                return false;
            }
        });

        btnStaffReminderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strReminder.length()>1){
                new EditStaffReminder().execute();
                }else {
                    Toast.makeText(EditStaffReminderActvity.this, "Please enter the Reminder to edit", Toast.LENGTH_SHORT).show();
                }

            }
        });
        edtStaffReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePicker = new TimePickerDialog(EditStaffReminderActvity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM = " AM";
                        if (selectedHour >= 12) {
                            AM_PM = " PM";
                            if (selectedHour >= 13 && selectedHour < 24) {
                                selectedHour -= 12;
                            } else {
                                selectedHour = 12;
                            }
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        strMinute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            strMinute = "0" + strMinute;

                        }
                        edtStaffReminderTime.setText(selectedHour + ":" + strMinute + AM_PM);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }
    public void setData(){
        try{
            staffArrray= new JSONArray(strStaffList);
            currentObject= new JSONObject(strCurrentObject);
            strReminder=currentObject.getString("Reminder");
            strReminderId=currentObject.getString("ReminderId");
            strReminderTitleId=currentObject.getString("ReminderTitleId");
            strSentTo=currentObject.getString("SentTo");
            strReminderTitle=currentObject.getString("ReminderTitle");
            strReminderCreatedDate=currentObject.getString("ReminderSetDate");
            strReminderDate=currentObject.getString("ReminderDate");
            strReminderEditDate=currentObject.getString("ReminderEditDate");
            strReminderTime=currentObject.getString("ReminderEditTime");
        }catch (Exception e){

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        tvStaffRemindertitle.setText(strReminderTitle);
        edtStaffReminder.setText(strReminder);
        StaffSpinnerAdapter adapter = new StaffSpinnerAdapter(staffArrray,EditStaffReminderActvity.this);
        spnrStaffReminderSentTo.setAdapter(adapter);
        int spnrPosistion=getSpinnerPosition();
        spnrStaffReminderSentTo.setSelection(spnrPosistion);
        edtStaffReminderDate.setText(strReminderEditDate);
        if(strReminderTime.equalsIgnoreCase("9999")){
            edtStaffReminderTime.setText("");
        }else {
            edtStaffReminderTime.setText(strReminderTime);
        }
    }
    public int getSpinnerPosition(){
        int pos=0;

        for( int i=0;i<staffArrray.length();i++)
        { try {
            String strItem = staffArrray.getJSONObject(i).getString("StaffDetailsId");

            if(strItem.equalsIgnoreCase(strSentTo)){
                pos=i;
                break;
            }

        }catch (Exception e){
            e.printStackTrace();

        }

        }
        return pos;
    }

    class EditStaffReminder extends AsyncTask<String,JSONArray,Void>{
        ProgressDialog progressDialog= new ProgressDialog(EditStaffReminderActvity.this);
        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Editing the Staff reminder please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editStaffReminder));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_ReminderId), strReminderId);
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                noticeBoardData.put(getString(R.string.key_EnteredBy), strStaffId);
                //strNoticeCreatedDate="10/10/2018";
                noticeBoardData.put(getString(R.string.key_ReminderSetDate), strReminderCreatedDate);
                noticeBoardData.put(getString(R.string.key_SentTo), spnrStaffReminderSentTo.getSelectedItem().toString());
                noticeBoardData.put(getString(R.string.key_ReminderTitle), strReminderTitleId);
                if (strReminderTitleId.equalsIgnoreCase("1")) {
                   String base64OtherTitle= StringUtil.textToBase64(strReminderTitle);
                    noticeBoardData.put(getString(R.string.key_OtherTitle),base64OtherTitle );
                } else {
                    noticeBoardData.put(getString(R.string.key_OtherTitle),null );
                }
                strReminder=edtStaffReminder.getText().toString();
                String base64Reminder= StringUtil.textToBase64(strReminder);
                noticeBoardData.put(getString(R.string.key_Reminder), base64Reminder);
                String checkNoticeTime=edtStaffReminderTime.getText().toString();
                if(checkNoticeTime.length()>1){
                    noticeBoardData.put(getString(R.string.key_ReminderTime),checkNoticeTime );
                }else{
                    noticeBoardData.put(getString(R.string.key_ReminderTime),"-9999" );
                }
                noticeBoardData.put(getString(R.string.key_ReminderDate), edtStaffReminderDate.getText().toString());
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage = "";
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success)) || (strStatus.equalsIgnoreCase(getString(R.string.key_Fail)))) {
                    strMessage = inObject.getString(getString(R.string.key_Message));
                    showAlert(strMessage, strStatus);
                }  else {
                    strStatus = "Error";
                    strMessage = "Something went wrong while editing";
                    showAlert(strMessage, strStatus);
                }


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

        private void showAlert(String alertMessage, String alertTitle) {
            EditStaffReminderActvity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditStaffReminderActvity.this);
                    builder.setMessage(alertMessage);
                    builder.setTitle(alertTitle);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(alertTitle.equalsIgnoreCase(getString(R.string.key_Success))){
                                onBackPressed();
                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }
}
