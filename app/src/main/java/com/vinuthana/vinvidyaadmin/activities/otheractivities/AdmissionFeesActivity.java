package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.dayatoday.AttendanceActivity;
import com.vinuthana.vinvidyaadmin.fragments.otherfragments.ClassWiseFeesFragment;
import com.vinuthana.vinvidyaadmin.fragments.otherfragments.StudentWiseFeesFragment;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

public class AdmissionFeesActivity extends AppCompatActivity {
    TabLayout tblyt;
    ViewPager vpgr;
    CheckConnection connection = new CheckConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_fees);
        Toolbar toolbar = findViewById(R.id.toolbar);
        tblyt = findViewById(R.id.tblyt);
        vpgr = findViewById(R.id.vpgr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Fees Collected");

        if (!connection.netInfo(AdmissionFeesActivity.this)) {
            connection.buildDialog(AdmissionFeesActivity.this).show();
        } else {
            init();
            setviewPager(vpgr);
            tblyt.setupWithViewPager(vpgr);
        }

        setviewPager(vpgr);
        tblyt.setupWithViewPager(vpgr);
    }
    public void init() {
        tblyt = (TabLayout) findViewById(R.id.tblyt);
        vpgr = (ViewPager) findViewById(R.id.vpgr);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setviewPager(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());

        hwadapter.addFragment(new ClassWiseFeesFragment(), "Class wise");
        hwadapter.addFragment(new StudentWiseFeesFragment(),"Student Wise");

        viewPager.setAdapter(hwadapter);
    }

}
