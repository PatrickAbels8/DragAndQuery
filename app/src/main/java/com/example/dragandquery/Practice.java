package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.SystemClock;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragandquery.block.Block;
import com.example.dragandquery.block.BlockFactory;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.block.BlockView;
import com.example.dragandquery.block.Node;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * - child doesnt move
 */

public class Practice extends AppCompatActivity {

    //coms
    Context context;
    RelativeLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        context = getApplicationContext();

        //init stuff
        layout = (RelativeLayout) findViewById(R.id.layout);


        /***
         * On Grounp Listener stuff
         */


        BlockT s = BlockT.SELECT;
        BlockT a = BlockT.ATTRIBUTE;

        BlockView sel = s.createView(context);
        BlockView att = a.createView(context);

        Node s_node = new Node(s);
        Node a_node = new Node(a);

        sel.setNode(s_node);
        att.setNode(a_node);

        RelativeLayout.LayoutParams s_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams a_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        s_params.setMargins(200, 500, 0, 0);
        a_params.setMargins(200, 700, 0, 0);

        layout.addView(sel, s_params);
        layout.addView(att, a_params);

        sel.setOnTouchListener(new Practice.MyOnTouchListener());
        att.setOnTouchListener(new Practice.MyOnTouchListener());

        //sel.setOnDragListener(new Practice.MyOnDragListener());
        //att.setOnDragListener(new Practice.MyOnDragListener());

