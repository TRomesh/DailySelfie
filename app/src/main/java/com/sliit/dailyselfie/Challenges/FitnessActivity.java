package com.sliit.dailyselfie.Challenges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.sql.SQLIntegrityConstraintViolationException;

import picker.ugurtekbas.com.Picker.Picker;

public class FitnessActivity extends AppCompatActivity {

    String fitType;
    Dialog d;
    Picker picker;

    private EditText fitname,fitDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;

    private RadioGroup ftype;
    private SwipeNumberPicker fheight,fweight,ftarweight;
    String userID;

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
                picker = (Picker) d.findViewById(R.id.amPicker);
                picker.setClockColor(Color.parseColor("#2196F3"));
                picker.setDialColor(Color.parseColor("#FF9800"));
                picker.getCurrentHour();
                picker.getCurrentMin();


                d.show();


//                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(FitnessActivity.this);
//                mBuilder.setSmallIcon(R.drawable.ic_noti_dailyselfie);
//                mBuilder.setContentTitle("DailySelfie");
//                mBuilder.setContentText("Time to take a Selfie!");
//                mBuilder.setSound(alarmSound);
//                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
//                Intent resultIntent = new Intent(FitnessActivity.this, CameraActivity.class);
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(FitnessActivity.this);
//                stackBuilder.addParentStack(CameraActivity.class);
//
//                stackBuilder.addNextIntent(resultIntent);
//                PendingIntent resultPendingIntent =
//                        stackBuilder.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                mBuilder.setContentIntent(resultPendingIntent);
//                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                mNotificationManager.notify(1001, mBuilder.build());
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

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
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
}
