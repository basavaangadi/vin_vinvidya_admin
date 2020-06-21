package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.SyllabusAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SubjectSyllabusActivity extends AppCompatActivity {

    TextView tvSyllabusSubject, tvSubject,tvSyllabusClass;
    public Bundle bundle;
    String syllabusList, strStudentId;
    private Session session;
    String strStaffId, strExam, strSchoolId, strClass, strClassId, strSubjectId,strSubject;
    RecyclerView recyclerViewSyllabus;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_syllabus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSyllabus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.key_syllabus_details);
        if (!connection.netInfo(SubjectSyllabusActivity.this)) {
            connection.buildDialog(SubjectSyllabusActivity.this).show();
        } else {
            init();
            allEvents();

            bundle = null;
            bundle = this.getIntent().getExtras();
            //syllabusList = bundle.getString("subjectSyllabus");
            //strStudentId = bundle.getString("SubjectId");
            strSubject = bundle.getString(getString(R.string.key_Subject));
            strSubjectId = bundle.getString(getString(R.string.key_SubjectId));
            strClassId = bundle.getString(getString(R.string.key_ClassId));
            strClass = bundle.getString("Class");
            if (bundle != null) {
                new GetSyllabusList().execute();
            } else {
                Toast.makeText(SubjectSyllabusActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
            }

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            tvSyllabusSubject.setText(strSubject);
            tvSyllabusClass.setText(strClass);
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
    }

    private void init() {
        session = new Session(getApplicationContext());
        tvSyllabusClass = (TextView) findViewById(R.id.tvSyllabusClass);
        tvSyllabusSubject = (TextView) findViewById(R.id.tvSyllabusSubject);

        recyclerViewSyllabus = (RecyclerView) findViewById(R.id.recyclerViewSyllabus);
        
    }

    private class GetSyllabusList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String studentId;
        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SubjectSyllabusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Syllabus List...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            //studentId = "5";

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffclasswiseSubjectSyllabus));
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_ClassId), strClassId);
                otherData.put(getString(R.string.key_SubjectId), strSubjectId);
                outObject.put(getString(R.string.key_otherData), otherData);
                Log.e("TAG", "GetSyllabusList, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetSyllabusList, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                JSONArray result = inObject.getJSONArray("Result");
                String strStatus=inObject.getString(getString(R.string.key_status));
                if(strStatus.equalsIgnoreCase("Success")){
                    publishProgress(result);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            SyllabusAdapter recyclerAdapter = new SyllabusAdapter(values[0], getApplicationContext(),SubjectSyllabusActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewSyllabus.setLayoutManager(layoutManager);
            recyclerViewSyllabus.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSyllabus.setAdapter(recyclerAdapter);
            /*try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String syllabus = jsonObject.getString(getString(R.string.key_Subject));
                String subject = jsonObject.getString(getString(R.string.key_Class));
                String strSyllabus = jsonObject.getString(getString(R.string.key_Syllabus));
                tvSyllabusSubject.append("\n"+syllabus);
                tvSubject.append("\n"+subject);
                tvSyllabusSubject.append("\n"+strSyllabus);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
