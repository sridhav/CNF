/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grammar4;
/**
 *
 * @author Sridhar
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Sridhar
 */

public class FileParsing {
    HashMap fileMap=new HashMap();
    ArrayList<String> fileLines=new ArrayList();
    ArrayList<String> keys=new ArrayList<>();
    ArrayList<String> epsLines=new ArrayList();
  
    FileParsing(String fileName) throws IOException{
        parseFile(fileName);      
        writeToFile();
    }
    
    private ArrayList<String> readLines(String fname) throws FileNotFoundException, IOException{
        ArrayList<String> temp=new ArrayList<>();
        FileReader fr=new FileReader(fname);
        BufferedReader br=new BufferedReader(fr);
         String readLin;
        while((readLin=br.readLine())!=null){
            temp.add(readLin);
        
        }
        br.close();
                
        return temp;
    }
    
    /*
        parses the file
    */
    
    private void parseFile(String fileName) throws IOException {
        FileReader fr=new FileReader(fileName);
        BufferedReader br=new BufferedReader(fr);
        
       
        StringTokenizer st;
        String leftVal;
        String rightVal;
        String convLeftVal;
        String convRightVal;
        
        
        String readLin;
        
        
        
        while((readLin=br.readLine())!=null){
            readLin=ignoreSpaces(readLin);
            st=new StringTokenizer(readLin,"::=");
            if(st.hasMoreTokens()){
                leftVal=st.nextToken();
                if(!fileMap.containsKey(leftVal)){
                    convLeftVal=getRandomChar()+"";
                    if(checkCFG(leftVal)){
                    keys.add(leftVal);
                    fileMap.put(leftVal,convLeftVal); 
                 //   System.out.println(convLeftVal);
                    }
                 }
            }
            
            if(st.hasMoreTokens()){
                rightVal=st.nextToken();
                for(int i=0;i<rightVal.length();i++){
                    if(rightVal.charAt(i)=='<'){
                        if(rightVal.indexOf(">")>=0){
                            String temp=rightVal.substring(i, rightVal.indexOf(">")+1);
                            rightVal=rightVal.substring(rightVal.indexOf(">")+1,rightVal.length());
                            i=i+temp.length()+1;
                            //System.out.println(i);
                        if(!fileMap.containsKey(temp)){
                            convRightVal=getRandomChar()+"";
                            keys.add(temp);
                            fileMap.put(temp,convRightVal); 
                           // System.out.println(convRightVal);
                        }
                       }   
                    }
                }
                
            }
            else{
                rightVal="\"";
            }
            fileLines.add(readLin);
        }
       br.close();
        
    }

    /*
        random char generator
    */
    private char getRandomChar() {
      
	for(int i=(int)'A';i<=(int)'Z';i++){
            String temp=(char)i+"";
	    if(!fileMap.containsValue(temp)) {
		return (char)i;
	  }

        }
	return '\0';
        
    }

    /*
        writes to the file 
    */
    
