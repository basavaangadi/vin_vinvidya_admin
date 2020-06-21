package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditStudentReminderActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.StudentReminderActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReminderRecylcerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReminderRecylcerViewAdapterBelowKitkat;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentListSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class StudReminderByDate extends Fragment {

    EditText edtStudRmndByDtFrmDt, edtStudRmndByDtToDt;
    DatePickerDialog datePickerDialogFrmDt, datePickerDialogToDt;
    Spinner spnrRmndrStudList, spnrStuRmndClass, spnrStuRmndSection;
    Calendar noticeFrmDt = Calendar.getInstance();
    Calendar noticeToDt = Calendar.getInstance();
    String strNoticeToDt, strNoticeFrmDt;
    String strDate;
    JSONArray reminderArray=new JSONArray();
    JSONArray studentListArray=new JSONArray();
    View view;
    Button btnRmndrStudByDate;
    RecyclerView recyclerViewRmndrStudByDate;
    private Session session;
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    ProgressDialog progressDialog;
    ReminderRecylcerViewAdapter reminderAdapter;
    ReminderRecylcerViewAdapterBelowKitkat reminderAdapterBelowKitkat;
    int roleId;
    String strStaffId, strSchoolId, strClass, strStatus, strClassId, strStudentId, strFrmDate;
    String strReminderId, strTodate, strAcademicYearId;
    boolean isFragmentLoaded = false;
    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtStudRmndByDtFrmDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFrmDt.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtStudRmndByDtToDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogToDt.dismiss();
        }
    };
    private int rmdToYear, rmdToMonth, rmdToDay, rmdFrmYear, rmdFrmMonth, rmdFrmDay;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stud_reminder_by_date, container, false);

        rmdFrmYear = noticeFrmDt.get(Calendar.YEAR);
        rmdFrmMonth = noticeFrmDt.get(Calendar.MONTH);
        rmdFrmDay = noticeFrmDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFrmDt = new DatePickerDialog(getActivity(), nlistner, rmdFrmYear, rmdFrmMonth, rmdFrmDay);
        //datePickerDialogFrmDt.getDatePicker().setMaxDate(System.currentTimeMillis());


        rmdToYear = noticeToDt.get(Calendar.YEAR);
        rmdToMonth = noticeToDt.get(Calendar.MONTH);
        rmdToDay = noticeToDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogToDt = new DatePickerDialog(getActivity(), mlistner, rmdToYear, rmdToMonth, rmdToDay);
        //datePickerDialogToDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            //new GetClass().execute();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            strStaffId=user.get(Session.KEY_STAFFDETAILS_ID);
            init();
            if (getActivity() != null){
                new GetClass().execute();
            }
            allEvents();
        }
    }

    public void init() {

        recyclerViewRmndrStudByDate = (RecyclerView) getActivity().findViewById(R.id.recyclerViewRmndrStudByDate);
        edtStudRmndByDtToDt = (EditText) getActivity().findViewById(R.id.edtStudRmndByDtToDt);
        edtStudRmndByDtFrmDt = (EditText) getActivity().findViewById(R.id.edtStudRmndByDtFrmDt);
        btnRmndrStudByDate = (Button) getActivity().findViewById(R.id.btnRmndrStudByDate);
        spnrRmndrStudList = (Spinner) getActivity().findViewById(R.id.spnrRmndrStudList);
        spnrStuRmndSection = (Spinner) getActivity().findViewById(R.id.spnrStuRmndSection);
        spnrStuRmndClass = (Spinner) getActivity().findViewById(R.id.spnrStuRmndClass);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            reminderAdapter = new ReminderRecylcerViewAdapter(reminderArray, getActivity(),roleId);
            reminderAdapter.setOnButtonClickListner(new ReminderRecylcerViewAdapter.OnReminderViewClickListener() {
                @Override
                public void onEdit(JSONObject ReminderData, int position, String reminderId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit this Student Reminder ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(getActivity(), EditStudentReminderActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putString("ReminderData",ReminderData.toString());
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("AcademicYearId",strAcademicYearId);
                            bundle.putString("ClassId",strClassId);
                            bundle.putString("StudentList",studentListArray.toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog= builder.create();
                    dailog.show();
                }

                @Override
                public void onDelete(int position, String reminderId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to Delete this Staff Reminder ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            strReminderId=reminderId;
                            new DeleteStudentReminderId().execute();
                            Log.e("u clicked onDelete",String.valueOf(position));
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog= builder.create();
                    dailog.show();
                }
            });
            RecyclerView.LayoutManager rmdrLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewRmndrStudByDate.setLayoutManager(rmdrLytMngr);
            recyclerViewRmndrStudByDate.setItemAnimator(new DefaultItemAnimator());
            recyclerViewRmndrStudByDate.setAdapter(reminderAdapter);

        }else{
            initRmdrAdptrBlwKitKat();


        }


    }

    private void allEvents() {

        edtStudRmndByDtFrmDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtStudRmndByDtFrmDt) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });

        edtStudRmndByDtToDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtStudRmndByDtToDt) {
                    datePickerDialogToDt.show();
                    return true;
                }
                return false;
            }
        });

        spnrStuRmndClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrStuRmndClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                        spnrStuRmndSection.setAdapter(null);
                        spnrRmndrStudList.setAdapter(null);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrStuRmndSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (strStatus.equals("Failure")) {
                    spnrStuRmndSection.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrStuRmndSection.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    strClassId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "" + strClassId);
                    //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                    int pos = adapterView.getSelectedItemPosition();
                    String strSection=tmpView.getText().toString();
                    if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                        new GetStudentList().execute();
                    }else{
                        spnrRmndrStudList.setAdapter(null);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrRmndrStudList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spnrRmndrStudList == null) {
                    spnrRmndrStudList.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrRmndrStudList.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    strClassId = adapterView.getItemAtPosition(i).toString();
                    strStudentId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "" + strClassId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRmndrStudByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new GetReminderByDate().execute();

                if (edtStudRmndByDtFrmDt.getText().toString().equals("") || edtStudRmndByDtToDt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "From Date and  To Date can't be blank", Toast.LENGTH_LONG).show();
                } else {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        strFrmDate = edtStudRmndByDtFrmDt.getText().toString();
                        strTodate = edtStudRmndByDtToDt.getText().toString();
                        Date fromDate = sdf.parse(strFrmDate);
                        Date toDate = sdf.parse(strTodate);
                        int clsPos = spnrStuRmndClass.getSelectedItemPosition();
                        int secPos = spnrStuRmndSection.getSelectedItemPosition();

                        if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)){
                            Toast toast= Toast.makeText(getActivity(),"select Class, section and student ",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }else if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        } else {
                            new GetReminderByDate().execute();
                        }
                    } catch (Exception e) {
                        Log.e("btnOnclick", e.toString());
                    }
                }
            }
        });
    }

    public void initRmdrAdptrBlwKitKat(){

        reminderAdapterBelowKitkat = new ReminderRecylcerViewAdapterBelowKitkat(reminderArray, getActivity(),roleId);
        reminderAdapterBelowKitkat.setOnButtonClickListner(new ReminderRecylcerViewAdapterBelowKitkat.OnReminderViewClickListener() {
            @Override
            public void onEdit(JSONObject ReminderData, int position, String reminderId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to edit this Student Reminder ?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), EditStudentReminderActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("ReminderData",ReminderData.toString());
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("StaffId",strStaffId);
                        bundle.putString("AcademicYearId",strAcademicYearId);
                        bundle.putString("ClassId",strClassId);
                        bundle.putString("StudentList",studentListArray.toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dailog= builder.create();
                dailog.show();
            }

            @Override
            public void onDelete(int position, String reminderId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to Delete this Staff Reminder ?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strReminderId=reminderId;
                        new DeleteStudentReminderId().execute();
                        Log.e("u clicked onDelete",String.valueOf(position));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dailog= builder.create();
                dailog.show();
            }
        });
        RecyclerView.LayoutManager rmdrLytMngr = new LinearLayoutManager(getActivity());
        recyclerViewRmndrStudByDate.setLayoutManager(rmdrLytMngr);
        recyclerViewRmndrStudByDate.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRmndrStudByDate.setAdapter(reminderAdapterBelowKitkat);
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
                            builder.setMessage("Classes not Found");
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
            spnrStuRmndClass.setPrompt("Choose Class");
            spnrStuRmndClass.setAdapter(adapter);
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
                            builder.setMessage("Sections not Found");
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
            spnrStuRmndSection.setPrompt("Choose Section");
            spnrStuRmndSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Student list...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseStudentsList));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage=inObject.getString(getString(R.string.key_Message));
                String strRollNotSet=inObject.getString(getString(R.string.key_Roll_Set));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    studentListArray = new JSONArray();
                    for(int i=0;i<resultArray.length();i++){
                        studentListArray.put(resultArray.getJSONObject(i));
                    }

                }else if(strRollNotSet.equalsIgnoreCase("1")){
                    showRollNoStatus(strStatus,strMessage);
                }
                else {
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
            StudentListSpinnerAdapter adapter = new StudentListSpinnerAdapter(values[0], getActivity());
            spnrRmndrStudList.setPrompt("Choose Section");
            spnrRmndrStudList.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        public void showRollNoStatus(String strTitle, String strMessage) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(false);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(getActivity(), StudentReminderActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });

        }


    }

    private class GetReminderByDate extends AsyncTask<String, JSONArray, String> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Current Reminder...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                for (int i = reminderArray.length(); i >= 0; i--) {

                    reminderArray.remove(i);
                }


            } else {
                reminderArray = new JSONArray();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_studentReminderDisplayByDate));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StudentId), strStudentId);
                noticeBoardData.put(getString(R.string.key_fromDate), strFrmDate);
                noticeBoardData.put(getString(R.string.key_toDate), strTodate);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetReminderByDate,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetReminderByDate,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        reminderArray.put(resultArray.getJSONObject(i));
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                reminderAdapter.notifyDataSetChanged();
            }else{
                initRmdrAdptrBlwKitKat();
            }


        }
    }
    class DeleteStudentReminderId extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog= new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Deleting the Student reminder...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = AD.url.base_url + "noticeBoardOperation.jsp";
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteStudentReminder));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_Id), strReminderId);

                outObject.put("noticeBoardData", noticeBoardData);
                Log.e("TAG", "Delete Staff Reminder,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "Delete Staff Reminder,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strMessage="";
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                    strMessage=inObject.getString(getString(R.string.key_Message));
                    showAlert(strMessage,strStatus);
                    //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    showAlert("Something went wrong Try again","Error");
                }

                /*JSONArray result = inObject.getJSONArray(getString(R.string.key_Result));
                publishProgress(result);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        private void showAlert(String alertMessage, String alertTitle) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(alertMessage);
                    builder.setTitle(alertTitle);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }
}
