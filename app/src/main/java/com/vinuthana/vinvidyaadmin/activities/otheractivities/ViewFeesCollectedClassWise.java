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
import com.vinuthana.vinvidyaadmin.adapters.ClsswiseFeesRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ViewFeesCollectedClassWise extends AppCompatActivity {
    String strClassId,strFeeType;
    RecyclerView rcvwAdmissionFees;
    //String url="http://192.168.43.155:8080/AdmissionFeesFragment/fees/admissionfees.jsp";
    String url ="http://192.168.43.155:8080/netvinvidyawebapi/operation/otherOperation.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fees_collected_class_wise);
        rcvwAdmissionFees= findViewById(R.id.rcvwAdmissionFees);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View Fees Collected");
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        strClassId = bundle.getString("ClassId");
        strFeeType = bundle.getString("Admission Fees");

        new GetPaidAndBalanceClassIdWise().execute();
    }
    class GetPaidAndBalanceClassIdWise  extends AsyncTask<String, JSONArray,Void> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(ViewFeesCollectedClassWise.this);
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
                outObject.put("OperationName","GetAdmissionFeesClassIdWise");
                JSONObject otherData= new JSONObject();
                otherData.put("ClassId",strClassId);
                outObject.put("otherData",otherData);
                Log.e("outObject is ",outObject.toString());
                String strRespText=response.getServerResopnse(url,outObject.toString());
                Log.e("Response is",strRespText);
                JSONObject inObject=new JSONObject(strRespText);
                String strStatus=inObject.getString("Status");
                if(strStatus.equalsIgnoreCase("Success")){
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                }else{
                    ViewFeesCollectedClassWise.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast=Toast.makeText(ViewFeesCollectedClassWise.this,"Data not found",Toast.LENGTH_LONG);
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
            ClsswiseFeesRecyclerViewAdapter mAdapter =new ClsswiseFeesRecyclerViewAdapter(values[0], ViewFeesCollectedClassWise.this, strFeeType);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(ViewFeesCollectedClassWise.this);
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
