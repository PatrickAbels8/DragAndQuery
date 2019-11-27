package com.example.dragandquery.block;

import com.example.dragandquery.R;

public class BlockWhere implements Block {

    private static final Class<? extends Block>[] sucs = new Class[]{};

    @Override
    public String getName() {
        return "WHERE";
    }

    @Override
    public int getDesign() {
        return R.drawable.where_block;
    }

    @Override
    public Class<? extends Block>[] getSuccessors() {
        return sucs;
    }
}
