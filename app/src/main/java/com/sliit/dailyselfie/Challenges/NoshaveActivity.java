package com.sliit.dailyselfie.Challenges;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.vi.swipenumberpicker.SwipeNumberPicker;

public class NoshaveActivity extends AppCompatActivity {

    private EditText noShavename,noShaveDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noshave);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.noShavesetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NoshaveActivity.this);
                mBuilder.setSmallIcon(R.drawable.ic_noti_dailyselfie);
                mBuilder.setContentTitle("DailySelfie");
                mBuilder.setContentText("Time to take a Selfie!");
                mBuilder.setSound(alarmSound);
                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                Intent resultIntent = new Intent(NoshaveActivity.this, CameraActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(NoshaveActivity.this);
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

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_noShaveName);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_noShavedescription);

        noShavename = (EditText)findViewById(R.id.noShaveName);
        noShaveDescription = (EditText)findViewById(R.id.noShavedescription);
        btnAdd = (Button) findViewById(R.id.noShave_submit_btn);

        noShavename.addTextChangedListener(new MyTextWatcher(noShavename));
        //fitDescription.addTextChangedListener(new MyTextWatcher(fitDescription));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {

                    String noshaveChallangename = noShavename.getText().toString();
                    String noshavedescription = noShaveDescription.getText().toString();

                    DBHelper helper = new DBHelper(NoshaveActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("type", "NoShave");
                    values.put("name", noshaveChallangename);
                    values.put("description", noshavedescription);

                    try {
                        db.insert("challanges", null, values);

                        successfulAlert();

                    } catch (SQLiteException e) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(NoshaveActivity.this);
                        a_builder.setMessage("User already exist!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        noShavename.setText("");
                                        noShaveDescription.setText("");
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
        AlertDialog.Builder a_builder = new AlertDialog.Builder(NoshaveActivity.this);
        a_builder.setMessage("Successfully created No Shave challange")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noShavename.setText("");
                        noShaveDescription.setText("");
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("Challenge", "noshave"));
                        dialog.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Congratulation!");
        alert.show();
    }

    private boolean submitForm() {
        if (!validateNSName()) {
            return false;
        }

        return true;
    }

    private boolean validateNSName() {
        if (noShavename.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter valid Challange name");
            requestFocus(noShavename);
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
                case R.id.noShaveName:
                    validateNSName();
                    break;

            }
        }
    }

}
