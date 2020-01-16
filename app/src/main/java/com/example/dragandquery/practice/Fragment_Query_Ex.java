package com.example.dragandquery.practice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.block.BlockView;
import com.example.dragandquery.block.Node;
import com.example.dragandquery.free.ClearView;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * - db icon
 * - child doesnt move when parent dropped
 */

public class Fragment_Query_Ex extends Fragment {

    //errors
    public static String SELECT_MISSING_ERROR;

    //coms
    private RelativeLayout rl_query;
    private ClearView btn_go;
    private ClearView btn_clear;
    private ImageView bird;
    private LinearLayout exercise;
    private TextView exercise_text;

    //vars
    private Fragment_Query_Ex_Listener listener;
    public Context context;
    private boolean ex_open = true;

    //interface
    public interface Fragment_Query_Ex_Listener{
        void onGo(String query);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_query_ex, container, false);

        //errors
        SELECT_MISSING_ERROR = getString(R.string.select_missing_error);

        //init coms
        rl_query = (RelativeLayout) v.findViewById(R.id.frag_query);
        btn_go = (ClearView) v.findViewById(R.id.frag_go);
        btn_clear = (ClearView) v.findViewById(R.id.frag_clear);
        bird = (ImageView) v.findViewById(R.id.ex_bird);
        context = getContext();
        exercise = (LinearLayout) v.findViewById(R.id.ll_ex);
        exercise_text = (TextView) v.findViewById(R.id.tv_ex);

        //listeners
        btn_go.setMyClearDragListener(new Fragment_Query_Ex.MyGoListener());
        btn_clear.setOnLongClickListener(new Fragment_Query_Ex.MyClearLongClickListener());
        btn_clear.setMyClearDragListener(new Fragment_Query_Ex.MyClearDragListener());
        bird.setOnClickListener(new Fragment_Query_Ex.MyBirdClickListener());

        //set ex text
        exercise_text.setText(getExText(this.getArguments().getInt(Exercise.ID_KEY)));

