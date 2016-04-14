package com.sliit.dailyselfie.Start;

import android.app.Dialog;
import android.content.ContentValues;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final int ActivityStartCAM=0;
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
                d=new Dialog(SignUpActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.propicdialog);
                d.show();


                ib1=(ImageButton)d.findViewById(R.id.procam);

                ib2=(ImageButton)d.findViewById(R.id.progal);

                ib2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(gallery,PICK_IMAGE);
                        Cam=false;
                        Gal=true;
                        d.cancel();
                    }
                });


                ib1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent CAMint= new Intent();
                        CAMint.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile=null;
                        try{
                            photoFile=createImageFile();
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        CAMint.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(CAMint, ActivityStartCAM);
                        Gal=false;
                        Cam=true;
                        d.cancel();
                    }
                });

            }
        });







    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(Cam){
            rotateImage(setReducedImageSize());
        }else if(Gal){
            CIV.setImageURI(imageUri);
        }else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE  && resultCode==RESULT_OK){
            imageUri=data.getData();
           bit64camimage=BitmaptoString(getRealPathFromURI(imageUri));
            CIV.setImageURI(imageUri);

        }
        if(requestCode==ActivityStartCAM  && resultCode==RESULT_OK){
            bit64camimage=BitmaptoString(ImageFileLoaction);
            rotateImage(setReducedImageSize());
            Toast.makeText(getApplicationContext(),bit64camimage,Toast.LENGTH_SHORT).show();
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

    public void signUp(View v) {
        EditText rfname = (EditText)findViewById(R.id.fname);
        EditText rlname = (EditText)findViewById(R.id.lname);
        EditText remail = (EditText)findViewById(R.id.semail);
        EditText rpassword = (EditText)findViewById(R.id.password);

        String fname = rfname.getText().toString();
        String lname = rlname.getText().toString();
        String email = remail.getText().toString();
        String password = rpassword.getText().toString();
        String propic = "";

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fname",fname);
        values.put("lname", lname);
        values.put("email",email);
        values.put("password", password);
        values.put("profilepic", email);

        try {

            db.insertOrThrow("register", null, values);
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

            Toast.makeText(this, "Registed !", Toast.LENGTH_LONG).show();
        }catch(SQLiteException e){
            Toast.makeText(this,"User already exsist", Toast.LENGTH_LONG).show();
        }
        rfname.setText("");
        rlname.setText("");
        remail.setText("");
        rpassword.setText("");
        EditText rcpassword = (EditText)findViewById(R.id.cpassword);
        rcpassword.setText("");
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