package com.example.dragandquery.block;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public interface Block{
    String getName();
    @DrawableRes int getDesign();
    Class<? extends Block>[] getSuccessors();
    default ImageView createView(Context context){
        ImageView view = new ImageView(context);
        view.setImageResource(getDesign());
        view.setTag(this);
        return view;
    }
}
