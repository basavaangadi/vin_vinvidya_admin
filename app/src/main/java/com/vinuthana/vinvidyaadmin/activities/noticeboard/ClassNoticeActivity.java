package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;

import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.ClassNoticeByDateFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.CurrentClassNoticeFragment;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.ParentNoteByDate;
import com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment.WriteClassNoticeFragment;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

public class ClassNoticeActivity extends AppCompatActivity {

    TabLayout tabClassNotice;
    ViewPager vpClassNote;
    CheckConnection connection = new CheckConnection();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.classNoticetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Class Notice");
        if (!connection.netInfo(ClassNoticeActivity.this)) {
            connection.buildDialog(ClassNoticeActivity.this).show();
        } else {
            init();
            setviewPage(vpClassNote);
            tabClassNotice.setupWithViewPager(vpClassNote);
        }

       
    }
    public void init(){
        tabClassNotice=findViewById(R.id.tabClassNotice);
        vpClassNote=findViewById(R.id.vpClassNote);
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
        hwadapter.addFragment(new WriteClassNoticeFragment(), "Write Class Notice");
        hwadapter.addFragment(new CurrentClassNoticeFragment(), "Current Class Notice");
        hwadapter.addFragment(new ClassNoticeByDateFragment(), "Class Notice By Date");
        viewPager.setAdapter(hwadapter);
    }

}
