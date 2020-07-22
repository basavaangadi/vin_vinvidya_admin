package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StudwiseFeesRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ViewFeesCollectedStudWise extends AppCompatActivity {
    RecyclerView rcvwAdmissionFees;
    String strFeeType,strStudentId;
    String url="http://192.168.43.155:8080/AdmissionFeesFragment/fees/admissionfees.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fees_collected_stud_wise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View Fees Collected");

        rcvwAdmissionFees=findViewById(R.id.rcvwAdmissionFees);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        strStudentId = bundle.getString("StudentId");
        strFeeType = bundle.getString("Admission Fees");

        new GetAdmissionFeesStudWise().execute();

    }
    class GetAdmissionFeesStudWise  extends AsyncTask<String, JSONArray,Void> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(ViewFeesCollectedStudWise.this);
            progressDialog.setMessage("Fetching Admission Fees StudWise...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response= new GetResponse();
            JSONObject outObject= new JSONObject();

            try {
                outObject.put("OperationName","GetAdmissionFeesStudWise");
                JSONObject userData= new JSONObject();
                userData.put("StudentId",strStudentId);
                outObject.put("userData",userData);
                Log.e("outObject is ",outObject.toString());
                String strRespText=response.getServerResopnse(url,outObject.toString());
                Log.e("Response is",strRespText);
                JSONObject inObject=new JSONObject(strRespText);
                String strStatus=inObject.getString("Status");
                if(strStatus.equalsIgnoreCase("Success")){
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                }else{
                    ViewFeesCollectedStudWise.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast=Toast.makeText(ViewFeesCollectedStudWise.this,"Data not found",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            StudwiseFeesRecyclerViewAdapter mAdapter =new StudwiseFeesRecyclerViewAdapter(values[0],ViewFeesCollectedStudWise.this, strFeeType);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(ViewFeesCollectedStudWise.this);
            rcvwAdmissionFees.setLayoutManager(mLayoutManager);
            rcvwAdmissionFees.setItemAnimator(new DefaultItemAnimator());
            rcvwAdmissionFees.setAdapter(mAdapter);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
    }


}
