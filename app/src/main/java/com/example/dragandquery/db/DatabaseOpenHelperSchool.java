package com.example.dragandquery.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelperSchool extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Schule3.db";
    private static final String DATABASE = "Schule3";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelperSchool(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onUpgrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE);
        onCreate(db);
    }*/
}
