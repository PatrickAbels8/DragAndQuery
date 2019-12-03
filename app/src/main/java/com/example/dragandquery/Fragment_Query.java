package com.example.dragandquery;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.block.Block;
import com.example.dragandquery.block.BlockFactory;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * - appearence depending on view pos
 * - show code (via button click)
 */

public class Fragment_Query extends Fragment {

    //coms
    private RelativeLayout rl_query;
    private Button btn_go;
    private ImageButton btn_clear;

    //vars
    private Fragment_Query_Listener listener;
    private List<ImageView> blocks_in_rl;
    public Context context;

    //interface
    public interface Fragment_Query_Listener{
        void onGo(CharSequence query);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_query, container, false);

        //init coms
        rl_query = (RelativeLayout) v.findViewById(R.id.frag_query);
        btn_go = (Button) v.findViewById(R.id.frag_go);
        btn_clear = (ImageButton) v.findViewById(R.id.frag_clear);
        blocks_in_rl = new ArrayList<>();
        context = getContext();

        //send query to db
        btn_go.setOnClickListener(view -> {
            //TODO parser
            CharSequence query = interpret();
            listener.onGo(query);
        });

        //remove all blocks
        btn_clear.setOnLongClickListener(view -> {
            //TODO ask again in popup
            for (int i=0; i<blocks_in_rl.size(); i++){
                rl_query.removeView(blocks_in_rl.get(i));
            }
            blocks_in_rl.clear();
            return false;
        });

        //remove block
        btn_clear.setOnDragListener((view, dragEvent) -> {
            int dragID = dragEvent.getAction();
            switch(dragID) {
                //what if night enters d4
                case DragEvent.ACTION_DRAG_ENTERED:
                    btn_clear.setHovered(true); //todo animate bin opened
                    break;
                //what if night exited d4
                case DragEvent.ACTION_DRAG_EXITED:
                    btn_clear.setHovered(false); //todo animate bin closed
                    break;
                //what if night is finally dropped on d4
                case DragEvent.ACTION_DROP:
                    Object o = dragEvent.getLocalState();
                    if(o instanceof ImageView) {
                        ImageView draggedView = (ImageView) o;
                        rl_query.removeView(draggedView);
                        blocks_in_rl.remove(draggedView);
                    }
                    break;
            }
            return true;
        });

        //drop mode: layout
        rl_query.setOnDragListener((view, dragEvent) -> {
            int dragID = dragEvent.getAction();
            switch(dragID) {
                //what if night enters d4
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                //what if night exited d4
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                //what if night is finally dropped on d4
                case DragEvent.ACTION_DROP:
                    Object o = dragEvent.getLocalState();
                    if(o instanceof ImageView) {
                        ImageView draggedView = (ImageView) o;
                        float margin = 50; //todo
                        draggedView.setX(dragEvent.getX()-margin);
                        draggedView.setY(dragEvent.getY()-margin);
                        draggedView.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        });

        return v;
    }

    //create new block when pressed on fragment_blocks
    @SuppressLint("ClickableViewAccessibility")
    public void createView(View view, float x, float y){
        //make a new iv at (x,y)
        Block block = null;

        /***
         * !!!!!!!!!!!! EVERY BLOCK HAS TO MANUEL BE CHOSEN HERE!!!!!!!!!!!
         */
        switch(((Block) view.getTag()).getDesign()){
            case R.drawable.select_block:
                block = BlockFactory.getInstance().SELECT;
                break;
            case R.drawable.star_block:
                block = BlockFactory.getInstance().STAR;
                break;
            case R.drawable.where_block:
                block = BlockFactory.getInstance().WHERE;
                break;
        }
        ImageView cur_view = block.createView(context);
        cur_view.setX(x);
        cur_view.setY(y);

        //add the new iv to rl and blocklist
        rl_query.addView(cur_view);
        blocks_in_rl.add(cur_view);

        //drag mode
        cur_view.setOnTouchListener(new Block.OnTouchListener());

        //drop mode: other block
        cur_view.setOnDragListener(new Block.OnDragListener());
    }

    public void goInclickable(){
        for(ImageView iv: blocks_in_rl){
            iv.setClickable(false);
        }
        btn_go.setVisibility(View.INVISIBLE);
        btn_clear.setVisibility(View.INVISIBLE);
    }


    public void goClickable(){
        for(ImageView iv: blocks_in_rl){
            iv.setClickable(true);
        }
        btn_go.setVisibility(View.VISIBLE);
        btn_clear.setVisibility(View.VISIBLE);
    }

    //TODO
    public CharSequence interpret(){
        return "SomeString";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Query_Listener){
            listener = (Fragment_Query_Listener) context;
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
}