    private void writeToFile() throws IOException{
       
        FileWriter fw=new FileWriter("transFile.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        String temp;
        for(int i=0;i<keys.size();i++){
            for(int j=0;j<fileLines.size();j++){
             temp=convert(fileMap.get(keys.get(i)),keys.get(i),fileLines.get(j));
             fileLines.set(j,temp);
              
            }
            
        }
        
        for(int i=0;i<fileLines.size();i++){
             StringTokenizer st=new StringTokenizer(fileLines.get(i),"::=");
             String temp2=fileLines.get(i);
             temp2=temp2.replaceAll("::="," ");
             bw.write("\n"+temp2);
             if(st.countTokens()==1){
                    bw.write(""+"\"");
             }
            
        }
        bw.close();
        
    }

    /*
        replaces the values
    */
    
    private String convert(Object fmap, Object key,String fileLine) {
           String resValMap=(String)fmap;
           String ke=(String)key;
           String replace=fileLine.replaceAll(ke,resValMap);
           //System.out.println(resValMap);
          // System.out.println(ke);
           
           
           return replace;
           
    }
    
    /*
        transform the file to BNF
    */
    public void transform(String fname) throws IOException{
        
        epsLines=readLines(fname+"pragna");
        ArrayList<String> epsNewLines=getNewEpsLines(epsLines);
      // System.out.print(epsNewLines.size());
        FileWriter fw=new FileWriter(fname);
        BufferedWriter bw=new BufferedWriter(fw);
        
       // System.out.println(fileMap);
        
        for(int i=0;i<keys.size();i++){
            for(int j=0;j<epsNewLines.size();j++){
                epsNewLines.set(j, convert(keys.get(i),fileMap.get(keys.get(i)),epsNewLines.get(j)));
            }
        }
       // System.out.println(epsNewLines);
        
        for(int i=0;i<epsNewLines.size();i++){
            String temps=epsNewLines.get(i);
            for(int j=0;j<temps.length();j++){
                char m=temps.charAt(j);
                if(m>='A'&&m<='Z'){
                    String unVar=getUnusedVar();
                    String unVal=m+"";
                    for(int k=0;k<epsNewLines.size();k++){
                        epsNewLines.set(k, convert(unVar,unVal,epsNewLines.get(k)));
                    }
                    fileMap.put(unVar, unVal);
                    keys.add(unVar);
                }
            }
        }
        //System.out.println(epsNewLines);
        for(int i=0;i<epsNewLines.size();i++){
               bw.write("\n"+epsNewLines.get(i));
                
        }
            
        bw.close();
        display(fname);
    }
    
    
    /*
        temporary files deleted
        Garbage collector
    */
    
    void DgarbageCollection() {
        File f1=new File("removeUnits.txtpragna");
        f1.delete();
        File f2=new File("removeEps.txtpragna");
        f2.delete();
         File f3=new File("transFile.txtpragna");
        f3.delete();
    
    }

    /*
        retrives all Production as List
    */
    private ArrayList<String> getNewEpsLines(ArrayList<String> epsLines) {
        ArrayList<String> temp=new ArrayList<>();
        StringTokenizer st;
        char source;
        for(int i=0;i<epsLines.size();i++){
            String temp2=epsLines.get(i);
            String result;
            st=new StringTokenizer(temp2," ");
            if(st.hasMoreTokens()){
                source=st.nextToken().charAt(0);
                while(st.hasMoreTokens()){
                    String temp3=st.nextToken();
                    if(temp3.equals("\"")){
                        result=source+"::=";
                    }
                    else{
                        result=source+"::="+temp3;
                    }
                    temp.add(result);
                  //  System.out.println(temp);
                }
            }
        }
       return temp;
    }
    
    /*
        Unused BNF vars
    */
    private String getUnusedVar(){
        String temp="";
        for(int i=0;i<1000;i++){
            String x="<expr"+i+">";
           // System.out.println("<expr"+i+">");
         //   System.out.println(keys.contains(x));
            if(!keys.contains(x)){
                temp=x;
                break;
            }
        }
        return temp;
    }

    /*
        Checks whether the Grammar is CFG or not if not stops executing
    */
    private boolean checkCFG(String leftVal) {
        
         if(leftVal.charAt(0)=='<' && leftVal.charAt(leftVal.length()-1)=='>'){
            
             String remain=leftVal.substring(leftVal.indexOf(">")+1,leftVal.length());
            // System.out.println(remain);
             if(remain.length()>1){
                 System.out.println("Not a valid CFG");
                 System.exit(1);
                 return false;
             }
             else{
                 return true;
            }
         }
         System.out.println("Not a valid CFG");
         System.exit(1);        
         return false;
   }

    /*
        Ignores invalid Spaces
    */
    private String ignoreSpaces(String readLin) {
        String res="";
        for(int i=0;i<readLin.length();i++){
            if(readLin.charAt(i)!=' ' && readLin.charAt(i)!='\t'){
                res=res+readLin.charAt(i);
            }
        }
        //System.out.println(res);
        return res;
    }
 
    /*
        Displays on Screen 
    */
    public void display(String fname) throws IOException{
       System.out.println();
        ArrayList<String> temp;
        if(fname.equals("removeUseless.txt")){
            System.out.println("Useless Productions removed");
        }
        else if(fname.equals("removeEps.txt")){
            System.out.println("Epsilon Productions removed");
        }
        else if(fname.equals("removeUnits.txt")){
             System.out.println("Unit Productions removed");
      
        }
        temp=readLines(fname);
        for(int i=0;i<temp.size();i++){
            if(!temp.get(i).equals(""))
            System.out.println(temp.get(i));
        }
        
    }
    
}
