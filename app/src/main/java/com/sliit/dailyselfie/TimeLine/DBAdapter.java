package com.sliit.dailyselfie.TimeLine;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sliit.dailyselfie.DB.DBHelper;

/**
 * Created by Tharaka on 01/05/2016.
 */
public class DBAdapter {

    Context c;
    SQLiteDatabase db;
    DBHelper  dbhelper;
    String chalName;

    public DBAdapter(Context c) {
        this.c = c;
        dbhelper=new DBHelper(c);
    }

   public DBAdapter openDB(){
        try{
               db=dbhelper.getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
       return this;
   }


    public void closeDB(){
        try{
        dbhelper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //return this;
    }

    public void getChalName(String name){
        chalName = name;
    }

    public Cursor getall(){

        String [] colomns = {"id","description","height","weight","waistSize","created_at","image","challangeName"};
        return db.query("posts",colomns,"challangeName=?", new String[] { chalName } ,null,null,"created_at"+" DESC",null);
    }

}
