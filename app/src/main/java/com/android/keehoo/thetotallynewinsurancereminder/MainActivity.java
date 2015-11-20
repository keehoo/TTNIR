package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    public static final String SHARED_PREFS_NAME = "prefs";
    public static final String SHARED_DATE = "data";
    public long ustwionaDataWMilisekundach;

    public SharedPreferences sharedPreferences;
    private Button sd;
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("OnCreate-Activity Main", "            OnCreate w Main Activity");

        /*boolean back = getIntent().getBooleanExtra("onbackpressed", false);
        Log.d("Back Button Pressed?", "          back button pressed? " + back);
        if (back) {
            finish();
            Log.d("System", "Exit(0)");
            System.exit(0);
        }*/


        sd = (Button) findViewById(R.id.set_date_is);
        textView = (TextView) findViewById(R.id.status_id);

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        if (sharedPreferences.contains(SHARED_DATE)) {   //Data juz ustawiona i zapisana w sharedPrefs.
            Log.d("SharedPreferences", "         Shared Preferences contain SHARED_DAT tag");
            Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
            intent.putExtra("data", ustwionaDataWMilisekundach);
            startActivity(intent);
            finish();

        } else {
            Log.d("SharedPreferences", "      Shared Preferences does not contain SHARED_DAT tag");
            Toast.makeText(MainActivity.this, "Wybierz date ubezpieczenia", Toast.LENGTH_LONG).show();
            sd.setOnClickListener(new View.OnClickListener() {
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

                }

            });
        }

    }


    public long getDateFromDatePicket(DatePicker datePicker) {   // chce zeby ta metoda zwracala date z datepickera w milisekundach
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
        //Toast.makeText(Context.UstawDateUbezpieczeniaActivity.this, "asdasdasd", Toast.LENGTH_SHORT).show();
    }


}
