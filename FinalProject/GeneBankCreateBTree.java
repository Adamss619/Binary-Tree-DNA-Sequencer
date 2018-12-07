

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Programs take in a metadata file of the human DNA and creates a btree from the sequences that were given
 * as a user argument. Supplies different levels of information based on what the user requested.
 * 
 * 
 * @author justin, spencer, binod, alkinish
 *
 */
public class GeneBankCreateBTree {
	
	private int degree, lengthOfSequence, cacheSize, debug, insertions;
	
	private File gbkWritenFile;
	private Parser parser;
	private String gbkFileName;
	private BTree bTreeObject;

	public static void main(String[] args) throws IOException {

		new GeneBankCreateBTree(args);

	}
	
	private void argumentParser(String[] args) {
		if (args.length < 4 || args.length > 6) {
			printInstructions("Program requires between 4 and 6 valid arguements to run");
		}

		
		try { 
			if (Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 1) {
				printInstructions("1st argument is must be either 0 or 1");
			}
			
			
		} catch (NumberFormatException e) {
			printInstructions("1st argument is must be either 0 or 1");
		}


		if (Integer.parseInt(args[0]) == 1) {
			if (args.length <= 4) {
				printInstructions("Must enter cache size if 1 was entered for 1st arguement");
			}
			if (Integer.parseInt(args[4]) <= 0) {
				printInstructions("Cache size must be a positive integer.");
			} else {
				cacheSize = Integer.parseInt(args[4]);
			}
		} else {
			cacheSize = 0;
		}

		try {
			degree = Integer.parseInt(args[1]);

		} catch (NumberFormatException e) {
			printInstructions("2nd argument declares degree of BTree, and must be an integer larger than 0.");
		}
		if (degree < 0) {
			printInstructions("Degree of BTree must be an integer larger than 0.");
		}
		if (degree == 0) {
			NodeData findOptimalDegree = new NodeData();
			degree = findOptimalDegree.CalculateBestDegree();
		}

		gbkFileName = args[2];
		gbkWritenFile = new File(gbkFileName);

		try { 
			lengthOfSequence = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			printInstructions("4th argument declares sequence length, must be between 1 and 31.");
		}
		if (lengthOfSequence < 1 || lengthOfSequence > 31) {
			printInstructions("Sequence length must be between 1 and 31.");
		}

		debug = 0;
		if (args.length == 6) { 
			try {
				debug = Integer.parseInt(args[5]);
			} catch (NumberFormatException e) {
				printInstructions("6th argument (optional) declares debug level, and must be either 0 or 1.");
			}
		}
		
		else if (args.length == 5 && Integer.parseInt(args[0]) == 0) {
			try {
				debug = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				printInstructions("5th argument (optional) declares debug level, and must be either 0 or 1.");
			}
		}
		if (debug < 0 || debug > 1) {
			printInstructions("Debug level must be either 0 or 1.");
		}
	}


	public GeneBankCreateBTree(String[] args) throws IOException {
		
		argumentParser(args);

		try {
			
			parser = new Parser(gbkWritenFile, lengthOfSequence);
		} catch (FileNotFoundException e) {
			printInstructions("The file " + gbkFileName + " could not be found.");
			
		}
		if (cacheSize > 0) {
			
			bTreeObject = new BTree(gbkFileName, degree, lengthOfSequence, debug, cacheSize);
			
		} else {
			bTreeObject = new BTree(gbkFileName, degree, lengthOfSequence, debug);

		}
		System.out.println("Beginning to process file...");
		while (parser.nextStringExists()) {

			long nextSequence = parser.incrementStartingString();
			if (nextSequence > -1) {
				
				bTreeObject.insert(nextSequence);
				insertions++;
			}
			if (insertions % 500 == 0) {
				System.out.println("..." + insertions + " sequences inserted...");
			}
		}
		bTreeObject.doneInserting();
	}

	private void printInstructions(String msg) {
		System.err.println(msg);
		System.out.println("User input should be as follows");
		System.out.println(
				"java GeneBankCreateBTree <0/1(W/ or with Cache)> <degree of tree> <gbk file location> <length of sequence> [<cache size>] [<debug level>]");
		System.out.println(
				"<0/1(W/ or with Cache)>: A 0 is no cache, while a 1 declares a cache");
		System.out.println(
				"			 a 5th argument must be supplied, and must be a positive integer if a cache is requested");
		System.out.println(
				"<degree>: 		The degree of the Btree to be created.  \n");
				System.out.println("0 will default the size to the most optimal degree, based on a memory block of 4096 bytes.");
				System.out.println("must be an integer larger than 1.");
				
		System.out.println("<gbk file location>:		gene bank file in which information is behing taken from.");
		System.out.println("<length of sequence>:		The length of DNA sequence to be analyzed.");

		System.out.println("[<cache Size>]:		(optional) if a cache is to be created, this argument is required");
		System.out.println(
				"			 The argument must be a positive integer");
		System.out.println(
				"[<debug level>]: either 0 or 1.  0 will create standard information messages. 1 will create a dump file with each and every valid sequences entered, with the frequency that they appeared at.");
		System.exit(1);
	}


}
