package com.vinuthana.vinvidyaadmin.activities.noticeboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vinuthana.vinvidyaadmin.R;

public class NoteActivity extends AppCompatActivity {

    Button btnStaffNotice, btnStudNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");
        init();
        allEvents();
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
        btnStaffNotice = findViewById(R.id.btnStaffNotice);
        btnStudNotice = findViewById(R.id.btnStudNotice);
    }

    public void allEvents() {
        btnStaffNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, StaffNoteActivity.class);
                startActivity(intent);
            }
        });

        btnStudNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, StudentNoteActivity.class);
                startActivity(intent);
            }
        });
    }
}
