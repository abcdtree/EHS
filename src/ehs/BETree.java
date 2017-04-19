/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehs;
import java.util.*;
import java.io.*;
/**
 *
 * @author Jianshu
 */
public class BETree {
    BTreeNode root;
    int numberOfMutations;
    ArrayList<String> mutations;
    ArrayList<BTreeNode> leaves;
    
    private BETree(){
        this.root = null;
        this.numberOfMutations = 0;
        this.mutations = new ArrayList<String>();
        this.leaves = new ArrayList<BTreeNode>();
        
    }
    
    public BETree(int num){
        this.numberOfMutations = num;
        ArrayList<String> names = BETree.giveSimuNames(num);
        this.mutations = names;
        this.root = new BTreeNode("|ROOT|");
        ArrayList<BTreeNode> nodes = new ArrayList<BTreeNode>();
        nodes.add(this.root);
        Random mRd = new Random();
        for(String name: names){
            ArrayList<BTreeNode> newNodes = new ArrayList<BTreeNode>();
            
            int i = mRd.nextInt(nodes.size());
            for(int j = 0; j < nodes.size(); j++){
                String tn = nodes.get(i).getName();
                BTreeNode current = nodes.get(j);
                if(j == i){
                    BTreeNode tmp = new BTreeNode(tn + "-" + name);
                    current.addChild(tmp);
                    newNodes.add(tmp);
                }
                BTreeNode copy = new BTreeNode(current);
                current.addChild(copy);
                newNodes.add(copy);
                
            }
            nodes = newNodes;
        }
        
        int k = nodes.size();
        //random 1 - high purity and top mutations have high probability to get high vaf
        /*
        double end = 100;
        for(int i = 0; i < k-1; i++){
            double rd = mRd.nextDouble();
            double tcp = Math.round(rd*end*100);
            tcp = tcp/100;
            nodes.get(i).setCP(tcp);
            end = end - tcp;
        }
        end = Math.round(end *100);
        nodes.get(k-1).setCP(end/100);
        */
        //random 2 - pure random
        ArrayList<Double> rdList = new ArrayList<Double>();
        rdList.add(new Double(0));
        for(int i = 0; i < k-1; i++){
            double rd = mRd.nextDouble();
            double tcp = this.mRound(rd*100);
            rdList.add(tcp);
            //System.out.println(tcp);
        }
        rdList.add(new Double(100));
        Collections.sort(rdList);
        for(int i= 1; i < rdList.size(); i++){
            nodes.get(i-1).setCP(this.mRound(rdList.get(i) - rdList.get(i-1)));
        }
        
        /*
        for(BTreeNode btn: nodes){
            System.out.println(btn.getName() + "_" + btn.getCP());
        }*/
        
        this.root.updateCP();
        this.leaves = nodes;
    }
    
    private double mRound(double d){
        double tmp = Math.round(d*100);
        return tmp/100;
    }
    
    public void simuBulk(String outFile){
        double tumorP = 100 - this.leaves.get(this.numberOfMutations-1).getCP();
        StringBuilder sb = new StringBuilder();
        sb.append("Tumor Purity:\t"+tumorP+"\n");
        sb.append("Number of Mutations:\t" + this.numberOfMutations + "\n");
        sb.append("Mutations\tVAF\n");
        HashMap<String, Double> vaf = new HashMap<String, Double>();
        for(BTreeNode btn: this.leaves){
            String[] names = btn.getName().split("-");
            double cp = btn.getCP();
            for(String name: names){
                if(!vaf.containsKey(name)){
                    vaf.put(name, cp);
                }
                else{
                    vaf.put(name, this.mRound(cp+vaf.get(name)));
                }
            }
        }
        
        for(String name: this.mutations){
            sb.append(name + "\t" + vaf.get(name) + "\n");
        }
        
        
        try(FileWriter fileWriter = new FileWriter(outFile)){
            fileWriter.write(sb.toString());
            fileWriter.flush();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public static ArrayList<String> giveSimuNames(int num){
        ArrayList<String> names = new ArrayList<String>();
        for(int i = 0; i < num; i++){
            String mut = "";
            int t = i;
            while(t >= 0){
                int p = t%26 + 65;
                mut = Character.toString((char) p) + mut;
                t = t/26 -1;
                //System.out.println(t);
            }
            names.add("|"+mut+"|");
        }
        return names;
    }
    
    public void outputCSV(String filePath){
        ArrayList<String> rows = new ArrayList<String>();
        rows.add("id,xxx,gene\n");
        recBuildCSVRow(this.root, "1", rows);
        try(FileWriter fileWriter = new FileWriter(filePath)){
            for(String row: rows){
                fileWriter.append(row);
            }
            fileWriter.flush();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public void outputCSV(String filePath, boolean cp){
        ArrayList<String> rows = new ArrayList<String>();
        rows.add("id,xxx,gene\n");
        recBuildCSVRowCP(this.root, "1", rows);
        try(FileWriter fileWriter = new FileWriter(filePath)){
            for(String row: rows){
                fileWriter.append(row);
            }
            fileWriter.flush();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    private void recBuildCSVRow(BTreeNode current, String id, ArrayList<String> rows){
        if(current != null){
            rows.add(id +",XXX,"+current.getName()+"\n");
            int count = 0;
            for(BTreeNode child: current.getChildren()){
                recBuildCSVRow(child, id + Integer.toString(count), rows);
                count ++;
            }
        }
    }
    
    private void recBuildCSVRowCP(BTreeNode current, String id, ArrayList<String> rows){
        if(current != null){
            rows.add(id +",XXX,"+current.getName()+"_"+current.getCP()+"\n");
            int count = 0;
            for(BTreeNode child: current.getChildren()){
                recBuildCSVRowCP(child, id + Integer.toString(count), rows);
                count ++;
            }
        }
    }
    
    
    
    
}
