package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.AtndRecyclerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.DayToDayAdpater.LeaveRequestAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PeriodWiseAttendanceShowAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReportRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReportRecyclerViewAdapterBelowKitKat;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewLeaveRequestFragment extends Fragment {
    DatePickerDialog datePickerDialog;
    Calendar viewHmFbByDt = Calendar.getInstance();
    private int mYear, mMonth, mDay,roleId;
    Button btnClasswiseLeaveRequest;
    Spinner spnrClasswiseLeaveRequestClass, spnrClasswiseLeaveRequestSection;
    EditText edtClasswiseLeaveRequestDate;
    LeaveRequestAdapter recyclerAdapter;
    private Session session;
    View view;
    RecyclerView recyclerClasswiseLeaveRequest;
    String strStaffId, strDayId, strSchoolId, strClass, strSection, strSubjectId;
    String  strHomeworkId,strClassId, strAcademicYearId,strDate,goal,strReportId;
    ProgressDialog progressDialog;
    /*ReportRecyclerViewAdapter recyclerAdapter;
    ReportRecyclerViewAdapterBelowKitKat recyclerAdapterBelowKitKat;*/
    JSONArray array;
    int dayId;
    boolean isFragmentLoaded = false;
    CheckConnection connection = new CheckConnection();
    ArrayList<String> classArrayList= new ArrayList<String>();
    ArrayList<String>sectionArrayList= new ArrayList<String>();
    ArrayList<String>subjectArrayList= new ArrayList<String>();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtClasswiseLeaveRequestDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };


    public ViewLeaveRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_leave_request, container, false);
    }
    public void init() {

        btnClasswiseLeaveRequest = (Button) getActivity().findViewById(R.id.btnClasswiseLeaveRequest);
        edtClasswiseLeaveRequestDate = (EditText) getActivity().findViewById(R.id.edtClasswiseLeaveRequestDate);
        spnrClasswiseLeaveRequestClass = (Spinner) getActivity().findViewById(R.id.spnrClasswiseLeaveRequestClass);
        spnrClasswiseLeaveRequestSection = (Spinner) getActivity().findViewById(R.id.spnrClasswiseLeaveRequestSection);
        recyclerClasswiseLeaveRequest = (RecyclerView) getActivity().findViewById(R.id.recyclerClasswiseLeaveRequest);
        array = new JSONArray();
        initRecylerViewAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            session = new Session(getContext());
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strStaffId=user.get(Session.KEY_STAFFDETAILS_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            mYear = viewHmFbByDt.get(Calendar.YEAR);
            mMonth = viewHmFbByDt.get(Calendar.MONTH);
            mDay = viewHmFbByDt.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
            init();
            if (getActivity() != null) {

                    new GetClass().execute();

                }
            allEvents();
            //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser) {
                new GetClass().execute();
               isFragmentLoaded = true;
            }
        }
    }
     public void initRecylerViewAdapter(){
         recyclerAdapter = new LeaveRequestAdapter(array, getActivity());
         RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
         recyclerClasswiseLeaveRequest.setLayoutManager(layoutManager);
         recyclerClasswiseLeaveRequest.setItemAnimator(new DefaultItemAnimator());
         recyclerClasswiseLeaveRequest.setAdapter(recyclerAdapter);
     }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void allEvents() {
        edtClasswiseLeaveRequestDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtClasswiseLeaveRequestDate) {
                    datePickerDialog.show();

                    return true;
                }
                return false;
            }
        });


        btnClasswiseLeaveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clsPos= spnrClasswiseLeaveRequestClass.getSelectedItemPosition();
                int secPos= spnrClasswiseLeaveRequestSection.getSelectedItemPosition();
                if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)){
                    Toast toast = Toast.makeText(getActivity(),"Select Class and Section",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=array.length();i>=0;i--){

                            array.remove(i);
                        }

                            recyclerAdapter.notifyDataSetChanged();
                        }
                    else{
                        array= new JSONArray();
                        initRecylerViewAdapter();
                    }

                }
                else if (edtClasswiseLeaveRequestDate.getText().toString().length()<3) {
                    Toast toast = Toast.makeText(getActivity(),"Date can't be blank",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=array.length();i>=0;i--){

                            array.remove(i);
                        }

                        recyclerAdapter.notifyDataSetChanged();
                    }
                    else{
                        array= new JSONArray();
                        initRecylerViewAdapter();
                    }



                }  else{
                        strDate = edtClasswiseLeaveRequestDate.getText().toString();

                        new GetClasswiseLeave().execute();
                    }
                }

        });

        spnrClasswiseLeaveRequestClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrClasswiseLeaveRequestClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrClasswiseLeaveRequestSection.setSelection(0);
                    spnrClasswiseLeaveRequestSection.setAdapter(null);
                }


                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                //new GetSection().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrClasswiseLeaveRequestSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrClasswiseLeaveRequestSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    class GetClasswiseLeave extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        JSONObject outObject= new JSONObject();
        GetResponse response = new GetResponse();
        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Current Attendance...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for(int i=array.length();i>=0;i--){

                    array.remove(i);
                }
            }else{
                array= new JSONArray();
            }
        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseStudentLeaveList));
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_ClassId), strClassId);
                studAttendanceData.put(getString(R.string.key_Date),strDate);
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray resultArray= new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < resultArray.length(); i++) {
                        array.put(resultArray.getJSONObject(i));
                    }
                 //   publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
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
                                    spnrClasswiseLeaveRequestClass.setSelection(0);
                                    spnrClasswiseLeaveRequestSection.setSelection(0);
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                recyclerAdapter.notifyDataSetChanged();

            }else{
                initRecylerViewAdapter();
            }

        }
    }


    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
                                    spnrClasswiseLeaveRequestSection.setSelection(-1);

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
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrClasswiseLeaveRequestClass.setPrompt("Choose Class");
            spnrClasswiseLeaveRequestClass.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetSection extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Plase wait fetching Sections...");
            progressDialog.show();
        }
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
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
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
                                    spnrClasswiseLeaveRequestSection.setSelection(0);
                                    spnrClasswiseLeaveRequestClass.setSelection(0);
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
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], getActivity());
            spnrClasswiseLeaveRequestSection.setPrompt("Choose Section");
            spnrClasswiseLeaveRequestSection.setAdapter(adapter);
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

                /*if (alertTitle.equalsIgnoreCase(getString(R.string.key_Success))) {

                    getActivity().onBackPressed();

                }*/
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
