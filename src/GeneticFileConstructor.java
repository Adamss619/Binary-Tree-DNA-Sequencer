
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class GeneticFileConstructor {


    protected RandomAccessFile bTreeFileData, dump, treeSearch;


    private long rootLocation;
    private int degree;
    final int frequency = 8;
    final int objectSize = 8;
    final int childrenPointer = 8;
    private int lengthOfSequence;
    final int maximumDataNodeSize = 4096;
    private String bTreeFileName;
    private TreeObject object;

    private boolean writeCheck;
    final int metadata = 16;
    final int parentPointer = 8;


    /**
     * @param dataFile
     * @param degree
     * @param sequenceLength
     * @throws IOException
     */
    public GeneticFileConstructor(String dataFile, int degree, int sequenceLength) throws IOException {

        File datafile = new File(bTreeFileName);
        bTreeFileData = new RandomAccessFile(datafile, "rw");
        writeCheck = true;
        this.bTreeFileName = dataFile + ".btree.data." + this.lengthOfSequence + "." + this.degree;

        degree = degree;
        lengthOfSequence = sequenceLength;
        rootLocation = metadata;
    }

    /**
     * @param dataFile
     * @throws IOException
     */
    public GeneticFileConstructor(File dataFile) throws IOException {
        File treeData = dataFile;
        bTreeFileData = new RandomAccessFile(treeData, "r");
        writeCheck = false;
    }

    /**
     * @param dataFile
     * @param degree
     * @param sequenceLength
     * @param cacheSize
     * @throws IOException
     */
    public GeneticFileConstructor(String dataFile, int degree, int sequenceLength, int cacheSize) throws IOException {
        this.degree = degree;
        this.lengthOfSequence = sequenceLength;

        writeCheck = true;
        this.bTreeFileName = dataFile + ".btree.data." + this.lengthOfSequence + "." + this.degree;
        File datafile = new File(bTreeFileName);

        bTreeFileData = new RandomAccessFile(datafile, "rw");
        rootLocation = metadata;
    }

    /**
     * @throws IOException
     */
    public GeneticFileConstructor() throws IOException {
        File fDump = new File("dump");

        dump = new RandomAccessFile(fDump, "rw");
    }

    /**
     * @param dataFile
     * @param cacheSize
     * @throws IOException
     */
    public GeneticFileConstructor(File dataFile, int cacheSize) throws IOException {
        File treeData = dataFile;

        writeCheck = false;

        bTreeFileData = new RandomAccessFile(treeData, "r");
    }


    /**
     * Finds the root of the tree file
     *
     * @return returnVal
     * @throws IOException
     */
    public long rootFinder() throws IOException {

        bTreeFileData.seek(0);
        long returnVal = bTreeFileData.readLong();

        return returnVal;
    }

    /**
     * @throws IOException
     */
    public void writeToTreeMetaData() throws IOException {
        bTreeFileData.writeLong(rootLocation);
        bTreeFileData.writeInt(degree);

        bTreeFileData.writeInt(lengthOfSequence);

        bTreeFileData.seek(0);

    }

    /**
     * This will get the sequence length
     *
     * @return returnVal
     * @throws IOException
     */
    public int getLengthOfSequence() throws IOException {
        bTreeFileData.seek(12);

        int returnVal = bTreeFileData.readInt();
        return returnVal;
    }

    /**
     * This will get the degree
     *
     * @return retVal
     * @throws IOException
     */
    public int getDegree() throws IOException {
        bTreeFileData.seek(8);

        int retVal = bTreeFileData.readInt();
        return retVal;
    }


    /**
     * This will update the root location
     *
     * @param rootOffset
     * @throws IOException
     */
    public void updateLocationOfRoot(long rootOffset) throws IOException {
        this.rootLocation = rootOffset;
        bTreeFileData.seek(0);
        bTreeFileData.writeLong(rootLocation);
    }

    /**
     * @param degree
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getFileName() {
        return this.bTreeFileName;
    }

    /**
     * This will write Data
     *
     * @param node
     * @throws IOException
     */
    public void writeDatatoFile(BTreeNode node) throws IOException {
        writeMetaDataToFile(node);
    }

    /**
     * This method will read data
     *
     * @param nodeOffset
     * @return
     * @throws IOException
     */
    public BTreeNode readData(long nodeOffset) throws IOException {
        BTreeNode returnNode;

        returnNode = readMetaData(nodeOffset);
        return returnNode;
    }

    /**
     * This method will write to file
     *
     * @param node
     * @throws IOException
     */
    public void writeMetaDataToFile(BTreeNode node) throws IOException {

        BTreeNode writenNode = node;

        //  bTreeFileData.seek(writenNode.getOffset());

        // bTreeFileData.writeLong(writenNode.getOffset());

        //bTreeFileData.writeInt(writenNode.getSize());

        //bTreeFileData.writeBoolean(writenNode.getLeaf());

        for (int i = 0; i < writenNode.getSize(); i++) {
            bTreeFileData.writeLong(writenNode.getParentValue(i));
        }
        for (int i = 0; i < writenNode.getSize(); i++) {
            bTreeFileData.writeLong(writenNode.getParentFrequancy(i));
        }
        for (int i = 0; i < writenNode.getSize() + 1; i++) {
            if (writenNode.getChild(i) != null)
                writeMetaDataToFile(writenNode.getChild(i));
            //  bTreeFileData.writeLong(writenNode.getChild(i));
        }
        //  bTreeFileData.writeLong(writenNode.parentNode.getParentOffset());
    }

    /**
     * This method reads from file
     *
     * @param nodeOffset
     * @return
     * @throws IOException
     */
    public BTreeNode readMetaData(long nodeOffset) throws IOException {

        bTreeFileData.seek(nodeOffset);
        BTreeNode returnNode = new BTreeNode(degree);
        object = new TreeObject(nodeOffset, 1);
        returnNode.setParent(0, object);
        returnNode.setSize(bTreeFileData.readInt());
        returnNode.setLeaf(bTreeFileData.readBoolean());

        for (int i = 0; i < ((2 * degree) - 1); i++) {
            TreeObject parentNode = new TreeObject(bTreeFileData.readLong(), 1);

            returnNode.setParent(i, parentNode);

        }
        for (int i = 0; i < (2 * degree); i++) {
            BTreeNode newNode = new BTreeNode(degree);
            TreeObject childNode = new TreeObject(bTreeFileData.readLong(), 1);
            newNode.setParent(0, childNode);
            returnNode.setChild(i, newNode);
        }
        TreeObject parentNode = new TreeObject(bTreeFileData.readLong(), 1);

        return returnNode;
    }

    /**
     * @throws IOException
     */
    public void writeDump() throws IOException {
        File dumpFile = new File("dump");
        dump = new RandomAccessFile(dumpFile, "rw");
    }


}


