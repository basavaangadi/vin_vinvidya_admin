package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.app.AlertDialog;
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
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ClassListActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassNoticeTitleSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ParentNoteTitleSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentModel;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.GetSoapResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteClassNoticeFragment extends Fragment {


    Spinner spnrWriteClassNoticeClass,  spnrWriteClassNoticeTitle;
    EditText edtSetClassNoticeDate, edtSetClassNoticeTime, edtSetClassNoticeOtherTitle, edtSetClassNotice;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    Button btnSendClassNotice;
    JSONArray jsonArray = new JSONArray();
    ListView lstWriteParentNt;
    RecyclerView recyclerViewWriteParentNt;
    Calendar currentCalendar = Calendar.getInstance();
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    String strDateTime, strPrntOtherTitle = "", strTime;
    String strMinute = "",base64NoteTitleName;
    String strSchoolId, strClassId, strClass, strStaffId, strNoteTitle, strStatus, strNoticeId, strDate, strNt;
    String strNoticeSetDate, strAcademicYearId;
    String base64OtherTitle,base64Note;
    private Session session;
    private ArrayList<StudentModel> studentModels;
    View view;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetClassNoticeDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };
    private int year, month, day;
    
    
    
    
    
    public WriteClassNoticeFragment() {
        // Required empty public constructor
    }


   
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_write_class_notice, container, false);
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
        edtSetClassNoticeOtherTitle = (EditText) getActivity().findViewById(R.id.edtSetClassNoticeOtherTitle);
        edtSetClassNoticeTime = (EditText) getActivity().findViewById(R.id.edtSetClassNoticeTime);
        edtSetClassNotice = (EditText) getActivity().findViewById(R.id.edtSetClassNotice);
        edtSetClassNoticeDate = (EditText) getActivity().findViewById(R.id.edtSetClassNoticeDate);
        btnSendClassNotice = (Button) getActivity().findViewById(R.id.btnSendClassNotice);
        /*spnrWriteClassNoticeClass = (Spinner) getActivity().findViewById(R.id.spnrWriteClassNoticeClass);
        spnrWriteClassNoticeSection = (Spinner) getActivity().findViewById(R.id.spnrWriteClassNoticeSection);*/
        spnrWriteClassNoticeTitle = (Spinner) getActivity().findViewById(R.id.spnrWriteClassNoticeTitle);
        //recyclerViewWriteParentNt = (RecyclerView) getActivity().findViewById(R.id.recyclerViewWriteParentNt);
        studentModels = new ArrayList<StudentModel>();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        strNoticeSetDate = df.format(currentCalendar.getTime());
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
            edtSetClassNoticeOtherTitle.setVisibility(View.GONE);
            new GetClassNoticeTitle().execute();
          //  new GetClass().execute();

        }
    }

    public void allEvents() {

        edtSetClassNoticeDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtSetClassNoticeDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        edtSetClassNoticeTime.setOnClickListener(new View.OnClickListener() {
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
                        edtSetClassNoticeTime.setText(selectedHour + ":" + strMinute + AM_PM);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        /*spnrWriteClassNoticeClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteClassNoticeClass.getSelectedView().findViewById(R.id.list);
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

        spnrWriteClassNoticeSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteClassNoticeSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        spnrWriteClassNoticeTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrWriteClassNoticeTitle.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strNoticeId = adapterView.getItemAtPosition(i).toString();
                strNoteTitle = tmpView.getText().toString();
                Log.e("Tag", "" + strNoticeId);
                if (strNoteTitle.equals("Other")) {
                    edtSetClassNoticeOtherTitle.setVisibility(View.VISIBLE);

                } else {
                    edtSetClassNoticeOtherTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSendClassNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate = edtSetClassNoticeDate.getText().toString();
                strTime = edtSetClassNoticeTime.getText().toString();
                strNt = edtSetClassNotice.getText().toString();
                strDateTime = edtSetClassNoticeDate.getText().toString() + " " + edtSetClassNoticeTime.getText().toString();
                strPrntOtherTitle = edtSetClassNoticeOtherTitle.getText().toString();


                if (edtSetClassNoticeOtherTitle.getVisibility() == View.VISIBLE) {
                    //Toast.makeText(getActivity(), "Note should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    if (edtSetClassNoticeDate.getText().toString().equalsIgnoreCase(null) || edtSetClassNoticeDate.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Date Can't be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetClassNoticeOtherTitle.getText().toString().length() < 3 || edtSetClassNoticeOtherTitle.getText().toString().length() > 100) {
                        Toast.makeText(getActivity(), "Other Note should be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetClassNotice.getText().toString().length() < 3 || edtSetClassNotice.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Note should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else {

                        base64OtherTitle= StringUtil.textToBase64(strPrntOtherTitle);
                        base64Note=StringUtil.textToBase64(strNt);
                       // Intent intentClassList = new Intent(getActivity(), ClassListActivity.class);
                        Intent intentClassList = new Intent(getActivity(), ClassListActivity.class);
                        intentClassList.putExtra(getString(R.string.key_SchoolId), strSchoolId);
                        intentClassList.putExtra(getString(R.string.key_AcademicYearId), strAcademicYearId);
                        intentClassList.putExtra(getString(R.string.key_NoticeTitleId), strNoticeId);
                        intentClassList.putExtra(getString(R.string.key_SetBy), strStaffId);
                        intentClassList.putExtra(getString(R.string.key_NoteTitleName),base64OtherTitle);
                        intentClassList.putExtra(getString(R.string.key_Notice), base64Note);
                        intentClassList.putExtra(getString(R.string.key_NoticeDate), strDate);
                        if (!(strTime.length() > 1)) {
                            strTime = "9999";
                        }
                        intentClassList.putExtra(getString(R.string.key_NoticeTime), strTime);

                        startActivity(intentClassList);
                       // new SetClassNotice().execute();

                    }

                } else if (edtSetClassNoticeOtherTitle.getVisibility() == View.GONE) {
                    if (edtSetClassNoticeDate.getText().toString().equalsIgnoreCase(null)|| edtSetClassNoticeDate.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Date Can't be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetClassNotice.getText().toString().length() < 3 || edtSetClassNotice.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Note should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    }  else {

                            base64NoteTitleName=StringUtil.textToBase64(strNoteTitle);
                           base64Note=StringUtil.textToBase64(strNt);
                        Intent intentClassList = new Intent(getActivity(), ClassListActivity.class);
                        intentClassList.putExtra(getString(R.string.key_SchoolId), strSchoolId);
                        intentClassList.putExtra(getString(R.string.key_AcademicYearId), strAcademicYearId);
                        intentClassList.putExtra(getString(R.string.key_NoticeTitleId), strNoticeId);
                        intentClassList.putExtra(getString(R.string.key_SetBy), strStaffId);
                        intentClassList.putExtra(getString(R.string.key_NoteTitleName),base64NoteTitleName);
                        intentClassList.putExtra(getString(R.string.key_Notice), base64Note);
                        intentClassList.putExtra(getString(R.string.key_NoticeDate), strDate);
                        if (!(strTime.length() > 1)) {
                            strTime = "9999";
                        }
                        intentClassList.putExtra(getString(R.string.key_NoticeTime), strTime);

                        startActivity(intentClassList);
                        //new SetClassNotice().execute();

                    }

                }

            }
        });
    }

    /*class GetClass extends AsyncTask<String, JSONArray, Void> {

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
            spnrWriteClassNoticeClass.setPrompt("Choose Class");
            spnrWriteClassNoticeClass.setAdapter(adapter);
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
            spnrWriteClassNoticeSection.setPrompt("Choose Section");
            spnrWriteClassNoticeSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }*/

    class GetClassNoticeTitle extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching  Notice Titles...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClassNoticeIdBySchoolId));
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
            ClassNoticeTitleSpinnerAdapter adapter = new ClassNoticeTitleSpinnerAdapter(values[0], getActivity());
            spnrWriteClassNoticeTitle.setPrompt("Choose Title");
            spnrWriteClassNoticeTitle.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    class SetClassNotice extends AsyncTask<String, JSONArray, Void> {
        String asmx_url=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_ClassNotice";
        String methodName="Insert_ClassNotice";
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        GetSoapResponse soapResponse= new GetSoapResponse();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Sending class notice");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            JSONObject outObject = new JSONObject();

            try {
              //  outObject.put(getString(R.string.key_OperationName), getString(R.string.web_insertClassNotice));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                noticeBoardData.put(getString(R.string.key_SetBy), strStaffId);
                noticeBoardData.put(getString(R.string.key_ClassId), strClassId);
               // noticeBoardData.put(getString(R.string.key_NoticeSetDate), strNoticeSetDate);
                noticeBoardData.put(getString(R.string.key_NoticeTitleId), strNoticeId);

                if (strNoticeId.equalsIgnoreCase("1")) {
                    noticeBoardData.put(getString(R.string.key_OtherTitle), base64OtherTitle);
                    noticeBoardData.put( getString(R.string.key_NoteTitleName),base64OtherTitle);
                }else{
                    String base64NoticeTitle=StringUtil.textToBase64(strNoteTitle);
                    noticeBoardData.put(getString(R.string.key_NoteTitleName),base64NoticeTitle);
                }

                noticeBoardData.put(getString(R.string.key_Notice), base64Note);
                noticeBoardData.put(getString(R.string.key_NoticeDate), strDate);
                if (!(strTime.length() > 1)) {
                    strTime = "9999";
                }
                noticeBoardData.put(getString(R.string.key_NoticeTime), strTime);
                List<PropertyInfo> param= new ArrayList<PropertyInfo>();
                PropertyInfo classNoticedata= new PropertyInfo();
                classNoticedata.setName("Insert_ClassNoticeData");
                classNoticedata.setValue(noticeBoardData.toString());
                Log.e("ClassNotice", "requestObject =" + noticeBoardData.toString());
                param.add(classNoticedata);
                String responseText = soapResponse.getSOAPStringResponse(asmx_url,methodName,param);
                Log.e("RespText",responseText);
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage=inObject.getString(getString(R.string.key_Message));
                int resId=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));
                   if(resId>0||resId==-1){
                       showAlert(strStatus,strMessage);
                   }else{
                       showAlert("Error","Something went wrong");
                   }


                //}
            } catch (Exception ex) {
                //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Exception ",ex.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ClassNoticeTitleSpinnerAdapter adapter = new ClassNoticeTitleSpinnerAdapter(values[0], getActivity());
            spnrWriteClassNoticeTitle.setPrompt("Choose Section");
            spnrWriteClassNoticeTitle.setAdapter(adapter);
        }

       public void showAlert(String strTitle,String strMessage){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", null);
                    builder.setTitle(strStatus);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}
