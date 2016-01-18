package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final String SHARED_DATE_INS_MILLIS_DURATION = "insurance duration in millis";
    public static final String SHARED_DATE_TECH_MILLIS_DURATION = "technical check duration in millis";  // to tez trzeba polaczy w klasie display ...
    public static final String INS_REMINDER_SET = "ustawione przypomnienie insurance";
    public static final String TECH_REMINDER_SET = "ustawione przypomnienie technical check";

    public static final String INSURANCE_BUTTON_ENABLED = "false";
    public static final String TECHNICAL_BUTTON_ENABLED = "false";

    public long ustwionaDataWMilisekundach;
    public long ustawionaDataTechWMilisekundach;
    public SharedPreferences sharedPreferences;

    Button insButton;  // default - visible only in package
    Button techButton;  //default accessor - visible only in package - I hope :)
    Button okButton; // button that saves the dates in the shared prefs


    @Bind(R.id.obecna_data_ubezpieczenia)
    protected TextView obecnaDataUbezpieczenia;

    @Bind(R.id.obecna_data_technical)
    protected TextView obecnaDataTechnical;

    @OnClick(R.id.ok_button_id)
    public void onOkClick() {

        if (ustawionaDataTechWMilisekundach == 0 || ustawionaDataTechWMilisekundach == 0) {
            Toast.makeText(MainActivity.this, "Nie wybrano jednej z dat", Toast.LENGTH_SHORT).show();

        } else {
            saveInSharedPreferences(SHARED_DATE, ustwionaDataWMilisekundach);
            saveInSharedPreferences(SHARED_DATE_TECHNICAL, ustawionaDataTechWMilisekundach);
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.set_insurance_date_id)
    public void onSetInsuranceDateClick() {
        final DatePicker dp = (DatePicker) findViewById(R.id.dp);
        ustwionaDataWMilisekundach = getDateFromDatePicket(dp);
        Log.d("MainActivity", "Data ubezpieczenia ustawiona na " + ustwionaDataWMilisekundach);
        /**
         * saveInSharedPreferences(SHARED_DATE, ustwionaDataWMilisekundach);
         * komentuje to, bo data powinna sie zapisywac w shared prefs w momencie klikniecia buttona ok!
         */
        obecnaDataUbezpieczenia.setText("Obecna data ubezpieczenia " + dateText(new DateTime(ustwionaDataWMilisekundach)));

    }

    @OnClick(R.id.set_technical_date_id)
    public void onSetTechnicalDateClick() {
        final DatePicker dp = (DatePicker) findViewById(R.id.dp);
        ustawionaDataTechWMilisekundach = getDateFromDatePicket(dp);
        Log.d("MainActivity", "Data przegladu ustawiona na " + ustawionaDataTechWMilisekundach);
        //saveInSharedPreferences(SHARED_DATE_TECHNICAL, ustwionaDataWMilisekundach);
        obecnaDataTechnical.setText("Obecna data przegladu " + dateText(new DateTime(ustawionaDataTechWMilisekundach)));


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        insButton = (Button) findViewById(R.id.set_insurance_date_id);

        techButton = (Button) findViewById(R.id.set_technical_date_id);


        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().getBoolean(INSURANCE_BUTTON_ENABLED)) {
                insButton.setEnabled(true);
                //ustawionaDataTechWMilisekundach = sharedPreferences.getLong(SHARED_DATE_TECHNICAL, 0L);

            } else {
                insButton.setEnabled(false);
                ustawionaDataTechWMilisekundach = sharedPreferences.getLong(SHARED_DATE_TECHNICAL, 0L);
            }
            if (getIntent().getExtras().getBoolean(TECHNICAL_BUTTON_ENABLED)) {
                techButton.setEnabled(true);
               // ustwionaDataWMilisekundach = sharedPreferences.getLong(SHARED_DATE, 0L);
            } else {techButton.setEnabled(false);
                ustwionaDataWMilisekundach = sharedPreferences.getLong(SHARED_DATE, 0L);

            }
        }


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

    public String dateText(DateTime dateTime) {
/**
 * zmienia date z JodaTime na text - tylko dzien, miesiac i rok
 */
        int rok = dateTime.getYear();
        int miesiac = dateTime.getMonthOfYear();
        int dzien = dateTime.getDayOfMonth();
        return new String(dzien + " / " + miesiac + " / " + rok);
    }
}
