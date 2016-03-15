package com.sliit.dailyselfie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Tharaka on 13/03/2016.
 */
public class BaseActivity extends AppCompatActivity {



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {


        findViewById(R.id.nav_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BaseActivity.this, "Button 1", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.nav_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BaseActivity.this, "Button 2", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.nav_btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BaseActivity.this, "Button 3", Toast.LENGTH_SHORT).show();
            }
        });

        super.onPostCreate(savedInstanceState);

    }
}
