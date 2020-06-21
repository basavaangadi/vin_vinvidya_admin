package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.MotionEvent;
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
import com.vinuthana.vinvidyaadmin.activities.dayatoday.EditHomeWorkActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.SubjectSyllabusActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.HmWrkRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.HmWrkRecyclerViewAdapterAbvKitKat;
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
import java.util.Date;
import java.util.HashMap;


public class ViewHomeWorkByDate extends Fragment {
    DatePickerDialog datePickerDialog,datePickerDialogToDate;
    Calendar viewHmFbByDt = Calendar.getInstance();
    Calendar viewHmFbByDtTo = Calendar.getInstance();
    private int mYear, mMonth, mDay,toYear,toMonth,toDay;
    String strToYear,strToMonth,strToDay;
    JSONArray subjectArray;

    Button btnViewHmWrkByDate;
    Spinner spnrViewHmWrkClass, spnrViewHmWrkSection, spnrViewHmWrkByDtSubject;
    EditText edtViewHmWrkByFromDate,edtViewHmWrkByToDate;
    Spinner spnrViewCurHmWrkClass, spnrViewCurHmWrkSection;
    private Session session;
    View view;
    RecyclerView recyclerViewHmByDt;
    String strStaffId, strExam, strSchoolId, strClass, strSection, strSubjectId;
     String  strHomeworkId,strClassId, strAcademicYearId,strDate, strToDate;
    ProgressDialog progressDialog;

