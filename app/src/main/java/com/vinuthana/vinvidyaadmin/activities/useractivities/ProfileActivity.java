package com.vinuthana.vinvidyaadmin.activities.useractivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Session session;
    TextView tvProfileFName, tvStaffId, tvProfilePNo, tvProfileEmail;
    TextView tvDesignation, tvSchoolId;
    String strFName, strStaffId, strPhoneNum, strEmail, strSchoolId,strSchool,strDesignation;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        if (!connection.netInfo(ProfileActivity.this)) {
            connection.buildDialog(ProfileActivity.this).show();
        } else {
            init();

            session = new Session(getApplicationContext());

            session.checkLogin();
            HashMap<String, String> user = session.getUserDetails();

            strFName = user.get(Session.KEY_NAME);
            strStaffId = user.get(Session.KEY_STAFF_ID);
            strPhoneNum = user.get(Session.KEY_PHONE_NO);
            strEmail = user.get(Session.KEY_EMAIL);
            strDesignation = user.get(Session.KEY_DESIGNATION);
            strSchool = user.get(Session.KEY_SCHOOL);
            tvProfileFName.setText(strFName);
            tvStaffId.setText(strStaffId);
            tvProfilePNo.setText(strPhoneNum);
            tvProfileEmail.setText(strEmail);
            tvSchoolId.setText(strSchool);
            tvDesignation.setText(strDesignation);
        }

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

    private void init() {
        tvProfileFName = (TextView) findViewById(R.id.tvProfileFName);
        tvSchoolId = (TextView) findViewById(R.id.tvSchoolId);
        tvProfilePNo = (TextView) findViewById(R.id.tvProfilePNo);
        tvProfileEmail = (TextView) findViewById(R.id.tvProfileEmail);
        tvStaffId = (TextView) findViewById(R.id.tvStaffId);
        tvDesignation = (TextView) findViewById(R.id.tvDesignation);
    }

}
