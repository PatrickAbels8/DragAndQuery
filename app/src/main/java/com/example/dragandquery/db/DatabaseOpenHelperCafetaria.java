package com.example.dragandquery.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelperCafetaria extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Schule2.db"; // todo change when available
    private static final String DATABASE = "Cafetaria";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelperCafetaria(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onUpgrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE);
        onCreate(db);
    }*/
}
