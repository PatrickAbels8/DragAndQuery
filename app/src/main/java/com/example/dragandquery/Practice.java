package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragandquery.block.Block;
import com.example.dragandquery.block.BlockAttribute;
import com.example.dragandquery.block.BlockSelect;

public class Practice extends AppCompatActivity {

    ImageButton btn;
    ImageView iv1;
    ImageView iv2;
    Context context;
    TextView text;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        context = getApplicationContext();

        btn = (ImageButton) findViewById(R.id.button);
        //iv1 = (ImageView) findViewById(R.id.iv1);
        //iv2 = (ImageView) findViewById(R.id.iv2);
        text = (TextView) findViewById(R.id.text);
        layout = (RelativeLayout) findViewById(R.id.layout);

        iv1 = (new BlockSelect()).createView(context);
        iv2 = (new BlockAttribute()).createView(context);
        layout.addView(iv1);
        layout.addView(iv2);

        iv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)&&((ImageView)view).getDrawable()!=null){
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadow, view, 0);
                    return true;
                }else{
                    return false;
                }
            }
        });

        iv2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)&&((ImageView)view).getDrawable()!=null){
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadow, view, 0);
                    return true;
                }else{
                    return false;
                }
            }
        });

        btn.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int dragID = dragEvent.getAction();
                switch(dragID) {
                    //what if night enters d4
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //Toast.makeText(getContext(), "entered noticed", Toast.LENGTH_LONG).show();
                        btn.setHovered(true);
                        break;
                    //what if night exited d4
                    case DragEvent.ACTION_DRAG_EXITED:
                        //Toast.makeText(getContext(), "exited noticed", Toast.LENGTH_LONG).show();
                        btn.setHovered(false);
                        break;
                    //what if night is finally dropped on d4
                    case DragEvent.ACTION_DROP:
                        Toast.makeText(context, "rop noticed", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        iv1.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                boolean viewFitsRight = false;
                boolean viewFitsLeft = false;

                Class<? extends Block> viewBlock = (Class<? extends Block>) view.getTag();
                Class<? extends Block> curViewBlock = (Class<? extends Block>) iv1.getTag();

                for (Class<? extends Block> b : ((Block) iv1.getTag()).getSuccessors()) {
                    if (viewBlock.equals(b)) {
                        viewFitsRight = true;
                    }
                }

                for (Class<? extends Block> b : ((Block) view.getTag()).getSuccessors()) {
                    if (curViewBlock.equals(b)) {
                        viewFitsLeft = true;
                    }
                }

                int dragID = dragEvent.getAction();
                switch (dragID) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //if pres and sucs match, make an animation
                        if (viewFitsLeft) {
                            text.setText("fits left");
                        }
                        if (viewFitsRight) {
                            text.setText("fits right");
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        //if there was, end the animation
                        text.setText("");
                        break;
                    case DragEvent.ACTION_DROP:
                        //if prex and sucs match, glue them together
                        if (viewFitsLeft) {
                            text.setText("DROP left");
                        }
                        if (viewFitsRight) {
                            text.setText("DROP right");
                        }
                        break;
                }
                return true;
            }
        });

                iv2.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View view, DragEvent dragEvent) {
                        boolean viewFitsRight = false;
                        boolean viewFitsLeft = false;

                        Class<? extends Block> viewBlock = (Class<? extends Block>) view.getTag();
                        Class<? extends Block> curViewBlock = (Class<? extends Block>) iv1.getTag();

                        for (Class<? extends Block> b : ((Block) iv1.getTag()).getSuccessors()) {
                            if (viewBlock.equals(b)) {
                                viewFitsRight = true;
                            }
                        }

                        for (Class<? extends Block> b : ((Block) view.getTag()).getSuccessors()) {
                            if (curViewBlock.equals(b)) {
                                viewFitsLeft = true;
                            }
                        }

                        int dragID = dragEvent.getAction();
                        switch (dragID) {
                            case DragEvent.ACTION_DRAG_ENTERED:
                                //if pres and sucs match, make an animation
                                if (viewFitsLeft) {
                                    text.setText("fits left");
                                }
                                if (viewFitsRight) {
                                    text.setText("fits right");
                                }
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                //if there was, end the animation
                                text.setText("");
                                break;
                            case DragEvent.ACTION_DROP:
                                //if prex and sucs match, glue them together
                                if (viewFitsLeft) {
                                    text.setText("DROP left");
                                }
                                if (viewFitsRight) {
                                    text.setText("DROP right");
                                }
                                break;
                        }
                        return true;
                    }
                });
    }
}
