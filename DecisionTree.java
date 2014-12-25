import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DecisionTree implements Classifier {

    private DataSet d;
    private int P = 0; //  positive label
    private int N = 1; //  negative label
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
     *  by the data set and the desired root. 
     */
    public DecisionTree(DataSet d, int attr) {
	this.d = d;
	
	Node node = new Node();
	
	// Set attribute for node
	node.attr = attr;
	
	// Set children for the node
	node.children = new Node[d.attrVals[attr].length];
	
	for (int i = 0; i < node.children.length; i++)
	    {
		Node child = new Node();
		int p = 0;
		int n = 0;
		
		for (int e = 0; e < d.numTrainExs; e++) {
		    if (d.trainEx[e][attr] == i) 
			{
			    if (d.trainLabel[e] == P) 
				p++;
			    else 
				n++;
			}
		}
		child.classification = p > n ? P : N;
		node.children[i] = child;
	    }
	
	root = node;
    }
    
    /* Recursively constructs a Decision Tree */
    private Node DecisionTreeLearning(List<Integer> examples, 
				      List<Integer> attributes) {
	if (examples.size() == 0) return null;
	
	Node node = new Node();
	int n = 0;
	int p = 0;
	for (int i: examples) {
	    if (d.trainLabel[i] == P) p++;
	    else n++;
	}
	//  classification based on plurality
	node.classification = p > n? P: N;
	// continue if not all examples have same classification and attributes != empty
	if (!(p == 0 || n == 0 || attributes.size() == 0)) {
	    //  decide which attribute to split on
	    double maxGain = Double.NEGATIVE_INFINITY;
	    int attr = -1;
	    for (int i: attributes) {
		double temp = infoGain(i, examples);
		//System.out.println("Attribute " + d.attrName[i] + ": " + temp);
		if (temp > maxGain) {
		    maxGain = temp;
		    attr = i;
		}
	    }
	    //System.out.println("Splitting on " + d.attrName[attr]);
	    node.attr = attr;
	    node.children = new Node[d.attrVals[attr].length];
	    
	    //  calculate subtrees
	    attributes.remove((Integer) attr);
	    for (int i = 0; i < node.children.length; i++) {
		//  new subset of examples
		List<Integer> newExamples = new ArrayList<Integer>();
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

	    if (pk + nk != 0)
		remainder += ((double) (pk + nk)/(p + n)) * entropy((double)pk/(pk + nk)); 
	}
	return entropy((double)p/(p+n)) - remainder;
    }
    /** Entropy of a Boolean random variable that is true with probability q */
    private double entropy(double q) {
	if (q == 0 || q == 1.0) return 0;
	double q2 = 1-q;
	double entropy = -(q*Math.log(q)/Math.log(2) + q2*Math.log(q2)/Math.log(2));
	return entropy;
    }

    /** Prediction based on the decision tree generated by constructor */
    public int predict(int[] ex) {
	Node node = root;
	int classification = N;
	while (node != null) {
	    classification = node.classification;
	    if (node.children == null) break;
	    int attrVal = ex[node.attr];
	    node = node.children[attrVal];
	}

	return classification;
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

	DataSet d = new DiscreteDataSet(filestem);

	Classifier c = new DecisionTree(d);
	//Classifier stump1 = new DecisionTree(d, 0);
	//Classifier stump2 = new DecisionTree(d, 1);
	//Classifier stump3 = new DecisionTree(d, 2);

	d.printTestPredictions(c, filestem);
	//d.printTestPredictions(stump1, filestem+"_stump1");
	//d.printTestPredictions(stump2, filestem+"_stump2");
	//d.printTestPredictions(stump3, filestem+"_stump3");
    }

}
