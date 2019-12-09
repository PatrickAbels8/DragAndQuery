package com.example.dragandquery.block;

import com.example.dragandquery.R;

public  class BlockFactory {

    private static BlockFactory INSTANCE = null;

    /***
     * !!!!!!!!!!!! EVERY BLOCK HAS TO INIT*DEC HERE IN RIGHT ORDER (SUCCESSORS...) !!!!!!!!!!!
     * --> add in BlockFactory
     * --> add in Fragment_Blocks
     * --> add in Fragment_Query
     */

    public Block ATTRIBUTE;
    public Block FROM;
    public Block SELECT;
    public Block STAR;
    public Block TABLE;
    public Block WHERE;

    private BlockFactory() {
        TABLE = new Block("", R.drawable.table_block);
        FROM = new Block("FROM", R.drawable.from_block, TABLE);
        ATTRIBUTE = new Block("", R.drawable.attribute_block, ATTRIBUTE); //todo use the right factory pattern
        STAR = new Block("*", R.drawable.star_block);
        WHERE = new Block("WHERE", R.drawable.where_block);
        SELECT = new Block("SELECT", R.drawable.select_block, STAR, ATTRIBUTE);
    }

    public Block createBlock(String name){
        Block block = new Block("", R.drawable.ic_golf);

        switch(name){
            case "SELECT":
                block = new Block("SELECT", R.drawable.select_block, STAR, ATTRIBUTE);
                break;
            case "FROM":
                block = new Block("FROM", R.drawable.from_block, TABLE);
                break;
            case "WHERE":
                block = new Block("WHERE", R.drawable.where_block);
                break;
            case "STAR":
                block = new Block("*", R.drawable.star_block);
                break;
            case "ATTRIBUTE":
                block = new Block("", R.drawable.attribute_block, ATTRIBUTE);
                break;
            case "TABLE":
                block = new Block("", R.drawable.table_block);
                break;
        }

        return block;
    }

    public static BlockFactory getInstance() {
        if (INSTANCE==null)
            INSTANCE = new BlockFactory();
        return INSTANCE;
    }
}
