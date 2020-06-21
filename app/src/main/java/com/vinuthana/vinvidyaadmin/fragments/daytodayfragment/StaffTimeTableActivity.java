package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.StaffNoticeByDate;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class StaffTimeTableActivity extends AppCompatActivity {

    ViewPager vpStaffTimeTable;
    TabLayout tabStafTimTbl;
    String strSchoolId, strClassId, strClass, strStfId, strStaffId, strNoteTitle, strStatus, strNoticeId, strDate;
    private Session session;
    TabLayout tabClassWiseTimeTable;
    ViewPager vpClassWiseTimeTable;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.key_Time_Table);

        if (!connection.netInfo(StaffTimeTableActivity.this)) {
            connection.buildDialog(StaffTimeTableActivity.this).show();
        } else {
            init();
            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);

            setviewPage(vpStaffTimeTable);
            tabStafTimTbl.setupWithViewPager(vpStaffTimeTable);
            //String strStfId = getIntent().getExtras().getString("StaffId");


            /*strClass = bundle.getString("Class");
            strClassId = bundle.getString("ClassId");*/
                //Toast.makeText(TimeTblActivity.this,strClass + " " + strClassId,Toast.LENGTH_SHORT).show();

            /*bundle.putString("Class",strClass);
            bundle.putString("ClassId",strClassId);*/


            Fragment fargment = new MondayFragment();
            Bundle bundle = new Bundle();
            bundle.putString("StaffId", strStaffId);
            fargment.setArguments(bundle);
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
        session = new Session(StaffTimeTableActivity.this);
        vpStaffTimeTable = findViewById(R.id.vpStaffTimeTable);
        tabStafTimTbl = findViewById(R.id.tabStafTimTbl);
    }

    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());
        hwadapter.addFragment(new StafMondayFragment(), "Mon");
        hwadapter.addFragment(new StafTuesdayFragment(), "Tue");
        hwadapter.addFragment(new StafWednesdayFragment(), "Wed");
        hwadapter.addFragment(new StafThursdayFragment(), "Thu");
        hwadapter.addFragment(new StafFridayFragment(), "Fri");
        hwadapter.addFragment(new StafSaturdayFragment(), "Sat");
       if(strSchoolId.equalsIgnoreCase("6")){
           hwadapter.addFragment(new StafSundayFragment(), "Sun");
       }
        viewPager.setAdapter(hwadapter);
    }
}
