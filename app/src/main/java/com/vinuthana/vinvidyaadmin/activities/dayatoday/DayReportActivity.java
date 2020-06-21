package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ClasswiseDayReportFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.MyDayReportFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.StaffwiseDayReportFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.WriteDayReportFragment;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class DayReportActivity extends AppCompatActivity {
    CheckConnection connection = new CheckConnection();
    TabLayout tabDayReport;
    ViewPager vpDayReportback;
    Toolbar toolbar;
    int roleId;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_report);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Day_Report));
        if (!connection.netInfo(DayReportActivity.this)) {
            connection.buildDialog(DayReportActivity.this).show();
        } else {
            session = new Session(DayReportActivity.this);
            HashMap<String, String> user = session.getUserDetails();
            roleId=Integer.parseInt(user.get(Session.KEY_ROLE_ID));
            init();
            setviewPage(vpDayReportback);
            tabDayReport.setupWithViewPager(vpDayReportback);
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
    public void init() {

        tabDayReport = (TabLayout) findViewById(R.id.tabDayReport);
        vpDayReportback = (ViewPager) findViewById(R.id.vpDayReportback);
    }

    public void setviewPage(ViewPager viewPager) {
        VPager dayReportAdapter = new VPager(getSupportFragmentManager());
        if(roleId==1){
            dayReportAdapter.addFragment(new WriteDayReportFragment(), getString(R.string.key_Write_Report));
            dayReportAdapter.addFragment(new ClasswiseDayReportFragment(), getString(R.string.key_Classwise_report));
            dayReportAdapter.addFragment(new StaffwiseDayReportFragment(), getString(R.string.key_Staffwise_Report));
        }else{
            dayReportAdapter.addFragment(new WriteDayReportFragment(), getString(R.string.key_Write_Report));
            dayReportAdapter.addFragment(new ClasswiseDayReportFragment(), getString(R.string.key_Classwise_report));
            dayReportAdapter.addFragment(new MyDayReportFragment(), getString(R.string.key_My_Report));
        }

        viewPager.setAdapter(dayReportAdapter);
    }

}
