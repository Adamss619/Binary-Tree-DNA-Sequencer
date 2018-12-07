
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * BTree class is the ADT of a B-Tree
 *
 * @param <T>
 * @author justin, spencer, binod, alkinish                 //fix the butchered one name
 */
public class BTree<T> {

    private BTreeNode<T> root;
    private FindNodeSize size;
    private GeneticFileConstructor readWrite;
    private PrintWriter Pwriter;
    private final long BTREE_METADATA = 16;
    private String fileName;
    private int degree;
    private int sequenceLength;
    private int debugLevel;
    private int nodeSize;
    private long offset;


    /**
     * BTree constructor without a cache takes in the parameters to create the first root node of the BTree
     *
     * @param dataFile
     * @param degree
     * @param sequenceLength
     * @param debugLevel
     * @throws IOException
     */
    public BTree(String dataFile, int degree, int sequenceLength, int debugLevel) throws IOException {
        this.fileName = dataFile;
        this.degree = degree;
        this.sequenceLength = sequenceLength;
        this.debugLevel = debugLevel;
        offset = 0;
        readWrite = new GeneticFileConstructor(dataFile, this.degree, this.sequenceLength);
        readWrite.writeToTreeMetaData();
        size = new FindNodeSize();
        this.nodeSize = size.CalculateSize(this.degree);
        offset = BTREE_METADATA;
        this.root = new BTreeNode(this.degree, offset);
        advanceOffset();
        root.setLeaf(true);
        writeNode(root);
    }

    /**
     * BTree constructor with a cache takes in the parameters to create the first root node of the BTree
     *
     * @param dataFile
     * @param degree
     * @param sequenceLength
     * @param debugLevel
     * @param cacheSize
     * @throws IOException
     */
    public BTree(String dataFile, int degree, int sequenceLength, int debugLevel, int cacheSize) throws IOException {
        this.degree = degree;
        this.sequenceLength = sequenceLength;
        this.debugLevel = debugLevel;
        offset = 0;
        readWrite = new GeneticFileConstructor(dataFile, this.degree, this.sequenceLength, cacheSize);
        readWrite.writeToTreeMetaData();
        size = new FindNodeSize();
        this.nodeSize = size.CalculateSize(this.degree);
        offset = BTREE_METADATA;
        this.root = new BTreeNode(this.degree, offset);
        advanceOffset();
        root.setLeaf(true);
        writeNode(root);
    }

    /**
     * call insert with passing in the value to insert into the BTree and it checks if the current root is full
     * if it is full it will split the root into two children and move the root up. If it is not full it will insert
     * the value passed in into the root node
     *
     * @param nextSequence
     * @throws IOException
     */
    public void insert(long nextSequence) throws IOException {
        BTreeNode root = this.root;
        //check if the root node key array is full, if true call splitChild
        if (root.isFull()) {
            BTreeNode newChild = new BTreeNode(this.degree, offset);
            advanceOffset();
            this.root = newChild;
            readWrite.updateLocationOfRoot(this.root.getOffset());
            newChild.children[0] = root.getOffset();
            splitChild(newChild, 0, root);
            insertNonFull(newChild, nextSequence);
        } else { // if the root is not full calls inertNonFull
            insertNonFull(root, nextSequence);
        }
    }

