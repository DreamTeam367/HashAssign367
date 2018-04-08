import java.io.*;

import java.util.*;
/**
 * This class implements a hashtable that using chaining for collision handling.
 * Any non-<tt>null</tt> item may be added to a hashtable.  Chains are 
 * implemented using <tt>LinkedList</tt>s.  When a hashtable is created, its 
 * initial size, maximum load factor, and (optionally) maximum chain length are 
 * specified.  The hashtable can hold arbitrarily many items and resizes itself 
 * whenever it reaches its maximum load factor or whenever it reaches its 
 * maximum chain length (if a maximum chain length has been specified).
 * 
 * Note that the hashtable allows duplicate entries.
 */


public class HashTable<T> {
    
    /**
     * Constructs an empty hashtable with the given initial size, maximum load
     * factor, and no maximum chain length.  The load factor should be a real 
     * number greater than 0.0 (not a percentage).  For example, to create a 
     * hash table with an initial size of 10 and a load factor of 0.85, one 
     * would use:
     * 
     * <dir><tt>HashTable ht = new HashTable(10, 0.85);</tt></dir>
     *
     * @param initSize the initial size of the hashtable.
     * @param loadFactor the load factor expressed as a real number.
     * @throws IllegalArgumentException if <tt>initSize</tt> is less than or 
     *         equal to 0 or if <tt>loadFactor</tt> is less than or equal to 0.0
     **/
	
	double loadFactor = 0.0;
	int numItems = 0; 
	LinkedList<T>[] hash;
	int maxLength=0; 
	boolean chainMatters;
	
    public HashTable(int initSize, double loadFactor) {
        if (initSize <= 0 || loadFactor<=0.0){
        	throw new IllegalArgumentException(); 
        }
    	
    	this.hash = (LinkedList<T>[])( new LinkedList[initSize]); 
    	for(int i = 0; i<hash.length;i++){
    		hash[i]=new LinkedList<T>();
    	}
    	this.loadFactor = loadFactor; 
    	chainMatters=false;
    	
    }
    
    
    /**
     * Constructs an empty hashtable with the given initial size, maximum load
     * factor, and maximum chain length.  The load factor should be a real 
     * number greater than 0.0 (and not a percentage).  For example, to create 
     * a hash table with an initial size of 10, a load factor of 0.85, and a 
     * maximum chain length of 20, one would use:
     * 
     * <dir><tt>HashTable ht = new HashTable(10, 0.85, 20);</tt></dir>
     *
     * @param initSize the initial size of the hashtable.
     * @param loadFactor the load factor expressed as a real number.
     * @param maxChainLength the maximum chain length.
     * @throws IllegalArgumentException if <tt>initSize</tt> is less than or 
     *         equal to 0 or if <tt>loadFactor</tt> is less than or equal to 0.0 
     *         or if <tt>maxChainLength</tt> is less than or equal to 0.
     **/
    public HashTable(int initSize, double loadFactor, int maxChainLength) {
    	this.hash = (LinkedList<T>[])( new LinkedList[initSize]); 
    	for(int i = 0; i<hash.length;i++){
    		hash[i]=new LinkedList<T>();
    	}
     
     	
    	this.loadFactor = loadFactor; 
    	this.maxLength = maxChainLength; 
    	chainMatters=true;
    }
    
    
    /**
     * Determines if the given item is in the hashtable and returns it if 
     * present.  If more than one copy of the item is in the hashtable, the 
     * first copy encountered is returned.
     *
     * @param item the item to search for in the hashtable.
     * @return the item if it is found and <tt>null</tt> if not found.
     **/
    public T lookup(T item) {
    	int index = item.hashCode(); 
         
        index = index % this.hash.length; 
     	
     	if (index<0){
     		index = index +this.hash.length; 
     	}
     	
     	for(int i =0; i<hash[index].size(); i++){
    	 if (hash[index].get(i).equals(item)) {
    	 return item; 
    	}
     
    	 
      
     }	
    	
    	return null; 
    	
    	
    }
    
    
    /**
     * Inserts the given item into the hashtable.  The item cannot be 
     * <tt>null</tt>.  If there is a collision, the item is added to the end of
     * the chain.
     * <p>
     * If the load factor of the hashtable after the insert would exceed 
     * (not equal) the maximum load factor (given in the constructor), then the 
     * hashtable is resized.  
     * 
     * If the maximum chain length of the hashtable after insert would exceed
     * (not equal) the maximum chain length (given in the constructor), then the
     * hashtable is resized.
     * 
     * When resizing, to make sure the size of the table is reasonable, the new 
     * size is always 2 x <i>old size</i> + 1.  For example, size 101 would 
     * become 203.  (This guarantees that it will be an odd size.)
     * </p>
     * <p>Note that duplicates <b>are</b> allowed.</p>
     *
     * @param item the item to add to the hashtable.
     * @throws NullPointerException if <tt>item</tt> is <tt>null</tt>.
     **/
    public void insert(T item) {
        if(item == null){
        	throw new NullPointerException(); 
        }
        
        if(((numItems + 1)/ this.hash.length)> this.loadFactor){
        	this.resize(this.hash.length*2 + 1);
        }
        
        int temp = item.hashCode(); 
        temp = temp % this.hash.length; 
    	if (temp<0){
    		temp = temp+this.hash.length; 
    	}
    	
    	if (this.hash[temp].size()+1>this.maxLength && chainMatters){
    		this.resize(this.hash.length*2 + 1);
    	}
    	
    	this.hash[temp].add(item); 
    	numItems++;
    	
    }
    
