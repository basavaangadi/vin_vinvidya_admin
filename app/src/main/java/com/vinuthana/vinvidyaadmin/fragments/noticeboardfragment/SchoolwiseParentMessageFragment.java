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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.StaffReplyActivity;
import com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters.ParentMessageAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolwiseParentMessageFragment extends Fragment {

    CheckConnection connection = new CheckConnection();
    DatePickerDialog datePickerDialog;
    Calendar viewHmFbByDt = Calendar.getInstance();
    private int mYear, mMonth, mDay;
    Button btnViewSchoolwiseParentMessage;
    private Session session;
    String strStaffId, strSchoolId,strAcademicYearId,strDate;
    ParentMessageAdapter recyclerAdapter;
    RecyclerView recyclerViewSchoolwiseParentMessage;
    ProgressDialog progressDialog;
    JSONArray parentMessageArray;
    EditText edtViewSchoolwiseParentMessageDate;


    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtViewSchoolwiseParentMessageDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    public SchoolwiseParentMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schoolwise_parent_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            allEvents();

            mYear = viewHmFbByDt.get(Calendar.YEAR);
            mMonth = viewHmFbByDt.get(Calendar.MONTH);
            mDay = viewHmFbByDt.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), mlistner, mYear, mMonth, mDay);
            //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            //new GetClass().execute();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();

            ArrayList<String> strUser = new ArrayList<>();


            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
        }
    }
    public void init() {

        edtViewSchoolwiseParentMessageDate = getActivity().findViewById(R.id.edtViewSchoolwiseParentMessageDate);
        recyclerViewSchoolwiseParentMessage = getActivity().findViewById(R.id.recyclerViewSchoolwiseParentMessage);
        btnViewSchoolwiseParentMessage = getActivity().findViewById(R.id.btnViewSchoolwiseParentMessage);
        initRecylerAdpater();
    }

    public void initRecylerAdpater(){
        recyclerAdapter = new ParentMessageAdapter(parentMessageArray, getActivity());
        recyclerAdapter.setOnButtonClickListener(new ParentMessageAdapter.onParentMessageClickListener() {
            @Override
            public void onReply(JSONObject parentMessageData, int position, String parentMessageId) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to Reply to this message");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), StaffReplyActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString(getString(R.string.key_ParentMessageId),parentMessageId);
                        bundle.putString(getString(R.string.key_CurrentData),parentMessageData.toString());
                        bundle.putString(getString(R.string.key_StaffId),strStaffId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }


        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewSchoolwiseParentMessage.setLayoutManager(layoutManager);
        recyclerViewSchoolwiseParentMessage.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSchoolwiseParentMessage.setAdapter(recyclerAdapter);
    }
    public void allEvents(){
        btnViewSchoolwiseParentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate = edtViewSchoolwiseParentMessageDate.getText().toString();
                if (strDate.equals("")||strSchoolId.equals("")||strSchoolId.equals(null)) {
                    Toast.makeText(getActivity(), "Date Cannot be blank", Toast.LENGTH_SHORT).show();
                } else {
                    new GetParentMessage().execute();
                }
                //new GetCurAttendanceByDate().execute();
            }
        });

        edtViewSchoolwiseParentMessageDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtViewSchoolwiseParentMessageDate) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });
    }
    public class GetParentMessage extends AsyncTask<String, JSONArray, Void>{
        String url= AD.url.base_url+"noticeBoardOPerations.jsp";
        JSONObject outObject= new JSONObject();
        GetResponse response= new GetResponse();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching  parent messages please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for(int i=parentMessageArray.length();i>=0;i--){


                    parentMessageArray.remove(i);
                }
            }else {
                parentMessageArray= new JSONArray();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {

                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseParentMessage));


                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                noticeBoardData.put(getString(R.string.key_Date), strDate);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));

                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray resultArray= inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<=resultArray.length();i++){
                        parentMessageArray.put(resultArray.getJSONObject(i));
                    }
                    //publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast= Toast.makeText(getActivity(),"Something went wrong while fetching the parent message",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                });
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            initRecylerAdpater();
        }
    }
}
