package com.sliit.dailyselfie.NavigationItems;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.sliit.dailyselfie.R;

public class AboutUs extends AppCompatActivity {

    ImageView IV1,IV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IV1=(ImageView)findViewById(R.id.aboutgif);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(IV1);
        Glide.with(this).load(R.drawable.daysel).into(imageViewTarget);

    }

}
