package com.sliit.dailyselfie.Challenges;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.sliit.dailyselfie.AlertReciver.AlarmReciver;
import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.util.Date;
import java.util.GregorianCalendar;

import picker.ugurtekbas.com.Picker.Picker;

public class FitnessActivity extends AppCompatActivity {

    Spinner spn;
    String fitType;
    Dialog d;
    Picker picker;
    Button bset,bcancle;

    private EditText fitname,fitDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;

    private RadioGroup ftype;
    private SwipeNumberPicker fheight,fweight,ftarweight;
    String userID;
    String hour,minutes;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.fitsetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new Dialog(FitnessActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.alarmmodel);
                bset = (Button) d.findViewById(R.id.BsetAlarm);
                bcancle = (Button) d.findViewById(R.id.BcancleAlarm);
                picker = (Picker) d.findViewById(R.id.amPicker);
                picker.setClockColor(Color.parseColor("#2196F3"));
                picker.setDialColor(Color.parseColor("#FF9800"));
                picker.getCurrentHour();
                picker.getCurrentMin();
                picker.getTime();
                d.show();

                bset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        date=picker.getTime();
                        hour=Integer.toString(picker.getCurrentHour());
                        minutes=Integer.toString(picker.getCurrentMin());
                        Toast.makeText(getApplicationContext(),"Alarm set to"+ Integer.toString(picker.getCurrentHour())+" "+Integer.toString(picker.getCurrentMin()), Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });

                bcancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), picker.getTime().toString(), Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });

            }
        });

        findViewById(R.id.fitcansel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitname.setText("");
                fitDescription.setText("");
            }
        });

        SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userID = userDetails.getString("loggedUserId","");

        fheight = (SwipeNumberPicker)findViewById(R.id.snpfit0);
        fheight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        fweight = (SwipeNumberPicker)findViewById(R.id.snpfit2);
        fweight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        ftarweight = (SwipeNumberPicker)findViewById(R.id.snpfit3);
        ftarweight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        spn = (Spinner)findViewById(R.id.fitdate);
        final String [] challengePeriodType = getResources().getStringArray(R.array.challengePeriodType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,challengePeriodType);
        spn.setAdapter(adapter);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_fitName);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_fitDescription);

        fitname = (EditText)findViewById(R.id.fitChalName);
        fitDescription = (EditText)findViewById(R.id.fitDescription);
        btnAdd = (Button) findViewById(R.id.fit_submit_btn);
        ftype = (RadioGroup)findViewById(R.id.fitnessradioGroup);
        fheight = (SwipeNumberPicker)findViewById(R.id.snpfit0);
        fweight = (SwipeNumberPicker)findViewById(R.id.snpfit2);
        ftarweight = (SwipeNumberPicker)findViewById(R.id.snpfit3);

        fitname.addTextChangedListener(new MyTextWatcher(fitname));
        //fitDescription.addTextChangedListener(new MyTextWatcher(fitDescription));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {

                    //startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("Challenge","fitness"));

                    if (ftype.getCheckedRadioButtonId() != -1) {
                        int id = ftype.getCheckedRadioButtonId();
                        View radioButton = ftype.findViewById(id);
                        int radioId = ftype.indexOfChild(radioButton);
                        RadioButton btn = (RadioButton) ftype.getChildAt(radioId);
                        fitType = (String) btn.getText();
                    }

                    String fitChallangename = fitname.getText().toString();
                    Double fitheight = Double.parseDouble((String) fheight.getText());
                    Double fitweight = Double.parseDouble((String) fweight.getText());
                    Double fittarweight = Double.parseDouble((String) ftarweight.getText());
                    String fitdescription = fitDescription.getText().toString();
                    String type = "Fitness";
                    int userId = Integer.parseInt(userID);

                    DBHelper helper = new DBHelper(FitnessActivity.this);
                    String sql = "INSERT INTO challanges (type,fitCategory,name,height,weight,targetWeight,description,userId)" +
                            " VALUES ('"+type+"','"+fitType+"','"+fitChallangename+"','"+fitheight+"','"+fitweight+"','"+fittarweight+"','"+fitdescription+"','"+userId+"') ";

                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        db.execSQL(sql);
                        successfulAlert();

                        SharedPreferences cDetails = getSharedPreferences("cDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = cDetails.edit();
                        editor1.putString("chName", fitChallangename);
                        editor1.apply();

                    } catch (SQLiteException e) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(FitnessActivity.this);
                        a_builder.setMessage("User already exist!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fitname.setText("");
                                        fitDescription.setText("");
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Alert");
                        alert.show();
                    }

                    //Toast.makeText(this, "Registed !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void successfulAlert(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(FitnessActivity.this);
        a_builder.setMessage("Successfully created Fitness challange")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fitname.setText("");
                        fitDescription.setText("");
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("Challenge", "fitness"));
                        dialog.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Congratulation!");
        alert.show();
    }

    private boolean submitForm() {
        if (!validateFName()) {
            return false;
        }

        //Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean validateFName() {
        if (fitname.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter the Challange name");
            requestFocus(fitname);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.fitChalName:
                    validateFName();
                    break;

            }
        }
    }


    public void SetAlarmBroadcast(){

        Long alertTime =new GregorianCalendar().getTimeInMillis();
        Intent alertIntent = new Intent(FitnessActivity.this,AlarmReciver.class).putExtra("Category","Fitness");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,PendingIntent.getBroadcast(FitnessActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alertTime,alarmManager.INTERVAL_DAY*7,PendingIntent.getBroadcast(FitnessActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));


    }
}
