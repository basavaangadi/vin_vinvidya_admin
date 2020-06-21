package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentStaffNotice;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.SendStaffNoticeFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.StaffNoticeByDate;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class StaffNoteActivity extends AppCompatActivity {

    ViewPager vpStaffNotice;
    TabLayout tabStaffNote;
    String strSchoolId, strStaffId, strRole;
    int role;
    private Session session;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStaffNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Staff Notice");
        if (!connection.netInfo(StaffNoteActivity.this)) {
            connection.buildDialog(StaffNoteActivity.this).show();
        } else {
            init();

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strRole = user.get(Session.KEY_ROLE_ID);

            role = Integer.parseInt(strRole);
            setviewPage(vpStaffNotice);
            tabStaffNote.setupWithViewPager(vpStaffNotice);
        }

    }

    public void init() {
        session = new Session(StaffNoteActivity.this);
        tabStaffNote = findViewById(R.id.tabStaffNote);
        vpStaffNotice = findViewById(R.id.vpStaffNotice);
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
            hwadapter.addFragment(new SendStaffNoticeFragment(), "Write Staff Notice");
            hwadapter.addFragment(new CurrentStaffNotice(), "Current Staff Note");
            hwadapter.addFragment(new StaffNoticeByDate(), "Staff Note By Date");
        } else {
            hwadapter.addFragment(new CurrentStaffNotice(), "Current Staff Note");
            hwadapter.addFragment(new StaffNoticeByDate(), "Staff Note By Date");
        }
        viewPager.setAdapter(hwadapter);
    }
}
