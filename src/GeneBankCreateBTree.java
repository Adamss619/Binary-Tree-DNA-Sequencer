
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GeneBankCreateBTree {
    private int degree, sequenceLength, cacheSize, debugLevel, insertCount;
    private String gbkFileName;
    private File gbkFile;
    private Parser parse;
    private BTree bTree;

    public static void main(String[] args) throws IOException {

        new GeneBankCreateBTree(args);

    }

    public GeneBankCreateBTree(String[] args) throws IOException {
        parseArguments(args);

        try {
            parse = new Parser(gbkFile, sequenceLength);
        } catch (FileNotFoundException e) {
            printUsage("The file " + gbkFileName + " could not be found.");
        }
        if (cacheSize > 0) {
            bTree = new BTree(gbkFileName, degree, sequenceLength, debugLevel, cacheSize);
        } else {
            bTree = new BTree(gbkFileName, degree, sequenceLength, debugLevel);

        }
        System.out.println("Beginning to process file...");
        while (parse.nextStringExists()) {

            long nextSequence = parse.incrementStartingString();
            if (nextSequence > -1) {
                bTree.insert(nextSequence);
                insertCount++;
            }
            // Print insertion progress
            if (insertCount % 500 == 0) {
                System.out.println("..." + insertCount + " sequences inserted...");
            }
        }
        bTree.doneInserting(); // calls for cache to empty to disk
    }

    private void parseArguments(String[] args) {
        // Check number of arguments provided.
        if (args.length < 4 || args.length > 6) {
            printUsage("Program requires between 4 and 6 valid arguements to run");
        }

        try { // Verify 1st argument is an integer, and either 0 or 1.
            if (Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 1) {
                printUsage("1st argument is must be either 0 or 1");
            }
        } catch (NumberFormatException e) {
            printUsage("1st argument is must be either 0 or 1");
        }

        // Verify cacheSize was provided if 1 was entered, and that cacheSize is
        // positive integer.
        if (Integer.parseInt(args[0]) == 1) {
            if (args.length <= 4) {
                printUsage("Must enter cache size for a 5th arguement if 1 was entered for 1st arguement");
            }
            if (Integer.parseInt(args[4]) <= 0) {
                printUsage("Cache size (5th arguement) must be a positive integer.");
            } else {
                cacheSize = Integer.parseInt(args[4]);
            }
        } else {
            cacheSize = 0;
        }

        try { // Verify argument entered for degree is a positive integer.
            degree = Integer.parseInt(args[1]);

        } catch (NumberFormatException e) {
            printUsage("2nd argument declares degree of BTree, and must be an integer larger than 0.");
        }
        if (degree < 0) {
            printUsage("Degree of BTree must be an integer larger than 0.");
        }
        if (degree == 0) {
            FindNodeSize findOptimalDegree = new FindNodeSize();
            degree = findOptimalDegree.CalculateOptimalDegree();
        }

        gbkFileName = args[2];
        gbkFile = new File(gbkFileName);

        try { // Verify argument for sequenceLength is a positive integer
            // between 1 and 31.
            sequenceLength = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            printUsage("4th argument declares sequence length, and must be an integer between 1 and 31.");
        }
        if (sequenceLength < 1 || sequenceLength > 31) {
            printUsage("Sequence length must be an integer between 1 and 31.");
        }

        debugLevel = 0; // Default debug level.
        if (args.length == 6) { // Verify 6th argument is an integer.
            try {
                debugLevel = Integer.parseInt(args[5]);
            } catch (NumberFormatException e) {
                printUsage("6th argument (optional) declares debug level, and must be either 0 or 1.");
            }
        }
        // Verify 5th argument is an integer if no cacheSize is provided.
        else if (args.length == 5 && Integer.parseInt(args[0]) == 0) {
            try {
                debugLevel = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                printUsage("5th argument (optional) declares debug level, and must be either 0 or 1.");
            }
        }
        // Verify debugLevel provided is either 0 or 1.
        if (debugLevel < 0 || debugLevel > 1) {
            printUsage("Debug level must be either 0 or 1.");
        }
    }

    private void printUsage(String msg) {
        System.err.println(msg);
        System.out.println("Command-line arguments should be set up as such:");
        System.out.println(
                "java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
        System.out.println(
                "<0/1 (no/with Cache)>: 	A 0 declares that there will be no cache, while a 1 declares a cache");
        System.out.println(
                "			 If a cache is declared, a 5th argument must given, and must be a positive integer");
        System.out.println(
                "<degree>: 		The degree that will be used to generate the B-Tree.  \n");
        System.out.println("Entering 0 will default to the optimal degree size, based on a memory block of 4096 bytes.");
        System.out.println("degree must be an integer larger than 1.");
        System.out.println("<gbk file file>:		The gene bank file to parse the sequences of.");
        System.out.println("<sequence length>:		The length of DNA sequence to be analyzed.");

        System.out.println("[<cache Size>]:		(optional) if a cache is to be created, this argument is required");
        System.out.println(
                "			 The argument must be a positive integer (an integer too big may crash the program");
        System.out.println(
                "[<debug level>]: 	(optional) either 0 or 1.  0 will generate standard system messages.\n1 will produce a dump file containing all the valid sequences entered, with the frequency that they appear.");
        System.exit(1);
    }

}
