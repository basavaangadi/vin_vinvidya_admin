package com.vinuthana.vinvidyaadmin.fragments.otherfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StudentCredentialsAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class GetPasswordFromNumberFragment extends Fragment {
    EditText edtPhnumberstudCrdntls;
    Button btnGetCredentials;
    String strPhoneNumber,strAcademicYearId,strPhoneNo,strPassword,strSchoolId;
    RecyclerView rcylviwGetPswdFrmNumbr;
    Session session;
    StudentCredentialsAdapter studentCredentialsAdapter;

    String strStatusstrUpdateStatus,strUpdateMessage,strStatus,strUpdateStatus,strStudentId;
    int resultId;
    JSONArray studentCredentialsArray=new JSONArray();
    public GetPasswordFromNumberFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_get_password_from_number, container, false);
        init(view);
        AllEvent();
        return view;
    }

    public void init(View view) {
        edtPhnumberstudCrdntls = view.findViewById(R.id.edtPhnumberstudCrdntls);
        btnGetCredentials = view.findViewById(R.id.btnGetCredentials);
        rcylviwGetPswdFrmNumbr = view.findViewById(R.id.rcylviwGetPswdFrmNumbr);
        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        initRecylerAdapter();
    }

    public void AllEvent() {
        btnGetCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPhoneNumber  = edtPhnumberstudCrdntls.getText().toString();

                if(strPhoneNumber.length()!=10){
                    Toast toast=Toast.makeText(getActivity(),"Enter valid phone number",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{
                    new GetStudentCredentials().execute();
                }
            }
        });
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
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getUserIdPasswordByPhoneNumber));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_phoneNumber), strPhoneNumber);
                userData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                userData.put(getString(R.string.key_SchoolId), strSchoolId);
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

    public void initRecylerAdapter(){

        studentCredentialsAdapter= new StudentCredentialsAdapter(studentCredentialsArray, getActivity());

        studentCredentialsAdapter.setOnButtonClickListener(new StudentCredentialsAdapter.onCredentialsClickListner() {
            @Override
            public void onEdit(JSONObject credntialsData, int position, String StudentId) {
                try {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Would you like to Edit Student credentials");
                    alertDialogBuilder.setCancelable(true);
                    strStudentId = StudentId;
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                dialog.dismiss();
                                ShowAlert(position,credntialsData);
                            } catch (Exception e) {
                                Log.e("onCredentail", e.toString());
                                Toast.makeText(getActivity(), "onStudentEditCredentials" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).show();
                }catch (Exception e){
                    Toast toast= Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });



        RecyclerView.LayoutManager prntNtLytMngr = new LinearLayoutManager(getActivity());
        rcylviwGetPswdFrmNumbr.setLayoutManager(prntNtLytMngr);
        rcylviwGetPswdFrmNumbr.setItemAnimator(new DefaultItemAnimator());
        rcylviwGetPswdFrmNumbr.setAdapter(studentCredentialsAdapter);


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
            editPhoneNo.setText(jsonData.getString(getString(R.string.key_PhoneNumber)));
            editPassword.setText(jsonData.getString(getString(R.string.key_Password)));
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



}
