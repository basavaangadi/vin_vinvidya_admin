package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ClassWiseTimeTable;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.MyTimeTableFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.StaffWiseTimeTable;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;


public class TimeTableActivity extends AppCompatActivity {

    TabLayout tabTimeTable;
    ViewPager vpTimeTable;
    Toolbar toolbar;
    private Session session;
    String strSchoolId, strStaffId, strRole;
    int role;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        toolbar = (Toolbar) findViewById(R.id.dayToDaytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Time_Table));
        if (!connection.netInfo(TimeTableActivity.this)) {
            connection.buildDialog(TimeTableActivity.this).show();
        } else {
            init();

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strRole = user.get(Session.KEY_ROLE_ID);
            role = Integer.parseInt(strRole);
            setviewPage(vpTimeTable);
            tabTimeTable.setupWithViewPager(vpTimeTable);
        }

    }

    public void init() {
        session = new Session(TimeTableActivity.this);
        tabTimeTable = (TabLayout) findViewById(R.id.tabTimeTable);
        vpTimeTable = (ViewPager) findViewById(R.id.vpTimeTable);
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
            hwadapter.addFragment(new ClassWiseTimeTable(), getString(R.string.key_Class_Wise_Time_Table));
            hwadapter.addFragment(new StaffWiseTimeTable(), getString(R.string.key_Staff_Wise_Time_Table));
        } else {
            hwadapter.addFragment(new ClassWiseTimeTable(), getString(R.string.key_Class_Wise_Time_Table));
            hwadapter.addFragment(new MyTimeTableFragment(), getString(R.string.key_my_Time_Table));
        }

        viewPager.setAdapter(hwadapter);
    }
}
