package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.vinuthana.vinvidyaadmin.adapters.AssignmentAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AssignmentActivity extends AppCompatActivity {
    Spinner spnrAssignmentClass, spnrAssignmentSection;
    RecyclerView assignmentRecyclerView;
    Button btnSubmit;
    private Session session;
    TextView tmpViewSection;
    public String strStaffId, strExam, strSchoolId, strClass, strClassId, strSubjectId,strSetBy,strAssignmentId,strTitle,strDescription;
    String strClassSection, strAcademicYearId,strFilePath,strFileName,strEditDate;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();
    JSONArray AssignmentArray = new JSONArray();
    AssignmentAdapter recyclerAdapter;
    ProgressDialog pDialog;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {"android.permission.ACCESS_NETWORK_STATE" ,
            "android.permission.INTERNET","android.permission.WRITE_EXTERNAL_STORAGE" ,
            "android.permission.READ_PHONE_STATE","android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.assignmentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.key_Assignment);
        if (!connection.netInfo(AssignmentActivity.this)) {
            connection.buildDialog(AssignmentActivity.this).show();
        } else {
            init();
            allEvents();
            new GetClass().execute();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
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

    public void allEvents() {
        spnrAssignmentClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrAssignmentClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrAssignmentSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tmpViewSection = (TextView) spnrAssignmentSection.getSelectedView().findViewById(R.id.list);
                tmpViewSection.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                strClassSection=strClass+" "+tmpViewSection.getText().toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetAssignment().execute();
            }
        });



    }
    public void initRecylerAdpater(){
        recyclerAdapter = new AssignmentAdapter(AssignmentArray, AssignmentActivity.this,strClassSection);
        recyclerAdapter.setOnButtonClickListener(new AssignmentAdapter.OnAssignmentClickListener() {
            @Override
            public void onDownload(JSONObject downloadData, int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AssignmentActivity.this);
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder.setMessage("Would you like to Download file");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            strFilePath=downloadData.getString(getString(R.string.key_File_path));
                            strFilePath=strFilePath.replace(" ","%20");
                            strFileName = downloadData.getString(getString(R.string.key_Filename));
                            //strFilePath = "https://api.androidhive.info/progressdialog/hive.jpg";
                            /*strFilePath = "http://www.africau.edu/images/default/sample.pdf";
                            strFileName = "sample.pdf";*/
                            if(ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[0]) != PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[1]) != PackageManager.PERMISSION_GRANTED ||

                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[2]) != PackageManager.PERMISSION_GRANTED ||

                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[3]) != PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(AssignmentActivity.this, mPermission[4]) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(AssignmentActivity.this, mPermission, REQUEST_CODE_PERMISSION);
                           }else{
                                new Download().execute();
                            }

                        } catch (Exception e) {
                            Log.e("onAssignmet", e.toString());
                            Toast.makeText(AssignmentActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();


            }

            @Override
            public void onEdit(JSONObject EditData, int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AssignmentActivity.this);
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder.setMessage("Would you like to Edit file");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            dialog.dismiss();
                            ShowAlert(position,EditData);
                        } catch (Exception e) {
                            Log.e("onSyllabus", e.toString());
                            Toast.makeText(AssignmentActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AssignmentActivity.this);
        assignmentRecyclerView.setLayoutManager(layoutManager);
        assignmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        assignmentRecyclerView.setAdapter(recyclerAdapter);

    }

    public void ShowAlert(int position,JSONObject jsonData) {
        final Dialog dialog = new Dialog(AssignmentActivity.this);
        dialog.setContentView(R.layout.custom_alert_assignment);
        EditText editDescription = (EditText) dialog.findViewById(R.id.editDescription);
        EditText editTitle = (EditText) dialog.findViewById(R.id.editTitle);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        dialog.setTitle(getString(R.string.key_Edit));
        try {
            editDescription.setText(jsonData.getString(getString(R.string.key_Description)));
            editTitle.setText(jsonData.getString(getString(R.string.key_Title)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // if button is clicked, close the custom dialog
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    strTitle = editTitle.getText().toString();
                    strDescription = editDescription.getText().toString();
                    strAssignmentId = jsonData.getString(getString(R.string.key_Assignment_id));
                    strSubjectId = jsonData.getString(getString(R.string.key_SubjectId));
                    strSetBy = jsonData.getString(getString(R.string.key_AddedBy));
                    DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
                    strEditDate = dateFormat.format(new Date());
                    strFilePath = jsonData.getString(getString(R.string.key_File_path));
                    new updateAssignment().execute();
                } catch (Exception e) {
                    Toast.makeText(AssignmentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void init() {
        session = new Session(getApplicationContext());
        spnrAssignmentClass = (Spinner) findViewById(R.id.spnrAssignmentClass);
        spnrAssignmentSection = (Spinner) findViewById(R.id.spnrAssignmentSection);
        assignmentRecyclerView = (RecyclerView) findViewById(R.id.assignmentRecyclerView);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        initRecylerAdpater();
    }

    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AssignmentActivity.this);
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_Staff_ClassNames));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_SchoolId), strSchoolId);
                classSubjectData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));

                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                        publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getApplicationContext());
            spnrAssignmentClass.setPrompt("Choose Class");
            spnrAssignmentClass.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetSection extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_Staff_Class_With_Section));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_SchoolId), strSchoolId);
                classSubjectData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                classSubjectData.put(getString(R.string.key_Clas), strClass);
                outObject.put("classSubjectData", classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], getApplicationContext());
            spnrAssignmentSection.setPrompt("Choose Section");
            spnrAssignmentSection.setAdapter(adapter);
        }
    }

    class updateAssignment extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_editAssignmentDetails));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_SchoolId), strSchoolId);
                classSubjectData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                classSubjectData.put(getString(R.string.key_SubjectId), strSubjectId);
                classSubjectData.put(getString(R.string.key_SetBy), strSetBy);
                classSubjectData.put(getString(R.string.key_AssignmentId), strAssignmentId);
                classSubjectData.put(getString(R.string.key_Title), strTitle);
                classSubjectData.put(getString(R.string.key_Description), strDescription);
                classSubjectData.put(getString(R.string.key_FilePath), strFilePath);
                classSubjectData.put(getString(R.string.key_EditDate), strEditDate);
                outObject.put("otherData", classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "response =" + responseText);
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AssignmentActivity.this);
            alertDialogBuilder.setTitle("Alert!");
            alertDialogBuilder.setMessage("Assignment updated successfully");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        Log.e("onAssignment", e.toString());
                        Toast.makeText(AssignmentActivity.this, "onAssignment" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).show();
        }
    }

    private class GetAssignment extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AssignmentActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Assignments...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = 0; i < AssignmentArray.length(); i++) {
                    AssignmentArray.remove(i);
                }
            }else{
                AssignmentArray= new JSONArray();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseAssignmentList));
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_otherData), otherData);
                Log.e("TAG", "GetAssignment, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetAssignment, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus= inObject.getString(getString(R.string.key_Status));
                JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    for(int i=0;i<resultArray.length();i++) {
                        AssignmentArray.put(resultArray.getJSONObject(i));
                        //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    }
                }
               // publishProgress(result);
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
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
               recyclerAdapter.notifyDataSetChanged();
            }else{
                initRecylerAdpater();
            }
        }
    }
    public  class Download extends AsyncTask<String,String,String> {

        String folder;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AssignmentActivity.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;

            try {
                URL url = new URL(strFilePath);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lengthOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                 folder= Environment.getExternalStorageDirectory()  + "/Vinvidya Staff/";
                File file = new File(folder);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, strFileName);
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = conection.getInputStream();

                byte[] buffer = new byte[1024];
                long total = 0;
                int len1;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    fos.write(buffer, 0, len1);
                    publishProgress(String.valueOf(Math.min((int) (total * 100 / lengthOfFile), 100)));
                    Thread.yield();
                }
                fos.close();
                is.close();

            }catch (Exception ex){
                Log.e("Cathed Error",ex.toString());
                /*AssignmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast= Toast.makeText(AssignmentActivity.this,ex.toString(),Toast.LENGTH_SHORT);
                    }
                });*/
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
            pDialog.setMessage("downloading "+values[0]+"/100");
        }

        @Override
        protected void onPostExecute(String location) {
            super.onPostExecute(location);
            //dismissDialog(progress_bar_type);
            //removeDialog(progress_bar_type);
            String imagePath = Environment.getExternalStorageDirectory().toString()+strFileName;
            pDialog.setMessage("download complete");
            Toast.makeText(getApplicationContext(),
                    "File has been downloaded at "+folder+"/"+strFileName, Toast.LENGTH_LONG).show();
            pDialog.dismiss();

            //  String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }
}
