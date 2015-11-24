package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewDebug;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DisplayDataActivity extends AppCompatActivity {

    private long dataUbezpieczenieWMilisekundach;
    public SharedPreferences sharedPreferences;
    private TextView textView;
    private TextView textView2;
    private TextView finalDisplayDays;
    public static final String SHARED_DATE = "data";
    private String dateAsString;


    @Override


    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        JodaTimeAndroid.init(this);

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        dataUbezpieczenieWMilisekundach = (sharedPreferences.getLong(SHARED_DATE, -1));
        textView = (TextView) findViewById(R.id.display_status_id);
        textView2 = (TextView) findViewById(R.id.display_status_id2);
        finalDisplayDays = (TextView) findViewById(R.id.days_left_display_id);
        textView.setText("Data ustawiona w milisekundach:   " + dataUbezpieczenieWMilisekundach);

        DateTime dt = new DateTime(dataUbezpieczenieWMilisekundach);
        DateTime dtYear = new DateTime(dt.plusYears(1));
        DateTime dtSixMonths = new DateTime(dt.plusMonths(6));


        textView2.setText("Data w milisekuncach przerobiona za pomoca Joda Time na date : " + dt.toString());

        finalDisplayDays.setText((dtYear.getDayOfWeek() + " / " + dtYear.getMonthOfYear() + " / " + dtYear.getYear()));
    }
}
