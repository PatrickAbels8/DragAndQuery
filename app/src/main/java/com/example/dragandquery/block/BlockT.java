package com.example.dragandquery.block;

import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.example.dragandquery.R;
import com.example.dragandquery.free.Fragment_Query;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO
 * -drag and touch not just for dv but for whole tree of dv's (viewgroup or dynamic linear lyouts)
 */

public enum BlockT {
    SELECT,
    FROM,
    WHERE,
    STAR,
    TABLE,
    ATTRIBUTE;

    public String getName() {
        switch(this){
            case SELECT:
                return "SELECT";
            case FROM:
                return "FROM";
            case WHERE:
                return "WHERE";
            case TABLE:
                return "table";
            case ATTRIBUTE:
                return "attribute";
            case STAR:
                return "*";
        }
        return "";
    }

    @DrawableRes
    public int getDesign(){
        switch(this){
            case SELECT:
                return R.drawable.select_block;
            case FROM:
                return R.drawable.from_block;
            case WHERE:
                return R.drawable.where_block;
            case TABLE:
                return R.drawable.table_block;
            case ATTRIBUTE:
                return R.drawable.attribute_block;
        }
        return R.drawable.star_block;
    }

    public List<BlockT> getRightSuccessors(){
        List<BlockT> sucs = new ArrayList<>();
        switch(this){
            case SELECT:
                sucs.add(BlockT.STAR);
                sucs.add(BlockT.ATTRIBUTE);
                break;
            case FROM:
                sucs.add(BlockT.TABLE);
                break;
            case WHERE:
                break;
            case TABLE:
                break;
            case ATTRIBUTE:
                sucs.add(BlockT.ATTRIBUTE);
                break;
            case STAR:
                break;
        }
        return sucs;
    }

    public List<BlockT> getDownSuccessors(){
        List<BlockT> sucs = new ArrayList<>();
        switch(this){
            case SELECT:
                sucs.add(BlockT.FROM);
                break;
            case FROM:
                sucs.add(BlockT.WHERE);
                break;
            case WHERE:
                break;
            case TABLE:
                break;
            case ATTRIBUTE:
                break;
            case STAR:
                break;
        }
        return sucs;
    }

    public ImageView createView(Context context){
        ImageView view = new BlockView(context);
        view.setImageResource(getDesign());
        view.setTag(this);
        return view;
    }

    public boolean hasRightSuccessor(BlockT draggedBlock){
        return this.getRightSuccessors().contains(draggedBlock);
    }

    public boolean hasDownSuccessor(BlockT draggedBlock){
        return this.getDownSuccessors().contains(draggedBlock);
    }

    //bv --> draggedNod --> draggedlockT || view --> thisNode --> thisBlockT
   /* public static class OnDragListener implements View.OnDragListener {
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

                        if(thisBlock.getName().equals("SELECT")){
                            thisNode.printTree();
                        }
                        break;

                }
            }
            return true;
        }
    }*/

    /*public static class OnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)&&((ImageView)view).getDrawable()!=null){
                List<ImageView> treeMembers =
                Log.d("##################### count: ", Integer.toString(treeMembers.size()));
                for(int i=0; i<treeMembers.size(); i++){
                    Log.d("########################", "another one");
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(treeMembers.get(i));
                    treeMembers.get(i).startDragAndDrop(data, shadow, treeMembers.get(i), View.DRAG_FLAG_OPAQUE);
                    treeMembers.get(i).setVisibility(View.INVISIBLE);
                }
                return true;
            }else
                return false;
        }
    }*/

}
