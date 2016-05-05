package com.sliit.dailyselfie.ActivityFeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Tharaka on 05/05/2016.
 */
public class DownloadAsync extends AsyncTask<Void,Integer,Bitmap> {


    Context c;
    String url;
    RecyclerView RV;
    ProgressDialog PD;
    ImageView IV;

    public DownloadAsync(Context c, String url, ImageView holim) {
        this.c = c;
        this.url = url;
        this.IV = holim;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bmp =this.ImageConverter(url);
        return bmp;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PD=new ProgressDialog(c);
        PD.setTitle("Downloading");
        PD.setMessage("Please Wait...");
    }



    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        PD.dismiss();
        if(bitmap != null){
             IV.setImageBitmap(bitmap);
            Toast.makeText(c,"Loaded",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(c,"Internet Connection Issue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    public Bitmap ImageConverter(String Image){

        byte[] imageAsBytes = Base64.decode(Image, Base64.DEFAULT);
        Bitmap bmp1 = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bmp1;
    }

}
