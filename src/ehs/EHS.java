/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehs;

/**
 *
 * @author Jianshu
 */
public class EHS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        BETree bet = new BETree(10);
        bet.outputCSV("./testOutput.csv",true);
        bet.simuBulk("./testOutBulk.txt");
    }
    
}
