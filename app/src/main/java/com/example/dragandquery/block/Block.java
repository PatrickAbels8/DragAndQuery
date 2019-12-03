package com.example.dragandquery.block;

import android.content.ClipData;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;

import com.example.dragandquery.Fragment_Query;
import com.example.dragandquery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/***
 * TODO:
 * - draglistener should draw whole blocksection instead of a single block, when he already has successors
 */
public class Block {
    private final String name;
    @DrawableRes private final int design;
    private final List<Block> successors;

    public Block(String name, @DrawableRes int design, Block... sucs) {
        this.name = name;
        this.design = design;
        this.successors = new ArrayList<>(sucs.length);

        for(int i=0; i<sucs.length; i++){
            successors.add(sucs[i]);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block blockv2 = (Block) o;
        return Objects.equals(name, blockv2.name);
    }

    @Override
    public String toString() {
        return "Block{" +
                "name='" + name + '\'' +
                ", successors=" + successors +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public int getDesign() {
        return design;
    }

    public ImageView createView(Context context){
        ImageView view = new ImageView(context);
        view.setImageResource(getDesign());
        view.setTag(this);
        return view;
    }

    public boolean hasSuccessor(Block draggedBlock){
        return this.successors.contains((draggedBlock));
    }

    public static class OnDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            Object o = dragEvent.getLocalState();
            if(o instanceof ImageView) {
                ImageView iv = (ImageView) o;
                Block draggedBlock = (Block) iv.getTag();
                Block thisBlock = (Block) view.getTag();
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (thisBlock.hasSuccessor(draggedBlock)) {
                            iv.setVisibility(View.VISIBLE);
                            iv.setAlpha(0.5f);
                            iv.setX(view.getX()+view.getWidth());
                            iv.setY(view.getY());
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        iv.setVisibility(View.INVISIBLE);
                        iv.setAlpha(1f);
                        break;
                    case DragEvent.ACTION_DROP:
                        iv.setAlpha(1f);
                        if (thisBlock.hasSuccessor(draggedBlock)) {
                            iv.setX(view.getX()+view.getWidth()); //todo has to fit to drawables
                            iv.setY(view.getY()); //todo also left of thisBlock
                            //sounds
                            MediaPlayer.create(iv.getContext(), R.raw.dropblock).start();
                        }else{
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
                //todo remove img while dragging and shadow not transparent
                return true;
            }else
                return false;
        }
    }
}
