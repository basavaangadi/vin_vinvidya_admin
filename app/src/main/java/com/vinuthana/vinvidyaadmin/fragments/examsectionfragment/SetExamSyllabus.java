package com.vinuthana.vinvidyaadmin.fragments.examsectionfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.vinuthana.vinvidyaadmin.adapters.ExamSchdlSpinnerAdapter;
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
import java.util.HashMap;


public class SetExamSyllabus extends Fragment {
    Spinner spnrExmSyllabusClass, spnrExmSyllabusSection, spnrExmSyllabusExam, spnrExmSyllabusSubject, spnrExmSylExmScdl;
    EditText edtExmSyllabusDate, edtExmSyllabusTime, edtExmSyllabus;
    Button btnSetExmSyllabus;
    RecyclerView recyclerViewSetExamSylbs;
    private Session session;
    String strSetDate, strStaffId, strClass, strSylabus, strClassId, strSubjectId, strExamId, strSchoolId, strMinute,
            strDateTime;
    DatePickerDialog datePickerDialog;
    Calendar dat = Calendar.getInstance();
    private int mYear, mMonth, mDay;
    String strDate, strScheduleDate, strAcademicYearId;
    View view;
    TimePickerDialog mTimePicker;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtExmSyllabusDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
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

        mYear = dat.get(Calendar.YEAR);
        mMonth = dat.get(Calendar.MONTH);
        mDay = dat.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        view = inflater.inflate(R.layout.fragment_set_exam_syllabus, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    public void init() {
        session = new Session(getActivity());
        btnSetExmSyllabus = (Button) getActivity().findViewById(R.id.btnSetExmSyllabus);
        recyclerViewSetExamSylbs = (RecyclerView) getActivity().findViewById(R.id.recyclerViewSetExamSylbs);
        spnrExmSyllabusClass = (Spinner) getActivity().findViewById(R.id.spnrExmSyllabusClass);
        spnrExmSyllabusSection = (Spinner) getActivity().findViewById(R.id.spnrExmSyllabusSection);
        spnrExmSyllabusExam = (Spinner) getActivity().findViewById(R.id.spnrExmSyllabusExam);
        spnrExmSyllabusSubject = (Spinner) getActivity().findViewById(R.id.spnrExmSyllabusSubject);
        spnrExmSylExmScdl = (Spinner) getActivity().findViewById(R.id.spnrExmSylExmScdl);

        edtExmSyllabus = (EditText) getActivity().findViewById(R.id.edtExmSyllabus);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            allEvents();
            new GetClass().execute();
            HashMap<String, String> user = session.getUserDetails();

            ArrayList<String> strUser = new ArrayList<>();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());
        }
    }

    private void allEvents() {

        spnrExmSyllabusClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmSyllabusClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                new GetSection().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmSyllabusSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmSyllabusSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
                new GetExam().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmSyllabusExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmSyllabusExam.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strExamId);
                //Toast.makeText(getActivity(), strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
                new GetSubject().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmSyllabusSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmSyllabusSubject.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strSubjectId);
                new GetResultExamSchedule().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmSylExmScdl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmSylExmScdl.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strScheduleDate = adapterView.getItemAtPosition(i).toString();
                strDate = tmpView.getText().toString();
                Log.e("Tag", "" + strSubjectId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSetExmSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtExmSyllabus.getText().toString().length() < 3 || edtExmSyllabus.getText().toString().length() > 1000) {
                    Toast.makeText(getActivity(), "Chapter name should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                } else {
                    strSylabus = edtExmSyllabus.getText().toString();

                    new SetExamSylbs().execute();
                }

                //}
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
                }  {
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
            spnrExmSyllabusClass.setPrompt("Choose Class");
            spnrExmSyllabusClass.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetSection extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

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
            spnrExmSyllabusSection.setPrompt("Choose Section");
            spnrExmSyllabusSection.setAdapter(adapter);
        }
    }

    class GetSubject extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

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
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], getActivity());
            spnrExmSyllabusSubject.setPrompt("Choose Section");
            spnrExmSyllabusSubject.setAdapter(adapter);
        }
    }

    class GetExam extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getScheduleExamName));

                //outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getExamNameForSyllabaus));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_examData), examData);
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
            ExamSpinnerDataAdapter adapter = new ExamSpinnerDataAdapter(values[0], getActivity());
            spnrExmSyllabusExam.setPrompt("Choose Exam");
            spnrExmSyllabusExam.setAdapter(adapter);
        }
    }

    class SetExamSylbs extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding Exam Syllabus...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_setStaffExamSyllabus));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_SubjectId), strSubjectId);
                examData.put(getString(R.string.key_SetBy), strStaffId);
                examData.put(getString(R.string.key_SetDate), strSetDate);
                examData.put(getString(R.string.key_ExamOnDate), strDate);
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_Syllabus), strSylabus);
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
                        if (strMsg.equals("inserted sucessfully")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        } else if (strMsg.equals("record already exist")) {
                            alertMessage = strMsg;
                            alertTitle = "fail";
                        } else {
                            alertTitle = "fail";
                            alertMessage = "record was not inserted..";
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
            /*ExamSheduleRecycler recyclerAdapter = new ExamSheduleRecycler(values[0], getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewSetExamSylbs.setLayoutManager(layoutManager);
            recyclerViewSetExamSylbs.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSetExamSylbs.setAdapter(recyclerAdapter);*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            edtExmSyllabus.setText("");
            /*edtExmSyllabusDate.setText("");
            edtExmSyllabusTime.setText("");*/
        }
    }

    class GetResultExamSchedule extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getresultExamSchedule));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_SubjectId), strSubjectId);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                }/* else {
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
            ExamSchdlSpinnerAdapter adapter = new ExamSchdlSpinnerAdapter(values[0], getActivity());
            spnrExmSylExmScdl.setAdapter(adapter);
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
