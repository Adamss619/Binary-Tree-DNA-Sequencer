
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GeneticFileConstructor {

    protected RandomAccessFile bTreeFile, dump, searchTree;

    private int sequenceLength;
    final int maxnodeSize = 4096;
    private String bTreeFileName;
    private boolean canWrite;
    final int metadata = 16;
    final int parentPointer = 8;
    private long rootLocation;
    private int degree;
    final int frequency = 8;
    final int objectSize = 8;
    final int childrenPointer = 8;

    /**
     *
     * @param dataFile
     * @throws IOException
     */
    public GeneticFileConstructor(File dataFile) throws IOException {
        File treeData = dataFile;
        bTreeFile = new RandomAccessFile(treeData, "r");
        canWrite = false;
    }

    /**
     *
     * @param dataFile
     * @param cacheSize
     * @throws IOException
     */
    public GeneticFileConstructor(File dataFile, int cacheSize) throws IOException {
        File treeData = dataFile;

        canWrite = false;
        bTreeFile = new RandomAccessFile(treeData, "r");
    }

    /**
     *
     * @param dataFile
     * @param degree
     * @param sequenceLength
     * @throws IOException
     */
    public GeneticFileConstructor(String dataFile, int degree, int sequenceLength) throws IOException {
        this.degree = degree;
        this.sequenceLength = sequenceLength;

        canWrite = true;
        this.bTreeFileName = dataFile + ".btree.data." + this.sequenceLength + "." + this.degree;
        File datafile = new File(bTreeFileName);
        bTreeFile = new RandomAccessFile(datafile, "rw");
        rootLocation = metadata;
    }

    /**
     *
     * @throws IOException
     */
    public GeneticFileConstructor() throws IOException {
        File dumpFile = new File("dump");
        dump = new RandomAccessFile(dumpFile, "rw");
    }

    /**
     *
     * @param dataFile
     * @param degree
     * @param sequenceLength
     * @param cacheSize
     * @throws IOException
     */
    public GeneticFileConstructor(String dataFile, int degree, int sequenceLength, int cacheSize) throws IOException {
        this.degree = degree;
        this.sequenceLength = sequenceLength;

        canWrite = true;
        this.bTreeFileName = dataFile + ".btree.data." + this.sequenceLength + "." + this.degree;
        File datafile = new File(bTreeFileName);
        bTreeFile = new RandomAccessFile(datafile, "rw");
        rootLocation = metadata;
    }

    /**
     * Find the root
     * @return retVal
     * @throws IOException
     */
    public long rootFinder() throws IOException {
        bTreeFile.seek(0);
        long retVal = bTreeFile.readLong();
        return retVal;
    }

    /**
     *
     * @throws IOException
     */
    public void writeToTreeMetaData() throws IOException {

        bTreeFile.seek(0);
        bTreeFile.writeLong(rootLocation);
        bTreeFile.writeInt(this.degree); // t
        bTreeFile.writeInt(this.sequenceLength); // k
    }

    /**
     * This will get the sequence length
     * @return retVal
     * @throws IOException
     */
    public int getLengthOfSequence() throws IOException {
        int retVal;
        bTreeFile.seek(12);
        retVal = bTreeFile.readInt();
        return retVal;
    }

    /**
     * This will get the degree
     * @return retVal
     * @throws IOException
     */
    public int getDegree() throws IOException {
        int retVal;
        bTreeFile.seek(8);
        retVal = bTreeFile.readInt();
        return retVal;
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
     * This will update the root location
     * @param rootOffset
     * @throws IOException
     */
    public void updateLocationOfRoot(long rootOffset) throws IOException {
        this.rootLocation = rootOffset;
        bTreeFile.seek(0);
        bTreeFile.writeLong(rootLocation);
    }

    /**
     * This method will read data
     * @param nodeOffset
     * @return
     * @throws IOException
     */
    public BTreeNode readData(long nodeOffset) throws IOException {
        BTreeNode retNode;

        retNode = readMetaData(nodeOffset);
        return retNode;
    }

    /**
     * This method will write to file
     * @param node
     * @throws IOException
     */
    public void writeMetaDataToFile(BTreeNode node) throws IOException {

        bTreeFile.seek(node.getOffSet());
        bTreeFile.writeLong(node.getOffSet());

        bTreeFile.writeInt(node.getSize());
        bTreeFile.writeBoolean(node.getLeaf());

        for (int i = 0; i < node.getSize(); i++) {
            bTreeFile.writeLong(node.getParentValue(i));
            bTreeFile.writeInt(node.getParentFrequancy(i));
        }
        for (int i = 0; i <= node.getSize(); i++) {
            if (node.getChild(i) == null) {
                bTreeFile.writeLong(0);
                bTreeFile.writeInt(0);
            } else {
                for (int j = 0; j < node.getChild(i).getSize(); j++) {
                    bTreeFile.writeLong(node.getChild(i).getParentValue(j));
                    bTreeFile.writeInt(node.getChild(i).getParentFrequancy(j));
                }
            }
        }
        bTreeFile.writeLong(node.getParentOffset());
    }

    /**
     * This method reads from file
     * @param nodeOffset
     * @return
     * @throws IOException
     */
    public BTreeNode readMetaData(long nodeOffset) throws IOException {

        bTreeFile.seek(nodeOffset);
        BTreeNode retNode = new BTreeNode(degree);
        retNode.setOffSet(bTreeFile.readLong());
        retNode.setSize(bTreeFile.readInt());
        retNode.setLeaf(bTreeFile.readBoolean());

        for (int i = 0; i < retNode.getSize(); i++) {
            TreeObject temp = new TreeObject(bTreeFile.readLong(), bTreeFile.readInt());
            retNode.setParent(i, temp);
        }
        for (int i = 0; i <= retNode.getSize(); i++) {
            BTreeNode child = new BTreeNode(degree);
            for (int j = 0; j < retNode.getSize() + 1; j++) {
                TreeObject temp = new TreeObject(bTreeFile.readLong(), bTreeFile.readInt());
                child.setParent(j, temp);
            }
            retNode.setChild(i,child);
        }
        retNode.setParentOffset(bTreeFile.readLong());
        return retNode;
    }

    /**
     *
     * @throws IOException
     */
    public void writeDump() throws IOException {
        File dumpFile = new File("dump");
        dump = new RandomAccessFile(dumpFile, "rw");
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
}
