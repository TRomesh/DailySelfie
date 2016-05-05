package com.sliit.dailyselfie.Camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.sliit.dailyselfie.TimeLine.TimeLine;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rebus.bottomdialog.BottomDialog;


public class CameraActivity extends AppCompatActivity {

    private static final int ActivityStartCAM=0;
    private final static int EDIT_IMAGE=1;
    private ImageView IV;
    private String ImageFileLoaction="";

    BottomBar CamBottomBar;
    SwipeNumberPicker fitpicker,maternitypicker,childpicker,postpicker;
    ImageButton bcan,bsnap,bdesc;
    Dialog d;
    BottomDialog dialog;
    String challenge;
    Bundle extras;
    boolean picpresent=false;

    DBHelper helper;
    SQLiteDatabase db;
    ContentValues values;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12;

    Uri ImageUri;
    String chalName;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         helper = new DBHelper(this);
         db = helper.getWritableDatabase();
         values = new ContentValues();

        SharedPreferences cDetails = getSharedPreferences("cDetails", Context.MODE_PRIVATE);
        chalName = cDetails.getString("chName","");

        IV = (ImageView)findViewById(R.id.snap1);
        fitpicker = (SwipeNumberPicker)findViewById(R.id.fitweight);
        b1=(Button)findViewById(R.id.fitadd);
        b2=(Button)findViewById(R.id.fitcancle);


