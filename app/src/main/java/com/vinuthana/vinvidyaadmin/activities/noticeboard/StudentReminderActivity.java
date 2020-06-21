package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentStudReminder;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.SetStudReminder;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.StudReminderByDate;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class StudentReminderActivity extends AppCompatActivity {
    ViewPager vpStudReminder;
    TabLayout tabStudReminder;
    private Session session;
    String strSchoolId, strStaffId, strRole;
    int role;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Reminder");
        if (!connection.netInfo(StudentReminderActivity.this)) {
            connection.buildDialog(StudentReminderActivity.this).show();
        } else {
            init();

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strRole = user.get(Session.KEY_ROLE_ID);

            role = Integer.parseInt(strRole);
            setviewPage(vpStudReminder);
            tabStudReminder.setupWithViewPager(vpStudReminder);
        }
    }

    public void init() {
        session = new Session(StudentReminderActivity.this);
        tabStudReminder = findViewById(R.id.tabStudReminder);
        vpStudReminder = findViewById(R.id.vpStudReminder);
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

    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());

        if (role == 1) {
            hwadapter.addFragment(new SetStudReminder(), "Set Student Reminder");
            hwadapter.addFragment(new CurrentStudReminder(), "Current Student Reminder");
            hwadapter.addFragment(new StudReminderByDate(), "Student Reminder By Date");
        } else {
            hwadapter.addFragment(new CurrentStudReminder(), "Current Student Reminder");
            hwadapter.addFragment(new StudReminderByDate(), "Student Reminder By Date");
        }
        viewPager.setAdapter(hwadapter);
    }
}
