package com.example.dragandquery.block;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class BlockView extends AppCompatImageView {


    /***
     * Listeners
     */
    public interface GroupDragListener{
        // void onGroupDrag(BlockView bv, BlockView v);

        void onGroupDrag(BlockView bv, float x, float y);
        void setDistance(BlockView bv, float x, float y); //distance from topleft to finger
    }
    public interface MyOnDragListener{

        void onMyDrag(BlockView me, BlockView him, float x, float y, int event);
    }

    //vars
    private Node node = null;
    private GroupDragListener listener;
    private MyOnDragListener mydrag_listener;
    private float xDis = 0f;
    private float yDis = 0f;
    public final static int MOVE = 1;
    public final static int UP = 2;

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

    public void setMydragListener(MyOnDragListener listener) {
        this.mydrag_listener = listener;
    }


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

    public void notifyMyDragListener(BlockView him, float x, float y, int ev){
        if(mydrag_listener != null){
            mydrag_listener.onMyDrag(this, him, x, y, ev);
        }
    }


}
