package com.example.dragandquery.block;

import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.example.dragandquery.R;

import java.util.ArrayList;
import java.util.List;

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
                return "TABLE";
            case ATTRIBUTE:
                return "ATTRIBUTE";
        }
        return "*";
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

    public List<BlockT> getSuccessors(){
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

    public ImageView createView(Context context){
        ImageView view = new ImageView(context);
        view.setImageResource(getDesign());
        view.setTag(this);
        return view;
    }

    public boolean hasSuccessor(BlockT draggedBlock){
        return this.getSuccessors().contains(draggedBlock);
    }

    public static class OnDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            Object o = dragEvent.getLocalState();
            if(o instanceof ImageView) {
                ImageView iv = (ImageView) o;
                BlockT draggedBlock = (BlockT) iv.getTag();
                BlockT thisBlock = (BlockT) view.getTag();
                boolean fits_right = thisBlock.hasSuccessor(draggedBlock);
                boolean fits_left = draggedBlock.hasSuccessor(thisBlock);
                boolean isOnRightSide = iv.getX() > view.getX();

                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (isOnRightSide && fits_right) {
                            iv.setVisibility(View.VISIBLE);
                            iv.setAlpha(0.5f);
                            iv.setX(view.getX()+view.getWidth());
                            iv.setY(view.getY());
                        } else if(!isOnRightSide && fits_left){
                            iv.setVisibility(View.VISIBLE);
                            iv.setAlpha(0.5f);
                            iv.setX(view.getX()-view.getWidth());
                            iv.setY(view.getY());
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        iv.setVisibility(View.INVISIBLE);
                        iv.setAlpha(1f);
                        break;
                    case DragEvent.ACTION_DROP:
                        iv.setAlpha(1f);
                        if (isOnRightSide && fits_right) {
                            iv.setX(view.getX()+view.getWidth()); //todo has to fit to drawables
                            iv.setY(view.getY());
                            //todo thisBlock.addFollower(draggedBlock);
                            //sounds
                            MediaPlayer.create(iv.getContext(), R.raw.dropblock).start();
                        } else if(!isOnRightSide && fits_left){
                            iv.setX(view.getX()-view.getWidth()); //todo has to fit to drawables
                            iv.setY(view.getY());
                            //todo thisBlock.addFollower(draggedBlock);
                            //sounds
                            MediaPlayer.create(iv.getContext(), R.raw.dropblock).start();
                        } else{
                            iv.setX(view.getX());
                            iv.setY(view.getY()+view.getHeight()); //todo maybe somewhere else
                        }
                        iv.setVisibility(View.VISIBLE);
                        break;
                }
            }
            return true;
        }
    }

    public static class OnTouchListener implements View.OnTouchListener{
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
    }

}
