package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.HashMap;

public class TimeTblActivity extends AppCompatActivity {

    String strSchoolId, strClassId, strClass, strStaffId, strNoteTitle, strStatus, strNoticeId, strDate;
    private Session session;
    TabLayout tabClassWiseTimeTable;
    ViewPager vpClassWiseTimeTable;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tbl);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Time Table");

        if (!connection.netInfo(TimeTblActivity.this)) {
            connection.buildDialog(TimeTblActivity.this).show();
        } else {
            init();


            tabClassWiseTimeTable.setupWithViewPager(vpClassWiseTimeTable);

            HashMap<String, String> user = session.getUserDetails();
            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);

            String strClassId = getIntent().getExtras().getString("ClassId");


        /*strClass = bundle.getString("Class");
        strClassId = bundle.getString("ClassId");*/
            //Toast.makeText(TimeTblActivity.this,strClass + " " + strClassId,Toast.LENGTH_SHORT).show();

        /*bundle.putString("Class",strClass);
        bundle.putString("ClassId",strClassId);*/

            setviewPage(vpClassWiseTimeTable);
            Fragment fargment = new MondayFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ClassId", strClassId);
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
        session = new Session(TimeTblActivity.this);
        vpClassWiseTimeTable = findViewById(R.id.vpClassWiseTimeTable);
        tabClassWiseTimeTable = findViewById(R.id.tabClassWiseTimeTable);
    }

    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());
        hwadapter.addFragment(new MondayFragment(), "Mon");
        hwadapter.addFragment(new TuesdayFragment(), "Tue");
        hwadapter.addFragment(new WednesdayFragment(), "Wed");
        hwadapter.addFragment(new ThursdayFragment(), "Thu");
        hwadapter.addFragment(new FridayFragment(), "Fri");
        hwadapter.addFragment(new SaturdayFragment(), "Sat");
        if(strSchoolId.equalsIgnoreCase("6")){
            hwadapter.addFragment(new SundayFragment(), "Sun");
        }
        viewPager.setAdapter(hwadapter);
    }
}
