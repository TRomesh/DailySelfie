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

public class PostMaternityActivity extends AppCompatActivity {

    private EditText pmaternityname,pmaternityDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;
    private SwipeNumberPicker pmaternityheight,pmaternityweight,pmaternityWaist,pmaternityTargetWaist;
    String userID;
    Button bset,bcancle;
    Dialog d;
    Picker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_maternity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.postMsetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new Dialog(PostMaternityActivity.this);
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

        SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userID = userDetails.getString("loggedUserId","");

        pmaternityheight = (SwipeNumberPicker)findViewById(R.id.snppostM0);
        pmaternityheight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        pmaternityweight = (SwipeNumberPicker)findViewById(R.id.snppostM2);
        pmaternityweight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        pmaternityWaist = (SwipeNumberPicker)findViewById(R.id.snppostM3);
        pmaternityWaist.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        pmaternityTargetWaist = (SwipeNumberPicker)findViewById(R.id.snppostM4);
        pmaternityTargetWaist.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_postMName);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_postMdescription);

        pmaternityname = (EditText)findViewById(R.id.postMName);
        pmaternityDescription = (EditText)findViewById(R.id.postMdescription);
        btnAdd = (Button) findViewById(R.id.postM_submit_btn);
        pmaternityheight = (SwipeNumberPicker)findViewById(R.id.snppostM0);
        pmaternityweight = (SwipeNumberPicker)findViewById(R.id.snppostM2);
        pmaternityWaist = (SwipeNumberPicker)findViewById(R.id.snppostM3);
        pmaternityTargetWaist = (SwipeNumberPicker)findViewById(R.id.snppostM4);

        pmaternityname.addTextChangedListener(new MyTextWatcher(pmaternityname));
        //fitDescription.addTextChangedListener(new MyTextWatcher(fitDescription));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {

                    String pmaternityChallangename = pmaternityname.getText().toString();
                    Double pmaternityHeight = Double.parseDouble((String) pmaternityheight.getText());
                    Double pmaternityWeight = Double.parseDouble((String) pmaternityweight.getText());
                    Double pmaternitywaist = Double.parseDouble((String) pmaternityWaist.getText());
                    Double pmaternitytargetwaist = Double.parseDouble((String) pmaternityTargetWaist.getText());
                    String pmaternitydescription = pmaternityDescription.getText().toString();
                    String type = "PostMaternity";
                    int userId = Integer.parseInt(userID);

                    DBHelper helper = new DBHelper(PostMaternityActivity.this);
                    String sql = "INSERT INTO challanges (type,name,height,weight,waistSize,targetWaistSize,description,userId)" +
                            " VALUES ('"+type+"','"+pmaternityChallangename+"','"+pmaternityHeight+"','"+pmaternityWeight+"','"+pmaternitywaist+"','"+pmaternitytargetwaist+"','"+pmaternitydescription+"','"+userId+"') ";

                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        db.execSQL(sql);
                        successfulAlert();

                    } catch (SQLiteException e) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(PostMaternityActivity.this);
                        a_builder.setMessage("User already exist!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pmaternityname.setText("");
                                        pmaternityDescription.setText("");
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
        AlertDialog.Builder a_builder = new AlertDialog.Builder(PostMaternityActivity.this);
        a_builder.setMessage("Successfully created Post Maternity challange")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pmaternityname.setText("");
                        pmaternityDescription.setText("");
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("Challenge", "postmaternity"));
                        dialog.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Congratulation!");
        alert.show();
    }

    private boolean submitForm() {
        if (!validatePMName()) {
            return false;
        }

        return true;
    }

    private boolean validatePMName() {
        if (pmaternityname.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter the Challange name");
            requestFocus(pmaternityname);
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
                case R.id.postMName:
                    validatePMName();
                    break;

            }
        }
    }

    public void SetAlarmBroadcast(){

        Long alertTime =new GregorianCalendar().getTimeInMillis();
        Intent alertIntent = new Intent(PostMaternityActivity.this,AlarmReciver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,PendingIntent.getBroadcast(PostMaternityActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alertTime,alarmManager.INTERVAL_DAY*7,PendingIntent.getBroadcast(PostMaternityActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));

    }

}
