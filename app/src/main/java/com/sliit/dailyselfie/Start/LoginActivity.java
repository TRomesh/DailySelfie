package com.sliit.dailyselfie.Start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.MainActivity;
import com.sliit.dailyselfie.R;

public class LoginActivity extends AppCompatActivity {

    DBHelper DBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBH = new DBHelper(this);


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


}
