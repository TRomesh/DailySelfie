package com.sliit.dailyselfie.Challenges;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

public class FitnessActivity extends AppCompatActivity {

    SwipeNumberPicker fitpicker;
    SwipeNumberPicker fitpicker1;
    SwipeNumberPicker fitpicker2;
    String fitType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.setAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(FitnessActivity.this);
                mBuilder.setSmallIcon(R.drawable.ic_noti_dailyselfie);
                mBuilder.setContentTitle("DailySelfie");
                mBuilder.setContentText("Time to take a Selfie!");
                mBuilder.setSound(alarmSound);
                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                Intent resultIntent = new Intent(FitnessActivity.this, CameraActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(FitnessActivity.this);
                stackBuilder.addParentStack(CameraActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1001, mBuilder.build());
            }
        });



        findViewById(R.id.fit_submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioGroup ftype = (RadioGroup)findViewById(R.id.fitnessradioGroup);
                EditText fcname = (EditText)findViewById(R.id.fitChalName);
                SwipeNumberPicker fheight = (SwipeNumberPicker)findViewById(R.id.snp0);
                SwipeNumberPicker fweight = (SwipeNumberPicker)findViewById(R.id.snp2);
                SwipeNumberPicker ftarweight = (SwipeNumberPicker)findViewById(R.id.snp3);
                EditText fdescription = (EditText)findViewById(R.id.fitDescription);

                if(ftype.getCheckedRadioButtonId()!=-1){
                    int id= ftype.getCheckedRadioButtonId();
                    View radioButton = ftype.findViewById(id);
                    int radioId = ftype.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) ftype.getChildAt(radioId);
                    fitType = (String) btn.getText();
                }

                String fitChallangename = fcname.getText().toString();
                Double fitheight = Double.parseDouble((String) fheight.getText());
                Double fitweight = Double.parseDouble((String) fweight.getText());
                Double fittarweight = Double.parseDouble((String) ftarweight.getText());
                String description = fdescription.getText().toString();

                DBHelper helper = new DBHelper(FitnessActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("type", "Fitness");
                values.put("fitCategory", fitType);
                values.put("name", fitChallangename);
                values.put("height",fitheight);
                values.put("weight", fitweight);
                values.put("targetWeight", fittarweight);
                values.put("description", description);

                db.insert("challanges", null, values);

                fcname.setText("");
                fdescription.setText("");

                Toast.makeText(FitnessActivity.this, "Saved !", Toast.LENGTH_LONG).show();

                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
            }
        });


        fitpicker = (SwipeNumberPicker)findViewById(R.id.snp0);
        fitpicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        fitpicker1 = (SwipeNumberPicker)findViewById(R.id.snp2);
        fitpicker1.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        fitpicker2 = (SwipeNumberPicker)findViewById(R.id.snp3);
        fitpicker2.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });



    }

}
