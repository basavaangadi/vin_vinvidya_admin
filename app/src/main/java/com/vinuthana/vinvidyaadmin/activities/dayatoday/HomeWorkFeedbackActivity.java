package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewCurrentHmWrkFb;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewHmWrkFbByDate;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;


public class HomeWorkFeedbackActivity extends AppCompatActivity {
    TabLayout tabHmWrkFeedback;
    ViewPager vpHmWrkFeedback;
    Toolbar toolbar;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_feedback);
        toolbar = (Toolbar) findViewById(R.id.dayToDaytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Home_Work_Feedback));
        if (!connection.netInfo(HomeWorkFeedbackActivity.this)) {
            connection.buildDialog(HomeWorkFeedbackActivity.this).show();
        } else {
            init();
            setviewPage(vpHmWrkFeedback);
            tabHmWrkFeedback.setupWithViewPager(vpHmWrkFeedback);
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

        tabHmWrkFeedback = (TabLayout) findViewById(R.id.tabHmWrkFeedback);
        vpHmWrkFeedback = (ViewPager) findViewById(R.id.vpHmWrkFeedback);
    }

    public void setviewPage(ViewPager viewPager) {
        VPager hwadapter = new VPager(getSupportFragmentManager());
        hwadapter.addFragment(new ViewCurrentHmWrkFb(), getString(R.string.key_View_Current_Home_Work_Feedback));
        hwadapter.addFragment(new ViewHmWrkFbByDate(), getString(R.string.key_View_Home_Work_Feedback_By_Date));
        viewPager.setAdapter(hwadapter);
    }
}
