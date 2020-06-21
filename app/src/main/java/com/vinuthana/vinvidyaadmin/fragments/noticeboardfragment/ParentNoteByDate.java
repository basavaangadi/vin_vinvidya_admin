package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditParentsNoteActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ParentNoteActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PrntNtRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PrntNtRecyclerViewAdapterBelowKitKat;
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


public class ParentNoteByDate extends Fragment {
    Spinner spnrParentNtByDtClass, spnrParentNtByDtSection, spnrParentNtByDtStudName;
    Button btnGetParentNtByDt;
    String strClass, strClassId, strSchoolId, strFrmDt, strToDt, strStatus, strAcademicYearId;
    String strStaffId,strNoticeId,strStudentId;
    private Session session;
    RecyclerView recyclerViewParentNtByDt;
    EditText edtParentNtByDtFrmDt, edtParentNtByDtToDt;
    DatePickerDialog datePickerDialogFrmDt, datePickerDialogToDt;
    View view;
    GetResponse response = new GetResponse();
    ProgressDialog progressDialog;
    JSONObject outObject = new JSONObject();
    Calendar noticeFrmDt = Calendar.getInstance();
    Calendar noticeToDt = Calendar.getInstance();
    boolean isFragmentLoaded = false;
    JSONArray prntArray= new JSONArray();
    JSONArray studentArrayList= new JSONArray();
    PrntNtRecyclerViewAdapter parentsNoteAdapter;
    PrntNtRecyclerViewAdapterBelowKitKat parentsNoteAdapterBelowKitKat;
    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtParentNtByDtFrmDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFrmDt.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edtParentNtByDtToDt.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
            datePickerDialogToDt.dismiss();
        }
    };
    private int noticeToYear, noticeToMonth, noticeToDay, noticeFrmYear, noticeFrmMonth, noticeFrmDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_note_by_date, container, false);

        noticeFrmYear = noticeFrmDt.get(Calendar.YEAR);
        noticeFrmMonth = noticeFrmDt.get(Calendar.MONTH);
        noticeFrmDay = noticeFrmDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFrmDt = new DatePickerDialog(getActivity(), mlistner, noticeFrmYear, noticeFrmMonth, noticeFrmDay);
        //datePickerDialogFrmDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        noticeToYear = noticeToDt.get(Calendar.YEAR);
        noticeToMonth = noticeToDt.get(Calendar.MONTH);
        noticeToDay = noticeToDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogToDt = new DatePickerDialog(getActivity(), nlistner, noticeToYear, noticeToMonth, noticeToDay);
        //datePickerDialogToDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        return view;
    }

    public void init() {

        btnGetParentNtByDt = getActivity().findViewById(R.id.btnGetParentNtByDt);
        spnrParentNtByDtClass = getActivity().findViewById(R.id.spnrParentNtByDtClass);
        spnrParentNtByDtSection = getActivity().findViewById(R.id.spnrParentNtByDtSection);
        spnrParentNtByDtStudName = getActivity().findViewById(R.id.spnrParentNtByDtStudName);
        edtParentNtByDtFrmDt = getActivity().findViewById(R.id.edtParentNtByDtFrmDt);
        edtParentNtByDtToDt = getActivity().findViewById(R.id.edtParentNtByDtToDt);
        recyclerViewParentNtByDt = getActivity().findViewById(R.id.recyclerViewParentNtByDt);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            parentsNoteAdapter = new PrntNtRecyclerViewAdapter(prntArray, getActivity());
            parentsNoteAdapter.setOnButtonClickListener(new PrntNtRecyclerViewAdapter.OnNoticeViewClickListener() {
                @Override
                public void onEdit(JSONObject noticeData, int position, String noticeId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit this Parent's note ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(getActivity(), EditParentsNoteActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putString("NoticeData",noticeData.toString());
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("AcademicYearId",strAcademicYearId);
                            bundle.putString("StudentList",studentArrayList.toString());
                            bundle.putString("ClassId",strClassId);
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
                public void onDelete(int position, String noticeId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do want to Delete this Parent's note?.. ");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            strNoticeId=noticeId;
                            new DeleteParentsNote().execute();
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
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewParentNtByDt.setLayoutManager(prntNtLytMngr);
            recyclerViewParentNtByDt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewParentNtByDt.setAdapter(parentsNoteAdapter);

        }else{
            initparentNoteAdapterBelowKitkat();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            session = new Session(getActivity());
            //new GetClass().execute();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strStaffId=user.get(Session.KEY_STAFFDETAILS_ID);
            init();

            if (getActivity() != null) {

                new GetClass().execute();

            }
            allEvents();
        }
    }

    private void allEvents() {

        edtParentNtByDtFrmDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtParentNtByDtFrmDt) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });

        edtParentNtByDtToDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtParentNtByDtToDt) {
                    datePickerDialogToDt.show();
                    return true;
                }
                return false;
            }
        });

        spnrParentNtByDtClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrParentNtByDtClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrParentNtByDtStudName.setAdapter(null);
                    spnrParentNtByDtSection.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrParentNtByDtSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrParentNtByDtSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                String strSection=tmpView.getText().toString();
                if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                    new GetStudentList().execute();
                }else{
                    spnrParentNtByDtStudName.setAdapter(null);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrParentNtByDtStudName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrParentNtByDtStudName.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
               // strClassId = adapterView.getItemAtPosition(i).toString();
                strStudentId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGetParentNtByDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtParentNtByDtFrmDt.getText().toString().equalsIgnoreCase("") || edtParentNtByDtToDt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "From Date and  To Date can't be blank", Toast.LENGTH_LONG).show();
                }else {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        strFrmDt = edtParentNtByDtFrmDt.getText().toString();
                        strToDt = edtParentNtByDtToDt.getText().toString();
                        int clsPos = spnrParentNtByDtClass.getSelectedItemPosition();
                        int secPos = spnrParentNtByDtSection.getSelectedItemPosition();

                        Date fromDate = sdf.parse(strFrmDt);
                        Date toDate = sdf.parse(strToDt);
                        if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)) {
                            Toast toast = Toast.makeText(getActivity(), "select Class, section and student ", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            new GetParentNoteByDate().execute();
                        }
                    } catch (Exception e) {
                        Log.e("btnOnclick", e.toString());
                    }
                }

            }
        });
    }

    public  void initparentNoteAdapterBelowKitkat(){
        parentsNoteAdapterBelowKitKat = new PrntNtRecyclerViewAdapterBelowKitKat(prntArray, getActivity());
        parentsNoteAdapterBelowKitKat.setOnButtonClickListener(new PrntNtRecyclerViewAdapterBelowKitKat.OnNoticeViewClickListener() {
            @Override
            public void onEdit(JSONObject noticeData, int position, String noticeId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to edit this Parent's note ?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), EditParentsNoteActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("NoticeData",noticeData.toString());
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("StaffId",strStaffId);
                        bundle.putString("AcademicYearId",strAcademicYearId);
                        bundle.putString("StudentList",studentArrayList.toString());
                        bundle.putString("ClassId",strClassId);
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
            public void onDelete(int position, String noticeId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do want to Delete this Parent's note?.. ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strNoticeId=noticeId;
                        new DeleteParentsNote().execute();
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
        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        recyclerViewParentNtByDt.setLayoutManager(prntNtLytMngr);
        recyclerViewParentNtByDt.setItemAnimator(new DefaultItemAnimator());
        recyclerViewParentNtByDt.setAdapter(parentsNoteAdapterBelowKitKat);
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
            spnrParentNtByDtClass.setPrompt("Choose Class");
            spnrParentNtByDtClass.setAdapter(adapter);
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
            progressDialog.setMessage("Please wait fetching Classes...");
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
            spnrParentNtByDtSection.setPrompt("Choose Section");
            spnrParentNtByDtSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Students...");
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
                String strStatus = inObject.getString(getString(R.string.key_Status));
               String strMessage=inObject.getString(getString(R.string.key_Message));
                String strRollNotSet=inObject.getString(getString(R.string.key_Roll_Set));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    studentArrayList=new JSONArray();
                    for(int i=0;i<resultArray.length();i++){
                        studentArrayList.put(resultArray.getJSONObject(i));
                    }
                }else if(strRollNotSet.equalsIgnoreCase("1")){
                    showRollNoStatus(strStatus,strMessage);
                }else {
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
            spnrParentNtByDtStudName.setPrompt("Choose Section");
            spnrParentNtByDtStudName.setAdapter(adapter);
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

                            Intent intent = new Intent(getActivity(), ParentNoteActivity.class);
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

    private class GetParentNoteByDate extends AsyncTask<String, JSONArray, String> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Notice...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            for(int i=prntArray.length();i>=0;i--){

                  prntArray.remove(i);
                }
            }else{
                prntArray= new JSONArray();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String strResult;
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_parentnoteDisplayByDate));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StudentId), strStudentId);
                noticeBoardData.put(getString(R.string.key_fromDate), strFrmDt);
                noticeBoardData.put(getString(R.string.key_toDate), strToDt);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetParentNoteByDate,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetParentNoteByDate,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        prntArray.put(resultArray.getJSONObject(i));
                    }
                    //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
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
            } catch (Exception e) {
                e.printStackTrace();
                strResult = "Exception " + e.toString();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                parentsNoteAdapter.notifyDataSetChanged();
            }else{
                initparentNoteAdapterBelowKitkat();
            }


        }


    }

    class DeleteParentsNote extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog= new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Deleting the Parent's Note...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = AD.url.base_url + "noticeBoardOperation.jsp";
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteParentNoteById));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_Id), strNoticeId);

                outObject.put("noticeBoardData", noticeBoardData);
                Log.e("TAG", "Delete Parents Note,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "Delete Parents Note,doInBackground, respText =" + respText);
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
                            if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
                                new GetParentNoteByDate().execute();

                                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

                                    parentsNoteAdapter.notifyDataSetChanged();

                                }else{

                                    initparentNoteAdapterBelowKitkat();

                                }
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }
}
