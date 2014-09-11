
package grammar4;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 *
 * @author Sridhar
 */
public class CheckGrammar{
    
   
    FileParsing m;
    
    public CheckGrammar(String fname,FileParsing m) throws IOException{
	
	 prod = new TreeSet();
	    readFile(fname);
	    createMap();
         //   setNonTerminals();
            this.m=m;
          
    }
    /*
        removes useless Symbols
    */
    public void removeUseless(){
        boolean haveUseless = false;
         do {
             Set vAccessible = new HashSet();
             vAccessible.add(start);
             
             LinkedList queue = new LinkedList();
             queue.addLast(start);
             while (!queue.isEmpty()) {
                 char a =(char) queue.removeLast();
                 Iterator itL = prod.iterator();
                 while (itL.hasNext()) {
                     Production lmk=(Production) itL.next();
                     char lx=lmk.getLeftSide();
                     if(lx==a){
                     String w = lmk.getRightSide();
                     for(int i=0;i<w.length();i++){
                     if(w.charAt(i)>='A' && w.charAt(i)<='Z'){
                         char b=w.charAt(i);
                         if (vAccessible.add(b)) {
                             queue.addLast(b);
                         }
                     }
                 }
                }
             }
                 
            Set vProductive = new HashSet();
             boolean changed;
             do {
                 changed = false;
                 Iterator itV = nonTerms.iterator();
                 while (itV.hasNext()) {
                     Production x = (Production)itV.next();
                     if (!vProductive.contains(x)) {
                         Iterator itP = prod.iterator();
                         while (itP.hasNext()) {
                             String w = ((Production)itP.next()).getRightSide();
                             
                             boolean isProductive = true;
                             for(int i=0;i<w.length();i++){
                                if(w.charAt(i)>='A' && w.charAt(i)<='Z'){
                                 char b = w.charAt(i);
                                 if (!vProductive.contains(b)) {
                                     isProductive = false;
                                     break;
                                 }
                             }
                             if (isProductive) {
                                 vProductive.add(a);
                                 changed = true;
                                 break;
                             }
                         }
                     }
                 }
               }
             } while (changed);
             
              haveUseless = vAccessible.size()<nonTerms.size()||
                     vProductive.size()<nonTerms.size();
             if (haveUseless) {
                 
 LinkedList vUseless = new LinkedList();
                    Iterator lxt=nonTerms.iterator();
                    while(lxt.hasNext()){
                     Production l= (Production)lxt.next();
                     if (!vAccessible.contains(l) || !vProductive.contains(l)) {
                         vUseless.add(l);
                     }
                 }
            //     removeUselessSym(nonTerms,vUseless);
            //     removeUselessSym(prod,vUseless);
             }
             }
         } while(haveUseless);
         createMap();
     }
             
         
    //removes Epsilon Productions
    
    public void removeEps() throws IOException{

	
	Production newStart = new Production(getUnused(),start+"");
	start = newStart.getLeftSide();
	prod.add(newStart);
	createMap();
	
       
	Object[] vars = leftVars.toArray();
	Iterator itr;
	Production rule;
	char symbol;
	HashSet tempSet = new HashSet();
	
	
	while(!hasNoEpsilons()) {
            for (Object var : vars) {
                symbol = ((Character) var).charValue();
                if(prod.remove(new Production(symbol, "" ))) {
                    
                    itr = prod.iterator();
                    while(itr.hasNext()) {
                        rule = ((Production)itr.next()).copyReplace(symbol,"");
                        if(rule != null) {
                            if(!rule.isEpsProd() || rule.getLeftSide()== start)
                                tempSet.add(rule);
                        }
                    }
                }
            } 

	    
	    prod.addAll(tempSet);
	    tempSet = new HashSet();
	    //Update map
	    createMap();
	}
    
    }


  
    /*
        removes Unit Productions
    
    */
    
    public void removeUnits() throws Exception{
	if (!hasNoEpsilons())
	    throw new Exception("has no epsilon transutions");
	Production rule;
	Iterator iter1;
	Iterator iter2;

	HashSet removeSet; 
	HashSet addSet; 
	

	while(hasUnitTrans()) {

	    removeSet = new HashSet();
	    addSet = new HashSet();
	    iter1 = prod.iterator();
	    
	    while(iter1.hasNext()) {
		rule = (Production)iter1.next();
		if(rule.isUnit()) {

		    char LHS = rule.getRightSide().charAt(0);
		    if(leftVars.contains(new Character(LHS))) {
			removeSet.add(rule);
			if(rule.isReflexive()) continue; 
			
		    }
		    else continue; 
		    

		    
		    SortedSet subset = prod.subSet(new Production(LHS,""),new Production(LHS,"zzzzzzzzzzzzzzz"));
		    iter2 = subset.iterator();			
		    while(iter2.hasNext()) {
			Production currRule = (Production)iter2.next();
			Production newRule = new Production(rule.getLeftSide(),currRule.getRightSide());
			if(!newRule.isReflexive())
			    addSet.add(newRule);
			
		    } 
		    
		}
	    }

	    prod.removeAll(removeSet);
	    prod.addAll(addSet);

	    //update map 
	    createMap();
	    
	}
	
        
    }
    
    
    /*
        Read Input File
    
    */
   
