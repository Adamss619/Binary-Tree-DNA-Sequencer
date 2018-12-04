import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GeneBankSearch {
	
	private static Scanner queryFile;
	private static Scanner bTreeFile;
	public static final int A = 0; //00 
	public static final int T = 3; //11
	public static final int C = 1; //01
	public static final int G = 2; //10
	
	public static void main(String[] args) {
		String queryFilename = null;
		String bTreeFilename = null;
		int querySequenceLength = 0;
		boolean cache = false;
		int cacheSize = 0;
		boolean debugMode = false;
		boolean debug0 = true;
		int btreeSequenceLength =0;
		StringBuilder debugMsg = new StringBuilder();
		
		//Checking the command line arguments
		if (args.length < 3 || args.length > 5) {
			System.out.println("vsadasdasd");
			printUsage();
		}
		
		try{
			if(Integer.parseInt(args[0]) == 0) {
				cache = false;
				debugMsg.append("Not using cache option\n");
			} else if (Integer.parseInt(args[0]) == 1){
				cache = true;
				debugMsg.append("Using cache option\n");
			} else {
				System.out.println("vasdas");
				printUsage();
				
			}
			
			//Getting the btree file from the disk: args[1]
			try{
				
				bTreeFilename = args[1];
				debugMsg.append("Provided B-Tree filename: " + bTreeFilename + "\n");
				
				String[] splits = bTreeFilename.split("\\.");
				btreeSequenceLength = Integer.parseInt(splits[4]); 
				debugMsg.append("Sequence Length from B-Tree filename: " + btreeSequenceLength + "\n");
			
			} catch (Exception e) {
				System.err.println("The B-tree file was not found!\n");
				//System.out.println("vASas");
				printUsage();
			}
			
			//Get the query file to parse: args[2]
			try {
				queryFilename = args[2];
				queryFile = new Scanner(new File(queryFilename));
				debugMsg.append("Provided query filename: " + queryFilename + "\n");
				
				if(queryFilename.length() > 5) {
					querySequenceLength = Integer.parseInt(queryFilename.substring(5));
					if(querySequenceLength != btreeSequenceLength){
						//System.out.println("vasas");
						printUsage();
						
					}
					debugMsg.append("Sequence Length from query filename: " + querySequenceLength + "\n");
				}
			} catch (Exception e){
				//System.out.println("vaa");
				printUsage();
				
			}
			
			if(args.length >= 3 || args.length <= 5) {
				if(args.length == 3 & cache) {
					System.out.println("v");
					printUsage();
					
				}
				
				if (args.length == 4 && cache && Integer.parseInt(args[3]) > 0) {
					cacheSize = Integer.parseInt(args[3]);
					debugMsg.append("The size of cache is: " + cacheSize + "\n");
					debug0 = true;
					debugMsg.append("Debug mode 0 is on.\n");
				} else if (args.length == 4) {
					if (Integer.parseInt(args[3]) == 0) {
						debug0 = true;
						debugMode = true;
						debugMsg.append("Debug mode 0 is on.\n");
					} else {
						System.out.println("q");
						printUsage();
						
					}
				}
			
			//Debug level: args[4]
			if (args.length == 5 && !debugMode && cache) {
				if (Integer.parseInt(args[4]) == 0) {
					debug0 = true;
					debugMsg.append("Debug mode 0 is on.\n");
				} else {
					debugMode = true;
					debugMsg.append("Debug Mode 0\n");
				}
			}
			} else {
				System.out.println("a");
				printUsage();
				
			}
			
		}catch (Exception e) {
			System.out.println("b");
			printUsage();
			
		}
	}

	public static long convertStringToLong(String word) {
		long finalNumber=0;

				for  (int j = 0; j < word.length(); j++){
					char geneCode = word.charAt(j);
					byte conversion = 0;
					
					switch(geneCode){
					case 'a':
						conversion = A;
						break;
					case 'c':
						conversion = C;
						break;
					case 'g':
						conversion = G;
						break;
					case 't':
						conversion = T;
						break;
					default:
						//not a gene character
						break;
					}
					
					//offset the finalLong value
					finalNumber= finalNumber<<2;
					
					//append the new char to finalLong
					finalNumber += conversion;
				}

			return finalNumber;
			}
		
		
		/**
		 * converting a long value into a gene sequence which contains a, c, g, or t
		 * @param c long to convert
		 * @param len int of sequence
		 * @return String representation of code
		 */
		public static String backConvert1(long c, int len){
			StringBuilder backConvert = new StringBuilder("");
			for(int i = 1; i <= len; i++){
				long numToConvert = c;
				numToConvert = numToConvert >> (2*(len-i));
				
				switch((int)(numToConvert % 4)){
				case 0:
					backConvert.append("a");
					break;
				case 1:
					backConvert.append("c");
					break;
				case 2:
					backConvert.append("g");
					break;
				case 3:
					backConvert.append("t");
					break;
				}
			}
			return backConvert.toString();	
		}
		
	/**
	 * Prints usage message for invalid inputs
	 */
	private static void printUsage() {
		System.err.print("Usage: GeneBankSearch <0/1(no/with Cache)> ");
		System.err.print("<btree file> <query file> [<cache size>] ");
		System.err.println("[<debug level>");
		
		System.exit(0);
	}
}
