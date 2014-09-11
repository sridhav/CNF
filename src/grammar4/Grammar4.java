/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grammar4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

/**
 *
 * @author Sridhar
 */
public class Grammar4 {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws FileNotFoundException, Exception{
	
	String fname = "inp.txt";
	
	if(args.length==1){
            fname=args[0];
        }
        else{
            System.out.println("Usage java -jar Grammar4.jar <inp.txt>");
            System.exit(1);
        }
        
        boundryConditions(fname);
        
       //File Parser
        
        FileParsing m=new FileParsing(fname);

       //Decidability whether Grammar Generates Language or Nor

       GrammarDecidability n=new GrammarDecidability("transFile.txt");
        if(n.isGrammarEmpty()){
            System.out.println("L cannot be generated");
        }
        else{
            System.out.println("L can be generated");
        }

        //Class for removing useless, unit and epsilon production

        CheckGrammar inp = new CheckGrammar("transFile.txt",m);
        TestGrammar x=new TestGrammar();
        x.test(inp);
        //removes EPS productions
        inp.removeEps();

        inp.addToFile("removeEps.txt");

        //remove Unit productions
        inp.removeUnits();

        inp.addToFile("removeUnits.txt");


        //Garbage Collector
        m.DgarbageCollection();

     }

    //Boundry conditions
    private static void boundryConditions(String fname) {
        File f=new File(fname);
        boolean empty=false;
        if(f.length()==0){
            empty=true;
        }
        if(empty){
            System.out.println("Input File Cannot be Empty");
            System.exit(1);
        }
    }
	    
}    