    JSONArray array;
    CheckConnection connection = new CheckConnection();
    ArrayList<String> classArrayList= new ArrayList<String>();
    ArrayList<String>sectionArrayList= new ArrayList<String>();
    ArrayList<String>subjectArrayList= new ArrayList<String>();
    HmWrkRecyclerViewAdapterAbvKitKat recyclerAdapterAbvKitKat;
    HmWrkRecyclerViewAdapter recyclerAdapter;
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtViewHmWrkByFromDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int toYear, int tomonth, int toDayOfMonth) {
            edtViewHmWrkByToDate.setText(toDayOfMonth + "-" + String.valueOf(tomonth + 1) + "-" + toYear);
            datePickerDialogToDate.dismiss();
        }
    };
    boolean isFragmentLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init() {
        session = new Session(getContext());
        btnViewHmWrkByDate = (Button) getActivity().findViewById(R.id.btnViewHmWrkByDate);
        edtViewHmWrkByFromDate = (EditText) getActivity().findViewById(R.id.edtViewHmWrkByFromDate);
        edtViewHmWrkByToDate = (EditText) getActivity().findViewById(R.id.edtViewHmWrkByToDate);
        spnrViewHmWrkClass = (Spinner) getActivity().findViewById(R.id.spnrViewHmWrkByDtClass);
        spnrViewHmWrkSection = (Spinner) getActivity().findViewById(R.id.spnrViewHmWrkByDtSection);
        spnrViewHmWrkByDtSubject = (Spinner) getActivity().findViewById(R.id.spnrViewHmWrkByDtSubject);
        recyclerViewHmByDt = (RecyclerView) getActivity().findViewById(R.id.recyclerViewHmByDt);
        array = new JSONArray();

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            recyclerAdapterAbvKitKat = new HmWrkRecyclerViewAdapterAbvKitKat(array, getActivity(),ViewHomeWorkByDate.this);

            recyclerAdapterAbvKitKat.setOnButtonClickListener(new HmWrkRecyclerViewAdapterAbvKitKat.OnHomworkViewClickListener() {

                @Override
                public void onEdit(JSONObject currentData, int position, int hid) {
                    Log.e("TAG_ON_BTN_CLICKED", "onEdit: Hid:=> "+hid+" JSONObject:=> "+currentData+" Position of click:=> "+position);


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// Add the buttons
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do You want edit the homework ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent editHomework= new Intent(getActivity(), EditHomeWorkActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("HomeworkId",String.valueOf(hid));
                            bundle.putString("currentData",currentData.toString());
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("AcademicYearId",strAcademicYearId);
                            bundle.putString("ClassList",classArrayList.toString());
                            bundle.putString("SectionList",sectionArrayList.toString());
                            bundle.putString("SubjectList",subjectArray.toString());
                            editHomework.putExtras(bundle);
                            Log.e("Before navigation List",subjectArray.toString());
                            startActivity(editHomework);

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    ;


                }

                @Override
                public void onDelete(int hid, int position) {

                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to delete the homework ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("TAG_ON_BTN_CLICKED", "onDelete: Hid:=> "+hid+" Position of click:=> "+position);
                            strHomeworkId=String.valueOf(hid);
                            new DeleteHomework().execute();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }


                @Override
                public void onFeed(int hid, int position,int classId) {

                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    Toast.makeText(getActivity(),String.valueOf(classId),Toast.LENGTH_LONG);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to give the feedback for  this homework ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("TAG_ON_BTN_CLICKED", "onFeed: Hid:=> "+hid+" Position of click:=> "+position);
                            strHomeworkId=String.valueOf(hid);
                            strClassId=String.valueOf(classId);
                            Bundle bundle= new Bundle();
                            bundle.putString("HomeworkId",strHomeworkId);
                            bundle.putString("ClassId",strClassId);
                            Intent feedbackIntent= new Intent(getActivity(),GiveHmWrkFbActivity.class);
                            feedbackIntent.putExtras(bundle);
                            startActivity(feedbackIntent);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                @Override
                public void onSubjectSyllabus(JSONObject currentData, int position, int classId, String strSubject, int subjectId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    Toast.makeText(getActivity(),String.valueOf(classId),Toast.LENGTH_LONG);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you like to navigate Syllabus of this  Subject ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Log.e("TAG_ON_BTN_CLICKED", "onSubjectSyllabus:  Position of click:=> " + position);
                                String strClass = currentData.getString("Class");
                                strClassId = String.valueOf(classId);
                                Bundle bundle = new Bundle();
                                bundle.putString("Class", strClass);
                                bundle.putString("ClassId", strClassId);
                                bundle.putString("Subject", strSubject);
                                bundle.putString("SubjectId", String.valueOf(subjectId));
                                Intent feedbackIntent = new Intent(getActivity(), SubjectSyllabusActivity.class);
                                feedbackIntent.putExtras(bundle);
                                startActivity(feedbackIntent);
                            }catch (Exception e){
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewHmByDt.setLayoutManager(layoutManager);
            recyclerViewHmByDt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewHmByDt.setAdapter(recyclerAdapterAbvKitKat);
        }

        else {
            initRecylerViewAdapter();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_home_work_by_date, container, false);
        // Inflate the layout for this fragment
        return view;
    }
    public void initRecylerViewAdapter(){
        recyclerAdapter = new HmWrkRecyclerViewAdapter(array, getActivity(),ViewHomeWorkByDate.this);

        recyclerAdapter.setOnButtonClickListener(new HmWrkRecyclerViewAdapter.OnHomworkViewClickListener() {

            @Override
            public void onEdit(JSONObject currentData, int position, int hid) {
                Log.e("TAG_ON_BTN_CLICKED", "onEdit: Hid:=> "+hid+" JSONObject:=> "+currentData+" Position of click:=> "+position);


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// Add the buttons
                builder.setTitle("Confirmation");
                builder.setMessage("Do You want edit the homework ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent editHomework= new Intent(getActivity(), EditHomeWorkActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("HomeworkId",String.valueOf(hid));
                        bundle.putString("currentData",currentData.toString());
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("StaffId",strStaffId);
                        bundle.putString("AcademicYearId",strAcademicYearId);
                        bundle.putString("ClassList",classArrayList.toString());
                        bundle.putString("SectionList",sectionArrayList.toString());
                        bundle.putString("SubjectList",subjectArray.toString());
                        editHomework.putExtras(bundle);
                        Log.e("Before navigation List",subjectArray.toString());
                        startActivity(editHomework);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                ;


            }

            @Override
            public void onDelete(int hid, int position) {

                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to delete the homework ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("TAG_ON_BTN_CLICKED", "onDelete: Hid:=> "+hid+" Position of click:=> "+position);
                        strHomeworkId=String.valueOf(hid);
                        new DeleteHomework().execute();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }


            @Override
            public void onFeed(int hid, int position,int classId) {

                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                Toast.makeText(getActivity(),String.valueOf(classId),Toast.LENGTH_LONG);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to give the feedback for  this homework ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("TAG_ON_BTN_CLICKED", "onFeed: Hid:=> "+hid+" Position of click:=> "+position);
                        strHomeworkId=String.valueOf(hid);
                        strClassId=String.valueOf(classId);
                        Bundle bundle= new Bundle();
                        bundle.putString("HomeworkId",strHomeworkId);
                        bundle.putString("ClassId",strClassId);
                        Intent feedbackIntent= new Intent(getActivity(),GiveHmWrkFbActivity.class);
                        feedbackIntent.putExtras(bundle);
                        startActivity(feedbackIntent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public void onSubjectSyllabus(JSONObject currentData, int position, int classId, String strSubject, int subjectId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                Toast.makeText(getActivity(),String.valueOf(classId),Toast.LENGTH_LONG);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you like to navigate Syllabus of this  Subject ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Log.e("TAG_ON_BTN_CLICKED", "onSubjectSyllabus:  Position of click:=> " + position);
                            String strClass = currentData.getString("Class");
                            strClassId = String.valueOf(classId);
                            Bundle bundle = new Bundle();
                            bundle.putString("Class", strClass);
                            bundle.putString("ClassId", strClassId);
                            bundle.putString("Subject", strSubject);
                            bundle.putString("SubjectId", String.valueOf(subjectId));
                            Intent feedbackIntent = new Intent(getActivity(), SubjectSyllabusActivity.class);
                            feedbackIntent.putExtras(bundle);
                            startActivity(feedbackIntent);
                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHmByDt.setLayoutManager(layoutManager);
        recyclerViewHmByDt.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHmByDt.setAdapter(recyclerAdapter);
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
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strStaffId=user.get(Session.KEY_STAFFDETAILS_ID);
            mYear = viewHmFbByDt.get(Calendar.YEAR);
            mMonth = viewHmFbByDt.get(Calendar.MONTH);
            mDay = viewHmFbByDt.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);

            toYear = viewHmFbByDtTo.get(Calendar.YEAR);
            toMonth = viewHmFbByDtTo.get(Calendar.MONTH);
            toDay = viewHmFbByDtTo.get(Calendar.DAY_OF_MONTH);

            datePickerDialogToDate = new DatePickerDialog(getActivity(), nlistner, toYear, toMonth, toDay);
            if (getActivity() != null) {

                new GetClass().execute();

            }
            //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void allEvents() {
        edtViewHmWrkByFromDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtViewHmWrkByFromDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        edtViewHmWrkByToDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtViewHmWrkByToDate) {
                    datePickerDialogToDate.show();
                    return true;
                }
                return false;
            }
        });

        btnViewHmWrkByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clsPos= spnrViewHmWrkClass.getSelectedItemPosition();
                int secPos= spnrViewHmWrkSection.getSelectedItemPosition();
                int subPos= spnrViewHmWrkByDtSubject.getSelectedItemPosition();
                if((clsPos==0||clsPos==-1)||(secPos==-1||secPos==0)||(subPos==0||subPos==-1)){
                    Toast toast=Toast.makeText(getActivity(),"Select class,section and subject",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=array.length();i>=0;i--){

                            array.remove(i);
                        }
                        recyclerAdapterAbvKitKat.notifyDataSetChanged();
                    }else{
                        array= new JSONArray();
                        initRecylerViewAdapter();
                    }

                }
               else if (edtViewHmWrkByFromDate.getText().toString().length()<3||edtViewHmWrkByToDate.getText().toString().length()<3) {
                    Toast toast=Toast.makeText(getActivity(),"Date can't be blank",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        for(int i=array.length();i>=0;i--){

                            array.remove(i);
                        }
                        recyclerAdapterAbvKitKat.notifyDataSetChanged();
                    }else{
                        array= new JSONArray();
                        initRecylerViewAdapter();
                    }

                    //Toast.makeText(getActivity(), "Date can't be blank", Toast.LENGTH_LONG).show();
                }
                else {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        strDate = edtViewHmWrkByFromDate.getText().toString();
                        strToDate = edtViewHmWrkByToDate.getText().toString();
                        Date fromDate = sdf.parse(strDate);
                        Date toDate=sdf.parse(strToDate);
                    if(fromDate.after(toDate)){
                        Toast toast=Toast.makeText(getActivity(),"From date should be less then To date",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            for(int i=array.length();i>=0;i--){

                                array.remove(i);
                            }
                            recyclerAdapterAbvKitKat.notifyDataSetChanged();
                        }else{
                            array= new JSONArray();
                            initRecylerViewAdapter();
                        }
                        //Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                    }else {
                        new GetHomeWorkByDate().execute();
                    }
                    }catch (Exception e){
                        Log.e("btnOnclick",e.toString());
                    }
                }
            }
        });

        spnrViewHmWrkClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrViewHmWrkClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrViewHmWrkSection.setSelection(0);
                    spnrViewHmWrkSection.setAdapter(null);
                    spnrViewHmWrkByDtSubject.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrViewHmWrkSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrViewHmWrkSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetSubject().execute();
                }else{
                    spnrViewHmWrkByDtSubject.setSelection(0);
                    spnrViewHmWrkByDtSubject.setAdapter(null);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrViewHmWrkByDtSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrViewHmWrkByDtSubject.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strSubjectId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strSubjectId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class GetHomeWorkByDate extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Home Work...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
             for(int i=array.length();i>=0;i--){

                 array.remove(i);
              }
            }else{
                array= new JSONArray();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_homeworkDisplayClasswiseByDate));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_ClassId), strClassId);
                homeworkData.put(getString(R.string.key_SubjectId), strSubjectId);
                homeworkData.put(getString(R.string.key_FromDate), strDate);
                homeworkData.put(getString(R.string.key_ToDate), strToDate);
                outObject.put(getString(R.string.key_homeworkData), homeworkData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray result = new JSONObject(responseText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < result.length(); i++) {
                        array.put(result.getJSONObject(i));
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
                recyclerAdapterAbvKitKat.notifyDataSetChanged();
            }else{
                initRecylerViewAdapter();


               /* recyclerAdapter = new HmWrkRecyclerViewAdapter(array, getActivity(),ViewHomeWorkByDate.this);

                recyclerAdapter.setOnButtonClickListener(new HmWrkRecyclerViewAdapter.OnHomworkViewClickListener() {

                    @Override
                    public void onEdit(JSONObject currentData, int position, int hid) {
                        Log.e("TAG_ON_BTN_CLICKED", "onEdit: Hid:=> "+hid+" JSONObject:=> "+currentData+" Position of click:=> "+position);


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// Add the buttons
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do You want edit the homework ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent editHomework= new Intent(getActivity(), EditHomeWorkActivity.class);
                                Bundle bundle = new Bundle();

                                bundle.putString("HomeworkId",String.valueOf(hid));
                                bundle.putString("currentData",currentData.toString());
                                bundle.putString("SchoolId",strSchoolId);
                                bundle.putString("StaffId",strStaffId);
                                bundle.putString("AcademicYearId",strAcademicYearId);
                                bundle.putString("ClassList",classArrayList.toString());
                                bundle.putString("SectionList",sectionArrayList.toString());
                                bundle.putString("SubjectList",subjectArray.toString());
                                editHomework.putExtras(bundle);
                                Log.e("Before navigation List",subjectArray.toString());
                                startActivity(editHomework);

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        ;


                    }

                    @Override
                    public void onDelete(int hid, int position) {

                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to delete the homework ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("TAG_ON_BTN_CLICKED", "onDelete: Hid:=> "+hid+" Position of click:=> "+position);
                                strHomeworkId=String.valueOf(hid);
                                new DeleteHomework().execute();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }


                    @Override
                    public void onFeed(int hid, int position,int classId) {

                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        Toast.makeText(getActivity(),String.valueOf(classId),Toast.LENGTH_LONG);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to give the feedback for  this homework ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("TAG_ON_BTN_CLICKED", "onFeed: Hid:=> "+hid+" Position of click:=> "+position);
                                strHomeworkId=String.valueOf(hid);
                                strClassId=String.valueOf(classId);
                                Bundle bundle= new Bundle();
                                bundle.putString("HomeworkId",strHomeworkId);
                                bundle.putString("ClassId",strClassId);
                                Intent feedbackIntent= new Intent(getActivity(),GiveHmWrkFbActivity.class);
                                feedbackIntent.putExtras(bundle);
                                startActivity(feedbackIntent);
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }

                    @Override
                    public void onSubjectSyllabus(JSONObject currentData, int position, int classId, String strSubject, int subjectId) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        Toast.makeText(getActivity(),String.valueOf(classId),Toast.LENGTH_LONG);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you like to navigate Syllabus of this  Subject ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Log.e("TAG_ON_BTN_CLICKED", "onSubjectSyllabus:  Position of click:=> " + position);
                                    String strClass = currentData.getString("Class");
                                    strClassId = String.valueOf(classId);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Class", strClass);
                                    bundle.putString("ClassId", strClassId);
                                    bundle.putString("Subject", strSubject);
                                    bundle.putString("SubjectId", String.valueOf(subjectId));
                                    Intent feedbackIntent = new Intent(getActivity(), SubjectSyllabusActivity.class);
                                    feedbackIntent.putExtras(bundle);
                                    startActivity(feedbackIntent);
                                }catch (Exception e){
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });


                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewHmByDt.setLayoutManager(layoutManager);
                recyclerViewHmByDt.setItemAnimator(new DefaultItemAnimator());
                recyclerViewHmByDt.setAdapter(recyclerAdapter);*/
            }



        }
    }

    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
            spnrViewHmWrkClass.setPrompt("Choose Class");
            spnrViewHmWrkClass.setAdapter(adapter);
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
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spnrViewHmWrkSection.setSelection(0);
                                    spnrViewHmWrkByDtSubject.setSelection(-1);
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
            SectionSpinnerDataAdapter adapter = new SectionSpinnerDataAdapter(values[0], getActivity());
            spnrViewHmWrkSection.setPrompt("Choose Section");
            spnrViewHmWrkSection.setAdapter(adapter);
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
                    subjectArray =inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<subjectArray.length();i++){
                        String sub=subjectArray.getJSONObject(i).getString("Subject");
                        subjectArrayList.add(sub);
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
                                    spnrViewHmWrkSection.setSelection(0);
                                    spnrViewHmWrkByDtSubject.setSelection(0);
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
            spnrViewHmWrkByDtSubject.setPrompt("Choose Section");
            spnrViewHmWrkByDtSubject.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class DeleteHomework extends AsyncTask<String, JSONArray, Void>{
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "homeworkOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Deleting the Home Work...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteHomework));
                JSONObject homeworkData = new JSONObject();
                homeworkData.put(getString(R.string.key_HomeworkId), strHomeworkId);
                outObject.put(getString(R.string.key_homeworkData), homeworkData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                String strMsg=inObject.getString(getString(R.string.key_Message));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        /*if (strMsg.equalsIgnoreCase("Homework deleted sucessfully..")) {
                            alertMessage = strMsg;
                            alertTitle = "Success";
                        }*/
                        if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                            alertMessage = strMsg;
                            alertTitle = strStatus;
                        }/*else if (strMsg.equalsIgnoreCase("Homework couldn't be deleted..")) {
                            alertMessage = strMsg;
                            alertTitle = "Failure";
                        }*/ else {
                            alertTitle = "Error";
                            alertMessage = "Something went wrong While editing Home Work..";
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
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
                spnrViewHmWrkClass.setSelection(0);
                spnrViewHmWrkSection.setSelection(0);
                spnrViewHmWrkByDtSubject.setSelection(0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
