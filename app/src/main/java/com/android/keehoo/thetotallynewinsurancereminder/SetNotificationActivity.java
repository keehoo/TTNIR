package com.android.keehoo.thetotallynewinsurancereminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetNotificationActivity extends AppCompatActivity {

    public static final String SHARED_DATE = "data";
    public static final String SHARED_PREFS_NAME = "prefs";
    public static final String BROADCAST_ACTION = BuildConfig.APPLICATION_ID + ".BROADCAST_ACTION";
    public SharedPreferences sharedPreferences;

    protected long currentInsuranceDate;

    @OnClick(R.id.set_notification_id)
    public void onClick() {
        scheduleNotification("Powiadomienie", 5);
    }

    @Bind(R.id.status_id)
    protected TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(SHARED_DATE)) {
            Log.d("SetNotificationActivity", "SharedPrefs contain SHARED_DATA");
            currentInsuranceDate = sharedPreferences.getLong(SHARED_DATE, -1);
            status.setText("Current insurance date in millis: " + currentInsuranceDate);


        } else Log.d("SetNotificationActivity", "Shared prefs does not containg SHARED_DATA");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void scheduleNotification(String text, int seconds) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent broadcast = new Intent(BROADCAST_ACTION);
        broadcast.putExtra(NotificationService.EXTRA_NOTIFICATION_TEXT, text);
        Log.d("MainActivity", "Nowy intent o nazwie broadcast (INTENT(BROADCAST_ACTION) put Extra pod zmienna text= EXTRA_NOTIFICATION_TEXT");
        PendingIntent alarmAction = PendingIntent.getBroadcast(this, text.hashCode(), broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("MainActivity", " PendingIntent alarmAction z parametrami this, hashCode, broadcast i pendingIntent.FLAG_UPDATE_CURRENT");
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), alarmAction);  //dodawanie long = int najwyrazniej daje longa...
        Log.d("MainActivity", "setAlarmManager z parametrami AlarmManager.RTC_WAKEUP, currentTime + sekundy ustawione wczesniej, alarmAction... alarmAction to jest pendingItentn powyzej");
    }
}
