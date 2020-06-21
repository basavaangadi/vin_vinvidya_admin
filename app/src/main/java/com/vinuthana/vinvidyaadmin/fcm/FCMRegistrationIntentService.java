package com.vinuthana.vinvidyaadmin.fcm;

import android.Manifest;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Anirudh on 10/07/16. IntentService
 */
public class FCMRegistrationIntentService extends IntentService {
    Session session;
    String strStaffId,token,strImei,strSchoolId;
    CheckConnection connection = new CheckConnection();
    public static final String TAG = "FCMRegIntentService";

    public FCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.registerFCM();
    }
    /*public void unsubsribe(){
        session = new Session(FCMRegistrationIntentService.this);
        HashMap<String, String> user = session.getUserDetails();
        strSchoolId = user.get(Session.KEY_SCHOOL_ID);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(strSchoolId);
    }*/
    public void registerFCM() {

        Intent registrationComplete = null;
        token = null;
        session = new Session(FCMRegistrationIntentService.this);
        HashMap<String, String> user = session.getUserDetails();

        strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
        strSchoolId = user.get(Session.KEY_SCHOOL_ID);

        try {

            token = FirebaseInstanceId.getInstance().getToken();


            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
             strImei = telephonyManager.getDeviceId();
            Log.e(TAG, "token: " + token);
            Log.e(TAG, "StaffId: " + strStaffId);
            Log.e(TAG, "deviceID: " + strImei);
            //notify UI that registration Successfull
            registrationComplete =  new Intent(FCMUtils.messages.REG_SUCCESS);
            registrationComplete.putExtra("token",token);
          //  SharedPreferences preferences = getSharedPreferences(config.sp.KYE_PREF_NAME,0);
           // boolean tokenStored = preferences.getBoolean(config.sp.SP_KEY_STORED_TOKEN,false);
            boolean tokenStored=session.isDeviceDetailsStored();

            Log.e("TAG", token+"Length = "+token.length());
            if(!tokenStored) {
                if (connection.netInfo(FCMRegistrationIntentService.this)) {
                    new SubmitToken().execute();
                }
                FirebaseMessaging.getInstance().subscribeToTopic(strSchoolId)


                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast toast= Toast.makeText(FCMRegistrationIntentService.this,"User Registered for school",Toast.LENGTH_SHORT);
                            }
                        });
            }




        }catch (Exception ee){
            Log.e(TAG,ee.toString());
            registrationComplete =  new Intent(FCMUtils.messages.REG_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

      class SubmitToken extends AsyncTask<String, JSONArray, String> {
         ProgressDialog progressDialog= new ProgressDialog(FCMRegistrationIntentService.this);
          @Override
          protected void onPreExecute() {
              super.onPreExecute();
          }

          @Override
          protected String doInBackground(String... strings) {
              GetResponse response = new GetResponse();
              JSONObject outObject = new JSONObject();
              String url= AD.url.base_url+"userOperations.jsp";
              try {
                  outObject.put(getString(R.string.key_OperationName), getString(R.string.web_submitStaffUserToken));
                  JSONObject userData = new JSONObject();

                  userData.put(getString(R.string.key_StaffId), strStaffId);
                  userData.put(getString(R.string.key_UserToken), token);
                  userData.put(getString(R.string.key_DeviceId), strImei);

                  outObject.put(getString(R.string.key_userData), userData);
                  String responseText = response.getServerResopnse(url, outObject.toString());
                  JSONObject inObject = new JSONObject(responseText);
                  Log.e("Tag", "outObject =" + outObject.toString());
                  Log.e("Tag", "responseText is =" + responseText);
                  String strMsg = inObject.getString(getString(R.string.key_Message));

                  String strStatus=inObject.getString(getString(R.string.key_Status));
                  int resid=inObject.getInt(getString(R.string.key_resultId));
                  Log.e("FCM Doinbacground",strStatus);
                  return String.valueOf(resid);

              } catch (Exception ex) {
                  Log.e("doinbg Execption", ex.toString());
                  return ex.toString();
              }
          }

          @Override
          protected void onPostExecute(String resultid) {
              super.onPostExecute(resultid);


              if(resultid.equalsIgnoreCase("-2")||resultid.equalsIgnoreCase("-1")) {
                  Log.e("PostExecute","this is run is Sucessful");
                 session.setDeviceDetails(true,strImei);

                  Toast.makeText(FCMRegistrationIntentService.this, "User has been Registered", Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(FCMRegistrationIntentService.this, "User was not Registered", Toast.LENGTH_SHORT).show();
              }
          }
      }
}
