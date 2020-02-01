package com.example.dragandquery.free;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;
import com.example.dragandquery.block.Block;
import com.example.dragandquery.block.BlockFactory;
import com.example.dragandquery.block.BlockT;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * -
 */

public class Fragment_Blocks extends Fragment {

    //coms
    LinearLayout ll_blocks;
    LinearLayout ll_categories;
    ImageView [] categories; //0: DB
                             //1: Logic
                             //2: KeyWords
                             //3: Others
    List<ImageView> [] blocks_of_categories;

    //vars
    private Fragment_Blocks_Listener listener;
    private boolean blocks_open;
    private int current_category_index;
    private Context context;

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
        categories = new ImageView[]{
                v.findViewById(R.id.block_category1),
                v.findViewById(R.id.block_category2),
                v.findViewById(R.id.block_category3),
                v.findViewById(R.id.block_category4)};
        blocks_open = false;
        blocks_of_categories = new ArrayList[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};

        /***
         * !!!!!!!!!!!! EVERY BLOCK HAS TO MANUALLY BE ADDED HERE!!!!!!!!!!!
         */

        blocks_of_categories[0].add(BlockT.TABLE.createView(context));
        blocks_of_categories[0].add(BlockT.STAR.createView(context));
        blocks_of_categories[0].add(BlockT.ATTRIBUTE.createView(context));

        blocks_of_categories[2].add(BlockT.FROM.createView(context));
        blocks_of_categories[2].add(BlockT.WHERE.createView(context));
        blocks_of_categories[2].add(BlockT.SELECT.createView(context));

        //open blocks when category iv is clicked
        for(ImageView category: categories){
            category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = findIndex(categories, view);
                    showOrHideBlocks(blocks_of_categories[index], index);
                }
            });
        }

        //add block to query fragment and hide blocks when block iv is clicked (todo drag mode)
        for(int i=0; i<categories.length; i++){
            for(ImageView iv: blocks_of_categories[i]){
                iv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                            showOrHideBlocks(null, -1);
                            float rawX = motionEvent.getRawX();
                            float rawY = motionEvent.getRawY();
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
    public void showOrHideBlocks(List<ImageView> blocks_to_show, int index){
        if(!blocks_open){ //no cat opened yet
            for(ImageView iv: blocks_to_show){
                ll_blocks.addView(iv);
            }
            blocks_open = true;
            current_category_index = index;
        }else if(index>-1&&current_category_index!=index) { //another cat was already opened
            ll_blocks.removeAllViews();
            for(ImageView iv: blocks_to_show){
                ll_blocks.addView(iv);
            }
            blocks_open = true;
            current_category_index = index;
        }else{ //same clicked as openend
            ll_blocks.removeAllViews();
            blocks_open = false;
        }
    }

    public void goInvisible(){
        ll_blocks.setVisibility(View.INVISIBLE);
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

    public int findIndex(ImageView[] list, View element){
        for(int i=0; i<list.length; i++){
            if(list[i].getId()==element.getId()){
                return i;
            }
        }
        return -1;
    }
}
