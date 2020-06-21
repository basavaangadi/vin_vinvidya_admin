package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SubjectRecyclerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SyllabusActivity extends AppCompatActivity {
    Spinner spnrSyllabusClass, spnrSyllabusSection;
    RecyclerView syllabusRecyclerView;
    Button btnGetSyllabus;
    private Session session;
    TextView tmpViewSection;
    public String strStaffId, strExam, strSchoolId, strClass, strClassId, strSubjectId;
    String strClassSection, strAcademicYearId;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.syllabusToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.key_Syllabus);
        if (!connection.netInfo(SyllabusActivity.this)) {
            connection.buildDialog(SyllabusActivity.this).show();
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
        spnrSyllabusClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSyllabusClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                new GetSection().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSyllabusSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tmpViewSection = (TextView) spnrSyllabusSection.getSelectedView().findViewById(R.id.list);
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

        btnGetSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetSyllabus().execute();
            }
        });
    }

    public void init() {
        session = new Session(getApplicationContext());
        spnrSyllabusClass = (Spinner) findViewById(R.id.spnrSyllabusClass);
        spnrSyllabusSection = (Spinner) findViewById(R.id.spnrSyllabusSection);
        syllabusRecyclerView = (RecyclerView) findViewById(R.id.syllabusRecyclerView);
        btnGetSyllabus = (Button) findViewById(R.id.btnGetSyllabus);
    }

    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SyllabusActivity.this);
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
            spnrSyllabusClass.setPrompt("Choose Class");
            spnrSyllabusClass.setAdapter(adapter);
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
            spnrSyllabusSection.setPrompt("Choose Section");
            spnrSyllabusSection.setAdapter(adapter);
        }
    }

    private class GetSyllabus extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;
        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SyllabusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Subjects...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffclasswiseSubject));
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_otherData), otherData);
                Log.e("TAG", "GetSyllabus, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetSyllabus, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                JSONArray result = inObject.getJSONArray("Result");
                publishProgress(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

            SubjectRecyclerAdapter recyclerAdapter = new SubjectRecyclerAdapter(values[0], getApplicationContext(),strClassSection);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            syllabusRecyclerView.setLayoutManager(layoutManager);
            syllabusRecyclerView.setItemAnimator(new DefaultItemAnimator());
            syllabusRecyclerView.setAdapter(recyclerAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
