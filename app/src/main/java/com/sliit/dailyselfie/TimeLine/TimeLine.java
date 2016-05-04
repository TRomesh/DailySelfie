package com.sliit.dailyselfie.TimeLine;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.sliit.dailyselfie.R;

import java.util.ArrayList;

public class TimeLine extends AppCompatActivity {

    MyAdapter adapter;
    RecyclerView rv;
    String challangeName;
    ArrayList<Posts> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);

        rv=(RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter =new MyAdapter(this,posts);

        SharedPreferences challangeDetails = getSharedPreferences("challangeDetails", Context.MODE_PRIVATE);
        challangeName = challangeDetails.getString("challangeID","");

        showPost();
    }

    public void showPost(){

        posts.clear();

        DBAdapter db =  new DBAdapter(this);
        db.openDB();

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

            if(name == challangeName){
                Posts p = new Posts(id,des,height,weight,waist,time,picpath,name);
                posts.add(p);
            }

            if(posts.size() < 1){
                rv.setAdapter(adapter);
            }

        }
    }
}
