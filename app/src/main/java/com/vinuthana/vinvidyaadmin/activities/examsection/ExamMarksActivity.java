package com.vinuthana.vinvidyaadmin.activities.examsection;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.ExamMarksByExam;
import com.vinuthana.vinvidyaadmin.fragments.examsectionfragment.SubWiseResultFragment;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamMarksActivity extends AppCompatActivity {

    ViewPager vpExamMark;
    TabLayout tabExamMark;
    private Session session;
    String strSchoolId,strStaffId,strAcademicYearId;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_marks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examMarkToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Exam_Marks));
        if (!connection.netInfo(ExamMarksActivity.this)) {
            connection.buildDialog(ExamMarksActivity.this).show();
        } else {
            session = new Session(ExamMarksActivity.this);
            HashMap<String, String> user = session.getUserDetails();

            ArrayList<String> strUser = new ArrayList<>();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            init();
            setviewPage(vpExamMark);
            tabExamMark.setupWithViewPager(vpExamMark);
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

    private void init() {
        tabExamMark = (TabLayout) findViewById(R.id.tabExamMarks);
        vpExamMark = (ViewPager) findViewById(R.id.vpExamMark);
    }

    public void setviewPage(ViewPager viewPager) {
        VPager adapter = new VPager(getSupportFragmentManager());
        if(strSchoolId.equalsIgnoreCase("2")){

        }
        // adapter.addFragment(new SetExamMarks(), getString(R.string.key_Set_Exam_Marks));
        //adapter.addFragment(new CurrentExamMarks(), getString(R.string.key_Current_Exam_Marks));
        adapter.addFragment(new SubWiseResultFragment(), getString(R.string.key_sub_wise_result));
        adapter.addFragment(new ExamMarksByExam(), getString(R.string.key_Exam_Marks_By_Exam));
        viewPager.setAdapter(adapter);
    }

}
