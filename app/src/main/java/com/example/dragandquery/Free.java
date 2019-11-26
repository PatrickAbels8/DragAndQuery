package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/***
 * TODO
 * - change to table frag after go
 * - table frag needs try again button (or query button when go pressed visible)
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

        //remove title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
    public void onBlockDragged(View view, int x, int y) {
        fragQuery.createView(view, x, y);
    }

    @Override
    public void onGo(CharSequence query) {
        fragQuery.goInclickable();
        fragBlocks.goInvisible();
        fragTable.goVisible();
    }

    @Override
    public void onRetry() {
        fragQuery.goClickable();
        fragBlocks.goVisible();
        fragTable.goInvisible();
    }
}
