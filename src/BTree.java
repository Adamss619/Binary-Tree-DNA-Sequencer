
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
    private TreeObject object;


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
        object = new TreeObject(offset, 1);
        this.root = new BTreeNode<>(this.degree);
        this.root.setParent(0, object);
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
        object = new TreeObject(offset, 1);
        this.root = new BTreeNode<>(this.degree);
        this.root.setParent(0, object);
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
            BTreeNode newRoot = new BTreeNode(this.degree);         //create a new node that will be the new parent or root node
            advanceOffset();
            this.root = newRoot;                                    //save the new root to teh btree
            readWrite.updateLocationOfRoot(this.root.getOffset());
            //TreeObject node = new TreeObject(root.getOffset(),  root.getFrequancy());
            newRoot.setChild(0, root);                           //set the old root to the child node of the new root
            splitChild(newRoot, 0, root);                        //split the old root into two child nodes of the new root
            insertNonFull(newRoot, nextSequence);                   //now the new root should have space to insert the new sequence
        } else { // if the root is not full calls inertNonFull
            insertNonFull(root, nextSequence);                      //if there is space in the current nodes (root or children of root)
            // then insert the new sequence in teh right position
        }
    }

    /**
     * splitChild is called when a parent node is full and needs to split in order to make room and move the
     * root node up a level. The parameter parent is (if it is the new saved root will be an empty node)
     * that is the parent node of splitNode (which is the full node). splitNode needs to move the middle object
     * (middle of splitNodes parent array of objects) up to the parent node (parents parent array of objects)
     * and then divide into two child nodes with the remaining half of splitNodes objects (values in parent array)
     * will be set as the newChild nodes' objects(values in parent array). Then the children of splitNode
     * will be split the same way to keep the objects in the parent array of both the newChild and splitNode together.
     *
     *
     * @param parent
     * @param i
     * @param splitNode
     * @throws IOException
     */
    public void splitChild(BTreeNode parent, int i, BTreeNode splitNode) throws IOException {
        // make a new node with the BTree degree and offset then move the offset up one
        BTreeNode newChild = new BTreeNode(this.degree);                         //create a new node that will contain half of splitNode
        advanceOffset();
        newChild.setLeaf(splitNode.getLeaf());                                  //set the new child node to the same leaf status as splitNode
        newChild.setSize(degree - 1);                                           //set the new size equal
        //go through the node being split and assign half the values to the new node and remove them from the node being split
        for (int j = 0; j <= degree - 2; j++) {
            TreeObject node = new TreeObject(splitNode.getParentValue(degree + j), splitNode.getParentFrequancy(degree + j));
            newChild.setParent(j, node);
            splitNode.setParent(j, null);
        }
        // do the same but with the children nodes
        if (!splitNode.getLeaf()) {
            for (int j = 0; j <= degree - 1; j++) {
                newChild.setChild(j, splitNode.getChild(j + degree));
                splitNode.setChild(j + degree, null);
            }
        }
        //   for (int j = 0; j < parent.getSize(); j++) {
        //     newNode.setParent(j, parent.getParent(j)); // set parent
        newChild.setParentNode(parent); // set parent node
        //}
        splitNode.setSize(degree - 1);
        // move the parents children down to the children of newNode
        for (int j = parent.getSize(); j > i; j--) {
            parent.setChild(j + 1, parent.getChild(j));
        }
        // move the newNode to the child of parent
        // TreeObject node = new TreeObject(newChild.getOffset(), newChild.getFrequancy());
        parent.setChild(i + 1, newChild);
        for (int j = parent.getSize(); j > i; j--) {
            // TreeObject temp = new TreeObject(parent.getParentValue(j - 1), parent.getParentFrequancy(j - 1));
            parent.setParent(j, parent.getParent(j - 1));
        }
        parent.setParent(i, splitNode.getParent(i));
        splitNode.setParent(degree - 1, null);
        parent.setSize(parent.getSize() + 1);
        writeNode(parent);
        writeNode(splitNode);
        writeNode(newChild);
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
            if (i > 0 && nextSequence == node.getParentValue(i - 1)) {
                node.setFrequancy(i - 1);
                writeNode(node);
                return;
            }
            while (i >= 1 && nextSequence <= node.getParentValue(i - 1)) {
                if (nextSequence == node.getParentValue(i - 1)) {
                    node.setFrequancy(i - 1);
                    writeNode(node);
                    return;
                }
                i--;
            }
            i = node.getSize();
            while (i >= 1 && nextSequence < node.getParentValue(i - 1)) {
                node.setParent(i, node.getParent(i - 1));
                i--;
            }
            TreeObject temp = new TreeObject(nextSequence, degree, 1);
            node.setParent(i, temp);
            node.setSize(node.getSize() + 1);
            writeNode(node);
            return;
        } else {
            if (i > 0 && nextSequence == node.getParentValue(i - 1)) {
                node.setFrequancy(i - 1);
                writeNode(node);
                return;
            }
            while (i >= 1 && nextSequence <= node.getParentValue(i - 1)) {
                if (nextSequence == node.getParentValue(i - 1)) {
                    node.setFrequancy(i - 1);
                    writeNode(node);
                    return;
                }
                i--;
            }
            // Read child in from file or cache that sequence will pass to
            BTreeNode child = readNode(node.getChild(i).getData());
            if (child.isFull()) {
                splitChild(node, (i), child);
                if (nextSequence == node.getParentValue(i)) {
                    node.setFrequancy(i);
                    writeNode(node);
                    return;
                }
                // compare the values at position i with the new value to find where to insert it
                if (nextSequence > node.getParentValue(i)) {
                    i++;
                    child = readNode(node.getChildValue(i));
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
                if (node.getParentValue(i) > 0 || node.getChildValue(i) > 0) {
                    tempNode = readWrite.readData(node.getChildValue(i));
                    inOrder(tempNode);
                    if (i < node.getSize()) {
                        Pwriter.print(node.getParentFrequancy(i) + "\t\t" + sequenceBuilder(node.getParentValue(i)) + "\n");
                        Pwriter.flush();
                    }
                }
            } else if (node.getChildValue(i) > 0) {
                tempNode = readWrite.readData(node.getChildValue(i));
                inOrder(tempNode);
                if (i < node.getSize()) {
                    Pwriter.print(node.getParentFrequancy(i) + "\t\t" + sequenceBuilder(node.getParentValue(i)) + "\n");
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
            if (node.getParentValue(i) > 0) {
                Pwriter.print(node.getParentFrequancy(i) + "\t\t" + sequenceBuilder(node.getParentValue(i)) + "\n");
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
