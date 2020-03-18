package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.dragandquery.free.Fragment_Blocks;
import com.example.dragandquery.free.Fragment_Query;
import com.example.dragandquery.free.Fragment_Table;

import java.util.List;

/***
 * TODO
 */

//top: query fragment
//bottom: blocks fragment / table fragment
public class Free extends AppCompatActivity implements Fragment_Table.Fragment_Table_Listener, Fragment_Query.Fragment_Query_Listener, Fragment_Blocks.Fragment_Blocks_Listener{

    //fragments
    Fragment_Query fragQuery;
    Fragment_Blocks fragBlocks;
    Fragment_Table fragTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free);

        fragQuery = new Fragment_Query();
        fragBlocks = new Fragment_Blocks();
        fragTable = new Fragment_Table();

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
    }

    @Override
    public void onRetry() {
        fragQuery.goClickable();
        fragBlocks.goVisible();
        fragTable.goInvisible();
    }
}