        IV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                if (picpresent) {
                    dialog = new BottomDialog(CameraActivity.this);
                    dialog.title("Options");
                    dialog.canceledOnTouchOutside(true);
                    dialog.cancelable(true);
                    dialog.inflateMenu(R.menu.camera_bottomsheet_menu);
                    dialog.setOnItemSelectedListener(new BottomDialog.OnItemSelectedListener() {
                        @Override
                        public boolean onItemSelected(int id) {
                            switch (id) {
                                case R.id.addeffects:
                                    Toast.makeText(getApplicationContext(),"Edit",Toast.LENGTH_SHORT).show();
                                    return true;

                                case R.id.crop:
                                    Toast.makeText(getApplicationContext(),"crop",Toast.LENGTH_SHORT).show();
                                    return true;

                                default:
                                    return false;
                            }
                        }
                    });
                    dialog.show();
                }
                return true;
            }
        });


         extras = getIntent().getExtras();
        if (extras != null) {
            challenge = extras.getString("Challenge");
        }


        CamBottomBar = BottomBar.attach(this, savedInstanceState);
        CamBottomBar.noNavBarGoodness();



        CamBottomBar.setItemsFromMenu(R.menu.camera_bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if (menuItemId == R.id.nav_back) {

                    IV.setImageResource(R.drawable.selfieimage);
                    IV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    picpresent = false;

                } else if (menuItemId == R.id.nav_takeSnap) {
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


                } else if (menuItemId == R.id.nav_addDetails) {
                    if (picpresent) {

                        switch (challenge) {

                            case "fitness":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.fitnessdialog);
                                ImageView fitim = (ImageView) d.findViewById(R.id.fitdialogimg);
                                final EditText fittxt = (EditText) d.findViewById(R.id.fitdialogdesc);
                                fitpicker = (SwipeNumberPicker)d.findViewById(R.id.fitweight);
                                b1=(Button)d.findViewById(R.id.fitadd);
                                b2=(Button)d.findViewById(R.id.fitcancle);
                                fitpicker.setOnValueChangeListener(new OnValueChangeListener() {
                                    @Override
                                    public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                                        return true;
                                    }
                                });
                                b1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String fitCurrentDescription = fittxt.getText().toString();
                                        Double currentWeight = Double.parseDouble((String) fitpicker.getText());
                                        String currentImg = ImageFileLoaction;
                                        String ChallangeName = chalName;

                                        DBHelper helper = new DBHelper(CameraActivity.this);
                                        String sql = "INSERT INTO posts (description,weight,image,challangeName)" +
                                                " VALUES ('"+fitCurrentDescription+"','"+currentWeight+"','"+currentImg+"','"+ChallangeName+"') ";

                                        SQLiteDatabase db = helper.getWritableDatabase();

                                        try {
                                            db.execSQL(sql);
                                            if(successfulAlert()){
                                                startActivity(new Intent(CameraActivity.this, TimeLine.class));
                                            }

                                        } catch (SQLiteException e) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(CameraActivity.this);
                                            a_builder.setMessage("User already exist!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            d.dismiss();
                                                        }
                                                    });

                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Alert");
                                            alert.show();
                                        }

                                        //Toast.makeText(v.getContext(),fittxt.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                b2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        fittxt.setText("");
                                    }
                                });

                                d.show();
                                break;

                            case "maternity":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.maternitydialog);
                                ImageView matimg = (ImageView) d.findViewById(R.id.matdialogimg);
                                final EditText mattxt = (EditText) d.findViewById(R.id.matdialogdesc);
                                maternitypicker = (SwipeNumberPicker)d.findViewById(R.id.Bwaist);
                                b3=(Button)d.findViewById(R.id.matadd);
                                b4=(Button)d.findViewById(R.id.matcan);
                                maternitypicker.setOnValueChangeListener(new OnValueChangeListener() {
                                    @Override
                                    public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                                        return true;
                                    }
                                });
                                b3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String matCurrentDescription = mattxt.getText().toString();
                                        Double currentWaist = Double.parseDouble((String) maternitypicker.getText());
                                        String currentImg = ImageFileLoaction;
                                        String ChallangeName = chalName;

                                        DBHelper helper = new DBHelper(CameraActivity.this);
                                        String sql = "INSERT INTO posts (description,waistSize,image,challangeName)" +
                                                " VALUES ('"+matCurrentDescription+"','"+currentWaist+"','"+currentImg+"','"+ChallangeName+"') ";

                                        SQLiteDatabase db = helper.getWritableDatabase();

                                        try {
                                            db.execSQL(sql);
                                            if(successfulAlert()){
                                                startActivity(new Intent(CameraActivity.this, TimeLine.class));
                                            }

                                        } catch (SQLiteException e) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(CameraActivity.this);
                                            a_builder.setMessage("User already exist!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            d.dismiss();
                                                        }
                                                    });

                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Alert");
                                            alert.show();
                                        }

                                        //Toast.makeText(v.getContext(),fittxt.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                b4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mattxt.setText("");
                                    }
                                });
                                d.show();

                                break;

                            case "child":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.childdialog);
                                ImageView chilimg = (ImageView) d.findViewById(R.id.childialogimg);
                                final EditText chiltxt = (EditText) d.findViewById(R.id.childialogdesc);
                                childpicker = (SwipeNumberPicker)d.findViewById(R.id.Bheight);
                                b5=(Button)d.findViewById(R.id.chiladd);
                                b6=(Button)d.findViewById(R.id.chilcan);
                                childpicker.setOnValueChangeListener(new OnValueChangeListener() {
                                    @Override
                                    public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                                        return true;
                                    }
                                });
                                b5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String childCurrentDescription = chiltxt.getText().toString();
                                        Double currentHeight = Double.parseDouble((String) childpicker.getText());
                                        String currentImg = ImageFileLoaction;
                                        String ChallangeName = chalName;

                                        DBHelper helper = new DBHelper(CameraActivity.this);
                                        String sql = "INSERT INTO posts (description,height,image,challangeName)" +
                                                " VALUES ('"+childCurrentDescription+"','"+currentHeight+"','"+currentImg+"','"+ChallangeName+"') ";

                                        SQLiteDatabase db = helper.getWritableDatabase();

                                        try {
                                            db.execSQL(sql);
                                            if(successfulAlert()){
                                                startActivity(new Intent(CameraActivity.this, TimeLine.class));
                                            }

                                        } catch (SQLiteException e) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(CameraActivity.this);
                                            a_builder.setMessage("User already exist!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            d.dismiss();
                                                        }
                                                    });

                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Alert");
                                            alert.show();
                                        }

                                        //Toast.makeText(v.getContext(),fittxt.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                b6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        chiltxt.setText("");
                                    }
                                });
                                d.show();

                                break;
                            case "postmaternity":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.postmaternitydialog);
                                ImageView postimg = (ImageView) d.findViewById(R.id.postmatdialogimg);
                                final EditText posttxt = (EditText) d.findViewById(R.id.postdialogdesc);
                                postpicker = (SwipeNumberPicker)d.findViewById(R.id.Postwaist);
                                b7=(Button)d.findViewById(R.id.postadd);
                                b8=(Button)d.findViewById(R.id.postcan);
                                postpicker.setOnValueChangeListener(new OnValueChangeListener() {
                                    @Override
                                    public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                                        return true;
                                    }
                                });
                                b7.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String postCurrentDescription = posttxt.getText().toString();
                                        Double currentWaist = Double.parseDouble((String) postpicker.getText());
                                        String currentImg = ImageFileLoaction;
                                        String ChallangeName = chalName;

                                        DBHelper helper = new DBHelper(CameraActivity.this);
                                        String sql = "INSERT INTO posts (description,waistSize,image,challangeName)" +
                                                " VALUES ('"+postCurrentDescription+"','"+currentWaist+"','"+currentImg+"','"+ChallangeName+"') ";

                                        SQLiteDatabase db = helper.getWritableDatabase();

                                        try {
                                            db.execSQL(sql);
                                            if(successfulAlert()){
                                                startActivity(new Intent(CameraActivity.this, TimeLine.class));
                                            }

                                        } catch (SQLiteException e) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(CameraActivity.this);
                                            a_builder.setMessage("User already exist!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            d.dismiss();
                                                        }
                                                    });

                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Alert");
                                            alert.show();
                                        }

                                        //Toast.makeText(v.getContext(),fittxt.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                b8.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        posttxt.setText("");
                                    }
                                });
                                d.show();

                                break;
                            case "noshave":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.noshavedialog);
                                ImageView noshaveimg = (ImageView) d.findViewById(R.id.noshavedialogimg);
                                final EditText noshavetxt = (EditText) d.findViewById(R.id.noshavedialogdesc);
                                b9=(Button)d.findViewById(R.id.chiladd);
                                b10=(Button)d.findViewById(R.id.chilcan);

                                b9.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String noshaveCurrentDescription = noshavetxt.getText().toString();
                                        String currentImg = ImageFileLoaction;
                                        String ChallangeName = chalName;

                                        DBHelper helper = new DBHelper(CameraActivity.this);
                                        String sql = "INSERT INTO posts (description,image,challangeName)" +
                                                " VALUES ('"+noshaveCurrentDescription+"','"+currentImg+"','"+ChallangeName+"') ";

                                        SQLiteDatabase db = helper.getWritableDatabase();

                                        try {
                                            db.execSQL(sql);
                                            if(successfulAlert()){
                                                startActivity(new Intent(CameraActivity.this, TimeLine.class));
                                            }

                                        } catch (SQLiteException e) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(CameraActivity.this);
                                            a_builder.setMessage("User already exist!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            d.dismiss();
                                                        }
                                                    });

                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Alert");
                                            alert.show();
                                        }

                                        //Toast.makeText(v.getContext(),fittxt.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                b10.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        noshavetxt.setText("");
                                    }
                                });
                                d.show();

                                break;
                            case "customizedChallange":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.customchallangedialog);
                                ImageView cusimg = (ImageView) d.findViewById(R.id.cusdialogimg);
                                final EditText custxt = (EditText) d.findViewById(R.id.cusdialogdesc);
                                b11=(Button)d.findViewById(R.id.cusadd);
                                b12=(Button)d.findViewById(R.id.cuscan);

                                b11.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String cusCurrentDescription = custxt.getText().toString();
                                        String currentImg = ImageFileLoaction;
                                        String ChallangeName = chalName;

                                        DBHelper helper = new DBHelper(CameraActivity.this);
                                        String sql = "INSERT INTO posts (description,image,challangeName)" +
                                                " VALUES ('"+cusCurrentDescription+"','"+currentImg+"','"+ChallangeName+"') ";

                                        SQLiteDatabase db = helper.getWritableDatabase();

                                        try {
                                            db.execSQL(sql);
                                            if(successfulAlert()){
                                                startActivity(new Intent(CameraActivity.this, TimeLine.class));
                                            }

                                        } catch (SQLiteException e) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(CameraActivity.this);
                                            a_builder.setMessage("User already exist!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            d.dismiss();
                                                        }
                                                    });

                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Alert");
                                            alert.show();
                                        }

                                        //Toast.makeText(v.getContext(),fittxt.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                b12.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        custxt.setText("");
                                    }
                                });
                                d.show();

                                break;

                        }


                    }else{
                        Toast.makeText(CameraActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                    }

                }
