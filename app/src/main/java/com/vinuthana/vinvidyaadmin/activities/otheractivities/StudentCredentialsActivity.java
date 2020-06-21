package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.noticeboard.EditParentsNoteActivity;
import com.vinuthana.vinvidyaadmin.adapters.ClassSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.PrntNtRecyclerViewAdapter;
import com.vinuthana.vinvidyaadmin.adapters.SectionSpinnerDataAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentCredentialsAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StudentListSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.AddPeriodWiseAttendanceFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.PutAttendanceFragment;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewAttendanceByDate;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewCurrentAttendance;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.ViewLeaveRequestFragment;
import com.vinuthana.vinvidyaadmin.fragments.otherfragments.GetPasswordFromNumberFragment;
import com.vinuthana.vinvidyaadmin.fragments.otherfragments.GetPasswordFromStudentIdFragment;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.viewPagerAdapter.VPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class StudentCredentialsActivity extends AppCompatActivity {
    CheckConnection connection = new CheckConnection();


    TabLayout tblytStudntCrdntls;
    ViewPager vpStudntCrdntls;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_credentials);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStudntCrdntls);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Credentials");
        if (!connection.netInfo(StudentCredentialsActivity.this)) {
            connection.buildDialog(StudentCredentialsActivity.this).show();
        } else {

            init();
            setviewPage(vpStudntCrdntls);
            tblytStudntCrdntls.setupWithViewPager(vpStudntCrdntls);

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
    public void setviewPage(ViewPager viewPager) {
        VPager adapter = new VPager(getSupportFragmentManager());

            adapter.addFragment(new GetPasswordFromNumberFragment(), "Get phone number password");
            adapter.addFragment(new GetPasswordFromStudentIdFragment(), "Get student password");
          viewPager.setAdapter(adapter);
    }
    public void init() {
        tblytStudntCrdntls = (TabLayout) findViewById(R.id.tblytStudntCrdntls);
        vpStudntCrdntls = (ViewPager) findViewById(R.id.vpStudntCrdntls);
    }



    
}
