package com.vinuthana.vinvidyaadmin.fragments.otherfragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.vinuthana.vinvidyaadmin.activities.otheractivities.ViewFeesCollectedStudWise;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.FeesTypeSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentListSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.YearSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class StudentWiseFeesFragment extends Fragment {
    Button btnSubmit;
    Spinner spnrYear,spnrFeesType,spnrClass,spnrSection,spnrStudNames;
    String strSchId,strClass="",strSchoolId="",strClassId="",strStudentId="",strAcademicYearId="",strfees_type_Id="",strFeeType="";
    RecyclerView rcvwAdmissionFees;
    Session session;
    //String url="http://192.168.43.155:8080/AdmissionFeesFragment/fees/admissionfees.jsp";

    public StudentWiseFeesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_student_wise, container, false);

        btnSubmit=view.findViewById(R.id.btnSubmit);
        spnrYear=view.findViewById(R.id.spnrYear);
        spnrFeesType=view.findViewById(R.id.spnrFeesType);
        spnrClass=view.findViewById(R.id.spnrClass);
        spnrSection=view.findViewById(R.id.spnrSection);
        spnrStudNames=view.findViewById(R.id.spnrStudNames);
        rcvwAdmissionFees=view.findViewById(R.id.rcvwAdmissionFees);

        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();

        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);


        new GetYear().execute();


        spnrYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = spnrYear.getSelectedView().findViewById(R.id.list);
                strAcademicYearId = parent.getItemAtPosition(position).toString();
                String strSection = textView.getText().toString();
                if (!(strSection.equalsIgnoreCase("Select Year"))) {
                    new GetFeesType().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrFeesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = spnrFeesType.getSelectedView().findViewById(R.id.list);
                strfees_type_Id = parent.getItemAtPosition(position).toString();
                String strfees_type_Id = textView.getText().toString();
                strFeeType= textView.getText().toString();
                if (!(strfees_type_Id.equalsIgnoreCase("Select FeesType"))) {
                    new GetClass().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spnrClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView= spnrClass.getSelectedView().findViewById(R.id.list);
                strClass =parent.getItemAtPosition(position).toString();
                String strClass =textView.getText().toString();
                //strClassSec =textView.getText().toString();
                if(!(strClass.equalsIgnoreCase("Select Class"))){
                    new GetSection().execute("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spnrSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = spnrSection.getSelectedView().findViewById(R.id.list);
                strClassId = parent.getItemAtPosition(position).toString();
                String strSection = textView.getText().toString();
                if (!(strSection.equalsIgnoreCase("Select Section"))) {
                    new GetStudentList().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnrStudNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                TextView textView= spnrStudNames.getSelectedView().findViewById(R.id.list);
                strStudentId = adapterView.getItemAtPosition(i).toString();
                String strStudentId =textView.getText().toString();
                if(!(strStudentId.equalsIgnoreCase("Select Student"))){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewFeesCollectedStudWise.class);
                Bundle bundle= new Bundle();
                bundle.putString("StudentId",strStudentId);
                bundle.putString("Admission Fees",strFeeType);
                //bundle.putString("StudentId",strFeeType);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        return view;
    }

    class GetYear extends AsyncTask<String, JSONArray, Void> {
        String url ="http://192.168.43.155:8080/netvinvidyawebapi/operation/otherOperation.jsp";

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("please wait while we fetch your Data");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName", "GetYear");
                JSONObject otherData = new JSONObject();
                otherData.put("SchoolId", strSchoolId);
                outObject.put("otherData", otherData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is", e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            YearSpinnerDataAdapter adapter = new YearSpinnerDataAdapter(values[0], getActivity());
            spnrYear.setPrompt("Choose Year");
            spnrYear.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetFeesType extends AsyncTask<String, JSONArray, Void> {
        String url ="http://192.168.43.155:8080/netvinvidyawebapi/operation/otherOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("please wait while we fetch your Data");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName", "GetFeesType");
                JSONObject otherData = new JSONObject();
                otherData.put("SchoolId", strSchoolId);
                outObject.put("otherData", otherData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is", e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            FeesTypeSpinnerDataAdapter adapter = new FeesTypeSpinnerDataAdapter(values[0], getActivity());
            spnrFeesType.setPrompt("Choose Fees Type");
            spnrFeesType.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }




    class GetClass extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("please wait while we fetch your Data");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName",getString(R.string.web_Staff_ClassNames));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put("SchoolId", strSchoolId);
                classSubjectData.put("AcademicYearId", strAcademicYearId);
                outObject.put("classSubjectData", classSubjectData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is", e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrClass.setPrompt("Choose Class");
            spnrClass.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetSection extends AsyncTask<String, JSONArray,Void> {
        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("please wait while we fetch your Data");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put("OperationName",getString(R.string.web_Staff_Class_With_Section));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put("class", strClass);
                classSubjectData.put("SchoolId", strSchoolId);
                classSubjectData.put("AcademicYearId", strAcademicYearId);
                outObject.put("classSubjectData", classSubjectData);
                Log.e("outObject is ", outObject.toString());
                String strRespText = response.getServerResopnse(url, outObject.toString());
                Log.e("Response is", strRespText);
                JSONObject inObject = new JSONObject(strRespText);
                String strStatus = inObject.getString("Status");
                if (strStatus.equalsIgnoreCase("Success")) {
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is", e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], getActivity());
            spnrSection.setPrompt("Choose section");
            spnrSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

    }

    class GetStudentList  extends AsyncTask<String, JSONArray,Void> {
        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching Current StudentList...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response= new GetResponse();
            JSONObject outObject= new JSONObject();

            try {
                outObject.put("OperationName",getString(R.string.web_getClasswiseStudentsList));
                JSONObject classSubjectData= new JSONObject();
                classSubjectData.put("SchoolId",strSchoolId);
                classSubjectData.put("AcademicYearId",strAcademicYearId);
                classSubjectData.put("classId",strClassId);
                outObject.put("classSubjectData",classSubjectData);
                Log.e("outObject is ",outObject.toString());
                String strRespText=response.getServerResopnse(url,outObject.toString());
                Log.e("Response is",strRespText);
                JSONObject inObject=new JSONObject(strRespText);
                String strStatus=inObject.getString("Status");

                if(strStatus.equalsIgnoreCase("Success")){
                    publishProgress(new JSONObject(strRespText).getJSONArray("Result"));
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast=Toast.makeText(getContext(),"Data not found",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Exception is",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            StudentListSpinnerAdapter adapter = new StudentListSpinnerAdapter(values[0], getActivity());
            spnrStudNames.setPrompt("Choose StudNames");
            spnrStudNames.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
    }



}
