package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DisplayDataActivity extends AppCompatActivity {


    public static final String BACK_BUTTON = "onbackpressed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

    }

    @Override
    public void onBackPressed() {

        Log.d("Activity B", "         OnBackPressed");
        Intent i = new Intent(DisplayDataActivity.this, MainActivity.class);
        i.putExtra("onbackpressed", true);
        startActivity(i);
        super.onBackPressed();
    }


}
