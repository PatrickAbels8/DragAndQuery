package com.example.dragandquery.practice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.DbView;
import com.example.dragandquery.R;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.block.BlockView;
import com.example.dragandquery.block.Node;
import com.example.dragandquery.db.DatabaseAccess;
import com.example.dragandquery.free.ClearView;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

/***
 *
 */

public class Fragment_Query_Ex extends Fragment {

    //errors
    public static String SELECT_MISSING_ERROR;
    public static String SQL_ERROR;

    //coms
    private RelativeLayout rl_query;
    private ClearView btn_go;
    private ClearView btn_clear;
    private ImageView btn_db;
    private ImageView bird;
    private LinearLayout db_view;
    private LinearLayout exercise;
    private TextView exercise_text;
    private PhotoView db_img;
    private TextView title_cafetaria;
    private TextView title_legend;

    //vars
    private Fragment_Query_Ex_Listener listener;
    public Context context;
    private boolean ex_open = true;
    private boolean db_open = false;

    //interface
    public interface Fragment_Query_Ex_Listener{
        void onGo(String query, List<String[]> response, float runtime);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_query_ex, container, false);

        //errors
        SELECT_MISSING_ERROR = getString(R.string.select_missing_error);
        SQL_ERROR = getString(R.string.sql_error);


        //init coms
        rl_query = (RelativeLayout) v.findViewById(R.id.frag_query);
        btn_go = (ClearView) v.findViewById(R.id.frag_go);
        btn_clear = (ClearView) v.findViewById(R.id.frag_clear);
        btn_db = (ImageView) v.findViewById(R.id.frag_db);
        bird = (ImageView) v.findViewById(R.id.ex_bird);
        db_view = (LinearLayout) v.findViewById(R.id.db_view);
        hideDB();
        context = getContext();
        exercise = (LinearLayout) v.findViewById(R.id.ll_ex);
        exercise_text = (TextView) v.findViewById(R.id.tv_ex);
        db_img = v.findViewById(R.id.db_img);
        title_cafetaria = v.findViewById(R.id.db_title_cafetaria);
        title_legend = v.findViewById(R.id.db_title_legend);

        //listeners
        btn_go.setMyClearDragListener(new Fragment_Query_Ex.MyGoListener());
        btn_clear.setOnLongClickListener(new Fragment_Query_Ex.MyClearLongClickListener());
        btn_clear.setMyClearDragListener(new Fragment_Query_Ex.MyClearDragListener());
        bird.setOnClickListener(new Fragment_Query_Ex.MyBirdClickListener());
        btn_db.setOnClickListener(new Fragment_Query_Ex.MyDBClickListener());
        title_legend.setOnClickListener(new Fragment_Query_Ex.LegendListener());
        title_cafetaria.setOnClickListener(new Fragment_Query_Ex.CafetariaListener());

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
        String val = ((BlockView)view).getText().toString();
        Node root = new Node(blockT, val);
        BlockView cur_view = blockT.createView(context, val);
        cur_view.setNode(root);

