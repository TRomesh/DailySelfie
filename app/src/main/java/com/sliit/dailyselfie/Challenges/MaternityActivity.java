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

public class MaternityActivity extends AppCompatActivity {

    SwipeNumberPicker fitpicker;
    SwipeNumberPicker fitpicker1;
    SwipeNumberPicker fitpicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maternity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.setAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MaternityActivity.this);
                mBuilder.setSmallIcon(R.drawable.ic_noti_dailyselfie);
                mBuilder.setContentTitle("DailySelfie");
                mBuilder.setContentText("Time to take a Selfie!");
                mBuilder.setSound(alarmSound);
                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                Intent resultIntent = new Intent(MaternityActivity.this, CameraActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MaternityActivity.this);
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

                EditText mcname = (EditText)findViewById(R.id.maternityname);
                SwipeNumberPicker mheight = (SwipeNumberPicker)findViewById(R.id.snp0);
                SwipeNumberPicker mweight = (SwipeNumberPicker)findViewById(R.id.snp2);
                SwipeNumberPicker mwaist = (SwipeNumberPicker)findViewById(R.id.snp3);
                EditText mdescription = (EditText)findViewById(R.id.description);

                String materChallangename = mcname.getText().toString();
                Double materheight = Double.parseDouble((String) mheight.getText());
                Double materweight = Double.parseDouble((String) mweight.getText());
                Double materwaist = Double.parseDouble((String) mwaist.getText());
                String description = mdescription.getText().toString();

                DBHelper helper = new DBHelper(MaternityActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("type", "Maternity");
                values.put("name", materChallangename);
                values.put("height",materheight);
                values.put("weight", materweight);
                values.put("waistSize", materwaist);
                values.put("description", description);

                db.insert("challanges", null, values);

                mcname.setText("");
                mdescription.setText("");

                Toast.makeText(MaternityActivity.this, "Saved !", Toast.LENGTH_LONG).show();

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
