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
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

public class PostMaternityActivity extends AppCompatActivity {

    private EditText pmaternityname,pmaternityDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;
    private SwipeNumberPicker pmaternityheight,pmaternityweight,pmaternityWaist,pmaternityTargetWaist;

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

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(PostMaternityActivity.this);
                mBuilder.setSmallIcon(R.drawable.ic_noti_dailyselfie);
                mBuilder.setContentTitle("DailySelfie");
                mBuilder.setContentText("Time to take a Selfie!");
                mBuilder.setSound(alarmSound);
                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                Intent resultIntent = new Intent(PostMaternityActivity.this, CameraActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(PostMaternityActivity.this);
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

                    DBHelper helper = new DBHelper(PostMaternityActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("type", "PostMaternity");
                    values.put("name", pmaternityChallangename);
                    values.put("height", pmaternityHeight);
                    values.put("weight", pmaternityWeight);
                    values.put("waistSize", pmaternitywaist);
                    values.put("targetWaistSize",pmaternitytargetwaist);
                    values.put("description", pmaternitydescription);

                    try {
                        db.insert("challanges", null, values);

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
            inputLayoutName.setError("Enter valid Challange name");
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

}
