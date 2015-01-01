/** 
 * Helen Yu (heleny)
 * Aditya Agarwalla (aa4)
 */

public class Node {

    // The attribute that the tree is splitting on. if -1, no attribute
    public int attr;

    // Children[i] = subtree where attribute value = i
    public Node[] children;

    // Need to add comment here
    public int classification;

    /** 
     * Constructor 
     */
    public Node()
    {
	   // attr is default set to -1 to indicate not set yet
	   this.attr = -1;
    }
}