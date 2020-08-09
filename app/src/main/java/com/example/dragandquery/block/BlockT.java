package com.example.dragandquery.block;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.dragandquery.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 */

public enum BlockT {

    AND,
    AS,
    AVERAGE,
    COUNT,
    EQUAL,
    EMPTY,
    FROM,
    GREATER,
    GROUPBY,
    HAVING,
    IN,
    IFNULL,
    LIKE,
    LIMIT,
    MAX,
    MIN,
    NEQUAL,
    NOT,
    OR,
    ORDERBY,
    SELECT,
    DISTINCT,
    SMALLER,
    SUM,
    WHERE,
    XOR,
    INNER_JOIN,
    LEFT_OUTER_JOIN,
    RIGHT_OUTER_JOIN,
    FULL_OUTER_JOIN,
    ON;

    public int getCategory(){
        switch(this){
            case SELECT:
            case FROM:
            case GROUPBY:
            case HAVING:
            case LIMIT:
            case LIKE:
            case ORDERBY:
            case WHERE:
            case DISTINCT:
                return R.string.block_cat1;
            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case ON:
            case INNER_JOIN:
            case AS:
            case EMPTY:
                return R.string.block_cat2;
            case AND:
            case OR:
            case XOR:
            case EQUAL:
            case NEQUAL:
            case GREATER:
            case SMALLER:
            case IN:
            case IFNULL:
            case NOT:
                return R.string.block_cat3;
            case SUM:
            case AVERAGE:
            case MAX:
            case MIN:
            case COUNT:
                return R.string.block_cat4;
        }
        return 0;
    }

    public String getName() {
        switch(this){
            case MAX:
                return "MAX";
            case MIN:
                return "MIN";
            case OR:
                return "OR";
            case SUM:
                return "SUM";
            case XOR:
                return "XOR";
            case AVERAGE:
                return "AVG";
            case EQUAL:
                return "=";
            case NEQUAL:
                return "≠";
            case SELECT:
                return "SELECT";
            case FROM:
                return "FROM";
            case WHERE:
                return "WHERE";
            case LIMIT:
                return "LIMIT";
            case GROUPBY:
                return "GROUP BY";
            case HAVING:
                return "HAVING";
            case IN:
                return "IN";
            case IFNULL:
                return "IF NULL";
            case LIKE:
                return "LIKE";
            case NOT:
                return "NOT";
            case ORDERBY:
                return "ORDER BY";
            case DISTINCT:
                return "DISTINCT";
            case COUNT:
                return "COUNT";
            case AND:
                return "AND";
            case GREATER:
                return ">";
            case SMALLER:
                return "<";
            case AS:
                return "AS";
            case EMPTY:
                return "";
            case INNER_JOIN:
                return "inner join";
            case LEFT_OUTER_JOIN:
                return "left outer join";
            case RIGHT_OUTER_JOIN:
                return "right outer join";
            case FULL_OUTER_JOIN:
                return "full outer join";
            case ON:
                return "on";
        }
        return "";
    }

