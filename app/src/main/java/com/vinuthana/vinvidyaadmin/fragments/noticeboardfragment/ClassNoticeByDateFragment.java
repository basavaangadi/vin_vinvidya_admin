package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditClassNoitceActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditParentsNoteActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters.ClassNoticeRecylerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters.ClassNoticeRecylerViewDapterBelowKitkat;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassNoticeByDateFragment extends Fragment {


    public ClassNoticeByDateFragment() {
        // Required empty public constructor
    }



    Spinner spnrClassNoticeByDtClass, spnrClassNoticeByDtSection, spnrClassNoticeByDtStudName;
    Button btnGetClassNoticeByDt;
    String strClass, strClassId, strSchoolId, strFrmDt, strToDt, strStatus, strAcademicYearId;
    String strStaffId,strNoticeId;
    private Session session;
    RecyclerView recyclerViewClassNoticeByDt;
    EditText edtClassNoticeByDtFrmDt, edtClassNoticeByDtToDt;
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
    ClassNoticeRecylerViewAdapter classNoteAdapter;
    ClassNoticeRecylerViewDapterBelowKitkat classNoteAdapterBelowKitKat;
    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtClassNoticeByDtFrmDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFrmDt.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edtClassNoticeByDtToDt.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
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
        view = inflater.inflate(R.layout.fragment_class_notice_by_date, container, false);

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

        btnGetClassNoticeByDt = getActivity().findViewById(R.id.btnGetClassNoticeByDt);
        spnrClassNoticeByDtClass = getActivity().findViewById(R.id.spnrClassNoticeByDtClass);
        spnrClassNoticeByDtSection = getActivity().findViewById(R.id.spnrClassNoticeByDtSection);

        edtClassNoticeByDtFrmDt = getActivity().findViewById(R.id.edtClassNoticeByDtFrmDt);
        edtClassNoticeByDtToDt = getActivity().findViewById(R.id.edtClassNoticeByDtToDt);
        recyclerViewClassNoticeByDt = getActivity().findViewById(R.id.recyclerViewClassNoticeByDt);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            classNoteAdapter = new ClassNoticeRecylerViewAdapter(prntArray, getActivity());
            classNoteAdapter.setOnButtonClickListener(new ClassNoticeRecylerViewAdapter.OnNoticeViewClickListener() {
                @Override
                public void onEdit(JSONObject noticeData, int position, String noticeId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit this Class note ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(getActivity(), EditClassNoitceActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putString("NoticeData",noticeData.toString());
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("AcademicYearId",strAcademicYearId);

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
                    builder.setMessage("Do want to Delete this Class note?.. ");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            strNoticeId=noticeId;
                            Log.e("StrNoticeID = ",strNoticeId);
                            new DeleteClassNoteById().execute();
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
            recyclerViewClassNoticeByDt.setLayoutManager(prntNtLytMngr);
            recyclerViewClassNoticeByDt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewClassNoticeByDt.setAdapter(classNoteAdapter);

        }else{
            initClassNoteAdapterBelowKitkat();

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

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strStaffId=user.get(Session.KEY_STAFFDETAILS_ID);

            init();
            if (getActivity() != null){
                new GetClass().execute();
            }
            allEvents();
        }
    }

    private void allEvents() {

        edtClassNoticeByDtFrmDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtClassNoticeByDtFrmDt) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });

        edtClassNoticeByDtToDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtClassNoticeByDtToDt) {
                    datePickerDialogToDt.show();
                    return true;
                }
                return false;
            }
        });

        spnrClassNoticeByDtClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrClassNoticeByDtClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrClassNoticeByDtSection.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrClassNoticeByDtSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrClassNoticeByDtSection.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClassId = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClassId);
                //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnGetClassNoticeByDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtClassNoticeByDtFrmDt.getText().toString().equalsIgnoreCase("") || edtClassNoticeByDtToDt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "From Date and  To Date can't be blank", Toast.LENGTH_LONG).show();
                }else {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        strFrmDt = edtClassNoticeByDtFrmDt.getText().toString();
                        strToDt = edtClassNoticeByDtToDt.getText().toString();
                        Date fromDate = sdf.parse(strFrmDt);
                        Date toDate = sdf.parse(strToDt);
                        int clsPos = spnrClassNoticeByDtClass.getSelectedItemPosition();
                        int secPos = spnrClassNoticeByDtSection.getSelectedItemPosition();

                        if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)){
                            Toast toast= Toast.makeText(getActivity(),"select Class, section ",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                        else if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        } else {
                            new GetClassNoticeByDate().execute();
                        }
                    } catch (Exception e) {
                        Log.e("btnOnclick", e.toString());
                    }
                }

            }
        });
    }


    public void initClassNoteAdapterBelowKitkat(){
        classNoteAdapterBelowKitKat = new ClassNoticeRecylerViewDapterBelowKitkat(prntArray, getActivity());
        classNoteAdapterBelowKitKat.setOnButtonClickListener(new ClassNoticeRecylerViewDapterBelowKitkat.OnNoticeViewClickListener() {
            @Override
            public void onEdit(JSONObject noticeData, int position, String noticeId) {

                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to edit this Class note ?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), EditClassNoitceActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("NoticeData",noticeData.toString());
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("StaffId",strStaffId);
                        bundle.putString("AcademicYearId",strAcademicYearId);

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
                builder.setMessage("Do want to Delete this Class note?.. ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strNoticeId=noticeId;
                        new DeleteClassNoteById().execute();
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
        recyclerViewClassNoticeByDt.setLayoutManager(prntNtLytMngr);
        recyclerViewClassNoticeByDt.setItemAnimator(new DefaultItemAnimator());
        recyclerViewClassNoticeByDt.setAdapter(classNoteAdapterBelowKitKat);
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
            spnrClassNoticeByDtClass.setPrompt("Choose Class");
            spnrClassNoticeByDtClass.setAdapter(adapter);
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
            spnrClassNoticeByDtSection.setPrompt("Choose Section");
            spnrClassNoticeByDtSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }



    private class GetClassNoticeByDate extends AsyncTask<String, JSONArray, String> {
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClassNoticeForStaff));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_ClassId), strClassId);
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
                classNoteAdapter.notifyDataSetChanged();
            }else{
                initClassNoteAdapterBelowKitkat();
            }


        }
    }

    class DeleteClassNoteById extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog= new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Deleting the class Note...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = AD.url.base_url + "noticeBoardOperation.jsp";
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteClassNoteById));
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
                            if(strStatus.equalsIgnoreCase(getString(R.string.key_Success))){
                                new GetClassNoticeByDate().execute();

                                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

                                    classNoteAdapter.notifyDataSetChanged();

                                }else{

                                    initClassNoteAdapterBelowKitkat();

                                }
                            }
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
