package com.example.dragandquery.block;

import com.example.dragandquery.R;

public class BlockStar implements Block {

    private static final Class<? extends Block>[] sucs = new Class[]{};

    @Override
    public String getName() {
        return "*";
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
