package com.vinuthana.vinvidyaadmin.activities.useractivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Validation;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView tvLogin;
    EditText edtEmailAddress;
    Button btnContinue;
    String email;
    public Validation validation = new Validation();
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.forgotPwdToolbar);
        setSupportActionBar(toolbar);*/
        if (!connection.netInfo(ForgotPasswordActivity.this)) {
            connection.buildDialog(ForgotPasswordActivity.this).show();
        } else {
            init();
            allEvents();
        }

    }

    private void init() {
        btnContinue = (Button) findViewById(R.id.btnContinue);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
    }

    private void allEvents() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmailAddress.getText().toString();
                boolean hasNonAlpha = email.matches("^.*[^a-zA-Z0-9 ].*$");
                if (hasNonAlpha) {
                    Toast.makeText(ForgotPasswordActivity.this, "Entered Email id", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Entered Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = edtEmailAddress.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
