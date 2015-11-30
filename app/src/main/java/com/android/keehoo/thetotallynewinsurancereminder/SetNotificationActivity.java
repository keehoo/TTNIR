package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SetNotificationActivity extends AppCompatActivity {

    public static final String SHARED_DATE = "data";
    public static final String SHARED_PREFS_NAME = "prefs";
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(SHARED_DATE)) {
            Log.d("SetNotificationActivity", "SharedPrefs contain SHARED_DATA");

        } else Log.d("SetNotificationActivity", "Shared prefs does not containg SHARED_DATA");
    }
}
