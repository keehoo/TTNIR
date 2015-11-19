package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    public static final String SHARED_PREFS_NAME = "prefs";
    public static final String SHARED_DATE = "data";
    public long ustawionaData;
    public SharedPreferences insurance_date;
    private Button sd;


    public long getDateFromDatePicket(DatePicker datePicker) {   // chce zeby ta metoda zwracala date z datepickera w milisekundach
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
        //Toast.makeText(Context.UstawDateUbezpieczeniaActivity.this, "asdasdasd", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatePicker dp = (DatePicker) findViewById(R.id.dp);

            sd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ustawionaData = getDateFromDatePicket(dp);
                Intent intent = new Intent(MainActivity.this, DisplayDataActivity.class);
                intent.putExtra("data", ustawionaData);
                insurance_date = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = insurance_date.edit();
                editor.putLong(SHARED_DATE, ustawionaData);
                editor.commit();
                startActivity(intent);
            }
        });
    }
}
