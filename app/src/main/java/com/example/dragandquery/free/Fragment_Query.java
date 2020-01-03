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
 * - dropped views invisible (also when visible childs dropped on rl_query)
 * - kill relative layout parents wrong
 * - throw runtime exception (when go button pressed)
 * - maybe touchlistener on views but on layouts
 * - rmv logs and toats
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
    //private List<BlockView> blocks_in_rl;
    private List<RelativeLayout> blockGroups_in_rl;
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
        //blocks_in_rl = new ArrayList<>();
        blockGroups_in_rl = new ArrayList<>();
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
            if(!blockGroups_in_rl.isEmpty()){
                MediaPlayer.create(context, R.raw.clearblock).start();
            }
            for (int i=0; i<blockGroups_in_rl.size(); i++){
                rl_query.removeView(blockGroups_in_rl.get(i));
            }
            blockGroups_in_rl.clear();
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
                    /*if(o instanceof BlockView) {
                        BlockView draggedView = (BlockView) o;
                        List<BlockView> members = extractTreeViews(draggedView);
                        for(int i=0; i<members.size(); i++){
                            rl_query.removeView(members.get(i));
                            blocks_in_rl.remove(members.get(i));
                        }
                        //sounds
                        MediaPlayer.create(context, R.raw.clearblock).start();
                    } else*/  if(o instanceof RelativeLayout) {
                        RelativeLayout draggedLayout = (RelativeLayout) o;
                        ((ViewGroup)draggedLayout.getParent()).removeView(draggedLayout);
                        blockGroups_in_rl.remove(draggedLayout);
                        clearFromParent(getLayoutHead(draggedLayout));

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
                    /*if(o instanceof BlockView) {
                        BlockView draggedView = (BlockView) o;
                        draggedView.setX(dragEvent.getX()-draggedView.getWidth()/2);
                        draggedView.setY(dragEvent.getY()-draggedView.getHeight()/2);
                        draggedView.setVisibility(View.VISIBLE);
                    } else*/ if(o instanceof RelativeLayout) {
                        RelativeLayout draggedLayout = (RelativeLayout) o;
                        Log.d("############# dragged_Layout dim", Integer.toString(draggedLayout.getChildCount()));
                        draggedLayout.setX(dragEvent.getX() - draggedLayout.getWidth() / 2);
                        draggedLayout.setY(dragEvent.getY() - draggedLayout.getHeight() / 2);
                        draggedLayout.setVisibility(View.VISIBLE);
                        ((ViewGroup)draggedLayout.getParent()).removeView(draggedLayout);
                        updateLayoutSizes();
                        rl_query.addView(draggedLayout); //todo maybe wrong position
                        clearFromParent(getLayoutHead(draggedLayout));
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
        //create a blockT of the right type
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

        //create a corresponding node and BlockView
        Node root = new Node(blockT);
        BlockView cur_view = blockT.createView(context);
        cur_view.setNode(root);

        //add the new bv as head of a new rl to rl_query and blockGroup list
        RelativeLayout.LayoutParams lp_view = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rl_query.addView(cur_view, lp_view); //only temporary for margin issues
        RelativeLayout draggedLayout = new RelativeLayout(context); //here she comes
        RelativeLayout.LayoutParams lp_layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp_layout.setMargins((int)x, (int)y, 0, 0);
        rl_query.addView(draggedLayout, lp_layout);
        rl_query.removeView(cur_view); //told you
        draggedLayout.addView(cur_view);
        //draggedLayout.setBackgroundColor(getResources().getColor(R.color.background_light));
        blockGroups_in_rl.add(draggedLayout);

        Log.d("############# blockGroups_in_rl size", Integer.toString(blockGroups_in_rl.size()));

        //drag mode: touch --> single drag todo drag bv but rl
        draggedLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadow, view, View.DRAG_FLAG_OPAQUE);
                    view.setVisibility(View.INVISIBLE);
                    Log.d("########## START DRAG", "true");
                    return true;
                }else
                    return false;
            }
        });

        //drag mode: double touch --> group drag
        /*cur_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)&&((ImageView)view).getDrawable()!=null){
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadow, view, View.DRAG_FLAG_OPAQUE);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                }else
                    return false;
            }
        });*/

        //drag mode: longclick --> edit todo
        /*cur_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //if has dropdown start dropdown, if has editable start editable, if...
                return true;
            }
        });*/

        //drop mode: other block
        //todo: with view but layout ???
        draggedLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View thisView, DragEvent dragEvent) {
                Object o = dragEvent.getLocalState();
                if(o instanceof RelativeLayout) {
                    RelativeLayout draggedLay = (RelativeLayout) o;
                    //BlockT draggedBlock = (BlockT) iv.getTag();
                    //BlockT thisBlock = (BlockT) view.getTag();

                    Log.d("############# dragged_Lay dim", Integer.toString(draggedLay.getChildCount()));

                    Node draggedNode = getLayoutHead(draggedLay).getNode();
                    Node thisNode = getLayoutHead((RelativeLayout) thisView).getNode();
                    BlockT draggedBlock = draggedNode.getBlock();
                    BlockT thisBlock = thisNode.getBlock();
                    boolean fits_right = thisBlock.hasRightSuccessor(draggedBlock); //todo special cases like star without attribute
                    boolean fits_down = thisBlock.hasDownSuccessor(draggedBlock);

                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_ENTERED:
                            if (fits_right) {
                                draggedLay.setVisibility(View.VISIBLE);
                                draggedLay.setAlpha(0.5f);
                                draggedLay.setX(thisView.getX()+thisView.getWidth());
                                draggedLay.setY(thisView.getY());
                            } else if (fits_down) {
                                draggedLay.setVisibility(View.VISIBLE);
                                draggedLay.setAlpha(0.5f);
                                draggedLay.setX(thisView.getX());
                                draggedLay.setY(thisView.getY()+thisView.getHeight());
                            }
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            draggedLay.setVisibility(View.INVISIBLE);
                            draggedLay.setAlpha(1f);
                            break;
                        case DragEvent.ACTION_DROP:
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            RelativeLayout thisLay = (RelativeLayout) thisView;
                            draggedLay.setAlpha(1f);
                            if (fits_right) {
                                /*draggedLay.setX(getLayoutHead(thisLay).getX()+getLayoutHead(thisLay).getWidth());
                                draggedLay.setY(getLayoutHead(thisLay).getY());*/
                                lp.addRule(RelativeLayout.RIGHT_OF, getLayoutHead(thisLay).getId());
                                clearFromParent(getLayoutHead(draggedLay));
                                thisNode.addRightChild(draggedNode);
                                ((ViewGroup)draggedLay.getParent()).removeView(draggedLay);
                                thisLay.addView(draggedLay, lp);
                                updateLayoutSizes();
                                //sounds
                                MediaPlayer.create(thisView.getContext(), R.raw.dropblock).start();
                            } else if(fits_down) {
                                /*draggedLay.setX(getLayoutHead(thisLay).getX());
                                draggedLay.setY(getLayoutHead(thisLay).getY() + getLayoutHead(thisLay).getHeight());*/
                                lp.addRule(RelativeLayout.BELOW, getLayoutHead(thisLay).getId());
                                clearFromParent(getLayoutHead(draggedLay));
                                thisNode.addDownChild(draggedNode);
                                ((ViewGroup)draggedLay.getParent()).removeView(draggedLay);
                                thisLay.addView(draggedLay, lp);
                                updateLayoutSizes();
                                //sounds
                                MediaPlayer.create(thisView.getContext(), R.raw.dropblock).start();
                            } else{
                                /*draggedLay.setX(getLayoutHead(thisLay).getX());
                                draggedLay.setY(getLayoutHead(thisLay).getY()-getLayoutHead(thisLay).getHeight());*/ //todo no new position but sound
                            }
                            draggedLay.setVisibility(View.VISIBLE);
                            Log.d("############# dragged_Layout dim", Integer.toString(thisLay.getChildCount()));
                            break;

                    }
                }
                return true;
            }
        });
    }

    //when holding a blockview retrun the relative layout with bv as head
    public RelativeLayout getLayout(BlockView bv){
        for(int i=0; i<blockGroups_in_rl.size(); i++){
            if(getLayoutHead(blockGroups_in_rl.get(i)).getId() == bv.getId()){
                return blockGroups_in_rl.get(i);
            }
        }
        return null;
    }

    //for all the rel layouts in rl_query add child sizes up (max or sum)
    public void updateLayoutSizes(){
        for(int i=0; i<blockGroups_in_rl.size(); i++){
            updateLayoutSize(blockGroups_in_rl.get(i));
        }
    }

    //todo
    public void updateLayoutSize(RelativeLayout rl){
        int count = rl.getChildCount();
        int total_width = 0;
        int total_height = 0;
        for(int i=0; i<count; i++){
            View child = rl.getChildAt(i);
            total_width += child.getWidth();
            total_height += child.getHeight();
        }
        rl.setMinimumWidth(total_width);
        rl.setMinimumHeight(total_height);
    }

    //when holding a layout return the leading blockview
    public BlockView getLayoutHead(RelativeLayout rl){
        return (BlockView) rl.getChildAt(0);
    }

    //when removed or cleared, if had node parent remove
    public void clearFromParent(BlockView bv){
        Node n = bv.getNode();
        if(n.getParent() != null){
            if(n.getParent().isRightButDown(n)){
                n.getParent().removeRightChild(n);
            } else{
                n.getParent().removeDownChild(n);
            }
        }
    }

    //extract the views that have to move when one is dragged
    public List<BlockView> extractTreeViews(BlockView mover){
        List<Node> tree = mover.getNode().getTreeMembers();
        List<BlockView> members = new ArrayList<>();
        for(int i=0; i<blockGroups_in_rl.size(); i++){
            if(tree.contains(getLayoutHead(blockGroups_in_rl.get(i)).getNode())){
                members.add(getLayoutHead(blockGroups_in_rl.get(i)));
            }
        }
        for(int i=0; i<members.size(); i++)
            Log.d("############## members: ", members.get(i).getNode().getBlock().getName());
        return members;
    }

    public void goInclickable(){
        for(int i=0; i<blockGroups_in_rl.size(); i++){
            blockGroups_in_rl.get(i).setClickable(false);
        }
        btn_go.setVisibility(View.INVISIBLE);
        btn_clear.setVisibility(View.INVISIBLE);
    }

    public void goClickable(){
        for(int i=0; i<blockGroups_in_rl.size(); i++){
            blockGroups_in_rl.get(i).setClickable(true);
        }
        btn_go.setVisibility(View.VISIBLE);
        btn_clear.setVisibility(View.VISIBLE);
    }

    public void resetSelectLayoutColors(){
        for(int i=0; i<blockGroups_in_rl.size(); i++){
            blockGroups_in_rl.get(i).setBackgroundColor(getResources().getColor(R.color.invisible));
        }
    }

    //in blockfrag upleft starts with xy=00, for rawXY in queryfrag add height and sub blockfrag margin (4dp), already sub blockfrag height
    public float[] getRawXY(float x, float y){
        float qf_height = (float) rl_query.getHeight();
        float bf_margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
        return new float[]{x, y +qf_height -bf_margin};
    }

    //TODO color current selects and choose by userclick
    public String interpret(){
        List<RelativeLayout> select_layouts = new ArrayList<>();
        for(int i=0; i<blockGroups_in_rl.size(); i++){
            if(getLayoutHead(blockGroups_in_rl.get(i)).getNode().getBlock() == BlockT.SELECT){
                select_layouts.add(blockGroups_in_rl.get(i));
                blockGroups_in_rl.get(i).setBackgroundColor(getResources().getColor(R.color.select_layouts));
                getLayoutHead(blockGroups_in_rl.get(i)).getNode().printTree();
            }
        }
        if(select_layouts.size()==0){
            return SELECT_MISSING_ERROR;
        } else if(select_layouts.size()==1){
            return getLayoutHead(select_layouts.get(0)).getNode().toTreeString();
        } /*else{
            for(int i=0; i<select_layouts.size(); i++){
                select_layouts.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo
                    }
                });
            }
        }*/
        return getLayoutHead(select_layouts.get(0)).getNode().toTreeString();
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
