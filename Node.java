public class Node {

    //  the attribute that the tree is splitting on
    public int attr;
    //  maps from an attribute value to the appropriate subtree
    public Map<Integer, Node> children;
    //  the classification if this node is a leaf.  
    public int label;

    /** 
     * Constructor 
     */
    public Node(){
	
    }

}