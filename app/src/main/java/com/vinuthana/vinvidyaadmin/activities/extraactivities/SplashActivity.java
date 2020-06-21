package com.vinuthana.vinvidyaadmin.activities.extraactivities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
//import android.test.mock.MockPackageManager;

import com.vinuthana.vinvidyaadmin.activities.useractivities.LoginActivity;
import com.vinuthana.vinvidyaadmin.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    String[] mPermission = {"android.permission.ACCESS_NETWORK_STATE" ,
            "android.permission.INTERNET","android.permission.WRITE_EXTERNAL_STORAGE" ,
            "android.permission.READ_PHONE_STATE","android.permission.READ_EXTERNAL_STORAGE"};

    private static final int REQUEST_CODE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try{
            if(ActivityCompat.checkSelfPermission(this, mPermission[0]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1]) != PackageManager.PERMISSION_GRANTED ||

                    ActivityCompat.checkSelfPermission(this, mPermission[2]) != PackageManager.PERMISSION_GRANTED ||

                    ActivityCompat.checkSelfPermission(this, mPermission[3]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSION);
            }else {
                proceedNext();
        }
        }catch(Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //finish();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults.length == mPermission.length){
                proceedNext();
            }
        }
    }

    public  void proceedNext(){
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }
}