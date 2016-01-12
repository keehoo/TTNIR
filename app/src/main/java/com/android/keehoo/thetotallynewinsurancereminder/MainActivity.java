package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_NAME = "prefs";
    public static final String SHARED_DATE = "data";
    public static final String SHARED_DATE_TECHNICAL = "data_technical";
    public static final String SHARED_DATE_DURATION_TECH = "duration_of_the_technical";
    public static final String SHARED_DATE_DURATION_INS = "duration_of_the_insurance";

    public static final String INSURANCE_BUTTON_ENABLED = "false";
    public static final String TECHNICAL_BUTTON_ENABLED = "false";

    public long ustwionaDataWMilisekundach;
    public SharedPreferences sharedPreferences;

    Button insButton;  // default - visible only in package
    Button techButton;  //default accessor - visible only in package - I hope :)


    @Bind(R.id.obecna_data_ubezpieczenia)
    protected TextView obecnaDataUbezpieczenia;

    @Bind(R.id.obecna_data_technical)
    protected TextView obecnaDataTechnical;

    @OnClick(R.id.set_insurance_date_id)
    public void onSetInsuranceDateClick() {
        final DatePicker dp = (DatePicker) findViewById(R.id.dp);
        ustwionaDataWMilisekundach = getDateFromDatePicket(dp);
        saveInSharedPreferences(SHARED_DATE, ustwionaDataWMilisekundach);
        obecnaDataUbezpieczenia.setText("Obecna data ubezpieczenia " + dateText(new DateTime(sharedPreferences.getLong("data", -1))));
        ustwionaDataWMilisekundach = 0;
    }

    @OnClick(R.id.set_technical_date_id)
    public void onSetTechnicalDateClick() {
        final DatePicker dp = (DatePicker) findViewById(R.id.dp);
        ustwionaDataWMilisekundach = getDateFromDatePicket(dp);
        saveInSharedPreferences(SHARED_DATE_TECHNICAL, ustwionaDataWMilisekundach);
        obecnaDataTechnical.setText("Obecna data przegladu " + dateText(new DateTime(sharedPreferences.getLong("data_technical", -1))));
        ustwionaDataWMilisekundach = 0;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Log.d("MainActivity", "start");
        insButton = (Button) findViewById(R.id.set_insurance_date_id);
        Log.d("MainActivity", "insButton");
        techButton = (Button) findViewById(R.id.set_technical_date_id);
        Log.d("MainActivity", "techButton");

        if (getIntent().getExtras() != null) {
            insButton.setEnabled(getIntent().getExtras().getBoolean(INSURANCE_BUTTON_ENABLED, false));
            Log.d("MainActivity", "getBoolean INSURANCE BUTTON ENABLED");
            techButton.setEnabled(getIntent().getExtras().getBoolean(TECHNICAL_BUTTON_ENABLED, false));
            Log.d("MainActivity", "getBoolean TECH BUTTON ENABLED");
        }
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        if (sharedPreferences.contains(SHARED_DATE) && sharedPreferences.contains(SHARED_DATE_TECHNICAL)) {
            //Data juz ustawiona i zapisana w sharedPrefs. Otwieramy nowe okno z wywietlaczem
            Log.d("MainActivity - OnCreate", "    Shared prefs contain both - insurance date and technical check date");
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
            startActivity(intent);
            finish();
        }
        if (sharedPreferences.contains(SHARED_DATE_TECHNICAL)) {
            Log.d("MainActivity - OnCreate", "    Shared prefs contain technical check date only");
            insButton.setEnabled(true);

        }
        if (sharedPreferences.contains(SHARED_DATE)) {
            Log.d("MainActivity - OnCreate", "    Shared prefs contain insurance date only ");
            techButton.setEnabled(true);
        } else {
            Log.d("SharedPreferences", "      Shared Preferences does not contain SHARED_DAT tag");
        }
    }


    public long getDateFromDatePicket(DatePicker datePicker) {
        /**
         * getDateFromDatePicket - zwraca wybrana date z Date Picker'a - returns Calendar in miliseconds
         */
        // chce zeby ta metoda zwracala date z datepickera w milisekundach
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public void saveInSharedPreferences(String key, long dataWmilisekundach) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, dataWmilisekundach).apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ustaw_date) {
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String dateText(DateTime dateTime) {
/**
 * zmienia date z JodaTime na text - tylko dzien, miesiac i rok
 */
        int rok = dateTime.getYear();
        int miesiac = dateTime.getMonthOfYear();
        int dzien = dateTime.getDayOfMonth();
        return new String(dzien + " " + miesiac + " " + rok);
    }
}
