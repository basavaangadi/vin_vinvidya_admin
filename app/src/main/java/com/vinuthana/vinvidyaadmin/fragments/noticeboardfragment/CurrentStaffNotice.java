package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditNoticeActivity;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.NoticeRecyclerViewAdapterBelowKitKat;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.PutAttendanceActivity;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class CurrentStaffNotice extends Fragment {
    private Session session;
    String strStaffId, strSchoolId, strClass, strAcademicYearId,strNoticeId;
    int roleId;
    RecyclerView recyclerViewCurNotice;

    ProgressDialog progressDialog;
    CheckConnection connection = new CheckConnection();
    NoticeRecyclerViewAdapter noticeRecyclerViewAdapter;
    NoticeRecyclerViewAdapterBelowKitKat noticeRecyclerViewAdapterBelowKitKat;
    JSONArray noticeArray= new JSONArray();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_notice, container, false);
        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            strSchoolId=user.get(Session.KEY_SCHOOL_ID);
            init();
            new GetCurrentNotice().execute();

            //Toast.makeText(getActivity(), roleId, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){

            //new GetCurrentNotice().execute();
        }
    }

    public void init() {

        recyclerViewCurNotice = (RecyclerView) getActivity().findViewById(R.id.recyclerViewCurNotice);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            noticeRecyclerViewAdapter=new NoticeRecyclerViewAdapter(noticeArray,getActivity(),roleId);
            Log.e("fragment" ,String.valueOf(roleId));
            noticeRecyclerViewAdapter.setOnButtonClickListener(new NoticeRecyclerViewAdapter.OnNoticeViewClickListener(){

                @Override
                public void onEdit(JSONObject noticeObject,int position,String noticeId){
                    Log.e("Edit button Clicked ","for the Value of  "+noticeId+"  at the position "+position+" Data is "+noticeObject);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do You want edit the Notice ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent noticeIntent= new Intent(getActivity(),EditNoticeActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putString("NoticeData",noticeObject.toString());
                            bundle.putString("SchoolId",strSchoolId);
                            bundle.putString("StaffId",strStaffId);
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
            recyclerViewCurNotice.setLayoutManager(prntNtLytMngr);
            recyclerViewCurNotice.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurNotice.setAdapter(noticeRecyclerViewAdapter);
            Log.e("fragment","roleId is "+roleId);
        }else{
            initNoticeRecyclerViewAdapterBelowKitKat();

        }


    }

    void initNoticeRecyclerViewAdapterBelowKitKat(){
        noticeRecyclerViewAdapterBelowKitKat=new NoticeRecyclerViewAdapterBelowKitKat(noticeArray,getActivity(),roleId);
        Log.e("fragment" ,String.valueOf(roleId));
        noticeRecyclerViewAdapterBelowKitKat.setOnButtonClickListener(new NoticeRecyclerViewAdapterBelowKitKat.OnNoticeViewClickListener(){

            @Override
            public void onEdit(JSONObject noticeObject,int position,String noticeId){
                Log.e("Edit button Clicked ","for the Value of  "+noticeId+"  at the position "+position+" Data is "+noticeObject);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Do You want edit the Notice ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent noticeIntent= new Intent(getActivity(),EditNoticeActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("NoticeData",noticeObject.toString());
                        bundle.putString("SchoolId",strSchoolId);
                        bundle.putString("StaffId",strStaffId);
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
        recyclerViewCurNotice.setLayoutManager(prntNtLytMngr);
        recyclerViewCurNotice.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCurNotice.setAdapter(noticeRecyclerViewAdapterBelowKitKat);
        Log.e("fragment","roleId is "+roleId);
    }

    private class GetCurrentNotice extends AsyncTask<String, JSONArray, Void> {

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
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffNotice));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StaffId), strStaffId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetCurrentNotice,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurrentNotice,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray result = new JSONObject(respText).getJSONArray(getString(R.string.key_Result));
                    for (int i=0; i < result.length(); i++) {
                        noticeArray.put(result.getJSONObject(i));
                    }

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

            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                noticeRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                initNoticeRecyclerViewAdapterBelowKitKat();
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
