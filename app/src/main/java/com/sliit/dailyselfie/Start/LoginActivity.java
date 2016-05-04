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
import android.util.Log;
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
import com.sliit.dailyselfie.Start.mFacebook.MyConfigurations;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.listeners.OnLoginListener;

import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    DBHelper DBH;
    private EditText username,password;
    private TextInputLayout inputLayoutName,inputLayoutPassword;
    String uid;
    SimpleFacebook SF;
    Button fb,gplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SimpleFacebook.setConfiguration(new MyConfigurations().getMyConfigs());

        DBH = new DBHelper(this);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_userName);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        fb=(Button)findViewById(R.id.fb) ;
        gplus=(Button)findViewById(R.id.gplus);

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

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogin();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SF=SimpleFacebook.getInstance(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SF.onActivityResult(requestCode,resultCode,data);

    }

    private void fblogin(){
        OnLoginListener loginListener = new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Toast.makeText(LoginActivity.this, "Logged", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "cancled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e("onException",throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                Log.e("Fail",reason);
            }
        };

        SF.login(loginListener);
    }

}
