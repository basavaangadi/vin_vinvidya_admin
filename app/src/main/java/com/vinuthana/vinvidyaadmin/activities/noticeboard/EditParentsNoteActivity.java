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
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StudentListSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class EditParentsNoteActivity extends AppCompatActivity {
    String strSchoolId,strStaffId,strCurrentObject,strAcademicYearId,strStudentList,strStudentName,strStfId,strClass;
    String strSentTo,strNoticeTitle,strNotice,strNoticeDate,strNoticeTime,strNoticeotherTitle,strNoticeId;
    String strNoticeTitleId,strNoticeEditDate,strNoticeCreatedDate,strNoticeEditTime,strClassId;
    TextView tvPrntNoteNoticetitle,tvPrntNoteClass;
    EditText edtPrntNoteTxtbxNotice,edtPrntNoteTxtBxNoticeDate,edtPrntNoteTxtBxNoticeTime;
    Button btnPrntNoteEditNotice;
    Spinner spnrPrntNoteSentTo;
    JSONArray studentArrray;
    JSONObject currentObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parents_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Parent's Note");
        Bundle bundle = getIntent().getExtras();
        strSchoolId = bundle.getString("SchoolId");
        strStaffId = bundle.getString("StaffId");
        strCurrentObject=bundle.getString("NoticeData");
        strAcademicYearId=bundle.getString("AcademicYearId");
        strStudentList=bundle.getString("StudentList");
        strClassId=bundle.getString("ClassId");


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
    public void init(){
        edtPrntNoteTxtbxNotice= findViewById(R.id.edtPrntNoteTxtbxNotice);
        edtPrntNoteTxtBxNoticeDate=findViewById(R.id.edtPrntNoteTxtBxNoticeDate);
        edtPrntNoteTxtBxNoticeTime=findViewById(R.id.edtPrntNoteTxtBxNoticeTime);
        tvPrntNoteNoticetitle= findViewById(R.id.tvPrntNoteNoticetitle);
        btnPrntNoteEditNotice=findViewById(R.id.btnPrntNoteEditNotice);
        spnrPrntNoteSentTo=findViewById(R.id.spnrPrntNoteSentTo);
        tvPrntNoteClass=findViewById(R.id.tvPrntNoteClass);
        try {
            studentArrray = new JSONArray(strStudentList);
            currentObject= new JSONObject(strCurrentObject);
            strNotice= currentObject.getString("Notice");
            strNoticeId=currentObject.getString("NoticeId");
            strClass=currentObject.getString("Class");
            strNoticeCreatedDate= currentObject.getString("NoticeCreatedDate");
            strNoticeTitleId=currentObject.getString("NoteTitleId");
            strStudentName=currentObject.getString("StudentName");
            strNoticeTitle=currentObject.getString("NoteTitle");
            strSentTo=currentObject.getString("StudentDetailsId");
            strClass=currentObject.getString("Class");
            strNoticeEditDate=currentObject.getString("NoticeEditDate");
            strNoticeEditTime=currentObject.getString("NoticeEditTime");

        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        StudentListSpinnerAdapter adapter = new StudentListSpinnerAdapter(studentArrray,EditParentsNoteActivity.this);
        spnrPrntNoteSentTo.setPrompt("Choose Student");
        spnrPrntNoteSentTo.setAdapter(adapter);
        tvPrntNoteNoticetitle.setText(strNoticeTitle);
        tvPrntNoteClass.setText(strClass);
        edtPrntNoteTxtbxNotice.setText(strNotice);
        edtPrntNoteTxtBxNoticeDate.setText(strNoticeEditDate);
        if(strNoticeEditTime.equalsIgnoreCase("9999")){
            edtPrntNoteTxtBxNoticeTime.setText("");
        }else {
        edtPrntNoteTxtBxNoticeTime.setText(strNoticeEditTime);
        }
        int spinnerPosition=getSpinnerPosition();
        spnrPrntNoteSentTo.setSelection(spinnerPosition);


    }
    public void allEvents(){
   spnrPrntNoteSentTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       @Override
       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           TextView tmpView = (TextView) spnrPrntNoteSentTo.getSelectedView().findViewById(R.id.list);
           tmpView.setTextColor(Color.WHITE);
           //strClassId = adapterView.getItemAtPosition(i).toString();
           strSentTo = parent.getItemAtPosition(position).toString();
           Log.e("Tag", "" + strSentTo);
       }

       @Override
       public void onNothingSelected(AdapterView<?> parent) {

       }
   });
   btnPrntNoteEditNotice.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
          new EditParentNote().execute();
       }
   });
    }

    public int getSpinnerPosition(){
        int pos=0;

        for( int i=0;i<studentArrray.length();i++)
        { try {
            String strItem = studentArrray.getJSONObject(i).getString("StudentDetailsId");

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
    class EditParentNote extends AsyncTask<String, JSONArray, Void>{
        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(EditParentsNoteActivity.this);
            progressDialog.setMessage("Editing parent's note please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editParentNote));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_NoticeId), strNoticeId);
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                noticeBoardData.put(getString(R.string.key_SetBy), strStaffId);
                //strNoticeCreatedDate="10/10/2018";
                noticeBoardData.put(getString(R.string.key_NoteSentDate), strNoticeCreatedDate);
                noticeBoardData.put(getString(R.string.key_ClassId), strClassId);
                noticeBoardData.put(getString(R.string.key_SentTo), spnrPrntNoteSentTo.getSelectedItem().toString());
                noticeBoardData.put(getString(R.string.key_NoteTitleId), strNoticeTitleId);
                if (strNoticeTitleId.equalsIgnoreCase("1")) {

                    String base64Noticetitle= StringUtil.textToBase64(strNoticeTitle);
                    noticeBoardData.put(getString(R.string.key_OtherNoteTitle),base64Noticetitle );

                } else {
                    noticeBoardData.put(getString(R.string.key_OtherNoteTitle),"" );
                }
                String strNotice= edtPrntNoteTxtbxNotice.getText().toString();
                String base64Notice=StringUtil.textToBase64(strNotice);
                noticeBoardData.put(getString(R.string.key_Notice),base64Notice);
                String checkNoticeTime=edtPrntNoteTxtBxNoticeTime.getText().toString();
                if(checkNoticeTime.length()>1){
                    noticeBoardData.put(getString(R.string.key_NoticeTime),checkNoticeTime );
                }else{
                    noticeBoardData.put(getString(R.string.key_NoticeTime),"9999" );
                }
                noticeBoardData.put(getString(R.string.key_NoteOnDate), edtPrntNoteTxtBxNoticeDate.getText().toString());
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject = " + outObject.toString());
                Log.e("Tag", "responseText is = " + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage = "";
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success)) || (strStatus.equalsIgnoreCase(getString(R.string.key_Alert)))) {
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
            EditParentsNoteActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditParentsNoteActivity.this);
                    builder.setMessage(alertMessage);
                    builder.setTitle(alertTitle);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }
}