    public static BlockT getBlock(int design){
        switch(design){
            case R.drawable.nequal_block:
                return BlockT.NEQUAL;
            case R.drawable.like_block:
                return BlockT.LIKE;
            case R.drawable.max_block:
                return BlockT.MAX;
            case R.drawable.min_block:
                return BlockT.MIN;
            case R.drawable.or_block:
                return BlockT.OR;
            case R.drawable.sum_block:
                return BlockT.SUM;
            case R.drawable.xor_block:
                return BlockT.XOR;
            case R.drawable.equal_block:
                return BlockT.EQUAL;
            case R.drawable.avg_block:
                return BlockT.AVERAGE;
            case R.drawable.from_block:
                return BlockT.FROM;
            case R.drawable.select_block:
                return BlockT.SELECT;
            case R.drawable.where_block:
                return BlockT.WHERE;
            case R.drawable.limit_block:
                return BlockT.LIMIT;
            case R.drawable.groupby_block:
                return BlockT.GROUPBY;
            case R.drawable.having_block:
                return BlockT.HAVING;
            case R.drawable.in_block:
                return BlockT.IN;
            case R.drawable.ifnull_block:
                return BlockT.IFNULL;
            case R.drawable.not_block:
                return BlockT.NOT;
            case R.drawable.orderby_block:
                return BlockT.ORDERBY;
            case R.drawable.distinct_block:
                return BlockT.DISTINCT;
            case R.drawable.count_block:
                return BlockT.COUNT;
            case R.drawable.and_block:
                return BlockT.AND;
            case R.drawable.greater_block:
                return BlockT.GREATER;
            case R.drawable.smaller_block:
                return BlockT.SMALLER;
            case R.drawable.as_block:
                return BlockT.AS;
            case R.drawable.empty_block:
                return BlockT.EMPTY;
            case R.drawable.inner_join_block:
                return BlockT.INNER_JOIN;
            case R.drawable.left_outer_join_block:
                return BlockT.LEFT_OUTER_JOIN;
            case R.drawable.right_outer_join_block:
                return BlockT.RIGHT_OUTER_JOIN;
            case R.drawable.full_outer_join_block:
                return BlockT.FULL_OUTER_JOIN;
            case R.drawable.on_block:
                return BlockT.ON;
        }
        return BlockT.SELECT;
    }

    //@DrawableRes
    public int getDesign(){
        switch(this){
            case NEQUAL:
                return R.drawable.nequal_block;
            case LIKE:
                return R.drawable.like_block;
            case MAX:
                return R.drawable.max_block;
            case MIN:
                return R.drawable.min_block;
            case OR:
                return R.drawable.or_block;
            case SUM:
                return R.drawable.sum_block;
            case XOR:
                return R.drawable.xor_block;
            case EQUAL:
                return R.drawable.equal_block;
            case AVERAGE:
                return R.drawable.avg_block;
            case SELECT:
                return R.drawable.select_block;
            case FROM:
                return R.drawable.from_block;
            case WHERE:
                return R.drawable.where_block;
            case LIMIT:
                return R.drawable.limit_block;
            case GROUPBY:
                return R.drawable.groupby_block;
            case HAVING:
                return R.drawable.having_block;
            case IN:
                return R.drawable.in_block;
            case IFNULL:
                return R.drawable.ifnull_block;
            case NOT:
                return R.drawable.not_block;
            case ORDERBY:
                return R.drawable.orderby_block;
            case DISTINCT:
                return R.drawable.distinct_block;
            case COUNT:
                return R.drawable.count_block;
            case AND:
                return R.drawable.and_block;
            case GREATER:
                return R.drawable.greater_block;
            case SMALLER:
                return R.drawable.smaller_block;
            case AS:
                return R.drawable.as_block;
            case EMPTY:
                return R.drawable.empty_block;
            case INNER_JOIN:
                return R.drawable.inner_join_block;
            case LEFT_OUTER_JOIN:
                return R.drawable.left_outer_join_block;
            case RIGHT_OUTER_JOIN:
                return R.drawable.right_outer_join_block;
            case FULL_OUTER_JOIN:
                return R.drawable.full_outer_join_block;
            case ON:
                return R.drawable.on_block;
        }
        return R.drawable.empty_block;
    }

