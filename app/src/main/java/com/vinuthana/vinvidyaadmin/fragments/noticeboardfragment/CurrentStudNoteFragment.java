package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;


import android.app.ProgressDialog;
import android.app.AlertDialog;

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

import java.util.HashMap;


public class CurrentStudNoteFragment extends Fragment {

    private Session session;
    String strStaffId, strSchoolId, strClass, strAcademicYearId,strNoticeId;
    RecyclerView recyclerViewCurNotice;
    ProgressDialog progressDialog;
    boolean isFragmentLoaded = false;
    JSONArray noticeArray=new JSONArray();
    NoticeRecyclerViewAdapter noticeRecyclerViewAdapter;
    NoticeRecyclerViewAdapterBelowKitKat noticeRecyclerViewAdapterBelowKitKat;
    CheckConnection connection = new CheckConnection();
    int roleId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_current_stud_note, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            //new GetCurrentNotice().execute();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            init();
            new GetCurrentNotice().execute();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser == true) {

            }
        }
    }

    public void init() {

        recyclerViewCurNotice = (RecyclerView) getActivity().findViewById(R.id.recyclerViewCurNotice);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            noticeRecyclerViewAdapter= new NoticeRecyclerViewAdapter(noticeArray, getActivity(),roleId);
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
            recyclerViewCurNotice.setLayoutManager(prntNtLytMngr);
            recyclerViewCurNotice.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurNotice.setAdapter(noticeRecyclerViewAdapter);

        }else{
            noticeRecyclerViewAdapterBelowKitKat= new NoticeRecyclerViewAdapterBelowKitKat(noticeArray, getActivity(),roleId);
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
            recyclerViewCurNotice.setLayoutManager(prntNtLytMngr);
            recyclerViewCurNotice.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurNotice.setAdapter(noticeRecyclerViewAdapterBelowKitKat);
        }



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

             for(int i=noticeArray.length();i>=0;i--){

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        noticeArray.remove(i);
                    }



            }

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffStudentNotice));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
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
                   for(int i=0;i<=result.length();i++){

                       noticeArray.put(result.getJSONObject(i));
                   }
                    // publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
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
            /*NoticeRecyclerViewAdapter recyclerViewAdapter = new NoticeRecyclerViewAdapter(values[0], getActivity(),roleId);
            RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewCurNotice.setLayoutManager(prntNtLytMngr);
            recyclerViewCurNotice.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurNotice.setAdapter(recyclerViewAdapter);*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                noticeRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                noticeRecyclerViewAdapterBelowKitKat.notifyDataSetChanged();
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