    private void resize(int size){
    LinkedList<T>[] tempArray = this.hash; 
    
    this.hash = (LinkedList<T>[])( new LinkedList[size]); 
    for(int i = 0; i<hash.length;i++){
		hash[i]=new LinkedList<T>();
	}
 	numItems=0;
    for(int i =0; i<tempArray.length;i++){
    	while(tempArray[i].size()>0 ){
    		this.insert(tempArray[i].removeFirst()); 
    	}
    	
    	
    }
    	
    }
    
    /**
     * Removes and returns the given item from the hashtable.  If the item is 
     * not in the hashtable, <tt>null</tt> is returned.  If more than one copy 
     * of the item is in the hashtable, only the first copy encountered is 
     * removed and returned.
     *
     * @param item the item to delete in the hashtable.
     * @return the removed item if it was found and <tt>null</tt> if not found.
     **/
    public T delete(T item) {
    	 int index = item.hashCode(); 
         
         index = index % this.hash.length; 
     	
     	if (index<0){
     		index = index +this.hash.length; 
     	}
     	
        for(int i =0; i<hash[index].size(); i++){
	       	if (hash[index].get(i).equals(item)) { 
	       		 return hash[index].remove(i); 
	       	} 
        }	
       	
       	return null; 
       }
    
    
    /**
     * Prints all the items in the hashtable to the <tt>PrintStream</tt> 
     * supplied.  The items are printed in the order determined by the index of
     * the hashtable where they are stored (starting at 0 and going to 
     * (table size - 1)).  The values at each index are printed according 
     * to the order in the <tt>LinkedList</tt> starting from the beginning. 
     *
     * @param out the place to print all the output.
     **/
    public void dump(PrintStream out) {
    	out.println("Hashtable contents:");
    	
    	for(int i =0; i<hash.length;i++){
    		if(hash[i].size() != 0){
    			out.print(i+": [");
	    		Iterator<T> indexItr = hash[i].iterator();
	    		if(indexItr.hasNext()){
	    			out.print(indexItr.next());
	    		}
	    		while(indexItr.hasNext()){
	    			
	    			out.print("," + indexItr.next());
	    		}
	    		out.println("]");
    		}
    	}

    }
    
  
    /**
     * Prints statistics about the hashtable to the <tt>PrintStream</tt> 
     * supplied.  The statistics displayed are: 
     * <ul>
     * <li>the current table size
     * <li>the number of items currently in the table 
     * <li>the current load factor
     * <li>the length of the largest chain
     * <li>the number of chains of length 0
     * <li>the average length of the chains of length > 0
     * </ul>
     *
     * @param out the place to print all the output.
     **/
    public void displayStats(PrintStream out) {
    	out.println("Hashtable statistics:");
        out.println("  current table size:       " + hash.length);
        out.println("  # items in table:         " + numItems);
        double loadFactor = (double)numItems/hash.length;
        out.println("  current load factor:      " + loadFactor);
        
        int maxChainLength=0;
        int sumChain=0;
        int numNonZero=0;
        int numZero=0;
        
        for(int i=0;i<hash.length;i++){
        	if(hash[i].size()==0){
        		numZero++;
        	} else{
        		sumChain = sumChain + hash[i].size();
        		numNonZero++;
        		if(maxChainLength<hash[i].size()){
        			maxChainLength=hash[i].size();
        		}
        		
        	}
        }
        
        out.println("  longest chain length:     " + maxChainLength);
        out.println("  # 0-length chains:        " + numZero);
        
        double avg = ((double)sumChain)/numNonZero;
        out.println("  avg (non-0) chain length: "+ avg);
    }
}
