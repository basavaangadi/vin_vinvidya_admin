package com.vinuthana.vinvidyaadmin.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vinuthana.vinvidyaadmin.R;

import java.util.Map;
import java.util.Random;




/**
 * Created by Anirudh on 10/07/16.
 */
public class FCMPushReceiverService extends FirebaseMessagingService {

    public static final int REQUEST_CODE = 0;

    public static final String KEY = "message";
    int cnt=0;
    private static final String TAG = "FCMPushReceiverService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        String message = remoteMessage.getNotification().getBody();
        String clickAction = remoteMessage.getNotification().getClickAction();
        pushNotification(message, clickAction);
    }

    public void pushNotification(String message, String clickAtion){
        Intent ii = new Intent(clickAtion);
        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, ii, PendingIntent.FLAG_ONE_SHOT);
        // setting notificaion sound
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // build notification
        Bitmap largIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_launcher);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setColor(getResources().getColor(R.color.colorPrimary))
                .setVibrate(new long[] { 200, 200, 0, 0, 0 })
                .setContentTitle(getString(R.string.app_name))
                //.setContentTitle("ದಿಗ್ವಿಜಯ ಟಿವಿ")
                .setLargeIcon(largIcon)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int id = random.nextInt();
        notificationManager.notify(id,notificationBuilder.build());
    }
}

// http://apns-gcm.bryantan.info/

/*
        NotificationCompat notificationCompat = new NotificationCompat.BigTextStyle(notificationBuilder).bigText(message).build();
        //Notification notification = new Notification.BigTextStyle(notificationBuilder).bigText(message).build()*/