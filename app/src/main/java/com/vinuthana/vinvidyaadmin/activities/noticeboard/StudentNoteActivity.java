package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentStudNoteFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.SendStudNoticeFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.StudNoteByDateFragment;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class StudentNoteActivity extends AppCompatActivity {

    ViewPager vpStudNotice;
    TabLayout tabStudNote;
    private Session session;
    String strSchoolId, strStaffId, strRole;
    int role;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStudNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Notice");
        if (!connection.netInfo(StudentNoteActivity.this)) {
            connection.buildDialog(StudentNoteActivity.this).show();
        } else {
            init();

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strRole = user.get(Session.KEY_ROLE_ID);

            role = Integer.parseInt(strRole);
            setviewPage(vpStudNotice);
            tabStudNote.setupWithViewPager(vpStudNotice);
            //Toast.makeText(StudentNoteActivity.this, String.valueOf(role), Toast.LENGTH_SHORT).show();
        }

    }

    public void init() {
        session = new Session(StudentNoteActivity.this);
        tabStudNote = findViewById(R.id.tabStudNote);
        vpStudNotice = findViewById(R.id.vpStudNotice);
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
            hwadapter.addFragment(new SendStudNoticeFragment(), "Set Student Notice");
            hwadapter.addFragment(new CurrentStudNoteFragment(), "Current Student Note");
            hwadapter.addFragment(new StudNoteByDateFragment(), "Student Note By Date");
        } else {
            hwadapter.addFragment(new CurrentStudNoteFragment(), "Current Student Note");
            hwadapter.addFragment(new StudNoteByDateFragment(), "Student Note By Date");
        }
        viewPager.setAdapter(hwadapter);
    }
}
