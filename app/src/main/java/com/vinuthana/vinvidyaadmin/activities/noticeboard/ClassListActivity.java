package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters.ClassData;
import com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters.ClassInfoAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.GetSoapResponse;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

public class ClassListActivity extends AppCompatActivity {

    String strStaffId, strClassId, strDate, strNoteSentDate, strNoticeId,
            strStatus, strMessage, strSchoolId, strMsg, strAcademicYearId, strNoteTitle, strOtherTitle;
    String strNote, strTime;
    ArrayList<ClassData> ClassList = new ArrayList<ClassData>();
    ArrayList<String> classArray= new ArrayList<String>();
    public ClassInfoAdapter adapter;
    JSONArray jsonArray = new JSONArray();
    JSONObject inObject;
    int resId;
    Button btnSendClassNt;
    ListView lstWriteClassNt;
    ProgressDialog progressDialog;
    JSONArray js_array = new JSONArray();
    ArrayList<String> ClassNameList = new ArrayList<String>();
    ArrayList<String> studRollList = new ArrayList<String>();
    String strDateExtra,strTimeExtra,strNt,strDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Class List");
        lstWriteClassNt=findViewById(R.id.lstWriteClassNt);
        btnSendClassNt=findViewById(R.id.btnSendClassNt);
        Bundle extras = getIntent().getExtras();


        strSchoolId = extras.getString(getString(R.string.key_SchoolId),"");
        strAcademicYearId = extras.getString(getString(R.string.key_AcademicYearId),"");
        strNoticeId = extras.getString(getString(R.string.key_NoticeTitleId),"");
        strStaffId = extras.getString(getString(R.string.key_SetBy),"");
        strNoteTitle = extras.getString(getString(R.string.key_NoteTitleName),"");
        strNote = extras.getString(getString(R.string.key_Notice),"");

        strDateExtra = extras.getString(getString(R.string.key_NoticeDate),"");
        strTimeExtra = extras.getString(getString(R.string.key_NoticeTime),"");







