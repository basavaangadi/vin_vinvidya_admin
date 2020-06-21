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
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditStaffReminderActvity;
import com.vinuthana.vinvidyaadmin.adapters.ReminderRecylcerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReminderRecylcerViewAdapterBelowKitkat;
import com.vinuthana.vinvidyaadmin.adapters.StaffSpinnerAdapter;
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


public class StaffReminderByDate extends Fragment {
    EditText edtStaffRmndByDtFrmDt, edtStaffRmndByDtToDt;
    DatePickerDialog datePickerDialogFrmDt, datePickerDialogToDt;
    Spinner spnrRmndrStaffList;
    Calendar noticeFrmDt = Calendar.getInstance();
    Calendar noticeToDt = Calendar.getInstance();
    String strNoticeToDt, strNoticeFrmDt;
    String strDate;
    View view;
    int roleId;
    Button btnStfRemdrByDate;
    RecyclerView recyclerViewRemdrByDate;
    private Session session;
    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();
    JSONArray reminderArray=new JSONArray();
    JSONArray staffListArray=new JSONArray();
    String strStaffId, strSchoolId, strStaffName, strStatus, strStaff, strStfId, strFrmDate;
    String strTodate,strAcademicYearId,strReminderId;
    ReminderRecylcerViewAdapter recyclerViewAdapter;
    ReminderRecylcerViewAdapterBelowKitkat recyclerViewAdapterBelowKitkat;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        view = inflater.inflate(R.layout.fragment_staff_reminder_by_date, container, false);

        return view;
    }

    public void init() {

        recyclerViewRemdrByDate = (RecyclerView) getActivity().findViewById(R.id.recyclerViewRemdrByDate);
        edtStaffRmndByDtToDt = (EditText) getActivity().findViewById(R.id.edtStaffRmndByDtToDt);
        edtStaffRmndByDtFrmDt = (EditText) getActivity().findViewById(R.id.edtStaffRmndByDtFrmDt);
        btnStfRemdrByDate = (Button) getActivity().findViewById(R.id.btnStfRemdrByDate);
        spnrRmndrStaffList = (Spinner) getActivity().findViewById(R.id.spnrRmndrStaffList);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            recyclerViewAdapter= new ReminderRecylcerViewAdapter(reminderArray, getActivity(),roleId);
            recyclerViewAdapter.setOnButtonClickListner(new ReminderRecylcerViewAdapter.OnReminderViewClickListener() {
                @Override
                public void onEdit(JSONObject ReminderData, int position, String reminderId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit this Staff Reminder ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(getActivity(), EditStaffReminderActvity.class);
                            Bundle bundle= new Bundle();
                            bundle.putString("ReminderData",ReminderData.toString());
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("AcademicYearId",strAcademicYearId);
                            bundle.putString("SatffList",staffListArray.toString());
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
                            new DeleteStaffReminderId().execute();
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
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewRemdrByDate.setLayoutManager(prntNtLytMngr);
            recyclerViewRemdrByDate.setItemAnimator(new DefaultItemAnimator());
            recyclerViewRemdrByDate.setAdapter(recyclerViewAdapter);


        }else{
            intStfRmdrRcylrVwAdptr();
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
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            strAcademicYearId=user.get(Session.KEY_ACADEMIC_YEAR_ID);
            init();
            if (getActivity() != null) {

                new GetStaffList().execute();

            }

            allEvents();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (getActivity() != null) {
            if (isVisibleToUser) {
                new GetStaffList().execute();
                isFragmentLoaded = true;
            }
        }
    }

    private void allEvents() {
        edtStaffRmndByDtFrmDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtStaffRmndByDtFrmDt) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });

        edtStaffRmndByDtToDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtStaffRmndByDtToDt) {
                    datePickerDialogToDt.show();
                    return true;
                }
                return false;
            }
        });

        btnStfRemdrByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edtStaffRmndByDtFrmDt.getText().toString().equals("") || edtStaffRmndByDtToDt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Notice By Date and  Notice On can't be blank", Toast.LENGTH_LONG).show();
                } else {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        strFrmDate = edtStaffRmndByDtFrmDt.getText().toString();
                        strTodate = edtStaffRmndByDtToDt.getText().toString();
                        Date fromDate = sdf.parse(strFrmDate);
                        Date toDate = sdf.parse(strTodate);
                        if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        }else if(strStaffName.equalsIgnoreCase("StaffIdSelect Staff")){

                            Toast toast= Toast.makeText(getActivity(),"Select staff ",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                        else {
                            new GetStaffRmndrByDt().execute();
                        }
                    } catch (Exception e) {
                        Log.e("btnOnclick", e.toString());
                    }
                }
            }
        });


        spnrRmndrStaffList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvStaffId = (TextView) spnrRmndrStaffList.getSelectedView().findViewById(R.id.stafId);
                TextView tvStaffName = (TextView) spnrRmndrStaffList.getSelectedView().findViewById(R.id.stafName);
                tvStaffId.setTextColor(Color.WHITE);
                tvStaffName.setTextColor(Color.WHITE);
                strStaffName = tvStaffId.getText().toString() + tvStaffName.getText().toString();
                strStfId = adapterView.getItemAtPosition(i).toString();
                strStaff = tvStaffId.getText().toString();
                Log.e("Tag", "" + strStaffName);
                //Toast.makeText(getActivity(), strStfId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void intStfRmdrRcylrVwAdptr(){
        recyclerViewAdapterBelowKitkat= new ReminderRecylcerViewAdapterBelowKitkat(reminderArray, getActivity(),roleId);
        recyclerViewAdapterBelowKitkat.setOnButtonClickListner(new ReminderRecylcerViewAdapterBelowKitkat.OnReminderViewClickListener() {
            @Override
            public void onEdit(JSONObject ReminderData, int position, String reminderId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to edit this Staff Reminder ?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), EditStaffReminderActvity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("ReminderData",ReminderData.toString());
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("StaffId",strStaffId);
                        bundle.putString("AcademicYearId",strAcademicYearId);
                        bundle.putString("SatffList",staffListArray.toString());
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
                        new DeleteStaffReminderId().execute();
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
        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        recyclerViewRemdrByDate.setLayoutManager(prntNtLytMngr);
        recyclerViewRemdrByDate.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRemdrByDate.setAdapter(recyclerViewAdapterBelowKitkat);
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

           for(int i=reminderArray.length();i>=0;i--){

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        reminderArray.remove(i);
                    }
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", getString(R.string.web_staffReminderDisplayByDate));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StaffId), strStfId);
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
                   // publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<resultArray.length();i++){
                        reminderArray.put(resultArray.getJSONObject(i));
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
                recyclerViewAdapter.notifyDataSetChanged();
            }else{
                intStfRmdrRcylrVwAdptr();
            }


        }
    }

    class GetStaffList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Staff list...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffListBySchoolId));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    staffListArray= new JSONArray();
                    for(int i=0;i<resultArray.length();i++){
                        staffListArray.put(resultArray.getJSONObject(i));
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
            StaffSpinnerAdapter adapter = new StaffSpinnerAdapter(values[0], getActivity());
            spnrRmndrStaffList.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    class DeleteStaffReminderId extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog= new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Deleting the Staff's reminder Note...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = AD.url.base_url + "noticeBoardOperation.jsp";
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteStaffReminder));
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
