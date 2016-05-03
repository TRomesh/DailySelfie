package com.sliit.dailyselfie.Start;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.MainActivity;
import com.sliit.dailyselfie.R;

public class LoginActivity extends AppCompatActivity {

    DBHelper DBH;
    private EditText username,password;
    private TextInputLayout inputLayoutName,inputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBH = new DBHelper(this);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_userName);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);


        username = (EditText)findViewById(R.id.uname);
        password = (EditText)findViewById(R.id.password);

        Button blogin=(Button)findViewById(R.id.login);
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);

            }
        });

        findViewById(R.id.signUpbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }

    public void login() {

        if (username.getText().toString() == "" && password.getText().toString() == "") {
            Intent i=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        }
        else {

        }

    }
}
