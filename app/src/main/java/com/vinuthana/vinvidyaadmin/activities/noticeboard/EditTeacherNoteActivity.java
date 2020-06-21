package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StaffSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class EditTeacherNoteActivity extends AppCompatActivity {
    String strSchoolId,strStaffId,strCurrentObject,strAcademicYearId,strStaffList,strStaff,strStfId;
    String strSentTo,strNoticeTitle,strNotice,strNoticeDate,strNoticeTime,strNoticeotherTitle,strNoticeId;
    String strNoticeTitleId,strNoticeEditDate,strNoticeCreatedDate;
    TextView tvTchrNoteNoticetitle;
   EditText edtTchrNoteTxtbxNotice,edtTchrNoteTxtBxNoticeDate,edtTchrNoteTxtBxNoticeTime;
   Button btnTchrNoteEditNotice;
   Spinner spnrTchrNoteSentTo;
   JSONArray staffArrray;
   JSONObject currentObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit teacher's note");
        Bundle bundle = getIntent().getExtras();
        strSchoolId = bundle.getString("SchoolId");
        strStaffId = bundle.getString("StaffId");
        strCurrentObject=bundle.getString("NoticeData");
        strAcademicYearId=bundle.getString("AcademicYearId");
        strStaffList=bundle.getString("StaffList");

        init();
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
    public  void init(){
        tvTchrNoteNoticetitle= findViewById(R.id.tvTchrNoteNoticetitle);
        edtTchrNoteTxtbxNotice=findViewById(R.id.edtTchrNoteTxtbxNotice);
        edtTchrNoteTxtBxNoticeDate=findViewById(R.id.edtTchrNoteTxtBxNoticeDate);
        edtTchrNoteTxtBxNoticeTime=findViewById(R.id.edtTchrNoteTxtBxNoticeTime);
        btnTchrNoteEditNotice=findViewById(R.id.btnTchrNoteEditNotice);
        spnrTchrNoteSentTo= findViewById(R.id.spnrTchrNoteSentTo);
        try {

            staffArrray= new JSONArray(strStaffList);
            currentObject= new JSONObject(strCurrentObject);
            strNotice=currentObject.getString("Notice");
            strNoticeTime=currentObject.getString("NoticeEditTime");
            strNoticeDate=currentObject.getString("NoticeDate");
            strNoticeId=currentObject.getString("NoticeId");
            strNoticeTitleId=currentObject.getString("NoticeTitleId");
            strSentTo=currentObject.getString("ToWhom");
            strNoticeEditDate=currentObject.getString("NoticeEditDate");
            strNoticeTitle=currentObject.getString("NoticeTitle");
            strNoticeCreatedDate=currentObject.getString("NoticeCreatedDate");
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public void allEvents(){
        spnrTchrNoteSentTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView tvStaffId = (TextView) spnrTchrNoteSentTo.getSelectedView().findViewById(R.id.stafId);
                TextView tvStaffName = (TextView) spnrTchrNoteSentTo.getSelectedView().findViewById(R.id.stafName);
                tvStaffId.setTextColor(Color.WHITE);
                tvStaffName.setTextColor(Color.WHITE);
                strStaff = tvStaffId.getText().toString() + tvStaffName.getText().toString();
                strStfId = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTchrNoteEditNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditTeacherNote().execute();
            }
        });
        tvTchrNoteNoticetitle.setText(strNoticeTitle);
        edtTchrNoteTxtbxNotice.setText(strNotice);

        StaffSpinnerAdapter adapter = new StaffSpinnerAdapter(staffArrray, EditTeacherNoteActivity.this);
        spnrTchrNoteSentTo.setAdapter(adapter);
        int spinnerPosition=getSpinnerPosition();
        spnrTchrNoteSentTo.setSelection(spinnerPosition);
        edtTchrNoteTxtBxNoticeDate.setText(strNoticeEditDate);
        if(strNoticeTime.equalsIgnoreCase("9999")){
            edtTchrNoteTxtBxNoticeTime.setText("");
        }else{
            edtTchrNoteTxtBxNoticeTime.setText(strNoticeTime);
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
    class EditTeacherNote extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditTeacherNoteActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Editing the teachers note please wait !!!");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editTeachersNoteById));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_NoticeId), strNoticeId);
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                noticeBoardData.put(getString(R.string.key_SetBy), strStaffId);
                //strNoticeCreatedDate="10/10/2018";
                noticeBoardData.put(getString(R.string.key_NoteSentDate), strNoticeCreatedDate);
                noticeBoardData.put(getString(R.string.key_SentTo), spnrTchrNoteSentTo.getSelectedItem().toString());
                noticeBoardData.put(getString(R.string.key_NoteTitleId), strNoticeTitleId);
                if (strNoticeTitleId.equalsIgnoreCase("1")) {
                    String base64NoticeTitle=StringUtil.textToBase64(strNoticeTitle);
                    noticeBoardData.put(getString(R.string.key_OtherNoteTitle),base64NoticeTitle );
                } else {
                    noticeBoardData.put(getString(R.string.key_OtherNoteTitle),null );
                }
                strNotice=edtTchrNoteTxtbxNotice.getText().toString();
                String base64Notice= StringUtil.textToBase64(strNotice);
                noticeBoardData.put(getString(R.string.key_Notice), base64Notice);
                String checkNoticeTime=edtTchrNoteTxtBxNoticeTime.getText().toString();
                if(checkNoticeTime.length()>1){
                    noticeBoardData.put(getString(R.string.key_NoticeTime),checkNoticeTime );
                }else{
                    noticeBoardData.put(getString(R.string.key_NoticeTime),"9999" );
                }
                noticeBoardData.put(getString(R.string.key_NoteOnDate), edtTchrNoteTxtBxNoticeDate.getText().toString());
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
            EditTeacherNoteActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditTeacherNoteActivity.this);
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
