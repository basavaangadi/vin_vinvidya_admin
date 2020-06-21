package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ClasswiseDayReportFragment extends Fragment {

    DatePickerDialog datePickerDialog;
    Calendar viewHmFbByDt = Calendar.getInstance();
    private int mYear, mMonth, mDay,roleId;
    JSONArray subjectArray;

    Button btnClasswiseDayReport;
    Spinner spnrClasswiseDayReportClass, spnrClasswiseDayReportSection;
    EditText edtClasswiseDayReportDate;

    private Session session;
    View view;
    RecyclerView recyclerClasswiseDayReport;
    String strStaffId, strDayId, strSchoolId, strClass, strSection, strSubjectId;
    String  strHomeworkId,strClassId, strAcademicYearId,strDate,goal,strReportId;
    ProgressDialog progressDialog;
    ReportRecyclerViewAdapter recyclerAdapter;
    ReportRecyclerViewAdapterBelowKitKat recyclerAdapterBelowKitKat;
    JSONArray array;
    int dayId;
    CheckConnection connection = new CheckConnection();
    ArrayList<String> classArrayList= new ArrayList<String>();
    ArrayList<String>sectionArrayList= new ArrayList<String>();
    ArrayList<String>subjectArrayList= new ArrayList<String>();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtClasswiseDayReportDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    public ClasswiseDayReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_classwise_day_report, container, false);

         return view;
    }

    public void init() {

        btnClasswiseDayReport = (Button) getActivity().findViewById(R.id.btnClasswiseDayReport);
        edtClasswiseDayReportDate = (EditText) getActivity().findViewById(R.id.edtClasswiseDayReportDate);
        spnrClasswiseDayReportClass = (Spinner) getActivity().findViewById(R.id.spnrClasswiseDayReportClass);
        spnrClasswiseDayReportSection = (Spinner) getActivity().findViewById(R.id.spnrClasswiseDayReportSection);
        recyclerClasswiseDayReport = (RecyclerView) getActivity().findViewById(R.id.recyclerClasswiseDayReport);
        array = new JSONArray();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

            initRecylerAdpaterAboveKitkat();


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
        //        isFragmentLoaded = true;
            }
        }
    }
    public void initRecylerAdpaterAboveKitkat(){
        recyclerAdapter = new ReportRecyclerViewAdapter(array, getActivity(),roleId);
        recyclerAdapter.setOnButtonClickListener(new ReportRecyclerViewAdapter.OnReportClickListener() {
            @Override
            public void onEdit(JSONObject currentData, int position, String reportId) {
                Log.e("Classwise report","edit clicked for "+position);
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
                Log.e("Classwise report","Delete clicked for "+position);
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerClasswiseDayReport.setLayoutManager(layoutManager);
        recyclerClasswiseDayReport.setItemAnimator(new DefaultItemAnimator());
        recyclerClasswiseDayReport.setAdapter(recyclerAdapter);
    }

    public void initRecylerviewAdapter(){
        recyclerAdapterBelowKitKat = new ReportRecyclerViewAdapterBelowKitKat(array, getActivity(),roleId);
        recyclerAdapterBelowKitKat.setOnButtonClickListener(new ReportRecyclerViewAdapterBelowKitKat.OnReportClickListener() {
            @Override
            public void onEdit(JSONObject currentData, int position, String reportId) {
                Log.e("Classwise report","edit clicked for "+position);
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
                Log.e("Classwise report","Delete clicked for "+position);
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerClasswiseDayReport.setLayoutManager(layoutManager);
        recyclerClasswiseDayReport.setItemAnimator(new DefaultItemAnimator());
        recyclerClasswiseDayReport.setAdapter(recyclerAdapterBelowKitKat);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void allEvents() {
        edtClasswiseDayReportDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtClasswiseDayReportDate) {
                    datePickerDialog.show();

                    return true;
                }
                return false;
            }
        });


        btnClasswiseDayReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clsPos=spnrClasswiseDayReportClass.getSelectedItemPosition();
                int secPos=spnrClasswiseDayReportClass.getSelectedItemPosition();

                if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)){
                    Toast toast= Toast.makeText(getActivity(), "select class and section", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        for(int i=array.length();i>0;i--){

                            array.remove(i);
                        }
                        //recyclerAdapter.notifyDataSetChanged();
                        initRecylerAdpaterAboveKitkat();
                    }
                    else{
                        array= new JSONArray();
                        initRecylerviewAdapter();
                    }
                }
                 else if (edtClasswiseDayReportDate.getText().toString().equals("")) {
                    Toast toast= Toast.makeText(getActivity(), "Date can't be blank", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        for(int i=array.length();i>0;i--){

                            array.remove(i);
                        }
                      //  recyclerAdapter.notifyDataSetChanged();
                         initRecylerAdpaterAboveKitkat();
                    }
                    else{
                        array= new JSONArray();
                        initRecylerviewAdapter();
                    }
                }
                else {
                    strDate = edtClasswiseDayReportDate.getText().toString();

                    new GetClasswiseDayReport().execute();
                }
            }
        });

        spnrClasswiseDayReportClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrClasswiseDayReportClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);



                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrClasswiseDayReportSection.setSelection(0);
                    spnrClasswiseDayReportSection.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrClasswiseDayReportSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrClasswiseDayReportSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                 //Toast.makeText(getActivity(), strClassId + " You Clickedon", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    class GetClasswiseDayReport extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "reportingOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Reports...");
            progressDialog.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for(int i=array.length();i>0;i--){

                array.remove(i);
                    }

            }
            else{
                array= new JSONArray();
            }

           //  recyclerAdapter.notifyDataSetChanged();
            /*array=null;
            array= new JSONArray();*/
            //recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseStaffReport));
                JSONObject reportingData = new JSONObject();
                reportingData.put(getString(R.string.key_ClassId), strClassId);

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
                        array.put(result.getJSONObject(i));
                    }

                    Log.e("Tag", "array is =" + array.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                                recyclerAdapter.notifyDataSetChanged();
                            }else{
                                recyclerAdapterBelowKitKat.notifyDataSetChanged();
                            }*/

                        }
                    });
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
                                    spnrClasswiseDayReportSection.setSelection(0);

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
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                recyclerAdapter.notifyDataSetChanged();
                initRecylerAdpaterAboveKitkat();
            }else{
                initRecylerviewAdapter();
            }


           }
    }

    /*public static List<JSONObject> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }*/


   /* public static JSONArray remove(final int idx, final JSONArray from) {
        final List<JSONObject> objs = asList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final JSONObject obj : objs) {
            ja.put(obj);
        }

        return ja;
    }*/
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
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrClasswiseDayReportClass.setPrompt("Choose Class");
            spnrClasswiseDayReportClass.setAdapter(adapter);
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
        //= new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Sections...");
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
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrClasswiseDayReportClass.setSelection(0);
                                    spnrClasswiseDayReportSection.setSelection(0);
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
            spnrClasswiseDayReportSection.setPrompt("Choose Section");
            spnrClasswiseDayReportSection.setAdapter(adapter);
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
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                recyclerAdapter.notifyDataSetChanged();
            }else{
                recyclerAdapterBelowKitKat.notifyDataSetChanged();
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
