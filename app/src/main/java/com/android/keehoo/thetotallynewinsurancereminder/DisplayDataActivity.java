package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Days;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DisplayDataActivity extends AppCompatActivity {

    private long dataUbezpieczenieWMilisekundach;
    public SharedPreferences sharedPreferences;
    private TextView finalDisplayDays;
    public static final String SHARED_DATE = "data";
    private String days;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("DisplayData", "OnBackPressed");
    }

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
        finalDisplayDays = (TextView) findViewById(R.id.days_left_display_id);

        DateTime dt = new DateTime(dataUbezpieczenieWMilisekundach);
        DateTime dtYear = new DateTime(dt.plusYears(1));
        DateTime dtSixMonths = new DateTime(dt.plusMonths(6)); // unused yet - the funcionality will be added to display insurances that last 6 months or custom amount of days.
        DateTime now = new DateTime();

        days = String.valueOf(Days.daysBetween(now.withTimeAtStartOfDay(), dtYear.withTimeAtStartOfDay()).getDays());
        finalDisplayDays.setText(days);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ustaw_nowa_date_id) {
            Log.d("OnOptionsItemSelected", "      WYBRANO: set_data_id");
            sharedPreferences.edit().remove("data").apply();
            Log.d("OnOptionsItemSelected", "      Usunieto SHARED PREFERENCES");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.ustaw_notification) {
            Intent intent = new Intent(DisplayDataActivity.this, SetNotificationActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
