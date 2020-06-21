package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;


import android.app.AlertDialog;
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
import android.view.View;
import android.view.ViewGroup;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTeacherNoticeStaff extends Fragment {
    private Session session;
    String strStaffId, strSchoolId, strClass, strStatus, strStfId, strStaff, strAcademicYearId;
    RecyclerView recyclerViewCurTeacherNoteStf;
    CheckConnection connection = new CheckConnection();
    ProgressDialog progressDialog;
    int roleId;

    public CurrentTeacherNoticeStaff() {
        // Required empty public constructor
    }
    /*public CurrentTeacherNoticeStaff(String stfId) {
        // Required empty public constructor
        strStaffId=stfId;
    }*/

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
        }

        //  Toast.makeText(getActivity(), strStaffId, Toast.LENGTH_SHORT).show();
//        strStfId = getActivity().getIntent().getExtras().getString("ClassId");
//strStfId=getActivity().getIntent().getData().


    }

    private void init() {
        session = new Session(getActivity());
        recyclerViewCurTeacherNoteStf = (RecyclerView) getActivity().findViewById(R.id.recyclerViewCurTeacherNoteStf);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_teacher_notice_staff, container, false);
        /*if (getArguments() != null) {
            strStaffId = getArguments().getString("StaffId");
            strAcademicYearId = getArguments().getString("AcdemicYearId");

        }*/

        new GetCurrentTeacherNote().execute();

        return view;
    }

    private class GetCurrentTeacherNote extends AsyncTask<String, JSONArray, Void> {


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
        protected Void doInBackground(String... strings) {
            try {

                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getTeacherNote));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StaffId), strStaffId);
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
            recyclerViewCurTeacherNoteStf.setLayoutManager(prntNtLytMngr);
            recyclerViewCurTeacherNoteStf.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurTeacherNoteStf.setAdapter(recyclerViewAdapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}
