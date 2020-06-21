package com.vinuthana.vinvidyaadmin.activities.extraactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vinuthana.vinvidyaadmin.R;

public class InactiveMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inactive_message);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
