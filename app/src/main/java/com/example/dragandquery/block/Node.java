package com.example.dragandquery.block;

import android.util.Log;

import com.example.dragandquery.R;

import java.util.ArrayList;
import java.util.List;

/***
 *
 */


public class Node {
    private BlockT block = null;
    private Node rightChild = null;
    private Node downChild = null;
    private Node parent = null;
    private String value = "";

    public Node(BlockT block, String ... vals) {
        this.block = block;
        if(vals.length == 1)
            this.value = vals[0];
    }

    public void addRightChild(Node child){
        if(this.hasRight()){
            child.addRightChild(this.rightChild);
            this.rightChild.setParent(child.getLastRightTreeMember());
            child.getLastRightTreeMember().addRightChild(this.rightChild);
        }
        child.setParent(this);
        this.rightChild = child;

    }

    public void removeRightChild(Node child){
        child.setParent(null);
        this.rightChild = null;
    }

    public void addDownChild(Node child){
        if(this.hasDown()){
            child.addDownChild(this.downChild);
            this.downChild.setParent(child.getLastDownTreeMember());
            child.getLastDownTreeMember().addDownChild(this.downChild);
        }
        child.setParent(this);
        this.downChild = child;
    }

    public void removeDownChild(Node child){
        child.setParent(null);
        this.downChild = null;
    }

    public Node getLastRightTreeMember(){
        if(this.hasRight()){
            return this.rightChild.getLastRightTreeMember();
        }
        return this;
    }

    public Node getLastDownTreeMember(){
        if(this.hasDown()){
            return this.downChild.getLastDownTreeMember();
        }
        return this;
    }

    public void printTree(){
        Log.d("############## tree ###############", this.getBlock().getName());
        if(this.hasRight()) {
            this.getRightChild().printTree();
        }
        if(this.hasDown())
            this.getDownChild().printTree();
    }

    /***
     * main parser from blocks to sql(ite) query
     * @return string to perform query on db
     */
    public String toTreeString(){
        boolean brackets = this.getBlock()==BlockT.EMPTY && (
                this.getParent().getBlock().getCategory()==R.string.block_cat4 ||
                this.getParent().getBlock()==BlockT.IN ||
                this.getParent().getBlock()==BlockT.IFNULL);

        String s = "";

        //self
        if(this.getBlock()==BlockT.EMPTY && this.getParent().getBlock()==BlockT.EMPTY ||
            this.getBlock().getCategory()==R.string.block_cat4 && this.getParent().getBlock()==BlockT.EMPTY)
            s += ", ";
        if(brackets)
            s += "(";
        if(this.getBlock()==BlockT.NEQUAL)
            s += "!=";
        else
            s += this.getValue();
        if(brackets)
            s += ")";


        //right
        if(this.hasRight()) {
            s += " ";
            s += this.getRightChild().toTreeString();
        }

        //down
        if(this.hasDown()) {
            s += " ";
            s += this.getDownChild().toTreeString();
        }
        return s;
    }

    /***
     * parsing helper
     * @param old
     * @return
     * select schüler.nachname, lehrkraft.nachnae
     * from schüler full outer join lehrkraft
     *      on schüler.vorname = lehrkraft.vorname
     * ===>
     * select schüler.nachname, lehrkraft.nachname
     * from schüler left join lehrkraft
     * 	    on schüler.vorname = lehrkraft.vorname
     * union all
     * select schüler.nachname, lehrkraft.nachname
     * from lehrkraft left join schüler
     * 	    on schüler.vorname = lehrkraft.vorname
     * where schüler.vorname is null
     */
    public static String transform_foj(String old){
        if(!old.contains(BlockT.FULL_OUTER_JOIN.getName()))
            return old;
        String replaced = old.replace("full outer join", "left join");
        String left = " " + old.split(" from ")[1].split(" full ")[0].trim() + " ";
        String right = " " + old.split(" join ")[1].split(" on ")[0].trim() + " ";
        String condition = old.split(" on ")[1].split(" = ")[0];
        String second = replaced.split(left)[0] + right + replaced.split(left)[1].split(right)[0] + left + replaced.split(left)[1].split(right)[1];
        return replaced + " union all " + second + " where " + condition + " is null ";
    }

    public static String transform_roj(String old){
        if(!old.contains(BlockT.RIGHT_OUTER_JOIN.getName()))
            return old;
        String replaced = old.replace("right outer join", "left outer join");
        String left = " " + old.split(" from ")[1].split(" left ")[0].trim() + " ";
        String right = " " + old.split(" join ")[1].split(" on ")[0].trim() + " ";
        return replaced.split(left)[0] + right + replaced.split(left)[1].split(right)[0] + left + replaced.split(left)[1].split(right)[1];
    }

    public boolean hasRight(){
        return rightChild != null;
    }

    public boolean hasDown(){
        return downChild != null;
    }

    //if child right child of this return true, if is down child return false
    public boolean isRightButDown(Node child){
        if(this.rightChild != null)
            return this.rightChild.getBlock().getName().equals(child.getBlock().getName());
        return false;
    }

    public List<Node> getTreeMembers(){
        List<Node> members = new ArrayList<>();
        members.add(this);
        if(this.hasRight()) {
            members.addAll(this.getRightChild().getTreeMembers());
        }
        if(this.hasDown()) {
            members.addAll(this.getDownChild().getTreeMembers());
        }
        return members;
    }

    public BlockT getBlock() {
        return block;
    }

    public void setBlock(BlockT block) {
        this.block = block;
    }

    public String getValue(){
        return value;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public Node getDownChild() {
        return downChild;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
