package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.vinuthana.vinvidyaadmin.adapters.PeriodWiseAttendanceAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PeriodWiseAttendanceShowAdapter;
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


public class ViewAttendanceByDate extends Fragment {
    DatePickerDialog datePickerDialog;
    Calendar viewHmFbByDt = Calendar.getInstance();
    private int mYear, mMonth, mDay;
    Button btnViewAtndByDate;
    private Session session;
    PeriodWiseAttendanceShowAdapter periodRecyclerAdapter;
    AtndRecyclerAdapter atndRecyclerAdapter;
    String strStaffId, strPassword, strSchoolId, strClass, strSection, strClassId, strDate, strAcademicYearId;
    Spinner spnrViewCurAtndByDtClass, spnrViewCurAtndByDtSection;
    EditText edtAttendanceByDate;
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    RecyclerView recyclerViewAtndByDt;
    JSONArray attendArray= new JSONArray();
    ProgressDialog progressDialog;
    boolean isFragmentLoaded = false;
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtAttendanceByDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_attendance_by_date, container, false);
    }

    public void init() {

        edtAttendanceByDate = getActivity().findViewById(R.id.edtAttendanceByDate);
        recyclerViewAtndByDt = getActivity().findViewById(R.id.recyclerViewAtndByDt);
        spnrViewCurAtndByDtClass = getActivity().findViewById(R.id.spnrViewCurAtndByDtClass);
        spnrViewCurAtndByDtSection = getActivity().findViewById(R.id.spnrViewCurAtndByDtSection);
        btnViewAtndByDate = getActivity().findViewById(R.id.btnViewAtndByDate);
        initRecylerAdapter();
    }
    public  void initRecylerAdapter(){
        if (strSchoolId.equalsIgnoreCase("6")) {
            periodRecyclerAdapter = new PeriodWiseAttendanceShowAdapter(attendArray, getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewAtndByDt.setLayoutManager(layoutManager);
            recyclerViewAtndByDt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAtndByDt.setAdapter(periodRecyclerAdapter);
        } else {
            atndRecyclerAdapter = new AtndRecyclerAdapter(attendArray, getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewAtndByDt.setLayoutManager(layoutManager);
            recyclerViewAtndByDt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAtndByDt.setAdapter(atndRecyclerAdapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();

            ArrayList<String> strUser = new ArrayList<>();
            //int size =

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            init();
            if (getActivity() != null) {

                new GetClass().execute();

            }
            allEvents();
            mYear = viewHmFbByDt.get(Calendar.YEAR);
            mMonth = viewHmFbByDt.get(Calendar.MONTH);
            mDay = viewHmFbByDt.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
            //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            //new GetClass().execute();

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

    private void allEvents() {
        edtAttendanceByDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtAttendanceByDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        spnrViewCurAtndByDtClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrViewCurAtndByDtClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + "You Clicked on ", Toast.LENGTH_SHORT).show();

                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrViewCurAtndByDtSection.setSelection(0);
                    spnrViewCurAtndByDtSection.setAdapter(null);
                }

                //new GetSection().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrViewCurAtndByDtSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrViewCurAtndByDtSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnViewAtndByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate = edtAttendanceByDate.getText().toString();
                int clsPos= spnrViewCurAtndByDtClass.getSelectedItemPosition();
                int secPos= spnrViewCurAtndByDtSection.getSelectedItemPosition();
                if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)){
                    Toast toast=Toast.makeText(getActivity(),"Select class and section before fecthing attendance",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=attendArray.length();i>=0;i--){

                            attendArray.remove(i);
                        }
                        if(strSchoolId.equalsIgnoreCase("6")){
                            periodRecyclerAdapter.notifyDataSetChanged();
                        }else{
                            atndRecyclerAdapter.notifyDataSetChanged();}

                    }
                    else{
                        attendArray= new JSONArray();
                        initRecylerAdapter();
                    }
                } else if (strDate.equals("")) {
                    Toast toast=Toast.makeText(getActivity(),"Date Cannot be blank",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=attendArray.length();i>=0;i--){

                            attendArray.remove(i);
                        }
                        if(strSchoolId.equalsIgnoreCase("6")){
                            periodRecyclerAdapter.notifyDataSetChanged();
                        }else{
                            atndRecyclerAdapter.notifyDataSetChanged();}

                    }
                    else{
                        attendArray= new JSONArray();
                        initRecylerAdapter();
                    }

                } else {
                    new GetCurAttendanceByDate().execute();
                }
                //new GetCurAttendanceByDate().execute();
            }
        });
    }

    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Plase wait fetching data...");
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
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrViewCurAtndByDtSection.setSelection(0);
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
            spnrViewCurAtndByDtClass.setPrompt("Choose Class");
            spnrViewCurAtndByDtClass.setAdapter(adapter);
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
            progressDialog.setMessage("Plase wait fetching Subjects...");
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
                                    spnrViewCurAtndByDtSection.setSelection(0);
                                    spnrViewCurAtndByDtClass.setSelection(0);
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
            spnrViewCurAtndByDtSection.setPrompt("Choose Section");
            spnrViewCurAtndByDtSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetCurAttendanceByDate extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "studentAttendanceOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Attendance By Date...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for(int i=attendArray.length();i>=0;i--){

                    attendArray.remove(i);
                }
            }else{
                attendArray= new JSONArray();
            }
        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                if(strSchoolId.equalsIgnoreCase("6")){
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getperiodwiseClassAttendance));
                }else{
                    outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseAttendanceByDate));
                }

                JSONObject studAttendanceData = new JSONObject();
                studAttendanceData.put(getString(R.string.key_ClassId), strClassId);
                studAttendanceData.put(getString(R.string.key_Date), strDate);
                outObject.put(getString(R.string.key_studAttendanceData), studAttendanceData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray resultArray= new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < resultArray.length(); i++) {
                        attendArray.put(resultArray.getJSONObject(i));
                    }
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
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
                                    spnrViewCurAtndByDtClass.setSelection(0);
                                    spnrViewCurAtndByDtSection.setSelection(-1);
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
            /*if (strSchoolId.equalsIgnoreCase("6")) {
                PeriodWiseAttendanceShowAdapter recyclerAdapter = new PeriodWiseAttendanceShowAdapter(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewAtndByDt.setLayoutManager(layoutManager);
                recyclerViewAtndByDt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewAtndByDt.setAdapter(recyclerAdapter);
            } else {
                AtndRecyclerAdapter recyclerAdapter = new AtndRecyclerAdapter(values[0], getActivity());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewAtndByDt.setLayoutManager(layoutManager);
                recyclerViewAtndByDt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewAtndByDt.setAdapter(recyclerAdapter);
            }*/

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                if(strSchoolId.equalsIgnoreCase("6")){
                    periodRecyclerAdapter.notifyDataSetChanged();
                }else{
                    atndRecyclerAdapter.notifyDataSetChanged();}

            }else{
                initRecylerAdapter();
            }
        }
    }
}
