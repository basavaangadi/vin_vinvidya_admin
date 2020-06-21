package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.AddPeriodWiseAttendanceFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.PutAttendanceFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewAttendanceByDate;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewCurrentAttendance;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewLeaveRequestFragment;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class AttendanceActivity extends AppCompatActivity {
    TabLayout tabAttendance;
    ViewPager vpAttendance;
    CheckConnection connection = new CheckConnection();
    private Session session;
    String strSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.atndToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Attendance));
        session = new Session(AttendanceActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        if (!connection.netInfo(AttendanceActivity.this)) {
            connection.buildDialog(AttendanceActivity.this).show();
        } else {
            init();
            setviewPage(vpAttendance);
            tabAttendance.setupWithViewPager(vpAttendance);
        }

    }

    public void init() {
        tabAttendance = (TabLayout) findViewById(R.id.tabAttendance);
        vpAttendance = (ViewPager) findViewById(R.id.vpAttendance);
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
        VPager adapter = new VPager(getSupportFragmentManager());
        //TODO school id = 4
        if (strSchoolId.equalsIgnoreCase("6")) {
            adapter.addFragment(new AddPeriodWiseAttendanceFragment(), getString(R.string.key_Put_Attendance));
            adapter.addFragment(new ViewCurrentAttendance(), getString(R.string.key_View_Current_Attendance));
            adapter.addFragment(new ViewAttendanceByDate(), getString(R.string.key_View_Attendance_By_Date));
            adapter.addFragment(new ViewLeaveRequestFragment(), getString(R.string.key_Student_Leave_List));

        } else {
            adapter.addFragment(new PutAttendanceFragment(), getString(R.string.key_Put_Attendance));
            adapter.addFragment(new ViewCurrentAttendance(), getString(R.string.key_View_Current_Attendance));
            adapter.addFragment(new ViewAttendanceByDate(), getString(R.string.key_View_Attendance_By_Date));
            adapter.addFragment(new ViewLeaveRequestFragment(), getString(R.string.key_Student_Leave_List));
        }

        viewPager.setAdapter(adapter);
    }
}
