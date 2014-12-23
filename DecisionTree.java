import java.io.*;

public class DecisionTree implements Classifier {

    private DataSet d;
    private int P; //  positive label
    private int N; //  negative label
    private String author = "Helen and Aditya";
    private String description = "A decision tree classifier";
    // The root of the decision tree. See Node class for more details
    private Node root;

    /** Constructs a decision tree */
    public DecisionTree(DataSet d) {
	this.d = d;
	List<Integer> examples = new ArrayList<Integer>();
	List<Integer> attributes = new ArrayList<Integer>();
	for (int i = 0; i < d.numTrainExs; i++) {
	    examples.add(i);
	}
	for (int i = 0; i < d.numAttrs; i++) {
	    attributes.add(i);
	}
	root = DecisionTreeLearning(examples, attributes);
    }

    /** A simple decision tree for use in AdaBoost. The stump is parameterized 
     *  the data set, the weights of each of the examples, and the desired 
     *  depth of the tree (typically very small) 
     */
    public DecisionTree(DataSet d, double[] weights, int depth) {
	this.d = d;
    }
    
    private Node DecisionTreeLearning(List<Integer> examples, List<Integer> attributes) {
	if (examples.size == 0) return null;

	Node node = new Node();
	int n = 0;
	int p = 0;
	for (int i: examples) {
	    if (d.trainLabel[i] == P) p++;
	    else n++;
	}
	
	if (p == 0) {
	    node.label = n;
	}
	else if (n == 0) {
	    node.label = p;
	}
	else if (attributes.size == 0) {
	    node.label = p > n? p: n;
	}
	else {
	    //  decide which attribute to split on
	    int maxGain = -1;
	    for (int i: attributes) {
		int attr = -1;
		int temp = infoGain(i, examples);
		if (temp > maxGain) {
		    maxGain = temp;
		    attr = i;
		}
	    }
	    node.attr = attr;
	    node.children = new Node[d.attrVals[attr].length];
	    //  calculate subtrees
	    for (int i = 0; i < node.children.length; i++) {
		//  remove attr
		attributes.remove(attr);
		//  new subset of examples
		List<Integer> newExamples = new List<Integer>();
		for (int e: examples) {
		    if (d.trainEx[e][attr] == i) newExamples.add(e);
		}
		node.children[i] = DecisionTreeLearning(newExamples, attributes);
	    }
	}
	return node;
    }

    /** Information gain if splitting set examples on attribute attr */
    private double infoGain(int attr, List<Integer> examples) {
	int numVals = d.attrVals[attr].length;
	int p = 0;
	int n = 0;
	for (int e: examples) {
	    if (d.trainLabel[e] == P) p++;
	    else n++; 
	}
	double before = entropy(p/(p+n));
	double remainder = 0;
	for (int i = 0; i < numVals; i++) {
	    int nk = 0;
	    int pk = 0;
	    for (int e: examples) {
		if (d.trainEx[e][attr] == i) {
		    if (d.trainLabel[e] == P) pk++;
		    else nk++;
		}
	    }
	    remainder += ((pk + nk)/(p + n)) * entropy((pk)/(pk + nk)); 
	}
	return before - remainder;
    }
    /** Entropy of a Boolean random variable that is true with probability q */
    private double entropy(double q) {
	double q2 = 1-q;
	return -(q*Math.log(q)/Math.log(2) + q2*Math.log(q2)/Math.log(2));
    }

    /** Prediction based on the decision tree generated by constructor */
    public int predict(int[] ex) {
	Node node = root;
	int label;
	while (node != null) {
	    if (node.children == null) break;
	    int attrVal = ex[node.attr];
	    int label = node.label;
	    node = node.children[attrVal];
	}
	return label;
    }

    /** This method returns a description of the learning algorithm. */
    public String algorithmDescription() {
	return description;
    }

    /** This method returns the author of this program. */
    public String author() {
	return author;
    }

    /** A simple main for testing this algorithm.  This main reads a
     * filestem from the command line, runs the learning algorithm on
     * this dataset, and prints the test predictions to filestem.testout.
     */
    public static void main(String argv[])
	throws FileNotFoundException, IOException {

	if (argv.length < 1) {
	    System.err.println("argument: filestem");
	    return;
	}

	String filestem = argv[0];

	DataSet d = new DataSet(filestem);

	Classifier c = new DecisionTree(d);

	d.printTestPredictions(c, filestem);
    }

}
