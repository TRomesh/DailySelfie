package com.sliit.dailyselfie.Start;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.sliit.dailyselfie.Community.RegisterUser;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private EditText rfname,rlname,remail,rpassword,rconfirmpassword;
    private TextInputLayout inputLayoutFName,inputLayoutLName,inputLayoutEmail,inputLayoutPassword,inputLayoutcPassword;
    private Button btnSignUp;

    private final int ActivityStartCAM=0;
    private String ImageFileLoaction="";
    private String bit64camimage="";

    RegisterUser RU;
    Firebase SignupRef;
    CircleImageView CIV;
    Dialog d;
    ImageButton ib1;
    ImageButton ib2;
    private final static int PICK_IMAGE=1;
    Uri imageUri;
    String dimagepath="";

    boolean Cam=false;
    boolean Gal=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        CIV = (CircleImageView)findViewById(R.id.profile_image);
        CIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new Dialog(SignUpActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.propicdialog);
                d.show();


                ib1 = (ImageButton) d.findViewById(R.id.procam);

                ib2 = (ImageButton) d.findViewById(R.id.progal);

                ib2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(gallery, PICK_IMAGE);
                        Cam = false;
                        Gal = true;
                        d.cancel();
                    }
                });


                ib1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent CAMint = new Intent();
                        CAMint.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        CAMint.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(CAMint, ActivityStartCAM);
                        Gal = false;
                        Cam = true;
                        d.cancel();
                    }
                });

            }
        });

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
                    String propic = ImageFileLoaction;

                    DBHelper helper = new DBHelper(getApplicationContext());
                    String sql = "INSERT INTO register (fname,lname,email,password,profilepic)" +
                            " VALUES ('"+fname+"','"+lname+"','"+email+"','"+password+"','"+propic+"') ";

                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        db.execSQL(sql);

                        RU=new RegisterUser();
                        RU.setUname(fname);
                        RU.setLname(lname);
                        RU.setEmail(email);
                        RU.setPassword(password);

                        if(Cam){
                            RU.setProfilepic(bit64camimage);

                        }else if(Gal){
                            RU.setProfilepic(bit64camimage);
                        }else{
                            RU.setProfilepic("Gallery pic");
                        }
                        registerUser(RU);
                        successfulAlert();

                    }catch(SQLiteException e){
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(SignUpActivity.this);
                        a_builder.setMessage("User already exist!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        rfname.setText("");
                                        rlname.setText("");
                                        remail.setText("");
                                        rpassword.setText("");
                                        rconfirmpassword.setText("");
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
        AlertDialog.Builder a_builder = new AlertDialog.Builder(SignUpActivity.this);
        a_builder.setMessage("")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        dialog.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Welcome to Daily Selfi App!");
        alert.show();

        rlname.setText("");
        remail.setText("");
        rpassword.setText("");
        rconfirmpassword.setText("");
        rfname.setText("");
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
            return true;
        }
    }

    private boolean validateLName() {
        if (rlname.getText().toString().trim().isEmpty()) {
            inputLayoutLName.setError(getString(R.string.err_msg_lname));
            requestFocus(rlname);
            return false;
        } else {
            inputLayoutLName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = remail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(remail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        if (rpassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(rpassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (rconfirmpassword.getText().toString().trim().isEmpty() || !rconfirmpassword.getText().toString().equals(rpassword.getText().toString())) {
            inputLayoutcPassword.setError(getString(R.string.err_msg_confirmpassword));
            requestFocus(rconfirmpassword);
            return false;
        } else {
            inputLayoutcPassword.setErrorEnabled(false);
            return true;
        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(Cam){
            rotateImage(setReducedImageSize());
        }else if(Gal){
            //CIV.setImageURI(imageUri);
            Glide.with(this).load(imageUri).into(CIV);
        }else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE  && resultCode==RESULT_OK){
            imageUri=data.getData();
            bit64camimage=BitmaptoString(getRealPathFromURI(imageUri));
            // CIV.setImageURI(imageUri);
            Glide.with(this).load(imageUri).into(CIV);

        }
        if(requestCode==ActivityStartCAM  && resultCode==RESULT_OK){
            bit64camimage=BitmaptoString(ImageFileLoaction);
            rotateImage(setReducedImageSize());
            Toast.makeText(getApplicationContext(), bit64camimage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SignupRef=new Firebase("https://dailyselfie.firebaseio.com/users");
    }


    public void registerUser(RegisterUser ru){
        SignupRef.push().setValue(ru);
    }

    File createImageFile()throws IOException{
        String timestamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFieldname="Profile picture "+timestamp+"_";
        File storageDeirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFieldname,".jpg",storageDeirectory);
        ImageFileLoaction=image.getAbsolutePath();
        return image;
    }

    private Bitmap  setReducedImageSize(){
        int taretimagevieweidth=CIV.getWidth();
        int targetimageviewheight=CIV.getHeight();
        BitmapFactory.Options bmfop= new BitmapFactory.Options();
        bmfop.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(ImageFileLoaction,bmfop);
        int Cameraheight=bmfop.outHeight;
        int Camerawidth=bmfop.outWidth;
        int scalefactor =Math.min(Camerawidth/taretimagevieweidth,Cameraheight/targetimageviewheight);
        bmfop.inSampleSize=scalefactor;
        bmfop.inJustDecodeBounds=false;
        return  BitmapFactory.decodeFile(ImageFileLoaction,bmfop);
    }

    private void rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try{
            exifInterface = new ExifInterface(ImageFileLoaction);
        }catch(Exception e){
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            default:

        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        CIV.setImageBitmap(rotatedBitmap);

    }

    public String BitmaptoString(String path){
        // Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.thara1);
        Bitmap bmp = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        bmp.recycle();
        byte[] byteArray = stream.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return  imageFile;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}