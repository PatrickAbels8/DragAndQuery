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
     * main parser from blocks to sql query
     * :param in: true if IN has to be closed yet, false else
     * @return string to perform query on db
     */
    public String toTreeString(boolean in){
        boolean opening_in = this.getBlock()==BlockT.EMPTY && this.getParent().getBlock()==BlockT.IN;
        boolean closing_in = in &&this.getBlock()!=BlockT.EMPTY && this.getParent().getBlock()==BlockT.EMPTY;
        String s = "";

        //self
        if(this.getBlock()==BlockT.EMPTY && this.getParent().getBlock()==BlockT.EMPTY ||
            this.getBlock().getCategory()==R.string.block_cat4 && this.getParent().getBlock()==BlockT.EMPTY)
            s += ", ";
        if(this.getBlock()==BlockT.EMPTY && this.getParent().getBlock().getCategory()==R.string.block_cat4 ||
            opening_in)
            s += "(";
        s += this.getValue();
        if(this.getBlock()==BlockT.EMPTY && this.getParent().getBlock().getCategory()==R.string.block_cat4 ||
            closing_in)
            s += ")";


        //right
        if(this.hasRight()) {
            s += " ";
            if(opening_in || closing_in)
                s += this.getRightChild().toTreeString(!in);
            else
                s += this.getRightChild().toTreeString(in);
        }

        //down
        if(this.hasDown()) {
            s += " ";
            if(opening_in || closing_in)
                s += this.getDownChild().toTreeString(!in);
            else
                s += this.getDownChild().toTreeString(in);
        }
        return s;
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
