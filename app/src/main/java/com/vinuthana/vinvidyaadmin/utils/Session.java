package com.vinuthana.vinvidyaadmin.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vinuthana.vinvidyaadmin.activities.useractivities.LoginActivity;
import com.vinuthana.vinvidyaadmin.fcm.FCMRegistrationIntentService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Krish on 05-10-2017.
 */

public class Session {
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NO = "PhoneNo";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_SCHOOL_ID = "schoolId";
    public static final String KEY_DESIGNATION = "Designation";
    public static final String KEY_STAFFDETAILS_ID = "StaffDetailsId";
    public static final String KEY_STAFF_ID = "StaffId";
    private static final String PREFE_NAME = "StaffSession";
    private static final String IS_LOGIN = "IsLoggedin";
    public static final String KEY_ACADEMIC_YEAR_ID = "AcademicYearId";
    public static final String KEY_ROLE_ID = "RoleId";
    public static final String KEY_SCHOOL = "School";
    public static  final String KEY_USER_TOKEN="UserToken";
    public static final String KEY_DEVICE_ID="DeviceID";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    ArrayList<String> list = new ArrayList<>();


    public Session(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean("loggedInmode", loggedIn);
        editor.commit();
    }

    public void createLoginSession(String name,  String pNum, String strStaffId,
                                   String schoolId, String stfDetailsId, String strEmail, String strAcademicYearId,
                                   String strRoleId, String strDesignation,String strSchool) {
        //editor.putBoolean(IS_LOGIN,true);
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE_NO, pNum);
        editor.putString(KEY_STAFF_ID, strStaffId);
        editor.putString(KEY_SCHOOL_ID, schoolId);
        editor.putString(KEY_STAFFDETAILS_ID, stfDetailsId);
        editor.putString(KEY_EMAIL, strEmail);
        editor.putString(KEY_ACADEMIC_YEAR_ID, strAcademicYearId);
        editor.putString(KEY_DESIGNATION, strDesignation);
        editor.putString(KEY_ROLE_ID, strRoleId);
        editor.putString(KEY_SCHOOL, strSchool);
        editor.commit();

    }
    public void setDeviceDetails(Boolean tokenStored,String deviceId){
        editor.putBoolean(KEY_USER_TOKEN, tokenStored);
        editor.putString(KEY_DEVICE_ID, deviceId);
        editor.commit();
    }
    public boolean isDeviceDetailsStored(){
        return preferences.getBoolean(KEY_USER_TOKEN,false);
    }
    public void checkLogin() {
        if (!this.loggedin()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /*Get stored session data*/
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, preferences.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        user.put(KEY_PHONE_NO, preferences.getString(KEY_PHONE_NO, null));
        user.put(KEY_STAFF_ID, preferences.getString(KEY_STAFF_ID, null));
        user.put(KEY_SCHOOL_ID, preferences.getString(KEY_SCHOOL_ID, null));
        user.put(KEY_STAFFDETAILS_ID, preferences.getString(KEY_STAFFDETAILS_ID, null));
        user.put(KEY_ACADEMIC_YEAR_ID, preferences.getString(KEY_ACADEMIC_YEAR_ID, null));
        user.put(KEY_DESIGNATION, preferences.getString(KEY_DESIGNATION, null));
        user.put(KEY_ROLE_ID, preferences.getString(KEY_ROLE_ID, null));
        user.put(KEY_SCHOOL, preferences.getString(KEY_SCHOOL, null));
        return user;
    }

    public ArrayList<String> arrayList() {
        ArrayList<String> strList = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            strList.add(i, preferences.getString("list", strList.get(i)));
        }
        return strList;
    }

    /**
     * Clear session details
     */
    public void logOut() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

         /*FCMRegistrationIntentService fcmRegistrationIntentService= new FCMRegistrationIntentService();
        fcmRegistrationIntentService.unsubsribe();*/
    }

    public boolean loggedin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
