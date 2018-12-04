import java.io.*;
import java.util.Scanner;

public class GeneBankCreateBTree {
    /**
     * Main method
     *
     * @param args - String[]
     */
    public static void main(String[] args) {
        if (args.length < 4 || args.length > 6) {
            printUsage();
        }
        // read in command-line arguments
        int cache = Integer.parseInt(args[0]);
        int degree = Integer.parseInt(args[1]);
        String fileName = args[2];
        int length = Integer.parseInt(args[3]);
        int cacheSize = 0;          //default for 4096
        int debugLevel = 0;     //default level
        if (args.length >= 5)
            cacheSize = Integer.parseInt(args[4]);
        if (args.length == 6)
            debugLevel = Integer.parseInt(args[4]);
        BTree bTree;
        File file = new File(fileName);
        int count;

        try {
            Scanner fileScan = new Scanner(fileName);
        } catch (FileNotFoundException e) {
            printUsage();
        }
        if (cacheSize > 0) {
            bTree = new BTree(fileName, degree, length, debugLevel, cacheSize);
        } else {
            bTree = new BTree(fileName, degree, length, debugLevel);

        }
        while (fileScan.hasNext()) {

            long nextSequence = fileScan.nextSubstring();
            if (nextSequence >= 0) {
                bTree.insert(nextSequence);
            }
        }
    }
}