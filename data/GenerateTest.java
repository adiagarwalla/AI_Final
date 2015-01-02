/** 
 * Helen Yu (heleny)
 * Aditya Agarwalla (aa4)
 */

import java.util.Scanner;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;

public class GenerateTest {

	// Splits training dataset into two parts - one for training and one for
	// testing. Generates .train file for RoundsExperiment to take in as
	// training set, .test file to take in as testing set, .answers to compare
	// RoundsExperiment's testout to for accuracy and .names for use by
	// RoundsExperiment
    public static void main (String[] args) throws IOException{ 
	if (args.length < 2) {
	    System.out.println("GenerateTest filestem trainingSetSize"); 
	    return;
	}
	String filestem = args[0];
	int trainSize = Integer.parseInt(args[1]);
	String filestem2 = filestem+"_"+trainSize;
	FileReader fr = new FileReader(filestem+".train");
	Scanner scanner = new Scanner(fr);
	
	PrintWriter test = new PrintWriter(filestem2+".test");
	PrintWriter answers = new PrintWriter(filestem2+".answers");
	PrintWriter train = new PrintWriter(filestem2+".train");

	FileReader fr1 = new FileReader(filestem+".names");
	PrintWriter names = new PrintWriter(filestem2+".names");
	Scanner scannerNames = new Scanner(fr1);

	while (scannerNames.hasNextLine()) {
		String line = scannerNames.nextLine();
		names.println(line);
	}
	names.close();

	int count = 0;
	while (scanner.hasNextLine()) {
	    String line = scanner.nextLine();
	    if (count < trainSize) 
		train.println(line);
	    else {
		String[] temp = line.split(" ");
		for (int i = 0; i < temp.length - 1; i++) {
		    test.print(temp[i] + " ");
		}
		test.println();
		answers.println(temp[temp.length-1]);
	    }
	    count++;
	}
	train.close();
	test.close();
	answers.close();
	
    }
}