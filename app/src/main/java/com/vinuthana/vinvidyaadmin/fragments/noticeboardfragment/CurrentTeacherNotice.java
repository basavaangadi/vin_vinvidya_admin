package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditNoticeActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditTeacherNoteActivity;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapterBelowKitKat;
import com.vinuthana.vinvidyaadmin.adapters.StaffSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class CurrentTeacherNotice extends Fragment {
    private Session session;
    String strStaffId, strSchoolId, strClass, strStatus, strStfId, strStaff, strAcademicYearId,strNoticeId;
    RecyclerView recyclerViewCurTeacherNote;
    Spinner spnrCurTchrNtStaffList;
    Button btnGetCurTchrNt;
    int roleId;

    NoticeRecyclerViewAdapter noticeRecyclerViewAdapter;
    NoticeRecyclerViewAdapterBelowKitKat noticeRecyclerViewAdapterBelowKitKat;
    JSONArray teacherNoteArray= new JSONArray();
    JSONArray staffListArray= new JSONArray();
    CheckConnection connection = new CheckConnection();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_current_teacher_notice, container, false);

        return view;
    }

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
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            init();
            allEvents();
            new GetStaffList().execute();
        }
    }

    private void init() {

        recyclerViewCurTeacherNote = (RecyclerView) getActivity().findViewById(R.id.recyclerViewCurTeacherNote);
        spnrCurTchrNtStaffList = (Spinner) getActivity().findViewById(R.id.spnrCurTchrNtStaffList);
        btnGetCurTchrNt = (Button) getActivity().findViewById(R.id.btnGetCurTchrNt);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            noticeRecyclerViewAdapter = new NoticeRecyclerViewAdapter(teacherNoteArray, getActivity(),roleId);
            noticeRecyclerViewAdapter.setOnButtonClickListener(new NoticeRecyclerViewAdapter.OnNoticeViewClickListener() {
                @Override
                public void onEdit(JSONObject noticeObject, int position, String noticeId) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    Log.e("Edit button Clicked ","for the Value of  "+noticeId+"  at the position "+position+" Data is "+noticeObject);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to Edit this Notice?..");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent noticeIntent= new Intent(getActivity(),EditTeacherNoteActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putString("NoticeData",noticeObject.toString());
                            bundle.putString("StaffId",strStaffId);
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("AcademicYearId",strAcademicYearId);
                            bundle.putString("StaffList",staffListArray.toString());
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
                public void onDelete(int position, String noticeId) {

                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to delete this Notice?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            strNoticeId=noticeId;
                            new DeleteTeachersNoticeId().execute();
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
            recyclerViewCurTeacherNote.setLayoutManager(prntNtLytMngr);
            recyclerViewCurTeacherNote.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurTeacherNote.setAdapter(noticeRecyclerViewAdapter);

        }else{
            initNoticeRecyclerViewAdapterBelowKitKat();


        }


    }

    public void allEvents() {
        spnrCurTchrNtStaffList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvStaffId = (TextView) spnrCurTchrNtStaffList.getSelectedView().findViewById(R.id.stafId);
                TextView tvStaffName = (TextView) spnrCurTchrNtStaffList.getSelectedView().findViewById(R.id.stafName);
                tvStaffId.setTextColor(Color.WHITE);
                tvStaffName.setTextColor(Color.WHITE);
                strStaff = tvStaffId.getText().toString() + tvStaffName.getText().toString();
                strStfId = adapterView.getItemAtPosition(i).toString();
                //strStaff = tvStaffId.getText().toString();

                Log.e("Tag", "" + strStaff);
                //Toast.makeText(getActivity(), strStfId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnGetCurTchrNt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(strStaff.equalsIgnoreCase("StaffIdSelect Staff"))){
                    new GetCurrentTeacherNote().execute();
                }else{
                    Toast toast= Toast.makeText(getActivity(),"Select staff ",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
           //
            //new GetStaffList().execute();
        }
    }*/


    public void initNoticeRecyclerViewAdapterBelowKitKat(){
        noticeRecyclerViewAdapterBelowKitKat = new NoticeRecyclerViewAdapterBelowKitKat(teacherNoteArray, getActivity(),roleId);
        noticeRecyclerViewAdapterBelowKitKat.setOnButtonClickListener(new NoticeRecyclerViewAdapterBelowKitKat.OnNoticeViewClickListener() {
            @Override
            public void onEdit(JSONObject noticeObject, int position, String noticeId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                Log.e("Edit button Clicked ","for the Value of  "+noticeId+"  at the position "+position+" Data is "+noticeObject);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to Edit this Notice?..");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent noticeIntent= new Intent(getActivity(),EditTeacherNoteActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("NoticeData",noticeObject.toString());
                        bundle.putString("StaffId",strStaffId);
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("AcademicYearId",strAcademicYearId);
                        bundle.putString("StaffList",staffListArray.toString());
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
            public void onDelete(int position, String noticeId) {

                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to delete this Notice?..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strNoticeId=noticeId;
                        new DeleteTeachersNoticeId().execute();
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
        recyclerViewCurTeacherNote.setLayoutManager(prntNtLytMngr);
        recyclerViewCurTeacherNote.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCurTeacherNote.setAdapter(noticeRecyclerViewAdapterBelowKitKat);
    }
    class GetStaffList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching data...");
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
                    JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
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
            spnrCurTchrNtStaffList.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    private class GetCurrentTeacherNote extends AsyncTask<String, JSONArray, Void> {


        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Current Notice...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
             for(int i=teacherNoteArray.length();i>=0;i--){

                    teacherNoteArray.remove(i);
                    }
            }else{
                teacherNoteArray= new JSONArray();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getTeacherNote));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StaffId), strStfId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetCurrentNotice,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurrentNotice,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                /*JSONArray result = inObject.getJSONArray(getString(R.string.key_Result));
                publishProgress(result);*/
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray result = new JSONObject(respText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < result.length(); i++) {
                        teacherNoteArray.put(result.getJSONObject(i));
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
                noticeRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                initNoticeRecyclerViewAdapterBelowKitKat();
            }

            progressDialog.dismiss();
        }

    }

    class DeleteTeachersNoticeId extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(getActivity());
            progressDialog.setMessage("Deleting the teacher's note please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
            String op = "deleteTeacherNoteById";
            JSONObject outObject = new JSONObject();
            outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteTeacherNoteById));
            JSONObject noticeBoardData = new JSONObject();
            noticeBoardData.put(getString(R.string.key_Id), strNoticeId);
            outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
            Log.e("TAG", "Delete Teacher's Note,doInBackground, outObject =" + outObject);
            GetResponse response = new GetResponse();
            String respText = response.getServerResopnse(url, outObject.toString());
            Log.e("TAG", "Delete Teacher's Note,doInBackground, respText =" + respText);
            JSONObject inObject = new JSONObject(respText);
                /*JSONArray result = inObject.getJSONArray(getString(R.string.key_Result));
                publishProgress(result);*/
                String strMessage="";
            strStatus = inObject.getString(getString(R.string.key_Status));
            if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))||(strStatus.equalsIgnoreCase(getString(R.string.key_Fail)))) {
                strMessage=inObject.getString(getString(R.string.key_Message));
                showAlert(strMessage,strStatus);
                //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
            } else {
                showAlert("Something went wrong Try again","Error");
            }

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
