package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditStaffReminderActvity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditStudentReminderActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ParentNoteActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.StudentReminderActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReminderRecylcerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.ReminderRecylcerViewAdapterBelowKitkat;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentListSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class CurrentStudReminder extends Fragment {
    Spinner spnrCurStuRmndClass, spnrCurStuRmndSection, spnrCurStuRmndStudName;
    Button btnGetCurStuRmnd;
    String strClass, strClassId, strSchoolId, strStatus, strStudentId, strAcademicYearId;
    String strReminderId, strStaffId;
    private Session session;
    RecyclerView recyclerViewCurStuRmnd;
    JSONArray reminderArray = new JSONArray();
    JSONArray studentListArray = new JSONArray();
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    ProgressDialog progressDialog;
    ReminderRecylcerViewAdapter reminderAdapter;
    ReminderRecylcerViewAdapterBelowKitkat remniderViewAdapterBelowKitkat;
    boolean isFragmentLoaded = false;
    int roleId;
    CheckConnection connection = new CheckConnection();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_stud_reminder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {

            //new GetClass().execute();
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            roleId = Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            init();
            allEvents();
            new GetClass().execute();
        }
    }


    public void init() {

        btnGetCurStuRmnd = (Button) getActivity().findViewById(R.id.btnGetCurStuRmnd);
        spnrCurStuRmndClass = (Spinner) getActivity().findViewById(R.id.spnrCurStuRmndClass);
        spnrCurStuRmndSection = (Spinner) getActivity().findViewById(R.id.spnrCurStuRmndSection);
        spnrCurStuRmndStudName = (Spinner) getActivity().findViewById(R.id.spnrCurStuRmndStudName);
        recyclerViewCurStuRmnd = (RecyclerView) getActivity().findViewById(R.id.recyclerViewCurStuRmnd);

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {

            reminderAdapter = new ReminderRecylcerViewAdapter(reminderArray, getActivity(), roleId);
            reminderAdapter.setOnButtonClickListner(new ReminderRecylcerViewAdapter.OnReminderViewClickListener() {
                @Override
                public void onEdit(JSONObject ReminderData, int position, String reminderId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit this Staff Reminder ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), EditStudentReminderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ReminderData", ReminderData.toString());
                            bundle.putString("SchoolId", strSchoolId);
                            bundle.putString("StaffId", strStaffId);
                            bundle.putString("AcademicYearId", strAcademicYearId);
                            bundle.putString("ClassId", strClassId);
                            bundle.putString("StudentList", studentListArray.toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog = builder.create();
                    dailog.show();
                }

                @Override
                public void onDelete(int position, String reminderId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to Delete this Staff Reminder ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            strReminderId = reminderId;
                            new DeleteStudentReminderId().execute();
                            Log.e("u clicked onDelete", String.valueOf(position));
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog = builder.create();
                    dailog.show();
                }

            });
            RecyclerView.LayoutManager rmdrLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewCurStuRmnd.setLayoutManager(rmdrLytMngr);
            recyclerViewCurStuRmnd.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurStuRmnd.setAdapter(reminderAdapter);


        } else {

            remniderViewAdapterBelowKitkat = new ReminderRecylcerViewAdapterBelowKitkat(reminderArray, getActivity(), roleId);
            remniderViewAdapterBelowKitkat.setOnButtonClickListner(new ReminderRecylcerViewAdapterBelowKitkat.OnReminderViewClickListener() {
                @Override
                public void onEdit(JSONObject ReminderData, int position, String reminderId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit this Staff Reminder ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), EditStudentReminderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ReminderData", ReminderData.toString());
                            bundle.putString("SchoolId", strSchoolId);
                            bundle.putString("StaffId", strStaffId);
                            bundle.putString("AcademicYearId", strAcademicYearId);
                            bundle.putString("ClassId", strClassId);
                            bundle.putString("StudentList", studentListArray.toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog = builder.create();
                    dailog.show();
                }

                @Override
                public void onDelete(int position, String reminderId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to Delete this Staff Reminder ?..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            strReminderId = reminderId;
                            new DeleteStudentReminderId().execute();
                            Log.e("u clicked onDelete", String.valueOf(position));
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dailog = builder.create();
                    dailog.show();
                }

            });
            RecyclerView.LayoutManager rmdrLytMngr = new LinearLayoutManager(getActivity());
            recyclerViewCurStuRmnd.setLayoutManager(rmdrLytMngr);
            recyclerViewCurStuRmnd.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCurStuRmnd.setAdapter(remniderViewAdapterBelowKitkat);

        }


    }

    private void allEvents() {
        spnrCurStuRmndClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrCurStuRmndClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }else{
                    spnrCurStuRmndSection.setAdapter(null);
                    spnrCurStuRmndStudName.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrCurStuRmndSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (strStatus.equals("Failure")) {
                    spnrCurStuRmndSection.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrCurStuRmndSection.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    strClassId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "" + strClassId);
                    //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                    int pos = adapterView.getSelectedItemPosition();
                    String strSection=tmpView.getText().toString();
                    if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                        new GetStudentList().execute();
                    }else{

                        spnrCurStuRmndStudName.setAdapter(null);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrCurStuRmndStudName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spnrCurStuRmndStudName == null) {
                    spnrCurStuRmndStudName.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrCurStuRmndStudName.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    // strClassId = adapterView.getItemAtPosition(i).toString();
                    strStudentId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "studentId" + strStudentId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGetCurStuRmnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clsPos = spnrCurStuRmndClass.getSelectedItemPosition();
                int secPos = spnrCurStuRmndSection.getSelectedItemPosition();

                if((clsPos==0||clsPos==-1)||(secPos==0||secPos==-1)){
                    Toast toast= Toast.makeText(getActivity(),"select Class, section and student ",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{
                    new GetCurReminder().execute();
                }


            }
        });
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
                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));

                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                }
                else {
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
                            builder.setMessage("classes not Found");
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
            spnrCurStuRmndClass.setPrompt("Choose Class");
            spnrCurStuRmndClass.setAdapter(adapter);
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
                strStatus = inObject.getString(getString(R.string.key_Status));
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
            spnrCurStuRmndSection.setPrompt("Choose Section");
            spnrCurStuRmndSection.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Student list...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseStudentsList));
                JSONObject classSubjectData = new JSONObject();
                classSubjectData.put(getString(R.string.key_ClassId), strClassId);
                outObject.put(getString(R.string.key_classSubjectData), classSubjectData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                String strMessage=inObject.getString(getString(R.string.key_Message));
                String strRollNotSet=inObject.getString(getString(R.string.key_Roll_Set));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray = inObject.getJSONArray(getString(R.string.key_Result));
                    studentListArray = new JSONArray();
                    for (int i = 0; i < resultArray.length(); i++) {
                        studentListArray.put(resultArray.getJSONObject(i));
                    }
                }else if(strRollNotSet.equalsIgnoreCase("1")){
                    showRollNoStatus(strStatus,strMessage);
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
            StudentListSpinnerAdapter adapter = new StudentListSpinnerAdapter(values[0], getActivity());
            spnrCurStuRmndStudName.setPrompt("Choose Section");
            spnrCurStuRmndStudName.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        public void showRollNoStatus(String strTitle, String strMessage) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom, null);
                    builder.setView(convertView);
                    builder.setCancelable(false);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(getActivity(), StudentReminderActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setTitle(strTitle);
                    builder.setMessage(strMessage);
                    builder.show();
                }
            });

        }
    }

    private class GetCurReminder extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Current Reminder...");
            progressDialog.show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                for (int i = reminderArray.length(); i >= 0; i--) {

                    reminderArray.remove(i);
                }


            } else {
                reminderArray = new JSONArray();
            }

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_studentReminderDisplay_studentwise));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_StudentId), strStudentId);
                noticeBoardData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("TAG", "GetCurReminder,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetCurReminder,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    // publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                    JSONArray resultArray = inObject.getJSONArray(getString(R.string.key_Result));
                    for (int i = 0; i < resultArray.length(); i++) {
                        reminderArray.put(resultArray.getJSONObject(i));
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
            progressDialog.dismiss();
            int version = android.os.Build.VERSION.SDK_INT;
            int versionKitkat =  android.os.Build.VERSION_CODES.KITKAT;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                reminderAdapter.notifyDataSetChanged();
            } else {
                remniderViewAdapterBelowKitkat = new ReminderRecylcerViewAdapterBelowKitkat(reminderArray, getActivity(), roleId);
                remniderViewAdapterBelowKitkat.setOnButtonClickListner(new ReminderRecylcerViewAdapterBelowKitkat.OnReminderViewClickListener() {
                    @Override
                    public void onEdit(JSONObject ReminderData, int position, String reminderId) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to edit this Staff Reminder ?..");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), EditStudentReminderActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ReminderData", ReminderData.toString());
                                bundle.putString("SchoolId", strSchoolId);
                                bundle.putString("StaffId", strStaffId);
                                bundle.putString("AcademicYearId", strAcademicYearId);
                                bundle.putString("ClassId", strClassId);
                                bundle.putString("StudentList", studentListArray.toString());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dailog = builder.create();
                        dailog.show();
                    }

                    @Override
                    public void onDelete(int position, String reminderId) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to Delete this Staff Reminder ?..");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                strReminderId = reminderId;
                                new DeleteStudentReminderId().execute();
                                Log.e("u clicked onDelete", String.valueOf(position));
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dailog = builder.create();
                        dailog.show();
                    }

                });
                RecyclerView.LayoutManager rmdrLytMngr = new LinearLayoutManager(getActivity());
                recyclerViewCurStuRmnd.setLayoutManager(rmdrLytMngr);
                recyclerViewCurStuRmnd.setItemAnimator(new DefaultItemAnimator());
                recyclerViewCurStuRmnd.setAdapter(remniderViewAdapterBelowKitkat);
            }

        }
    }

    class DeleteStudentReminderId extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Deleting the Student's reminder...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url = AD.url.base_url + "noticeBoardOperation.jsp";
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_deleteStudentReminder));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_Id), strReminderId);

                outObject.put("noticeBoardData", noticeBoardData);
                Log.e("TAG", "Delete Staff Reminder,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "Delete Staff Reminder,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                String strMessage = "";
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success)) || strStatus.equalsIgnoreCase(getString(R.string.key_Fail))) {
                    strMessage = inObject.getString(getString(R.string.key_Message));
                    showAlert(strMessage, strStatus);
                    //publishProgress(new JSONObject(respText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    showAlert("Something went wrong Try again", "Error");
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
