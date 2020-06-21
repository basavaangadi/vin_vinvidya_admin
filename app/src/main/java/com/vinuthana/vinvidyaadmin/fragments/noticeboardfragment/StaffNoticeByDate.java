package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditNoticeActivity;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapterBelowKitKat;
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


public class StaffNoticeByDate extends Fragment {
    EditText edtNoticeByDateFrmDt, edtNoticeByDateToDt;
    DatePickerDialog datePickerDialogFrmDt, datePickerDialogToDt;
    Calendar noticeFrmDt = Calendar.getInstance();
    Calendar noticeToDt = Calendar.getInstance();
    String strNoticeToDt, strNoticeFrmDt,strNoticeId,strDate;

    int roleId;
    private Session session;
    RecyclerView recyclerViewNoticeByDt;
    boolean isFragmentLoaded = false;
    String strStaffId, strSchoolId, strClass, strSection, strStatus, strFromDt, strToDt, strAcademicYearId;
    View view;
    Button btnNOticeByDate;
    CheckConnection connection = new CheckConnection();
    NoticeRecyclerViewAdapter noticeRecyclerViewAdapter;
    NoticeRecyclerViewAdapterBelowKitKat noticeRecyclerViewAdapterBelowKitKat;

    JSONArray noticeArray=new JSONArray();
    //= new NoticeRecyclerViewAdapter(values[0], getActivity(),roleId);
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtNoticeByDateFrmDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFrmDt.dismiss();
        }
    };
    DatePickerDialog.OnDateSetListener nlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year_new, int month_new, int dayOfMonth_new) {
            edtNoticeByDateToDt.setText(dayOfMonth_new + "-" + String.valueOf(month_new + 1) + "-" + year_new);
            datePickerDialogToDt.dismiss();
        }
    };
    private int noticeToYear, noticeToMonth, noticeToDay, noticeFrmYear, noticeFrmMonth, noticeFrmDay;
    private String strNoticeToYear, strNoticeToMonth, strNoticeToDay, strNoticeFrmYear, strNoticeFrmMonth, strNoticeFrmDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init() {

        recyclerViewNoticeByDt = (RecyclerView) getActivity().findViewById(R.id.recyclerViewNoticeByDt);
        edtNoticeByDateFrmDt = (EditText) getActivity().findViewById(R.id.edtNoticeByDateFrmDt);
        edtNoticeByDateToDt = (EditText) getActivity().findViewById(R.id.edtNoticeByDateToDt);
        btnNOticeByDate = (Button) getActivity().findViewById(R.id.btnNOticeByDate);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            noticeRecyclerViewAdapter= new NoticeRecyclerViewAdapter(noticeArray,getActivity(),roleId);
            noticeRecyclerViewAdapter.setOnButtonClickListener(new NoticeRecyclerViewAdapter.OnNoticeViewClickListener() {
                @Override
                public void onEdit(JSONObject noticeObject, int position, String noticeId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    Log.e("Edit button Clicked ","for the Value of  "+noticeId+"  at the position "+position+" Data is "+noticeObject);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want toEdit this Notice?..");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent noticeIntent= new Intent(getActivity(),EditNoticeActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putString("NoticeData",noticeObject.toString());
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("AcademicYearId",strAcademicYearId);
                            noticeIntent.putExtras(bundle);
                            startActivity(noticeIntent);
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
                public void onDelete(int position,String noticeId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want delete this notice?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            strNoticeId=noticeId;
                            new DeleteNotice().execute();

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
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewNoticeByDt.setLayoutManager(prntNtLytMngr);
            recyclerViewNoticeByDt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewNoticeByDt.setAdapter(noticeRecyclerViewAdapter);

        }else{
            intStfNtcRcylrViewAdptrBlwKtKat();


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notice_by_date, container, false);

        noticeFrmYear = noticeFrmDt.get(Calendar.YEAR);
        noticeFrmMonth = noticeFrmDt.get(Calendar.MONTH);
        noticeFrmDay = noticeFrmDt.get(Calendar.DAY_OF_MONTH);

        noticeFrmDt.set(noticeFrmYear, noticeFrmMonth, noticeFrmDay);
        datePickerDialogFrmDt = new DatePickerDialog(getActivity(), mlistner, noticeFrmYear, noticeFrmMonth, noticeFrmDay);
        //datePickerDialogFrmDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        noticeToYear = noticeToDt.get(Calendar.YEAR);
        noticeToMonth = noticeToDt.get(Calendar.MONTH);
        noticeToDay = noticeToDt.get(Calendar.DAY_OF_MONTH);

        noticeToDt.set(noticeToDay, noticeToMonth, noticeToDay);
        datePickerDialogToDt = new DatePickerDialog(getActivity(), nlistner, noticeToYear, noticeToMonth, noticeToDay);
        //datePickerDialogToDt.getDatePicker().setMaxDate(System.currentTimeMillis());

        strNoticeFrmYear = String.valueOf(noticeFrmYear);
        strNoticeFrmMonth = String.valueOf(noticeFrmMonth);
        strNoticeFrmDay = String.valueOf(noticeFrmDay);

        strNoticeToYear = String.valueOf(noticeToYear);
        strNoticeToMonth = String.valueOf(noticeToMonth);
        strNoticeToDay = String.valueOf(noticeToDay);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            //new GetNoticeByDate().execute();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strSchoolId=user.get(Session.KEY_SCHOOL_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            init();
            allEvents();
        }
    }



    private void allEvents() {
        edtNoticeByDateFrmDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtNoticeByDateFrmDt) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });

        edtNoticeByDateToDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtNoticeByDateToDt) {
                    datePickerDialogToDt.show();
                    return true;
                }
                return false;
            }
        });

        btnNOticeByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFromDt = edtNoticeByDateFrmDt.getText().toString();
                strToDt = edtNoticeByDateToDt.getText().toString();

                if (edtNoticeByDateFrmDt.getText().toString().equals("") || edtNoticeByDateToDt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "From Date and  To Date can't be blank", Toast.LENGTH_LONG).show();
                } else {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        strFromDt = edtNoticeByDateFrmDt.getText().toString();
                        strToDt = edtNoticeByDateToDt.getText().toString();

                        Date fromDate = sdf.parse(strFromDt);
                        Date toDate = sdf.parse(strToDt);
                        if (fromDate.after(toDate)) {
                            Toast.makeText(getActivity(), "From date should be less then To date", Toast.LENGTH_SHORT).show();
                        } else {
                            new GetNoticeByDate().execute();
                        }
                    }catch (Exception e){
                        Log.e("btnOnclick",e.toString());
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                
            }
        });
    }

    public void intStfNtcRcylrViewAdptrBlwKtKat(){

        noticeRecyclerViewAdapterBelowKitKat= new NoticeRecyclerViewAdapterBelowKitKat(noticeArray,getActivity(),roleId);
        noticeRecyclerViewAdapterBelowKitKat.setOnButtonClickListener(new NoticeRecyclerViewAdapterBelowKitKat.OnNoticeViewClickListener() {
            @Override
            public void onEdit(JSONObject noticeObject, int position, String noticeId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                Log.e("Edit button Clicked ","for the Value of  "+noticeId+"  at the position "+position+" Data is "+noticeObject);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want toEdit this Notice?..");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent noticeIntent= new Intent(getActivity(),EditNoticeActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("NoticeData",noticeObject.toString());
                        bundle.putString("StaffId",strStaffId);
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("AcademicYearId",strAcademicYearId);
                        noticeIntent.putExtras(bundle);
                        startActivity(noticeIntent);
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
            public void onDelete(int position,String noticeId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want delete this notice?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strNoticeId=noticeId;
                        new DeleteNotice().execute();

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
        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        recyclerViewNoticeByDt.setLayoutManager(prntNtLytMngr);
        recyclerViewNoticeByDt.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNoticeByDt.setAdapter(noticeRecyclerViewAdapterBelowKitKat);
    }

    private class GetNoticeByDate extends AsyncTask<String, JSONArray, String> {

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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            for(int i=noticeArray.length();i>=0;i--){

                noticeArray.remove(i);
            }
            }else{
                noticeArray= new JSONArray();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put("OperationName", getString(R.string.web_getStaffNoticeByDate));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StaffId), strStaffId);
                noticeBoardData.put(getString(R.string.key_fromDate), strFromDt);
                noticeBoardData.put(getString(R.string.key_toDate), strToDt);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetCurrentNotice,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurrentNotice,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray result = new JSONObject(respText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < result.length(); i++) {
                        noticeArray.put(result.getJSONObject(i));
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
                noticeRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                intStfNtcRcylrViewAdptrBlwKtKat();
            }

        }
    }
    private class DeleteNotice extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String strMessage,strTitle,strAlertMessage;
        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait,deleting the Notice..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteNoticeById));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_Id), strNoticeId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetCurrentNotice,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurrentNotice,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));

                strMessage = inObject.getString(getString(R.string.key_Message));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                            showAlert(strMessage,strStatus);
                        } else{
                            showAlert("Something went wrong in deleting. ","Error");
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            progressDialog.dismiss();
        }
        private void showAlert(String alertMessage, String alertTitle) {
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
    }

}
