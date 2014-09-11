package grammar4;

/**
 *
 * @author Sridhar
 */

/*
    Production Class
    Production methods
*/
class Production implements Comparable{
    char left;  
    String right;
    
    public Production(char left, String right){
	this.left = left;
	this.right = right;
    }
    
        public boolean isUnit(){
	return right.length()==1;
    }

    public boolean isEpsProd(){
	return right.length()==0;
    }

    /*
        creates an extra copy
    */
    public Production copyReplace(char oldSymbol, String newSymbol) {
	int occurrences = 0;

	int length = right.length();
	StringBuffer buff = new StringBuffer();
	char c;
	for(int i=0;i<length;i++) {
	    c = right.charAt(i);
	    if(c == oldSymbol) {
		buff.append(newSymbol);
		occurrences ++;
	    }
	    else buff.append(c);
	}

	if(occurrences > 0) return new Production(left,buff.toString());
	return null;
    } 
    /*
        RightSide
    */
    public String getRightSide() {
	return right;
    }
   /*
    sets Right
    */
    public void setright(String right) {
	this.right = right;
    }
    
    /*
        LeftSide
    */
    
    public char getLeftSide() {
	return left;
    }

   
    /*
        Derives itself or not Reflexive property
    */
    public boolean isReflexive() {
	return (isUnit() && left == right.charAt(0));
    }
    
    
    /*
        Compare Function
    */
   public int compareTo(Object x){
	Production y = (Production)x;
	if( left < y.left ) return -1;
	if( left > y.left ) return 1;
	return(right.compareTo(y.right));
    }
	

}





