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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SubjectSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.GetSoapResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class GiveHomeWork extends Fragment {
    Spinner spnrGiveHmWrkClass, spnrGiveHmWrkSection,
            spnrGiveHmWrkSubject,spnrGiveHmWrkId;
    EditText edtHwrkType, edtGiveHmwrkHmwrk,edtGiveHmWrkChapter;
    Button btnGiveHmWrk;
    SectionSpinnerDataAdapter sectionAdapter;
    ArrayList<String>strSectionList=new ArrayList<>();
    EditText edtGiveHmWrkDate;
    private Session session;
    String strDate, strSubject, strStaffId, strClass, strSection, strClassId, strSubjectId;
    String   strChptName,strBase64ChptName, strHmWrk,strBase64Hmwrk, strSchoolId,strHomeWorkType;
    String strAcademicYearId;
    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    Calendar viewHmFbByDt = Calendar.getInstance();
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtGiveHmWrkDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
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
        return inflater.inflate(R.layout.fragment_give_home_work, container, false);
    }

    public void init() {
        session = new Session(getActivity());
        btnGiveHmWrk = (Button) getActivity().findViewById(R.id.btnGiveHmWrk);
        spnrGiveHmWrkClass = (Spinner) getActivity().findViewById(R.id.spnrGiveHmWrkClass);
        spnrGiveHmWrkSection = (Spinner) getActivity().findViewById(R.id.spnrGiveHmWrkSection);
        spnrGiveHmWrkSubject = (Spinner) getActivity().findViewById(R.id.spnrGiveHmWrkSubject);
        edtGiveHmWrkChapter = getActivity().findViewById(R.id.edtGiveHmWrkChapter);
        //spnrGiveHmWrkId = (Spinner) getActivity().findViewById(R.id.spnrGiveHmWrkId);
        //edtHwrkType = (EditText) getActivity().findViewById(R.id.edtHwrkType);
        edtGiveHmwrkHmwrk = (EditText) getActivity().findViewById(R.id.edtGiveHmwrkHmwrk);
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
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            mYear = viewHmFbByDt.get(Calendar.YEAR);
            mMonth = viewHmFbByDt.get(Calendar.MONTH);
            mDay = viewHmFbByDt.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
    }

    private void allEvents() {
        spnrGiveHmWrkClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrGiveHmWrkClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();

                Log.e("Tag", "" + strClass);
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrGiveHmWrkSection.setSelection(0);
                    spnrGiveHmWrkSection.setAdapter(null);
                    //spnrGiveHmWrkSubject.setSelection(0);
                    spnrGiveHmWrkSubject.setAdapter(null);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrGiveHmWrkSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrGiveHmWrkSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetSubject().execute();
                }else{
                    spnrGiveHmWrkSubject.setSelection(0);
                    spnrGiveHmWrkSubject.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrGiveHmWrkSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrGiveHmWrkSubject.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strSubjectId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnGiveHmWrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clsPos= spnrGiveHmWrkClass.getSelectedItemPosition();
                int secPos= spnrGiveHmWrkSection.getSelectedItemPosition();
                int subPos= spnrGiveHmWrkSubject.getSelectedItemPosition();

                 /*if (edtGiveHmWrkDate.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Date can't be blank", Toast.LENGTH_LONG).show();
                }else*/ if((clsPos==0||clsPos==-1)||(secPos==-1||secPos==0)||(subPos==0||subPos==-1)){
                    Toast toast=Toast.makeText(getActivity(),"Select class,section and subject",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                 else if (edtGiveHmwrkHmwrk.getText().toString().length() < 3 || edtGiveHmwrkHmwrk.getText().toString().length() > 1000) {
                    Toast.makeText(getActivity(), "Home work should be between 3 and 1000", Toast.LENGTH_SHORT).show();
                }else if (edtGiveHmWrkChapter.getText().toString().length() < 3 || edtGiveHmWrkChapter.getText().toString().length() > 1000) {
                    Toast.makeText(getActivity(), "Chapter name should be between 3 and 1000", Toast.LENGTH_SHORT).show();
                } else {
                    strChptName = edtGiveHmWrkChapter.getText().toString();

                    strHmWrk = edtGiveHmwrkHmwrk.getText().toString();
                    strBase64ChptName= StringUtil.textToBase64(strChptName);
                    strBase64Hmwrk=StringUtil.textToBase64(strHmWrk);
                    new GiveHmWork().execute();
                }
            }
        });
    }

    class GiveHmWork extends AsyncTask<String, JSONArray, Void> {
        //ProgressDialog progressDialog;
        String strMsg = "";
        String url = AD.url.base_url + "homeworkOperation.jsp";
        String asmx_url=AD.url.asmx_url+"WebService_DayToDayActivity.asmx?op=Insert_Homework";
        String methodName="Insert_Homework";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Adding Home Work...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_HomeWork_Insert));
                JSONObject homeworkData = new JSONObject();

                Log.e("Tag", "outObject =" + outObject.toString());



                GetSoapResponse soapResponse = new GetSoapResponse();

                JSONArray requestArray= new JSONArray();
                List<PropertyInfo> param = new ArrayList<PropertyInfo>();
                PropertyInfo propertyInfo = new PropertyInfo();
                homeworkData.put(getString(R.string.key_HomeworkId), "0");
                homeworkData.put(getString(R.string.key_StaffId), strStaffId);
                homeworkData.put(getString(R.string.key_ClassId), strClassId);
                homeworkData.put(getString(R.string.key_SubjectId), strSubjectId);
                homeworkData.put(getString(R.string.key_Chapter), strBase64ChptName);
                homeworkData.put(getString(R.string.key_Description), strBase64Hmwrk);

                    propertyInfo.setName("Insert_HomeworkData");
                    propertyInfo.setValue(homeworkData.toString());

                    param.add(propertyInfo);
                    final String resp = soapResponse.getSOAPStringResponse(asmx_url, methodName, param);
                    JSONObject inObject= new JSONObject(resp);
                String strMessage= inObject.getString(getString(R.string.key_Message));
                String strStatus=inObject.getString(getString(R.string.key_Status));
                int resid=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));


                Log.e("Tag", "responseText is =" + resp);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (resid==-1||resid>0){
                            alertMessage = strMessage;
                            alertTitle = strStatus;
                        } else {
                            alertTitle = "Error";
                            alertMessage = "Error Occured While Adding Home Work..";
                        }
                        showAlert(alertMessage, alertTitle);
                    }
                });

            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
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
            edtGiveHmwrkHmwrk.setText("");

        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               spnrGiveHmWrkSubject.setSelection(0);
               edtGiveHmWrkChapter.setText("");
                dialog.dismiss();
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
                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrGiveHmWrkClass.setPrompt("Choose Class");
            spnrGiveHmWrkClass.setAdapter(adapter);
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
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrGiveHmWrkSection.setSelection(0);
                                    spnrGiveHmWrkSubject.setSelection(-1);
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
             sectionAdapter = new SectionSpinnerDataAdapter(values[0], getActivity());
            spnrGiveHmWrkSection.setPrompt("Choose Section");
            spnrGiveHmWrkSection.setAdapter(sectionAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetSubject extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "classsubjectOperations.jsp";
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
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrGiveHmWrkSubject.setSelection(-1);
                                    spnrGiveHmWrkSection.setSelection(0);

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
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], getActivity());
            spnrGiveHmWrkSubject.setPrompt("Choose Section");
            spnrGiveHmWrkSubject.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    class GetChapterName extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Plase wait fetching Chapters...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getChapterName));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_ClassId), strClassId);
                homeworkData.put(getString(R.string.key_SubjectId),strSubjectId);
                outObject.put(getString(R.string.key_homeworkData), homeworkData);
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
                            builder.setMessage("Chapters not Found");
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
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], getActivity());
            spnrGiveHmWrkSubject.setPrompt("Choose Section");
            spnrGiveHmWrkSubject.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
