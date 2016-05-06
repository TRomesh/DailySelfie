package com.sliit.dailyselfie.TimeLine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.R;

import java.util.ArrayList;

public class TimeLine extends AppCompatActivity {

    MyAdapter adapter;
    RecyclerView rv;
    String chalName,chalType,userFName;
    ArrayList<Posts> posts = new ArrayList<>();
    Firebase fire;
    TextView challangename;
    BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noNavBarGoodness();
        Firebase.setAndroidContext(this);
        fire=new Firebase("https://dailyselfie.firebaseio.com/sharedpost");

        SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userFName = userDetails.getString("loggedUserfname","");

        SharedPreferences cDetails = getSharedPreferences("cDetails", Context.MODE_PRIVATE);
        chalName = cDetails.getString("chName","");
        chalType = cDetails.getString("chType","");

        mBottomBar.setItemsFromMenu(R.menu.bottomba_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if (menuItemId == R.id.nav_home) {

                    //Toast.makeText(TimeLine.this, "Timeline", Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.nav_fav) {
                    startActivity(new Intent(TimeLine.this, CameraActivity.class).putExtra("Challenge","fitness"));

                } else if (menuItemId == R.id.nav_gallery) {
                    //Toast.makeText(TimeLine.this, "Favorites", Toast.LENGTH_SHORT).show();

                }

                mBottomBar.mapColorForTab(0, ContextCompat.getColor(TimeLine.this, R.color.bottomPrimary));
                mBottomBar.mapColorForTab(1, ContextCompat.getColor(TimeLine.this, R.color.bottomPrimary));
                mBottomBar.mapColorForTab(2, ContextCompat.getColor(TimeLine.this, R.color.bottomPrimary));


            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        rv=(RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());


        challangename = (TextView) findViewById(R.id.challangeType);
        challangename.setText(chalName);


        adapter =new MyAdapter(this,posts,userFName);
        showPost();



    }

    public void showPost(){

        posts.clear();

        DBAdapter db =  new DBAdapter(this);
        db.openDB();
        db.getChalName(chalName);

        Cursor c = db.getall();


        while(c.moveToNext()){
            int id = c.getInt(0);
            String des = c.getString(1);
            double height = c.getDouble(2);
            double weight = c.getDouble(3);
            double waist = c.getDouble(4);
            String time = c.getString(5);
            String picpath = c.getString(6);
            String name = c.getString(7);

                Posts p = new Posts(id, des, height, weight, waist, time, picpath, name);

                posts.add(p);

            if(posts.size()>0){
                rv.setAdapter(adapter);
            }
        }
    }
}
