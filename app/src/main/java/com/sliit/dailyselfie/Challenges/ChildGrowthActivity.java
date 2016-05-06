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
import android.widget.Spinner;
import android.widget.Toast;

import com.sliit.dailyselfie.AlertReciver.AlarmReciver;
import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.util.GregorianCalendar;

import picker.ugurtekbas.com.Picker.Picker;

public class ChildGrowthActivity extends AppCompatActivity {

//    String fitType;
    Dialog d;
    Picker picker;

    private EditText childname,childDescription;
    private TextInputLayout inputLayoutName,inputLayoutDescription;
    private Button btnAdd;
    String userID;
    Button bset,bcancle;
    Spinner spn;

    private SwipeNumberPicker cheight,cweight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_growth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.childsetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new Dialog(ChildGrowthActivity.this);
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
                        //Toast.makeText(getApplicationContext(),"Set Alarm",Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });

                bcancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(),"cancle Alarm",Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });
            }
        });

        spn = (Spinner)findViewById(R.id.childdate);
        final String [] challengePeriodType = getResources().getStringArray(R.array.challengePeriodType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,challengePeriodType);
        spn.setAdapter(adapter);

        SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userID = userDetails.getString("loggedUserId","");

        cheight = (SwipeNumberPicker)findViewById(R.id.snpchild0);
        cheight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        cweight = (SwipeNumberPicker)findViewById(R.id.snpchild2);
        cweight.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                return true;
            }
        });

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_ChildName);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_Childdescription);

        childname = (EditText)findViewById(R.id.childgrowthName);
        childDescription = (EditText)findViewById(R.id.cdescription);
        btnAdd = (Button) findViewById(R.id.child_submit_btn);
        cheight = (SwipeNumberPicker)findViewById(R.id.snpchild0);
        cweight = (SwipeNumberPicker)findViewById(R.id.snpchild2);

        childname.addTextChangedListener(new MyTextWatcher(childname));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {

                    String cChallangename = childname.getText().toString();
                    Double cHeight = Double.parseDouble((String) cheight.getText());
                    Double cWeight = Double.parseDouble((String) cweight.getText());
                    String cdescription = childDescription.getText().toString();
                    String type = "ChildGrowth";
                    int userId = Integer.parseInt(userID);

                    DBHelper helper = new DBHelper(ChildGrowthActivity.this);
                    String sql = "INSERT INTO challanges (type,name,height,weight,description,userId)" +
                            " VALUES ('"+type+"','"+cChallangename+"','"+cHeight+"','"+cWeight+"','"+cdescription+"','"+userId+"') ";

                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        db.execSQL(sql);
                        successfulAlert();

                        SharedPreferences cDetails = getSharedPreferences("cDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = cDetails.edit();
                        editor1.putString("chName", cChallangename);
                        editor1.apply();

                    } catch (SQLiteException e) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(ChildGrowthActivity.this);
                        a_builder.setMessage("User already exist!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        childname.setText("");
                                        childDescription.setText("");
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
        AlertDialog.Builder a_builder = new AlertDialog.Builder(ChildGrowthActivity.this);
        a_builder.setMessage("Successfully created Child Growth challange")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        childname.setText("");
                        childDescription.setText("");
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("Challenge", "child"));
                        dialog.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Congratulation!");
        alert.show();
    }

    private boolean submitForm() {
        if (!validateChildName()) {
            return false;
        }
        return true;
    }

    private boolean validateChildName() {
        if (childname.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter the Challange name");
            requestFocus(childname);
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
                case R.id.childgrowthName:
                    validateChildName();
                    break;

            }
        }
    }

    public void SetAlarmBroadcast(){

        Long alertTime =new GregorianCalendar().getTimeInMillis();
        Intent alertIntent = new Intent(ChildGrowthActivity.this,AlarmReciver.class).putExtra("Category","Fitness");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,PendingIntent.getBroadcast(ChildGrowthActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alertTime,alarmManager.INTERVAL_DAY*7,PendingIntent.getBroadcast(ChildGrowthActivity.this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));


    }
 }

