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
import android.widget.Button;
import android.widget.Spinner;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.EventsRecyclerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class EventsActivity extends AppCompatActivity {

    Spinner spnrEventsClass, spnrEventsSection;
    Button btnGetEvetns;
    RecyclerView recyclerViewEvents;
    private Session session;
    String strStaffId, strAcademicYearId, strSchoolId, strClass, strSection, strClassId;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.eventsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Events");
        if (!connection.netInfo(EventsActivity.this)) {
            connection.buildDialog(EventsActivity.this).show();
        } else {
            init();

            HashMap<String, String> user = session.getUserDetails();

            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            new GetEventsList().execute();
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

    public void init() {
        session = new Session(getApplicationContext());
        recyclerViewEvents = (RecyclerView) findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents = (RecyclerView) findViewById(R.id.recyclerViewEvents);
        /*spnrEventsClass = (Spinner) findViewById(R.id.spnrEventsClass);
        spnrEventsSection = (Spinner) findViewById(R.id.spnrEventsSection);*/
    }

    private class GetEventsList extends AsyncTask<String, JSONArray, Void> {

        ProgressDialog progressDialog;

        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Events List...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffEventList));
                JSONObject otherData = new JSONObject();
                otherData.put(getString(R.string.key_StaffId), strStaffId);
                otherData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_otherData),otherData);
                Log.e("TAG", "GetEventsList, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetEventsList, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                JSONArray result = inObject.getJSONArray(getString(R.string.key_Result));
                publishProgress(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            EventsRecyclerAdapter recyclerAdapter = new EventsRecyclerAdapter(values[0], EventsActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewEvents.setLayoutManager(layoutManager);
            recyclerViewEvents.setItemAnimator(new DefaultItemAnimator());
            recyclerViewEvents.setAdapter(recyclerAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
