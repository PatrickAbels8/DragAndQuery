package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
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
import com.example.dragandquery.block.BlockFactory;

public class Practice extends AppCompatActivity {

    //coms
    ImageButton btn;
    ImageView iv1;
    ImageView iv2;
    Context context;
    TextView text;
    RelativeLayout layout;

    //vars
    int xDelta;
    int yDelta;
    Block bs;
    Block ba;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        context = getApplicationContext();

        //init stuff
        btn = (ImageButton) findViewById(R.id.button);
        //iv1 = (ImageView) findViewById(R.id.iv1);
        //iv2 = (ImageView) findViewById(R.id.iv2);
        text = (TextView) findViewById(R.id.text);
        layout = (RelativeLayout) findViewById(R.id.layout);

        bs = BlockFactory.getInstance().SELECT;
        ba = BlockFactory.getInstance().WHERE;
        iv1 = (bs).createView(context);
        iv2 = (ba).createView(context);
        iv2.setX(500);
        iv1.setY(500);
        layout.addView(iv1);
        layout.addView(iv2);

        btn.setTag("button");
        iv1.setTag("iv1");
        iv2.setTag("iv2");

        iv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int X = (int) motionEvent.getRawX();
                final int Y = (int) motionEvent.getRawY();
                switch(motionEvent.getAction()&MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        xDelta = X - lParams.leftMargin;
                        yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //set lable for clearing
                        /*ClipData data = ClipData.newPlainText("iv1", "");
                        View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                        view.startDragAndDrop(data, shadow, view, 0);*/

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = X - xDelta;
                        layoutParams.topMargin = Y - yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);


                        break;
                }
                layout.invalidate();
                return true;
            }
        });

        iv2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)&&((ImageView)view).getDrawable()!=null){
                    ClipData data = ClipData.newPlainText("iv2", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadow, view, 0);
                    return true;
                }else
                    return false;

            }
        });

        iv2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()){
                    case DragEvent.ACTION_DROP:
                        Toast.makeText(context, "DROP", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        /*iv2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int X = (int) motionEvent.getRawX();
                final int Y = (int) motionEvent.getRawY();
                switch(motionEvent.getAction()&MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        xDelta = X - lParams.leftMargin;
                        yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = X - xDelta;
                        layoutParams.topMargin = Y - yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                layout.invalidate();
                return true;
            }
        });*/

        btn.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                //Toast.makeText(context, "drag noticed", Toast.LENGTH_SHORT).show();
                int dragID = dragEvent.getAction();
                switch(dragID) {
                    //what if night enters d4
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Toast.makeText(context, "so entered me btn", Toast.LENGTH_SHORT).show();
                        btn.setHovered(true);
                        break;
                    //what if night exited d4
                    case DragEvent.ACTION_DRAG_EXITED:
                        Toast.makeText(context, "so exited me btn", Toast.LENGTH_SHORT).show();
                        btn.setHovered(false);
                        break;
                    //what if night is finally dropped on d4
                    case DragEvent.ACTION_DROP:
                        /*ClipData data = dragEvent.getClipData();
                        ClipDescription description = data.getDescription();
                        CharSequence droppedIv = description.getLabel();*/
                        Toast.makeText(context, "so dropped at me btn: "
                               //  +droppedIv
                                , Toast.LENGTH_SHORT).show();
                        //layout.removeView();
                }
                return true;
            }
        });

        /*iv2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                //Toast.makeText(context, "drag noticed", Toast.LENGTH_SHORT).show();
                int dragID = dragEvent.getAction();
                switch(dragID) {
                    //what if night enters d4
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Toast.makeText(context, "so entered me iv2", Toast.LENGTH_SHORT).show();
                        btn.setHovered(true);
                        break;
                    //what if night exited d4
                    case DragEvent.ACTION_DRAG_EXITED:
                        Toast.makeText(context, "so exited me iv2", Toast.LENGTH_SHORT).show();
                        btn.setHovered(false);
                        break;
                    //what if night is finally dropped on d4
                    case DragEvent.ACTION_DROP:
                        Toast.makeText(context, "so dropped at me iv2", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });*/

        iv1.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                /*boolean viewFitsRight = false;
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
*/
                int dragID = dragEvent.getAction();
                switch (dragID) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //if pres and sucs match, make an animation
                        /*if (viewFitsLeft) {
                            text.setText("fits left");
                        }
                        if (viewFitsRight) {
                            text.setText("fits right");
                        }*/
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        //if there was, end the animation
                        //text.setText("");
                        break;
                    case DragEvent.ACTION_DROP:
                        //if prex and sucs match, glue them together
                        /*if (viewFitsLeft) {
                            text.setText("DROP left");
                        }
                        if (viewFitsRight) {
                            text.setText("DROP right");
                        }*/
                        Toast.makeText(context, "DROP", Toast.LENGTH_SHORT).show();
                        Object o = dragEvent.getLocalState();
                        if(o instanceof ImageView) {
                            ImageView iv = (ImageView) o;
                            iv.setX(view.getX()+view.getWidth());
                            iv.setY(view.getY());
                        }
                        break;
                }
                return true;
            }
        });

                /*iv2.setOnDragListener(new View.OnDragListener() {
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
                });*/
   }
}
