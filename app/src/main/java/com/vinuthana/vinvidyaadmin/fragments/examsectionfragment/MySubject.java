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
import com.vinuthana.vinvidyaadmin.activities.examsection.EditExamScduleActivity;
import com.vinuthana.vinvidyaadmin.activities.examsection.EditExamSyllabusActivity;
import com.vinuthana.vinvidyaadmin.activities.examsection.MarksListActivity;
import com.vinuthana.vinvidyaadmin.activities.examsection.SetMaxMinMarksActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamSyllabusRecycler;
import com.vinuthana.vinvidyaadmin.adapters.ExamSyllabusRecyclerBelowKitKat;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MySubject extends Fragment {
    Spinner spnrCurExmSyllabusClass, spnrCurExmSyllabusSection, spnrMySubExm;
    Button btnCurExmSyllabusSetExam;
    RecyclerView recyclerViewCurExmSylbs;
    private Session session;
    ExamSyllabusRecycler recyclerAdapter;
    ExamSyllabusRecyclerBelowKitKat recyclerAdapterBelowKitkat;
    JSONArray exmSyllabusArray= new JSONArray();
    String strStaffId, strExam, strSchoolId, strClass, strExamId,outString;
    String strRoleId, strClassId, strAcademicYearId,strExamSyllabusId;
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

        return inflater.inflate(R.layout.fragment_current_exam_syllabus, container, false);
    }

    public void init() {

        recyclerViewCurExmSylbs = (RecyclerView) getActivity().findViewById(R.id.recyclerViewCurExmSylbs);

        spnrMySubExm = (Spinner) getActivity().findViewById(R.id.spnrMySubExm);
        btnCurExmSyllabusSetExam = (Button) getActivity().findViewById(R.id.btnCurExmSyllabusSetExam);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            recyclerAdapter = new ExamSyllabusRecycler(exmSyllabusArray, getActivity());

            recyclerAdapter.setOnButtonClickListener(new ExamSyllabusRecycler.OnExamSyllabusClickListener() {
                @Override
                public void onEdit(JSONObject syllabusData, int position, String examsylbusId) {
                    try {
                        strClassId = syllabusData.getString(getString(R.string.key_ClassId));
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Alert");
                        alertDialogBuilder.setMessage("Would you like to Edit this exam schedule for the selected exam");
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(getActivity(), EditExamSyllabusActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ExamSyllabusId", examsylbusId);
                                bundle.putString("ClassId", strClassId);
                                bundle.putString("StaffId", strStaffId);
                                // bundle.putString("ExamListArray", examListArray.toString());
                                bundle.putString("SyllabusData", syllabusData.toString());
                                Log.e("ExamSyllabusId", examsylbusId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                        });
                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onDelete(int position, String examsylbusId) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Alert");
                    alertDialogBuilder.setMessage("Would you like to Delete this exam schedule");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            strExamSyllabusId=examsylbusId;
                            new DeleteExamSyllabus().execute();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                @Override
                public void onSetMarks(JSONObject syllabusData, int position, String examScheduleId) {
                    try {
                        Log.e("selected syllabus",syllabusData.toString());
                        String strIsMarksSet = syllabusData.getString(getString(R.string.key_IsMarksSet));
                        strClassId = syllabusData.getString(getString(R.string.key_ClassId));

                        if(strIsMarksSet.equalsIgnoreCase("2")){
                            Toast toast=Toast.makeText(getActivity(),"Marks already entered",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }else if(strIsMarksSet.equalsIgnoreCase("1")){
                            String strExamResultId = syllabusData.getString(getString(R.string.key_ExamResultId));
                            String strScholastics = syllabusData.getString(getString(R.string.key_Scholastics));
                            int examResultId=Integer.parseInt(strExamResultId);
                            if(examResultId>0){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setTitle("Alert");
                                alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                                alertDialogBuilder.setCancelable(true);
                                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent intent = new Intent(getActivity(), MarksListActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ExamSceduleId", examScheduleId);
                                        bundle.putString("ClassId", strClassId);
                                        bundle.putString("StaffId", strStaffId);
                                        bundle.putString(getString(R.string.key_IsMarksSet), strIsMarksSet);
                                        bundle.putString(getString(R.string.key_ExamResultId), strExamResultId);
                                        bundle.putString(getString(R.string.key_Scholastics), strScholastics);
                                        bundle.putString("SyllabusData", syllabusData.toString());
                                        Log.e("ExamSyllabusId", examScheduleId);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    }
                                });
                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }else{
                                Toast toast= Toast.makeText(getActivity(),"Couldnt find the Schedule",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }

                        }else if(strIsMarksSet.equalsIgnoreCase("0")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Alert");
                            alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(getActivity(), SetMaxMinMarksActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ExamSceduleId", examScheduleId);
                                    bundle.putString("ClassId", strClassId);
                                    bundle.putString("StaffId", strStaffId);
                                    bundle.putString(getString(R.string.key_IsMarksSet), strIsMarksSet);
                                    bundle.putString("SyllabusData", syllabusData.toString());
                                    Log.e("ExamSyllabusId", examScheduleId);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });
                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    } catch (Exception e) {
                        Log.e("onMarksSet",e.toString());
                    }
                }

            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCurExmSylbs.setLayoutManager(layoutManager);
            recyclerViewCurExmSylbs.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurExmSylbs.setAdapter(recyclerAdapter);

        }else{
            initRecylerBelowKitkat();

        }


    }

    public void initRecylerBelowKitkat(){

        recyclerAdapterBelowKitkat = new ExamSyllabusRecyclerBelowKitKat(exmSyllabusArray, getActivity());
        recyclerAdapterBelowKitkat.setOnButtonClickListener(new ExamSyllabusRecyclerBelowKitKat.OnExamSyllabusClickListener() {
            @Override
            public void onEdit(JSONObject syllabusData, int position, String examsylbusId) {
                try {
                strClassId = syllabusData.getString(getString(R.string.key_ClassId));
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Alert");
                alertDialogBuilder.setMessage("Would you like to Edit this exam schedule for the selected exam");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(getActivity(), EditExamSyllabusActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ExamSyllabusId", examsylbusId);
                            bundle.putString("ClassId",strClassId);
                            bundle.putString("StaffId",strStaffId);
                            // bundle.putString("ExamListArray", examListArray.toString());
                            bundle.putString("SyllabusData",syllabusData.toString());
                            Log.e("ExamSyllabusId",examsylbusId);
                            intent.putExtras(bundle);
                            startActivity(intent);

                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                } catch (Exception e) {
                    Log.e("onEdit",e.toString());
                }
            }

            @Override
            public void onSetMarks(JSONObject syllabusData, int position, String examScheduleId) {
                try {
                    Log.e("selected syllabus",syllabusData.toString());
                    String strIsMarksSet = syllabusData.getString(getString(R.string.key_IsMarksSet));
                     strClassId = syllabusData.getString(getString(R.string.key_ClassId));

                    if(strIsMarksSet.equalsIgnoreCase("2")){
                        Toast toast=Toast.makeText(getActivity(),"Marks already entered",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else if(strIsMarksSet.equalsIgnoreCase("1")){
                        String strExamResultId = syllabusData.getString(getString(R.string.key_ExamResultId));
                        String strScholastics = syllabusData.getString(getString(R.string.key_Scholastics));
                        int examResultId=Integer.parseInt(strExamResultId);
                        if(examResultId>0){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Alert");
                            alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(getActivity(), MarksListActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ExamSceduleId", examScheduleId);
                                    bundle.putString("ClassId", strClassId);
                                    bundle.putString("StaffId", strStaffId);
                                    bundle.putString(getString(R.string.key_IsMarksSet), strIsMarksSet);
                                    bundle.putString(getString(R.string.key_ExamResultId), strExamResultId);
                                    bundle.putString(getString(R.string.key_Scholastics), strScholastics);
                                    bundle.putString("SyllabusData", syllabusData.toString());
                                    Log.e("ExamSyllabusId", examScheduleId);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });
                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }else{
                            Toast toast= Toast.makeText(getActivity(),"Couldnt find the Schedule",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }

                    }else if(strIsMarksSet.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Alert");
                        alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(getActivity(), SetMaxMinMarksActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ExamSceduleId", examScheduleId);
                                bundle.putString("ClassId", strClassId);
                                bundle.putString("StaffId", strStaffId);
                                bundle.putString(getString(R.string.key_IsMarksSet), strIsMarksSet);
                                bundle.putString("SyllabusData", syllabusData.toString());
                                Log.e("ExamSyllabusId", examScheduleId);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    Log.e("onMarksSet",e.toString());
                }
            }







            @Override
            public void onDelete(int position, String examsylbusId) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Alert");
                alertDialogBuilder.setMessage("Would you like to Delete this exam syllabus");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        strExamSyllabusId=examsylbusId;
                        new DeleteExamSyllabus().execute();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCurExmSylbs.setLayoutManager(layoutManager);
        recyclerViewCurExmSylbs.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCurExmSylbs.setAdapter(recyclerAdapterBelowKitkat);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            session = new Session(getContext());
            HashMap<String, String> user = session.getUserDetails();

            ArrayList<String> strUser = new ArrayList<>();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strRoleId=user.get(Session.KEY_ROLE_ID);
            init();
            allEvents();
            new GetExam().execute();
        }
    }

    private void allEvents() {


        spnrMySubExm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrMySubExm.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strExamId);
                //Toast.makeText(getActivity(), strSection + " You Clicked on", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCurExmSyllabusSetExam.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                int exmPos=spnrMySubExm.getSelectedItemPosition();
                if(exmPos==-1||exmPos==0){
                    Toast toast=Toast.makeText(getActivity(), "select Exam", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    new GetCurExamSyllabus().execute();
                }

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
                  //  publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        exmSyllabusArray.put(resultArray.getJSONObject(i));
                    }

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
            spnrCurExmSyllabusClass.setPrompt("Choose Class");
            spnrCurExmSyllabusClass.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class DeleteExamSyllabus extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Deleting  exam schedule...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), (getString(R.string.web_deleteExamSyllabus)));
                JSONObject examData = new JSONObject();

                examData.put(getString(R.string.key_ExamSyllabusId), strExamSyllabusId);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage=inObject.getString(getString(R.string.key_Message));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    showAlert(strStatus,strMessage);
                } else {
                    showAlert("Error","Error");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

            outString=values[0].toString();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
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
                    builder.setNegativeButton("ok", null);
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });
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
            spnrCurExmSyllabusSection.setPrompt("Choose Section");
            spnrCurExmSyllabusSection.setAdapter(adapter);
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getEnteredExamNameForSyllabusOfStaff));
                JSONObject examData = new JSONObject();
               // examData.put(getString(R.string.key_SchoolId), strSchoolId);
                examData.put(getString(R.string.key_StaffId), strStaffId);
                examData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
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
                            builder.setMessage("Exam not found");
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
            spnrMySubExm.setPrompt("Choose Exam");
            spnrMySubExm.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetCurExamSyllabus extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("please wait Fetching Subject...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for (int i = exmSyllabusArray.length(); i >= 0; i--) {

                    exmSyllabusArray.remove(i);
                }
            } else {
                exmSyllabusArray = new JSONArray();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamSyllabus));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StaffId), strStaffId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    // publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

                    JSONArray resultArray = inObject.getJSONArray(getString(R.string.key_Result));
                    for (int i = 0; i < resultArray.length(); i++) {
                        exmSyllabusArray.put(resultArray.getJSONObject(i));
                    }

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

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                recyclerAdapter.notifyDataSetChanged();
            } else {
                initRecylerBelowKitkat();
              }
            }
        }

}
