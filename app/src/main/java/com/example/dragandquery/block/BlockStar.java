package com.example.dragandquery.block;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.example.dragandquery.R;
import com.example.dragandquery.block.Block;

import java.util.List;

public class BlockSelect implements Block {

    private static final Class<? extends Block>[] sucs = new Class[]{BlockAttribute.class};

    @Override
    public String getName() {
        return "SELECT";
    }

    @Override
    public int getDesign() {
        return R.drawable.select_block;
    }

    @Override
    public Class<? extends Block>[] getSuccessors() {
        return sucs;
    }
}
