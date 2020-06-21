package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentParentNote;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.ParentNoteByDate;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.WriteParentNote;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

public class ParentNoteActivity extends AppCompatActivity {
    TabLayout tabParentNote;
    ViewPager vpParentNote;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.parentNoteToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Parent Note");
        if (!connection.netInfo(ParentNoteActivity.this)) {
            connection.buildDialog(ParentNoteActivity.this).show();
        } else {
            init();
            setviewPage(vpParentNote);
            tabParentNote.setupWithViewPager(vpParentNote);
        }

    }

    public void init() {
        tabParentNote = (TabLayout) findViewById(R.id.tabParentNote);
        vpParentNote = (ViewPager) findViewById(R.id.vpParentNote);
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
        hwadapter.addFragment(new WriteParentNote(), "Write Parent Note");
        hwadapter.addFragment(new CurrentParentNote(), "Current Parent Note");
        hwadapter.addFragment(new ParentNoteByDate(), "Parent Note By Date");
        viewPager.setAdapter(hwadapter);
    }
}
