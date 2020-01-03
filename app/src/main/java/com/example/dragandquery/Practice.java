package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragandquery.block.Block;
import com.example.dragandquery.block.BlockFactory;
import com.example.dragandquery.block.BlockT;
import com.example.dragandquery.block.BlockView;
import com.example.dragandquery.block.Node;

/***
 * TODO
 * - add nd
 */

public class Practice extends AppCompatActivity {

    //coms
    /*BlockView draggedView;
    BlockView otherView;*/
    Context context;
    TextView text;
    RelativeLayout layout;

    //RelativeLayout draggedLayout;

    RelativeLayout b;
    RelativeLayout c;
    RelativeLayout d;
    RelativeLayout e;

    ImageView b1;
    ImageView d1;

    //vars
    int xDelta;
    int yDelta;
    BlockT draggedBlock;
    BlockT otherBlock;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        context = getApplicationContext();

        //init stuff
        text = (TextView) findViewById(R.id.text);
        layout = (RelativeLayout) findViewById(R.id.layout);

        b = (RelativeLayout) findViewById(R.id.b);
        c = (RelativeLayout) findViewById(R.id.c);
        d = (RelativeLayout) findViewById(R.id.d);
        e = (RelativeLayout) findViewById(R.id.e);

        b1 = (ImageView) findViewById(R.id.b1);
        d1 = (ImageView) findViewById(R.id.d1);
        //b.setOnTouchListener(new Practice.MyOnTouchListener());
        //c.setOnTouchListener(new Practice.MyOnTouchListener());
        //d.setOnTouchListener(new Practice.MyOnTouchListener());
        //e.setOnTouchListener(new Practice.MyOnTouchListener());
        b1.setOnTouchListener(new Practice.MyOnSELECTTouchListener());
        d1.setOnTouchListener(new Practice.MyOnFROMTouchListener());


        /*draggedBlock = BlockT.SELECT;
        otherBlock = BlockT.ATTRIBUTE;
        draggedView = draggedBlock.createView(context);
        otherView = otherBlock.createView(context);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMarginStart(500);
        lp2.setMarginStart(800);
        layout.addView(draggedView, lp1);
        layout.addView(otherView, lp2);

        draggedLayout = new RelativeLayout(context);

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp3.setMarginStart(500);
        layout.addView(draggedLayout, lp3);

        layout.removeView(draggedView);
        draggedLayout.addView(draggedView);*/

        //Log.d("############# dragged_Layout dim", Integer.toString(draggedLayout.getChildCount()));

        //todo maybe listener on bv but on layouts
        /*draggedLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)){
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadow, view, View.DRAG_FLAG_OPAQUE);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                }else
                    return false;
            }
        });*/

        layout.setOnDragListener((view, dragEvent) -> {
            int dragID = dragEvent.getAction();
            switch(dragID) {
                //what if night enters d4
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                //what if night exited d4
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                //what if night is finally dropped on d4
                case DragEvent.ACTION_DROP:
                    Object o = dragEvent.getLocalState();
                    if(o instanceof BlockView) {
                        BlockView draggedView = (BlockView) o;
                        draggedView.setX(dragEvent.getX()-draggedView.getWidth()/2);
                        draggedView.setY(dragEvent.getY()-draggedView.getHeight()/2);
                        draggedView.setVisibility(View.VISIBLE);
                    }else if(o instanceof RelativeLayout) {
                        RelativeLayout draggedLayout = (RelativeLayout) o;
                        ((ViewGroup)draggedLayout.getParent()).removeView(draggedLayout);
                        layout.addView(draggedLayout);
                        draggedLayout.setX(dragEvent.getX()-draggedLayout.getWidth()/2);
                        draggedLayout.setY(dragEvent.getY()-draggedLayout.getHeight()/2);
                        draggedLayout.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        });

        /*otherView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.RIGHT_OF, draggedView.getId());
                layout.removeView(view);
                draggedLayout.addView(view, lp);
                return true;
            }
        });*/


   }

   public class MyOnTouchListener implements View.OnTouchListener{

       @Override
       public boolean onTouch(View view, MotionEvent motionEvent) {
           if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)){
               ClipData data = ClipData.newPlainText("", "");
               View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
               view.startDragAndDrop(data, shadow, view, View.DRAG_FLAG_OPAQUE);
               view.setVisibility(View.INVISIBLE);
               return true;
           }else
               return false;
       }
   }

    public class MyOnSELECTTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)){
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(b);
                view.startDragAndDrop(data, shadow, b, View.DRAG_FLAG_OPAQUE);
                //view.setVisibility(View.INVISIBLE);
                return true;
            }else
                return false;
        }
    }

    public class MyOnFROMTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if((motionEvent.getAction()==MotionEvent.ACTION_DOWN)){
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(d);
                view.startDragAndDrop(data, shadow, d, View.DRAG_FLAG_OPAQUE);
                //view.setVisibility(View.INVISIBLE);
                return true;
            }else
                return false;
        }
    }
}
