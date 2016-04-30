package com.sliit.dailyselfie.Start;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText rfname,rlname,remail,rpassword,rconfirmpassword;
    private TextInputLayout inputLayoutFName,inputLayoutLName,inputLayoutEmail,inputLayoutPassword,inputLayoutcPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputLayoutFName = (TextInputLayout) findViewById(R.id.input_layout_firstName);
        inputLayoutLName = (TextInputLayout) findViewById(R.id.input_layout_LastName);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_Email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_Password);
        inputLayoutcPassword = (TextInputLayout) findViewById(R.id.input_layout_confirmPassword);

        rfname = (EditText)findViewById(R.id.input_firstName);
        rlname = (EditText)findViewById(R.id.input_LastName);
        remail = (EditText)findViewById(R.id.input_Email);
        rpassword = (EditText)findViewById(R.id.input_Password);
        rconfirmpassword = (EditText)findViewById(R.id.input_confirmPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        rfname.addTextChangedListener(new MyTextWatcher(rfname));
        rlname.addTextChangedListener(new MyTextWatcher(rlname));
        remail.addTextChangedListener(new MyTextWatcher(remail));
        rpassword.addTextChangedListener(new MyTextWatcher(rpassword));
        rconfirmpassword.addTextChangedListener(new MyTextWatcher(rconfirmpassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submitForm()){

                    String fname = rfname.getText().toString();
                    String lname = rlname.getText().toString();
                    String email = remail.getText().toString();
                    String password = rpassword.getText().toString();

                    DBHelper helper = new DBHelper(getApplicationContext());
                    SQLiteDatabase db = helper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("fname",fname);
                    values.put("lname", lname);
                    values.put("email",email);
                    values.put("password", password);
                    values.put("profilepic", email);

                    db.insert("register", null, values);

                    //Toast.makeText(this, "Registed !", Toast.LENGTH_LONG).show();

                    rfname.setText("");
                    rlname.setText("");
                    remail.setText("");
                    rpassword.setText("");
                    rconfirmpassword.setText("");
                }
            }
        });
    }

    private boolean submitForm() {
        if (!validateFName()) {
            return false;
        }
        if (!validateLName()) {
            return false;
        }
        if (!validateEmail()) {
            return false;
        }
        if (!validatePassword()) {
            return false;
        }
        if (!validateConfirmPassword()) {
            return false;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean validateFName() {
        if (rfname.getText().toString().trim().isEmpty()) {
            inputLayoutFName.setError(getString(R.string.err_msg_fname));
            requestFocus(rfname);
            return false;
        } else {
            inputLayoutFName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLName() {
        if (rlname.getText().toString().trim().isEmpty()) {
            inputLayoutLName.setError(getString(R.string.err_msg_lname));
            requestFocus(rlname);
            return false;
        } else {
            inputLayoutLName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = remail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(remail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (rpassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(rpassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        if (rconfirmpassword.getText().toString().trim().isEmpty() || rconfirmpassword.getText().toString()==rpassword.getText().toString()) {
            inputLayoutcPassword.setError(getString(R.string.err_msg_confirmpassword));
            requestFocus(rconfirmpassword);
            return false;
        } else {
            inputLayoutcPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                case R.id.input_firstName:
                    validateFName();
                    break;
                case R.id.input_LastName:
                    validateFName();
                    break;
                case R.id.input_Email:
                    validateEmail();
                    break;
                case R.id.input_Password:
                    validatePassword();
                    break;
                case R.id.input_confirmPassword:
                    validateConfirmPassword();
                    break;
            }
        }
    }

}
