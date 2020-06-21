package com.vinuthana.vinvidyaadmin.activities.useractivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Validation;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    public Validation validation = new Validation();
    EditText edtxbRegisterUname, edtxbRegisterPwd, edtxbRegisterEmail, edtxbRegisterPhoneNo, edtxbRegisterConfrmPwd, edtxbRegisterName;
    Button btnRegister;
    TextView tvLoginRegister;
    String strEmail, strPhoneNumber, strUserName, strPassword, strCnfrmPassword, strName;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (!connection.netInfo(RegisterActivity.this)) {
            connection.buildDialog(RegisterActivity.this).show();
        } else {
            init();
            allEvents();
        }

    }

    private void init() {
        edtxbRegisterName = (EditText) findViewById(R.id.edtxbRegisterName);
        edtxbRegisterUname = (EditText) findViewById(R.id.edtxbRegisterUname);
        edtxbRegisterPwd = (EditText) findViewById(R.id.edtxbRegisterPwd);
        edtxbRegisterEmail = (EditText) findViewById(R.id.edtxbRegisterEmail);
        edtxbRegisterPhoneNo = (EditText) findViewById(R.id.edtxbRegisterPhoneNo);
        edtxbRegisterConfrmPwd = (EditText) findViewById(R.id.edtxbRegisterConfrmPwd);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvLoginRegister = (TextView) findViewById(R.id.tvLogin);
    }

    private void allEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = edtxbRegisterName.getText().toString();
                strUserName = edtxbRegisterUname.getText().toString();
                strPassword = edtxbRegisterPwd.getText().toString();
                strCnfrmPassword = edtxbRegisterConfrmPwd.getText().toString();
                strEmail = edtxbRegisterEmail.getText().toString();
                strPhoneNumber = edtxbRegisterPhoneNo.getText().toString();
                if (strName.equals("") && strUserName.equals("") && strPassword.equals("") && strCnfrmPassword.toString().equals("") && strEmail.equals("") && strPhoneNumber.equals("")) {
                    //Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    edtxbRegisterName.setError("Enter Name");
                    edtxbRegisterUname.setError("Enter User Name");
                    edtxbRegisterPwd.setError("Enter Password");
                    edtxbRegisterConfrmPwd.setError("Password does not match");
                    //edtxbRegisterEmail.setError("Please Enter Valid Email");
                    edtxbRegisterPhoneNo.setError("Please Enter Valid Phone Number");
                }
                /*if (!validation.isValidEmail(strUserId)) {
                    edtxbRegisterEmail.setError("Please Enter Valid Email");
                }*/
                if (!validation.isValidMobile(strPhoneNumber)) {
                    edtxbRegisterPhoneNo.setError("Please Enter Valid Phone Number");
                }
                new AddUser().execute();
            }
        });

        edtxbRegisterConfrmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = edtxbRegisterPwd.getText().toString();
                String cpwd = edtxbRegisterConfrmPwd.getText().toString();
                if (!cpwd.equals(pwd)) {
                    edtxbRegisterConfrmPwd.setError("Password do not match");
                } /*else {
                    edtxbRegisterConfrmPwd.setError("Password do not match");
                }*/
            }
        });

        tvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showAlert(String alertMessage, String alertTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

    class AddUser extends AsyncTask<String, JSONObject, Void> {
        String url = AD.url.base_url + "userOperations.jsp";
        String message = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Registering the User");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject outObject = new JSONObject();
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_addUser));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_name), strName);
                userData.put(getString(R.string.key_userName), strUserName);
                userData.put(getString(R.string.key_password), strPassword);
                userData.put(getString(R.string.key_email), strEmail);
                userData.put(getString(R.string.key_mobileNumber), strPhoneNumber);
                outObject.put(getString(R.string.key_userData), userData);
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(respText);
                message = inObject.getString(getString(R.string.key_Message));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String alertMessage, alertTitle;
                        if (message.equals("User added successfully")) {
                            alertMessage = message;
                            alertTitle = "Success";
                        } else if (message.equals("User could not be added")) {
                            alertMessage = message;
                            alertTitle = "Warning";
                        } else {
                            alertTitle = "Error";
                            alertMessage = "Error Occured While Adding the user..";
                        }
                        showAlert(alertMessage, alertTitle);
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
            progressDialog.dismiss();
            edtxbRegisterUname.setText("");
            edtxbRegisterPwd.setText("");
            edtxbRegisterConfrmPwd.setText("");
            edtxbRegisterEmail.setText("");
            edtxbRegisterPhoneNo.setText("");
        }

        @Override
        protected void onProgressUpdate(JSONObject... values) {
            super.onProgressUpdate(values);
        }
    }
}
