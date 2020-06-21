package com.vinuthana.vinvidyaadmin.fcm;


import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Anirudh on 10/07/16.
 */
public class FCMTokenRefreshListenerService extends FirebaseInstanceIdService {
   /*
    *
    *  When token is refreshed and to get new Token
    */
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent ii = new Intent(this, FCMRegistrationIntentService.class);
        startService(ii);
    }
}
