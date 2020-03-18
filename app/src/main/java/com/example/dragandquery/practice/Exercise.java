package com.example.dragandquery.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.dragandquery.R;
import com.example.dragandquery.block.BlockT;

import java.util.ArrayList;
import java.util.List;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO:
 * -stars not centered
 * - wait 24h before try again
 */

//exp: "02001203"
public class Exercise extends AppCompatActivity implements Fragment_Table_Ex.Fragment_Table_Ex_Listener, Fragment_Query_Ex.Fragment_Query_Ex_Listener, Fragment_Blocks_Ex.Fragment_Blocks_Ex_Listener{

    //vars
    int ex_id;
    public static final String ID_KEY = "com.example.dragandquery.practice.Exercise.ID_KEY";
    public static final int STAR_0 = 0;
    public static final int STAR_1 = 1;
    public static final int STAR_2 = 2;
    public static final int STAR_3 = 3;

    //fragments
    Fragment_Query_Ex fragQuery;
    Fragment_Blocks_Ex fragBlocks;
    Fragment_Table_Ex fragTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //choose content match ex id 0-(n-1)+100/200/300
        Intent intent = getIntent();
        if(intent.hasExtra(Practices.EX_ID)){
            ex_id = intent.getIntExtra(Practices.EX_ID, 100);
            Log.d("#######", Integer.toString(ex_id));
        }

        fragQuery = new Fragment_Query_Ex();
        fragBlocks = new Fragment_Blocks_Ex();
        fragTable = new Fragment_Table_Ex();

        //frags need to know the lec id
        Bundle args = new Bundle();
        args.putInt(ID_KEY, ex_id);
        fragBlocks.setArguments(args);
        fragQuery.setArguments(args);
        fragTable.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_query, fragQuery)
                .replace(R.id.container_blocks, fragBlocks)
                .replace(R.id.container_table, fragTable)
                .commit();
    }

    @Override
    public void onBlockDragged(View view, float x, float y) {
        fragQuery.createView(view, x, y);
    }

    @Override
    public void onGo(String query, List<String[]> response) {
        fragQuery.goInclickable();
        fragBlocks.goInvisible();
        fragTable.goVisible(query, response);

        int isCorrect = fragTable.isCorrect(query);
        if(isCorrect!=STAR_0){
            int index = ex_id%100;
            String key = ex_id/100==1? getString(R.string.prac_easy_key): ex_id/100==2? getString(R.string.prac_medium_key): getString(R.string.prac_hard_key);
            String defalut = ex_id/100==1? "000000": ex_id/100==2? "00000000": "000000000000";
            String oldData = loadData(key, defalut);
            String newData = oldData.substring(0, index)+Integer.toString(isCorrect)+oldData.substring(index+1);
            Log.d("#########", oldData);
            Log.d("#########", newData);
            saveData(key, newData);
        }

    }

    @Override
    public void onRetry() {
        fragQuery.goClickable();
        fragBlocks.goVisible();
        fragTable.goInvisible();
    }

    //key value store
    public void saveData(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
    }

    //key value store
    public String loadData(String key,  String default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String data = sharedPref.getString(key, default_value);
        return data;
    }
}
