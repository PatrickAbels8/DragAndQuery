package com.example.dragandquery.free;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.block.BlockView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * - padding on blocks
 */

public class Fragment_Blocks extends Fragment {


    //coms
    LinearLayout ll_blocks;
    LinearLayout ll_categories;
    Button[] categories; //0: Key, 1: DB, 2: Logic, 3: Agg
    List<BlockView> [] blocks_of_categories;

    EditText et;

    //vars
    private Fragment_Blocks_Listener listener;
    private boolean blocks_open;
    private int current_category_index;
    private Context context;

    //private float[] editPosition = new float[2];

    //interface
    public interface Fragment_Blocks_Listener{
        void onBlockDragged(View view, float x, float y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blocks, container, false);
        context = getContext();

        //init stuff
        ll_blocks = v.findViewById(R.id.ll_blocks);
        ll_categories = v.findViewById(R.id.ll_categories);
        categories = new Button[]{
                v.findViewById(R.id.block_category1),
                v.findViewById(R.id.block_category2),
                v.findViewById(R.id.block_category3),
                v.findViewById(R.id.block_category4)};
        blocks_open = false;
        blocks_of_categories = new ArrayList[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};


        // edit block
        et = new EditText(context);
        et.setBackgroundResource(R.drawable.empty_block);
        /*et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    editPosition[0] = motionEvent.getRawX()-(float)view.getWidth()/2;
                    editPosition[1] = motionEvent.getRawY()-(float)view.getHeight()/2;
                }
                return true;
            }
        });*/
        et.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String s = et.getText().toString();
                if(s.length() == 0){
                    Toast.makeText(context, "Text fehlt!", Toast.LENGTH_SHORT).show();
                }else{
                    showOrHideBlocks(null, -1);
                    float rawX = (float)Resources.getSystem().getDisplayMetrics().widthPixels/2-(float)view.getWidth()/2; //editPosition[0];
                    float rawY = (float)Resources.getSystem().getDisplayMetrics().heightPixels/2-(float)view.getHeight()/2;
                    BlockView edit_block = BlockT.EMPTY.createView(context, s);
                    listener.onBlockDragged(edit_block, rawX, rawY);
                    et.setText("");
                }
                return true;
            }
        });

        /***
         * !!!!!!!!!!!! EVERY BLOCK HAS TO MANUALLY BE ADDED HERE!!!!!!!!!!!
         */

        blocks_of_categories[0].addAll(Arrays.asList( //Key
                BlockT.SELECT.createView(context),
                BlockT.FROM.createView(context),
                BlockT.WHERE.createView(context),
                BlockT.ORDERBY.createView(context),
                BlockT.GROUPBY.createView(context),
                BlockT.LIMIT.createView(context),
                BlockT.SELECTDISTINCT.createView(context),
                BlockT.HAVING.createView(context)
        ));

        blocks_of_categories[1].addAll(Arrays.asList( //DB
                BlockT.AS.createView(context),
                BlockT.INNER_JOIN.createView(context),
                BlockT.LEFT_OUTER_JOIN.createView(context),
                BlockT.RIGHT_OUTER_JOIN.createView(context),
                BlockT.FULL_OUTER_JOIN.createView(context),
                BlockT.ON.createView(context)
        ));

        blocks_of_categories[2].addAll(Arrays.asList( //Logic
                BlockT.AND.createView(context),
                BlockT.NOT.createView(context),
                BlockT.IN.createView(context),
                BlockT.ISNULL.createView(context),
                BlockT.LIKE.createView(context),
                BlockT.GREATER.createView(context),
                BlockT.EQUAL.createView(context),
                BlockT.NEQUAL.createView(context),
                BlockT.OR.createView(context),
                BlockT.XOR.createView(context)
        ));

        blocks_of_categories[3].addAll(Arrays.asList( //Agg
                BlockT.COUNT.createView(context),
                BlockT.MIN.createView(context),
                BlockT.MAX.createView(context),
                BlockT.AVERAGE.createView(context),
                BlockT.SUM.createView(context)
        ));

        //open blocks when category iv is clicked
        for(int i=0; i<categories.length; i++){
            categories[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = findIndex(categories, view);
                    showOrHideBlocks(blocks_of_categories[index], index);
                }
            });
        }

        //add block to query fragment and hide blocks when block iv is clicked
        for(int i=0; i<categories.length; i++){
            for(BlockView iv: blocks_of_categories[i]){
                iv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                            showOrHideBlocks(null, -1);
                            float rawX = motionEvent.getRawX()-(float)view.getWidth()/2;
                            float rawY = motionEvent.getRawY()-(float)view.getHeight()/2;
                            listener.onBlockDragged(view, rawX, rawY);
                        }
                        return true;
                    }
                });
            }
        }
        return v;
    }

    //open ll verti by adding all blocks / close it b removing all views of category x
    public void showOrHideBlocks(List<BlockView> blocks_to_show, int index){
        if(!blocks_open || //no cat opened yet
                index>-1&&current_category_index!=index){ //another cat was already opened
            if(index>-1&&current_category_index!=index)
                ll_blocks.removeAllViews();
            et.setPadding(dp_to_int(15), dp_to_int(10), dp_to_int(15), dp_to_int(10));
            LinearLayout.LayoutParams lp_eB = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_eB.setMargins(0, dp_to_int(8), 0, dp_to_int(8));
            ll_blocks.addView(et, lp_eB);
            for(int i=0; i<blocks_to_show.size(); i++){
                LinearLayout.LayoutParams lp_mB = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_mB.setMargins(0, 0, 0, dp_to_int(8));
                BlockView bv = blocks_to_show.get(i);
                ll_blocks.addView(bv, lp_mB);
            }
            blocks_open = true;
            current_category_index = index;
            changeActiveCats(index);
        }else{ //same clicked as openend
            ll_blocks.removeAllViews();
            blocks_open = false;
            changeActiveCats(-1);
        }
    }

    public void changeActiveCats(int index){
        for(int i=0; i<4; i++)
            categories[i].setBackgroundResource(R.drawable.cat_block_inactive);
        ll_blocks.setBackgroundColor(getResources().getColor(R.color.transparent));

        switch(index){
            case 0:
                categories[0].setBackgroundResource(R.drawable.cat1_block_active);
                ll_blocks.setBackgroundResource(R.drawable.cat1_border);
                break;
            case 1:
                categories[1].setBackgroundResource(R.drawable.cat2_block_active);
                ll_blocks.setBackgroundResource(R.drawable.cat2_border);
                break;
            case 2:
                categories[2].setBackgroundResource(R.drawable.cat3_block_active);
                ll_blocks.setBackgroundResource(R.drawable.cat3_border);
                break;
            case 3:
                categories[3].setBackgroundResource(R.drawable.cat4_block_active);
                ll_blocks.setBackgroundResource(R.drawable.cat4_border);
                break;
            default:
                break;
        }
    }

    public void goInvisible(){
        ll_blocks.setVisibility(View.GONE);
    }

    public void goVisible(){
        ll_blocks.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Blocks_Listener){
            listener = (Fragment_Blocks_Listener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public int findIndex(Button[] list, View element){
        for(int i=0; i<list.length; i++){
            if(list[i].getId()==element.getId()){
                return i;
            }
        }
        return -1;
    }

    //helper
    public int dp_to_int(int dp){
        float scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp*scale+0.5f);
        return pix;
    }
}
