package com.example.dragandquery.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if(instance==null){
            instance = new DatabaseAccess(context);
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
                    for(int i=0; i<num_cols; i++)
                        row[i] = c.getString(i);
                    data.add(row);
                }while(c.moveToNext());
            }
        }

        return data;
    }
}
