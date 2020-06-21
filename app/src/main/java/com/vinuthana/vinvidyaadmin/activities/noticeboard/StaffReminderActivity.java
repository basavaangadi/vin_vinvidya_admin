package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentStaffReminder;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentStaffReminderStaff;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.SetStaffReminder;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.StaffReminderByDate;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.StaffReminderByDateStaff;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class StaffReminderActivity extends AppCompatActivity {
    TabLayout tabStaffReminder;
    ViewPager vpStaffReminder;
    private Session session;
    String strSchoolId, strStaffId, strRole;
    int role;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.staffRmdToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Staff Reminder");
        if (!connection.netInfo(StaffReminderActivity.this)) {
            connection.buildDialog(StaffReminderActivity.this).show();
        } else {
            init();
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strRole = user.get(Session.KEY_ROLE_ID);
            role = Integer.parseInt(strRole);
            setviewPage(vpStaffReminder);
            tabStaffReminder.setupWithViewPager(vpStaffReminder);
        }

    }

    public void init() {
        session = new Session(StaffReminderActivity.this);
        tabStaffReminder = (TabLayout) findViewById(R.id.tabStaffReminder);
        vpStaffReminder = (ViewPager) findViewById(R.id.vpStaffReminder);
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
            hwadapter.addFragment(new SetStaffReminder(), "Set Staff Reminder");
            hwadapter.addFragment(new CurrentStaffReminder(), "Current Staff Reminder");
            hwadapter.addFragment(new StaffReminderByDate(), "Staff Reminder By Date");
        } else {
            hwadapter.addFragment(new CurrentStaffReminderStaff(), "Current Staff Reminder");
            hwadapter.addFragment(new StaffReminderByDateStaff(), "Staff Reminder By Date");
        }
        viewPager.setAdapter(hwadapter);
    }
}
