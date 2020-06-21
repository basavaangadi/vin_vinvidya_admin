package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.extraactivities.MainActivity;
import com.vinuthana.vinvidyaadmin.adapters.HwrkFbRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HmWkFeedBackActivity extends AppCompatActivity {

    TextView tvHmWkFbSubject, tvHmWkFbClass, tvHmWkFbChptrName, tvHmWkFbDate, tvHmWkDate, tvHmWkFbHmWork;
    RecyclerView recyclerViewCurHmWrkFb;
    private ProgressDialog progressDialog;
    private Session session;
    LinearLayout lynrLytFedBack;
    String strStaffId, strDate, strSchoolId, strAcademicYearId, strReason, strClassId, strStatus, strSubjectId, strMsg;
    JSONArray jsonArray = new JSONArray();
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hm_wk_feed_back);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_home_work_feedback));
        if (!connection.netInfo(HmWkFeedBackActivity.this)) {
            connection.buildDialog(HmWkFeedBackActivity.this).show();
        } else {
            init();
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_STAFFDETAILS_ID);
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle = intent.getExtras();
            strClassId = bundle.getString("strClass");
            strSubjectId = bundle.getString("strSubject");

            new GetCurHomeWorkFb().execute();

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
        session = new Session(HmWkFeedBackActivity.this);
        recyclerViewCurHmWrkFb = findViewById(R.id.recyclerViewCurHmWrkFb);
        tvHmWkFbSubject = findViewById(R.id.tvHmWkFbSubject);
        tvHmWkFbClass = findViewById(R.id.tvHmWkFbClass);
        tvHmWkFbChptrName = findViewById(R.id.tvHmWkFbChptrName);
        lynrLytFedBack = findViewById(R.id.lynrLytFedBack);
        tvHmWkFbDate = findViewById(R.id.tvHmWkFbDate);
        tvHmWkDate = findViewById(R.id.tvHmWkDate);
        tvHmWkFbHmWork = findViewById(R.id.tvHmWkFbHmWork);
    }

    class GetCurHomeWorkFb extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        JSONObject jsonObject;
        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HmWkFeedBackActivity.this);
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffhomeworkFeedback));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_ClassId), strClassId);
                homeworkData.put(getString(R.string.key_SubjectId), strSubjectId);
                outObject.put(getString(R.string.key_homeworkData), homeworkData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                strStatus= inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HmWkFeedBackActivity.this);
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
                Log.e("doinBackGroud", ex.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            HwrkFbRecyclerViewAdapter recyclerAdapter = new HwrkFbRecyclerViewAdapter(values[0], HmWkFeedBackActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HmWkFeedBackActivity.this);
            recyclerViewCurHmWrkFb.setLayoutManager(layoutManager);
            recyclerViewCurHmWrkFb.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurHmWrkFb.setAdapter(recyclerAdapter);
            try {
                jsonObject = values[0].getJSONObject(0);

            } catch (Exception e) {
                Log.e("pogressUpdate",e.toString());

                Toast.makeText(HmWkFeedBackActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if(!strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
                    lynrLytFedBack.setVisibility(View.GONE);

                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){

                        Toast.makeText(HmWkFeedBackActivity.this, "feedback not found", Toast.LENGTH_SHORT).show();
                        //update ui here
                        // display toast here
                    }
                });
                }
                else if(jsonObject.length()>1) {
                    String strChapterName = jsonObject.getString(getString(R.string.key_chapterName));
                    String strFeedbackDate = jsonObject.getString(getString(R.string.key_FeedbackDate));
                    String strHomeworkDate = jsonObject.getString(getString(R.string.key_homeworkDate));
                    String strSubject = jsonObject.getString(getString(R.string.key_Subject));
                    String strClass = jsonObject.getString(getString(R.string.key_Clas));
                    String strHomeWork = jsonObject.getString(getString(R.string.key_Homework));
                    if (strHomeWork.length() > 1) {
                        tvHmWkFbChptrName.setText(strChapterName);
                        tvHmWkFbSubject.setText(strSubject);
                        tvHmWkFbDate.setText(strFeedbackDate);
                        tvHmWkDate.setText(strHomeworkDate);
                        tvHmWkFbClass.setText(strClass);
                        tvHmWkFbHmWork.setText(strHomeWork);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }
}
