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
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.ReminderRecylcerViewAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class StaffReminderByDateStaff extends Fragment {
    EditText edtStaffRmndByDtFrmDt, edtStaffRmndByDtToDt;
    DatePickerDialog datePickerDialogFrmDt, datePickerDialogToDt;

    Calendar noticeFrmDt = Calendar.getInstance();
    Calendar noticeToDt = Calendar.getInstance();
    String strNoticeToDt, strNoticeFrmDt;
    String strDate;
    int roleId;
    View view;
    Button btnStfRemdrByDate;
    RecyclerView recyclerViewRemdrByDate;
    private Session session;
    ProgressDialog progressDialog;
    String strStaffId, strSchoolId, strClass, strStatus, strFrmDate, strTodate;
    CheckConnection connection = new CheckConnection();

    boolean isFragmentLoaded = false;
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtStaffRmndByDtFrmDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFrmDt.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edtStaffRmndByDtToDt.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
            datePickerDialogToDt.dismiss();
        }
    };
    private int rmdToYear, rmdToMonth, rmdToDay, rmdFrmYear, rmdFrmMonth, rmdFrmDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rmdFrmYear = noticeFrmDt.get(Calendar.YEAR);
        rmdFrmMonth = noticeFrmDt.get(Calendar.MONTH);
        rmdFrmDay = noticeFrmDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFrmDt = new DatePickerDialog(getActivity(), mlistner, rmdFrmYear, rmdFrmMonth, rmdFrmDay);
        //datePickerDialogFrmDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        rmdToYear = noticeToDt.get(Calendar.YEAR);
        rmdToMonth = noticeToDt.get(Calendar.MONTH);
        rmdToDay = noticeToDt.get(Calendar.DAY_OF_MONTH);
        datePickerDialogToDt = new DatePickerDialog(getActivity(), nlistner, rmdToYear, rmdToMonth, rmdToDay);
        //datePickerDialogToDt.getDatePicker().setMaxDate(System.currentTimeMillis());
        return inflater.inflate(R.layout.fragment_staff_reminder_by_date_staff, container, false);
    }

    public void init() {
        session = new Session(getActivity());
        recyclerViewRemdrByDate = (RecyclerView) getActivity().findViewById(R.id.recyclerViewRemdrByDatestf);
        edtStaffRmndByDtToDt = (EditText) getActivity().findViewById(R.id.edtStaffRmndByDtToDtStf);
        edtStaffRmndByDtFrmDt = (EditText) getActivity().findViewById(R.id.edtStaffRmndByDtFrmDtStf);
        btnStfRemdrByDate = (Button) getActivity().findViewById(R.id.btnStfRemdrByDateStf);

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
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            //
        }
        if (getActivity() != null) {
            if (isVisibleToUser) {

                isFragmentLoaded = true;
            }
        }
    }

    private void allEvents() {
        edtStaffRmndByDtFrmDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtStaffRmndByDtFrmDtStf) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });

        edtStaffRmndByDtToDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtStaffRmndByDtToDtStf) {
                    datePickerDialogToDt.show();
                    return true;
                }
                return false;
            }
        });

        btnStfRemdrByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFrmDate = edtStaffRmndByDtFrmDt.getText().toString();
                strTodate = edtStaffRmndByDtToDt.getText().toString();
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    strFrmDate = edtStaffRmndByDtFrmDt.getText().toString();
                    strTodate = edtStaffRmndByDtToDt.getText().toString();
                    Date fromDate = sdf.parse(strFrmDate);
                    Date toDate = sdf.parse(strTodate);
                    if (fromDate.after(toDate)) {
                        Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.e("BtnSubmit",e.toString());
                }
                /*String strNoticeFrmDate = edtStaffRmndByDtFrmDt.getText().toString();
                String strNoticeToDate = edtStaffRmndByDtToDt.getText().toString();
                if (strNoticeFrmDate.equals("") && strNoticeToDate.equals("")) {
                    //Toast.makeText(getActivity(), "Enter any one Date", Toast.LENGTH_SHORT).show();
                } else if (!strNoticeFrmDate.equals("") && !strNoticeToDate.equals("")) {
                    //Toast.makeText(getActivity(), "Both text are entered", Toast.LENGTH_SHORT).show();
                    new GetStaffRmndrByDt().execute(strNoticeFrmDate, strNoticeToDate);
                } else if (!strNoticeFrmDate.equals("") && strNoticeToDate.equals("")) {
                    //Toast.makeText(getActivity(), "strOnDate is only entered", Toast.LENGTH_SHORT).show();
                    new GetStaffRmndrByDt().execute("", strNoticeToDate);
                } else if (!strNoticeFrmDate.equals("") && strNoticeToDate.equals("")) {
                    //Toast.makeText(getActivity(), "strSentDate is only entered", Toast.LENGTH_SHORT).show();
                    new GetStaffRmndrByDt().execute(strNoticeFrmDate, "");
                }*/

                if (edtStaffRmndByDtFrmDt.getText().toString().equals("") || edtStaffRmndByDtToDt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Notice By Date and  Notice On can't be blank", Toast.LENGTH_LONG).show();
                }
                else {
                    new GetStaffRmndrByDt().execute();
                }
                //new GetNoticeByDate().execute();
            }
        });


    }

    private class GetStaffRmndrByDt extends AsyncTask<String, JSONArray, String> {
        ProgressDialog progressDialog;

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
                outObject.put("OperationName", getString(R.string.web_staffReminderDisplayByDate));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StaffId), strStaffId);
                noticeBoardData.put(getString(R.string.key_fromDate), strFrmDate);
                noticeBoardData.put(getString(R.string.key_toDate), strTodate);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetStaffRmndrByDt,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetStaffRmndrByDt,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
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
            ReminderRecylcerViewAdapter recyclerViewAdapter = new ReminderRecylcerViewAdapter(values[0], getActivity(),roleId);
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewRemdrByDate.setLayoutManager(prntNtLytMngr);
            recyclerViewRemdrByDate.setItemAnimator(new DefaultItemAnimator());
            recyclerViewRemdrByDate.setAdapter(recyclerViewAdapter);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}
