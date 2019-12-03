package com.example.dragandquery.block;

import com.example.dragandquery.R;

public  class BlockFactory {

    private static BlockFactory INSTANCE = null;

    public Block SELECT;
    public Block STAR;
    public Block WHERE;

    /***
     * !!!!!!!!!!!! EVERY BLOCK HAS TO INIT*DEC HERE IN RIGHT ORDER (SUCCESSORS...) !!!!!!!!!!!
     */

    private BlockFactory() {
        STAR = new Block("*", R.drawable.star_block);
        WHERE = new Block("WHERE", R.drawable.where_block);
        SELECT = new Block("SELECT", R.drawable.select_block, STAR);
    }

    public static BlockFactory getInstance() {
        if (INSTANCE==null)
            INSTANCE = new BlockFactory();
        return INSTANCE;

    }


    void useBlock() {

        Block select = BlockFactory.getInstance().SELECT;

    }

    void useBlock2() {

        Block select2 = BlockFactory.getInstance().SELECT;



    }


}
