package com.sliit.dailyselfie;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void signUp(View v) {
        EditText rfname = (EditText)findViewById(R.id.fname);
        EditText rlname = (EditText)findViewById(R.id.lname);
        EditText remail = (EditText)findViewById(R.id.semail);
        EditText rpassword = (EditText)findViewById(R.id.password);

        String fname = rfname.getText().toString();
        String lname = rlname.getText().toString();
        String email = remail.getText().toString();
        String password = rpassword.getText().toString();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fname",fname);
        values.put("lname", lname);
        values.put("email",email);
        values.put("password", password);
        values.put("profilepic", email);

        db.insert("register", null, values);

        Toast.makeText(this, "Registed !", Toast.LENGTH_LONG).show();

        rfname.setText("");
        rlname.setText("");
        remail.setText("");
        rpassword.setText("");
        EditText rcpassword = (EditText)findViewById(R.id.cpassword);
        rcpassword.setText("");
    }

}
