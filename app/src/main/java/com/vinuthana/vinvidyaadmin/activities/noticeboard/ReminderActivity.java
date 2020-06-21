package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;

public class ReminderActivity extends AppCompatActivity {
    
    Button btnStaffReminder, btnStudReminder;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reminder");
        /*if (!connection.netInfo(ReminderActivity.this)) {
            connection.buildDialog(ReminderActivity.this).show();
        } else {*/
            init();
            allEvents();
        /*}*/

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
        btnStaffReminder = findViewById(R.id.btnStaffReminder);
        btnStudReminder = findViewById(R.id.btnStudReminder);
    }

    public void allEvents() {
        btnStaffReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReminderActivity.this, StaffReminderActivity.class);
                startActivity(intent);
            }
        });

        btnStudReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReminderActivity.this, StudentReminderActivity.class);
                startActivity(intent);
            }
        });
    }
}
