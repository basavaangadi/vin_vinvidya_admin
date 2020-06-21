package com.vinuthana.vinvidyaadmin.fragments.examsectionfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.vinuthana.vinvidyaadmin.adapters.ExamSheduleRecycler;
import com.vinuthana.vinvidyaadmin.adapters.ExamSheduleRecyclerBelowKitKat;
import com.vinuthana.vinvidyaadmin.adapters.ExamSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class MyExamSchedule extends Fragment {

    Spinner spnrCurExmSchdlClass, spnrCurExmSchdlSection, spnrMyExmSchdlExam;
    Button btnGetExamSchdl;
    public SharedPreferences preferences;
    SharedPreferences.Editor edit;
    int roleId;
    String outString="",strExamScheduleId;
    JSONArray examScduleArray= new JSONArray();
    JSONArray examListArray =new JSONArray();
    private Session session;
    //RecyclerView recyclerViewExmSchByExam;
    String strStaffId, strExamId, strSchoolId, strClass, strSection, strClassId, strAcademicYearId;
    RecyclerView recyclerViewCurExmSch;
    ProgressDialog progressDialog;
    ExamSheduleRecycler recyclerAdapter;
    ExamSheduleRecyclerBelowKitKat recyclerBelowKitKat;

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
        return inflater.inflate(R.layout.fragment_current_exam_schedule, container, false);
    }

    public void init() {

        recyclerViewCurExmSch = (RecyclerView) getActivity().findViewById(R.id.recyclerViewCurExmSch);
        btnGetExamSchdl = (Button) getActivity().findViewById(R.id.btnGetExamSchdl);
        spnrMyExmSchdlExam = (Spinner) getActivity().findViewById(R.id.spnrMyExmSchdlExam);


        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {


            recyclerAdapter= new ExamSheduleRecycler(examScduleArray, getActivity());
            recyclerAdapter.setOnButtonClickListener(new ExamSheduleRecycler.OnExamSchduleClickListener() {
                @Override
                public void onEdit(JSONObject schduleData, int position, String examscduleId) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Would you like to Edit this exam schedule for the selected exam");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                strClassId=schduleData.getString(getString(R.string.key_ClassId));
                                Intent intent = new Intent(getActivity(), EditExamScduleActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ExamSchduleId", examscduleId);
                                bundle.putString("ClassId",strClassId);
                                bundle.putString("StaffId",strStaffId);
                                bundle.putString("ExamListArray", examListArray.toString());
                                bundle.putString("SchduleData",schduleData.toString());
                                Log.e("EmScdulId",examscduleId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                public void onDelete(int position, String ScheduleId) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Would you like to Delete this exam schedule");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            strExamScheduleId=ScheduleId;
                            new DeleteExamScdule().execute();
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
                public void onSyllabus(JSONObject schduleData,int position, String examscduleId) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Would you like to add exam syllabus for the selected exam");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                strClassId=schduleData.getString(getString(R.string.key_ClassId));
                                Intent intent = new Intent(getActivity(), SetExamSyllabusActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("classId", strClassId);
                                bundle.putString("SchduleData",schduleData.toString());

                                Log.e("EmScdulId",examscduleId);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
            recyclerViewCurExmSch.setLayoutManager(layoutManager);
            recyclerViewCurExmSch.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurExmSch.setAdapter(recyclerAdapter);



        }else{

            intiRecylerViewAdapter();


        }
    }
 @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        //if (getActivity() != null) {
            if (isVisibleToUser) {
                if(getActivity()!=null)
                {
                    new GetExam().execute();
                isFragmentLoaded = true;
            }
            }
        //}
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();


            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));

            init();
            allEvents();


        }
    }

    private void allEvents() {


        spnrMyExmSchdlExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrMyExmSchdlExam.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strExamId);
                //Toast.makeText(getActivity(), strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGetExamSchdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int exmPos=spnrMyExmSchdlExam.getSelectedItemPosition();
                if(exmPos==0){
                    Toast toast= Toast.makeText(getActivity(),"select exam before fetching the schedule",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=examScduleArray.length();i>=0;i--){

                            examScduleArray.remove(i);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }else{
                        examScduleArray= new JSONArray();
                        intiRecylerViewAdapter();
                    }
                }else{
                    new GetExamSchedule().execute();
                }

            }
        });
    }
    public void intiRecylerViewAdapter(){
        recyclerBelowKitKat= new ExamSheduleRecyclerBelowKitKat(examScduleArray, getActivity());
        recyclerBelowKitKat.setOnButtonClickListener(new ExamSheduleRecyclerBelowKitKat.OnExamSchduleClickListener() {
            @Override
            public void onEdit(JSONObject schduleData, int position, String examscduleId) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder.setMessage("Would you like to Edit this exam schedule for the selected exam");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            strClassId=schduleData.getString(getString(R.string.key_ClassId));
                            Intent intent = new Intent(getActivity(), EditExamScduleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ExamSchduleId", examscduleId);
                            bundle.putString("ClassId",strClassId);
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("ExamListArray", examListArray.toString());
                            bundle.putString("SchduleData",schduleData.toString());
                            Log.e("EmScdulId",examscduleId);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
            public void onDelete(int position, String ScheduleId) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder.setMessage("Would you like to Delete this exam schedule");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        strExamScheduleId=ScheduleId;
                        new DeleteExamScdule().execute();
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
            public void onSyllabus(JSONObject schduleData,int position, String examscduleId) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder.setMessage("Would you like to add exam syllabus for the selected exam");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            strClassId=schduleData.getString(getString(R.string.key_ClassId));
                            Intent intent = new Intent(getActivity(), SetExamSyllabusActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("classId", strClassId);
                            bundle.putString("SchduleData",schduleData.toString());

                            Log.e("EmScdulId",examscduleId);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        recyclerViewCurExmSch.setLayoutManager(layoutManager);
        recyclerViewCurExmSch.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCurExmSch.setAdapter(recyclerBelowKitKat);
    }

    class GetExamSchedule extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Exam Schedule...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
             for(int i=examScduleArray.length();i>=0;i--){

                        examScduleArray.remove(i);
                    }
            }else{
                examScduleArray= new JSONArray();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffexamSchdule));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_StaffId), strStaffId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_AcademicYearId),strAcademicYearId);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                   // publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                        JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                        for (int i=0;i<resultArray.length();i++){
                             examScduleArray.put(resultArray.getJSONObject(i));
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
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrMyExmSchdlExam.setSelection(-1);
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


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                recyclerAdapter.notifyDataSetChanged();
            }else{
                intiRecylerViewAdapter();
            }
            progressDialog.dismiss();
        }
    }


    class DeleteExamScdule extends AsyncTask<String, JSONArray, Void> {
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
                outObject.put(getString(R.string.key_OperationName), (getString(R.string.web_deleteExamSchedule)));
                JSONObject examData = new JSONObject();

                examData.put(getString(R.string.key_ExamScheduleId), strExamScheduleId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

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
            Log.e("Tag Outstring",outString);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCurExmSch.setLayoutManager(layoutManager);
            recyclerViewCurExmSch.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurExmSch.setAdapter(recyclerAdapter);
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
                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(strTitle.equalsIgnoreCase(getString(R.string.key_Success))){

                                getActivity().onBackPressed();

                            }
                        }
                    });
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getEnteredExamNameForScheduleOfStaff));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                examData.put(getString(R.string.key_StaffId), strStaffId);
                outObject.put(getString(R.string.key_examData), examData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {

                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        examListArray.put(resultArray.getJSONObject(i));
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
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrMyExmSchdlExam.setSelection(0);
                                }
                            });
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
            spnrMyExmSchdlExam.setPrompt("Choose Exam");
            spnrMyExmSchdlExam.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
