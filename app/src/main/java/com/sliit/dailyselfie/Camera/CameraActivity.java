package com.sliit.dailyselfie.Camera;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.dailyselfie.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class CameraActivity extends AppCompatActivity {

    private static final int ActivityStartCAM=0;
    private ImageView IV;
    private String ImageFileLoaction="";

    ImageButton bcan,bsnap,bdesc;
    Dialog d;
    String challenge;
    Bundle extras;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IV = (ImageView)findViewById(R.id.snap1);
        bcan = (ImageButton)findViewById(R.id.cancle);
        bsnap = (ImageButton)findViewById(R.id.takeSnap);
        bdesc = (ImageButton)findViewById(R.id.addDesc);

         extras = getIntent().getExtras();
        if (extras != null) {
            challenge = extras.getString("Challenge");
        }


        bdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (challenge) {

                    case "fitness":
                      d = new Dialog(CameraActivity.this);
                      d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                      d.setContentView(R.layout.fitnessdialog);
                      ImageView fitim = (ImageView) d.findViewById(R.id.fitdialogimg);
                      TextView fittxt = (TextView) d.findViewById(R.id.fitdialogdesc);
                      d.show();
                        break;

                    case "maternity" :
                       d = new Dialog(CameraActivity.this);
                       d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                       d.setContentView(R.layout.maternitydialog);
                       ImageView matimg = (ImageView) d.findViewById(R.id.matdialogimg);
                       TextView mattxt = (TextView) d.findViewById(R.id.matdialogdesc);
                       d.show();
                        break;

                    case "child":
                        d = new Dialog(CameraActivity.this);
                        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        d.setContentView(R.layout.childdialog);
                       ImageView chilimg = (ImageView) d.findViewById(R.id.childialogimg);
                       TextView chiltxt = (TextView) d.findViewById(R.id.childialogdesc);
                       d.show();
                        break;

                }

            }
        });

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rotateImage(setReducedImageSize());
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
