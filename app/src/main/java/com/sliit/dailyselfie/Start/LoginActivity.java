package com.sliit.dailyselfie.Start;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.sliit.dailyselfie.Camera.CameraActivity;
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
    String uid,ufname,ulname,uemail,upropic;
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

                String Username = username.getText().toString();
                String Password = password.getText().toString();

                if(!Username.isEmpty()){

                    if(!Password.isEmpty()){

                        DBHelper helper = new DBHelper(getApplicationContext());
                        SQLiteDatabase db = helper.getWritableDatabase();

                        String sql = "SELECT id,fname,lname,email,profilepic FROM register WHERE email = '" + Username + "' AND password = '" + Password + "'";
                        Cursor results = db.rawQuery(sql, null);

                        if (results.moveToFirst()) {
                            uid = results.getString(0);
                            ufname = results.getString(1);
                            ulname = results.getString(2);
                            uemail = results.getString(3);
                            upropic = results.getString(4);

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                            SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = userDetails.edit();
                            editor.putString("loggedUserId", uid);
                            editor.putString("loggedUserfname", ufname);
                            editor.putString("loggedUserlname", ulname);
                            editor.putString("loggedUseremail", uemail);
                            editor.putString("loggedUserpropic", upropic);
                            editor.apply();

                        } else {

                            AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
                            a_builder.setMessage("User Name or Password does not match")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert = a_builder.create();
                            alert.setTitle("Error");
                            alert.show();
                        }
                    } else {

                        AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
                        a_builder.setMessage("Password textfield is empty")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Error");
                        alert.show();
                    }
                } else {

                    AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
                    a_builder.setMessage("Username textfield is empty")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Error");
                    alert.show();
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
