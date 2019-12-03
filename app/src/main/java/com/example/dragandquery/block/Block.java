package com.example.dragandquery.block;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.example.dragandquery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Blockv2 {
    private final String name;
    @DrawableRes private final int design;
    private final List<Blockv2> successors;

    public Blockv2(String name, @DrawableRes int design, Blockv2 ... sucs) {
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
        Blockv2 blockv2 = (Blockv2) o;
        return Objects.equals(name, blockv2.name);
    }

    @Override
    public String toString() {
        return "Blockv2{" +
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

    public boolean hasSuccessor(Blockv2 draggedBlock){
        return this.successors.contains((draggedBlock));
    }

    public class OnDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DROP:
                    Object o = dragEvent.getLocalState();
                    if(o instanceof ImageView) {
                        ImageView iv = (ImageView) o;
                        Blockv2 draggedBlock = (Blockv2) iv.getTag(); //TODO save cast
                        Blockv2 thisBlock = (Blockv2) view.getTag();  //TODO save cast
                        if (thisBlock.hasSuccessor(draggedBlock)) {
                            iv.setX(view.getX()+view.getWidth());
                            iv.setY(view.getY());
                        }
                    }
            }

            return false;
        }
    }
}