        //add the new bv to rl
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(x<0 || y<0)
            params.setMargins(rl_query.getWidth()-cur_view.getWidth(), rl_query.getHeight()-cur_view.getHeight(), 0, 0);
        else
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
        btn_go.setVisibility(View.GONE);
        btn_clear.setVisibility(View.GONE);
        btn_db.setVisibility(View.GONE);

    }

    public void goClickable(){
        for(int i=0; i<extractLayoutViews(null).size(); i++){
            extractLayoutViews(null).get(i).setClickable(true);
        }
        btn_go.setVisibility(View.VISIBLE);
        btn_clear.setVisibility(View.VISIBLE);
        btn_db.setVisibility(View.VISIBLE);
    }

    public String interpret(BlockView select){
        if(select.getNode().getBlock() != BlockT.SELECT){
            Toast.makeText(context, SELECT_MISSING_ERROR, Toast.LENGTH_SHORT).show();
            return null;

        }
        return Node.transform_roj(Node.transform_foj(select.getNode().toTreeString()));
    }

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
            case 104:
                text += getString(R.string.ex_easy_5_text);
                break;
            case 105:
                text += getString(R.string.ex_easy_6_text);
                break;
            case 106:
                text += getString(R.string.ex_easy_7_text);
                break;
            case 107:
                text += getString(R.string.ex_easy_8_text);
                break;
            case 108:
                text += getString(R.string.ex_easy_9_text);
                break;
            case 109:
                text += getString(R.string.ex_easy_10_text);
                break;
            case 200:
                text += getString(R.string.ex_medium_1_text);
                break;
            case 201:
                text += getString(R.string.ex_medium_2_text);
                break;
            case 202:
                text += getString(R.string.ex_medium_3_text);
                break;
            case 203:
                text += getString(R.string.ex_medium_4_text);
                break;
            case 204:
                text += getString(R.string.ex_medium_5_text);
                break;
            case 205:
                text += getString(R.string.ex_medium_6_text);
                break;
            case 206:
                text += getString(R.string.ex_medium_7_text);
                break;
            case 207:
                text += getString(R.string.ex_medium_8_text);
                break;
            case 208:
                text += getString(R.string.ex_medium_9_text);
                break;
            case 300:
                text += getString(R.string.ex_hard_1_text);
                break;
            case 301:
                text += getString(R.string.ex_hard_2_text);
                break;
            case 302:
                text += getString(R.string.ex_hard_3_text);
                break;
            case 303:
                text += getString(R.string.ex_hard_4_text);
                break;
            case 304:
                text += getString(R.string.ex_hard_5_text);
                break;
            case 305:
                text += getString(R.string.ex_hard_6_text);
                break;
            case 306:
                text += getString(R.string.ex_hard_7_text);
                break;
            case 307:
                text += getString(R.string.ex_hard_8_text);
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

    //helper
    public int dp_to_int(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp*scale+0.5f);
    }

    public List<String[]> queryDB(String query){
        try{
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context, DatabaseAccess.DB_CAFETARIA);
            databaseAccess.open();
            List<String[]> response = databaseAccess.query(query);
            databaseAccess.close();
            return response;
        }catch(Exception e){
            Log.d("contodo", e.toString());
            return null;
        }
    }

    /***
     * Listeners
     */

    // when a parent block is dragged his whole family shoudld call their listeners
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

    // if a block is dragged and dropped above another block
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

                            //ui:
                            //1. count members
                            //2. each member has to remember distanc to parent
                            //3. parent changes position
                            //4. each member changes position relative to parents new position
                            List<BlockView> subtree = extractTreeViews(him);
                            for(int i=0; i<subtree.size(); i++){
                                subtree.get(i).notifyListenerDistance(subtree.get(i).getX()-him.getX(), subtree.get(i).getY()-him.getY());
                            }
                            him.setX(me.getX()+me.getWidth()-getResources().getDimension(R.dimen.block_ui_overlapping_h));
                            him.setY(me.getY());
                            for(int i=0; i<subtree.size(); i++) {
                                subtree.get(i).notifyListener(him.getX(), him.getY());
                            }

                            //logic
                            me.getNode().addRightChild(him.getNode());

                            //sounds
                            MediaPlayer.create(me.getContext(), R.raw.dropblock).start();
                        }
                        if (fits_down && !me_node.hasDown()) {
                            //ui:
                            //1. count members
                            //2. each member has to remember distanc to parent
                            //3. parent changes position
                            //4. each member changes position relative to parents new position
                            List<BlockView> subtree = extractTreeViews(him);
                            for(int i=0; i<subtree.size(); i++){
                                subtree.get(i).notifyListenerDistance(subtree.get(i).getX()-him.getX(), subtree.get(i).getY()-him.getY());
                            }
                            him.setX(me.getX());
                            him.setY(me.getY()+me.getHeight()-getResources().getDimension(R.dimen.block_ui_overlapping_v));
                            for(int i=0; i<subtree.size(); i++) {
                                subtree.get(i).notifyListener(him.getX(), him.getY());
                            }

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

    //when notified move along with your parent
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
                        if(query == null) {
                            break;
                        }
                        long start = System.currentTimeMillis();
                        List<String[]> response = queryDB(query);
                        long stop = System.currentTimeMillis();
                        if(response != null) {
                            MediaPlayer.create(me.getContext(), R.raw.submit).start();
                            listener.onGo(query, response, (float) (stop - start) / 1000);
                        }
                        else
                            Toast.makeText(context, SQL_ERROR+": "+query, Toast.LENGTH_LONG).show();
                        hideBird();
                        hideDB();
                        //sounds
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
                hideBird();
            }else{
                showBird();
                hideDB();
            }
        }
    }

    public class MyDBClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(db_open){
                hideDB();
            }else{
                showDB();
                hideBird();
            }
        }
    }

    public class CafetariaListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            title_cafetaria.setBackground(getResources().getDrawable(R.drawable.border_white));
            title_legend.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_img.setImageResource(DbView.DB_CAFETERIA_ER);
        }
    }

    public class LegendListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            title_cafetaria.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            title_legend.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_img.setImageResource(DbView.DB_LEGEND);
        }
    }

    public void showBird(){
        exercise.setVisibility(View.VISIBLE);
        ex_open = true;
    }

    public void hideBird(){
        exercise.setVisibility(View.GONE);
        ex_open = false;
    }

    public void showDB(){
        db_view.setVisibility(View.VISIBLE);
        db_open = true;
    }

    public void hideDB(){
        db_view.setVisibility(View.GONE);
        db_open = false;
    }
}