    public List<BlockT> getRightSuccessors(){
        List<BlockT> sucs = new ArrayList<>();
        switch(this){
            case NEQUAL:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case MAX:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case MIN:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case OR:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL, BlockT.NOT));
                break;
            case SUM:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case XOR:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL, BlockT.NOT));
                break;
            case AND:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL, BlockT.NOT));
                break;
            case AS:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case EQUAL:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case AVERAGE:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case COUNT:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case FROM:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case GREATER:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case GROUPBY:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.AVERAGE, BlockT.COUNT, BlockT.MAX, BlockT.MIN, BlockT.SUM));
                break;
            case HAVING:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.AVERAGE, BlockT.COUNT, BlockT.MAX, BlockT.MIN, BlockT.SUM, BlockT.IFNULL, BlockT.NOT));
                break;
            case IN:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case IFNULL:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case LIKE:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case LIMIT:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case NOT:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case ORDERBY:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.AVERAGE, BlockT.COUNT, BlockT.MAX, BlockT.MIN, BlockT.SUM));
                break;
            case SELECT:
                sucs.addAll(Arrays.asList(BlockT.DISTINCT, BlockT.EMPTY, BlockT.AVERAGE, BlockT.COUNT, BlockT.MAX, BlockT.MIN, BlockT.SUM, BlockT.IFNULL));
                break;
            case DISTINCT:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.AVERAGE, BlockT.COUNT, BlockT.MAX, BlockT.MIN, BlockT.SUM, BlockT.IFNULL));
                break;
            case WHERE:
                sucs.addAll(Arrays.asList(BlockT.IFNULL, BlockT.EMPTY, BlockT.AVERAGE, BlockT.COUNT, BlockT.MAX, BlockT.MIN, BlockT.SUM, BlockT.NOT));
                break;
            case SMALLER:
                sucs.addAll(Arrays.asList(BlockT.EMPTY, BlockT.IFNULL));
                break;
            case EMPTY:
                sucs.addAll(Arrays.asList(BlockT.ON, BlockT.NEQUAL, BlockT.EMPTY, BlockT.SMALLER, BlockT.EQUAL, BlockT.GREATER, BlockT.AND, BlockT.AS, BlockT.AVERAGE, BlockT.COUNT, BlockT.IN, BlockT.IFNULL, BlockT.LIKE, BlockT.MAX, BlockT.MIN, BlockT.NOT, BlockT.OR, BlockT.SUM, BlockT.XOR));
                break;
            case INNER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case LEFT_OUTER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case RIGHT_OUTER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case FULL_OUTER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
            case ON:
                sucs.addAll(Arrays.asList(BlockT.EMPTY));
                break;
        }
        return sucs;
    }

    public List<BlockT> getDownSuccessors(){
        List<BlockT> sucs = new ArrayList<>();
        switch(this){
            case NEQUAL:
                sucs.addAll(Arrays.asList());
                break;
            case MAX:
                sucs.addAll(Arrays.asList());
                break;
            case MIN:
                sucs.addAll(Arrays.asList());
                break;
            case OR:
                sucs.addAll(Arrays.asList());
                break;
            case SUM:
                sucs.addAll(Arrays.asList());
                break;
            case XOR:
                sucs.addAll(Arrays.asList());
                break;
            case EQUAL:
                sucs.addAll(Arrays.asList());
                break;
            case AND:
                sucs.addAll(Arrays.asList());
                break;
            case AS:
                sucs.addAll(Arrays.asList());
                break;
            case AVERAGE:
                sucs.addAll(Arrays.asList());
                break;
            case COUNT:
                sucs.addAll(Arrays.asList());
                break;
            case FROM:
                sucs.addAll(Arrays.asList(BlockT.GROUPBY, BlockT.LIMIT, BlockT.ORDERBY, BlockT.WHERE, BlockT.INNER_JOIN, BlockT.LEFT_OUTER_JOIN, BlockT.RIGHT_OUTER_JOIN, BlockT.FULL_OUTER_JOIN));
                break;
            case GREATER:
                sucs.addAll(Arrays.asList());
                break;
            case GROUPBY:
                sucs.addAll(Arrays.asList(BlockT.HAVING, BlockT.LIMIT, BlockT.ORDERBY));
                break;
            case HAVING:
                sucs.addAll(Arrays.asList(BlockT.LIMIT, BlockT.ORDERBY));
                break;
            case IN:
                sucs.addAll(Arrays.asList());
                break;
            case IFNULL:
                sucs.addAll(Arrays.asList());
                break;
            case LIKE:
                sucs.addAll(Arrays.asList());
                break;
            case LIMIT:
                sucs.addAll(Arrays.asList());
                break;
            case NOT:
                sucs.addAll(Arrays.asList());
                break;
            case ORDERBY:
                sucs.addAll(Arrays.asList(BlockT.LIMIT));
                break;
            case SELECT:
                sucs.addAll(Arrays.asList(BlockT.FROM));
                break;
            case DISTINCT:
                sucs.addAll(Arrays.asList());
                break;
            case WHERE:
                sucs.addAll(Arrays.asList(BlockT.GROUPBY, BlockT.LIMIT, BlockT.ORDERBY));
                break;
            case SMALLER:
                sucs.addAll(Arrays.asList());
                break;
            case EMPTY:
                sucs.addAll(Arrays.asList());
                break;
            case ON:
                sucs.addAll(Arrays.asList());
                break;
            case INNER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.GROUPBY, BlockT.LIMIT, BlockT.ORDERBY, BlockT.WHERE, BlockT.INNER_JOIN, BlockT.LEFT_OUTER_JOIN, BlockT.RIGHT_OUTER_JOIN, BlockT.FULL_OUTER_JOIN));
                break;
            case LEFT_OUTER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.GROUPBY, BlockT.LIMIT, BlockT.ORDERBY, BlockT.WHERE, BlockT.INNER_JOIN, BlockT.LEFT_OUTER_JOIN, BlockT.RIGHT_OUTER_JOIN, BlockT.FULL_OUTER_JOIN));
                break;
            case RIGHT_OUTER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.GROUPBY, BlockT.LIMIT, BlockT.ORDERBY, BlockT.WHERE, BlockT.INNER_JOIN, BlockT.LEFT_OUTER_JOIN, BlockT.RIGHT_OUTER_JOIN, BlockT.FULL_OUTER_JOIN));
                break;
            case FULL_OUTER_JOIN:
                sucs.addAll(Arrays.asList(BlockT.GROUPBY, BlockT.LIMIT, BlockT.ORDERBY, BlockT.WHERE, BlockT.INNER_JOIN, BlockT.LEFT_OUTER_JOIN, BlockT.RIGHT_OUTER_JOIN, BlockT.FULL_OUTER_JOIN));
                break;
        }
        return sucs;
    }

    public BlockView createView(Context context, String ... vals){
        BlockView view = new BlockView(context);
        view.setBackgroundResource(getDesign());
        if(vals.length==0)
            view.setText(getName());
        else
            view.setText(vals[0]);

        if(Arrays.asList(BlockT.SELECT, BlockT.DISTINCT, BlockT.FROM,
                BlockT.WHERE, BlockT.LIMIT, BlockT.HAVING, BlockT.AS,
                BlockT.GROUPBY, BlockT.ORDERBY).contains(this))
            view.setTextColor(ContextCompat.getColor(context, R.color.textcolor_white));
        else
            view.setTextColor(ContextCompat.getColor(context, R.color.textcolor_black));
        if(this == BlockT.EMPTY)
            view.setPadding(dp_to_int(25), dp_to_int(20), dp_to_int(25), dp_to_int(20));
        else
            view.setPadding(dp_to_int(20), dp_to_int(20), dp_to_int(25), dp_to_int(20));
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        view.setTag(this);
        return view;
    }

    public boolean hasRightSuccessor(BlockT draggedBlock){
        if(this == BlockT.EMPTY || draggedBlock == BlockT.EMPTY)
            return true;
        return this.getRightSuccessors().contains(draggedBlock);
    }

    public boolean hasDownSuccessor(BlockT draggedBlock){
        return this.getDownSuccessors().contains(draggedBlock);
    }

    //helper
    public int dp_to_int(int dp){
        /*float scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp*scale+0.5f);
        return pix;*/
        return dp+5; //todo
    }

}
