package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;


public class GiveHmWrkFeedBack extends AppCompatActivity {
    EditText edtGiveHmWrkFbByDate;
    DatePickerDialog datePickerDialog;
    Calendar hmFbByDt = Calendar.getInstance();
    private int mYear, mMonth, mDay;
    String strDate;
    private Session session;
    RecyclerView recyclerViewCurHmWrk;
    String strStaffId, strExam, strSchoolId, strClass, strSection;
    Spinner spnrGiveHmWrkFbClass, spnrGiveHmWrkFbSection;
    ListView lstGiveHmWrkFb;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtGiveHmWrkFbByDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_hm_wrk_feed_back);
        Toolbar toolbar = findViewById(R.id.toolbarGivHmWrkFb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Give_Home_Work_feedback));
        if (!connection.netInfo(GiveHmWrkFeedBack.this)) {
            connection.buildDialog(GiveHmWrkFeedBack.this).show();
        } else {
            init();
            allEvents();
            new GetClass().execute();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            mYear = hmFbByDt.get(Calendar.YEAR);
            mMonth = hmFbByDt.get(Calendar.MONTH);
            mDay = hmFbByDt.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getApplicationContext(), mlistner, mYear, mMonth, mDay);
        }
    }

    public void init() {
        session = new Session(getApplicationContext());
        /*edtGiveHmWrkFbByDate = (EditText)findViewById(R.id.edtGiveHmWrkFbByDate);
        spnrGiveHmWrkFbClass = (Spinner) findViewById(R.id.spnrGiveHmWrkFbClass);
        spnrGiveHmWrkFbSection = (Spinner) findViewById(R.id.spnrGiveHmWrkFbSection);
        lstGiveHmWrkFb = (ListView) findViewById(R.id.lstGiveHmWrkFb);*/
    }

    public void allEvents() {
        /*edtGiveHmWrkFbByDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtGiveHmWrkFbByDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });*/

        spnrGiveHmWrkFbClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrGiveHmWrkFbClass.getSelectedView().findViewById(R.id.list);
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

        spnrGiveHmWrkFbSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrGiveHmWrkFbSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSection = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strSection);
                //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                //new GetExamSchedule().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GiveHmWrkFeedBack.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Current Notice...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName", "Staff_ClassNames");
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put("SchoolId", strSchoolId);
                classSubjectData.put("AcademicYearId", "2");
                outObject.put("classSubjectData", classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getApplicationContext());
            spnrGiveHmWrkFbClass.setPrompt("Choose Class");
            spnrGiveHmWrkFbClass.setAdapter(adapter);
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
                outObject.put("OperationName", "Staff_Class_With_Section");
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put("SchoolId", strSchoolId);
                classSubjectData.put("AcademicYearId", "2");
                classSubjectData.put("Class", strClass);
                outObject.put("classSubjectData", classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], getApplicationContext());
            spnrGiveHmWrkFbSection.setPrompt("Choose Section");
            spnrGiveHmWrkFbSection.setAdapter(adapter);
        }
    }

    /*class GiveHomeWorkFeedBack extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Current Home Work...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName", "homeworkDisplayClasswise");
                JSONObject homeworkData = new JSONObject();
                homeworkData.put("ClassId", strClassId);
                homeworkData.put("SubjectId", strSubjectId);
                outObject.put("homeworkData", homeworkData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(responseText).getJSONArray("Result"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
           *//* HmWrkRecyclerViewAdapter recyclerAdapter = new HmWrkRecyclerViewAdapter(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCurHmWrkFb.setLayoutManager(layoutManager);
            recyclerViewCurHmWrkFb.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurHmWrkFb.setAdapter(recyclerAdapter);*//*
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }*/
}
