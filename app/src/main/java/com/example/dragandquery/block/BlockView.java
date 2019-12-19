package com.example.dragandquery.block;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class BlockView extends AppCompatImageView {
    private Node node = null;

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
}