        new GetClassList().execute();
        allEvents();
    }


    public void allEvents() {
        btnSendClassNt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    for (int i = 0; i < adapter.getCount(); i++) {
                        //student is selected then Chekstatus of adapter will be 1

                        if (adapter.mCheckStates.get(i) == true) {
                            JSONObject json_obj = new JSONObject();
                            json_obj.put(getString(R.string.key_SchoolId), strSchoolId);
                            json_obj.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                            json_obj.put(getString(R.string.key_SetBy), strStaffId);
                            json_obj.put(getString(R.string.key_ClassId), ClassList.get(i).getClassId());
                            classArray.add(ClassList.get(i).getClasses());
                            json_obj.put(getString(R.string.key_NoticeTitleId), strNoticeId);
                            if(strNoticeId.equalsIgnoreCase("1")){
                                json_obj.put(getString(R.string.key_OtherTitle), strNoteTitle);
                                json_obj.put(getString(R.string.key_NoteTitleName),strNoteTitle);
                            }else {
                                json_obj.put(getString(R.string.key_NoteTitleName),strNoteTitle);
                            }
                            json_obj.put(getString(R.string.key_Notice), strNote);
                            json_obj.put(getString(R.string.key_NoticeDate),  strDateExtra);
                            json_obj.put(getString(R.string.key_NoticeTime), strTimeExtra);
                            js_array.put(json_obj);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ClassListActivity.this);
                            /*LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.list_layout, null);*/
                            LayoutInflater inflater = LayoutInflater.from(ClassListActivity.this);
                            View view = inflater.inflate(R.layout.custom_alert_dialog, null);
                            TextView textView = (TextView) view.findViewById(R.id.tview_message);
                            builder.setView(view);
                            builder.setCancelable(true);
                            builder.setTitle("Class list");

                            String strClassNames = "";
                            for (int i = 0; i < classArray.size(); i++) {
                                strClassNames += classArray.get(i) + "\n";
                            }
                            textView.setText(strClassNames);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new InsertClassNotice().execute();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Don't do anything
                                    js_array = new JSONArray();
                                    classArray.clear();

                                }
                            });
                            builder.create().show();
                        }
                    });



                }catch(Exception e){
                    Log.e("Tag",e.toString());
                }






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
    class InsertClassNotice extends AsyncTask<String, JSONArray, Void> {
        //GetResponse response = new GetResponse();
        GetSoapResponse soapResponse= new GetSoapResponse();
        String responseText1;
        //String url = AD.url.base_url + "noticeBoardOperation.jsp";
        String asmx_url=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_Multiple_ClassNotice";
        //String asmx_url=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_ParentNote";
        String alertMessage, alertTitle;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ClassListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                // outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertParentNote));
                //outObject.put("OperationName", "");//,"EnteredBy":"2","StudentId": "7", "Status":"0","Date":"18-01-2018"}}


                JSONObject noticeBoardData = new JSONObject();
                //noticeBoardData.put(getString(R.string.key_ParentNoteArray), js_array);
                Log.e("Tag", "outObject =" +js_array.toString());
                String methodName="Insert_Multiple_ClassNotice";
                List<PropertyInfo> param=new ArrayList<PropertyInfo>();
                PropertyInfo parentsNoteInsertData= new PropertyInfo();
                parentsNoteInsertData.setName("Insert_Multiple_ClassNoticeData");
                parentsNoteInsertData.setValue(js_array.toString());
                Log.e("Tag","outObjectObject : "+js_array.toString());
                param.add(parentsNoteInsertData);
                //final json object
               // outObject.put("noticeBoardData", noticeBoardData);


                String responseText1=soapResponse.getSOAPStringResponse(asmx_url,methodName,param);
               //<< get json string from server
                //JSONObject jsonObject = new JSONObject(responseText1);

                inObject = new JSONObject(responseText1);

                Log.e("Tag", "responseText is =" + responseText1);
                strMsg = inObject.getString("Message");
                strStatus=inObject.getString("Status");
                resId=0;
                if (strStatus.equals(getString(R.string.key_Success))) {
                        resId=Integer.parseInt(inObject.getString("resId"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                       /* if (strMsg.equals("Parent's note inserted sucessfully")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equals("Parent's note has been already added")) {
                            alertMessage = strMsg;
                            alertTitle = "Fail";
                        } else {
                            alertTitle = "Fail";
                            alertMessage = "Parents note couldn't be added because of some error";
                        }*/
                       if(resId>0||resId==-1){
                           showAlert(strMsg, strStatus);
                       }else {
                           showAlert("Error", "Something went wrong");
                       }

                    }
                });

                /*strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    jsonArray = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                }*/
            } catch (Exception e) {
               Log.e(" send Exe",e.toString());
               ClassListActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if(e.toString().equalsIgnoreCase("org.json.JSONException: End of input at character 0 of")){
                           showAlert("Success", "Class message sent Sucessfuly ");
                       }
                   }
               });

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
             classArray.clear();
            progressDialog.dismiss();
        }
    }

    class GetClassList extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ClassListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding data...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName", "getClasslist");
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put("SchoolId", strSchoolId);
                classSubjectData.put("AcademicYearId", strAcademicYearId);

                outObject.put("classSubjectData", classSubjectData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString("Status");
                strMessage = inObject.getString("Message");
                //String strRollNotSet=inObject.getString("Roll_Set");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(responseText).getJSONArray("Result"));
                    jsonArray = new JSONObject(responseText).getJSONArray("Result");
                }
                //resId=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));



            } catch (Exception ex) {
                Log.e("Error DoingBackground", ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    ClassData classData = new ClassData();
                    classData.setClasses(object.getString("Class"));
                    classData.setClassId(object.getString("ClassId"));
                    classData.setChecked(false);
                    ClassList.add(classData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

                adapter = new ClassInfoAdapter(ClassListActivity.this, R.layout.class_list_layout, ClassList, false);
                lstWriteClassNt.setAdapter(adapter);
            progressDialog.dismiss();

        }
    }
    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassListActivity.this);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(resId>0||resId==-1){
                    onBackPressed();
                }


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}