        ((Button)findViewById(R.id.btttttn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                att.setX(sel.getX()+sel.getWidth());
                att.setY(sel.getY());
                //sounds
                MediaPlayer.create(sel.getContext(), R.raw.dropblock).start();

                sel.getNode().addRightChild(att.getNode());
            }
        });

        sel.setMydragListener(new BlockView.MyOnDragListener() {
            @Override
            public void onMyDrag(BlockView me, BlockView him, float x, float y, int event) {
                Log.d("############ mydrag", "onMyDrag");
                boolean isInMe = me.getX()<x && x<me.getX()+me.getWidth() && me.getY()<y && y<me.getY()+me.getHeight();
                Log.d("############ mydrag", Float.toString(me.getX()));
                Log.d("############ mydrag", Float.toString(x));
                Log.d("############ mydrag", Float.toString(me.getWidth()));
                Log.d("############ mydrag", Float.toString(me.getY()));
                Log.d("############ mydrag", Float.toString(y));
                Log.d("############ mydrag", Float.toString(me.getHeight()));
                Log.d("############ mydrag", "isinMe:"+Boolean.toString(isInMe));
                if(isInMe){
                    boolean fits_right = true; //todo
                    switch (event){
                        case BlockView.MOVE:
                            Log.d("############ mydrag", "MOVE");
                            break;
                        case BlockView.UP:
                            Log.d("############ mydrag", "UP");
                            if (fits_right) {
                                him.setX(me.getX()+me.getWidth());
                                him.setY(me.getY());
                                //sounds
                                MediaPlayer.create(me.getContext(), R.raw.dropblock).start();

                                me.getNode().addRightChild(him.getNode());
                            }
                            break;

                    }
                }

            }
        });

        att.setMydragListener(new BlockView.MyOnDragListener() {
            @Override
            public void onMyDrag(BlockView me, BlockView him, float x, float y, int event) {
                boolean isInMe = me.getX()<x && x<me.getX()+me.getWidth() && me.getY()<y && y<me.getY()+me.getHeight();
                if(isInMe){
                    boolean fits_right = true; //todo
                    switch (event){
                        case BlockView.MOVE:
                            break;
                        case BlockView.UP:
                            if (fits_right) {
                                him.setX(me.getX()+me.getWidth());
                                him.setY(me.getY());
                                //sounds
                                MediaPlayer.create(me.getContext(), R.raw.dropblock).start();

                                me.getNode().addRightChild(him.getNode());
                            }
                            break;

                    }
                }
            }
        });

        sel.setListener(new BlockView.GroupDragListener() {
            @Override
            //public void onGroupDrag(BlockView child, BlockView parent) {
            public void onGroupDrag(BlockView child, float xd, float yd) {
                Log.d("############### lets change my pos", "sel");
                //child.setX(parent.getX()+child.getxDis());
                child.setX(xd+child.getxDis());
                //child.setY(parent.getY()+child.getyDis());
                child.setY(yd+child.getyDis());
            }

            @Override
            public void setDistance(BlockView bv, float x, float y) {
                bv.setxDis(x);
                bv.setyDis(y);
                Log.d("############### x_sel", Float.toString(x));
                Log.d("############### y_sel", Float.toString(y));
            }
        });

        att.setListener(new BlockView.GroupDragListener() {
            @Override
            //public void onGroupDrag(BlockView child, BlockView parent) {
            public void onGroupDrag(BlockView child, float xd, float yd) {
                Log.d("############### lets change my pos", "att");
                //child.setX(parent.getX()+child.getxDis());
                child.setX(xd+child.getxDis());
                //child.setY(parent.getY()+child.getyDis());
                child.setY(yd+child.getyDis());
            }

            @Override
            public void setDistance(BlockView bv, float x, float y) {
                bv.setxDis(x);
                bv.setyDis(y);
                Log.d("############### x_att", Float.toString(x));
                Log.d("############### y_att", Float.toString(y));
            }
        });

        layout.setOnDragListener((view, dragEvent) -> {
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
                    if(o instanceof BlockView) {
                        BlockView draggedView = (BlockView) o;
                        draggedView.setX(dragEvent.getX()-draggedView.getWidth()/2);
                        draggedView.setY(dragEvent.getY()-draggedView.getHeight()/2);
                        draggedView.setVisibility(View.VISIBLE);
                    }else if(o instanceof RelativeLayout) {
                        RelativeLayout draggedLayout = (RelativeLayout) o;
                        ((ViewGroup)draggedLayout.getParent()).removeView(draggedLayout);
                        layout.addView(draggedLayout);
                        draggedLayout.setX(dragEvent.getX()-draggedLayout.getWidth()/2);
                        draggedLayout.setY(dragEvent.getY()-draggedLayout.getHeight()/2);
                        draggedLayout.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        });

        /*otherView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.RIGHT_OF, draggedView.getId());
                layout.removeView(view);
                draggedLayout.addView(view, lp);
                return true;
            }
        });*/


   }

    public List<BlockView> extractTreeViews(BlockView mover){
        List<Node> tree = mover.getNode().getTreeMembers();
        List<BlockView> members = new ArrayList<>();
        for(int i=0; i<layout.getChildCount(); i++){
            View child = layout.getChildAt(i);
            if(child instanceof BlockView && tree.contains(((BlockView)child).getNode())){
                members.add((BlockView)child);
            }
        }
        Log.d("############### members count", Integer.toString(members.size()));
        return members;
    }

    public List<BlockView> extractLayoutViews(BlockView without){
        List<BlockView> members = new ArrayList<>();
        for(int i=0; i<layout.getChildCount(); i++){
            View child = layout.getChildAt(i);
            if(child instanceof BlockView){
                BlockView b_child = (BlockView)child;
                if(b_child != without){
                    members.add(b_child);
                }
            }
        }
        Log.d("############### members count", Integer.toString(members.size()));
        return members;
    }

   public class MyOnDragListener implements View.OnDragListener{

       @Override
       public boolean onDrag(View view, DragEvent dragEvent) {
           Object o = dragEvent.getLocalState();
           if(o instanceof BlockView) {
               BlockView draggedView = (BlockView) o;
               boolean fits_right = true;

               switch (dragEvent.getAction()) {
                   case DragEvent.ACTION_DRAG_STARTED:
                       Log.d("############### DRAG STARTED", "__________");
                       break;
                   case DragEvent.ACTION_DRAG_ENTERED:
                       Log.d("############### DRAG ENTEERED", "__________");
                       break;
                   case DragEvent.ACTION_DRAG_EXITED:
                       Log.d("############### DRAG EXITED", "__________");
                       break;
                   case DragEvent.ACTION_DROP:
                       BlockView thisView = (BlockView) view;
                       if (fits_right) {
                           draggedView.setX(thisView.getX()+thisView.getWidth());
                           draggedView.setY(thisView.getY());
                           //sounds
                           MediaPlayer.create(thisView.getContext(), R.raw.dropblock).start();

                           thisView.getNode().addRightChild(draggedView.getNode());
                       }
                       break;

               }
           }
           return true;
       }
   }

   public class TestTouchListener implements View.OnTouchListener{
       @Override
       public boolean onTouch(View view, MotionEvent motionEvent) {
           switch(motionEvent.getAction()){
               case MotionEvent.ACTION_DOWN:
                   Log.d("############### action down", "noticed");
                   View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                   view.startDragAndDrop(null, shadow, view, View.DRAG_FLAG_OPAQUE);

                   break;
               case MotionEvent.ACTION_MOVE:
                   Log.d("############### action move", "noticed");
                   break;
               case MotionEvent.ACTION_CANCEL:
                   Log.d("############### action cancel", "noticed");
                   break;
               case MotionEvent.ACTION_UP:
                   Log.d("############### action up", "noticed");
                   break;
               case MotionEvent.ACTION_POINTER_DOWN:
                   Log.d("############### action pointer down", "noticed");
                   break;
               case MotionEvent.ACTION_POINTER_UP:
                   Log.d("############### action pointer up", "noticed");
                   break;
               default:
                   Log.d("############### action default", "noticed");
                   return false;
           }
           return true;
       }
   }



   public class MyOnTouchListener implements View.OnTouchListener{

       @Override
       public boolean onTouch(View view, MotionEvent motionEvent) {
           Log.d("############### action", "noticed");
           List<BlockView> subtree = extractTreeViews((BlockView)view);
           List<BlockView> blocks = extractLayoutViews((BlockView)view);
           switch(motionEvent.getAction()){
               case MotionEvent.ACTION_DOWN:
                   //notify listener current distance
                   Log.d("############### action down", "notices");
                   /*for(int i=0; i<subtree.size(); i++){
                       subtree.get(i).notifyListenerDistance(subtree.get(i).getX()-view.getX(), subtree.get(i).getY()-view.getY());
                   }*/

                   //View.DragShadowBuilder shadow = new View.DragShadowBuilder(view); //todo when rmv no drop
                   //view.startDragAndDrop(null, shadow, view, View.DRAG_FLAG_OPAQUE); //todo when there no moveevent
                   for(int i=0; i<subtree.size(); i++){
                       subtree.get(i).notifyListenerDistance(subtree.get(i).getX()-motionEvent.getRawX(), subtree.get(i).getY()-motionEvent.getRawY());
                   }



                   //view.setVisibility(View.INVISIBLE);
                   break;
               case MotionEvent.ACTION_MOVE:
                   //notify listener moving
                   Log.d("############### action move", "noticed");
                   for(int i=0; i<subtree.size(); i++){
                       Log.d("############### found 1", "noticed");
                       //subtree.get(i).notifyListener((BlockView)view);
                       subtree.get(i).notifyListener(motionEvent.getRawX(), motionEvent.getRawY());
                   }
                   for(int i=0; i<blocks.size(); i++){
                       Log.d("########### mytouch", "notify mydrag");
                       blocks.get(i).notifyMyDragListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.MOVE);
                   }

                   break;
               case MotionEvent.ACTION_UP:
                   for(int i=0; i<blocks.size(); i++){
                       blocks.get(i).notifyMyDragListener((BlockView)view, motionEvent.getRawX(), motionEvent.getRawY(), BlockView.UP);
                   }
                   break;
               default:
                   Log.d("############### action default", "noticed");
                   return false;
           }
           return true;
       }
   }
}
