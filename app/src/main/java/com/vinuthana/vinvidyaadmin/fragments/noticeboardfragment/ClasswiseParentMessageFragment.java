package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog;

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

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditParentsNoteActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ParentMessageActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.NoticeBoardAdapters.ParentMessageAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PrntNtRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PrntNtRecyclerViewAdapterBelowKitKat;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentListSpinnerAdapter;
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
public class ClasswiseParentMessageFragment extends Fragment {

    Spinner spnrViewClasswiseParentMessageClass, spnrViewClasswiseParentMessageSection, spnrCurParentNtStudName;
    Button btnViewClasswiseParentMessage;
    String strClass, strClassId, strSchoolId, strStatus, strStudentId;
    String  strDate, strAcademicYearId,strStaffId;
    String strNoticeId;
    DatePickerDialog datePickerDialogFrmDt;
    EditText edtViewClasswiseParentMessageDate;
    private Session session;
    RecyclerView recyclerViewClasswiseParentMessage;
    JSONArray parentNoteArray=new JSONArray();
    JSONArray studentArrayList= new JSONArray();
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();

    ParentMessageAdapter parentMessageAdapter;

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtViewClasswiseParentMessageDate.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialogFrmDt.dismiss();
        }
    };


    CheckConnection connection = new CheckConnection();
    public ClasswiseParentMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classwise_parent_message, container, false);
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
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            init();
            allEvents();

            new GetClass().execute();
        }
    }
        public void init() {

            btnViewClasswiseParentMessage = (Button) getActivity().findViewById(R.id.btnViewClasswiseParentMessage);
            spnrViewClasswiseParentMessageClass = (Spinner) getActivity().findViewById(R.id.spnrViewClasswiseParentMessageClass);
            spnrViewClasswiseParentMessageSection = (Spinner) getActivity().findViewById(R.id.spnrViewClasswiseParentMessageSection);
            edtViewClasswiseParentMessageDate = (EditText) getActivity().findViewById(R.id.edtViewClasswiseParentMessageDate);
           // spnrCurParentNtStudName = (Spinner) getActivity().findViewById(R.id.spnrCurParentNtStudName);
            recyclerViewClasswiseParentMessage = (RecyclerView) getActivity().findViewById(R.id.recyclerViewClasswiseParentMessage);



                parentMessageAdapter = new ParentMessageAdapter(parentNoteArray, getActivity());
                parentMessageAdapter.setOnButtonClickListener(new ParentMessageAdapter.onParentMessageClickListener() {
                    @Override
                    public void onReply(JSONObject noticeData, int position, String noticeId) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to edit this Parent's note ?..");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                /*Intent intent= new Intent(getActivity(), EditParentsNoteActivity.class);
                                Bundle bundle= new Bundle();
                                bundle.putString("NoticeData",noticeData.toString());
                                bundle.putString("SchoolId",strSchoolId);
                                bundle.putString("StaffId",strStaffId);
                                bundle.putString("AcademicYearId",strAcademicYearId);
                                bundle.putString("StudentList",studentArrayList.toString());
                                bundle.putString("ClassId",strClassId);
                                intent.putExtras(bundle);
                                startActivity(intent);*/
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
                recyclerViewClasswiseParentMessage.setLayoutManager(prntNtLytMngr);
                recyclerViewClasswiseParentMessage.setItemAnimator(new DefaultItemAnimator());
                recyclerViewClasswiseParentMessage.setAdapter(parentMessageAdapter);





        }

    private void allEvents() {
        spnrViewClasswiseParentMessageClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrViewClasswiseParentMessageClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                new GetSection().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrViewClasswiseParentMessageSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (strStatus.equals("Failure")) {
                    spnrViewClasswiseParentMessageSection.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrViewClasswiseParentMessageSection.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    strClassId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "" + strClassId);
                    //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                   // new GetStudentList().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*spnrCurParentNtStudName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spnrViewClasswiseParentMessageSection == null) {
                    spnrCurParentNtStudName.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrCurParentNtStudName.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    //strClassId = adapterView.getItemAtPosition(i).toString();
                    strStudentId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "" + strStudentId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        btnViewClasswiseParentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDate=edtViewClasswiseParentMessageDate.getText().toString();

                new GetParentMessage().execute();
            }
        });


        edtViewClasswiseParentMessageDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtViewClasswiseParentMessageDate) {
                    datePickerDialogFrmDt.show();
                    return true;
                }
                return false;
            }
        });
    }


    class GetClass extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";
        ProgressDialog progressDialog;
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
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
                String strStatus = inObject.getString(getString(R.string.key_Status));
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
            ClassSpinnerDataAdapter adapter = new ClassSpinnerDataAdapter(values[0], getActivity());
            spnrViewClasswiseParentMessageClass.setPrompt("Choose Class");
            spnrViewClasswiseParentMessageClass.setAdapter(adapter);
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
        @Override
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
            spnrViewClasswiseParentMessageSection.setPrompt("Choose Section");
            spnrViewClasswiseParentMessageSection.setAdapter(adapter);
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching student list...");
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
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                    studentArrayList=new JSONArray();

                    for(int i=0;i<resultArray.length();i++){
                        studentArrayList.put(resultArray.getJSONObject(i));
                    }
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
            StudentListSpinnerAdapter adapter = new StudentListSpinnerAdapter(values[0], getActivity());
            spnrCurParentNtStudName.setPrompt("Choose Student");
            spnrCurParentNtStudName.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    private class GetParentMessage extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Fetching Parent Note...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for(int i=parentNoteArray.length();i>=0;i--){


                    parentNoteArray.remove(i);
                }
            }else {
                parentNoteArray= new JSONArray();
            }

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getClasswiseParentMessage));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_ClassId), strClassId);
                noticeBoardData.put(getString(R.string.key_Date), strDate);
                outObject.put("noticeBoardData", noticeBoardData);
                Log.e("TAG", "GetParentNote,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetParentNote,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray parentArray=inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<parentArray.length();i++){
                        parentNoteArray.put(parentArray.getJSONObject(i));
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
                            builder.setNegativeButton("Ok", null);
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
                /*JSONArray result = inObject.getJSONArray(getString(R.string.key_Result));
                publishProgress(result);*/
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

                parentMessageAdapter.notifyDataSetChanged();

            }else{
                parentMessageAdapter = new ParentMessageAdapter(parentNoteArray, getActivity());
                parentMessageAdapter.setOnButtonClickListener(new ParentMessageAdapter.onParentMessageClickListener() {
                    @Override
                    public void onReply(JSONObject noticeData, int position, String noticeId) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to edit this Parent's note ?..");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent= new Intent(getActivity(), EditParentsNoteActivity.class);
                                Bundle bundle= new Bundle();
                                bundle.putString("NoticeData",noticeData.toString());
                                bundle.putString("SchoolId",strSchoolId);
                                bundle.putString("StaffId",strStaffId);
                                bundle.putString("AcademicYearId",strAcademicYearId);
                                bundle.putString("StudentList",studentArrayList.toString());
                                bundle.putString("ClassId",strClassId);
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


                });
                RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
                recyclerViewClasswiseParentMessage.setLayoutManager(prntNtLytMngr);
                recyclerViewClasswiseParentMessage.setItemAnimator(new DefaultItemAnimator());
                recyclerViewClasswiseParentMessage.setAdapter(parentMessageAdapter);
            }


        }
    }



}