//
//                CamBottomBar.mapColorForTab(0, ContextCompat.getColor(CameraActivity.this, R.color.bottomPrimary));
//                CamBottomBar.mapColorForTab(1, ContextCompat.getColor(CameraActivity.this, R.color.bottomPrimary));
//                CamBottomBar.mapColorForTab(2, ContextCompat.getColor(CameraActivity.this, R.color.bottomPrimary));


            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {


                if (menuItemId == R.id.nav_back) {

                    IV.setImageResource(R.drawable.selfieimage);
                    IV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    picpresent = false;


                } else if (menuItemId == R.id.nav_takeSnap) {
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


                } else if (menuItemId == R.id.nav_addDetails) {

                    if (picpresent) {

                        switch (challenge) {

                            case "fitness":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.fitnessdialog);
                                ImageView fitim = (ImageView) d.findViewById(R.id.fitdialogimg);
                                EditText fittxt = (EditText) d.findViewById(R.id.fitdialogdesc);
                                d.show();
                                break;

                            case "maternity":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.maternitydialog);
                                ImageView matimg = (ImageView) d.findViewById(R.id.matdialogimg);
                                EditText mattxt = (EditText) d.findViewById(R.id.matdialogdesc);
                                d.show();

                                break;

                            case "child":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.childdialog);
                                ImageView chilimg = (ImageView) d.findViewById(R.id.childialogimg);
                                EditText chiltxt = (EditText) d.findViewById(R.id.childialogdesc);
                                d.show();

                                break;

                            case "postmaternity":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.postmaternitydialog);
                                ImageView postimg = (ImageView) d.findViewById(R.id.postmatdialogimg);
                                EditText posttxt = (EditText) d.findViewById(R.id.postdialogdesc);
                                d.show();

                                break;

                            case "noshave":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.noshavedialog);
                                ImageView noshaveimg = (ImageView) d.findViewById(R.id.noshavedialogimg);
                                EditText noshavetxt = (EditText) d.findViewById(R.id.noshavedialogdesc);
                                d.show();

                                break;

                            case "customizedChallange":
                                d = new Dialog(CameraActivity.this);
                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                d.setContentView(R.layout.customchallangedialog);
                                ImageView cusimg = (ImageView) d.findViewById(R.id.cusdialogimg);
                                EditText custxt = (EditText) d.findViewById(R.id.cusdialogdesc);
                                d.show();

                                break;

                        }


                    }else{
                        Toast.makeText(CameraActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                    }

                }

