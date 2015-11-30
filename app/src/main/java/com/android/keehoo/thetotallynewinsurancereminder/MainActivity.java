package com.android.keehoo.thetotallynewinsurancereminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION =
            BuildConfig.APPLICATION_ID + ".BROADCAST_ACTION";
    public static final String SHARED_PREFS_NAME = "prefs";
    public static final String SHARED_DATE = "data";
    public long ustwionaDataWMilisekundach;
    private boolean set;

    public SharedPreferences sharedPreferences;
    private Button sd;
    private TextView textView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ustaw_date) {
            final DatePicker dp = (DatePicker) findViewById(R.id.dp);
            ustwionaDataWMilisekundach = getDateFromDatePicket(dp);
            sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(SHARED_DATE, ustwionaDataWMilisekundach).apply();
            Log.d("SharedPreferences", "Zmienna ustawionaDataWMilisekundach o wartosci "
                    + ustwionaDataWMilisekundach + " zostala zapisana w SharedPreferences pod tagiem SHARED_DATE");
            set = true;
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
            startActivity(intent);
            Log.d("OnOptionsItemSelected", "   Wybrano date i uruchomiono Activity Display Date");
            finish();
            return true;
        } else if (item.getItemId() == R.id.ustaw_notification) {
            Intent intent = new Intent(this, SetNotificationActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("OnCreate-Activity Main", "            OnCreate w Main Activity");

        //sd = (Button) findViewById(R.id.set_date_is);
        //textView = (TextView) findViewById(R.id.status_id);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        if (sharedPreferences.contains(SHARED_DATE)) {
            //Data juz ustawiona i zapisana w sharedPrefs. Otwieramy nowe okno z wywietlaczem
            Log.d("SharedPreferences", "         Shared Preferences contains SHARED_DATA tag");
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
            intent.putExtra("data", ustwionaDataWMilisekundach);
            startActivity(intent);
            finish();


        } else {
            Log.d("SharedPreferences", "      Shared Preferences does not contain SHARED_DAT tag");
            Toast.makeText(MainActivity.this, "Wybierz date ubezpieczenia", Toast.LENGTH_LONG).show();
        /*    sd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatePicker dp = (DatePicker) findViewById(R.id.dp);
                    ustwionaDataWMilisekundach = getDateFromDatePicket(dp);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String ustawionaDataJakoData = formatter.format(new Date(ustwionaDataWMilisekundach));
                    textView.setText(ustawionaDataJakoData);
                    sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(SHARED_DATE, ustwionaDataWMilisekundach).apply();
                    Log.d("SharedPreferences", "Zmienna ustawionaDataWMilisekundach o wartosci "
                            + ustwionaDataWMilisekundach + " zostala zapisana w SharedPreferences pod tagiem SHARED_DATE");
                    Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
                    startActivity(intent);

                }

            });*/
        }
    }

    /**
     * getDateFromDatePicket - zwraca wybrana date z Date Picker'a - returns Calendar in miliseconds
     */

    public long getDateFromDatePicket(DatePicker datePicker) {
        // chce zeby ta metoda zwracala date z datepickera w milisekundach
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

   /* private void scheduleNotification(String text, int seconds) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Log.d("MainActivity", "  alarmManager - getSystemService(Context.ALARM_SERVICE");

        Intent broadcast = new Intent(BROADCAST_ACTION);
        broadcast.putExtra(NotificationService.EXTRA_NOTIFICATION_TEXT, text);
        //Log.d("MainActivity", "Nowy intent o nazwie broadcast (INTENT(BROADCAST_ACTION) put Extra pod zmienna text= EXTRA_NOTIFICATION_TEXT");

        PendingIntent alarmAction = PendingIntent.getBroadcast(this, text.hashCode(), broadcast, PendingIntent.FLAG_UPDATE_CURRENT);

        //Log.d("MainActivity", " PendingIntent alarmAction z parametrami this, hashCode, broadcast i pendingIntent.FLAG_UPDATE_CURRENT");
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), alarmAction);
        //Log.d("MainActivity", "setAlarmManager z parametrami AlarmManager.RTC_WAKEUP, currentTime + sekundy ustawione wczesniej, alarmAction... alarmAction to jest pendingItentn powyzej");
    }*/
}
