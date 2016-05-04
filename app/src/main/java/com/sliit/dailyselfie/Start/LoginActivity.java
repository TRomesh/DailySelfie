package com.sliit.dailyselfie.Start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.MainActivity;
import com.sliit.dailyselfie.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    DBHelper DBH;
    private EditText username,password;
    private TextInputLayout inputLayoutName,inputLayoutPassword;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBH = new DBHelper(this);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_userName);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        username = (EditText)findViewById(R.id.uname);
        password = (EditText)findViewById(R.id.password);

        findViewById(R.id.signUpbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });


        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("login button awa");

                String Username = username.getText().toString();
                String Password = password.getText().toString();

                DBHelper helper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();

                String sql = "SELECT id FROM register WHERE email = '" + Username + "' AND password = '" + Password + "'";
                Cursor results = db.rawQuery(sql, null);

                if (results.moveToFirst()) {
                    uid = results.getString(0);

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                    SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userDetails.edit();
                    editor.putString("loggedUserId", uid);
                    editor.apply();

                } else {
                    Toast.makeText(LoginActivity.this, "Some problem occurred", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }




}
