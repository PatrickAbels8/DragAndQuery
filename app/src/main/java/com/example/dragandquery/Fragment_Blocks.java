package com.example.dragandquery;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * - categories: logic, db, ...
 * - classes for every block and category
 * - when cat1 open and cat2 clicked, dont close cat1 but open cat2
 */

public class Fragment_Blocks extends Fragment {

    //coms
    LinearLayout ll_blocks;
    LinearLayout ll_categories;
    ImageView [] categories;
    List<ImageView> [] blocks_of_categories;

    //vars
    private Fragment_Blocks_Listener listener;
    private boolean blocks_open;
    private int current_category_index;

    //interface
    public interface Fragment_Blocks_Listener{
        void onBlockDragged(View view, int x, int y);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blocks, container, false);

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

        //testing
        ImageView select = new ImageView(getContext());
        select.setImageResource(R.drawable.buttonthreedee);
        select.setTag(R.drawable.buttonthreedee);
        blocks_of_categories[0].add(select);
        ImageView and = new ImageView(getContext());
        and.setImageResource(R.drawable.select_block);
        and.setTag(R.drawable.select_block);
        blocks_of_categories[0].add(and);
        ImageView students = new ImageView(getContext());
        students.setImageResource(R.drawable.where_block);
        students.setTag(R.drawable.where_block);
        blocks_of_categories[1].add(students);
        ImageView from = new ImageView(getContext());
        from.setImageResource(R.drawable.star_block);
        from.setTag(R.drawable.star_block);
        blocks_of_categories[1].add(from);
        ImageView star = new ImageView(getContext());
        star.setImageResource(R.drawable.select_block);
        star.setTag(R.drawable.select_block);
        blocks_of_categories[1].add(star);


        //drag
        /*for(ImageView b: blocks){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //shadow stuff
                    /*ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    shadow.getView().setBackgroundResource(R.color.invisible);
                    view.startDrag(data, shadow, view, 0);

                    //send to frag
                    listener.onBlockDragged(view);
                }
            });
        }*/

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
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showOrHideBlocks(null, -1);
                        int[] location = new int[2];
                        view.getLocationOnScreen(location);
                        listener.onBlockDragged(view, location[0], location[1]);
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
