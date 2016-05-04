package com.sliit.dailyselfie.NavigationItems;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sliit.dailyselfie.DB.DBHelper;
import com.sliit.dailyselfie.R;
import com.sliit.dailyselfie.TimeLine.TimeLine;

import java.util.ArrayList;
import java.util.List;

public class MyChallegesActivity extends AppCompatActivity {

    ArrayList<String> AL;
    ArrayAdapter<String> AD;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challeges);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AL = new ArrayList<String>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT name,type FROM challanges WHERE userId=1";
        Cursor results = db.rawQuery(sql, null);

        while(results.moveToNext()){
            String cName = results.getString(0);
            //String cType = results.getString(1);
            AL.add(cName);
        }

        lv = (ListView)findViewById(R.id.MyChallengeslistView);
        AD = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AL);
        lv.setAdapter(AD);

        AdapterView.OnItemClickListener handler = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)lv.getItemAtPosition(position);

                startActivity(new Intent(getApplicationContext(),TimeLine.class));

                SharedPreferences challangeDetails = getSharedPreferences("challangeDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = challangeDetails.edit();
                editor.putString("challangeID", value);
                editor.apply();

            }

        };

        lv.setOnItemClickListener(handler);

    }

}
