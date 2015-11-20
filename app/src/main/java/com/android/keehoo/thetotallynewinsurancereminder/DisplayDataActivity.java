package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Date;

public class DisplayDataActivity extends AppCompatActivity {

    private long dataUbezpieczenieWMilisekundach;
    public SharedPreferences sharedPreferences;
    private TextView textView;
    private TextView textView2;
    public static final String SHARED_DATE = "data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        JodaTimeAndroid.init(this);


        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        dataUbezpieczenieWMilisekundach = (sharedPreferences.getLong(SHARED_DATE, -1));
        textView = (TextView) findViewById(R.id.display_status_id);
        textView2 = (TextView) findViewById(R.id.display_status_id2);
        textView.setText("Data ustawiona w milisekundach:   " + dataUbezpieczenieWMilisekundach);

        Date data = new Date(dataUbezpieczenieWMilisekundach);
        DateTime dt = new DateTime(data);
        //LocalDate lc = new LocalDate(dt);

        textView2.setText("Data w milisekuncach przerobiona za pomoca Joda Time na date : " + dt.toString());







    }




}
