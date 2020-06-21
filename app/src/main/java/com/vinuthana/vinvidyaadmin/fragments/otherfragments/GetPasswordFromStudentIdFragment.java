package com.vinuthana.vinvidyaadmin.fragments.otherfragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditParentsNoteActivity;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.ParentNoteActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.AssignmentActivity;
import com.vinuthana.vinvidyaadmin.activities.otheractivities.StudentCredentialsActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PrntNtRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentCredentialsAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentListSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetPasswordFromStudentIdFragment extends Fragment {
    Spinner spnrGetPasswordFrmStudIdClass, spnrGetPasswordFrmStudIdSection, spnrGetPasswordFrmStudIdStudName;
    Button btnGetGetPasswordFrmStudId;
    RecyclerView recyclerViewGetPasswordFrmStudId;
    private Session session;
    StudentCredentialsAdapter studentCredentialsAdapter;
    JSONArray studentCredentialsArray=new JSONArray();
    JSONArray studentArrayList= new JSONArray();
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    String strClass, strClassId, strSchoolId, strStatus,strMessage ,strStudentId,strPhoneNo,strPassword;
    String strAcademicYearId,strStaffId, strUserId,strUpdateStatus,strUpdateMessage;
    int resultId;
    public GetPasswordFromStudentIdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_get_password_from_student_id, container, false);
        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();

        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
        strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
        init(view);
        allEvents();
       // new GetClass().execute();

         return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser) {
                new GetClass().execute();

            }
        }
    }
    private void allEvents() {
        spnrGetPasswordFrmStudIdClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrGetPasswordFrmStudIdClass.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strClass = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strClass);
                //Toast.makeText(getActivity(), strClass + " You Clicked on", Toast.LENGTH_SHORT).show();
                int pos = adapterView.getSelectedItemPosition();
                if(!(strClass.equalsIgnoreCase("Select Class")||pos==0)){
                    new GetSection().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrGetPasswordFrmStudIdSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (strStatus.equals("Failure")) {
                    spnrGetPasswordFrmStudIdSection.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrGetPasswordFrmStudIdSection.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    strClassId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "" + strClassId);
                    //Toast.makeText(getActivity(), strClassId + " You Clicked on", Toast.LENGTH_SHORT).show();
                    int pos = adapterView.getSelectedItemPosition();
                    String strSection=tmpView.getText().toString();
                    if(!(strSection.equalsIgnoreCase("Select Section")||pos==0)){
                        new GetStudentList().execute();
                    }



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrGetPasswordFrmStudIdStudName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spnrGetPasswordFrmStudIdSection == null) {
                    spnrGetPasswordFrmStudIdStudName.setAdapter(null);
                } else {
                    TextView tmpView = (TextView) spnrGetPasswordFrmStudIdStudName.getSelectedView().findViewById(R.id.list);
                    tmpView.setTextColor(Color.WHITE);
                    //strClassId = adapterView.getItemAtPosition(i).toString();
                    strStudentId = adapterView.getItemAtPosition(i).toString();
                    Log.e("Tag", "" + strStudentId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGetGetPasswordFrmStudId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetStudentCredentials().execute();
            }
        });
    }


    public void ShowAlert(int position,JSONObject jsonData) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.alert_student_credential);
        TextView tvRollNo = (TextView) dialog.findViewById(R.id.tvRollNoStudCred);
        TextView tvClass = (TextView) dialog.findViewById(R.id.tvClass);
        EditText editPhoneNo = (EditText) dialog.findViewById(R.id.editPhoneNo);
        EditText editPassword = (EditText) dialog.findViewById(R.id.editPassword);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        try {
            String strClass=jsonData.getString("Class");
        String strStudName=jsonData.getString("StudentName");
        dialog.setTitle(getString(R.string.key_Edit));
        String strRollNo = "";
        if (jsonData.getString("RollNo")==null ||jsonData.getString("RollNo").equals("null"))
            strRollNo = strStudName;
        else
            strRollNo=jsonData.getString("RollNo")+" "+strStudName;
        tvRollNo.setText(strRollNo);
        tvClass.setText(strClass);
        String strPassword=jsonData.getString(getString(R.string.key_Password));
        if(strPassword.equalsIgnoreCase("-9999")){
            strPassword="unregistered";
        }
            editPhoneNo.setText(jsonData.getString(getString(R.string.key_PhoneNumber)));
            editPassword.setText(strPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // if button is clicked, close the custom dialog
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    strPhoneNo = editPhoneNo.getText().toString();
                    strPassword = editPassword.getText().toString();
                    if(strPassword.equalsIgnoreCase("unregistered")){
                        strPassword="-9999";
                    }
                   new updateStudentCredentials().execute();
                } catch (Exception e) {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void init(View view) {

        btnGetGetPasswordFrmStudId = (Button) view.findViewById(R.id.btnGetGetPasswordFrmStudId);
        spnrGetPasswordFrmStudIdClass = (Spinner) view.findViewById(R.id.spnrGetPasswordFrmStudIdClass);
        spnrGetPasswordFrmStudIdSection = (Spinner) view.findViewById(R.id.spnrGetPasswordFrmStudIdSection);
        spnrGetPasswordFrmStudIdStudName = (Spinner) view.findViewById(R.id.spnrGetPasswordFrmStudIdStudName);
        recyclerViewGetPasswordFrmStudId = (RecyclerView) view.findViewById(R.id.recyclerViewGetPasswordFrmStudId);
        initRecylerAdapter();
    }

    public void initRecylerAdapter(){

        studentCredentialsAdapter= new StudentCredentialsAdapter(studentCredentialsArray, getActivity());

        studentCredentialsAdapter.setOnButtonClickListener(new StudentCredentialsAdapter.onCredentialsClickListner() {
            @Override
            public void onEdit(JSONObject credntialsData, int position, String strStudentId) {
                try {
                    String strPassword=credntialsData.getString(getString(R.string.key_Password));
                    String strPhoneNumber=credntialsData.getString(getString(R.string.key_PhoneNumber));

                    if(strPhoneNumber.equalsIgnoreCase("-9999")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Confirmation");
                        alertDialogBuilder.setMessage("Would you like to Edit Student credentials");
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                                ShowAlert(position,credntialsData);

                            }
                        }).show();
                    }
                     else if(strPassword.equalsIgnoreCase("-9999")){
                        Toast toast= Toast.makeText(getActivity(),"Please ask Parent to register using parent app",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Would you like to Edit Student credentials");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                            ShowAlert(position,credntialsData);

                        }
                    }).show();
                    }
                }catch (Exception e){
                    Toast toast= Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });

        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        recyclerViewGetPasswordFrmStudId.setLayoutManager(prntNtLytMngr);
        recyclerViewGetPasswordFrmStudId.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGetPasswordFrmStudId.setAdapter(studentCredentialsAdapter);


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
            progressDialog.setMessage("Please wait fetching data...");
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
            spnrGetPasswordFrmStudIdClass.setPrompt("Choose Class");
            spnrGetPasswordFrmStudIdClass.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
        }
    }


    class GetSection extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

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
            spnrGetPasswordFrmStudIdSection.setPrompt("Choose Section");
            spnrGetPasswordFrmStudIdSection.setAdapter(adapter);
        }
    }

    class GetStudentList extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "classsubjectOperations.jsp";

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
                strMessage=inObject.getString(getString(R.string.key_Message));
                String strRollNotSet=inObject.getString(getString(R.string.key_Roll_Set));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray resultArray=inObject.getJSONArray(getString(R.string.key_Result));
                    studentArrayList=new JSONArray();

                    for(int i=0;i<resultArray.length();i++){
                        studentArrayList.put(resultArray.getJSONObject(i));
                    }
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));

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
            spnrGetPasswordFrmStudIdStudName.setPrompt("Choose Student");
            spnrGetPasswordFrmStudIdStudName.setAdapter(adapter);
        }

        public void showRollNoStatus(String strTitle, String strMessage){
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

                            Intent intent= new Intent(getActivity(), StudentCredentialsActivity.class);
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

    class updateStudentCredentials extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "userOperations.jsp";

        @Override
        protected Void doInBackground(String... params) {
            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_updateStudentCredentials));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_PhoneNumber),strPhoneNo);
                userData.put(getString(R.string.key_Password),strPassword);
                userData.put(getString(R.string.key_StudentId),strStudentId);
                outObject.put(getString(R.string.key_userData), userData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                Log.e("Tag", "response =" + responseText);
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
                Log.e("Tag", "responseText is =" + responseText);
              strUpdateStatus= inObject.getString(getString(R.string.key_Status));
               strUpdateMessage = inObject.getString(getString(R.string.key_Message));
               resultId=inObject.getInt(getString(R.string.key_ResultId));





            } catch (Exception ex) {
                ex.printStackTrace();
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
            if(resultId==-2||resultId==-1){
                showStatusAlert(strUpdateStatus,strUpdateMessage);
            }else {
                showStatusAlert("Error","something went wrong while updating credentials");
            }

        }

    }

    public  void  showStatusAlert(String title,String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("onCredentials", e.toString());
                    Toast.makeText(getActivity(), "onCredentials" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).show();

    }



    private class GetStudentCredentials extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;

        String url = AD.url.base_url + "userOperations.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Fetching Student Credentials...");
            progressDialog.show();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                for(int i=studentCredentialsArray.length();i>=0;i--){


                    studentCredentialsArray.remove(i);
                }
            }else {
                studentCredentialsArray= new JSONArray();
            }

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getUserIdPasswordByStudentId));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_StudentId), strStudentId);
                userData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_userData), userData);
                Log.e("TAG", "GetParentNote,doInBackground, outObject =" + outObject);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetParentNote,doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    JSONArray parentArray=inObject.getJSONArray(getString(R.string.key_Result));
                    for(int i=0;i<parentArray.length();i++){
                        studentCredentialsArray.put(parentArray.getJSONObject(i));
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

                studentCredentialsAdapter.notifyDataSetChanged();

            }else{

                initRecylerAdapter();

            }


        }
    }


}