    /**
     * splitChild is called when a parent node is full and needs to split in order to make room and move the
     * root node up a level.
     *
     * @param parent
     * @param i
     * @param splitNode
     * @throws IOException
     */
    public void splitChild(BTreeNode parent, int i, BTreeNode splitNode) throws IOException {
        // make a new node with the BTree degree and offset then move the offset up one
        BTreeNode newNode = new BTreeNode(this.degree, offset);
        advanceOffset();
        newNode.setLeaf(splitNode.getLeaf());
        newNode.setSize(degree - 1);
        //go through the node being split and assign half the values to the new node and remove them from the node being split
        for (int j = 0; j <= degree - 2; j++) {
            newNode.keyArray[j] = splitNode.keyArray[degree + j];
            newNode.frequency[j] = splitNode.frequency[degree + j];
            splitNode.keyArray[degree + j] = 0;
            splitNode.frequency[degree + j] = 0;
        }
        // do the same but with the children nodes
        if (!splitNode.getLeaf()) {
            for (int j = 0; j <= degree - 1; j++) {
                newNode.children[j] = splitNode.children[j + degree];
                splitNode.children[j + degree] = 0;
            }
        }
        newNode.setParent(parent.getOffset()); // set parent
        splitNode.setSize(degree - 1);
        // move the parents children down to the children of newNode
        for (int j = parent.getSize(); j > i; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        // move the newNode to the child of parent
        parent.children[i + 1] = newNode.getOffset();
        for (int j = parent.getSize(); j > i; j--) {
            parent.keyArray[j] = parent.keyArray[j - 1];
            parent.frequency[j] = parent.frequency[j - 1];
        }
        parent.keyArray[i] = splitNode.keyArray[degree - 1];
        parent.frequency[i] = splitNode.frequency[degree - 1];
        splitNode.keyArray[degree - 1] = 0;
        splitNode.frequency[degree - 1] = 0;
        parent.setSize(parent.getSize() + 1);
        writeNode(parent);
        writeNode(splitNode);
        writeNode(newNode);
    }

    /**
     * insertNonFull inserts a new value into the current root node or parent
     *
     * @param node
     * @param nextSequence
     * @throws IOException
     */
    public void insertNonFull(BTreeNode node, long nextSequence) throws IOException {
        int i = node.getSize();

        if (node.getLeaf()) {
            if (i > 0 && nextSequence == node.keyArray[i - 1]) {
                node.frequency[i - 1]++;
                writeNode(node);
                return;
            }
            while (i >= 1 && nextSequence <= node.keyArray[i - 1]) {
                if (nextSequence == node.keyArray[i - 1]) {
                    node.frequency[i - 1]++;
                    writeNode(node);
                    return;
                }
                i--;
            }
            i = node.getSize();
            while (i >= 1 && nextSequence < node.keyArray[i - 1]) {
                node.keyArray[i] = node.keyArray[i - 1];
                node.frequency[i] = node.frequency[i - 1];
                i--;
            }
            node.keyArray[i] = nextSequence;
            node.frequency[i] = 1;
            node.setSize(node.getSize() + 1);
            writeNode(node);
            return;
        } else {
            if (i > 0 && nextSequence == node.keyArray[i - 1]) {
                node.frequency[i - 1]++;
                writeNode(node);
                return;
            }
            while (i >= 1 && nextSequence <= node.keyArray[i - 1]) {
                if (nextSequence == node.keyArray[i - 1]) {
                    node.frequency[i - 1]++;
                    writeNode(node);
                    return;
                }
                i--;
            }
            // Read child in from file or cache that sequence will pass to
            BTreeNode child = readNode(node.children[i]);
            if (child.isFull()) {
                splitChild(node, (i), child);
                if (nextSequence == node.keyArray[i]) {
                    node.frequency[i]++;
                    writeNode(node);
                    return;
                }
                // compare the values at position i with the new value to find where to insert it
                if (nextSequence > node.keyArray[i]) {
                    i++;
                    child = readNode(node.children[i]);
                }
            }
            insertNonFull(child, nextSequence);
        }
    }

    /**
     * this is a recursion that writes the BTree data to the new file in order
     *
     * @param node
     * @throws IOException
     */
    public void inOrder(BTreeNode node) throws IOException {
        BTreeNode tempNode;

        if (node.getLeaf()) {
            writeNodeToGBK(node);
            return;
        }
        for (int i = 0; i <= node.getSize(); i++) {
            if (i < (2 * degree) - 1) {
                if (node.keyArray[i] > 0 || node.children[i] > 0) {
                    tempNode = readWrite.readData(node.children[i]);
                    inOrder(tempNode);
                    if (i < node.getSize()) {
                        Pwriter.print(node.frequency[i] + "\t\t" + sequenceBuilder(node.keyArray[i]) + "\n");
                        Pwriter.flush();
                    }
                }
            } else if (node.children[i] > 0) {
                tempNode = readWrite.readData(node.children[i]);
                inOrder(tempNode);
                if (i < node.getSize()) {
                    Pwriter.print(node.frequency[i] + "\t\t" + sequenceBuilder(node.keyArray[i]) + "\n");
                    Pwriter.flush();
                }
            } else {
                return;
            }
        }
    }

    private void createGBKFile() throws IOException {
        File GBKFile = new File(fileName + ".btree.data." + sequenceLength + "." + degree);
        Pwriter = new PrintWriter(new FileWriter(GBKFile), true);
    }

    private void writeNodeToGBK(BTreeNode node) {
        for (int i = 0; i < (2 * degree) - 1; i++) {
            if (node.keyArray[i] > 0) {
                Pwriter.print(node.frequency[i] + "\t\t" + sequenceBuilder(node.keyArray[i]) + "\n");
            }
        }
        Pwriter.flush();
    }

    private void advanceOffset() {
        offset += nodeSize;
    }

    public void writeNode(BTreeNode node) throws IOException {
        readWrite.writeDatatoFile(node);
    }

    public BTreeNode readNode(long fileOffset) throws IOException {
        return readWrite.readData(fileOffset);
    }

    public void doneInserting() throws IOException {
        System.out.println("All sequences have been inserted.");
        writeNode(root);
        //readWrite.writeCache();
        System.out.println("A B-Tree file (" + readWrite.getFileName() + ") has been created.");
        if (debugLevel == 1) {
            System.out.println("Generating GBK file...");
            createGBKFile();
            inOrder(root);
            System.out.println("A debug file (" + fileName + ".btree.data." + sequenceLength + "." + degree + ") has been created.");
            Pwriter.close();
        }
    }

    public String sequenceBuilder(long sequence) {
        String str = "";
        if (sequence == -1) {
            str = str.concat("n");
            return str;
        }
        long letter = 0;

        while (sequence != 1) {
            letter = sequence & 3;
            sequence = sequence >> 2;

            if (letter == 0)
                str = "a" + str;
            else if (letter == 3)
                str = "t" + str;
            else if (letter == 1)
                str = "c" + str;
            else if (letter == 2)
                str = "g" + str;
            else
                str = "n" + str;
        }

        return str;
    }
}
