package com.example.dragandquery.free;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.dragandquery.block.BlockView;

public class ClearView extends AppCompatImageView {

    private MyClearDragListener listener;

    public interface MyClearDragListener{

        void onMyDrag(ClearView me, BlockView him, float x, float y, int event);
    }

    public void setMyClearDragListener(MyClearDragListener listener) {
        this.listener = listener;
    }

    public void notifyListener(BlockView him, float x, float y, int ev){
        if(listener != null){
            listener.onMyDrag(this, him, x, y, ev);
        }
    }

    public ClearView(Context context) {
        super(context);
    }

    public ClearView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