    public void readFile(String fname) throws FileNotFoundException, IOException{
            FileReader fr=new FileReader(fname);
	    BufferedReader br = new BufferedReader(fr);
	    StringTokenizer st;
	    int count = 0;
	    char current;
	    leftVars = new HashSet();
            
	    while (br.ready()){
		
		String line = br.readLine();
		if( line == null) continue; 
		st = new StringTokenizer(line, " \t");
		if (!st.hasMoreTokens()){ 
		  
		    continue;
		}

		
		String variable = st.nextToken();
		
                current = variable.charAt(0);
		leftVars.add(new Character(current));

		
		if (count == 0)
		    start = current;

		
		while(st.hasMoreTokens()){
		    String prode = st.nextToken();
		    if (prode.equals("\"")) 
			this.prod.add(new Production(current,"")); 
		   
		    else
			this.prod.add(new Production(current,prode));
		}
		count++;
	    }
	   br.close();
	
    }

    /*
    Checks if there is Var
    */
    
    public boolean hasVar(char c){
	return leftVars.contains(new Character(c));
    }
    
    /*
        Writes to File
    */
    
    public void addToFile(String fName)throws FileNotFoundException, IOException{
	FileWriter fw=new FileWriter(fName+"pragna");
        BufferedWriter br=new BufferedWriter(fw);
	Object[] lhsV = leftVars.toArray();
	    Character LHS;
	    Object[] rhsV;
        for (Object lhsV1 : lhsV) {
            LHS = (Character) lhsV1;
            br.write(LHS);
            rhsV = ((ArrayList)prods.get(LHS)).toArray();
            for (Object rhsV1 : rhsV) {
                br.write(" " + rhsV1);
            }
            br.write("\n");
        }
	
	 br.close();
             m.transform(fName);

    }
    

    /*
      Maps LHS with the Right hand side
        For Productions
     */
    public void createMap(){
	Object[] vars  = leftVars.toArray();
	Object[] rules = prod.toArray();
	ArrayList al;  
	Character curr; 

	
	prods = new HashMap();
        for (Object var : vars) {
            curr = (Character) (var);
            al = new ArrayList();
            prods.put(curr, al);
        }
        for (Object rule : rules) {
            Production r = (Production) rule;
            curr = new Character(r.left);
            al = (ArrayList)prods.get(curr);
            al.add(r.right);
        }
    }	

   
    /*
        Checks for epsilon Productions
    */
   
    public boolean hasEps(){
	
	Object[] v = leftVars.toArray();
        for (Object v1 : v) {
            if (((ArrayList) prods.get((Character) v1)).size() > 0 && ((ArrayList) prods.get((Character) v1)).get(0).equals("")) {
                return true;
            }
        }
	return false;
    }

   /*
            checks if there are no epsilon productions
    */
    public boolean hasNoEpsilons(){
	
	Object[] v = leftVars.toArray();
        for (Object v1 : v) {
            if (((ArrayList) prods.get((Character) v1)).size() > 0 && ((ArrayList) prods.get((Character) v1)).get(0).equals("") && !v1.equals(new Character(start))) {
                return false;
            }
        }
	return true;
    }
    
    /*
        Checks if there are any unit transitions
    */

        
    public boolean hasUnitTrans(){
	Object[] v = leftVars.toArray();
        for (Object v1 : v) {
            ArrayList al = (ArrayList) prods.get((Character) v1);
            for(int j=0; j<al.size(); j++){
                String RHS = (String)al.get(j);
                if( RHS.length() == 1 && leftVars.contains(new Character(RHS.charAt(0))) )
                    return true;
            }
        }
	return false;
    }

   
    
    /*
        for Extra Vars
    */
  
    private char getUnused() {
	Character c;
	for(int i=(int)'A';i<=(int)'Z';i++)
	    if(!leftVars.contains(c = new Character((char)i))) {
		leftVars.add(c);
		
		return (char)i;
	  }

	
	return '\0';
    }

    /*
        gets the start State
    */
    
   public char getStart() {
	return start;
    }
   /*
    Sets Non Terminals unused code
   */
    
     private void setNonTerminals() {
         Iterator m=prod.iterator();
         while(!m.hasNext()){
             Production x=(Production) m.next();
             String lhs=x.getLeftSide()+"";
             String rhs=x.getRightSide();
             if(rhs.length()==1){
                 if(!(rhs.charAt(0)>='A' && rhs.charAt(0)<='Z')){
                     Terms.add(x);
                 }
                 else{
                     nonTerms.add(x);
                 }
             }
             else{
                 nonTerms.add(x); 
            }
             
         }
     }

    
    //Declared Variables
    
    
    //Start Start
    char start;  

    HashSet nonTerms;
    
    HashSet Terms;
    
    //Productions values
    TreeSet prod; 
    
    //mapped productions
    HashMap prods; 
    
    //leftHand variables
    HashSet leftVars; 

   /* private void removeUselessSym(Set nonTerms, LinkedList vUseless) {
            for(int i=0;i<vUseless.size();i++){
            Production toRem=(Production) vUseless.get(i);
                if(nonTerms.contains(toRem)){
                    nonTerms.remove(toRem);
                }
            }
    
    }*/

   

    
}// class CFG







