package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReminderSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StaffAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StaffData;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class SetStudReminder extends Fragment {

    Spinner spnrSetStudRmndr, spnrSetStudRmndrClass, spnrSetStudRmndrSection;
    Button btnSetStudRmndr;
    EditText edtSetStudRmndrOthrTitle, edtSetStudRmndr, edtSetStudRmndrDt, edtSetStudRmndrTim;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    String strSchoolId, strClassId, strClass, strStaffId, strRmndrTitle, strStatus, strRmndrId, strSetDate, strRmndr, strOtherTitle;
    private int year, month, day;
    private Session session;
    private ArrayList<StaffData> staffData;
    View view;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    String strDateTime, strDate, strAcademicYearId, strTime;
    String strMinute = "";
    ListView lstSetTchrNt;
    JSONArray jsonArray;
    ProgressDialog progressDialog;
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    StaffAdapter adapter;
    JSONArray js_array = new JSONArray();
    ArrayList<String> staffNameList = new ArrayList<String>();
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetStudRmndrDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
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
        view = inflater.inflate(R.layout.fragment_set_stud_reminder, container, false);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            allEvents();

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            new GetClass().execute();
            new GetStudReminder().execute();
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());

            edtSetStudRmndrOthrTitle.setVisibility(View.GONE);
        }
    }

    public void init() {
        session = new Session(getActivity());
        btnSetStudRmndr = getActivity().findViewById(R.id.btnSetStudRmndr);
        spnrSetStudRmndr = getActivity().findViewById(R.id.spnrSetStudRmndr);
        spnrSetStudRmndrClass = getActivity().findViewById(R.id.spnrSetStudRmndrClass);
        spnrSetStudRmndrSection = getActivity().findViewById(R.id.spnrSetStudRmndrSection);
        edtSetStudRmndrOthrTitle = getActivity().findViewById(R.id.edtSetStudRmndrOthrTitle);
        edtSetStudRmndr = getActivity().findViewById(R.id.edtSetStudRmndr);
        edtSetStudRmndrDt = getActivity().findViewById(R.id.edtSetStudRmndrDt);
        edtSetStudRmndrTim = getActivity().findViewById(R.id.edtSetStudRmndrTim);
        staffData = new ArrayList<StaffData>();
    }

    private void allEvents() {
        edtSetStudRmndrDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtSetStudRmndrDt) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        edtSetStudRmndrTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM = " AM";
                        if (selectedHour >= 12) {
                            AM_PM = " PM";
                            if (selectedHour >= 13 && selectedHour < 24) {
                                selectedHour -= 12;
                            } else {
                                selectedHour = 12;
                            }
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        strMinute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            strMinute = "0" + strMinute;

                        }
                        edtSetStudRmndrTim.setText(selectedHour + ":" + strMinute + AM_PM);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btnSetStudRmndr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOtherTitle = edtSetStudRmndrOthrTitle.getText().toString();
                strRmndr = edtSetStudRmndr.getText().toString();
               String base64Rmndr= StringUtil.textToBase64(strRmndr);
                strDate = edtSetStudRmndrDt.getText().toString();
                strTime = edtSetStudRmndrTim.getText().toString();
                strDateTime = edtSetStudRmndrDt.getText().toString() ;

                if (edtSetStudRmndrOthrTitle.getVisibility() == View.VISIBLE) {
                    //Toast.makeText(getActivity(), "Reminder should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    if (edtSetStudRmndrDt.getText().toString().equalsIgnoreCase(null) || edtSetStudRmndrDt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Date cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetStudRmndr.getText().toString().length() < 3 || edtSetStudRmndr.getText().toString().length() > 100) {
                        Toast.makeText(getActivity(), "Reminder should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetStudRmndrOthrTitle.getText().toString().length() < 3 || edtSetStudRmndrOthrTitle.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Other Title should be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), StudReminderActivity.class);
                        Bundle bundle = new Bundle();
                        String base64NoteTitle=StringUtil.textToBase64(strOtherTitle);
                        bundle.putString("ReminderTitle", base64NoteTitle);
                        bundle.putString("ReminderTitleId", strRmndrId);
                        bundle.putString("Reminder", base64Rmndr);
                        bundle.putString("ReminderDate", strDate);
                        bundle.putString("ClassId", strClassId);
                        bundle.putString("ReminderTime", strTime);
                        intent.putExtras(bundle);
                        //Toast.makeText(getActivity(), strTime, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                } else if (edtSetStudRmndrOthrTitle.getVisibility() == View.GONE) {
                    if (edtSetStudRmndrDt.getText().toString().equalsIgnoreCase(null) || edtSetStudRmndrDt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Fields cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetStudRmndr.getText().toString().length() < 3 || edtSetStudRmndr.getText().toString().length() > 100) {
                        Toast.makeText(getActivity(), "Reminder should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } /*else if (edtSetStudRmndrOthrTitle.getText().toString().length() < 3 || edtSetStudRmndrOthrTitle.getText().toString().length() < 1000) {
                        Toast.makeText(getActivity(), "Reminder should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    }*/ else {
                        Intent intent = new Intent(getActivity(), StudReminderActivity.class);
                        Bundle bundle = new Bundle();
                        String base64ReminderTitle=StringUtil.textToBase64(strRmndrTitle);
                        bundle.putString("ReminderTitle", base64ReminderTitle);
                        bundle.putString("ReminderTitleId", strRmndrId);
                        bundle.putString("Reminder", base64Rmndr);
                        bundle.putString("ReminderDate", strDate);
                        bundle.putString("ClassId", strClassId);
                        bundle.putString("ReminderTime", strTime);
                        intent.putExtras(bundle);
                        //Toast.makeText(getActivity(), strTime, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }

                /*Intent intent = new Intent(getActivity(), StaffRmndrActivity.class);
                startActivity(intent);*/
                //Toast.makeText(getActivity(), strClass + " " + strClassId, Toast.LENGTH_SHORT).show();
            }
        });

        spnrSetStudRmndr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSetStudRmndr.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strRmndrId = adapterView.getItemAtPosition(i).toString();
                strRmndrTitle = tmpView.getText().toString();
                if (strRmndrTitle.equals("Other")) {
                    //Toast.makeText(getActivity(), "Selected Text is: " + strRmndrTitle, Toast.LENGTH_SHORT).show();
                    edtSetStudRmndrOthrTitle.setVisibility(View.VISIBLE);
                } else {
                    //Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                    edtSetStudRmndrOthrTitle.setVisibility(View.GONE);
                }
                Log.e("Tag", "" + strRmndrTitle);
                //Toast.makeText(getActivity(), strRmndrId + " " + strRmndrTitle, Toast.LENGTH_SHORT).show();
                //new GetStaffList().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSetStudRmndrClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSetStudRmndrClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrSetStudRmndrSection.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSetStudRmndrSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSetStudRmndrSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class GetStudReminder extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Student Reminder title...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStudentReminderIdBySchoolId));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } /*else {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ReminderSpinnerAdapter adapter = new ReminderSpinnerAdapter(values[0], getActivity());
            spnrSetStudRmndr.setPrompt("Choose Section");
            spnrSetStudRmndr.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Classes...");
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
                            builder.setMessage("Could Not find the classes");
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
            spnrSetStudRmndrClass.setPrompt("Choose Class");
            spnrSetStudRmndrClass.setAdapter(adapter);
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
                } /*else {
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
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], getActivity());
            spnrSetStudRmndrSection.setPrompt("Choose Section");
            spnrSetStudRmndrSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
