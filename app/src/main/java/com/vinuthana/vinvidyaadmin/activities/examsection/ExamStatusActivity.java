package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamDetailsRecylerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamDetailsRecylerAdapterBelowKitkat;
import com.vinuthana.vinvidyaadmin.adapters.ExamSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ExamSyllabusRecycler;
import com.vinuthana.vinvidyaadmin.adapters.ExamSyllabusRecyclerBelowKitKat;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamStatusActivity extends AppCompatActivity {
    Spinner spnrExmStatusDetails,spnrExmStatusDetailsClass,spnrExmStatusDetailsSection;
    Button btnGetExmStatusDetails;
    JSONArray examDetailsArray = new JSONArray();
    JSONArray examListArray =new JSONArray();
    String strExamId,strSchoolId,strAcademicYearId,strClass,strClassId,strStaffId,strExam;
    ExamDetailsRecylerAdapter recyclerAdapter;
    RecyclerView recyclerViewExmDetails;
    Session session;
    String strIsScheduleSet,strIsSyllabusSet,strIsResultSet;
    ExamDetailsRecylerAdapterBelowKitkat recyclerBelowKitKat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_status);
        session = new Session(ExamStatusActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Status Details");
        ArrayList<String> strUser = new ArrayList<>();

        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

        strStaffId=user.get(Session.KEY_STAFFDETAILS_ID);
        initialisation();
        allEvents();

        new GetClass().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void initialisation(){
        spnrExmStatusDetails=findViewById(R.id.spnrExmStatusDetails);
        spnrExmStatusDetailsClass=findViewById(R.id.spnrExmStatusDetailsClass);
        spnrExmStatusDetailsSection=findViewById(R.id.spnrExmStatusDetailsSection);
        btnGetExmStatusDetails=findViewById(R.id.btnGetExmStatusDetails);
        recyclerViewExmDetails=findViewById(R.id.recyclerViewExmDetails);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            recyclerAdapter = new ExamDetailsRecylerAdapter(examDetailsArray, ExamStatusActivity.this);
            recyclerAdapter.setOnButtonClickListener(new ExamDetailsRecylerAdapter.OnExamDetailsClickListener() {
                @Override
                public void onSetSchedule(JSONObject examStatusData, int position) {
                    try {
                        strIsScheduleSet = examStatusData.getString("ScheduleSet");
                        strIsSyllabusSet = examStatusData.getString("SyllabusSet");
                        strIsResultSet = examStatusData.getString("ResultSet");

                        if (strIsScheduleSet.equalsIgnoreCase("1")) {
                            Toast toast = Toast.makeText(ExamStatusActivity.this, "Schedule already set", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else if (strIsScheduleSet.equalsIgnoreCase("-11")) {
                            Toast toast = Toast.makeText(ExamStatusActivity.this, "Schedule is deleted contact service provider to re-enable it", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                            alertDialogBuilder.setTitle("Confirmation");
                            alertDialogBuilder.setMessage("Would you like to Add the exam schedule for the selected exam");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        Intent intent = new Intent(ExamStatusActivity.this, SetExamSeduleByStatusActivity.class);
                                        Bundle bundle = new Bundle();

                                        bundle.putString("StaffId", strStaffId);
                                        bundle.putString("ExamId", strExamId);
                                        bundle.putString("Exam", strExam);
                                        bundle.putString("ExamStatusData", examStatusData.toString());
                                        Log.e("ExamStatusData", examStatusData.toString());
                                        intent.putExtras(bundle);
                                        dialog.cancel();
                                        startActivity(intent);


                                    } catch (Exception e) {
                                        Log.e("onSyllabus", e.toString());
                                        Toast.makeText(ExamStatusActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
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
                    }catch (Exception e){
                        Log.e(" schedule Error",e.toString());
                    }

                }

                @Override
                public void onSetSyllabus(JSONObject examStatusData, int position) {
                   // before method

                    try {
                        strIsScheduleSet = examStatusData.getString("ScheduleSet");
                        strIsSyllabusSet = examStatusData.getString("SyllabusSet");
                        if(strIsScheduleSet.equalsIgnoreCase("0")){
                            Toast toast= Toast.makeText(ExamStatusActivity.this,"set Schedule before setting syllabus",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }else if (strIsSyllabusSet.equalsIgnoreCase("1")){
                            Toast toast= Toast.makeText(ExamStatusActivity.this,"syllabus already set",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }else if (strIsSyllabusSet.equalsIgnoreCase("-11")||strIsScheduleSet.equalsIgnoreCase("-11")){
                            Toast toast= Toast.makeText(ExamStatusActivity.this,"Schedule or syllabus is deleted contact service provider to re-enable it",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                            alertDialogBuilder.setTitle("Confirmation");
                            alertDialogBuilder.setMessage("Would you like to Add the exam Syllabus for the selected exam");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                       dialog.cancel();
                                        Intent intent = new Intent(ExamStatusActivity.this, SetExamSyllabusByStatusActivity.class);
                                        Bundle bundle = new Bundle();

                                        bundle.putString("StaffId", strStaffId);
                                        bundle.putString("ExamId", strExamId);
                                        bundle.putString("Exam", strExam);
                                        bundle.putString("ExamStatusData", examStatusData.toString());
                                        Log.e("ExamStatusData", examStatusData.toString());
                                        intent.putExtras(bundle);
                                        dialog.cancel();
                                        startActivity(intent);

                                    } catch (Exception e) {
                                        Log.e(" onSyllabus", e.toString());
                                        Toast.makeText(ExamStatusActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
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
                    }catch(Exception e){
                        Log.e("Syllabus Error",e.toString());
                    }



                    /*try {
                        strIsScheduleSet = examStatusData.getString("ScheduleSet");
                        strIsSyllabusSet = examStatusData.getString("SyllabusSet");
                        if (strIsScheduleSet.equalsIgnoreCase("0")) {
                            Toast toast = Toast.makeText(ExamStatusActivity.this, "set Schedule before setting syllabus", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (strIsSyllabusSet.equalsIgnoreCase("1")) {
                            Toast toast = Toast.makeText(ExamStatusActivity.this, "syllabus already set", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                            alertDialogBuilder.setTitle("Confirmation");
                            alertDialogBuilder.setMessage("Would you like to Add the exam Syllabus for the selected exam");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        Intent intent = new Intent(ExamStatusActivity.this, SetExamSyllabusByStatusActivity.class);
                                        Bundle bundle = new Bundle();

                                        bundle.putString("StaffId", strStaffId);
                                        bundle.putString("ExamId", strExamId);
                                        bundle.putString("Exam", strExam);
                                        bundle.putString("ExamStatusData", examStatusData.toString());
                                        Log.e("ExamStatusData", examStatusData.toString());
                                        intent.putExtras(bundle);
                                       dialog.cancel();

                                        startActivity(intent);
                                        dialog.cancel();
                                    } catch (Exception e) {
                                        Log.e(" onSyllabus", e.toString());
                                        Toast.makeText(ExamStatusActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
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
                    }catch (Exception e){
                        Log.e("syllabus error",e.toString());
                    }*/

                }

                @Override
                public void onSetMarks(String strExamScheduleId,JSONObject marksData, int position) {

                    try {
                        Log.e("selected marksData", marksData.toString());
                        strIsResultSet = marksData.getString(getString(R.string.key_ResultSet));
                        strIsScheduleSet = marksData.getString("ScheduleSet");
                        strIsSyllabusSet = marksData.getString("SyllabusSet");
                        // strIsResultSet = marksData.getString("ResultSet");
                        if (strIsScheduleSet.equalsIgnoreCase("0")) {
                            Toast toast = Toast.makeText(ExamStatusActivity.this, "set Schedule before setting marks", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (strIsSyllabusSet.equalsIgnoreCase("0")) {
                            Toast toast = Toast.makeText(ExamStatusActivity.this, "set syllabus before setting marks", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else if (strIsSyllabusSet.equalsIgnoreCase("-11")||strIsScheduleSet.equalsIgnoreCase("-11")){
                            Toast toast= Toast.makeText(ExamStatusActivity.this,"schedule or syllabus is deleted",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        } else {
                            if (strIsResultSet.equalsIgnoreCase("2")) {
                                Toast toast = Toast.makeText(ExamStatusActivity.this, "Marks already entered", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else if (strIsResultSet.equalsIgnoreCase("1")) {
                                String strExamResultId = marksData.getString(getString(R.string.key_ExamResultId));
                                String strScholastics = marksData.getString(getString(R.string.key_Scholastics));
                                int examResultId = Integer.parseInt(strExamResultId);
                                if (examResultId > 0) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                                    alertDialogBuilder.setTitle("Alert");
                                    alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                                    alertDialogBuilder.setCancelable(true);
                                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent intent = new Intent(ExamStatusActivity.this, MarksListActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("ExamSceduleId", strExamScheduleId);
                                            bundle.putString("ClassId", strClassId);
                                            bundle.putString("StaffId", strStaffId);
                                            bundle.putString(getString(R.string.key_IsMarksSet), strIsResultSet);
                                            bundle.putString(getString(R.string.key_ExamResultId), strExamResultId);
                                            bundle.putString(getString(R.string.key_Scholastics), strScholastics);
                                            bundle.putString("SyllabusData", marksData.toString());
                                            Log.e("ExamSceduleId", strExamScheduleId);
                                            intent.putExtras(bundle);
                                            dialog.cancel();
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
                                } else {
                                    Toast toast = Toast.makeText(ExamStatusActivity.this, "Couldnt find the Schedule", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            } else if (strIsResultSet.equalsIgnoreCase("0")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                                alertDialogBuilder.setTitle("Alert");
                                alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                                alertDialogBuilder.setCancelable(true);
                                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent intent = new Intent(ExamStatusActivity.this, SetMaxMinMarksActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ExamSceduleId", strExamScheduleId);
                                        bundle.putString("ClassId", strClassId);
                                        bundle.putString("StaffId", strStaffId);
                                        bundle.putString(getString(R.string.key_IsMarksSet), strIsResultSet);
                                        bundle.putString("SyllabusData", marksData.toString());
                                        Log.e("ExamSyllabusId", strExamScheduleId);
                                        intent.putExtras(bundle);
                                        dialog.cancel();
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
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }





                }
            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ExamStatusActivity.this);
            recyclerViewExmDetails.setLayoutManager(layoutManager);
            recyclerViewExmDetails.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExmDetails.setAdapter(recyclerAdapter);
        }else{

            initRecylerAdapterBelow();
        }

    }

    public void initRecylerAdapterBelow(){
        recyclerBelowKitKat = new ExamDetailsRecylerAdapterBelowKitkat(examDetailsArray, ExamStatusActivity.this);
        recyclerBelowKitKat.setOnButtonClickListener(new ExamDetailsRecylerAdapterBelowKitkat.OnExamDetailsClickListener() {
            @Override
            public void onSetSchedule(JSONObject examStatusData, int position) {
                try {
                    strIsScheduleSet = examStatusData.getString("ScheduleSet");
                    strIsSyllabusSet = examStatusData.getString("SyllabusSet");
                    strIsResultSet = examStatusData.getString("ResultSet");

                if(strIsScheduleSet.equalsIgnoreCase("1")){
                        Toast toast= Toast.makeText(ExamStatusActivity.this,"Schedule already set",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else if(strIsScheduleSet.equalsIgnoreCase("-11")){
                    Toast toast= Toast.makeText(ExamStatusActivity.this,"Schedule is deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Would you like to Add the exam schedule for the selected exam");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                Intent intent = new Intent(ExamStatusActivity.this, SetExamSeduleByStatusActivity.class);
                                Bundle bundle = new Bundle();

                                bundle.putString("StaffId", strStaffId);
                                bundle.putString("ExamId", strExamId);
                                bundle.putString("Exam", strExam);
                                bundle.putString("ExamStatusData", examStatusData.toString());
                                Log.e("ExamStatusData", examStatusData.toString());
                                intent.putExtras(bundle);
                                dialog.dismiss();

                                startActivity(intent);

                            } catch (Exception e) {
                                Log.e("onSyllabus", e.toString());
                                Toast.makeText(ExamStatusActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
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

                }catch (Exception e){
                    Log.e("error",e.toString());
                }
            }

            @Override
            public void onSetSyllabus(JSONObject examStatusData, int position) {
                try {
                    strIsScheduleSet = examStatusData.getString("ScheduleSet");
                    strIsSyllabusSet = examStatusData.getString("SyllabusSet");
                    if(strIsScheduleSet.equalsIgnoreCase("0")){
                        Toast toast= Toast.makeText(ExamStatusActivity.this,"set Schedule before setting syllabus",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else if (strIsSyllabusSet.equalsIgnoreCase("1")){
                        Toast toast= Toast.makeText(ExamStatusActivity.this,"syllabus already set",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else if(strIsScheduleSet.equalsIgnoreCase("-11")||strIsSyllabusSet.equalsIgnoreCase("-11")){
                        Toast toast= Toast.makeText(ExamStatusActivity.this,"Schedule or syllabus is deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                        alertDialogBuilder.setTitle("Confirmation");
                        alertDialogBuilder.setMessage("Would you like to Add the exam Syllabus for the selected exam");
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    Intent intent = new Intent(ExamStatusActivity.this, SetExamSyllabusByStatusActivity.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putString("StaffId", strStaffId);
                                    bundle.putString("ExamId", strExamId);
                                    bundle.putString("Exam", strExam);
                                    bundle.putString("ExamStatusData", examStatusData.toString());
                                    Log.e("ExamStatusData", examStatusData.toString());
                                    intent.putExtras(bundle);
                                    dialog.dismiss();
                                    startActivity(intent);

                                } catch (Exception e) {
                                    Log.e(" onSyllabus", e.toString());
                                    Toast.makeText(ExamStatusActivity.this, "onSyllabus" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
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
                }catch(Exception e){
                    Log.e("Syllabus Error",e.toString());
                }
            }

            @Override
            public void onSetMarks(String strExamScheduleId,JSONObject marksData, int position) {


                try {
                    Log.e("selected marksData", marksData.toString());
                    strIsResultSet = marksData.getString(getString(R.string.key_ResultSet));
                    strIsScheduleSet = marksData.getString("ScheduleSet");
                    strIsSyllabusSet = marksData.getString("SyllabusSet");
                    // strIsResultSet = marksData.getString("ResultSet");
                    if (strIsScheduleSet.equalsIgnoreCase("0")) {
                        Toast toast = Toast.makeText(ExamStatusActivity.this, "set Schedule before setting marks", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (strIsSyllabusSet.equalsIgnoreCase("0")) {
                        Toast toast = Toast.makeText(ExamStatusActivity.this, "set syllabus before setting marks", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if(strIsScheduleSet.equalsIgnoreCase("-11")||strIsSyllabusSet.equalsIgnoreCase("-11")){
                        Toast toast= Toast.makeText(ExamStatusActivity.this,"Schedule or syllabus is deleted please contact service provider to re-enable it",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    } else {
                        if (strIsResultSet.equalsIgnoreCase("2")) {
                            Toast toast = Toast.makeText(ExamStatusActivity.this, "Marks already entered", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (strIsResultSet.equalsIgnoreCase("1")) {
                            String strExamResultId = marksData.getString(getString(R.string.key_ExamResultId));
                            String strScholastics = marksData.getString(getString(R.string.key_Scholastics));
                            int examResultId = Integer.parseInt(strExamResultId);
                            if (examResultId > 0) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                                alertDialogBuilder.setTitle("Alert");
                                alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                                alertDialogBuilder.setCancelable(true);
                                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent intent = new Intent(ExamStatusActivity.this, MarksListActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ExamSceduleId", strExamScheduleId);
                                        bundle.putString("ClassId", strClassId);
                                        bundle.putString("StaffId", strStaffId);
                                        bundle.putString(getString(R.string.key_IsMarksSet), strIsResultSet);
                                        bundle.putString(getString(R.string.key_ExamResultId), strExamResultId);
                                        bundle.putString(getString(R.string.key_Scholastics), strScholastics);
                                        bundle.putString("SyllabusData", marksData.toString());
                                        Log.e("ExamSceduleId", strExamScheduleId);
                                        intent.putExtras(bundle);
                                        dialog.dismiss();
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
                            } else {
                                Toast toast = Toast.makeText(ExamStatusActivity.this, "Couldnt find the Schedule", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        } else if (strIsResultSet.equalsIgnoreCase("0")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamStatusActivity.this);
                            alertDialogBuilder.setTitle("Alert");
                            alertDialogBuilder.setMessage("Would you like to Enter the marks for the selected exam");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(ExamStatusActivity.this, SetMaxMinMarksActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ExamSceduleId", strExamScheduleId);
                                    bundle.putString("ClassId", strClassId);
                                    bundle.putString("StaffId", strStaffId);
                                    bundle.putString(getString(R.string.key_IsMarksSet), strIsResultSet);
                                    bundle.putString("SyllabusData", marksData.toString());
                                    Log.e("ExamSyllabusId", strExamScheduleId);
                                    intent.putExtras(bundle);
                                    dialog.dismiss();
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
                    }
                    } catch(Exception e){
                        e.printStackTrace();
                    }

            }



        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ExamStatusActivity.this);
        recyclerViewExmDetails.setLayoutManager(layoutManager);
        recyclerViewExmDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewExmDetails.setAdapter(recyclerBelowKitKat);
    }
    public void allEvents(){

        spnrExmStatusDetailsClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmStatusDetailsClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(ExamStatusActivity.this, strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrExmStatusDetailsSection.setSelection(0);
                    spnrExmStatusDetailsSection.setAdapter(null);
                    spnrExmStatusDetails.setAdapter(null);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrExmStatusDetailsSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmStatusDetailsSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(ExamStatusActivity.this, strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetExam().execute();
                }
                else{
                    spnrExmStatusDetails.setSelection(0);
                    spnrExmStatusDetails.setAdapter(null);
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        
        spnrExmStatusDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrExmStatusDetails.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strExamId = adapterView.getItemAtPosition(i).toString();
                strExam =tmpView.getText().toString();
                //Toast.makeText(ExamStatusActivity.this, tmpView.getText().toString(), Toast.LENGTH_SHORT).show();
                Log.e("Tag", "" + strExamId);
                //Toast.makeText(ExamStatusActivity.this, strSection + " You Clicked on", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGetExmStatusDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clsPos=spnrExmStatusDetailsClass.getSelectedItemPosition();
                int secPos=spnrExmStatusDetailsSection.getSelectedItemPosition();
                int exmPos=spnrExmStatusDetails.getSelectedItemPosition();

                if((clsPos==0||clsPos==-1)||(secPos==-1||secPos==0)||(exmPos==0||exmPos==-1)){
                    Toast toast=Toast.makeText(ExamStatusActivity.this,"Select class,section and exam ",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=examDetailsArray.length();i>=0;i--){
                            examDetailsArray.remove(i);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }else{
                        examDetailsArray= new JSONArray();
                        initRecylerAdapterBelow();
                    }
                }else {
                    new GetExamDetailsNotEnteredList().execute();
                }



            }
        });
    }
    class GetExam extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "examsectionOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ExamStatusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching exam list...");
            progressDialog.show();
            for(int i=examListArray.length();i>=0;i--){

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    examListArray.remove(i);
                }


            }

        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                //outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getScheduleExamName));
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffExamName));
                JSONObject examData = new JSONObject();
                //examData.put(getString(R.string.key_ClassId), strClassId);
                examData.put(getString(R.string.key_SchoolId), strSchoolId);
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
                    ExamStatusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExamStatusActivity.this);
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
            ExamSpinnerDataAdapter adapter = new ExamSpinnerDataAdapter(values[0], ExamStatusActivity.this);
            spnrExmStatusDetails.setPrompt("Choose Exam");
            spnrExmStatusDetails.setAdapter(adapter);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetExamDetailsNotEnteredList extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "examsectionOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ExamStatusActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching class wise exam schedule...");
            progressDialog.show();

            for(int i = examDetailsArray.length(); i>=0; i--){

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    examDetailsArray.remove(i);
                }else{
                    examDetailsArray= new JSONArray();
                }



            }

        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), (getString(R.string.web_checkIsExamDetailsEntered)));
                JSONObject examData = new JSONObject();
                examData.put(getString(R.string.key_SchoolId), strSchoolId);
                examData.put(getString(R.string.key_ExamId), strExamId);
                examData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                examData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_examData), examData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                JSONObject inObject = new JSONObject(responseText);


                String strStatus = inObject.getString(getString(R.string.key_Status));

                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        examDetailsArray.put(resultArray.getJSONObject(i));
                    }
                } else {
                   ExamStatusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExamStatusActivity.this);
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
                ExamStatusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ExamStatusActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);

            /*outString=values[0].toString();
            Log.e("Tag Outstring",outString);*/

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                recyclerAdapter.notifyDataSetChanged();
            }else{
                initRecylerAdapterBelow();
            }

        }
    }
    class GetClass extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            progressDialog = new ProgressDialog(ExamStatusActivity.this);
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
                } /*else {
                    ExamStatusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExamStatusActivity.this);
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
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], ExamStatusActivity.this);
            spnrExmStatusDetailsClass.setPrompt("Choose Class");
            spnrExmStatusDetailsClass.setAdapter(adapter);
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
            progressDialog = new ProgressDialog(ExamStatusActivity.this);
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
                    ExamStatusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExamStatusActivity.this);
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
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], ExamStatusActivity.this);
            spnrExmStatusDetailsSection.setPrompt("Choose Section");
            spnrExmStatusDetailsSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
