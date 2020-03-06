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
 * TODO when adding a block: fragment block (3x) + here + change map!!
 */

public enum BlockT {

    AND,
    AS,
    ATTRIBUTE,
    BETWEEN,
    COUNT,
    FROM,
    GREATER,
    GROUPBY,
    HAVING,
    IN,
    ISNULL,
    LIKE,
    LIMIT,
    NOT,
    ORDERBY,
    SELECT,
    SELECTDISTINCT,
    STAR,
    TABLE,
    WHERE;

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
            case LIMIT:
                return "LIMIT";
            case BETWEEN:
                return "BETWEEN";
            case GROUPBY:
                return "GROUP BY";
            case HAVING:
                return "HAVING";
            case IN:
                return "IN";
            case ISNULL:
                return "IS NULL";
            case LIKE:
                return "LIKE";
            case NOT:
                return "NOT";
            case ORDERBY:
                return "ORDER BY";
            case SELECTDISTINCT:
                return "SELECT DISTINCT";
            case COUNT:
                return "COUNT";
            case AND:
                return "AND";
            case GREATER:
                return ">";
            case AS:
                return "AS";
        }
        return "";
    }

    public static BlockT getBlock(int design){
        switch(design){
            case R.drawable.attribute_block:
                return BlockT.ATTRIBUTE;
            case R.drawable.from_block:
                return BlockT.FROM;
            case R.drawable.select_block:
                return BlockT.SELECT;
            case R.drawable.star_block:
                return BlockT.STAR;
            case R.drawable.table_block:
                return BlockT.TABLE;
            case R.drawable.where_block:
                return BlockT.WHERE;
            case R.drawable.limit_block:
                return BlockT.LIMIT;
            case R.drawable.between_block:
                return BlockT.BETWEEN;
            case R.drawable.groupby_block:
                return BlockT.GROUPBY;
            case R.drawable.having_block:
                return BlockT.HAVING;
            case R.drawable.in_block:
                return BlockT.IN;
            case R.drawable.isnull_block:
                return BlockT.ISNULL;
            case R.drawable.not_block:
                return BlockT.NOT;
            case R.drawable.orderby_block:
                return BlockT.ORDERBY;
            case R.drawable.selectdistinct_block:
                return BlockT.SELECTDISTINCT;
            case R.drawable.count_block:
                return BlockT.COUNT;
            case R.drawable.and_block:
                return BlockT.AND;
            case R.drawable.greater_block:
                return BlockT.GREATER;
            case R.drawable.as_block:
                return BlockT.AS;
        }
        return BlockT.SELECT;
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
            case LIMIT:
                return R.drawable.limit_block;
            case BETWEEN:
                return R.drawable.between_block;
            case GROUPBY:
                return R.drawable.groupby_block;
            case HAVING:
                return R.drawable.having_block;
            case IN:
                return R.drawable.in_block;
            case ISNULL:
                return R.drawable.isnull_block;
            case NOT:
                return R.drawable.not_block;
            case ORDERBY:
                return R.drawable.orderby_block;
            case SELECTDISTINCT:
                return R.drawable.selectdistinct_block;
            case COUNT:
                return R.drawable.count_block;
            case AND:
                return R.drawable.and_block;
            case GREATER:
                return R.drawable.greater_block;
            case AS:
                return R.drawable.as_block;
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
            case LIMIT:
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
            case LIMIT:
                break;
        }
        return sucs;
    }

    public BlockView createView(Context context){
        BlockView view = new BlockView(context);
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
