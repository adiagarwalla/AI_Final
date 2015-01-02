/** 
 * Helen Yu (heleny)
 * Aditya Agarwalla (aa4)
 */

import java.io.*;
import java.util.*;

public class HeightExperiment {

    // Main method for systematic experiment for AdaBoost decision tree height
    // Requires four arguments to run
    // E.g.java HeightExperiment data/dna_500 data/census_1000 data/ocr17_1000 
    // data/ocr49_1000
    public static void main(String argv[]) throws FileNotFoundException, 
                                                  IOException
    {
        if (argv.length < 4) 
        {
            System.out.println("HeightExperiment file1 file2 file3 file4"); 
            return;
        }

        String[] filestem = new String[4];

        for (int i = 0; i < 4; i++)
            filestem[i] = argv[i];

        DataSet[] d = new DataSet[4];
        for (int i = 0; i < 4; i++)
            d[i] = new DiscreteDataSet(filestem[i]);

        for (int i = 0; i < 4; i++)
        {
	    int rounds = 150;
            System.out.println("DataSet is: " + filestem[i]);

            for (int height = 1; height <= 6; height++)
            {
                // Sum of errors across all trials
                double sumError = 0.0;

                // Number of trials
                int trial;

                for (trial = 0; trial < 10; trial++)
                {
                    Classifier c = new AdaBoost(d[i], rounds, height);

                    d[i].printTestPredictions(c, filestem[i]);
        	
                    FileReader frTO = new FileReader(filestem[i]+".testout");
                    FileReader frAns = new FileReader(filestem[i]+".answers");

                    Scanner scannerTO = new Scanner(frTO);
                    Scanner scannerAns = new Scanner(frAns);

                    int skip = 0;

                    while (skip < 4) {
                        String line = scannerTO.nextLine();
                        skip++;
                    }

                    int error = 0;
                    int numLines = 0;

                    while (scannerTO.hasNextLine())
                    {
                        String TO_Ans = scannerTO.nextLine();
                        String Ans = scannerAns.nextLine();

                        numLines++;

                        if (!TO_Ans.equals(Ans))
                            error++;
                    }

                    sumError += (double)error/numLines;
                }

                System.out.println("Error rate average over " + trial + 
                   " trials " + "for " + height + " height tree: " + sumError/trial);
            }
        }
    }
}