package com.example.dragandquery.block;

import com.example.dragandquery.R;

public class BlockFrom implements Block {

    private static final Class<? extends Block>[] sucs = new Class[]{
            BlockTable.class};

    @Override
    public String getName() {
        return "FROM";
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
