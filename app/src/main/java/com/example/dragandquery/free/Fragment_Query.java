package com.example.dragandquery.free;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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

import com.example.dragandquery.R;
import com.example.dragandquery.block.Block;
import com.example.dragandquery.block.BlockFactory;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.block.BlockView;
import com.example.dragandquery.block.Node;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * - appearence depending on view pos (not good enough yet)to
 * - drag mode with several blocks
 * - touch mode with several blocks (maybe vie viewgroup or linearlyouts)
 * - kill parentship has to diff between killing right and down child
 * - not just add right but also add parent from left
 */

public class Fragment_Query extends Fragment {

    //errors
    public static String SELECT_MISSING_ERROR;

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
        void onGo(String query);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_query, container, false);

        //errors
        SELECT_MISSING_ERROR = getString(R.string.select_missing_error);

        //init coms
        rl_query = (RelativeLayout) v.findViewById(R.id.frag_query);
        btn_go = (Button) v.findViewById(R.id.frag_go);
        btn_clear = (ImageButton) v.findViewById(R.id.frag_clear);
        blocks_in_rl = new ArrayList<>();
        context = getContext();

        //send query to db
        btn_go.setOnClickListener(view -> {
            String query = interpret();
            listener.onGo(query);
        });

        //remove all blocks
        btn_clear.setOnLongClickListener(view -> {
            //TODO ask again in popup fragment
            //sounds
            if(!blocks_in_rl.isEmpty()){
                MediaPlayer.create(context, R.raw.clearblock).start();
            }
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
                    btn_clear.setImageResource(R.drawable.garbage_collector_x);
                    break;
                //what if night exited d4
                case DragEvent.ACTION_DRAG_EXITED:
                    btn_clear.setImageResource(R.drawable.garbage_collector);
                    break;
                //what if night is finally dropped on d4
                case DragEvent.ACTION_DROP:
                    btn_clear.setImageResource(R.drawable.garbage_collector);
                    Object o = dragEvent.getLocalState();
                    if(o instanceof ImageView) {
                        ImageView draggedView = (ImageView) o;
                        for(ImageView iv: extractTreeViews(draggedView)){
                            rl_query.removeView(iv);
                            blocks_in_rl.remove(iv);
                        }
                        //sounds
                        MediaPlayer.create(context, R.raw.clearblock).start();
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
                        BlockView draggedView = (BlockView) o;
                        float margin = 50; //todo
                        draggedView.setX(dragEvent.getX()-margin);
                        draggedView.setY(dragEvent.getY()-margin);
                        draggedView.setVisibility(View.VISIBLE);
                        //todo diff between exiting with a right or down child
                        Node draggedNode = draggedView.getNode();
                        if((draggedNode.getParent() != null) && (draggedNode.getParent().hasRight()))
                            draggedNode.getParent().removeRightChild(draggedNode);
                        if((draggedNode.getParent() != null) && (draggedNode.getParent().hasDown()))
                            draggedNode.getParent().removeDownChild(draggedNode);

                    }
                    break;
            }
            return true;
        });

        return v;
    }

    //create new block when pressed on fragment_blocks
    @SuppressLint("ClickableViewAccessibility")
    public void createView(View view, float x, float y) {
        //make a new iv at (x,y)
        BlockT blockT = null;

        /***
         * !!!!!!!!!!!! EVERY BLOCK HAS TO MANUALLY BE CHOSEN HERE!!!!!!!!!!!
         */
        switch (((BlockT) view.getTag()).getDesign()) {
            case R.drawable.attribute_block:
                blockT = BlockT.ATTRIBUTE;
                break;
            case R.drawable.from_block:
                blockT = BlockT.FROM;
                break;
            case R.drawable.select_block:
                blockT = BlockT.SELECT;
                break;
            case R.drawable.star_block:
                blockT = BlockT.STAR;
                break;
            case R.drawable.table_block:
                blockT = BlockT.TABLE;
                break;
            case R.drawable.where_block:
                blockT = BlockT.WHERE;
                break;
        }

        //tree stuff
        Node root = new Node(blockT);

        //ImageView cur_view = block.createView(context);
        ImageView cur_view = blockT.createView(context);
        ((BlockView) cur_view).setNode(root);
        float[] rawXY = getRawXY(x, y);
        cur_view.setX(rawXY[0]);
        cur_view.setY(rawXY[1]); //todo add upwards margin of

        //add the new iv to rl and blocklist
        rl_query.addView(cur_view);
        blocks_in_rl.add(cur_view);

        //drag mode
        //cur_view.setOnTouchListener(new BlockT.OnTouchListener());
        cur_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)&&((ImageView)view).getDrawable()!=null){
                    /*List<ImageView> treeMembers = extractTreeViews((ImageView)view);
                            Log.d("##################### count: ", Integer.toString(treeMembers.size()));
                    for(int i=0; i<treeMembers.size(); i++){ //todo not good yet
                        Log.d("########################", "another one");
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadow = new View.DragShadowBuilder(treeMembers.get(i));
                        treeMembers.get(i).startDragAndDrop(data, shadow, treeMembers.get(i), View.DRAG_FLAG_OPAQUE);
                        treeMembers.get(i).setVisibility(View.INVISIBLE);
                    }*/
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadow, view, View.DRAG_FLAG_OPAQUE);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                }else
                    return false;
            }
        });

        //drop mode: other block
        cur_view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Object o = dragEvent.getLocalState();
                if(o instanceof ImageView) {
                    BlockView bv = (BlockView) o;
                    //BlockT draggedBlock = (BlockT) iv.getTag();
                    //BlockT thisBlock = (BlockT) view.getTag();

                    Node draggedNode = bv.getNode();
                    Node thisNode = ((BlockView) view).getNode();
                    BlockT draggedBlock = draggedNode.getBlock();
                    BlockT thisBlock = thisNode.getBlock();
                    boolean fits_right = thisBlock.hasRightSuccessor(draggedBlock);
                    boolean fits_down = thisBlock.hasDownSuccessor(draggedBlock);
                    //boolean isOnRightSide = bv.getX() > view.getX();

                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_ENTERED:
                            if (fits_right) {
                                bv.setVisibility(View.VISIBLE);
                                bv.setAlpha(0.5f);
                                bv.setX(view.getX()+view.getWidth());
                                bv.setY(view.getY());
                            }
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            bv.setVisibility(View.INVISIBLE);
                            bv.setAlpha(1f);
                            break;
                        case DragEvent.ACTION_DROP:
                            bv.setAlpha(1f);
                            if (fits_right) {
                                bv.setX(view.getX()+view.getWidth()); //todo has to fit to drawables
                                bv.setY(view.getY());
                                thisNode.addRightChild(draggedNode);
                                //sounds
                                MediaPlayer.create(bv.getContext(), R.raw.dropblock).start();
                            } else if(fits_down) {
                                bv.setX(view.getX());
                                bv.setY(view.getY() + view.getHeight()); //todo has to fit to drawables
                                thisNode.addDownChild(draggedNode);
                                //sounds
                                MediaPlayer.create(bv.getContext(), R.raw.dropblock).start();
                            } else{
                                bv.setX(view.getX());
                                bv.setY(view.getY()-5*view.getHeight()); //todo maybe somewhere else
                            }
                            bv.setVisibility(View.VISIBLE);
                            break;

                    }
                }
                return true;
            }
        });
    }

    //extract the views that have to move when one is dragged
    public List<ImageView> extractTreeViews(ImageView mover){
        List<Node> tree = ((BlockView) mover).getNode().getTreeMembers();
        List<ImageView> members = new ArrayList<>();
        for(ImageView iv: blocks_in_rl){
            if(tree.contains(((BlockView) iv).getNode())){
                members.add(iv);
            }
        }
        for(int i=0; i<members.size(); i++)
            Log.d("############## members: ", ((BlockView)members.get(i)).getNode().getBlock().getName());
        return members;
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

    //in blockfrag upleft starts with xy=00, for rawXY in queryfrag add height and sub blockfrag margin (4dp), already sub blockfrag height
    public float[] getRawXY(float x, float y){
        float qf_height = (float) rl_query.getHeight();
        float bf_margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
        return new float[]{x, y +qf_height -bf_margin};
    }

    //TODO
    public String interpret(){
        for(ImageView iv: blocks_in_rl){
            if(((BlockView)iv).getNode().getBlock() == BlockT.SELECT){
                ((BlockView)iv).getNode().printTree();
                return ((BlockView)iv).getNode().toTreeString();
            }
        }
        return SELECT_MISSING_ERROR;
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
