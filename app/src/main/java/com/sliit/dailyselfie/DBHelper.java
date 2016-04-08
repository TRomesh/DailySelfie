package com.sliit.dailyselfie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tharaka on 08/04/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "dailyselfie";
    public static int DB_VERSION  = 1;


    public static String REGISTER_TABLE =
            "CREATE TABLE register (id INTEGER PRIMARY KEY AUTOINCREMENT, fname VARCHAR(255), lname VARCHAR(255), email VARCHAR(255), profilepic VARCHAR(255) )";


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(REGISTER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion==1 && newVersion==2){
          //  db.execSQL();  if new table is to be added in futre
        }else{

           // db.execSQL("DROP TABLE IF EXISTS register ");


        }
    }
}
