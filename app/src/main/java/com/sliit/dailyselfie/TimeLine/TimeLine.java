package com.sliit.dailyselfie.TimeLine;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.sliit.dailyselfie.R;

public class TimeLine extends AppCompatActivity {

    String[] players;
    String[] names;
    int[] images;
    MyAdapter adapter;
    RecyclerView rv;
    Button b1;
    String challangeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);

        players= new String[]{"Opening Batsman", "Middle order Batsman", "Wicketkeeper Batsman", "Tailender"};
        names= new String[]{"Dilshan", "Mahela", "Sangakkara", "Kulesakara"};
        images= new int[]{R.drawable.dill, R.drawable.maiya, R.drawable.sanga, R.drawable.kule};



        rv=(RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter =new MyAdapter(this,players,names,images);
        rv.setAdapter(adapter);

        SharedPreferences challangeDetails = getSharedPreferences("challangeDetails", Context.MODE_PRIVATE);
        challangeName = challangeDetails.getString("challangeID","");
        Toast.makeText(getApplicationContext(),challangeName,Toast.LENGTH_SHORT).show();

    }

}