        return v;
    }

    //create new block when pressed on fragment_blocks
    @SuppressLint("ClickableViewAccessibility")
    public void createView(View view, float x, float y) {
        //create a blockT of the right type
        BlockT blockT = BlockT.getBlock(((BlockT) view.getTag()).getDesign());

        //create a corresponding node and BlockView
        Node root = new Node(blockT);
        BlockView cur_view = blockT.createView(context);
        cur_view.setNode(root);

        //add the new bv to rl
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins((int)x-cur_view.getWidth(), (int)y-cur_view.getHeight(), 0, 0);
        rl_query.addView(cur_view, params);

        //add listeners
        cur_view.setOnTouchListener(new Fragment_Query_Ex.MyOnGroupTouchListener());
        cur_view.setMydragListener(new Fragment_Query_Ex.MyDragListener());
        cur_view.setListener(new Fragment_Query_Ex.MyGroupDragListener());
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
        for(int i=0; i<rl_query.getChildCount(); i++){
            View child = rl_query.getChildAt(i);
            if(child instanceof BlockView && tree.contains(((BlockView)child).getNode())){
                members.add((BlockView)child);
            }
        }
        return members;
    }

    //list of al the blocks in rl
    public List<BlockView> extractLayoutViews(BlockView without){
        List<BlockView> members = new ArrayList<>();
        for(int i=0; i<rl_query.getChildCount(); i++){
            View child = rl_query.getChildAt(i);
            if(child instanceof BlockView){
                BlockView b_child = (BlockView)child;
                if(b_child != without){
                    members.add(b_child);
                }
            }
        }
        return members;
    }

    public void goInclickable(){
        for(int i=0; i<extractLayoutViews(null).size(); i++){
            extractLayoutViews(null).get(i).setClickable(false);
        }
        btn_go.setVisibility(View.INVISIBLE);
        btn_clear.setVisibility(View.INVISIBLE);
    }

    public void goClickable(){
        for(int i=0; i<extractLayoutViews(null).size(); i++){
            extractLayoutViews(null).get(i).setClickable(true);
        }
        btn_go.setVisibility(View.VISIBLE);
        btn_clear.setVisibility(View.VISIBLE);
    }

    public String interpret(BlockView select){
        if(select.getNode().getBlock() != BlockT.SELECT){
            return SELECT_MISSING_ERROR;
        }
        return select.getNode().toTreeString();
    }

    //todo add exs via strings
    public String getExText(int ex_id){
        String text = "";
        switch(ex_id){
            case 100:
                text += getString(R.string.ex_easy_1_text);
                break;
            case 101:
                text += getString(R.string.ex_easy_2_text);
                break;
            case 102:
                text += getString(R.string.ex_easy_3_text);
                break;
            case 103:
                text += getString(R.string.ex_easy_4_text);
                break;
        }
        return text;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Query_Ex_Listener){
            listener = (Fragment_Query_Ex_Listener) context;
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

    /***
     * Listeners
     */

    public class MyOnGroupTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            List<BlockView> subtree = extractTreeViews((BlockView)view);
            List<BlockView> blocks = extractLayoutViews((BlockView)view);
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    clearFromParent((BlockView)view);
                    for(int i=0; i<subtree.size(); i++){
                        subtree.get(i).notifyListenerDistance(subtree.get(i).getX()-motionEvent.getRawX(), subtree.get(i).getY()-motionEvent.getRawY());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    for(int i=0; i<subtree.size(); i++){
                        subtree.get(i).notifyListener(motionEvent.getRawX(), motionEvent.getRawY());
                    }
                    for(int i=0; i<blocks.size(); i++){
                        blocks.get(i).notifyMyDragListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.MOVE);
                    }
                    btn_clear.notifyListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.MOVE);
                    btn_go.notifyListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.MOVE);
                    break;
                case MotionEvent.ACTION_UP:
                    for(int i=0; i<blocks.size(); i++){
                        blocks.get(i).notifyMyDragListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.UP);
                    }
                    btn_clear.notifyListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.UP);
                    btn_go.notifyListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.UP);
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    public class MyDragListener implements BlockView.MyOnDragListener{

        @Override
        public void onMyDrag(BlockView me, BlockView him, float x, float y, int event) {
            boolean isInMe = me.getX()<x && x<me.getX()+me.getWidth() && me.getY()<y && y<me.getY()+me.getHeight();
            if(isInMe){
                Node me_node = me.getNode();
                Node him_node = him.getNode();
                BlockT me_blockT = me_node.getBlock();
                BlockT him_blockT = him_node.getBlock();
                boolean fits_right = me_blockT.hasRightSuccessor(him_blockT);
                boolean fits_down = me_blockT.hasDownSuccessor(him_blockT);
                switch (event){
                    case BlockView.MOVE:
                        break;
                    case BlockView.UP:
                        if (fits_right && !me_node.hasRight()) {

                            //ui
                            him.setX(me.getX()+me.getWidth());
                            him.setY(me.getY());



                            //logic
                            me.getNode().addRightChild(him.getNode());

                            //sounds
                            MediaPlayer.create(me.getContext(), R.raw.dropblock).start();
                        }
                        if (fits_down && !me_node.hasDown()) {
                            //ui
                            him.setX(me.getX());
                            him.setY(me.getY()+me.getHeight());

                            //logic
                            me.getNode().addDownChild(him.getNode());

                            //sounds
                            MediaPlayer.create(me.getContext(), R.raw.dropblock).start();
                        }

                        break;

                }
            }
        }
    }

    //todo has to be notified when parent is dropped on other block
    public class MyGroupDragListener implements BlockView.GroupDragListener{

        @Override
        public void onGroupDrag(BlockView child, float xd, float yd) {
            child.setX(xd+child.getxDis());
            child.setY(yd+child.getyDis());
        }

        @Override
        public void setDistance(BlockView me, float x, float y) {
            me.setxDis(x);
            me.setyDis(y);
        }
    }




    public class MyGoListener implements ClearView.MyClearDragListener{
        @Override
        public void onMyDrag(ClearView me, BlockView him, float x, float y, int event) {
            boolean isInMe = me.getX()<x && x<me.getX()+me.getWidth() && me.getY()<y && y<me.getY()+me.getHeight();
            Log.d("########## go: ", Integer.toString(event));
            switch(event) {
                case BlockView.MOVE:
                    if(isInMe)
                        btn_go.setImageResource(R.drawable.go_x);
                    else
                        btn_go.setImageResource(R.drawable.go);
                    break;
                case BlockView.UP:
                    if(isInMe){
                        btn_go.setImageResource(R.drawable.go);
                        String query = interpret(him);
                        listener.onGo(query);
                        //sounds todo
                        btn_go.startAnimation(AnimationUtils.loadAnimation(me.getContext(), R.anim.vibrate_short));
                    }

                    break;
            }
        }
    }

    public class MyClearLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View view) {
            List<BlockView> blocks = extractLayoutViews(null);
            if(!blocks.isEmpty()){
                MediaPlayer.create(context, R.raw.clearblock).start();
                btn_clear.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.vibrate_short));
            }
            for (int i=0; i<blocks.size(); i++){
                rl_query.removeView(blocks.get(i));
            }
            return false;
        }
    }

    //todo dont notify all the time but only notify when isInMe fullfilled --> performane
    public class MyClearDragListener implements ClearView.MyClearDragListener{

        @Override
        public void onMyDrag(ClearView me, BlockView him, float x, float y, int event) {
            boolean isInMe = me.getX()<x && x<me.getX()+me.getWidth() && me.getY()<y && y<me.getY()+me.getHeight();
            switch(event) {
                case BlockView.MOVE:
                    if(isInMe)
                        btn_clear.setImageResource(R.drawable.garbage_collector_x);
                    else
                        btn_clear.setImageResource(R.drawable.garbage_collector);
                    break;
                case BlockView.UP:
                    if(isInMe){
                        btn_clear.setImageResource(R.drawable.garbage_collector);
                        List<BlockView> members = extractTreeViews(him);
                        for(int i=0; i<members.size(); i++){
                            rl_query.removeView(members.get(i));
                        }
                        clearFromParent(him);
                        //sounds
                        MediaPlayer.create(context, R.raw.clearblock).start();
                        btn_clear.startAnimation(AnimationUtils.loadAnimation(me.getContext(), R.anim.vibrate_short));
                    }

                    break;
            }
        }
    }

    public class MyBirdClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(ex_open){
                exercise.setVisibility(View.INVISIBLE);
                ex_open = false;
            }else{
                exercise.setVisibility(View.VISIBLE);
                ex_open = true;
            }
        }
    }
}