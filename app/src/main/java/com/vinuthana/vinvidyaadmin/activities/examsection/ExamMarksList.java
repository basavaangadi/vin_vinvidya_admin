package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ResultAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ResultAdapterGDGB;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExamMarksList extends AppCompatActivity {
    String strClassId, strSubjectId, strExamId, strSceduleDate,strSchoolId;
    RecyclerView recyclerViewSubRslt;
    CheckConnection connection = new CheckConnection();
    JSONArray exmArray;
    TextView tvEmresCls,tvEmressub,tvEmResMxMrks,tvEmresMinMarks;
    LinearLayout lynrlytStudentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_marks_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Marks List");
        initialisation();

        if (!connection.netInfo(ExamMarksList.this)) {
            connection.buildDialog(ExamMarksList.this).show();
        } else {
            try {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle = intent.getExtras();
                String bunData=bundle.toString();
                Log.e("oncreate",bunData);
                strSchoolId = bundle.getString(getString(R.string.key_SchoolId));
                Log.e("SchoolId",strSchoolId);
                strClassId = bundle.getString("ClassId");
                Log.e("ClassId",strClassId);
                strExamId = bundle.getString("ExamId");
                Log.e("ExamId",strExamId);
                strSubjectId = bundle.getString("SubjectId");
                Log.e("SubjectId",strSubjectId);
                strSceduleDate = bundle.getString("ExamSchduleSetDate");
                Log.e("ExamSchduleSetDate",strSceduleDate);
                new GetSubjectwiseResult().execute();
            }catch (Exception e){
                Toast.makeText(ExamMarksList.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("examMarksList ",e.toString());
            }
        }


    }
    public void initialisation(){
        tvEmresCls=findViewById(R.id.tvEmresCls);
        tvEmressub=findViewById(R.id.tvEmressub);
        tvEmResMxMrks=findViewById(R.id.tvEmResMxMrks);
        tvEmresMinMarks=findViewById(R.id.tvEmresMinMarks);
        recyclerViewSubRslt = (RecyclerView) findViewById(R.id.recyclerViewSubRslt);
        lynrlytStudentDetails =  findViewById(R.id.lynrlytStudentDetails);


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

    class GetSubjectwiseResult extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ExamMarksList.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                if(strSchoolId.equalsIgnoreCase("2")||strSchoolId.equalsIgnoreCase("13")) {
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getSubjectwiseResultGDGB));
                }else {
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getSubjectwiseResultRVK));
                }
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_SubjectId), strSubjectId);
                examData.put(getString(R.string.key_ExamSchduleDate), strSceduleDate);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    ExamMarksList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExamMarksList.this);
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
                Log.e("DoinBGKMARKS", ex.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            exmArray=values[0];
            if(strSchoolId.equalsIgnoreCase("2")||strSchoolId.equalsIgnoreCase("13")){
                ResultAdapterGDGB resultAdapterGDGB= new ResultAdapterGDGB(values[0], ExamMarksList.this);
                RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(ExamMarksList.this);
                recyclerViewSubRslt.setLayoutManager(prntNtLytMngr);
                recyclerViewSubRslt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewSubRslt.setAdapter(resultAdapterGDGB);
            }else{
                ResultAdapter recyclerViewAdapter = new ResultAdapter(values[0], strSchoolId,ExamMarksList.this);
                RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(ExamMarksList.this);
                recyclerViewSubRslt.setLayoutManager(prntNtLytMngr);
                recyclerViewSubRslt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewSubRslt.setAdapter(recyclerViewAdapter);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            try {
                JSONObject object = exmArray.getJSONObject(0);

                tvEmresCls.setText(object.getString("class"));
                tvEmressub.setText(object.getString("Subject"));
                tvEmResMxMrks.setText(object.getString("MaxMarks"));
                tvEmresMinMarks.setText(object.getString("MinMarks"));
                 String strMaxMarks=object.getString("MaxMarks");
                 if(strMaxMarks.length()>0){
                     lynrlytStudentDetails.setVisibility(View.VISIBLE);
                 }

            }catch (Exception e){
                e.toString();
            }
            }
    }
}
