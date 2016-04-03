package com.sliit.dailyselfie;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

public class FitnessActivity extends BaseActivity {

    SwipeNumberPicker fitpicker;
    SwipeNumberPicker fitpicker1;
    SwipeNumberPicker fitpicker2;
    SwipeNumberPicker fitpicker3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);





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
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
            }
        });


        fitpicker = (SwipeNumberPicker)findViewById(R.id.snp1);
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

        fitpicker3 = (SwipeNumberPicker)findViewById(R.id.snp4);
        fitpicker3.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });
    }



}
