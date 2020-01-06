package com.example.dragandquery.block;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class BlockView extends AppCompatImageView {

    public void setxDis(float xDis) {
        this.xDis = xDis;
    }

    public void setyDis(float yDis) {
        this.yDis = yDis;
    }

    public float getxDis() {
        return xDis;
    }

    public float getyDis() {
        return yDis;
    }

    /***
     * Listener
     */
    public interface GroupDragListener{
       // void onGroupDrag(BlockView bv, BlockView v);
        void onGroupDrag(BlockView v, float x, float y);
        void setDistance(BlockView bv, float x, float y);
    }

    private Node node = null;
    private GroupDragListener listener;
    private float xDis = 0f;
    private float yDis = 0f;

    public BlockView(Context context) {
        super(context);
    }

    public BlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setListener(GroupDragListener listener) {
        this.listener = listener;
    }

    //public void notifyListener(BlockView v){
    public void notifyListener(float x, float y){
        Log.d("############### ????", " ??");
        if(listener != null){
            Log.d("############### LETS MOVE", "!!");
            //listener.onGroupDrag(this, v);
            listener.onGroupDrag(this, x, y);
        }
    }

    public void notifyListenerDistance(float x, float y){
        if(listener != null){
            listener.setDistance(this, x, y);
        }
    }


}
