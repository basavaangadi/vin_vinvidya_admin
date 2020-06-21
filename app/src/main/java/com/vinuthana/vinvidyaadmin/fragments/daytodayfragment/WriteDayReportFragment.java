package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;


import android.app.DatePickerDialog;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PeriodSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SubjectSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteDayReportFragment extends Fragment {

    Spinner spnrWriteDayReportClass, spnrWriteDayReportSection,
            spnrWriteDayReportSubject,spnrWriteDayReportPeriod;
    EditText edtWriteDayReportDate, edtWriteDayReport;
    Button btnWriteDayReport;

    private Session session;
    String strDate, strSubject, strStaffId, strClass, strSection, strClassId, strSubjectId;
    String strReportSentDate,strDayId, strReport, strBase64Report, strSchoolId,strPeroid;
    String strAcademicYearId;
    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    Calendar viewHmFbByDt = Calendar.getInstance();
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtWriteDayReportDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    public WriteDayReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_write_day_report, container, false);

         return view;
    }
    public void init() {

        btnWriteDayReport = (Button) getActivity().findViewById(R.id.btnWriteDayReport);
        spnrWriteDayReportClass = (Spinner) getActivity().findViewById(R.id.spnrWriteDayReportClass);
        spnrWriteDayReportSection = (Spinner) getActivity().findViewById(R.id.spnrWriteDayReportSection);
        spnrWriteDayReportSubject = (Spinner) getActivity().findViewById(R.id.spnrWriteDayReportSubject);
        spnrWriteDayReportPeriod = (Spinner) getActivity().findViewById(R.id.spnrWriteDayReportPeriod);
        edtWriteDayReport = (EditText) getActivity().findViewById(R.id.edtWriteDayReport);
        edtWriteDayReportDate = (EditText) getActivity().findViewById(R.id.edtWriteDayReportDate);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            allEvents();

            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();

            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strReportSentDate = df.format(c.getTime());

            mYear = viewHmFbByDt.get(Calendar.YEAR);
            mMonth = viewHmFbByDt.get(Calendar.MONTH);
            mDay = viewHmFbByDt.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            new GetClass().execute();
        }
    }

    private void allEvents() {
        spnrWriteDayReportClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteDayReportClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrWriteDayReportSection.setSelection(0);
                    spnrWriteDayReportSection.setAdapter(null);
                    spnrWriteDayReportSubject.setAdapter(null);
                    spnrWriteDayReportPeriod.setAdapter(null);
                }
                //new GetSubject().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnrWriteDayReportPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) spnrWriteDayReportPeriod.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strPeroid = parent.getItemAtPosition(position).toString();
                Log.e("Tag", "" + strPeroid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrWriteDayReportSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteDayReportSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetSubject().execute();
                }else{
                    spnrWriteDayReportSubject.setAdapter(null);
                    spnrWriteDayReportPeriod.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrWriteDayReportSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteDayReportSubject.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strSubjectId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtWriteDayReportDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtWriteDayReportDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });
        edtWriteDayReportDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                if( !(edtWriteDayReportDate.getText().toString().length()<3)){

                        String strdaydate = edtWriteDayReportDate.getText().toString();
                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");

                        Date date = inFormat.parse(strdaydate);
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
                        if(strDayId.equalsIgnoreCase("7")) {
                            if(!(strSchoolId.equalsIgnoreCase("6"))){
                                Toast toast=  Toast.makeText(getActivity(), "please enter the date other then sunday", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                //Toast.makeText(getActivity(), "please enter the date other then sunday", Toast.LENGTH_SHORT).show();
                                spnrWriteDayReportPeriod.setAdapter(null);
                            }else if(edtWriteDayReportDate.getText().length()<3){
                                Toast toast=  Toast.makeText(getActivity(), "please enter the date", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                            else{

                                new GetPeriod().execute();
                            }
                        }else{
                            if(edtWriteDayReportDate.getText().length()<3){
                                Toast toast=  Toast.makeText(getActivity(), "please enter the date", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                            else{

                                new GetPeriod().execute();
                            }
                        }

               }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnWriteDayReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clsPos=spnrWriteDayReportClass.getSelectedItemPosition();
                int secPos=spnrWriteDayReportSection.getSelectedItemPosition();
                int subPos=spnrWriteDayReportSubject.getSelectedItemPosition();
                int perPos=spnrWriteDayReportPeriod.getSelectedItemPosition();

                if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)||(subPos==0||subPos==-1)||(perPos==0||perPos==-1)){
                    Toast toast=Toast.makeText(getActivity(), "Select all fields before sending the Report", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

                else if (edtWriteDayReport.getText().toString().length() < 3 || edtWriteDayReport.getText().toString().length() > 1000) {
                    Toast toast=Toast.makeText(getActivity(), "Day report should be between 3 and 1000 characters", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if (edtWriteDayReportDate.getText().toString().equals("")) {
                    Toast toast=  Toast.makeText(getActivity(), "Date can't be blank", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                }
                else {

                    strDate = edtWriteDayReportDate.getText().toString();

                    strReport = edtWriteDayReport.getText().toString();

                    strBase64Report =StringUtil.textToBase64(strReport);
                    new WriteDayReport().execute();
                }
            }
        });
    }

    class WriteDayReport extends AsyncTask<String, JSONArray, Void> {
        //ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "reportingOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding Daily report...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertStaffDailyReport));
                JSONObject reportingData = new JSONObject();

                reportingData.put(getString(R.string.key_StaffId), strStaffId);
                reportingData.put(getString(R.string.key_ClassId), strClassId);
                reportingData.put(getString(R.string.key_Date), strDate);
                reportingData.put(getString(R.string.key_SubjectId), strSubjectId);
                reportingData.put(getString(R.string.key_Report), strBase64Report);
                reportingData.put(getString(R.string.key_Period), strPeroid);
                reportingData.put(getString(R.string.key_ReportSentDate), strReportSentDate);
                outObject.put(getString(R.string.key_reportingData), reportingData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));
                int resid=inObject.getInt(getString(R.string.key_resId));
                String strStatus=inObject.getString(getString(R.string.key_Status));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (resid>0||resid==-1) {
                            alertMessage = strMsg;
                            alertTitle = strStatus;

                        } else {
                            alertTitle = "Error";
                            alertMessage = "Error Occured While Adding report..";
                            Toast.makeText(getActivity(), String.valueOf(resid), Toast.LENGTH_SHORT).show();
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
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

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
                spnrWriteDayReportSubject.setSelection(0);
                spnrWriteDayReportPeriod.setSelection(0);
                edtWriteDayReport.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

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
            spnrWriteDayReportClass.setPrompt("Choose Class");
            spnrWriteDayReportClass.setAdapter(adapter);
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
                }else{
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
                                    spnrWriteDayReportClass.setSelection(0);
                                    spnrWriteDayReportSection.setSelection(0);
                                    spnrWriteDayReportPeriod.setSelection(0);
                                    spnrWriteDayReportSubject.setSelection(0);
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
            spnrWriteDayReportSection.setPrompt("Choose Section");
            spnrWriteDayReportSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetSubject extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Subjects...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseSubjects));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
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

                                    spnrWriteDayReportSubject.setAdapter(null);

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
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], getActivity());
            spnrWriteDayReportSubject.setPrompt("Choose Section");
            spnrWriteDayReportSubject.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    class GetPeriod extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Subjects...");
            progressDialog.show();
        }
        String url = AD.url.base_url + "reportingOperation.jsp";

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

                                    spnrWriteDayReportPeriod.setSelection(0);
                                    spnrWriteDayReportSubject.setSelection(0);
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
            spnrWriteDayReportPeriod.setPrompt("Choose Section");
            spnrWriteDayReportPeriod.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
    }
}
