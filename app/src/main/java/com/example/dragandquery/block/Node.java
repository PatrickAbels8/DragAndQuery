package com.example.dragandquery.block;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/***
 *TODO: edit blocks miss their text in toTreeString
 */


public class Node {
    private BlockT block = null;
    private Node rightChild = null;
    private Node downChild = null;
    private Node parent = null;
    private String value = "";
    //private int id;

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
        else {
            //Log.d("############## tree ###############", "no right");
        }
        if(this.hasDown())
            this.getDownChild().printTree();
        else{
            //Log.d("############## tree ###############", "no down");
        }
    }

    /***
     * main parser from blocks to sql query
     * @return string to perform query on db
     */
    public String toTreeString(){
        String s = this.getValue();
        if(this.hasRight()) {
            s += " ";
            s += this.getRightChild().toTreeString();
        }
        if(this.hasDown()) {
            s += " ";
            s += this.getDownChild().toTreeString();
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

    /***
     * getter & setter
     */
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

    /*public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }*/
}
