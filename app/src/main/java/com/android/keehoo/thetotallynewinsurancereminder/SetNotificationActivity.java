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
import android.widget.Toast;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetNotificationActivity extends AppCompatActivity {

    public static final String SHARED_DATE = "data";
    public static final String SHARED_PREFS_NAME = "prefs";
    public static final String BROADCAST_ACTION = BuildConfig.APPLICATION_ID + ".BROADCAST_ACTION";
    public SharedPreferences sharedPreferences;

    protected long currentInsuranceDate;
    protected long alarmDate;

    @OnClick(R.id.set_notification_id)
    public void onClick() {

        alarmDate = sharedPreferences.getLong(SHARED_DATE, -1);
        DateTime dt = new DateTime(alarmDate);
        dt = dt.plusMonths(11);
        scheduleNotification("!!! ALARM !!!", dt.getMillis());
        status.setText("Alarm Ustawiony na " + dt.getDayOfWeek() + " / " + dt.getDayOfMonth() + " / " + dt.getMonthOfYear() + " / " + dt.getYear());
        alarmUstawiony.setText("alarm ustawiony");
        dataUbezpieczenia.setText("Data ubezpieczenia " + (new Date(alarmDate).toString()));
    }

    @Bind(R.id.status_id)
    protected TextView status;

    @Bind(R.id.alarm_ustawiony_id)
    protected TextView alarmUstawiony;

    @Bind(R.id.data_ubezpieczenia_id)
    protected TextView dataUbezpieczenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);


        if (sharedPreferences.contains(SHARED_DATE)) {
            Log.d("SetNotificationActivity", "SharedPrefs contain SHARED_DATA");
            currentInsuranceDate = sharedPreferences.getLong(SHARED_DATE, -1);
            status.setText("Current insurance date in millis: " + currentInsuranceDate);


        } else {
            Log.d("SetNotificationActivity", "Shared prefs does not containg SHARED_DATA");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void scheduleNotification(String text, long seconds) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent broadcast = new Intent(BROADCAST_ACTION);
        broadcast.putExtra(NotificationService.EXTRA_NOTIFICATION_TEXT, text);
        Log.d("MainActivity", "Nowy intent o nazwie broadcast (INTENT(BROADCAST_ACTION) put Extra pod zmienna text= EXTRA_NOTIFICATION_TEXT");
        PendingIntent alarmAction = PendingIntent.getBroadcast(this, 10, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("MainActivity", " PendingIntent alarmAction z parametrami this, hashCode, broadcast i pendingIntent.FLAG_UPDATE_CURRENT");
        alarmManager.set(AlarmManager.RTC_WAKEUP, seconds, alarmAction);  //dodawanie long = int najwyrazniej daje longa...
        Log.d("MainActivity", "setAlarmManager z parametrami AlarmManager.RTC_WAKEUP, currentTime + sekundy ustawione wczesniej, alarmAction... alarmAction to jest pendingItentn powyzej");
        Toast.makeText(SetNotificationActivity.this, "Alarm Ustawiony na " + seconds + " sekund", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.clear_notification_id)
    protected void removeScheduledNotification() {
        boolean alarmUp = (PendingIntent.getBroadcast(this, 10,
                new Intent(BROADCAST_ACTION),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            Log.d("SetNotificationActivity", "       Alarm is already active");
            status.setText("Alarm jest zalaczony!");
        }
    }
}
