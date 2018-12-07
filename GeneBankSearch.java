import java.io.FileNotFoundException;
import java.io.File;
import java.util.IllegalFormatException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;

/**
 * parses through a B-Tree for specific query files. If found program
 * will print the DNA sequence. Will inform you if file cannot be found in directory.
 * 
 * @author Spencer, Justin, Binod, Akhilesh
 */

public class GeneBankSearch {
	
	private int degree, debug;
	
	private String BTreeName, QueryName;
	private PrintWriter printWriter;
	
	private File beTreeFile, queryFileObject;
	private GeneticFileConstructor dataReader;
    
	public GeneBankSearch(String[] args) {
		parserOfArguments(args);
		findAndStore();
	}

	public static void main(String[] args) {
		new GeneBankSearch(args);
	}
	
	
	/**
	 * search through b-tree for a specific key value
	 * 
	 * @param beginNode - node in which search will begin from
	 * @param keyNodeSearch - key value we are searching for
	 * @return long value if value is found
	 * @throws IOException
	 */
	private String keyValueNodeSearch(BTreeNode beginNode, long keyNodeSearch) throws IOException {
		int x = 0;

		while (x < (beginNode.getSize()) && keyNodeSearch > beginNode.keyArray[x]) {
			
			x++;
		}
		if (x < beginNode.getSize() && keyNodeSearch == beginNode.keyArray[x]) {
			if (debug == 1) {

				printWriter.println(sequenceDecoder(beginNode.keyArray[x]) + ": " + beginNode.frequency[x]);
			}
			return sequenceDecoder(beginNode.keyArray[x]) + ": " + beginNode.frequency[x];
			
		} else if (beginNode.getLeaf())
			
			return null;
		else {
			return keyValueNodeSearch(dataReader.readData(beginNode.children[x]), keyNodeSearch);
		}
	}
	
	/**
	 * parser of data and will print value if found. 
	 * Will inform user if data is not supplied correctly
	 */
	private void findAndStore() {
		try {
			long keyValue = 0;
			String DNA;

			Scanner scanQuery = new Scanner(queryFileObject);
			dataReader = new GeneticFileConstructor(beTreeFile);
			
			if (debug == 1) {
				outputFileConstructor();
			}
			long rootLocation = dataReader.rootFinder();
			int sequenceLength = dataReader.getLengthOfSequence();
			
			this.degree = dataReader.getDegree();
			dataReader.setDegree(degree);
			
			while (scanQuery.hasNextLine()) {
				String geneticSequence = scanQuery.nextLine();

				if (sequenceLength != geneticSequence.length()) {
					throw new IOException();
				}

				keyValue = convert(geneticSequence);
				DNA = keyValueNodeSearch(dataReader.readData(rootLocation), keyValue);
				if (DNA != null)

					System.out.println(DNA);
			}
			if (debug == 1) {
				System.out.println("The Results File (" + BTreeName + " " + QueryName + " ) has been created.");
			}
			scanQuery.close();
		} catch (FileNotFoundException e) {
			printUsage("File not found please insert in the correct directory");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Converts current string of DNA to Long
	 * 
	 * @return converted string of DNA notation to Long
	 */
private long convert(String subString) {
	int i = 0;
	long returnConversion = 1;
	String[] split = subString.split("");
	
	while (i < subString.length()) {
		returnConversion = returnConversion << 2;
		
		if (split[i].equals("A") || split[i].equals("a"))
			returnConversion = returnConversion | 0;
		else if (split[i].equals("T") || split[i].equals("t"))
			returnConversion = returnConversion | 3;
		else if (split[i].equals("C") || split[i].equals("c"))
			returnConversion = returnConversion | 1;
		else if (split[i].equals("G") || split[i].equals("g"))
			returnConversion = returnConversion | 2;
		else 
			return -1;
		i++;
	}
	return returnConversion;
}

private void outputFileConstructor() throws IOException {
	
	File outputFile = new File(BTreeName + " " + QueryName + " Results");
	printWriter = new PrintWriter(new FileWriter(outputFile), true);
}

/**
 * check the user inputs for the program. Will inform the user if 
 * the arguments are not correct
 *
 * @param args program arguments
 */
private void parserOfArguments(String[] args) {

	if (args.length < 3 || args.length > 5)
		printUsage("Program requires between 3 and 5 valid arguements to run");

	try {
		if (Integer.parseInt(args[0]) == 1)
			printUsage("1st argument must be either 0 or 1 ");
		
	} catch (IllegalFormatException e) {
		printUsage("1st argument must be either 0 or 1 ");
	}

	BTreeName = args[1];
	
	beTreeFile = new File(BTreeName);

	QueryName = args[2];
	
	queryFileObject = new File(QueryName);
}


	/**
	 * Converts a binary long value back into its orignal DNA sequence
	 * 
	 * @param binaryDNAsequence the long to be decoded back into a String
	 * @return the decodedStr that was decoded from the long
	 */
	public String sequenceDecoder(long binaryDNAsequence) {
		String stringConversion = "";
		if (binaryDNAsequence == -1) {
			
			stringConversion = stringConversion.concat("n");
			return stringConversion;
		}
		long letter = 0;

		while (binaryDNAsequence != 1) {
			letter = binaryDNAsequence & 3;
			binaryDNAsequence = binaryDNAsequence >> 2;

			if (letter == 0)
				stringConversion = "a" + stringConversion;
			else if (letter == 3)
				stringConversion = "t" + stringConversion;
			else if (letter == 1)
				stringConversion = "c" + stringConversion;
			else if (letter == 2)
				stringConversion = "g" + stringConversion;
			else
				stringConversion = "n" + stringConversion;
		}
		return stringConversion;
	}

	/**
	 * Informs the user at launch what the program requires to run.
	 * Will also inform the user if the arguments passed are not correct
	 * 
	 * @param error reason for crash
	 */
	private void printUsage(String error) {
		
		System.err.println(error);
		System.out.println();

		System.out.println(
				"<0/1 (no/with Cache)>: 	A 0 declares that there will be no cache, while a 1 declares a cache");
		System.out.println("			 If a cache is declared, the 4th argument must be a positive integer");
		System.out.println("<btree file>: 		The binary file where the BTree is stored");
		System.out.println("<query file>:		The query file to compare the btree file to");
		System.out.println(
				"			 The argument must be a positive integer (an integer too big may crash the program");
		System.out.println(
				"[<debug level>]: 	(optional) either 0 or 1, will print out different output with different arguments");
		System.exit(1);
	}
}
