package com.android.keehoo.thetotallynewinsurancereminder;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

import butterknife.Bind;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DisplayDataActivity extends AppCompatActivity {


    private long dataUbezpieczenieWMilisekundach;
    private long dataTechnicalWMilisekundach;
    public SharedPreferences sharedPreferences;
    private TextView finalDisplayDays;
    private TextView finalTechnicalDisplayDays;
    private Button newInsuranceDate;
    private Button newTechnicalDate;
    private RadioGroup radiogroup;
    private RadioGroup radiogroupTech;
    private SeekBar seekBarTech;
    private SeekBar seekBarInsurance;


    private int okresUbezpieczeniaWmiesiacach;
    private int okresPrzegladuTechWmiesiacach;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        JodaTimeAndroid.init(this);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        seekBarInsurance = (SeekBar)findViewById(R.id.seekBar);
        seekBarTech = (SeekBar)findViewById(R.id.seekBar2);
        radiogroup = (RadioGroup) findViewById(R.id.radio_group_insurance_id);
        radiogroupTech = (RadioGroup) findViewById(R.id.radio_group_technical_id);
        finalDisplayDays = (TextView) findViewById(R.id.days_left_display_id);
        finalTechnicalDisplayDays = (TextView) findViewById(R.id.days_left_display_technical_id);
        newInsuranceDate = (Button) findViewById(R.id.new_insurance_date_id);
        newInsuranceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromSharedPreferences(MainActivity.SHARED_DATE);
                Log.d("DisplayData", "Usunieto date ubezpieczenia");
                finalDisplayDays.setText("N/A");
                if (!sharedPreferences.contains(MainActivity.SHARED_DATE)) {
                    Intent intent = new Intent(DisplayDataActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.TECHNICAL_BUTTON_ENABLED, false);
                    startActivity(intent);
                } else {
                    Log.d("Display", "Wyglada na to, ze nie usunelo sie SHARED_DATE");
                }
            }
        });


        newTechnicalDate = (Button) findViewById(R.id.new_technical_date_id);
        newTechnicalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromSharedPreferences(MainActivity.SHARED_DATE_TECHNICAL);
                Log.d("DisplayDateAct", " Usunieto Date przegladu technicznego");
                finalTechnicalDisplayDays.setText("N/A");
                if (sharedPreferences.contains(MainActivity.SHARED_DATE_TECHNICAL)) {
                    Log.d("Display", " Wyglada na to, ze shared date sie nie ususnel z kluczaa SHARED_DATE_TECHNICAL");
                } else {
                    Intent intent = new Intent(DisplayDataActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.INSURANCE_BUTTON_ENABLED, false);
                    startActivity(intent);
                }
            }
        });




        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.twelve_months_insurance_id) {
                    setOkresUbezpieczeniaWmiesiacach(12);
                    setInsuranceDisplay(okresUbezpieczeniaWmiesiacach);
                }
                if (checkedId == R.id.six_months_insurance_id) {
                    setOkresUbezpieczeniaWmiesiacach(6);
                    setInsuranceDisplay(okresUbezpieczeniaWmiesiacach);
                }

                if (checkedId == R.id.choose_months_insurance_id)
                    setOkresUbezpieczeniaWmiesiacach(1);
            }
        });

        radiogroupTech.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twelve_months_id) {
                    setOkresPrzegladuTechWmiesiacach(12);
                }

                if (checkedId == R.id.six_months_id) {setOkresPrzegladuTechWmiesiacach(6);

                }
                if (checkedId == R.id.choose_months_id) {

                }
            }
        });

        seekBarInsurance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setOkresUbezpieczeniaWmiesiacach(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                radiogroup.check(R.id.choose_months_insurance_id);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setInsuranceDisplay(okresUbezpieczeniaWmiesiacach);

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("DisplayData", "OnBackPressed");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public String daysBetween(DateTime dateTime, int numberOfMonths) {
        /**
         * Zawraca stringa (liczbe dni pomiedzy) parametry: DateTime & ile miesiecy od DateTime
         */
        DateTime now = new DateTime();
        DateTime dataZaIlesTamMiesiecy = dateTime.plusMonths(numberOfMonths);
        return String.valueOf(Days.daysBetween(now.withTimeAtStartOfDay(), dataZaIlesTamMiesiecy.withTimeAtStartOfDay()).getDays());
    }

    public void removeFromSharedPreferences(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
    }

    public void setOkresUbezpieczeniaWmiesiacach(int okresUbezpieczeniaWmiesiacach) {
        this.okresUbezpieczeniaWmiesiacach = okresUbezpieczeniaWmiesiacach;
    }

    public void setOkresPrzegladuTechWmiesiacach(int okresPrzegladuTechWmiesiacach) {
        this.okresPrzegladuTechWmiesiacach = okresPrzegladuTechWmiesiacach;
    }

    public void setInsuranceDisplay(int okresUbezpieczeniaWmiesiacach) {
        dataUbezpieczenieWMilisekundach = (sharedPreferences.getLong(MainActivity.SHARED_DATE, -1));
        finalDisplayDays.setText(daysBetween(new DateTime(dataUbezpieczenieWMilisekundach), okresUbezpieczeniaWmiesiacach));

    }

    public void setTechnicaDisplay(int okresPrzegladuTechWmiesiacach) {
        dataTechnicalWMilisekundach = (sharedPreferences.getLong(MainActivity.SHARED_DATE_TECHNICAL, -1));
        finalTechnicalDisplayDays.setText(daysBetween(new DateTime(dataTechnicalWMilisekundach), okresPrzegladuTechWmiesiacach));
    }

}