//
//                CamBottomBar.mapColorForTab(0, ContextCompat.getColor(CameraActivity.this, R.color.bottomPrimary));
//                CamBottomBar.mapColorForTab(1, ContextCompat.getColor(CameraActivity.this, R.color.bottomPrimary));
//                CamBottomBar.mapColorForTab(2, ContextCompat.getColor(CameraActivity.this, R.color.bottomPrimary));


            }
        });

    }

    public boolean successfulAlert(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(CameraActivity.this);
        a_builder.setMessage("Successfully Inserted")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        d.dismiss();

                    }
                });

        AlertDialog alert = a_builder.create();
        alert.show();
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rotateImage(setReducedImageSize());
        IV.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!picpresent){
            IV.setImageResource(R.drawable.selfieimage);
            IV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    public void TakePhoto(View v){
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

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ActivityStartCAM  && resultCode==RESULT_OK){
            rotateImage(setReducedImageSize());
           ImageUri = Uri.parse(ImageFileLoaction);
            IV.setScaleType(ImageView.ScaleType.CENTER_CROP);
            picpresent=true;

        }
        if (resultCode == RESULT_OK && requestCode==EDIT_IMAGE) {


                /* Make a case for the request code we passed to startActivityForResult() */

                    /* Show the image! */
            Uri editedImageUri = data.getData();
            ImageUri=data.getData();
           Glide.with(this).load(editedImageUri).into(IV);
            picpresent=true;



        }

    }

    File createImageFile()throws IOException{
        String timestamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFieldname="IMAGE "+timestamp+"_";
        File storageDeirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFieldname,".jpg",storageDeirectory);
        ImageFileLoaction=image.getAbsolutePath();
        return image;
    }

    private Bitmap  setReducedImageSize(){
        int taretimagevieweidth=IV.getWidth();
        int targetimageviewheight=IV.getHeight();
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
        IV.setImageBitmap(rotatedBitmap);
    }



}
