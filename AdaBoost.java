import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileReader;

public class AdaBoost implements Classifier {
	
    // To be returned by the public methods for author and description
    private String author = "Helen and Aditya";
    private String description = "An adaBoost classifier using decision tree (max height 3) as the weak learner";
    
    // An array of example weights
    private double[] weights;

    // An array of all the hypotheses in the form of decision trees with height
    // limited
    private DecisionTree[] hypotheses;

    // An array of alphas
    private double[] alpha;

    // The two classifications
    private final int P = 0;
    private final int N = 1;

    // Number of rounds -- set through command line for systematic experiments
    private int rounds;

    // Constructor
    // PROVIDE GOOD DESCRIPTION HERE
    public AdaBoost(DataSet d, int rounds){
	
        this.rounds = rounds;
	
    	// Assign number of weights from training examples
    	weights = new double[d.numTrainExs];

        // Initialize the weights to 1 / number of training examples
        for (int i = 0; i < d.numTrainExs; i++)
            weights[i] = 1.0 / d.numTrainExs;

    	// Initialize array for hypotheses
    	hypotheses = new DecisionTree[rounds];
	
    	// Initialize array for alpha
    	alpha = new double[rounds];

    	// Iterate the entire process for the number of rounds
    	for (int counter = 0; counter < rounds; counter++)
        {

	        // Randomly sample numTrainExs times based on weights of example
            List<Integer> examples = new ArrayList<Integer>();
	    
            for (int j = 0; j < d.numTrainExs; j++) 
            {
                for (int i = 0; i < d.numTrainExs; i++)
		        {
                    if (Math.random() <= weights[i])
                        examples.add(i);
		        }
	        }
	    
            // Set hypothesis for current iteration
            hypotheses[counter] = new DecisionTree(d, examples, 3);

            double error = 0.0;

            for (int i = 0; i < d.numTrainExs; i++)
            {
                if (hypotheses[counter].predict(d.trainEx[i]) != d.trainLabel[i])
                    error += weights[i];
            }
	    
    	    double sumWeights = 0.0;
                
            for (int i = 0; i < d.numTrainExs; i++)
            {
    		    if (hypotheses[counter].predict(d.trainEx[i]) == d.trainLabel[i])
                    weights[i] *= error/(1 - error);
    		
                sumWeights += weights[i];
	        }

            // Normalize
            for (int j = 0; j < d.numTrainExs; j++)
                weights[j] /= sumWeights;

            // Set alpha
            alpha[counter] = Math.log((1 - error) / error);
    	}
    }
    
    /** Prediction based on the decision tree generated by constructor */
    public int predict(int[] ex) {
    	double p = 0;
    	double n = 0;

    	for (int i = 0; i < rounds; i++) 
        {
    	    if (hypotheses[i].predict(ex) == P) 
                p += alpha[i];
    	    else 
                n += alpha[i];
    	}

    	return p > n ? P : N;
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
    throws FileNotFoundException, IOException 
    {
        if (argv.length < 1) 
        {
            System.err.println("argument: filestem");
            return;
        }

        String filestem = argv[0];

        DataSet d = new DiscreteDataSet(filestem);

        Classifier c = new AdaBoost(d, 500);

        d.printTestPredictions(c, filestem);
	
         // FileReader frTO = new FileReader(filestem+".testout");
         // FileReader frAns = new FileReader(filestem+".answers");

         // Scanner scannerTO = new Scanner(frTO);
         // Scanner scannerAns = new Scanner(frAns);

         // int skip = 0;

         // while (skip < 4) {
        
         //     String line = scannerTO.nextLine();
         //     skip++;
         // }

         // int error = 0;
         // int numLines = 0;

         // while (scannerTO.hasNextLine())
         // {
         //     String TO_Ans = scannerTO.nextLine();
         //     String Ans = scannerAns.nextLine();

         //     numLines++;

         //     if (!TO_Ans.equals(Ans))
         //         error++;
         // }

         // System.out.println("Error rate: " + (double)error/numLines);
	 
    }
}