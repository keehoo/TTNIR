package com.android.keehoo.thetotallynewinsurancereminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetNotificationActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION = BuildConfig.APPLICATION_ID + ".BROADCAST_ACTION";
    public SharedPreferences sharedPreferences;

    protected long currentInsuranceDate;
    protected long currentTechnicalDate;
    protected int okresUbezpieczenia;
    protected int okresPrzegladu;
    protected int iloscDniDoKoncaPrzegladu;
    protected int iloscDniDoKoncaUbezpieczenia;


    protected int wyprzedzenieAlarmu;


    protected int wyprzedzenieAlarmuTech;
    protected long alarmDate;
    SeekBar insuranceseekBar;
    SeekBar technicalSeekBar;


    @Bind(R.id.status_id)
    protected TextView status;

    @Bind(R.id.alarm_ustawiony_id)
    protected TextView alarmUstawiony;

    @Bind(R.id.data_ubezpieczenia_id)
    protected TextView dataUbezpieczenia;

    public PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        iloscDniDoKoncaUbezpieczenia = Integer.parseInt(getIntent().getExtras().getString(DisplayDataActivity.ILOSC_DNI, "nothing"));
        iloscDniDoKoncaPrzegladu = Integer.parseInt(getIntent().getExtras().getString(DisplayDataActivity.ILOSC_DNI_TECHNICAL, "nothing at all"));


        if (sharedPreferences.contains(MainActivity.SHARED_DATE)) {
            Log.d("SetNotificationActivity", "SharedPrefs contain SHARED_DATA");
            currentInsuranceDate = sharedPreferences.getLong(MainActivity.SHARED_DATE, -1);
            currentTechnicalDate = sharedPreferences.getLong(MainActivity.SHARED_DATE_TECHNICAL, -1);
            okresUbezpieczenia = sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_INS, 12);
            okresPrzegladu = sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_TECH, 12);

            status.setText("Data rozpoczecia okres ubezpieczenia to " + new DateTime(currentInsuranceDate) + " i bedzie trwal przez  " +
                    okresUbezpieczenia + " miesiecy" + " a obecnie do konca pozostalo" + iloscDniDoKoncaUbezpieczenia + " dni");
        } else {
            Log.d("SetNotificationActivity", "Shared prefs does not containg SHARED_DATA");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        insuranceseekBar = (SeekBar) findViewById(R.id.seekBar_insurance_notice_id);
        insuranceseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setWyprzedzenieAlarmu(progress);
                status.setText("Alarm zadzwoni " + wyprzedzenieAlarmu + " dni przed koncem ubezpieczenia");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        technicalSeekBar = (SeekBar) findViewById(R.id.seekBar_technical_notice_id);
        technicalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setWyprzedzenieAlarmuTech(progress);
                status.setText("wyprzedzenie alarmu "+wyprzedzenieAlarmuTech);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void scheduleNotification(String text, long milliseconds) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent broadcast = new Intent(BROADCAST_ACTION);
        broadcast.putExtra(NotificationService.EXTRA_NOTIFICATION_TEXT, text);
        Log.d("MainActivity", "Nowy intent o nazwie broadcast (INTENT(BROADCAST_ACTION) put Extra pod zmienna text= EXTRA_NOTIFICATION_TEXT");
        PendingIntent alarmAction = PendingIntent.getBroadcast(this, 10, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("MainActivity", " PendingIntent alarmAction z parametrami this, hashCode, broadcast i pendingIntent.FLAG_UPDATE_CURRENT");
        pendingIntent = alarmAction; // to jest po to, zeby miec dostep do tego samego intentu zeby go anulowac (cancel)
        alarmManager.set(AlarmManager.RTC_WAKEUP, milliseconds, alarmAction);  //dodawanie long = int najwyrazniej daje longa...
        Log.d("MainActivity", "setAlarmManager z parametrami AlarmManager.RTC_WAKEUP, currentTime + sekundy ustawione wczesniej, alarmAction... alarmAction to jest pendingItentn powyzej");
        Toast.makeText(SetNotificationActivity.this, "Alarm Ustawiony na " + milliseconds + " sekund", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.clear_notification_id)
    protected void removeScheduledNotification() {
        boolean alarmUp = (PendingIntent.getBroadcast(this, 10,
                new Intent(BROADCAST_ACTION),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            //PendingIntent.getBroadcast(this, 10, new Intent(BROADCAST_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.d("SetNotificationActivity", "       Disabling the alarm . . . . . DISABLED!  ");

        } else {
            Log.d("Is Alarm Set?", "                 Alarm isn't set at this moment");
            status.setText("Alarm is disabled");
        }
    }

    @OnClick(R.id.set_notification_id)
    public void onClick() {

        alarmDate = sharedPreferences.getLong(MainActivity.SHARED_DATE, -1);
        DateTime dt = new DateTime(alarmDate).plusMonths(okresUbezpieczenia);
        scheduleNotification("!!! ALARM !!!", dt.minusDays(wyprzedzenieAlarmu).getMillis());
        status.setText("Alarm ustawiony na " + dt.minusDays(wyprzedzenieAlarmu) + "wyprzedzenie alarmu to " + wyprzedzenieAlarmu + " " +
                "data podpisania umowy ubezpieczeniowej " + alarmDate + " okres ubezpieczenia to " + okresUbezpieczenia);
    }

    @OnClick(R.id.set_notification_technical)
    public void onClickTech() {
        alarmDate = sharedPreferences.getLong(MainActivity.SHARED_DATE_TECHNICAL, -1);
        DateTime dt = new DateTime(alarmDate).plusMonths(okresPrzegladu);
        scheduleNotification("!!! ALARM !!!", dt.minusDays(wyprzedzenieAlarmu).getMillis());


    }


    public void setWyprzedzenieAlarmu(int wyprzedzenieAlarmu) {
        this.wyprzedzenieAlarmu = wyprzedzenieAlarmu;
    }

    public void setWyprzedzenieAlarmuTech(int wyprzedzenieAlarmuTech) {
        this.wyprzedzenieAlarmuTech = wyprzedzenieAlarmuTech;
    }


}
