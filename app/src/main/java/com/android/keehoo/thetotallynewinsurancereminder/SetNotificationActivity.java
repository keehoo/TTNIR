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
import android.widget.Switch;
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
    protected Switch switch_ins;
    protected Switch switch_tech;

    public PendingIntent pendingIntent;


    private boolean insReminderSet;
    private boolean techReminderSet;


    protected int wyprzedzenieAlarmu;


    protected int wyprzedzenieAlarmuTech;
    protected long alarmDate;
    SeekBar insuranceseekBar;
    SeekBar technicalSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        initiate();
        checkIfTheReminderIsSet();

        iloscDniDoKoncaUbezpieczenia = Integer.parseInt(getIntent().getExtras().getString(DisplayDataActivity.ILOSC_DNI, "nothing"));
        iloscDniDoKoncaPrzegladu = Integer.parseInt(getIntent().getExtras().getString(DisplayDataActivity.ILOSC_DNI_TECHNICAL, "nothing at all"));


        if (sharedPreferences.contains(MainActivity.SHARED_DATE)) {
            Log.d("SetNotificationActivity", "SharedPrefs contain SHARED_DATA");
            currentInsuranceDate = sharedPreferences.getLong(MainActivity.SHARED_DATE, -1);
            currentTechnicalDate = sharedPreferences.getLong(MainActivity.SHARED_DATE_TECHNICAL, -1);
            okresUbezpieczenia = sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_INS, 12);
            okresPrzegladu = sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_TECH, 12);

            status.setText("Obecna data podpisania umowy ubezpieczeniowej "
                    + System.getProperty("line.separator")
                    + dateText(new DateTime(currentInsuranceDate)) + System.getProperty("line.separator")
                    + "Obecna data wykonania przglądu " + System.getProperty("line.separator") + dateText(new DateTime(currentTechnicalDate)));


        } else {
            Log.d("SetNotificationActivity", "Shared prefs does not containg SHARED_DATA");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


    }


    private void scheduleNotification(String text, long milliseconds, int id) {

        if (id==10 || id==20) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent broadcast = new Intent(BROADCAST_ACTION);
            broadcast.putExtra(NotificationService.EXTRA_NOTIFICATION_TEXT, text);
            Log.d("MainActivity", "Nowy intent o nazwie broadcast (INTENT(BROADCAST_ACTION) put Extra pod zmienna text= EXTRA_NOTIFICATION_TEXT");
            PendingIntent alarmAction = PendingIntent.getBroadcast(this, id, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d("MainActivity", " PendingIntent alarmAction z parametrami this, hashCode, broadcast i pendingIntent.FLAG_UPDATE_CURRENT");
            pendingIntent = alarmAction; // to jest po to, zeby miec dostep do tego samego intentu zeby go anulowac (cancel)
            alarmManager.set(AlarmManager.RTC_WAKEUP, milliseconds, alarmAction);  //dodawanie long = int najwyrazniej daje longa...
            Log.d("MainActivity", "setAlarmManager z parametrami AlarmManager.RTC_WAKEUP, currentTime + sekundy ustawione wczesniej, alarmAction... alarmAction to jest pendingItentn powyzej");
            Toast.makeText(SetNotificationActivity.this, "Alarm Ustawiony na " + milliseconds + " sekund", Toast.LENGTH_SHORT).show();
        }
    else
            Log.d("scheduleNotification", "Bad id parameter for the schedule notification method, should be 10 for insurance or 20 for technical check");
    }

    @OnClick(R.id.clear_notification_id)
    protected void removeScheduledNotification() {
        boolean alarmUp = (PendingIntent.getBroadcast(this, 10,
                new Intent(BROADCAST_ACTION),
                PendingIntent.FLAG_NO_CREATE) != null);
        checkIfTheReminderIsSet();

        if (alarmUp && pendingIntent != null) {

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.d("SetNotificationActivity", "       Disabling the alarm . . . . . DISABLED!  ");
            sharedPreferences.edit().putBoolean(MainActivity.INS_REMINDER_SET, false).apply();
            //sharedPreferences.edit().putBoolean(MainActivity.TECH_REMINDER_SET, false).apply();

            resetSwitches();

        } else {
            Log.d("Is Alarm Set?", "                 Alarm isn't set at this moment, no need to disable anything");
            status.setText("Alarm is already disabled");
            resetSwitches();
        }

    }

    @OnClick(R.id.tech_reminder_delete_id)
    protected void removeTechScheduledNotification() {
        boolean alarmUp = (PendingIntent.getBroadcast(this, 20,
                new Intent(BROADCAST_ACTION),
                PendingIntent.FLAG_NO_CREATE) != null);
        checkIfTheReminderIsSet();

        if (alarmUp && pendingIntent != null) {

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.d("SetNotificationActivity", "       Disabling the alarm . . . . . DISABLED!  ");
            sharedPreferences.edit().putBoolean(MainActivity.TECH_REMINDER_SET, false).apply();

            resetSwitches();

        } else {
            Log.d("Is Alarm Set?", "                 Alarm isn't set at this moment, no need to disable anything");
            status.setText("Alarm is already disabled");
            resetSwitches();
        }

    }

    @OnClick(R.id.set_notification_id)
    public void onClick() {

        alarmDate = sharedPreferences.getLong(MainActivity.SHARED_DATE, -1);  // data podpisania umowy ubezpieczeniowej
        DateTime dt = new DateTime(alarmDate).plusMonths(okresUbezpieczenia);  //dt to data zakonczenia okresu ubezpieczenia
        scheduleNotification("!!! ALARM !!!", dt.minusDays(wyprzedzenieAlarmu).getMillis(), 10);  // zalaczenie alarmu na date o wyprzedzenie alarmu krotsza niz koniec okresu ubezpieczenia...
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainActivity.INS_REMINDER_SET, true).apply();
        resetSwitches();

        Log.d("OnClickSetIns", "ustawione przypomnienie ?  " + sharedPreferences.getBoolean(MainActivity.INS_REMINDER_SET, false));
    }

    @OnClick(R.id.set_notification_technical)
    public void onClickTech() {
        alarmDate = sharedPreferences.getLong(MainActivity.SHARED_DATE_TECHNICAL, -1);
        DateTime dt = new DateTime(alarmDate).plusMonths(okresPrzegladu);
        scheduleNotification("!!! ALARM !!!", dt.minusDays(wyprzedzenieAlarmu).getMillis(), 20);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainActivity.TECH_REMINDER_SET, true).apply();
        resetSwitches();
    }


    public void setWyprzedzenieAlarmu(int wyprzedzenieAlarmu) {
        this.wyprzedzenieAlarmu = wyprzedzenieAlarmu;
    }

    public void setWyprzedzenieAlarmuTech(int wyprzedzenieAlarmuTech) {
        this.wyprzedzenieAlarmuTech = wyprzedzenieAlarmuTech;
    }

    public String dateText(DateTime dateTime) {
/**
 * zmienia date z JodaTime na text - tylko dzien, miesiac i rok
 */
        int rok = dateTime.getYear();
        int miesiac = dateTime.getMonthOfYear();
        int dzien = dateTime.getDayOfMonth();
        return new String(dzien + " / " + miesiac + " / " + rok);
    }

    private void checkIfTheReminderIsSet() {
        if (sharedPreferences.getBoolean(MainActivity.INS_REMINDER_SET, false))
            setInsReminderSet(true);
        else setInsReminderSet(false);

        Log.d("CheckIfTheRemindersRUp", "Reminder is up?   " + isInsReminderSet());

        if (sharedPreferences.getBoolean(MainActivity.TECH_REMINDER_SET, false))
            setTechReminderSet(true);
        else setTechReminderSet(false);
        resetSwitches();
    }

    private void resetSwitches() {


        switch_ins.setEnabled(true);
        switch_ins.setChecked(sharedPreferences.getBoolean(MainActivity.INS_REMINDER_SET, false));
        Log.d("ResetSwitches", "set check z shared preferences  " + sharedPreferences.getBoolean(MainActivity.INS_REMINDER_SET, false));
        switch_ins.setEnabled(false);

        switch_tech.setEnabled(true);
        switch_tech.setChecked(sharedPreferences.getBoolean(MainActivity.TECH_REMINDER_SET, false));
        Log.d("ResetSwitches", "Technical switch, TECH REMINDER SET   "+ sharedPreferences.getBoolean(MainActivity.TECH_REMINDER_SET, false));
        switch_tech.setEnabled(false);

    }

    public boolean isInsReminderSet() {
        return insReminderSet;
    }

    public void setInsReminderSet(boolean insReminderSet) {
        this.insReminderSet = insReminderSet;
    }

    public boolean isTechReminderSet() {
        return techReminderSet;
    }

    public void setTechReminderSet(boolean techReminderSet) {
        this.techReminderSet = techReminderSet;
    }

    public void initiate() {

        switch_ins = (Switch) findViewById(R.id.switch_insurance);
        switch_tech = (Switch) findViewById(R.id.switch_technical);

        switch_ins.setChecked(sharedPreferences.getBoolean(MainActivity.INS_REMINDER_SET, false));
        switch_ins.setChecked(sharedPreferences.getBoolean(MainActivity.TECH_REMINDER_SET, false));

        switch_ins.setEnabled(false);
        switch_tech.setEnabled(false);

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
                status.setText("wyprzedzenie alarmu " + wyprzedzenieAlarmuTech);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Bind(R.id.status_id)
    protected TextView status;

    @Bind(R.id.alarm_ustawiony_id)
    protected TextView alarmUstawiony;

    @Bind(R.id.data_ubezpieczenia_id)
    protected TextView dataUbezpieczenia;

}
