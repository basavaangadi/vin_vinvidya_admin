package com.vinuthana.vinvidyaadmin.activities.useractivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONObject;

import java.util.HashMap;


public class ChangePasswordActivity extends AppCompatActivity {
    EditText edtxbOldPwdChangePwd, edtxbNewPwdChangePwd, edtxbCnfrmPwdChangePwd;
    Button btnChangePassword;
    TextView tvLogin;
    public String strPassword, strConfrmPwd, strOldPwd;
    public String oldPwd, newPwd, confrmPwd;
    public String strUserName;
    private Session session;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.changePwdToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        if (!connection.netInfo(ChangePasswordActivity.this)) {
            connection.buildDialog(ChangePasswordActivity.this).show();
        } else {
            init();
            allEvents();

            session = new Session(this);
            //Toast.makeText(ListActivity_new.this, "User Login Status: " + session.loggedin(), Toast.LENGTH_SHORT).show();
            session.checkLogin();
            HashMap<String, String> user = session.getUserDetails();
            strUserName = user.get(Session.KEY_STAFF_ID);
        }
    }

    private void init() {
        edtxbOldPwdChangePwd = (EditText) findViewById(R.id.edtxbOldPwdChangePwd);
        edtxbNewPwdChangePwd = (EditText) findViewById(R.id.edtxbNewPwdChangePwd);
        edtxbCnfrmPwdChangePwd = (EditText) findViewById(R.id.edtxbCnfrmPwdChangePwd);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
    }

    private void allEvents() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOldPwd = edtxbOldPwdChangePwd.getText().toString();
                strPassword = edtxbNewPwdChangePwd.getText().toString();
                strConfrmPwd = edtxbCnfrmPwdChangePwd.getText().toString();
                if (strOldPwd.equals("") || strPassword.equals("") || strConfrmPwd.equals("")) {
                    //Toast.makeText(ChangePasswordActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                    edtxbOldPwdChangePwd.setError("Enter Old Password");
                    edtxbNewPwdChangePwd.setError("Enter New Password");
                    edtxbCnfrmPwdChangePwd.setError("Enter Confirm Password");
                } else {
                    new ChangePassword().execute();
                }
            }
        });

        edtxbCnfrmPwdChangePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                strPassword = edtxbNewPwdChangePwd.getText().toString();
                strConfrmPwd = edtxbCnfrmPwdChangePwd.getText().toString();
                if (!strConfrmPwd.equals(strPassword)) {
                    edtxbCnfrmPwdChangePwd.setError("Password Do Not Match");
                }
            }
        });
    }

    class ChangePassword extends AsyncTask<String, JSONObject, Void> {
        ProgressDialog progressDialog;
        String url = AD.url.base_url + "userOperations.jsp";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChangePasswordActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Changing the Password");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_changeStaffPassword));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_StaffId), strUserName);
                userData.put(getString(R.string.key_NewPassword), strPassword);
                userData.put(getString(R.string.key_CurPassword), strOldPwd);
                outObject.put(getString(R.string.key_userData), userData);
                Log.e("TAG", "ChangePassword, doInBackground, outObject =" + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "ChangePassword, doInBackground, respText =" + respText);
                JSONObject inObject = new JSONObject(respText);
                message = inObject.getString(getString(R.string.key_Message));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (message.equals("Password changed successfully")) {
                            alertMessage = message;
                            alertTitle = "Success";
                        } else if (message.equals("Password could not be Changed")) {
                            alertMessage = message;
                            alertTitle = "Warning";
                        } else {
                            alertTitle = "Error";
                            alertMessage = "Error Occurred While Changing Password..";
                        }
                        showAlert(alertMessage, alertTitle);
                        //Toast.makeText(ChangePasswordActivity.this, "Passwor Changed Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (edtxbOldPwdChangePwd.equals("") || edtxbNewPwdChangePwd.equals("") || edtxbCnfrmPwdChangePwd.equals("")) {
                //showAlert("Please Enter all fields","Change Password");
                Toast.makeText(ChangePasswordActivity.this, "Please Enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                //showAlert("Password Successfully Changed","Change Password");
                Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            }
            edtxbOldPwdChangePwd.setText("");
            edtxbNewPwdChangePwd.setText("");
            edtxbCnfrmPwdChangePwd.setText("");
        }

        @Override
        protected void onProgressUpdate(JSONObject... values) {
            super.onProgressUpdate(values);
        }
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
