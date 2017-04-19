/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehs;
import java.util.*;
/**
 *
 * @author Jianshu
 */
public class BTreeNode {
    String name;
    BTreeNode lChild;
    BTreeNode rChild;
    BTreeNode parent;
    double cp;
    
    private BTreeNode(){
        this.name = "TEMP";
        this.lChild = null;
        this.rChild = null;
        this.parent = null;
        this.cp = 0.0;
    }
    
    
    public BTreeNode(String name){
        this();
        this.setName(name);
    }
    
    public BTreeNode(BTreeNode copy){
        this(copy.getName());
    }
    
    public double updateCP(){
        if(this.lChild == null && this.rChild == null){
            return this.cp;
        }
        double left = (this.lChild != null) ? this.lChild.updateCP() : 0;
        double right = (this.rChild != null) ? this.rChild.updateCP() : 0;
        this.cp = this.mRound(left + right);
        return this.cp;
    }
    
    private double mRound(double d){
        double tmp = Math.round(d*100);
        return tmp/100;
    }
    
    private void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setParent(BTreeNode p){
        this.parent = p;
    }
    
    public boolean addChild(BTreeNode p){
        if(this.lChild == null){
            this.lChild = p;
        }
        else if(this.rChild == null){
            this.rChild = p;
        }
        else{
            return false;
        }
        p.setParent(this);
        return true;
    }
    
    private BTreeNode getLChild(){
        return this.lChild;
    }
    
    private BTreeNode getRChild(){
        return this.rChild;
    }
    
    public ArrayList<BTreeNode> getChildren(){
        ArrayList<BTreeNode> returnList = new ArrayList<BTreeNode>();
        returnList.add(this.getLChild());
        returnList.add(this.getRChild());
        
        return returnList;
    }
    
    public BTreeNode getParent(){
        return this.parent;
    }
    
    public boolean isRoot(){
        if(this.parent == null){
            return true;
        }
        else{
            return false;
        }           
    }
    
    public boolean setCP(double cp){
        if(cp >=0.0 && cp <= 100.0){
            this.cp = cp;
            return true;
        }
        else{
            return false;
        }
    }
    
    public double getCP(){
        return this.cp;
    }
}
