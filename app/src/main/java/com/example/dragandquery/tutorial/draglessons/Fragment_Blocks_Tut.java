package com.example.dragandquery.tutorial.draglessons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.block.BlockView;

import java.util.ArrayList;
import java.util.List;

/***
 * -
 */

public class Fragment_Blocks_Tut extends Fragment {

    //coms
    LinearLayout ll_blocks;
    LinearLayout ll_categories;
    Button[] categories; //0: DB
                             //1: Logic
                             //2: KeyWords
                             //3: Others
    List<BlockView> [] blocks_of_categories;

    EditText et;

    //vars
    private Fragment_Blocks_Tut_Listener listener;
    private boolean blocks_open;
    private int current_category_index;
    private Context context;

    //interface
    public interface Fragment_Blocks_Tut_Listener{
        void onBlockDragged(View view, float x, float y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blocks_tut, container, false);
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
        et.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String s = et.getText().toString();
                if(s.length() == 0){
                    Toast.makeText(context, "Text fehlt!", Toast.LENGTH_SHORT).show();
                }else{
                    showOrHideBlocks(null, -1);
                    float rawX = (float) Resources.getSystem().getDisplayMetrics().widthPixels/2-(float)view.getWidth()/2;
                    float rawY = (float) Resources.getSystem().getDisplayMetrics().heightPixels/2-(float)view.getHeight()/2;
                    BlockView edit_block = BlockT.EMPTY.createView(context, s);
                    listener.onBlockDragged(edit_block, rawX, rawY);
                    et.setText("");
                }
                return true;
            }
        });

        //match blocks to bundle
        Bundle args = this.getArguments();
        ArrayList<String> blocks = (ArrayList<String>)args.get(DragLesson.ARGS_KEY);
        for(int i=0; i<blocks.size(); i++){
            addBlock(blocks.get(i));
        }

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



    /***
     * !!!!!!!!!!!! EVERY BLOCK HAS TO MANUALLY BE ADDED HERE!!!!!!!!!!!
     */

    public void addBlock(String name){
        BlockT[] myblocks = BlockT.class.getEnumConstants();
        for(int i=0; i<myblocks.length; i++){
            if(name.equals(myblocks[i].getName())){
                if(myblocks[i].getCategory()==R.string.block_cat1){
                    blocks_of_categories[0].add(myblocks[i].createView(context));
                }else if(myblocks[i].getCategory()==R.string.block_cat2){
                    blocks_of_categories[1].add(myblocks[i].createView(context));
                }else if(myblocks[i].getCategory()==R.string.block_cat3){
                    blocks_of_categories[2].add(myblocks[i].createView(context));
                }else if(myblocks[i].getCategory()==R.string.block_cat4){
                    blocks_of_categories[3].add(myblocks[i].createView(context));
                }
                return;
            }
        }
        /*if(name.equals(BlockT.SELECT.getName())) //Key
            blocks_of_categories[0].add(BlockT.SELECT.createView(context));
        else if(name.equals(BlockT.FROM.getName()))
            blocks_of_categories[0].add(BlockT.FROM.createView(context));
        else if(name.equals(BlockT.WHERE.getName()))
            blocks_of_categories[0].add(BlockT.WHERE.createView(context));
        else if(name.equals(BlockT.ORDERBY.getName()))
            blocks_of_categories[0].add(BlockT.ORDERBY.createView(context));
        else if(name.equals(BlockT.GROUPBY.getName()))
            blocks_of_categories[0].add(BlockT.GROUPBY.createView(context));
        else if(name.equals(BlockT.LIMIT.getName()))
            blocks_of_categories[0].add(BlockT.LIMIT.createView(context));
        else if(name.equals(BlockT.DISTINCT.getName()))
            blocks_of_categories[0].add(BlockT.DISTINCT.createView(context));
        else if(name.equals(BlockT.HAVING.getName()))
            blocks_of_categories[0].add(BlockT.HAVING.createView(context));

        else if(name.equals(BlockT.AS.getName())) //DB
            blocks_of_categories[1].add(BlockT.AS.createView(context));
        else if(name.equals(BlockT.INNER_JOIN.getName())) //DB
            blocks_of_categories[1].add(BlockT.INNER_JOIN.createView(context));
        else if(name.equals(BlockT.LEFT_OUTER_JOIN.getName())) //DB
            blocks_of_categories[1].add(BlockT.LEFT_OUTER_JOIN.createView(context));
        else if(name.equals(BlockT.RIGHT_OUTER_JOIN.getName())) //DB
            blocks_of_categories[1].add(BlockT.RIGHT_OUTER_JOIN.createView(context));
        else if(name.equals(BlockT.FULL_OUTER_JOIN.getName())) //DB
            blocks_of_categories[1].add(BlockT.FULL_OUTER_JOIN.createView(context));
        else if(name.equals(BlockT.ON.getName())) //DB
            blocks_of_categories[1].add(BlockT.ON.createView(context));

        else if(name.equals(BlockT.AND.getName())) // Logic
            blocks_of_categories[2].add(BlockT.AND.createView(context));//müsste hier nicht überall 2 in den eckigen Klammer stehen?
        else if(name.equals(BlockT.NOT.getName()))
            blocks_of_categories[2].add(BlockT.NOT.createView(context));
        else if(name.equals(BlockT.IN.getName()))
            blocks_of_categories[2].add(BlockT.IN.createView(context));
        else if(name.equals(BlockT.ISNULL.getName()))
            blocks_of_categories[2].add(BlockT.ISNULL.createView(context));
        else if(name.equals(BlockT.LIKE.getName()))
            blocks_of_categories[2].add(BlockT.LIKE.createView(context));
        else if(name.equals(BlockT.GREATER.getName()))
            blocks_of_categories[2].add(BlockT.GREATER.createView(context));
        else if(name.equals(BlockT.SMALLER.getName()))
            blocks_of_categories[2].add(BlockT.SMALLER.createView(context));
        else if(name.equals(BlockT.EQUAL.getName()))
            blocks_of_categories[2].add(BlockT.EQUAL.createView(context));
        else if(name.equals(BlockT.NEQUAL.getName()))
            blocks_of_categories[2].add(BlockT.NEQUAL.createView(context));
        else if(name.equals(BlockT.OR.getName()))
            blocks_of_categories[2].add(BlockT.OR.createView(context));
        else if(name.equals(BlockT.XOR.getName()))
            blocks_of_categories[2].add(BlockT.XOR.createView(context));

        else if(name.equals(BlockT.COUNT.getName())) // Agg
            blocks_of_categories[3].add(BlockT.COUNT.createView(context));
        else if(name.equals(BlockT.MIN.getName()))
            blocks_of_categories[3].add(BlockT.MIN.createView(context));
        else if(name.equals(BlockT.MAX.getName()))
            blocks_of_categories[3].add(BlockT.MAX.createView(context));
        else if(name.equals(BlockT.AVERAGE.getName()))
            blocks_of_categories[3].add(BlockT.AVERAGE.createView(context));
        else if(name.equals(BlockT.SUM.getName()))
            blocks_of_categories[3].add(BlockT.SUM.createView(context));**/
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
            if(index == 1 || index == 2)
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
        if(context instanceof Fragment_Blocks_Tut_Listener){
            listener = (Fragment_Blocks_Tut_Listener) context;
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
