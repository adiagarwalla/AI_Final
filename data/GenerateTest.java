import java.util.Scanner;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;

public class GenerateTest {

    public static void main (String[] args) throws IOException{ 
	if (args.length < 2) {
	    System.out.println("GenerateTest filestem trainingSetSize"); 
	    return;
	}
	String filestem = args[0];
	String filestem2 = filestem+"_2";
	int trainSize = Integer.parseInt(args[1]);
	FileReader fr = new FileReader(filestem+".train");
	Scanner scanner = new Scanner(fr);
	
	PrintWriter test = new PrintWriter(filestem2+".test");
	PrintWriter train = new PrintWriter(filestem2+".train");
	int count = 0;
	while (scanner.hasNextLine()) {
	    String line = scanner.nextLine();
	    if (count < trainSize) 
		train.println(line);
	    else 
		test.println(line.substring(0, line.length() - 2));
	    count++;
	}
	train.close();
	test.close();

    }





}