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
 * - better: send same arguments to everyone, they calc their info on that arguments
 */

public class Exercise extends AppCompatActivity implements Fragment_Table_Ex.Fragment_Table_Ex_Listener, Fragment_Query_Ex.Fragment_Query_Ex_Listener, Fragment_Blocks_Ex.Fragment_Blocks_Ex_Listener{

    //vars
    int ex_id;
    public static final String ARGS_KEY = "com.example.dragandquery.practice.Exercise.ARGS_KEY";
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

        //notify blocks wich one to show
        Bundle blocks_args = new Bundle();
        blocks_args.putStringArrayList(ARGS_KEY, getBlockStrings());
        fragBlocks.setArguments(blocks_args);

        Bundle query_args = new Bundle();
        query_args.putInt(ID_KEY, ex_id);
        fragQuery.setArguments(query_args);
        fragTable.setArguments(query_args);

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

    public ArrayList<String> getBlockStrings(){
        //todo depending on ex id return blocks to choose
        ArrayList<String> blocks = new ArrayList<>();
        blocks.add(BlockT.SELECT.getName());
        blocks.add(BlockT.ATTRIBUTE.getName());
        return blocks;
    }
}
