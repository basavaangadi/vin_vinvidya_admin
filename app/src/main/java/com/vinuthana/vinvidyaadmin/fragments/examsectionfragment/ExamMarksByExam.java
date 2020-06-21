package com.vinuthana.vinvidyaadmin.fragments.examsectionfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.examsection.AllSubjectActivityDemo;
import com.vinuthana.vinvidyaadmin.activities.examsection.AllSubjectExamMarksActivity;
import com.vinuthana.vinvidyaadmin.activities.examsection.ExamMarksList;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class ExamMarksByExam extends Fragment {
    Spinner spnrExmMarksByExamClass, spnrExmMarksByExamSection, spnrExmMarksByExam;
    Button btnExmMarksGetExmMrk;
    private Session session;
    String strExam, strSchoolId, strClass, strClassId, strSection, strExamId, strAcademicYearId;
    RecyclerView recyclerViewExmMarksByExm;
    ProgressDialog progressDialog;
    boolean isFragmentLoaded = false;
    CheckConnection connection = new CheckConnection();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_marks_by_exam, container, false);
    }

    public void init() {

        btnExmMarksGetExmMrk = (Button) getActivity().findViewById(R.id.btnExmMarksGetExmMrk);
        recyclerViewExmMarksByExm = (RecyclerView) getActivity().findViewById(R.id.recyclerViewExmMarksByExm);
        spnrExmMarksByExamClass = (Spinner) getActivity().findViewById(R.id.spnrExmMarksByExamClass);
        spnrExmMarksByExamSection = (Spinner) getActivity().findViewById(R.id.spnrExmMarksByExamSection);
        spnrExmMarksByExam = (Spinner) getActivity().findViewById(R.id.spnrExmMarksByExam);
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

    public void allEvents() {
        spnrExmMarksByExamClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmMarksByExamClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrExmMarksByExamSection.setSelection(0);
                    spnrExmMarksByExamSection.setAdapter(null);
                    spnrExmMarksByExam.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmMarksByExamSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmMarksByExamSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                strSection=tmpView.getText().toString();
                Log.e("Tag", "" + strClassId);

                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetExam().execute();
                }else{

                    spnrExmMarksByExam.setAdapter(null);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmMarksByExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmMarksByExam.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strExamId);
                strExam=tmpView.getText().toString();
                Log.e("Exam is ",strExam);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnExmMarksGetExmMrk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int clsPos = spnrExmMarksByExamClass.getSelectedItemPosition();
                int secPos = spnrExmMarksByExamSection.getSelectedItemPosition();

                int exmPos = spnrExmMarksByExam.getSelectedItemPosition();

                if ((clsPos == 0 || clsPos == -1) || (secPos == -1 || secPos == 0) || (exmPos == 0 || exmPos == -1)) {
                    Toast toast = Toast.makeText(getActivity(), "Select class,section and exam ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                       Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.key_SchoolId), strSchoolId);
                    bundle.putString(getString(R.string.key_ClassId), strClassId);
                    bundle.putString(getString(R.string.key_ExamId), strExamId);
                    bundle.putString(getString(R.string.key_Exam), strExam);
                    bundle.putString(getString(R.string.key_Class),strClass);
                    String strClassSection = strClass + strSection;
                    bundle.putString(getString(R.string.key_Cls_Section), strClassSection);
                    Log.e("beforeStart", strClassId + " and " + strExamId);
                   // if ((strSchoolId.equalsIgnoreCase("2") || strSchoolId.equalsIgnoreCase("13"))) {
                    if (!(strSchoolId.equalsIgnoreCase("3"))) {
                        Intent intent = new Intent(getActivity(), AllSubjectActivityDemo.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        //}else if(strSchoolId.equalsIgnoreCase("3")){
                        Intent intent = new Intent(getActivity(), AllSubjectExamMarksActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            init();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            allEvents();
            //new GetClass().execute();

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
            spnrExmMarksByExamClass.setPrompt("Choose Class");
            spnrExmMarksByExamClass.setAdapter(adapter);
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
            spnrExmMarksByExamSection.setPrompt("Choose Section");
            spnrExmMarksByExamSection.setAdapter(adapter);
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getEnteredExamNameForMarksOfClass));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
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
            spnrExmMarksByExam.setPrompt("Choose Exam");
            spnrExmMarksByExam.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
