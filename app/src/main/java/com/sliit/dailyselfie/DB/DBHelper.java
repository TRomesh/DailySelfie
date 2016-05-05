package com.sliit.dailyselfie.DB;

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
            "CREATE TABLE register (id INTEGER PRIMARY KEY AUTOINCREMENT, fname VARCHAR(255), lname VARCHAR(255), email VARCHAR(255) NOT NULL UNIQUE,password VARCHAR(10), profilepic VARCHAR(255) )";

    public static String LOGIN_TABLE =
            "CREATE TABLE login (id INTEGER PRIMARY KEY AUTOINCREMENT,activatedStatus VARCHAR(50), userId INT, FOREIGN KEY (userId) REFERENCES register(id) )";

    public static String CHALLANGES_TABLE =
            "CREATE TABLE challanges (id INTEGER PRIMARY KEY AUTOINCREMENT, type VARCHAR(200), name VARCHAR(250) NOT NULL UNIQUE, description TEXT, fitCategory VARCHAR(100), height DOUBLE, weight DOUBLE, waistSize DOUBLE, targetWeight DOUBLE, targetWaistSize DOUBLE, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, userId INT, FOREIGN KEY (userId) REFERENCES register(id) )";

    public static String POST_TABLE =
            "CREATE TABLE posts (id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT, height DOUBLE, weight DOUBLE, waistSize DOUBLE, created_at DATETIME DEFAULT CURRENT_TIMESTAMP,image VARCHAR(500), challangeName VARCHAR(250), FOREIGN KEY (challangeName) REFERENCES challanges(name) )";


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(REGISTER_TABLE);
        db.execSQL(LOGIN_TABLE);
        db.execSQL(CHALLANGES_TABLE);
        db.execSQL(POST_TABLE);
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
