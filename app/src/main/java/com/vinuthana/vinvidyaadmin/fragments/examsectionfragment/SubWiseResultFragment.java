package com.vinuthana.vinvidyaadmin.fragments.examsectionfragment;


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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.examsection.ExamMarksList;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;

import com.vinuthana.vinvidyaadmin.adapters.ExamSchdlSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ResultAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SubjectSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class SubWiseResultFragment extends Fragment {
    Button btnGetSubRslt;
    TextView tvExamScheduleForSub;
    Spinner spnrSubRsltClass, spnrSubRsltSection, spnrSubRsltExam, spnrSubRsltSubject, spnrSubRsltExmScdl;
    RecyclerView recyclerViewSubRslt;
    private Session session;
    int isworkExam;
    String strExam, strSchoolId, strClass, strSection, strStaffId, strClassId, strExamId, strSubjectId, strScheduleDate;
    String strDate, strAcademicYearId;
    ProgressDialog progressDialog;
    boolean isFragmentLoaded = false;
    JSONArray examArray=new JSONArray();
    //SubjectWiseExamResultExamAdapter adapter;
    CheckConnection connection = new CheckConnection();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sub_wise_result, container, false);



        return view;
    }

    public void init() {
        //btnGetCurExamMarks = (Button) getActivity().findViewById(R.id.btnGetCurExamMarks);

        recyclerViewSubRslt = (RecyclerView) getActivity().findViewById(R.id.recyclerViewSubRslt);
        spnrSubRsltClass = (Spinner) getActivity().findViewById(R.id.spnrSubRsltClass);
        spnrSubRsltSection = (Spinner) getActivity().findViewById(R.id.spnrSubRsltSection);
        spnrSubRsltExam = (Spinner) getActivity().findViewById(R.id.spnrSubRsltExam);
        spnrSubRsltSubject = (Spinner) getActivity().findViewById(R.id.spnrSubRsltSubject);
        spnrSubRsltExmScdl = (Spinner) getActivity().findViewById(R.id.spnrSubRsltExmScdl);
        btnGetSubRslt = (Button) getActivity().findViewById(R.id.btnGetSubRslt);
        tvExamScheduleForSub = (TextView) getActivity().findViewById(R.id.tvExamScheduleForSub);
    }

   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser) {
                new GetClass().execute();
                isFragmentLoaded = true;
            }
        }
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            init();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            new GetClass().execute();
            allEvents();


        }
    }

    private void allEvents() {
        spnrSubRsltClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSubRsltClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrSubRsltSection.setSelection(0);
                    spnrSubRsltSection.setAdapter(null);
                    spnrSubRsltExmScdl.setAdapter(null);
                    spnrSubRsltExam.setAdapter(null);
                    spnrSubRsltSubject.setAdapter(null);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSubRsltSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSubRsltSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag classID", "" + strClassId);
                //Toast.makeText(getActivity(), strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
                    int pos = adapterView.getSelectedItemPosition();
                    String strSection=tmpView.getText().toString();
                    if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                        new GetExam().execute();
                    }else{

                        spnrSubRsltExmScdl.setAdapter(null);
                        spnrSubRsltExam.setAdapter(null);
                        spnrSubRsltSubject.setAdapter(null);
                    }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSubRsltExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSubRsltExam.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag examId", "" + strExamId);
                int pos = adapterView.getSelectedItemPosition();

                strExam=tmpView.getText().toString();

                 isworkExam=isWorkBookExam(strExam);
                 if(isworkExam==1){
                     spnrSubRsltExmScdl.setVisibility(View.GONE);
                     tvExamScheduleForSub.setVisibility(View.GONE);
                     strScheduleDate="0001-01-01";
                 }else{
                     spnrSubRsltExmScdl.setVisibility(View.VISIBLE);
                     tvExamScheduleForSub.setVisibility(View.VISIBLE);
                 }
                if(!(strExam.equalsIgnoreCase("Select Exam")||pos==0)){
                    new GetSubject().execute();
                }else{

                    spnrSubRsltExmScdl.setAdapter(null);

                    spnrSubRsltSubject.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSubRsltSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSubRsltSubject.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag subjectId", "" + strSubjectId);
                int pos = adapterView.getSelectedItemPosition();
                String strSubject=tmpView.getText().toString();
                if(!(strSubject.equalsIgnoreCase("Select Subject")||pos==0||isworkExam==1)){
                    new GetResultExamSchedule().execute();
                }else{

                    spnrSubRsltExmScdl.setAdapter(null);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSubRsltExmScdl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSubRsltExmScdl.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strScheduleDate = adapterView.getItemAtPosition(i).toString();
                strDate = tmpView.getText().toString();
                Log.e("Tag scheduleDate ", "" + strScheduleDate);
                //new GetResultExamSchedule().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGetSubRslt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= null;
                int clsPos= spnrSubRsltClass.getSelectedItemPosition();
                int secPos= spnrSubRsltSection.getSelectedItemPosition();
                int subPos= spnrSubRsltSubject.getSelectedItemPosition();
                int exmPos= spnrSubRsltExam.getSelectedItemPosition();


                if(isworkExam==0){
                    int schPos= spnrSubRsltExmScdl.getSelectedItemPosition();
                    if((clsPos==-1||clsPos==0)||(secPos==-1||secPos==0)||(subPos==-1||subPos==0)||(exmPos==-1||exmPos==0)||(schPos==-1||schPos==0)){
                        Toast toast=Toast.makeText(getActivity(),"Select Class,Section,Exam and Subject,Schedule ",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else  {
                        Intent intent = new Intent(getActivity(), ExamMarksList.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.key_SchoolId), strSchoolId);
                        bundle.putString(getString(R.string.key_ClassId), strClassId);
                        bundle.putString(getString(R.string.key_ExamId), strExamId);
                        bundle.putString(getString(R.string.key_SubjectId), strSubjectId);
                        bundle.putString(getString(R.string.key_ExamSchduleSetDate), strDate);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }else if(isworkExam==1){

                    if((clsPos==-1||clsPos==0)||(secPos==-1||secPos==0)||(subPos==-1||subPos==0)||(exmPos==-1||exmPos==0)){
                        Toast toast=Toast.makeText(getActivity(),"Select Class,Section,Exam and Subject ",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else  {
                        strDate="0001-01-01";
                        Intent intent = new Intent(getActivity(), ExamMarksList.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.key_SchoolId), strSchoolId);
                        bundle.putString(getString(R.string.key_ClassId), strClassId);
                        bundle.putString(getString(R.string.key_ExamId), strExamId);
                        bundle.putString(getString(R.string.key_SubjectId), strSubjectId);
                        bundle.putString(getString(R.string.key_ExamSchduleSetDate), strDate);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                }
            }
        });
    }
    public int isWorkBookExam(String strExam){
        int isWorkBookExam=0;
        for(int i=0;i<examArray.length();i++){
            try{
                JSONObject object= examArray.getJSONObject(i);
                String strResExamName=object.getString(getString(R.string.key_Exam));
                if(strResExamName.equalsIgnoreCase(strExam)){
                     String strIsWorkBook=object.getString("IsWorkBookExam");
                     isWorkBookExam=Integer.parseInt(strIsWorkBook);
                     break;
                }
            }catch (Exception e){
                Log.e("IswokBookExam",e.toString());
            }

        }
        return isWorkBookExam;
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
            spnrSubRsltClass.setPrompt("Choose Class");
            spnrSubRsltClass.setAdapter(adapter);
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
            progressDialog.setMessage("Plase wait fetching Sections...");
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
            spnrSubRsltSection.setPrompt("Choose Section");
            spnrSubRsltSection.setAdapter(adapter);
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
            progressDialog.setMessage("Plase wait fetching Exams...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                //outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getEnteredExamNameForMarksOfClass));
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getEnteredExamNameForMarksOfSubject));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray result = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < result.length(); i++) {
                        examArray.put(result.getJSONObject(i));
                    }
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // spnrSubRsltExam.getSelectedItem() =null;
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrSubRsltExam.setSelection(-1);
                                    spnrSubRsltExmScdl.setSelection(-1);
                                    spnrSubRsltSubject.setSelection(-1);
                                }
                            });
                            builder.setTitle("Alert");
                            builder.setMessage("Exam not Found");
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
            spnrSubRsltExam.setPrompt("Choose Exam");
            spnrSubRsltExam.setAdapter(adapter);


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
            progressDialog.setMessage("Plase wait fetching Subjects...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getMarksEnteredSubjects));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                classSubjectData.put(getString(R.string.key_ExamId), strExamId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {

                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

                }else {
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

                                    spnrSubRsltExmScdl.setSelection(-1);
                                    spnrSubRsltSubject.setSelection(-1);
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
            SubjectSpinnerDataAdapter adapter = new SubjectSpinnerDataAdapter(values[0], getActivity());
            spnrSubRsltSubject.setPrompt("Choose Section");
            spnrSubRsltSubject.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetResultExamSchedule extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());

            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Plase wait fetching Schedule...");
            progressDialog.show();
        }
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
                }else {
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
                                    spnrSubRsltExmScdl.setSelection(-1);
                                }
                            });
                            builder.setTitle("Alert");
                            builder.setMessage("Couldn't find the Schedule");
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
            ExamSchdlSpinnerAdapter adapter = new ExamSchdlSpinnerAdapter(values[0], getActivity());
            spnrSubRsltExmScdl.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetSubjectwiseResult extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getSubjectwiseResult));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_SubjectId), strSubjectId);
                examData.put(getString(R.string.key_ExamSchduleSetDate), strDate);
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
            ResultAdapter recyclerViewAdapter = new ResultAdapter(values[0],strSchoolId ,getActivity());
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewSubRslt.setLayoutManager(prntNtLytMngr);
            recyclerViewSubRslt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSubRslt.setAdapter(recyclerViewAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
