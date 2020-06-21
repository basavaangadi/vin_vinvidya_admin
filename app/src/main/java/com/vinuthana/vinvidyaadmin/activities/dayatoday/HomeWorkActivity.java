package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.GiveHomeWork;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewCurrentHomeWork;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewHomeWorkByDate;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

public class HomeWorkActivity extends AppCompatActivity {

    TabLayout tabHomeWork;
    ViewPager vpHomeWork;
    Toolbar toolbar;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);
        toolbar = (Toolbar) findViewById(R.id.toolbarHomeWork);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Home_Work));
        if (!connection.netInfo(HomeWorkActivity.this)) {
            connection.buildDialog(HomeWorkActivity.this).show();
        } else {
            init();
            setviewPage(vpHomeWork);
            tabHomeWork.setupWithViewPager(vpHomeWork);
        }

    }

    public void init() {

        vpHomeWork = (ViewPager) findViewById(R.id.vpHomeWork);
        tabHomeWork = (TabLayout) findViewById(R.id.tabHomeWork);
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
        hwadapter.addFragment(new GiveHomeWork(), getString(R.string.key_Give_Home_Work));
        hwadapter.addFragment(new ViewCurrentHomeWork(), getString(R.string.key_View_Home_Work));
        hwadapter.addFragment(new ViewHomeWorkByDate(), getString(R.string.key_View_Home_Work_By_Date));
        viewPager.setAdapter(hwadapter);
    }
}
