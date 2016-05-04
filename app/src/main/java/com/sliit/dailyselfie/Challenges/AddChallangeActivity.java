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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.Community.RegisterUser;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;

public class AddChallangeActivity extends AppCompatActivity {

    Spinner spn;
    private EditText cname,cDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.cusChallangesetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AddChallangeActivity.this);
                mBuilder.setSmallIcon(R.drawable.ic_noti_dailyselfie);
                mBuilder.setContentTitle("DailySelfie");
                mBuilder.setContentText("Time to take a Selfie!");
                mBuilder.setSound(alarmSound);
                mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                Intent resultIntent = new Intent(AddChallangeActivity.this, CameraActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(AddChallangeActivity.this);
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

        spn = (Spinner)findViewById(R.id.cusChallangespinner);
        final String [] challenges = getResources().getStringArray(R.array.challenges);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,challenges);
        spn.setAdapter(adapter);


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_cusChallangeName);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_cusChallangedescription);

        cname = (EditText)findViewById(R.id.cusChallangeName);
        cDescription = (EditText)findViewById(R.id.cusChallangedescription);
        btnAdd = (Button) findViewById(R.id.cusChallange_submit_btn);

        cname.addTextChangedListener(new MyTextWatcher(cname));
        //cDescription.addTextChangedListener(new MyTextWatcher(cDescription));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {

                    String Challangename = cname.getText().toString();
                    String chaltype = (String) spn.getSelectedItem();
                    String description = cDescription.getText().toString();
                    int userId = 1;

                    DBHelper helper = new DBHelper(AddChallangeActivity.this);
                    String sql = "INSERT INTO challanges (type,name,description,userId)" +
                            " VALUES ('"+chaltype+"','"+Challangename+"','"+description+"','"+userId+"') ";

                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        db.execSQL(sql);
                        successfulAlert();

                    } catch (SQLiteException e) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(AddChallangeActivity.this);
                        a_builder.setMessage("User already exist!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cname.setText("");
                                        //spn.getSelectedItem();
                                        cDescription.setText("");
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
        AlertDialog.Builder a_builder = new AlertDialog.Builder(AddChallangeActivity.this);
        a_builder.setMessage("Successfully created challange")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cname.setText("");
                        //spn.getSelectedItem();
                        cDescription.setText("");
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("Challenge", "customizedChallange"));
                        dialog.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Congratulation!");
        alert.show();
    }

    private boolean submitForm() {
        if (!validateCName()) {
            return false;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean validateCName() {
        if (cname.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter the Challange name");
            requestFocus(cname);
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
                case R.id.cusChallangeName:
                    validateCName();
                    break;

            }
        }
    }
}
