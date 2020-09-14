package com.example.dragandquery.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    public final static String DB_SCHOOL = "School";
    public final static String DB_CAFETARIA = "Cafetaria";


    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context, String db_name){
        if(db_name.equals(DB_SCHOOL))
            this.openHelper = new DatabaseOpenHelperSchool(context);
        else if (db_name.equals(DB_CAFETARIA))
            this.openHelper = new DatabaseOpenHelperCafetaria(context);
    }


    public static DatabaseAccess getInstance(Context context, String db_name){
        if(instance==null){
            instance = new DatabaseAccess(context, db_name);
        }
        return instance;
    }

    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    public void close(){
        if(db != null){
            this.db.close();
        }
    }

    public List<String[]> query(String query_string){
        List<String[]> data = new ArrayList<>();
        c = db.rawQuery(query_string, new String[]{});
        int num_rows = c.getCount();
        int num_cols = c.getColumnCount();
        String[] col_names = c.getColumnNames();

        data.add(col_names);
        if(c != null){
            if(c.moveToFirst()){
                do{
                    String[] row = new String[num_cols];
                    for(int i=0; i<num_cols; i++) {
                        row[i] = c.getString(i);
                    }
                    data.add(row);
                }while(c.moveToNext());
            }
        }

        return data;
    }
}
