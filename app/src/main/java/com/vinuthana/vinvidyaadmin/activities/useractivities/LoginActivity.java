package com.vinuthana.vinvidyaadmin.activities.useractivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.extraactivities.MainActivity;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvForgotPassword, tvSignUp;
    public SharedPreferences preferences;
    SharedPreferences.Editor edit;
    private ProgressDialog progressDialog;
    String strStatus = "";
    private Session session;
    String strUserId, strPassword;
    JSONArray result;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackgrounds();
        setContentView(R.layout.activity_login);
        init();
        if (!connection.netInfo(LoginActivity.this)) {
            connection.buildDialog(LoginActivity.this).show();
        } else {
            allEvents();
        }
        if (session.loggedin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void setBackgrounds() {
        if (Build.VERSION.SDK_INT >= 19) {
              //  forgot.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_ripple));

        }
    }
    public void init() {
        session = new Session(this);
        //tvSignUp = (TextView) findViewById(R.id.tvSignUp);
       // tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    private void allEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        etUsername.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        /*tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        /*tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
                finish();
            }
        });*/
    }

    public void login() {
        strUserId = etUsername.getText().toString();
        strPassword = etPassword.getText().toString();
        if (strUserId.equals("") || strPassword.equals("")) {
            etUsername.setError("Enter Username");
            etPassword.setError("Enter Password");
        } else if (strUserId.equals("")) {
            etUsername.setError("Enter Username");
        } else if (strPassword.equals("")) {
            etPassword.setError("Enter Password");
        } else {
            new GetUserDetails().execute();

            /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();*/
        }
    }

    private class GetUserDetails extends AsyncTask<String, JSONArray, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "userOperations.jsp";
        Context mContext;
        SharedPreferences pref;
        String status = "";

        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait Logging In...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_authStaff));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_StaffId), strUserId);
                userData.put(getString(R.string.key_Password), strPassword);
                outObject.put(getString(R.string.key_userData), userData);
                Log.e("TAG", "GetUserDetails, doInBackground, outObject =" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "GetUserDetails, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                result = inObject.getJSONArray("result");
                strStatus = result.getJSONObject(0).getString(getString(R.string.key_Status));
                getDetails(strStatus);
                Toast.makeText(LoginActivity.this, strStatus, Toast.LENGTH_SHORT);
                publishProgress(result);


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
            if (strStatus.equals("Success")) {
                getDetails(strStatus);

            } else {
                Toast.makeText(getApplicationContext(), "Wrong UserName/Password", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        public void getDetails(String strStatus) {
            if (strStatus.equals("Success")) {
                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject values = result.getJSONObject(i);
                        String strName = values.getString(getString(R.string.key_name));
                        String strPhoneNumber = values.getString(getString(R.string.key_PhoneNo));
                        String strUserId = values.getString(getString(R.string.key_StaffId));
                        String strSchId = values.getString(getString(R.string.key_SchoolId));
                        String strEmail = values.getString(getString(R.string.key_email));
                        //String strDesignation = values.getString("Designation");
                        String strStaffDetailsId = values.getString(getString(R.string.key_StaffDetailsId));
                        String strAcademicYearId = values.getString(getString(R.string.key_AcademicYearId));
                        String strRoleId = values.getString(getString(R.string.key_RoleId));
                        String strDesignation = values.getString(getString(R.string.key_Designation));
                        String strSchool = values.getString(getString(R.string.key_School));
                        //String strStaffDetailsId = values.getString("StaffDetailsId");
                        /*for (int j = 0; j < list.size(); j++) {
                            session.createLoginSession();
                        }*/
                        session.createLoginSession(strName, strPhoneNumber, strUserId, strSchId, strStaffDetailsId, strEmail,
                                strAcademicYearId, strRoleId,strDesignation ,strSchool);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(intent);
                        finish();
                        Log.e("OK", "take the data");
                        Log.e("Login doinbackground", result.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e("error", "Error");
            }
        }
    }
}
