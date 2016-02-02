package com.multivonex.keehoo.thetotallynewinsurancereminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.android.keehoo.thetotallynewinsurancereminder.R;

import java.util.UUID;


public class NotificationService extends IntentService {
    public static final String EXTRA_NOTIFICATION_TEXT = "notification_text";

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationText = intent.getStringExtra(EXTRA_NOTIFICATION_TEXT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle(notificationText)
                .setTicker(notificationText)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true).build();

        notificationManager.notify(UUID.randomUUID().toString(), 1, notification);
    }
}