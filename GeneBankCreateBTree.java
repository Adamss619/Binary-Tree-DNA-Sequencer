import java.io.*;
import java.util.Scanner;

public class GeneBankCreateBTree {
    /**
     * Main method
     *
     * @param args - String[]
     */
    public static void main(String[] args) {
        // simple input verification
        if (args.length < 4 | args.length > 6) {
            System.out.println
                    ("Usage: java GeneBankCreateBTree <with cache> <degree> <gbk file> <sequence length> < | cache size> < | debug level>");
            System.out.println
                    ("<with cache>: 0 is without using a cache and 1 is use a cache.");
            System.out.println
                    ("<degree>: minimum degree, t, to be used for the B-Tree. If 0, " +
                            "then the optimum degree used is based on a disk block size of " +
                            "4096 bytes and the size of your B-Tree node on disk.\n");
            System.out.println
                    ("<gbk file>: the input file to be parsed");
            System.out.println
                    ("<sequence length>: the length of gene sequences.");
            System.out.println
                    ("<cache size>: the length of cache.");
            System.out.println
                    ("<debug level>: 0 to print usage and 1 to create dump file ");
            System.exit(1);
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

        Scanner fileScan = new Scanner()


    }
}
