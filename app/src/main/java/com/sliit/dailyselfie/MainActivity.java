package com.sliit.dailyselfie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sliit.dailyselfie.ActivityFeed.ActivityFeed;
import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.Challenges.AddChallangeActivity;
import com.sliit.dailyselfie.Challenges.ChildGrowthActivity;
import com.sliit.dailyselfie.Challenges.FitnessActivity;
import com.sliit.dailyselfie.Challenges.MaternityActivity;
import com.sliit.dailyselfie.Challenges.NoshaveActivity;
import com.sliit.dailyselfie.Challenges.PostMaternityActivity;
import com.sliit.dailyselfie.NavigationItems.AboutUs;
import com.sliit.dailyselfie.NavigationItems.MyChallegesActivity;
import com.sliit.dailyselfie.NavigationItems.SettingsActivity;
import com.sliit.dailyselfie.Start.LoginActivity;
import com.sliit.dailyselfie.TimeLine.TimeLine;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    boolean doubleBackToExitPressedOnce = false;
    String userID,userFName,userEmail,userpropic,userLName;
    ImageView PIV;
    TextView Pname,Pemail;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userID = userDetails.getString("loggedUserId","");
        userFName = userDetails.getString("loggedUserfname","");
        userLName = userDetails.getString("loggedUserlname","");
        userEmail = userDetails.getString("loggedUseremail","");
        userpropic = userDetails.getString("loggedUserpropic","");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddChallangeActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        PIV=(ImageView)header.findViewById(R.id.profile_image);
        Pname = (TextView)header.findViewById(R.id.profile_name);
        Pemail = (TextView)header.findViewById(R.id.profile_email);
        Pname.setText(userFName);
        Pemail.setText(userEmail);
        Glide.with(this).load(userpropic).into(PIV);


        findViewById(R.id.button_fitness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FitnessActivity.class));
            }
        });


        findViewById(R.id.button_noshave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NoshaveActivity.class));
            }
        });

        findViewById(R.id.button_maternity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MaternityActivity.class));
            }
        });

        findViewById(R.id.button_childgrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChildGrowthActivity.class));
            }
        });

        findViewById(R.id.button_diary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PostMaternityActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            startActivity(new Intent(MainActivity.this,ActivityFeed.class));

        } else if (id == R.id.nav_slideshow) {

            startActivity(new Intent(MainActivity.this,MyChallegesActivity.class));

        } else if (id == R.id.nav_settings) {

            startActivity(new Intent(MainActivity.this,SettingsActivity.class));

        } else if (id == R.id.nav_aboutus) {

            startActivity(new Intent(MainActivity.this,AboutUs.class));

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_exit) {

            SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = userDetails.edit();
            editor.putString("loggedUserId", "");
            editor.apply();

            SharedPreferences cDetails = getSharedPreferences("cDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = cDetails.edit();
            editor1.putString("chName", "");
            editor1.apply();

            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            findViewById(R.id.activity_MainMenu).setPadding(72, 0, 72, -250);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            findViewById(R.id.activity_MainMenu).setPadding(16, 16, 16, 16);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        PIV=(ImageView)header.findViewById(R.id.profile_image);
        Pname = (TextView)header.findViewById(R.id.profile_name);
        Pemail = (TextView)header.findViewById(R.id.profile_email);
        Pname.setText(userFName);
        Pemail.setText(userEmail);
        Glide.with(this).load(userpropic).into(PIV);

    }


}
