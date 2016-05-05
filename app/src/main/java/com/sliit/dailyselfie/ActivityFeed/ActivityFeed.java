package com.sliit.dailyselfie.ActivityFeed;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.sliit.dailyselfie.Camera.CameraActivity;
import com.sliit.dailyselfie.R;

import java.util.ArrayList;

public class ActivityFeed extends AppCompatActivity {

    BottomBar mBottomBar;
    ArrayList<SharePost>  SharedPosts = new ArrayList<>();;
    RecyclerView RV;
    AdapterAC adapterAC;
    Firebase fire;
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RV= (RecyclerView)findViewById(R.id.recycler1);
        RV.setLayoutManager(new LinearLayoutManager(this));
        Firebase.setAndroidContext(this);

        fire=new Firebase("https://dailyselfie.firebaseio.com/sharedpost");

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noNavBarGoodness();

            // this.RefreshData();

        mBottomBar.setItemsFromMenu(R.menu.bottomba_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if (menuItemId == R.id.nav_home) {

                    Toast.makeText(ActivityFeed.this, "Timeline", Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.nav_fav) {
                    startActivity(new Intent(ActivityFeed.this, CameraActivity.class));

                } else if (menuItemId == R.id.nav_gallery) {
                    Toast.makeText(ActivityFeed.this, "Favorites", Toast.LENGTH_SHORT).show();

                }

                mBottomBar.mapColorForTab(0, ContextCompat.getColor(ActivityFeed.this, R.color.bottomPrimary));
                mBottomBar.mapColorForTab(1, ContextCompat.getColor(ActivityFeed.this, R.color.bottomPrimary));
                mBottomBar.mapColorForTab(2, ContextCompat.getColor(ActivityFeed.this, R.color.bottomPrimary));


            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
        this.RefreshData();
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    public void RetriveData(){

        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GetDataUpdates(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void GetDataUpdates(DataSnapshot dataSnapshot ){
         SharedPosts.clear();

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            SharePost sp = new SharePost();
            sp.setPostSharer(ds.getValue(SharePost.class).getPostSharer());
            sp.setPostType(ds.getValue(SharePost.class).getPostType());
            sp.setPostDescription(ds.getValue(SharePost.class).getPostDescription());
            sp.setPostImage(ds.getValue(SharePost.class).getPostImage());
            sp.setPostedTime(ds.getValue(SharePost.class).getPostedTime());



            SharedPosts.add(sp);

        }

         if(SharedPosts.size()>0){
                adapterAC = new AdapterAC(ActivityFeed.this,SharedPosts);
                RV.setAdapter(adapterAC);

        }else{
             Toast.makeText(ActivityFeed.this, "No data available", Toast.LENGTH_SHORT).show();
         }

    }

    public void RefreshData(){

        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GetDataUpdates(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    public  void save(String name,String type,String description,String date){

        SharePost sp = new SharePost();
        sp.setPostSharer(name);
        sp.setPostType(type);
        sp.setPostDescription(description);
        sp.setPostedTime(date);
        fire.push().setValue(sp);
        d.dismiss();


    }

    public  void showD(){
        d= new Dialog(ActivityFeed.this);
        d.setTitle("Save data");
        d.setContentView(R.layout.shr);
        final EditText name = (EditText)d.findViewById(R.id.editText);
        final EditText type = (EditText)d.findViewById(R.id.editText2);
        final EditText description = (EditText)d.findViewById(R.id.editText3);
        final EditText date = (EditText)d.findViewById(R.id.editText4);
        Button b = (Button)d.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(name.getText().toString(),type.getText().toString(),description.getText().toString(),date.getText().toString());
                name.setText("");
                type.setText("");
                description.setText("");
                date.setText("");
            }
        });
        d.show();
    }

}
