package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.EditHomeWorkActivity;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditNoticeActivity extends AppCompatActivity {
    String strSchoolId,strStaffId,strCurrentObject,strAcademicYearId;
    TextView tvNoticetitle;
    Button btnEditNotice;

    EditText edtTxtbxNotice,edtTxtBxNoticeDate,edtTxtBxNoticeTime;
    Spinner  spnrToWhom;
    JSONArray jsonArray;
    String[] item = new String[]{"All Staff","All"};
    String strNoticeTitle,strNotice,strNoticeDate,strNoticeTime,strToWhom,strNoticeId;
    String strNoteDateTime,strNoticeTitleId;
    DatePickerDialog noticeDatePicker;
    int ntcYear, ntcMonth, ntcDay;
    TimePickerDialog mTimePicker;
    Calendar noticeDate = Calendar.getInstance();
    int hour = noticeDate.get(Calendar.HOUR_OF_DAY);
    int minute = noticeDate.get(Calendar.MINUTE);
    String strMinute = "";
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtTxtBxNoticeDate.setText(dayOfMonth + "/" + String.valueOf(month + 1) + "/" + year);
            noticeDatePicker.dismiss();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Notice");
        Bundle bundle = getIntent().getExtras();
        strSchoolId = bundle.getString("SchoolId");
        strStaffId = bundle.getString("StaffId");
        strCurrentObject=bundle.getString("NoticeData");
        strAcademicYearId=bundle.getString("AcademicYearId");
        init();
        allEvents();
        ntcYear = noticeDate.get(Calendar.YEAR);
        ntcMonth = noticeDate.get(Calendar.MONTH);
        ntcDay = noticeDate.get(Calendar.DAY_OF_MONTH);
        noticeDatePicker = new DatePickerDialog(EditNoticeActivity.this, nlistner, ntcYear, ntcMonth, ntcDay);
        noticeDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


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

        tvNoticetitle= findViewById(R.id.tvNoticetitle);
        edtTxtbxNotice=findViewById(R.id.edtTxtbxNotice);
        edtTxtBxNoticeDate=findViewById(R.id.edtTxtBxNoticeDate);
        edtTxtBxNoticeTime=findViewById(R.id.edtTxtBxNoticeTime);
        spnrToWhom=findViewById(R.id.spnrToWhom);
        btnEditNotice=findViewById(R.id.btnEditNotice);

        try {

            JSONObject object = new JSONObject(strCurrentObject);

            strNotice = object.getString("Notice");
            strNoticeId = object.getString("NoticeId");
            strNoticeTime = object.getString("NoticeEditTime");
            strNoticeDate = object.getString("NoticeEditDate");
            strToWhom = object.getString("ToWhom");
            strNoticeTitle = object.getString("NoticeTitle");
            strNoticeTitleId=object.getString("NoticeTitleId");
            if(strNoticeTime.equalsIgnoreCase("9999")){
                edtTxtBxNoticeTime.setText("");
            }else {
                edtTxtBxNoticeTime.setText(strNoticeTime);
            }

            edtTxtbxNotice.setText(strNotice);
            edtTxtBxNoticeDate.setText(strNoticeDate);
            tvNoticetitle.setText(strNoticeTitle);
            final List<String> itemList = new ArrayList<>(Arrays.asList(item));

            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(EditNoticeActivity.this, R.layout.spinner_item, itemList) {
                @Override
                public boolean isEnabled(int position) {

                        return true;

                }
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);

                    return view;
                }
            };
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            spnrToWhom.setAdapter(spinnerAdapter);

           int spinnerPosition=getSpinnerPosition();
            spnrToWhom.setSelection(spinnerPosition);
        }catch (Exception e){

            e.printStackTrace();
        }

    }
    public void allEvents(){
        btnEditNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new EditNotice().execute();
            }
        });
        edtTxtBxNoticeDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.edtTxtBxNoticeDate) {
                    noticeDatePicker.show();
                    return true;
                }
                return false;
            }
        });
        edtTxtBxNoticeTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(view.getId()==R.id.edtTxtBxNoticeTime){
                    mTimePicker = new TimePickerDialog(EditNoticeActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                            edtTxtBxNoticeTime.setText(selectedHour + ":" + strMinute + AM_PM);
                        }
                    }, hour, minute, false);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                    return true;
                }
                return false;
                }
        });
    }
    public int getSpinnerPosition(){
        int pos=0;

        for( int i=0;i<item.length;i++)
        { try {
            String strItem = item[i];

            if(strItem.equalsIgnoreCase(strToWhom)){
                pos=i;
                break;
                }

        }catch (Exception e){
            e.printStackTrace();

        }

        }
        return pos;
    }
class EditNotice extends AsyncTask<String, JSONArray, Void>{
        String url= AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;
        String strStatus,strMessage;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog= new ProgressDialog(EditNoticeActivity.this);
        progressDialog.setMessage(" Editing Notice please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        GetResponse response = new GetResponse();
        JSONObject outObject = new JSONObject();

        try {
            outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editNoticeById));
            JSONObject noticeBoardData = new JSONObject();
            noticeBoardData.put(getString(R.string.key_NoticeId), strNoticeId);
            noticeBoardData.put(getString(R.string.key_SchoolId),strSchoolId);
            noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
            noticeBoardData.put(getString(R.string.key_SetBy),strStaffId);
            noticeBoardData.put(getString(R.string.key_NoteSetDate),strNoticeDate);
            noticeBoardData.put(getString(R.string.key_ToWhom),spnrToWhom.getSelectedItem().toString());
            noticeBoardData.put(getString(R.string.key_NoticeTitle),strNoticeTitleId);
            if(strNoticeTitleId.equalsIgnoreCase("1")){

                String base64NoticeTitle= StringUtil.textToBase64(strNoticeTitle);
                noticeBoardData.put(getString(R.string.key_OtherTitle),base64NoticeTitle);

            }else{
                noticeBoardData.put(getString(R.string.key_OtherTitle),"");
            }
           strNotice=edtTxtbxNotice.getText().toString();
            String base64Notice=StringUtil.textToBase64(strNotice);
            noticeBoardData.put(getString(R.string.key_Notice),base64Notice);
            noticeBoardData.put(getString(R.string.key_NoticeDate),edtTxtBxNoticeDate.getText().toString());
            String checkNoteTime=edtTxtBxNoticeTime.getText().toString();
            if(checkNoteTime.length()>1){
                noticeBoardData.put(getString(R.string.key_NoticeTime),checkNoteTime);
            }else {
            noticeBoardData.put(getString(R.string.key_NoticeTime),"9999");
            }
            outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
            Log.e("Tag", "outObject =" + outObject.toString());
            String responseText = response.getServerResopnse(url, outObject.toString());
            JSONObject inObject = new JSONObject(responseText);

            Log.e("Tag", "responseText is =" + responseText);
            strStatus = inObject.getString(getString(R.string.key_Status));
            if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||(strStatus.equalsIgnoreCase(getString(R.string.key_Fail)))) {
                strMessage=inObject.getString(getString(R.string.key_Message));
                showAlert(strMessage, strStatus);
            }
            else {
                strStatus="Error";
                strMessage="Something went wrong while editing";
                showAlert(strMessage,strStatus);
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
        EditNoticeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditNoticeActivity.this);
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
