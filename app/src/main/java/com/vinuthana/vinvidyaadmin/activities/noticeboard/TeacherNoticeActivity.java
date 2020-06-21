package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentTeacherNotice;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentTeacherNoticeStaff;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.SetTeacherNoteFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.TeacherNoteByDate;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.TeacherNoteByDateStaff;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class TeacherNoticeActivity extends AppCompatActivity {
    TabLayout tabTeacherNote;
    ViewPager vpTeacherNote;
    private Session session;
    String strSchoolId, strStaffId, strRole;
    int role;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.teacherNoticeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Teacher Notice");
        if (!connection.netInfo(TeacherNoticeActivity.this)) {
            connection.buildDialog(TeacherNoticeActivity.this).show();
        } else {
            init();
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strRole = user.get(Session.KEY_ROLE_ID);
            role = Integer.parseInt(strRole);
            setviewPage(vpTeacherNote);
            tabTeacherNote.setupWithViewPager(vpTeacherNote);
        }
    }

    private void init() {
        session = new Session(TeacherNoticeActivity.this);
        tabTeacherNote = (TabLayout) findViewById(R.id.tabTeacherNote);
        vpTeacherNote = (ViewPager) findViewById(R.id.vpTeacherNote);
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
            hwadapter.addFragment(new SetTeacherNoteFragment(), "Set Teacher Note");
            hwadapter.addFragment(new CurrentTeacherNotice(), "Current Teacher Notice");
            hwadapter.addFragment(new TeacherNoteByDate(), "Teacher Note By Date");
        } else {
            hwadapter.addFragment(new CurrentTeacherNoticeStaff(), "Current Teacher Notice");
            hwadapter.addFragment(new TeacherNoteByDateStaff(), "Teacher Note By Date");
        }
        viewPager.setAdapter(hwadapter);
    }
}
