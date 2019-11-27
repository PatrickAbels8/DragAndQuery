package com.example.dragandquery;

import android.annotation.TargetApi;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.block.BlockSelect;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * - garbage collector (one view ok, several views bug, drag bug)
 * - appearence depending on view pos
 * - putting pieces together, before need to specify types of blocks in own classes maybe
 * - show code (via button click)
 */

public class Fragment_Query extends Fragment {

    //coms
    private RelativeLayout rl_query;
    private Button btn_go;
    private ImageButton btn_clear;
    private TextView numBlocks;

    //vars
    private Fragment_Query_Listener listener;
    private int xDelta;
    private int yDelta;
    private List<ImageView> blocks_in_rl;
    private int num_blocks;

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
        numBlocks = (TextView) v.findViewById(R.id.numBlocks);
        num_blocks = 0;

        //send query to db
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO parser
                CharSequence query = interpret();
                listener.onGo(query);
            }
        });

        //remove block
        btn_clear.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int dragID = dragEvent.getAction();
                switch(dragID) {
                    //what if night enters d4
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //Toast.makeText(getContext(), "entered noticed", Toast.LENGTH_LONG).show();
                        btn_clear.setHovered(true);
                        break;
                    //what if night exited d4
                    case DragEvent.ACTION_DRAG_EXITED:
                        //Toast.makeText(getContext(), "exited noticed", Toast.LENGTH_LONG).show();
                        btn_clear.setHovered(false);
                        break;
                    //what if night is finally dropped on d4
                    case DragEvent.ACTION_DROP:
                        Toast.makeText(getContext(), "drop noticed", Toast.LENGTH_LONG).show();
                        rl_query.removeView(view);
                        blocks_in_rl.remove(view);
                        break;
                }
                return true;
            }
        });

        //remove all blocks
        btn_clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO ask again in popup
                for (int i=0; i<blocks_in_rl.size(); i++){
                    rl_query.removeView(blocks_in_rl.get(i));
                }
                blocks_in_rl.clear();
                return false;
            }
        });

        //testing
        numBlocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numBlocks.setText("counter: "+num_blocks+", rl: "+blocks_in_rl.size());
            }
        });

        return v;
    }

    public void createView(View view, int x, int y){
        //make a new iv todo send position via listener
        ImageView cur_view = new ImageView(getContext());
        //cur_view.setX(x);
        //cur_view.setY(y);

        //set drawable
        cur_view.setImageResource(getDrawableId(view));

        //add teh new iv to rl and blocklist
        rl_query.addView(cur_view);
        blocks_in_rl.add(cur_view);
        num_blocks++;

        /*cur_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                shadow.getView().setBackgroundResource(R.color.invisible);
                view.startDrag(data, shadow, view, 0);
            }
        });*/


        cur_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    final int X = (int) motionEvent.getRawX();
                    final int Y = (int) motionEvent.getRawY();
                    switch(motionEvent.getAction()&MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                            xDelta = X - lParams.leftMargin;
                            yDelta = Y - lParams.topMargin;
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.leftMargin = X - xDelta;
                            layoutParams.topMargin = Y - yDelta;
                            layoutParams.rightMargin = -250;
                            layoutParams.bottomMargin = -250;
                            view.setLayoutParams(layoutParams);
                            break;
                    }
                    rl_query.invalidate();
                    return true;
                }
        });

    }

    public void goInclickable(){
        for(ImageView iv: blocks_in_rl){
            iv.setClickable(false);
        }
        btn_go.setVisibility(View.INVISIBLE);
        btn_clear.setVisibility(View.INVISIBLE);
    }

    public int getDrawableId(View view){
        return (int) view.getTag();
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

    private final class MyTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }else{
                return false;
            }
        }
    }
}
