package com.sliit.dailyselfie.Challenges;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sliit.dailyselfie.AlertReciver.AlarmReciver;
import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.util.GregorianCalendar;

import picker.ugurtekbas.com.Picker.Picker;

public class MaternityActivity extends AppCompatActivity {

    private EditText maternityname,maternityDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;
    private SwipeNumberPicker maternityheight,maternityweight,maternityWaist;
    Button bset,bcancle;
    Dialog d;
    Picker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maternity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.maternitysetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new Dialog(MaternityActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.alarmmodel);
                bset=(Button)d.findViewById(R.id.BsetAlarm);
                bcancle=(Button)d.findViewById(R.id.BcancleAlarm);
                picker = (Picker) d.findViewById(R.id.amPicker);
                picker.setClockColor(Color.parseColor("#2196F3"));
                picker.setDialColor(Color.parseColor("#FF9800"));
                picker.getCurrentHour();
                picker.getCurrentMin();
                d.show();

                bset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Set Alarm",Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });

                bcancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"cancle Alarm",Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });

     }
        });

        maternityheight = (SwipeNumberPicker)findViewById(R.id.snpmaternity0);
        maternityheight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        maternityweight = (SwipeNumberPicker)findViewById(R.id.snpmaternity2);
        maternityweight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        maternityWaist = (SwipeNumberPicker)findViewById(R.id.snpmaternity3);
        maternityWaist.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_maternityName);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_mdescription);

        maternityname = (EditText)findViewById(R.id.maternityname);
        maternityDescription = (EditText)findViewById(R.id.mdescription);
        btnAdd = (Button) findViewById(R.id.maternity_submit_btn);
        maternityheight = (SwipeNumberPicker)findViewById(R.id.snpmaternity0);
        maternityweight = (SwipeNumberPicker)findViewById(R.id.snpmaternity2);
        maternityWaist = (SwipeNumberPicker)findViewById(R.id.snpmaternity3);

        maternityname.addTextChangedListener(new MyTextWatcher(maternityname));
        //fitDescription.addTextChangedListener(new MyTextWatcher(fitDescription));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {

                    String maternityChallangename = maternityname.getText().toString();
                    Double maternityHeight = Double.parseDouble((String) maternityheight.getText());
                    Double maternityWeight = Double.parseDouble((String) maternityweight.getText());
                    Double maternitywaist = Double.parseDouble((String) maternityWaist.getText());
                    String maternitydescription = maternityDescription.getText().toString();
                    String type = "Maternity";
                    int userId = 1;

                    DBHelper helper = new DBHelper(MaternityActivity.this);
                    String sql = "INSERT INTO challanges (type,name,height,weight,waistSize,description,userId)" +
                            " VALUES ('"+type+"','"+maternityChallangename+"','"+maternityHeight+"','"+maternityWeight+"','"+maternitywaist+"','"+maternitydescription+"','"+userId+"') ";

                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        db.execSQL(sql);
                        successfulAlert();

                    } catch (SQLiteException e) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(MaternityActivity.this);
                        a_builder.setMessage("User already exist!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        maternityname.setText("");
                                        maternityDescription.setText("");
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
        AlertDialog.Builder a_builder = new AlertDialog.Builder(MaternityActivity.this);
        a_builder.setMessage("Successfully created Maternity challange")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        maternityname.setText("");
                        maternityDescription.setText("");
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("Challenge", "maternity"));
                        dialog.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Congratulation!");
        alert.show();
    }

    private boolean submitForm() {
        if (!validateMName()) {
            return false;
        }

        return true;
    }

    private boolean validateMName() {
        if (maternityname.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter the Challange name");
            requestFocus(maternityname);
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
                case R.id.maternityname:
                    validateMName();
                    break;

            }
        }
    }

    public void SetAlarmBroadcast(){

        Long alertTime =new GregorianCalendar().getTimeInMillis();
        Intent alertIntent = new Intent(MaternityActivity.this,AlarmReciver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,PendingIntent.getBroadcast(MaternityActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alertTime,alarmManager.INTERVAL_DAY*7,PendingIntent.getBroadcast(MaternityActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));

    }

}
