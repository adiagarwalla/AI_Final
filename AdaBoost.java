public class AdaBoost implements Classifier {
	
	// To be returned by the public methods for author and description
	private String author = "Helen and Aditya";
    private String description = "An adaBoost classifier using decision stump
    							  as the weak learner";

    // An array of example weights
    private double[] weights;

    // An array of all the hypotheses
    private int[] hypotheses;

    // An array of alphas
    private double[] alpha;

    // Number of rounds - NO CLUE HOW TO DETERMINE IT AS OF NOW
    private rounds;


    // Constructor
    // PROVIDE GOOD DESCRIPTION HERE
    public AdaBoost(DataSet d)
    {
    	// Assign number of weights from training examples
    	weights = new double[d.numTrainExs];

    	// Assign value to number of rounds
    	rounds = 100;

    	// Initialize array for hypotheses
    	hypotheses = new int[rounds];

    	// Initialize array for alphas
    	alpha = new double[rounds];

    	// Iterate the entire process for the number of rounds
    	for (int counter = 0; counter < rounds; counter++)
    	{
    		// TO DO
    	}


    }
}