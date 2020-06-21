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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ParentNoteTitleSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentModel;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class WriteParentNote extends Fragment {
    Spinner spnrWriteParentNtClass, spnrWriteParentNtSection, spnrWriteParentNtTitle;
    EditText edtSetPrntNoteDt, edtSetPrntNoteTim, edtSetPrntOtherNoteTitle, edtSetPrntNot;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    Button btnStudentList;
    JSONArray jsonArray = new JSONArray();
    ListView lstWriteParentNt;
    RecyclerView recyclerViewWriteParentNt;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    String strDateTime, strPrntOtherTitle = "", strTime;
    String strMinute = "";
    String strSchoolId, strClassId, strClass, strStaffId, strNoteTitle, strStatus, strNoticeId, strDate, strNt, strAcademicYearId;
    private Session session;
    private ArrayList<StudentModel> studentModels;
    View view;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetPrntNoteDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    private int year, month, day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_write_parent_note, container, false);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, stdList);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return view;
    }

    public void init() {
        session = new Session(getActivity());
        edtSetPrntOtherNoteTitle = (EditText) getActivity().findViewById(R.id.edtSetPrntOtherNoteTitle);
        edtSetPrntNoteTim = (EditText) getActivity().findViewById(R.id.edtSetPrntNoteTim);
        edtSetPrntNot = (EditText) getActivity().findViewById(R.id.edtSetPrntNot);
        edtSetPrntNoteDt = (EditText) getActivity().findViewById(R.id.edtSetPrntNoteDt);
        btnStudentList = (Button) getActivity().findViewById(R.id.btnStudentList);
        spnrWriteParentNtClass = (Spinner) getActivity().findViewById(R.id.spnrWriteParentNtClass);
        spnrWriteParentNtSection = (Spinner) getActivity().findViewById(R.id.spnrWriteParentNtSection);
        spnrWriteParentNtTitle = (Spinner) getActivity().findViewById(R.id.spnrWriteParentNtTitle);
        //recyclerViewWriteParentNt = (RecyclerView) getActivity().findViewById(R.id.recyclerViewWriteParentNt);
        studentModels = new ArrayList<StudentModel>();
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

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            edtSetPrntOtherNoteTitle.setVisibility(View.GONE);
        }
    }

    public void allEvents() {

        edtSetPrntNoteDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtSetPrntNoteDt) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        edtSetPrntNoteTim.setOnClickListener(new View.OnClickListener() {
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
                        edtSetPrntNoteTim.setText(selectedHour + ":" + strMinute + AM_PM);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        spnrWriteParentNtClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteParentNtClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrWriteParentNtSection.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrWriteParentNtSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteParentNtSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetParentNoteTitle().execute();
                }else{
                    spnrWriteParentNtTitle.setAdapter(null);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrWriteParentNtTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteParentNtTitle.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strNoticeId = adapterView.getItemAtPosition(i).toString();
                strNoteTitle = tmpView.getText().toString();
                Log.e("Tag", "" + strSchoolId);
                if (strNoteTitle.equals("Other")) {
                    edtSetPrntOtherNoteTitle.setVisibility(View.VISIBLE);
                } else {
                    edtSetPrntOtherNoteTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate = edtSetPrntNoteDt.getText().toString();
                strTime = edtSetPrntNoteTim.getText().toString();
                strNt = edtSetPrntNot.getText().toString();
                strDateTime = edtSetPrntNoteDt.getText().toString() + " " + edtSetPrntNoteTim.getText().toString();
                strPrntOtherTitle = edtSetPrntOtherNoteTitle.getText().toString();
                //strNoteTitle = edtSetPrntNot.getText().toString();
                //strWriteNote = edtWriteParentNt.getText().toString();
                        if(strTime.length()<2){
                            strTime="9999";
                        }
                if (edtSetPrntOtherNoteTitle.getVisibility() == View.VISIBLE) {
                    //Toast.makeText(getActivity(), "Note should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    if (edtSetPrntNoteDt.getText().toString().equalsIgnoreCase(null) || edtSetPrntNoteDt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Date Can't be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetPrntOtherNoteTitle.getText().toString().length() < 3 || edtSetPrntOtherNoteTitle.getText().toString().length() > 100) {
                        Toast.makeText(getActivity(), "Other Note should be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetPrntNot.getText().toString().length() < 3 || edtSetPrntNot.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Note should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), StudentListActivity.class);
                        Bundle bundle = new Bundle();
                        String base64OtherTitle,base64Note;
                        base64OtherTitle=StringUtil.textToBase64(strPrntOtherTitle);
                        base64Note=StringUtil.textToBase64(strNt);
                        strNoteTitle=edtSetPrntOtherNoteTitle.getText().toString();
                        String base64NoiticeTitle=StringUtil.textToBase64(strNoteTitle);
                        //bundle.putString("staffId", strStaffId);
                        bundle.putString("classId", strClassId);
                        bundle.putString("strDate", strDate);
                        bundle.putString("noticeTitleId", strNoticeId);
                        bundle.putString("NoteTitle", base64NoiticeTitle);
                        bundle.putString("otherTitle", base64OtherTitle);
                        bundle.putString("note", base64Note);
                        bundle.putString("strTime", strTime);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        ///Toast.makeText(getActivity(), , Toast.LENGTH_SHORT).show();
                    }
                } else if (edtSetPrntOtherNoteTitle.getVisibility() == View.GONE) {
                    if (edtSetPrntNoteDt.getText().toString().equalsIgnoreCase(null)|| edtSetPrntNoteDt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Date Can't be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetPrntNot.getText().toString().length() < 3 || edtSetPrntNot.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Note should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    }  else {
                        if(strTime.length()<2){
                            strTime="9999";
                        }
                        Intent intent = new Intent(getActivity(), StudentListActivity.class);
                        Bundle bundle = new Bundle();
                        String base64OtherTitle,base64Note;
                        base64OtherTitle=StringUtil.textToBase64(strPrntOtherTitle);
                        base64Note=StringUtil.textToBase64(strNt);
                        String base64NoiticeTitle=StringUtil.textToBase64(strNoteTitle);
                        bundle.putString("classId", strClassId);
                        bundle.putString("strDate", strDate);
                        bundle.putString("noticeTitleId", strNoticeId);
                        bundle.putString("otherTitle", base64OtherTitle);
                        bundle.putString("NoteTitle", base64NoiticeTitle);
                        bundle.putString("note", base64Note);
                        bundle.putString("strTime", strTime);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }


                //new GetStudentList().execute();
                //Toast.makeText(getActivity(), strClassId + " \n " + strRmndrId, Toast.LENGTH_SHORT).show();
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
            spnrWriteParentNtClass.setPrompt("Choose Class");
            spnrWriteParentNtClass.setAdapter(adapter);
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
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], getActivity());
            spnrWriteParentNtSection.setPrompt("Choose Section");
            spnrWriteParentNtSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetParentNoteTitle extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Parent notice Title...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getParentNoteTitle));
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
            ParentNoteTitleSpinnerAdapter adapter = new ParentNoteTitleSpinnerAdapter(values[0], getActivity());
            spnrWriteParentNtSection.setPrompt("Choose Section");
            spnrWriteParentNtTitle.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
