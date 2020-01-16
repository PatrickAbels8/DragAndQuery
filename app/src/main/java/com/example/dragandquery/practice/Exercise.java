package com.example.dragandquery.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dragandquery.R;
import com.example.dragandquery.block.BlockT;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO:
 */

public class Exercise extends AppCompatActivity implements Fragment_Table_Ex.Fragment_Table_Ex_Listener, Fragment_Query_Ex.Fragment_Query_Ex_Listener, Fragment_Blocks_Ex.Fragment_Blocks_Ex_Listener{

    //vars
    int ex_id;
    public static final String ID_KEY = "com.example.dragandquery.practice.Exercise.ID_KEY";

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
    public void onGo(String query) {
        fragQuery.goInclickable();
        fragBlocks.goInvisible();
        fragTable.goVisible(query);
    }

    @Override
    public void onRetry() {
        fragQuery.goClickable();
        fragBlocks.goVisible();
        fragTable.goInvisible();
    }
}
