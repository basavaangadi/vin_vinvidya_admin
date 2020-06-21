package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.EditDailyReportActivity;
import com.vinuthana.vinvidyaadmin.adapters.ReportRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReportRecyclerViewAdapterBelowKitKat;
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
public class MyDayReportFragment extends Fragment {


    DatePickerDialog datePickerDialog;
    boolean isFragmentLoaded = false;
    Calendar staffReportDt = Calendar.getInstance();
    Button btnGetMyDayReport;
    EditText edtMyDayReportDate;
    private Session session;
    CheckConnection connection = new CheckConnection();
    RecyclerView recyclerMyDayReport;
    ReportRecyclerViewAdapter reportRecyclerViewAdapter;
    ReportRecyclerViewAdapterBelowKitKat reportRecyclerViewAdapterBelowKitKat;
    JSONArray reportArray = new JSONArray();
    String strStaffId,strDate,strSchoolId,strAcademicYearId,strReportId;
    int roleId,staffReportDtYear,staffReportDtMonth,staffReportDtDay;
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtMyDayReportDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    public MyDayReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        staffReportDtYear = staffReportDt.get(Calendar.YEAR);
        staffReportDtMonth = staffReportDt.get(Calendar.MONTH);
        staffReportDtDay = staffReportDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, staffReportDtYear, staffReportDtMonth, staffReportDtDay);
        View view=inflater.inflate(R.layout.fragment_my_day_report_by_date, container, false);
    return view;
    }
    public void init() {

        recyclerMyDayReport = (RecyclerView) getActivity().findViewById(R.id.recyclerMyDayReport);
        btnGetMyDayReport = (Button) getActivity().findViewById(R.id.btnGetMyDayReport);
        edtMyDayReportDate = (EditText) getActivity().findViewById(R.id.edtMyDayReportDate);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            reportRecyclerViewAdapter= new ReportRecyclerViewAdapter(reportArray, getActivity(),1);
            reportRecyclerViewAdapter.setOnButtonClickListener(new ReportRecyclerViewAdapter.OnReportClickListener() {
                @Override
                public void onEdit(JSONObject currentData, int position, String reportId) {
                    Log.e("Staffwise report","edit clicked for "+position);
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want edit this Report");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle= new Bundle();

                            bundle.putString("CurrentData",currentData.toString());
                            bundle.putString("StaffId",strStaffId);
                            Intent intent= new Intent(getActivity(), EditDailyReportActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog=builder.create();
                    dailog.show();
                }
                @Override
                public void onDelete(String reportId, int position) {
                    Log.e("Staffwise report","Delete clicked for "+position);
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want Delete this Report");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            strReportId=reportId;
                            new DeleteStaffReport().execute();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog=builder.create();
                    dailog.show();
                }
            });

            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerMyDayReport.setLayoutManager(prntNtLytMngr);
            recyclerMyDayReport.setItemAnimator(new DefaultItemAnimator());
            recyclerMyDayReport.setAdapter(reportRecyclerViewAdapter);

        }else{

            reportRecyclerViewAdapterBelowKitKat= new ReportRecyclerViewAdapterBelowKitKat(reportArray, getActivity(),1);
            reportRecyclerViewAdapterBelowKitKat.setOnButtonClickListener(new ReportRecyclerViewAdapterBelowKitKat.OnReportClickListener() {
                @Override
                public void onEdit(JSONObject currentData, int position, String reportId) {
                    Log.e("Staffwise report","edit clicked for "+position);
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want edit this Report");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle= new Bundle();

                            bundle.putString("CurrentData",currentData.toString());
                            bundle.putString("StaffId",strStaffId);
                            Intent intent= new Intent(getActivity(), EditDailyReportActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog=builder.create();
                    dailog.show();
                }
                @Override
                public void onDelete(String reportId, int position) {
                    Log.e("Staffwise report","Delete clicked for "+position);
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want Delete this Report");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            strReportId=reportId;
                            new DeleteStaffReport().execute();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog=builder.create();
                    dailog.show();
                }
            });

            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerMyDayReport.setLayoutManager(prntNtLytMngr);
            recyclerMyDayReport.setItemAnimator(new DefaultItemAnimator());
            recyclerMyDayReport.setAdapter(reportRecyclerViewAdapterBelowKitKat);
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
            allEvents();
        }
    }
    public void allEvents(){
        edtMyDayReportDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId()==R.id.edtMyDayReportDate){
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        btnGetMyDayReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(strDate.length()<3){
                    Toast toast=Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{
                new GetStaffwiseDayReport().execute();
                }
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
                strDate=edtMyDayReportDate.getText().toString();
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
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                reportRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                reportRecyclerViewAdapterBelowKitKat= new ReportRecyclerViewAdapterBelowKitKat(reportArray, getActivity(),1);
                reportRecyclerViewAdapterBelowKitKat.setOnButtonClickListener(new ReportRecyclerViewAdapterBelowKitKat.OnReportClickListener() {
                    @Override
                    public void onEdit(JSONObject currentData, int position, String reportId) {
                        Log.e("Staffwise report","edit clicked for "+position);
                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want edit this Report");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle= new Bundle();

                                bundle.putString("CurrentData",currentData.toString());
                                bundle.putString("StaffId",strStaffId);
                                Intent intent= new Intent(getActivity(), EditDailyReportActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dailog=builder.create();
                        dailog.show();
                    }
                    @Override
                    public void onDelete(String reportId, int position) {
                        Log.e("Staffwise report","Delete clicked for "+position);
                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want Delete this Report");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                strReportId=reportId;
                                new DeleteStaffReport().execute();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dailog=builder.create();
                        dailog.show();
                    }
                });

                RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
                recyclerMyDayReport.setLayoutManager(prntNtLytMngr);
                recyclerMyDayReport.setItemAnimator(new DefaultItemAnimator());
                recyclerMyDayReport.setAdapter(reportRecyclerViewAdapterBelowKitKat);
            }



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
            //recyclerAdapter.notifyDataSetChanged();
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                reportRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                reportRecyclerViewAdapterBelowKitKat.notifyDataSetChanged();
            }


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
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
