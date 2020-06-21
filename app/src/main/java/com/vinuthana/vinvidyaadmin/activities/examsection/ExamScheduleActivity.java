package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.MyExamSchedule;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.ClassWiseExamSchedule;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.SetExamSchedule;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

public class ExamScheduleActivity extends AppCompatActivity {
    TabLayout tabExmSchdl;
    ViewPager vpExmSchdl;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.exmSchdlToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Exam_Schedule));
        if (!connection.netInfo(ExamScheduleActivity.this)) {
            connection.buildDialog(ExamScheduleActivity.this).show();
        } else {
            init();
            setviewPage(vpExmSchdl);
            tabExmSchdl.setupWithViewPager(vpExmSchdl);
        }

    }

    public void init() {
        vpExmSchdl = (ViewPager) findViewById(R.id.vpExmSchdl);
        tabExmSchdl = (TabLayout) findViewById(R.id.tabExmSchdl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setviewPage(ViewPager viewPager) {
        VPager adapter = new VPager(getSupportFragmentManager());
        adapter.addFragment(new SetExamSchedule(), getString(R.string.key_Set_Exam_Schedule));
        adapter.addFragment(new MyExamSchedule(), getString(R.string.key_my_Exam_Schedule));
        adapter.addFragment(new ClassWiseExamSchedule(), getString(R.string.key_class_wise_Schedule));
        viewPager.setAdapter(adapter);
    }
}
