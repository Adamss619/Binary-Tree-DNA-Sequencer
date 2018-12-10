
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.util.IllegalFormatException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This program will search a B Tree for strings of a query file. If the program
 * finds the code then it will print the human genome, if it does not find the
 * code it will print a statement that says that the file must exist in the same
 * directory.
 *
 * @author Kris Veruari
 */

public class GeneBankSearch {
    private File bTree, query;
    private GeneticFileConstructor dataReader;
    private int degree, debug;
    private String BTreeName, QueryName;
    private PrintWriter printWriter;

    public GeneBankSearch(String[] args) {
        parseArguments(args);
        searchAndPrint();
    }

    public static void main(String[] args) {
        new GeneBankSearch(args);
    }

    /**
     * This method will check the arguments passed for an invalid input and
     * assigns the arguments to global variables
     *
     * @param args
     *            arguments passed into the program
     */
    private void parseArguments(String[] args) {

        if (args.length < 3 || args.length > 5)
            printUsage("Program requires between 3 and 5 valid arguements to run");

        try {
            if (Integer.parseInt(args[0]) == 1)
                this.debug = 1;
            else if (Integer.parseInt(args[0]) == 0)
                this.debug = 0;
        } catch (IllegalFormatException e) {
            printUsage("1st argument must be either 0 or 1 ");
        }

        BTreeName = args[1];
        bTree = new File(BTreeName);

        QueryName = args[2];
        query = new File(QueryName);
    }

    /**
     * This method searches the B Tree for the next line of the text file query
     * and then will print the human genome if found. If not found an error
     * statement will display.
     */
    private void searchAndPrint() {
        try {
            long key = 0;
            String humanGenome;

            Scanner scanQuery = new Scanner(query);
            dataReader = new GeneticFileConstructor(bTree);
            if (debug == 1) {
                createOutputFile();
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

                key = makeBinary(geneticSequence);
                humanGenome = bTreeSearch(dataReader.readData(rootLocation), key);
                if (humanGenome != null)

                    System.out.println(humanGenome);
            }
            if (debug == 1) {
                System.out.println("An output file (" + BTreeName + " " + QueryName + " Results) has been created.");
            }
            scanQuery.close();
        } catch (FileNotFoundException e) {
            printUsage("The query and BTree file must exist within the same directory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will search the BTree with a starting node for key value
     *
     * @param startNode,
     *            node to start search from
     * @param keySearch,
     *            the key to search for
     * @return the long if the key is found else
     */
    private String bTreeSearch(BTreeNode startNode, long keySearch) {
        int i = startNode.getSize();
        while (i > 0 && keySearch <= startNode.getParentValue(i - 1)) {
            if (keySearch == startNode.getParentValue(i - 1)) {
                if (debug == 1) {
                    printWriter.println(decoder(startNode.getParentValue(i - 1)) + ": " + startNode.getParentFrequancy(i - 1));
                }
                return decoder(startNode.getParentValue(i - 1)) + ": " + startNode.getParentFrequancy(i - 1);
            }
            i--;
        }
        if (startNode.getChild(i) != null) {
            return bTreeSearch(startNode.getChild(i), keySearch);
        }
        return null;
    }

    /**
     * This will convert the variable subString into a long containing the bit
     * representation of the human genome
     *
     * @return the long from the inputStr, or -1 if there are invalid characters
     */
    private long makeBinary(String inputStr) {

        int i = 0;
        long retval = 1;
        String[] split = inputStr.split("");

        while (i < inputStr.length()) {
            retval = retval << 2;

            if (split[i].equals("A") || split[i].equals("a"))
                retval = retval | 0;
            else if (split[i].equals("T") || split[i].equals("t"))
                retval = retval | 3;
            else if (split[i].equals("C") || split[i].equals("c"))
                retval = retval | 1;
            else if (split[i].equals("G") || split[i].equals("g"))
                retval = retval | 2;
            else
                return -1;
            i++;
        }

        return retval;
    }

    /**
     * This will decode a long back into its original human genome and returns
     * it as a String
     *
     * @param genomeSequence,
     *            the long to be decoded back into a String
     * @return the decodedStr that was decoded from the long
     */
    public String decoder(long genomeSequence) {
        String decodedStr = "";
        if (genomeSequence == -1) {
            decodedStr = decodedStr.concat("n");
            return decodedStr;
        }
        long letter = 0;

        while (genomeSequence != 1) {
            letter = genomeSequence & 3;
            genomeSequence = genomeSequence >> 2;

            if (letter == 0)
                decodedStr = "a" + decodedStr;
            else if (letter == 3)
                decodedStr = "t" + decodedStr;
            else if (letter == 1)
                decodedStr = "c" + decodedStr;
            else if (letter == 2)
                decodedStr = "g" + decodedStr;
            else
                decodedStr = "n" + decodedStr;
        }

        return decodedStr;
    }

    /**
     * This will print what the program will execute at launch and/or the error
     * statement that caused the program not to execute
     *
     * @param error,
     *            the error that caused the crash
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

    private void createOutputFile() throws IOException {
        File outputFile = new File(BTreeName + " " + QueryName + " Results");
        printWriter = new PrintWriter(new FileWriter(outputFile), true);
    }
}
