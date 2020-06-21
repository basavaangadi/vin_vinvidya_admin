package com.vinuthana.vinvidyaadmin.fragments.examsectionfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SubjectSpinnerDataAdapter;
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


public class SetExamSchedule extends Fragment {

    Spinner spnrExmSchdlClass, spnrExmSchdlSection, spnrExmSchdlExam, spnrExmSchdlSubject;
    Button btnSetExmSchdl;
    EditText edtExmSchdlDate, edtExmSchdlEndTime, edtExmSchdlStartTime;
    DatePickerDialog datePickerDialog;
    Calendar hmFbByDt = Calendar.getInstance();
    public SharedPreferences preferences;
    SharedPreferences.Editor edit;
    private Session session;
    String strSetDate, strStaffId, strClass, strSection, strClassId, strSubjectId, strExamId, strSchoolId;
    private int mYear, mMonth, mDay;
    Calendar startTime = Calendar.getInstance();
    Calendar endTime = Calendar.getInstance();
    int startHour = startTime.get(Calendar.HOUR_OF_DAY);
    int startMinute = startTime.get(Calendar.MINUTE);
    int endHour = endTime.get(Calendar.HOUR_OF_DAY);
    int endMinute = endTime.get(Calendar.MINUTE);
    TimePickerDialog startTimePicker, endTimePicker;
    String startFromTime, strDate, strAcademicYearId, endToTime;
    ProgressDialog progressDialog;
    String strFrmMinute = "";
    String strToMinute = "";
    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtExmSchdlDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    /*TimePickerDialog.OnTimeSetListener timelistener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            edtExmSchdlTime.setText(time1);
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init() {

        edtExmSchdlDate = (EditText) getActivity().findViewById(R.id.edtExmSchdlDate);
        edtExmSchdlStartTime = (EditText) getActivity().findViewById(R.id.edtExmSchdlStartTime);
        edtExmSchdlEndTime = (EditText) getActivity().findViewById(R.id.edtExmSchdlEndTime);
        spnrExmSchdlClass = (Spinner) getActivity().findViewById(R.id.spnrExmSchdlClass);
        spnrExmSchdlSection = (Spinner) getActivity().findViewById(R.id.spnrExmSchdlSection);
        spnrExmSchdlExam = (Spinner) getActivity().findViewById(R.id.spnrExmSchdlExam);
        spnrExmSchdlSubject = (Spinner) getActivity().findViewById(R.id.spnrExmSchdlSubject);
        btnSetExmSchdl = (Button) getActivity().findViewById(R.id.btnSetExmSchdl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_exam_schedule, container, false);
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
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

            new GetClass().execute();
            mYear = hmFbByDt.get(Calendar.YEAR);
            mMonth = hmFbByDt.get(Calendar.MONTH);
            mDay = hmFbByDt.get(Calendar.DAY_OF_MONTH);
            init();
            allEvents();
            datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());
            // strSetDate have current date/time
            //Toast.makeText(getActivity(), strSetDate, Toast.LENGTH_SHORT).show();
        }
    }

    public void allEvents() {
        edtExmSchdlDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtExmSchdlDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        /*edtExmSchdlTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtExmSchdlTime) {

                }
                return false;
            }
        });*/

        edtExmSchdlStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
                        strFrmMinute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            strFrmMinute = "0" + strFrmMinute;

                        }

                        edtExmSchdlStartTime.setText(selectedHour + ":" + strFrmMinute + AM_PM);
                    }
                }, startHour, startMinute, false);
                startTimePicker.setTitle("Select Time");
                startTimePicker.show();
            }
        });

        edtExmSchdlEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
                        strToMinute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            strToMinute = "0" + strToMinute;

                        }
                        edtExmSchdlEndTime.setText(selectedHour + ":" + strToMinute + AM_PM);
                    }
                }, endHour, endMinute, false);
                endTimePicker.setTitle("Select Time");
                endTimePicker.show();
            }
        });


        spnrExmSchdlClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmSchdlClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);

                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);

                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrExmSchdlSection.setSelection(0);
                    spnrExmSchdlSection.setAdapter(null);
                    spnrExmSchdlExam.setAdapter(null);
                    spnrExmSchdlSubject.setAdapter(null);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmSchdlSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmSchdlSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetExam().execute();
                    new GetSubject().execute();
                }else {
                    spnrExmSchdlExam.setAdapter(null);
                    spnrExmSchdlSubject.setAdapter(null);

                }




                //new GetSubject().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmSchdlExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*String selected = (String) adapterView.getItemAtPosition(i);
                if (i > 0){
                    Toast.makeText(getActivity(), "Selected " + selected, Toast.LENGTH_SHORT).show();
                }*/
                TextView tmpView = (TextView) spnrExmSchdlExam.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                int pos = adapterView.getSelectedItemPosition();
                String strExam=tmpView.getText().toString();
                if(!(strExam.equalsIgnoreCase("Select Exam")||pos==0||pos==-1)){
                    new GetSubject().execute();
                }
                else{
                    spnrExmSchdlSubject.setSelection(0);
                    spnrExmSchdlSubject.setAdapter(null);
                }

                Log.e("Tag", "" + strExamId);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmSchdlSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*String selected = (String) adapterView.getItemAtPosition(i);
                if (i > 0){
                    Toast.makeText(getActivity(), "Selected " + selected, Toast.LENGTH_SHORT).show();
                }*/
                TextView tmpView = (TextView) spnrExmSchdlSubject.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strSubjectId);
                //Toast.makeText(getActivity(), strExam + " You Clicked on", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSetExmSchdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate = edtExmSchdlDate.getText().toString();
                startFromTime = edtExmSchdlStartTime.getText().toString();
                endToTime = edtExmSchdlEndTime.getText().toString();
                int clsPos= spnrExmSchdlClass.getSelectedItemPosition();
                int secPos= spnrExmSchdlSection.getSelectedItemPosition();
                int subPos= spnrExmSchdlSubject.getSelectedItemPosition();
                int exmPos= spnrExmSchdlExam.getSelectedItemPosition();

                  if((clsPos==0||clsPos==-1)||(secPos==-1||secPos==0)||(subPos==0||subPos==-1)||(exmPos==0||exmPos==-1)){
                    Toast toast=Toast.makeText(getActivity(),"Select class,section,subject and exam ",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if (edtExmSchdlDate.getText().toString().equals("") || edtExmSchdlStartTime.getText().toString().equals("") || edtExmSchdlEndTime.getText().toString().equals("")) {
                    Toast toast=Toast.makeText(getActivity(),"Date, Start Time and End Time can't be blank",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                   // Toast.makeText(getActivity(), "Date, Start Time and End Time can't be blank", Toast.LENGTH_SHORT).show();
                } else {

                        new SetExamSchdule().execute();

                }
            }
        });

    }

    private boolean checktimings(String startTime, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endtime);

            if(startDate.before(endDate)) {
                return true;
            } else {

                return false;
            }
        } catch (Exception e){
            Log.e("checkingTiming",e.toString());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    class SetExamSchdule extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding Exam Schedule...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_setStaffexamSchdule));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_SubjectId), strSubjectId);
                examData.put(getString(R.string.key_SetBy), strStaffId);
                examData.put(getString(R.string.key_SetDate), strSetDate);
                examData.put(getString(R.string.key_ExamOnDate), strDate);
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_ExamStartTime), startFromTime);
                examData.put(getString(R.string.key_ExamEndTime), endToTime);
                outObject.put(getString(R.string.key_examData), examData);

                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strMsg = inObject.getString(getString(R.string.key_Message));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (strMsg.equals("Exam schedule set sucessfully")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equals("Exam schedule has been already set")) {
                            alertMessage = strMsg;
                            alertTitle = "Fail";
                        } else if (strMsg.equals("Exam schedule has been already set please contact service provider to re-enable it!!"))
                        {
                            alertMessage = strMsg;
                            alertTitle = "Fail";
                        } else{
                            alertTitle = "Fail";
                            alertMessage = "Coludn't set the exam schedule because of some error..";
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
            edtExmSchdlDate.setText("");
            edtExmSchdlStartTime.setText("");
            edtExmSchdlEndTime.setText("");
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
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
                            View convertView = (View) inflater.inflate(R.layout.custom,null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrExmSchdlSection.setSelection(-1);
                                    spnrExmSchdlExam.setSelection(-1);
                                    spnrExmSchdlSubject.setSelection(-1);
                                }
                            });
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
            spnrExmSchdlClass.setPrompt("Choose Class");
            spnrExmSchdlClass.setAdapter(adapter);
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
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom,null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrExmSchdlSection.setSelection(-1);
                                    spnrExmSchdlExam.setSelection(-1);
                                    spnrExmSchdlSubject.setSelection(-1);
                                }
                            });
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
            spnrExmSchdlSection.setPrompt("Choose Section");
            spnrExmSchdlSection.setAdapter(adapter);
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
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom,null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    spnrExmSchdlExam.setSelection(-1);
                                    spnrExmSchdlSubject.setSelection(-1);
                                }
                            });
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
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], getActivity());
            spnrExmSchdlSubject.setPrompt("Choose Subject");
            spnrExmSchdlSubject.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetExam extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Exams...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                //outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamName));
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getExamNameForSchedule));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_examData), examData);
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
                            View convertView = (View) inflater.inflate(R.layout.custom,null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    spnrExmSchdlExam.setSelection(-1);
                                    spnrExmSchdlSubject.setSelection(-1);
                                }
                            });
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
            ExamSpinnerDataAdapter adapter = new ExamSpinnerDataAdapter(values[0], getActivity());
            spnrExmSchdlExam.setPrompt("Choose Exam");
            spnrExmSchdlExam.setAdapter(adapter);
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

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
