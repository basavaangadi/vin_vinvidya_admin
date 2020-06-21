package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.MySubject;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.ClassWiseSyllabus;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

public class ExamSyllabusActivity extends AppCompatActivity {
    TabLayout tabExamSyllabus;
    ViewPager vpExamSyllabus;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_syllabus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examSyllabusToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Exam_Syllabus));
        if (!connection.netInfo(ExamSyllabusActivity.this)) {
            connection.buildDialog(ExamSyllabusActivity.this).show();
        } else {
            init();
            setviewPage(vpExamSyllabus);
            tabExamSyllabus.setupWithViewPager(vpExamSyllabus);
        }

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

    private void init() {
        tabExamSyllabus = (TabLayout) findViewById(R.id.tabExamSyllabus);
        vpExamSyllabus = (ViewPager) findViewById(R.id.vpExamSyllabus);
    }

    public void setviewPage(ViewPager viewPager) {
        VPager adapter = new VPager(getSupportFragmentManager());
        //adapter.addFragment(new SetExamSyllabus(), getString(R.string.key_Set_Exam_Syllabus));
        adapter.addFragment(new MySubject(), getString(R.string.key_my_subject));
        adapter.addFragment(new ClassWiseSyllabus(), getString(R.string.key_class_wise_syllabus));
        viewPager.setAdapter(adapter);
    }


}
