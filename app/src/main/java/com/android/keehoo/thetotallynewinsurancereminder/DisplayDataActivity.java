package com.android.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Days;

import cn.iwgang.countdownview.CountdownView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DisplayDataActivity extends AppCompatActivity {


    public static final String ILOSC_DNI = "ilosc dni do konca ubezpieczenia";
    public static final String ILOSC_DNI_TECHNICAL = "ilosc dni do konca przegladu technicznego";

    private long dataUbezpieczenieWMilisekundach;
    private long dataZakonczeniaUbezpieczeniaWMilisekundach;
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
    private TextView durationDisplay;
    private Button hide;
    private Button setNotification;
    private boolean visible = true;
    public CountdownView insCountDown;
    private DateTime dataZakonczeniaUbezpieczenia;

    private int okresUbezpieczeniaWmiesiacach;  //ilosc miesiecy
    private int okresPrzegladuTechWmiesiacach;  //ilosc miesiecy




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        JodaTimeAndroid.init(this);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        initiateVariables(); /*

        initiateVariables method adds all the view from the layout XML file and adds code for the listeners for buttons and seekbars.

        */


        if (sharedPreferences.contains(MainActivity.SHARED_DATE_DURATION_INS)) {
            setInsuranceDisplay(sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_INS, 12));
            durationDisplay.setText("Obecny czas trawania ubezpieczenia to " + sharedPreferences.getInt(MainActivity
                    .SHARED_DATE_DURATION_INS, 12));
            insuranceCounterReStart();
            durationDisplay.setTextColor(getResources().getColor(R.color.czerwony));
            durationDisplay.setText("data zakoczenia ubezp "+ dataZakonczeniaUbezpieczenia);

            /**
             * TODO: add the proper amount of miliseconds to the count down....
             */
        } else {
            Toast.makeText(DisplayDataActivity.this, "Nie ma ustawionej dlugosci ubezpieczenia!!!!", Toast.LENGTH_SHORT).show();
        }

        if (sharedPreferences.contains(MainActivity.SHARED_DATE_DURATION_TECH)) {
            setTechnicaDisplay(sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_TECH, 12));
        } else {
            Toast.makeText(DisplayDataActivity.this, "Nie ma podanej daty zakonczenia trwania przegladu technicznego", Toast.LENGTH_SHORT).show();
        }
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

    public void insuranceCounterReStart() {
        dataZakonczeniaUbezpieczenia = new DateTime(dataUbezpieczenieWMilisekundach).plusDays(Integer.parseInt(finalDisplayDays.getText().toString()));
        insCountDown.start(dataZakonczeniaUbezpieczenia.getMillis() - dataUbezpieczenieWMilisekundach);
    }

    public void hide() {


        LinearLayout linearRadio = (LinearLayout) findViewById(R.id.linear_layout_for_radio_group_id);
        LinearLayout linearSeekBar = (LinearLayout) findViewById(R.id.seek_bar_linear_layout_id);

        if (this.visible == false) {

            durationDisplay.setVisibility(View.INVISIBLE);
            linearRadio.setVisibility(View.INVISIBLE);
            linearSeekBar.setVisibility(View.INVISIBLE);
            this.visible = true;

        } else {
            durationDisplay.setVisibility(View.VISIBLE);
            linearRadio.setVisibility(View.VISIBLE);
            linearSeekBar.setVisibility(View.VISIBLE);
            this.visible = false;
        }

    }

    public void setTechnicaDisplay(int okresPrzegladuTechWmiesiacach) {
        dataTechnicalWMilisekundach = (sharedPreferences.getLong(MainActivity.SHARED_DATE_TECHNICAL, -1));
        finalTechnicalDisplayDays.setText(daysBetween(new DateTime(dataTechnicalWMilisekundach), okresPrzegladuTechWmiesiacach));
    }

    public void initiateVariables() {
        insCountDown = (CountdownView) findViewById(R.id.count_down_ins_id);
        seekBarInsurance = (SeekBar) findViewById(R.id.seekBar);
        seekBarTech = (SeekBar) findViewById(R.id.seekBar2);
        radiogroup = (RadioGroup) findViewById(R.id.radio_group_insurance_id);
        radiogroupTech = (RadioGroup) findViewById(R.id.radio_group_technical_id);
        finalDisplayDays = (TextView) findViewById(R.id.days_left_display_id);
        finalTechnicalDisplayDays = (TextView) findViewById(R.id.days_left_display_technical_id);
        newInsuranceDate = (Button) findViewById(R.id.new_insurance_date_id);
        setNotification = (Button) findViewById(R.id.set_notification_id);
        durationDisplay = (TextView) findViewById(R.id.duration_display_id);
        hide = (Button) findViewById(R.id.hide_id);
        final int givenRadioIns = radiogroup.getCheckedRadioButtonId();
        int givenRadioTech = radiogroupTech.getCheckedRadioButtonId();

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        newInsuranceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromSharedPreferences(MainActivity.SHARED_DATE);
                Log.d("DisplayData", "Usunieto date ubezpieczenia");
                finalDisplayDays.setText("0");
                if (!sharedPreferences.contains(MainActivity.SHARED_DATE)) {
                    Intent intent = new Intent(DisplayDataActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.TECHNICAL_BUTTON_ENABLED, false);
                    startActivity(intent);
                    finish();
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
                finalTechnicalDisplayDays.setText("0");
                if (sharedPreferences.contains(MainActivity.SHARED_DATE_TECHNICAL)) {
                    Log.d("Display", " Wyglada na to, ze shared date sie nie ususnel z kluczaa SHARED_DATE_TECHNICAL");
                } else {
                    Intent intent = new Intent(DisplayDataActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.INSURANCE_BUTTON_ENABLED, false);
                    startActivity(intent);
                    finish();
                }
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.twelve_months_insurance_id) {
                    setOkresUbezpieczeniaWmiesiacach(12);
                    setInsuranceDisplay(okresUbezpieczeniaWmiesiacach);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(MainActivity.SHARED_DATE_DURATION_INS, 12).apply();
                    dataZakonczeniaUbezpieczeniaWMilisekundach = new DateTime(dataUbezpieczenieWMilisekundach).plusMonths(12).getMillis();
                    editor.putLong(MainActivity.SHARED_DATE_INS_MILLIS_DURATION, dataZakonczeniaUbezpieczeniaWMilisekundach).apply();
                    durationDisplay.setText("Obecny czas trawania ubezpieczenia to " + sharedPreferences.getInt(MainActivity.
                            SHARED_DATE_DURATION_INS, 12));
                    insuranceCounterReStart();

                }
                if (checkedId == R.id.six_months_insurance_id) {
                    setOkresUbezpieczeniaWmiesiacach(6);
                    setInsuranceDisplay(okresUbezpieczeniaWmiesiacach);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(MainActivity.SHARED_DATE_DURATION_INS, 6).apply();
                    dataZakonczeniaUbezpieczeniaWMilisekundach = new DateTime(dataUbezpieczenieWMilisekundach).plusMonths(6).getMillis();
                    editor.putLong(MainActivity.SHARED_DATE_INS_MILLIS_DURATION, dataZakonczeniaUbezpieczeniaWMilisekundach).apply();
                    durationDisplay.setText("Obecny czas trawania ubezpieczenia to " + sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_INS, 12));
                    insuranceCounterReStart();
                }

                if (checkedId == R.id.choose_months_insurance_id)
                    setOkresUbezpieczeniaWmiesiacach(givenRadioIns);
            }
        });

        radiogroupTech.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twelve_months_id) {
                    setOkresPrzegladuTechWmiesiacach(12);
                    setTechnicaDisplay(okresPrzegladuTechWmiesiacach);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(MainActivity.SHARED_DATE_DURATION_TECH, 12).apply();
                    durationDisplay.setText("Obecny czas trawania przegladu " + sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_TECH, 12));
                }

                if (checkedId == R.id.six_months_id) {
                    setOkresPrzegladuTechWmiesiacach(6);
                    setTechnicaDisplay(okresPrzegladuTechWmiesiacach);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(MainActivity.SHARED_DATE_DURATION_TECH, 6).apply();
                    durationDisplay.setText("Obecny czas trwania przegladu technicznego to " + sharedPreferences.getInt(MainActivity.SHARED_DATE_DURATION_TECH, 12));

                }
                if (checkedId == R.id.choose_months_id) {

                }
            }
        });

        seekBarInsurance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setOkresUbezpieczeniaWmiesiacach(progress);
                durationDisplay.setText("Duration of the insurance: " + progress);
                sharedPreferences.edit().putInt(MainActivity.SHARED_DATE_DURATION_INS, progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                radiogroup.check(R.id.choose_months_insurance_id);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setInsuranceDisplay(okresUbezpieczeniaWmiesiacach);
                insuranceCounterReStart();


            }
        });

        seekBarTech.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setOkresPrzegladuTechWmiesiacach(progress);
                durationDisplay.setText("Duration of technical check: " + progress);
                sharedPreferences.edit().putInt(MainActivity.SHARED_DATE_DURATION_TECH, progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                radiogroupTech.check(R.id.choose_months_id);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setInsuranceDisplay(okresPrzegladuTechWmiesiacach);
            }
        });

        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayDataActivity.this, SetNotificationActivity.class);
                intent.putExtra(ILOSC_DNI, finalDisplayDays.getText().toString());
                intent.putExtra(ILOSC_DNI_TECHNICAL, finalTechnicalDisplayDays.getText().toString());
                startActivity(intent);

                //daysBetween(new DateTime(dataUbezpieczenieWMilisekundach), okresUbezpieczeniaWmiesiacach
            }
        });


    }

}
