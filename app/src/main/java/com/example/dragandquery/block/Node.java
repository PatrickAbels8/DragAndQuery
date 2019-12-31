package com.example.dragandquery.block;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/***
 *TODO:
 * -
 */


public class Node {
    private BlockT block = null;
    private Node rightChild = null;
    private Node downChild = null;
    private Node parent = null;
    //private int id;

    public Node(BlockT block) {
        this.block = block;
    }

    public void addRightChild(Node child){
        child.setParent(this);
        this.rightChild = child;
    }

    public void removeRightChild(Node child){
        child.setParent(null);
        this.rightChild = null;
    }

    public void addDownChild(Node child){
        child.setParent(this);
        this.downChild = child;
    }

    public void removeDownChild(Node child){
        child.setParent(null);
        this.downChild = null;
    }

    //todo for parser
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

    public String toTreeString(){
        String s = this.getBlock().getName();
        if(this.hasRight()) {
            s += " ";
            s += this.getRightChild().toTreeString();
        }
        if(this.hasDown()) {
            s += "\n";
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