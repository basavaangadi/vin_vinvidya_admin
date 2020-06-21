package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.EditDailyReportActivity;
import com.vinuthana.vinvidyaadmin.adapters.ReportRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReportRecyclerViewAdapterBelowKitKat;
import com.vinuthana.vinvidyaadmin.adapters.StaffSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaffwiseDayReportFragment extends Fragment {

    Spinner spnrStaffwiseDayReportStaffList;
    DatePickerDialog datePickerDialog;
    boolean isFragmentLoaded = false;
    Calendar staffReportDt = Calendar.getInstance();
    Button btnGetStaffwiseDayReport;
    EditText edtStaffwiseDayReportDate;
    private Session session;
    CheckConnection connection = new CheckConnection();
    RecyclerView recyclerStaffwiseDayReport;
    ReportRecyclerViewAdapter reportRecyclerViewAdapter;
    ReportRecyclerViewAdapterBelowKitKat reportRecyclerViewAdapterBelowKitKat;
    JSONArray reportArray = new JSONArray();
    JSONArray staffListArray= new JSONArray();
    String strStaffId, strSchoolId, strClass,strStaff,strReportId;
    String strStaffNameId,strAcademicYearId,strNoticeId,strDate;
    int roleId,staffReportDtYear,staffReportDtMonth,staffReportDtDay;
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtStaffwiseDayReportDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    public StaffwiseDayReportFragment() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser) {
                new GetStaffList().execute();
                isFragmentLoaded = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_staffwise_day_report_by_date, container, false);
        staffReportDtYear = staffReportDt.get(Calendar.YEAR);
        staffReportDtMonth = staffReportDt.get(Calendar.MONTH);
        staffReportDtDay = staffReportDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, staffReportDtYear, staffReportDtMonth, staffReportDtDay);
        return view;
    }
    public void init() {

        recyclerStaffwiseDayReport = (RecyclerView) getActivity().findViewById(R.id.recyclerStaffwiseDayReport);
        btnGetStaffwiseDayReport = (Button) getActivity().findViewById(R.id.btnGetStaffwiseDayReport);
        edtStaffwiseDayReportDate = (EditText) getActivity().findViewById(R.id.edtStaffwiseDayReportDate);
        spnrStaffwiseDayReportStaffList = (Spinner) getActivity().findViewById(R.id.spnrStaffwiseDayReportStaffList);







        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            reportRecyclerViewAdapter = new ReportRecyclerViewAdapter(reportArray, getActivity(), roleId);
            reportRecyclerViewAdapter.setOnButtonClickListener(new ReportRecyclerViewAdapter.OnReportClickListener() {
                @Override
                public void onEdit(JSONObject currentData, int position, String reportId) {
                    Log.e("Staffwise report", "edit clicked for " + position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want edit this Report");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = new Bundle();

                            bundle.putString("CurrentData", currentData.toString());
                            bundle.putString("StaffId", strStaffId);
                            Intent intent = new Intent(getActivity(), EditDailyReportActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog = builder.create();
                    dailog.show();
                }

                @Override
                public void onDelete(String reportId, int position) {
                    Log.e("Staffwise report", "Delete clicked for " + position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want Delete this Report");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            strReportId = reportId;
                            new DeleteStaffReport().execute();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog = builder.create();
                    dailog.show();
                }
            });

            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerStaffwiseDayReport.setLayoutManager(prntNtLytMngr);
            recyclerStaffwiseDayReport.setItemAnimator(new DefaultItemAnimator());
            recyclerStaffwiseDayReport.setAdapter(reportRecyclerViewAdapter);
        }else{
            initRecylerviewAdapter();

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            //new GetTeacherNoteByDate().execute();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            strAcademicYearId=user.get(Session.KEY_ACADEMIC_YEAR_ID);
            init();
            if (getActivity() != null) {

                new GetStaffList().execute();

            }

            allEvents();
        }
    }
    public void initRecylerviewAdapter(){
        reportRecyclerViewAdapterBelowKitKat = new ReportRecyclerViewAdapterBelowKitKat(reportArray, getActivity(), roleId);
        reportRecyclerViewAdapterBelowKitKat.setOnButtonClickListener(new ReportRecyclerViewAdapterBelowKitKat.OnReportClickListener() {
            @Override
            public void onEdit(JSONObject currentData, int position, String reportId) {
                Log.e("Staffwise report", "edit clicked for " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want edit this Report");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();

                        bundle.putString("CurrentData", currentData.toString());
                        bundle.putString("StaffId", strStaffId);
                        Intent intent = new Intent(getActivity(), EditDailyReportActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dailog = builder.create();
                dailog.show();
            }

            @Override
            public void onDelete(String reportId, int position) {
                Log.e("Staffwise report", "Delete clicked for " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want Delete this Report");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strReportId = reportId;
                        new DeleteStaffReport().execute();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dailog = builder.create();
                dailog.show();
            }
        });

        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        recyclerStaffwiseDayReport.setLayoutManager(prntNtLytMngr);
        recyclerStaffwiseDayReport.setItemAnimator(new DefaultItemAnimator());
        recyclerStaffwiseDayReport.setAdapter(reportRecyclerViewAdapterBelowKitKat);
    }
public void allEvents(){
        edtStaffwiseDayReportDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId()==R.id.edtStaffwiseDayReportDate){
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });
        edtStaffwiseDayReportDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                strDate=edtStaffwiseDayReportDate.getText().toString();
            }
        });
        btnGetStaffwiseDayReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               int stfPos= spnrStaffwiseDayReportStaffList.getSelectedItemPosition();

               if(stfPos==-1||stfPos==0){
                   Toast toast=Toast.makeText(getActivity(), "Select Staff", Toast.LENGTH_SHORT);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                       for(int i=reportArray.length();i>0;i--){

                           reportArray.remove(i);
                       }
                       reportRecyclerViewAdapter.notifyDataSetChanged();
                   }
                   else{
                       reportArray= new JSONArray();
                       initRecylerviewAdapter();
                   }
               }else if(edtStaffwiseDayReportDate.getText().length()<3){
                   Toast toast=Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                       for(int i=reportArray.length();i>0;i--){

                           reportArray.remove(i);
                       }
                       reportRecyclerViewAdapter.notifyDataSetChanged();
                   }
                   else{
                       reportArray= new JSONArray();
                       initRecylerviewAdapter();
                   }

               }else{new GetStaffwiseDayReport().execute();}
            }
        });
    spnrStaffwiseDayReportStaffList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            TextView tvStaffId = (TextView) spnrStaffwiseDayReportStaffList.getSelectedView().findViewById(R.id.stafId);
            TextView tvStaffName = (TextView) spnrStaffwiseDayReportStaffList.getSelectedView().findViewById(R.id.stafName);
            tvStaffId.setTextColor(Color.WHITE);
            tvStaffName.setTextColor(Color.WHITE);
            strStaffNameId = tvStaffId.getText().toString() + tvStaffName.getText().toString();
            strStaffId = adapterView.getItemAtPosition(i).toString();
            strStaff = tvStaffId.getText().toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    });
}
    class GetStaffwiseDayReport extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "reportingOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Home Work...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
             for(int i=reportArray.length();i>=0;i--){

                        reportArray.remove(i);
                    }
            }else{
                reportArray= new JSONArray();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffwiseStaffReport));
                JSONObject reportingData = new JSONObject();
                reportingData.put(getString(R.string.key_StaffId), strStaffId);

                reportingData.put(getString(R.string.key_Date), strDate);
                outObject.put(getString(R.string.key_reportingData), reportingData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray result = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < result.length(); i++) {
                        reportArray.put(result.getJSONObject(i));
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrStaffwiseDayReportStaffList.setSelection(0);
                                }
                            });
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

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                reportRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                initRecylerviewAdapter();
            }

            progressDialog.dismiss();

        }
    }



    class DeleteHomework extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Deleting the Home Work...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteHomework));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_HomeworkId), strNoticeId);
                outObject.put(getString(R.string.key_homeworkData), homeworkData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMsg=inObject.getString(getString(R.string.key_Message));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (strMsg.equalsIgnoreCase("Homework deleted sucessfully..")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equalsIgnoreCase("Homework couldn't be deleted..")) {
                            alertMessage = strMsg;
                            alertTitle = "Failure";
                        } else {
                            alertTitle = "Error";
                            alertMessage = "Something went wrong While editing Home Work..";
                        }
                        showAlert(alertMessage, alertTitle);
                    }
                });



            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(alertTitle.equalsIgnoreCase(getString(R.string.key_Success))){
                        getActivity().onBackPressed();
                    }
                }


        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    class GetStaffList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffListBySchoolId));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        staffListArray.put(resultArray.getJSONObject(i));
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrStaffwiseDayReportStaffList.setSelection(0);
                                }
                            });
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
            StaffSpinnerAdapter adapter = new StaffSpinnerAdapter(values[0], getActivity());
            spnrStaffwiseDayReportStaffList.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class DeleteStaffReport extends AsyncTask<String, JSONArray, Void>{
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "reportingOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Deleting the Staff report...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteStaffReport));
                JSONObject reportingData = new JSONObject();
                reportingData.put(getString(R.string.key_Id), strReportId);
                outObject.put(getString(R.string.key_reportingData), reportingData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMsg=inObject.getString(getString(R.string.key_Message));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (strStatus.equalsIgnoreCase("Success")||strStatus.equalsIgnoreCase("Fail")) {
                            alertMessage = strMsg;
                            alertTitle = strStatus;
                        }  else {
                            alertTitle = "Error";
                            alertMessage = "Something went wrong While deleting the report..";
                        }
                        showAlert(alertMessage, alertTitle);
                    }
                });



            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                reportRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                reportRecyclerViewAdapterBelowKitKat.notifyDataSetChanged();
            }

        }
    }
   /* private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/

}
