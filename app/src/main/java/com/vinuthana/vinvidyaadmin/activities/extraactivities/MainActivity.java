package com.vinuthana.vinvidyaadmin.activities.extraactivities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.activities.useractivities.ChangePasswordActivity;
import com.vinuthana.vinvidyaadmin.activities.useractivities.ProfileActivity;
import com.vinuthana.vinvidyaadmin.adapters.RecyclerAdapterDayToDay;
import com.vinuthana.vinvidyaadmin.adapters.RviewAdapterExamSection;
import com.vinuthana.vinvidyaadmin.adapters.RviewAdapterNoticeBoard;
import com.vinuthana.vinvidyaadmin.adapters.RviewAdapterOthers;
import com.vinuthana.vinvidyaadmin.fcm.FCMRegistrationIntentService;
import com.vinuthana.vinvidyaadmin.fcm.FCMUtils;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> titleDayToDay;
    ArrayList<Integer> imagesDayToDay = new ArrayList<>(Arrays.asList(R.drawable.day_to_day, R.drawable.notice, R.drawable.assignment_black, R.drawable.progress_report,R.drawable.ic_apps_black_48dp));

    ArrayList<String>  titleNoticeBoard;
    ArrayList<Integer> imagesNoticeBoard;
    ArrayList<String>  titleExamSection;
    ArrayList<Integer> imagesExamSection;
    ArrayList<String>  titleOthers;
    ArrayList<Integer> imagesOthers;
    ListView activityMenuListView;
    String strUsername, strEmail,token,strRoleId,strStaffId,strSchoolId;
     int resultId=1, serverVersionCode =1;
    TextView tvStatus, tvEmail;
    private Session session;
    RecyclerView rViewDayTODay, rViewNoticeBoard, rViewExamSection, rViewOthers;
    CheckConnection connection = new CheckConnection();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        session = new Session(this);
        //Toast.makeText(ListActivity_new.this, "User Login Status: " + session.loggedin(), Toast.LENGTH_SHORT).show();
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        ArrayList<String> strUser = new ArrayList<>();
        //int size =

        strUsername = user.get(Session.KEY_NAME);
        strRoleId = user.get(Session.KEY_ROLE_ID);
        strStaffId=user.get(Session.KEY_STAFFDETAILS_ID);
        strSchoolId=user.get(Session.KEY_SCHOOL_ID);

            new CheckIsStaffActive().execute();
        if (!connection.netInfo(MainActivity.this)) {
            connection.buildDialog(MainActivity.this).show();
        }/*else if(resultId==0){
            Toast.makeText(MainActivity.this, "Your Account is inactive", Toast.LENGTH_SHORT).show();
            Intent inactiv= new Intent(MainActivity.this,InactiveMessageActivity.class);
            startActivity(inactiv);
           finish();

        }*/
        else {

            tvStatus = (TextView) findViewById(R.id.tvStatus);
            for (int i = 0; i < strUser.size(); i++) {
                //strUser.add(session.getUserDetails())
            }
            //tvEmail = (TextView) findViewById(R.id.tvEmail);

            tvStatus.setText(Html.fromHtml("Welcome <b> " + strUsername + "<b>"));

            SharedPreferences preferences = getSharedPreferences("myapp", MODE_PRIVATE);
            strUsername = preferences.getString("UserName", "UNKNOWN");
            initialisation();
            recycler();
        }


    }

    public void recycler() {
        /*Day To Day Start*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewDayTODay.setLayoutManager(linearLayoutManager);
        RecyclerAdapterDayToDay rcAdapter = new RecyclerAdapterDayToDay(MainActivity.this, titleDayToDay, imagesDayToDay);
        rViewDayTODay.setAdapter(rcAdapter);
        /*Day To Day End*/

        /*Notice Board Start*/
        LinearLayoutManager managerNoticeBoard = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewNoticeBoard.setLayoutManager(managerNoticeBoard);
        RviewAdapterNoticeBoard rcAdapterNoticeBoard = new RviewAdapterNoticeBoard(MainActivity.this, titleNoticeBoard, imagesNoticeBoard);
        rViewNoticeBoard.setAdapter(rcAdapterNoticeBoard);
        /*Notice Board End*/

        /*Exam Section Start*/
        LinearLayoutManager managerExamSection = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewExamSection.setLayoutManager(managerExamSection);
        RviewAdapterExamSection rcAdapterExamSection = new RviewAdapterExamSection(MainActivity.this, titleExamSection, imagesExamSection);
        rViewExamSection.setAdapter(rcAdapterExamSection);
        /*Exam Section End*/

        /*Others Start*/
        LinearLayoutManager managerOthers = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rViewOthers.setLayoutManager(managerOthers);
        RviewAdapterOthers rcAdapterOthers = new RviewAdapterOthers(MainActivity.this, titleOthers, imagesOthers);
        rViewOthers.setAdapter(rcAdapterOthers);
        /*Others End*/
    }

    private void initialisation() {
        rViewDayTODay = (RecyclerView) findViewById(R.id.recyclerViewDayToDay);
        rViewNoticeBoard = (RecyclerView) findViewById(R.id.recyclerviewNoticeBoard);

           titleExamSection = new ArrayList<>(Arrays.asList("Exam Schedule", "Exam Syllabus", "Exam Marks","Exam Status"));
           imagesExamSection = new ArrayList<>(Arrays.asList(R.drawable.ic_calendar_multiple_check_black_36dp, R.drawable.ic_book_open_variant_black_36dp, R.drawable.ic_format_list_numbers_black_36dp,R.drawable.exam_staus_24dp));

        rViewExamSection = (RecyclerView) findViewById(R.id.recyclerviewExamSection);
        rViewOthers = (RecyclerView) findViewById(R.id.recyclerviewOthers);
        titleDayToDay = new ArrayList<>(Arrays.asList("Attendance", "Home Work", "FeedBack HW", "Day Report","Time Table"));
        imagesDayToDay = new ArrayList<>(Arrays.asList(R.drawable.day_to_day, R.drawable.notice, R.drawable.assignment_black, R.drawable.progress_report,R.drawable.ic_apps_black_48dp));
     //   if(strSchoolId.equalsIgnoreCase("6")){
            titleNoticeBoard = new ArrayList<>(Arrays.asList("Notice", "Parent note","Class Notice" ,"Teacher Notice", "Reminder"));
            imagesNoticeBoard = new ArrayList<>(Arrays.asList(R.drawable.ic_clipboard_outline_black_48dp, R.drawable.people_connect_icon,R.drawable.presentation ,R.drawable.ic_clipboard_outline_black_48dp, R.drawable.ic_bell_ring_black_36dp));
        /*}else {
            titleNoticeBoard = new ArrayList<>(Arrays.asList("Notice", "Parent note", "Class Notice", "Teacher Notice", "Reminder"));
            imagesNoticeBoard = new ArrayList<>(Arrays.asList(R.drawable.ic_clipboard_outline_black_48dp, R.drawable.people_connect_icon, R.drawable.presentation, R.drawable.ic_clipboard_outline_black_48dp, R.drawable.ic_bell_ring_black_36dp));
        }*/if(!strRoleId.equalsIgnoreCase("1")){
            titleOthers = new ArrayList<>(Arrays.asList("Events", "Gallery", "Syllabus","Assignment"));
            imagesOthers = new ArrayList<>(Arrays.asList(R.drawable.ic_calendar_check_black_48dp, R.drawable.ic_folder_multiple_image_black_48dp, R.drawable.ic_book_open_variant_black_48dp,R.drawable.ic_assignment_24dp));
        }else{
            titleOthers = new ArrayList<>(Arrays.asList("Events", "Gallery", "Syllabus","Assignment","Student Credentials","Fees Section"));
            imagesOthers = new ArrayList<>(Arrays.asList(R.drawable.ic_calendar_check_black_48dp, R.drawable.ic_folder_multiple_image_black_48dp, R.drawable.ic_book_open_variant_black_48dp,R.drawable.ic_assignment_24dp,R.drawable.ic_account_circle_black_24dp,R.drawable.ic_attach_money_black_24dp));
        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(FCMUtils.messages.REG_SUCCESS)) {
                      //  token=intent.getExtras().getString()
                    token = intent.getStringExtra("token");
                    if (token != null) {
                        Log.e("TAG", token+"Length = "+token.length());
                    }
                } else if (intent.getAction().equals(FCMUtils.messages.REG_ERROR)) {
                    Log.e("TAG", "Error");
                }
            }
        };
        Intent intent = new Intent(this, FCMRegistrationIntentService.class);
        startService(intent);

    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.e("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FCMUtils.messages.REG_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FCMUtils.messages.REG_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_profile:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
           /* case R.id.action_aboutUs:
                intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;*/
            case R.id.action_changePassword:
                intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                /*FCMRegistrationIntentService fcmRegistrationIntentService= new FCMRegistrationIntentService();
                fcmRegistrationIntentService.unsubsribe();*/
                FirebaseMessaging.getInstance().unsubscribeFromTopic(strSchoolId);
                session.logOut();
                finish();
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    class  CheckIsStaffActive extends AsyncTask<String, JSONArray, Void>{

        String url = AD.url.base_url + "userOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait Checking user...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_checkIsStaffActive));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_StaffId), strStaffId);
                userData.put(getString(R.string.key_App), "2");
                outObject.put(getString(R.string.key_userData), userData);

                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

                int appVersionCode = BuildConfig.VERSION_CODE;
                String versionName = BuildConfig.VERSION_NAME;
                Log.e("Tag", "responseText is =" + responseText);

                resultId = inObject.getInt(getString(R.string.key_ResultId));
                serverVersionCode =inObject.getInt(getString(R.string.key_VersionCode));
                progressDialog.dismiss();
                if(resultId==0){
                  //  Toast.makeText(MainActivity.this, "Your Account is inactive", Toast.LENGTH_SHORT).show();
                    Intent inactiv= new Intent(MainActivity.this,InactiveMessageActivity.class);
                    startActivity(inactiv);
                    finish();
                }else if(serverVersionCode >appVersionCode){
                    Intent update= new Intent(MainActivity.this,UpdateActivity.class);
                    startActivity(update);
                    finish();
                }

            } catch (Exception ex) {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        Intent update= new Intent(MainActivity.this,UserErrorActivity.class);
                        startActivity(update);
                        finish();
                        Toast.makeText(MainActivity.this, "check internet if internet strength is good check then also problem exist contact service provider", Toast.LENGTH_SHORT).show();
                        Log.e("User Verification error",ex.getMessage());
                    }
                });
               // Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }

            return null;
        }


    }


    class  CheckRecentVersion extends AsyncTask<String, JSONArray, Void>{

        String url = AD.url.base_url + "userOperations.jsp";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait Checking user...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            GetResponse response = new GetResponse();
            JSONObject outObject = new JSONObject();

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_checkRecentAppVersion));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_App), "2");
                outObject.put(getString(R.string.key_userData), userData);

                Log.e("Tag", "outObject =" + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);


                Log.e("Tag", "responseText is =" + responseText);

                resultId = inObject.getInt(getString(R.string.key_ResultId));
                progressDialog.dismiss();
                if(resultId<33){
                    //  Toast.makeText(MainActivity.this, "Your Account is inactive", Toast.LENGTH_SHORT).show();
                    Intent inactiv= new Intent(MainActivity.this,UpdateActivity.class);
                    startActivity(inactiv);
                    finish();
                }

            } catch (Exception ex) {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(MainActivity.this, "check internet if internet strength is good check then also problem exist contact service provider", Toast.LENGTH_SHORT).show();
                        //update ui here
                        // display toast here
                    }
                });
                // Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }

            return null;
        }


    }



}
