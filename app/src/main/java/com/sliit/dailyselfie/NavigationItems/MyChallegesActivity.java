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

    ArrayList<String> AL,AL1,AL2,AL3,AL4,AL5,AL6,AL7,AL8;
    //ArrayList<String> ;
    ArrayAdapter<String> AD;
    ListView lv;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challeges);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences userDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userID = userDetails.getString("loggedUserId","");
        int userId = Integer.parseInt(userID);

        AL = new ArrayList<String>();
        AL2 = new ArrayList<String>();
        AL3 = new ArrayList<String>();
        AL4 = new ArrayList<String>();
        AL5 = new ArrayList<String>();
        AL6 = new ArrayList<String>();
        AL7 = new ArrayList<String>();
        AL8 = new ArrayList<String>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT name,type,id,description,fitCategory,height,weight,waistSize,targetWeight,targetWaistSize FROM challanges WHERE userId='"+userId+"'";
        Cursor results = db.rawQuery(sql, null);

        while(results.moveToNext()){
            String cName = results.getString(0);
            String cType = results.getString(1);
            //String cId = results.getString(2);
            String cDesc = results.getString(3);
            String cFitCat = results.getString(4);
            String cHeight = results.getString(5);
            String cWeight = results.getString(6);
            String cWaist = results.getString(7);
            String ctarWeight = results.getString(8);
            String ctarWaist = results.getString(9);
            AL.add(cName);
            AL1.add(cType);
            AL2.add(cDesc);
            AL3.add(cFitCat);
            AL4.add(cHeight);
            AL5.add(cWeight);
            AL6.add(cWaist);
            AL7.add(ctarWeight);
            AL8.add(ctarWaist);
        }

        lv = (ListView)findViewById(R.id.MyChallengeslistView);
        AD = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AL);
        lv.setAdapter(AD);

        AdapterView.OnItemClickListener handler = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)lv.getItemAtPosition(position);
                String type = AL1.get(position);
                String decs = AL2.get(position);
                String fitCat = AL3.get(position);
                String height = AL4.get(position);
                String weight = AL5.get(position);
                String waist = AL6.get(position);
                String tarweight = AL7.get(position);
                String tarwaist = AL8.get(position);

                SharedPreferences cDetails = getSharedPreferences("cDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = cDetails.edit();
                editor1.putString("chName", value);
                editor1.putString("chType", type);
                editor1.putString("chDesc", decs);
                editor1.putString("cFitCat",fitCat);
                editor1.putString("chHeight", height);
                editor1.putString("chWeight", weight);
                editor1.putString("chWaist", waist);
                editor1.putString("chtarWeight", tarweight);
                editor1.putString("chtarWaist", tarwaist);
                editor1.apply();
                Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), TimeLine.class));
            }

        };

        lv.setOnItemClickListener(handler);

    }

}
