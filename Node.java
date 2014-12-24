public class Node {

    //  the attribute that the tree is splitting on. if -1, no attribute
    public int attr;
    //  children[i] = subtree where attribute value  = i
    public Node[] children;
    public int classification;

    /** 
     * Constructor 
     */
    public Node(){
	// attr is default set to -1 to indicate not set yet
	this.attr = -1;
    }

}