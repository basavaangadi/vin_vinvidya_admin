package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentStaffNotice;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.StaffNoticeByDate;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.SendStaffNoticeFragment;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class NoticeActivity extends AppCompatActivity {

    TabLayout tabNotice;
    ViewPager vpNotice;
    private Session session;
    String strSchoolId, strStaffId, strRole;
    int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.noticeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");
        init();
        setviewPage(vpNotice);
        tabNotice.setupWithViewPager(vpNotice);
        HashMap<String, String> user = session.getUserDetails();

        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
        strRole = user.get(Session.KEY_ROLE_ID);

        role = Integer.parseInt(strRole);
    }

    public void init() {
        session = new Session(NoticeActivity.this);
        vpNotice = (ViewPager) findViewById(R.id.vpNotice);
        tabNotice = (TabLayout) findViewById(R.id.tabNotice);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setviewPage(ViewPager viewPager) {
        VPager adapter = new VPager(getSupportFragmentManager());

        if (role == 1) {
            adapter.addFragment(new SendStaffNoticeFragment(), "Send Notice");
            adapter.addFragment(new CurrentStaffNotice(), "Current Notice");
            adapter.addFragment(new StaffNoticeByDate(), "Notice By Date");
        } else {
            adapter.addFragment(new CurrentStaffNotice(), "Current Notice");
            adapter.addFragment(new StaffNoticeByDate(), "Notice By Date");
        }
        viewPager.setAdapter(adapter);
    }
}