package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.ClasswiseParentMessageFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentParentNote;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.ParentNoteByDate;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.SchoolwiseParentMessageFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.WriteParentNote;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

public class ParentMessageActivity extends AppCompatActivity {

    TabLayout tabParentMessage;
    ViewPager vpParentMessage;
    CheckConnection connection = new CheckConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Parent Message");
        if (!connection.netInfo(ParentMessageActivity.this)) {
            connection.buildDialog(ParentMessageActivity.this).show();
        } else {
            init();
            setviewPage(vpParentMessage);
            tabParentMessage.setupWithViewPager(vpParentMessage);
        }

    }

    public void init() {
        tabParentMessage = (TabLayout) findViewById(R.id.tabParentMessage);
        vpParentMessage = (ViewPager) findViewById(R.id.vpParentMessage);
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
        hwadapter.addFragment(new ClasswiseParentMessageFragment(), "Classwise Parent Message");
        hwadapter.addFragment(new SchoolwiseParentMessageFragment(), "Schoolwise Parent Message");

        viewPager.setAdapter(hwadapter);
    }

}
