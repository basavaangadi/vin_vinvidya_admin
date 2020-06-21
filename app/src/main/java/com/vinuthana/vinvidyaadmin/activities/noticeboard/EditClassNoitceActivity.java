package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassNoticeTitleSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class EditClassNoitceActivity extends AppCompatActivity {

    String strSchoolId,strStaffId,strCurrentObject,strAcademicYearId,strStudentList,strStfId,strClass;
    String strNoticeTitle,strNotice,strNoticeDate,strNoticeTime,strNoticeotherTitle,strNoticeId;
    String strNoticeTitleId,strNoticeEditDate,strNoticeCreatedDate,strNoticeEditTime,strClassId;
    TextView tvClassNoticeTitle, tvClassNoticeClass,tvEditClassNoticeSentDate,tvEditClassNoticeClass;
    EditText edtEditClassNotice,edtEditClassNoticeDate,edtEditClassNoticeTime;
    Button btnEditClassNotice;
    JSONObject currentObject;
    DatePickerDialog noticeDatePicker;
    int ntcYear, ntcMonth, ntcDay;
    TimePickerDialog mTimePicker;
    Calendar noticeDate = Calendar.getInstance();
    int hour = noticeDate.get(Calendar.HOUR_OF_DAY);
    int minute = noticeDate.get(Calendar.MINUTE);
    String strMinute = "", base64ClassNotice="";
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtEditClassNoticeDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            noticeDatePicker.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_noitce);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Class Notice");
        Bundle bundle = getIntent().getExtras();
        strSchoolId = bundle.getString("SchoolId");
        strStaffId = bundle.getString("StaffId");
        strCurrentObject=bundle.getString("NoticeData");
        strAcademicYearId=bundle.getString("AcademicYearId");

        strClassId=bundle.getString("ClassId");
        init();
        setValue();
        allEvents();

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

   public void init(){

       tvClassNoticeClass=findViewById(R.id.tvClassNoticeClass);
        tvEditClassNoticeSentDate=findViewById(R.id.tvEditClassNoticeSentDate);
        tvClassNoticeTitle =findViewById(R.id.tvClassNoticeTitle);
        edtEditClassNotice=findViewById(R.id.edtEditClassNotice);
        edtEditClassNoticeDate=findViewById(R.id.edtEditClassNoticeDate);
        edtEditClassNoticeTime=findViewById(R.id.edtEditClassNoticeTime);
        btnEditClassNotice=findViewById(R.id.btnEditClassNotice);

    }
    public void allEvents(){
        btnEditClassNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strNotice=edtEditClassNotice.getText().toString();
                base64ClassNotice=StringUtil.textToBase64(strNotice);
                if(strNotice.length()<3||strNoticeDate.length()<3){
                    Toast.makeText(EditClassNoitceActivity.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    new EditClassNotice().execute();
                }
            }
        });
    }
    public void setValue(){
     try{

         JSONObject object = new JSONObject(strCurrentObject);

         strNotice = object.getString("Notice");
         edtEditClassNotice.setText(strNotice);
         strNoticeId = object.getString("NoticeId");

         strNoticeDate = object.getString("NoticeDate");
         edtEditClassNoticeDate.setText(strNoticeDate);
         strClassId = object.getString("ClassId");
         strClass=object.getString("Class");
         String text = "<font color=#FF8C00>Class : </font> <font color=#ffffff>"+strClass+"</font>";
         tvClassNoticeClass.setText(Html.fromHtml(text));
         //tvClassNoticeClass.setText(strClass);
         strNoticeCreatedDate=object.getString("NoticeCreatedDate");
         String createdText = "<font color=#FF8C00>Sent on : </font> <font color=#ffffff>"+strNoticeCreatedDate+"</font>";
         tvEditClassNoticeSentDate.setText(Html.fromHtml(createdText));
         //tvEditClassNoticeSentDate.setText(strNoticeCreatedDate);
         strNoticeTitle = object.getString("NoticeTitle");
         tvClassNoticeTitle.setText(strNoticeTitle);
         strNoticeTitleId=object.getString("NoticeTitleId");
         strNoticeTime = object.getString("NoticeTime");
         if(strNoticeTime.equalsIgnoreCase("9999")){
             edtEditClassNoticeTime.setText("");
         }else {

             String base64ClassNotice= StringUtil.textToBase64(strNoticeTitle);
             edtEditClassNoticeTime.setText(strNoticeTime);
         }





     }catch (Exception e){
         Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
     }
    }
    class EditClassNotice extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(EditClassNoitceActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Editing class notice");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editClassNotice));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_NoticeId),strNoticeId);
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                noticeBoardData.put(getString(R.string.key_SetBy), strStaffId);
                noticeBoardData.put(getString(R.string.key_ClassId), strClassId);
                noticeBoardData.put(getString(R.string.key_NoticeSetDate), strNoticeCreatedDate);
                noticeBoardData.put(getString(R.string.key_NoticeTitle), strNoticeTitleId);
                if (strNoticeTitleId.equalsIgnoreCase("1")) {
                     String base64OtherTitle= StringUtil.textToBase64(strNoticeTitle);
                    noticeBoardData.put(getString(R.string.key_OtherTitle), base64OtherTitle);
                }

                noticeBoardData.put(getString(R.string.key_Notice), base64ClassNotice);
                strNoticeDate=edtEditClassNoticeDate.getText().toString();
                noticeBoardData.put(getString(R.string.key_NoticeDate),strNoticeDate);
                strNoticeTime= edtEditClassNoticeTime.getText().toString();
                if (!(strNoticeTime.length() > 1)) {
                    strNoticeTime = "9999";
                }
                noticeBoardData.put(getString(R.string.key_NoticeTime), strNoticeTime);


                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
              String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage=inObject.getString(getString(R.string.key_Message));
               /* if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                   // publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    showAlert(strStatus,strMessage);
                } else {*/
                showAlert(strStatus,strMessage);

                //}
            } catch (Exception ex) {
              //  ex.printStackTrace();
                toastError(ex.toString());
                //Toast.makeText(EditClassNoitceActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            /*
            ClassNoticeTitleSpinnerAdapter adapter = new ClassNoticeTitleSpinnerAdapter(values[0], getActivity());
            spnrWriteClassNoticeSection.setPrompt("Choose Section");
            spnrWriteClassNoticeTitle.setAdapter(adapter);*/
        }
public void toastError(String strExecption){
       EditClassNoitceActivity.this.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(EditClassNoitceActivity.this,strExecption, Toast.LENGTH_SHORT).show();
           }
       });
}
        public void showAlert(String strTitle,String strMessage){
            EditClassNoitceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditClassNoitceActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", null);
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
