/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grammar4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Sridhar
 */
public class GrammarDecidability {
    
    ArrayList<String> termList=new ArrayList<>();
    ArrayList<String> termListVal=new ArrayList<>();
    ArrayList<String> prod=new ArrayList<>();
    ArrayList<String> rhs=new ArrayList<>();
    ArrayList<String> lhs=new ArrayList<>();
    
    
    
    GrammarDecidability(String transFiletxt) throws FileNotFoundException, IOException {
        readFile(transFiletxt);
    }

    /*
        reads File
    */
    private void readFile(String transFiletxt) throws FileNotFoundException, IOException {
        FileReader fr=new FileReader(transFiletxt);
        BufferedReader br=new BufferedReader(fr);
        String temp;
        while((temp=br.readLine())!=null){
            prod.add(temp);
            StringTokenizer st=new StringTokenizer(temp," ");
            if(st.hasMoreTokens()){
                lhs.add(st.nextToken());
            }
            if(st.hasMoreTokens()){
                String temp2=st.nextToken();
                rhs.add(temp2);
            }
            if(isTerminal(temp)){
                
            }
        }
        br.close();
    }

    /*
        checks whether it is terminal
    */
    private boolean isTerminal(String temp) {
        StringTokenizer st=new StringTokenizer(temp," ");
        String source="";
        String dest;
         boolean val=false;
         int count=0;
        if(st.hasMoreTokens()){
            source=st.nextToken();
        }
        if(st.hasMoreTokens()){
            if(st.countTokens()==1){
                dest=st.nextToken();
               
                for(int i=0;i<dest.length();i++){
                val=false;
                if(!(dest.charAt(i)>='A' && dest.charAt(i)<='Z')){
                        if(dest.charAt(i)=='\"'){
                           count++;                      
                        }
                        else{
                            count++;    
                        }
                    }
                }
                if(count==dest.length()){
                    if(!termList.contains(source) ){
                        termListVal.add(dest);
                        termList.add(source);
                    }
                    
                    val=true;
                }
                else{
                    val=false;
                }
            }
        }
        //System.out.println(termListVal);
     //   System.out.println(termList);
        
        return val;
    
    }

    /*
        return true is grammar generates language which is empty
    else false
    */
    public boolean isGrammarEmpty() {
        ArrayList<String> tempProd=new ArrayList<>(rhs);
       // System.out.println("dasdasd"+tempProd.size());
        for(int i=0;i<termListVal.size();i++){
            String key=termList.get(i);
            String val=termListVal.get(i);
            for(int j=0;j<tempProd.size();j++){
             String tempVar=tempProd.get(j);
               tempProd.set(j,tempVar.replaceAll(key,val));
               String tempVar2=lhs.get(j)+" "+tempProd.get(j);
               //System.out.println(tempVar2);
               isTerminal(tempVar2);
               
                   
               
            }
        }
        
        for(int i=0;i<tempProd.size();i++){
            String temp=tempProd.get(i);
             //System.out.println(tempProd.get(i));
            for(int j=0;j<temp.length();j++){
                if(temp.charAt(j)>='A' && temp.charAt(j)<='Z'){
                   
                    return true;
                }
            }
        }
        return false;
        
    }

    
}
