package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.PeriodWiseAttendanceActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PeriodSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PeriodWiseAttendanceShowAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentModel;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPeriodWiseAttendanceFragment extends Fragment {

    Spinner spnrMarkPeriodWiseAttendanceClass,spnrMarkPeriodWiseAttendanceSection,spnrMarkPeriodWiseAttendancePeriod;
    EditText edtPeriodWiseAttendanceDate;
    Button btnMarkPeriodWiseAttendanceStudentList;
    View view;
    int status;
    RecyclerView rvPutPeriodwiseAtnd;
    private Session session;
    private ArrayList<StudentModel> studentModels;
    CheckConnection connection = new CheckConnection();
    JSONObject outObject = new JSONObject();
    GetResponse response = new GetResponse();
    Calendar calendar = Calendar.getInstance();
    String strClass,strStaffId,strSchoolId,strAcademicYearId,strClassId,strPeriod;
    String atndStatus,strAttendanceDate,strDayId,strCurrentDate;
    
    public AddPeriodWiseAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getActivity()!=null){
            if(isVisibleToUser){
               // new GetClass().execute();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_add_period_wise_attendance, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

        new GetClass().execute();
        //strAcademicYearId = "4";
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        strAttendanceDate = df.format(calendar.getTime());
        //String strdaydate = edtWriteDayReportDate.getText().toString();
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = inFormat.parse(strAttendanceDate);
            strCurrentDate = inFormat.format(date);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String day = outFormat.format(date);

            switch (day) {
                case "Monday":
                    strDayId = "1";
                    break;
                case "Tuesday":
                    strDayId = "2";
                    break;
                case "Wednesday":
                    strDayId = "3";
                    break;
                case "Thursday":
                    strDayId = "4";
                    break;
                case "Friday":
                    strDayId = "5";
                    break;
                case "Saturday":
                    strDayId = "6";
                    break;
                default:

                    strDayId = "7";

            }




        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }



        init();
        allEvents();

    }

    public void init(){
        spnrMarkPeriodWiseAttendanceClass =getActivity().findViewById(R.id.spnrMarkPeriodWiseAttendanceClass);
        spnrMarkPeriodWiseAttendanceSection=getActivity().findViewById(R.id.spnrMarkPeriodWiseAttendanceSection);
        spnrMarkPeriodWiseAttendancePeriod=getActivity().findViewById(R.id.spnrMarkPeriodWiseAttendancePeriod);
        edtPeriodWiseAttendanceDate=getActivity().findViewById(R.id.edtPeriodWiseAttendanceDate);
        btnMarkPeriodWiseAttendanceStudentList=getActivity().findViewById(R.id.btnMarkPeriodWiseAttendanceStudentList);
        rvPutPeriodwiseAtnd=getActivity().findViewById(R.id.rvPutPeriodwiseAtnd);
    }
    
    public void allEvents(){
        edtPeriodWiseAttendanceDate.setText(strCurrentDate);
        spnrMarkPeriodWiseAttendanceClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrMarkPeriodWiseAttendanceClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrMarkPeriodWiseAttendanceSection.setSelection(0);
                    spnrMarkPeriodWiseAttendanceSection.setAdapter(null);
                }
                Log.e("Tag", "" + strClass);



                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();

                   /// new GetSection().execute();




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrMarkPeriodWiseAttendanceSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrMarkPeriodWiseAttendanceSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();




                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetPeriod().execute();
                }else{
                    spnrMarkPeriodWiseAttendancePeriod.setSelection(0);
                    spnrMarkPeriodWiseAttendancePeriod.setAdapter(null);
                }
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strClassId + " You Clickedon", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnrMarkPeriodWiseAttendancePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrMarkPeriodWiseAttendancePeriod.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strPeriod = tmpView.getText().toString();
                Log.e("Tag", "" + strPeriod);
                //Toast.makeText(getActivity(), strClassId + " You Clickedon", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnMarkPeriodWiseAttendanceStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clsPos=spnrMarkPeriodWiseAttendanceClass.getSelectedItemPosition();
                int secPos=spnrMarkPeriodWiseAttendanceSection.getSelectedItemPosition();
                int perPos=spnrMarkPeriodWiseAttendancePeriod.getSelectedItemPosition();
                if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)||(perPos==0||perPos==-1)){
                    Toast toast=Toast.makeText(getActivity(),"Select class,section and Period",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{


                new checkStudentAttendance().execute();}
            }
        });
    }
    class checkStudentAttendance extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_checkStudentAttendancePeriodWise));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                classSubjectData.put(getString(R.string.key_Period), strPeriod);
                //classSubjectData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_studAttendanceData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                progressDialog.dismiss();
                atndStatus = inObject.getString(getString(R.string.key_Status));
                status = Integer.parseInt(atndStatus);


                /*if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
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
                }*/
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrMarkPeriodWiseAttendanceClass.setPrompt("Choose Class");
            spnrMarkPeriodWiseAttendanceClass.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (status ==-1) {

                Intent intent = new Intent(getActivity(), PeriodWiseAttendanceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("classId", strClassId);
                bundle.putString(getString(R.string.key_Period), strPeriod);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (status >0) {
                new GetCurAttendance().execute();
            }
        }
    }


    class GetCurAttendance extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Current Attendance...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getperiodwiseClassAttendance));
                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_ClassId), strClassId);
                studAttendanceData.put(getString(R.string.key_Date), "01-01-0001");
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
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
                                    spnrMarkPeriodWiseAttendanceSection.setSelection(0);
                                    spnrMarkPeriodWiseAttendancePeriod.setSelection(-1);
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
            PeriodWiseAttendanceShowAdapter recyclerAdapter = new PeriodWiseAttendanceShowAdapter(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvPutPeriodwiseAtnd.setLayoutManager(layoutManager);
            rvPutPeriodwiseAtnd.setItemAnimator(new DefaultItemAnimator());
            rvPutPeriodwiseAtnd.setAdapter(recyclerAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }


    class GetPeriod extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "reportingOperation.jsp";
        ProgressDialog progressDialog;
        //= new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Period...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getPeriodForReport));
                JSONObject reportingData = new JSONObject();
                reportingData.put(getString(R.string.key_ClassId), strClassId);
                reportingData.put(getString(R.string.key_DayId), strDayId);
                outObject.put(getString(R.string.key_reportingData), reportingData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    Log.e("Tag","status if satisfied");
                    //Toast.makeText(getActivity(), strStatus, Toast.LENGTH_SHORT).show();
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrMarkPeriodWiseAttendanceSection.setSelection(0);
                                    spnrMarkPeriodWiseAttendancePeriod.setSelection(-1);
                                }
                            });
                            builder.setTitle("Alert");
                            builder.setMessage("Peroid not Found");
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
            PeriodSpinnerDataAdapter adapter = new PeriodSpinnerDataAdapter(values[0], getActivity());
            spnrMarkPeriodWiseAttendancePeriod.setPrompt("Choose Period");
            spnrMarkPeriodWiseAttendancePeriod.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }



    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
        //= new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Class...");
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
                                    spnrMarkPeriodWiseAttendanceSection.setSelection(-1);
                                    spnrMarkPeriodWiseAttendancePeriod.setSelection(-1);
                                }
                            });
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrMarkPeriodWiseAttendanceClass.setPrompt("Choose Class");
            spnrMarkPeriodWiseAttendanceClass.setAdapter(adapter);
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
            progressDialog.setMessage("Please wait fetching Section...");
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
                                    spnrMarkPeriodWiseAttendanceSection.setSelection(0);
                                    spnrMarkPeriodWiseAttendancePeriod.setSelection(-1);

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
            spnrMarkPeriodWiseAttendanceSection.setPrompt("Choose Section");
            spnrMarkPeriodWiseAttendanceSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}
