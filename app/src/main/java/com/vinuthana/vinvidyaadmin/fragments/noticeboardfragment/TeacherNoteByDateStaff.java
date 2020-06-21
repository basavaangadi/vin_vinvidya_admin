package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapter;
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


public class TeacherNoteByDateStaff extends Fragment {
    EditText edtTeacherNtByDtFrmDtStf, edtTeacherNtByDtToDtStf;
    Spinner spnrTchrNtStaffList;
    DatePickerDialog datePickerDialogFrmDt, datePickerDialogToDt;
    Calendar teacherNtFrmDt = Calendar.getInstance();
    Calendar teacherNtToDt = Calendar.getInstance();
    String strNoticeToDt, strNoticeFrmDt, strStfId, strStaff;
    String strDate;
    View view;
    boolean isFragmentLoaded = false;
    ProgressDialog progressDialog;
    Button btnTeacherNoteByDateStf;
    private Session session;
    int roleId;
    RecyclerView recyclerViewTeacherNtByDtStf;
    String strStaffId, strSchoolId, strClass, strSection;
    CheckConnection connection = new CheckConnection();
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtTeacherNtByDtFrmDtStf.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFrmDt.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edtTeacherNtByDtToDtStf.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
            datePickerDialogToDt.dismiss();
        }
    };
    private int teacherNtToYear, teacherNtToMonth, teacherNtToDay, teacherNtFrmYear, teacherNtFrmMonth, teacherNtFrmDay;


    public TeacherNoteByDateStaff() {
        // Required empty public constructor
    }
   /* public TeacherNoteByDateStaff(String stfId) {
        // Required empty public constructor
        strStaffId=stfId;
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_note_by_date_staff, container, false);
        teacherNtFrmYear = teacherNtFrmDt.get(Calendar.YEAR);
        teacherNtFrmMonth = teacherNtFrmDt.get(Calendar.MONTH);
        teacherNtFrmDay = teacherNtFrmDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFrmDt = new DatePickerDialog(getActivity(), mlistner, teacherNtFrmYear, teacherNtFrmMonth, teacherNtFrmDay);
        datePickerDialogFrmDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        teacherNtToYear = teacherNtToDt.get(Calendar.YEAR);
        teacherNtToMonth = teacherNtToDt.get(Calendar.MONTH);
        teacherNtToDay = teacherNtToDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogToDt = new DatePickerDialog(getActivity(), nlistner, teacherNtToYear, teacherNtToMonth, teacherNtToDay);
        //datePickerDialogToDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        return view;
    }

    public void init() {
        session = new Session(getActivity());
        recyclerViewTeacherNtByDtStf = (RecyclerView) getActivity().findViewById(R.id.recyclerViewTeacherNtByDtStf);
        btnTeacherNoteByDateStf = (Button) getActivity().findViewById(R.id.btnTeacherNoteByDateStf);
        edtTeacherNtByDtFrmDtStf = (EditText) getActivity().findViewById(R.id.edtTeacherNtByDtFrmDtStf);
        edtTeacherNtByDtToDtStf = (EditText) getActivity().findViewById(R.id.edtTeacherNtByDtToDtStf);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            allEvents();
            //new GetTeacherNoteByDate().execute();
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
        }
    }

    private void allEvents() {
        edtTeacherNtByDtFrmDtStf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtTeacherNtByDtFrmDtStf) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });

        edtTeacherNtByDtToDtStf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtTeacherNtByDtToDtStf) {
                    datePickerDialogToDt.show();
                    return true;
                }
                return false;
            }
        });

        btnTeacherNoteByDateStf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNoticeFrmDate = edtTeacherNtByDtFrmDtStf.getText().toString();
                String strNoticeToDate = edtTeacherNtByDtToDtStf.getText().toString();
                if (strNoticeFrmDate.equals("") || strNoticeToDate.equals("")) {
                    Toast.makeText(getActivity(), "Enter both from and to dates", Toast.LENGTH_SHORT).show();
                }else {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                        Date fromDate = sdf.parse(strNoticeFrmDate);
                        Date toDate = sdf.parse(strNoticeToDate);
                        if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        }  else {

                            new GetTeacherNoteByDate().execute();
                        }
                    } catch (Exception e) {
                        Log.e("btnOnclick", e.toString());
                    }
                }








            }
        });
    }

    private class GetTeacherNoteByDate extends AsyncTask<String, JSONArray, String> {


        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Current Notice...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getTeacherNoteByDate));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StaffId), strStaffId);
                noticeBoardData.put(getString(R.string.key_fromDate), strings[0]);
                noticeBoardData.put(getString(R.string.key_toDate), strings[1]);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetCurrentNotice,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurrentNotice,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
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
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            NoticeRecyclerViewAdapter recyclerViewAdapter = new NoticeRecyclerViewAdapter(values[0], getActivity(),roleId);
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewTeacherNtByDtStf.setLayoutManager(prntNtLytMngr);
            recyclerViewTeacherNtByDtStf.setItemAnimator(new DefaultItemAnimator());
            recyclerViewTeacherNtByDtStf.setAdapter(recyclerViewAdapter);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}
