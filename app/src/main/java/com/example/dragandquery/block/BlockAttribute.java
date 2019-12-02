package com.example.dragandquery.block;

import com.example.dragandquery.R;

public class BlockAttribute implements Block {

    private static final Class<? extends Block>[] sucs = new Class[]{
            BlockAttribute.class};

    @Override
    public String getName() {
        return "";
    } //todo read from dropdown

    @Override
    public int getDesign() {
        return R.drawable.attribute_block;
    }

    @Override
    public Class<? extends Block>[] getSuccessors() {
        return sucs;
    }
}
