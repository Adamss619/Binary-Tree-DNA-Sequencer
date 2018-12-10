import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;


/**
 * BTree class is the ADT of a B-Tree
 *
 * @param <T>
 * @author justin, spencer, binod, alkinish                 /
 */
public class BTree<T> {

    private BTreeNode<T> root;
    private FindNodeSize size;
    private GeneticFileConstructor readWrite;
    private PrintWriter writer;
    private final long BTREE_METADATA = 16;
    private String fileName;
    private int degree;
    private int sequenceLength;
    private int debugLevel;
    private int nodeSize;
    private long offset;
    private TreeObject object;
    private BTreeCache cache;
    private RandomAccessFile RAF;


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
        // object = new TreeObject(offset, 1);
        this.root = new BTreeNode<>(this.degree);
        this.root.setOffSet(offset);
        // this.root.setParent(0, object);
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
        // object = new TreeObject(offset, 1);
        this.root = new BTreeNode<>(this.degree);
        this.root.setOffSet(offset);
        //    this.root.setParent(0, object);
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
            newRoot.setOffSet(offset);
            advanceOffset();
            this.root = newRoot;                                    //save the new root to teh btree
            //TreeObject node = new TreeObject(root.getOffset(),  root.getFrequancy());
            newRoot.setChild(0, root);                           //set the old root to the child node of the new root
            newRoot.getChild(0).setOffSet(root.getOffSet());
            root.setParentNode(newRoot);
            splitChild(newRoot, 0, root);                        //split the old root into two child nodes of the new root
            insertNonFull(newRoot, nextSequence);                   //now the new root should have space to insert the new sequence
            readWrite.updateLocationOfRoot(newRoot.getOffSet());
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
        newChild.setOffSet(splitNode.getOffSet());
        splitNode.setOffSet(offset);
        advanceOffset();
        newChild.setLeaf(splitNode.getLeaf());                                  //set the new child node to the same leaf status as splitNode
        newChild.setSize(degree - 1);                                           //set the new size equal
        //go through the node being split and assign half the values to the new node and remove them from the node being split
        for (int j = 0; j <= degree - 2; j++) {
            TreeObject node = new TreeObject(splitNode.getParentValue(j), splitNode.getParentFrequancy(j));
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
        splitNode.setParentNode(parent);                                 // set parent node
        newChild.setParentNode(parent);                                 // set parent node
        newChild.setParentOffset(parent.getOffSet());                                 // set parent node
        splitNode.setParentOffset(parent.getOffSet());
        splitNode.setSize(degree - 1);                                  //set splitNode size
        // move the parents children up one position
        for (int j = parent.getSize(); j >= i; j--) {
            parent.setChild(j + 1, parent.getChild(j));
        }
        // move the newNode to the child of parent
        parent.setChild(i, newChild);                       //set the position of the new child node to one after i

        //go through and move the parent nodes parent values up one position
        for (int j = parent.getSize(); j > i; j--) {
            parent.setParent(j, parent.getParent(j - 1));
        }
        parent.setParent(i, splitNode.getParent(degree - 1));                //set the parent parent value to the new parent (middle of splitchild)
        splitNode.setParent(degree - 1, null);          //remove the parent value of splitchild because it is now in parent
        for (int j = 0; j < splitNode.getSize(); j++) {
            splitNode.setParent(j, splitNode.getParent(degree + j));
            splitNode.setParent(degree + j, null);          //remove the parent value of splitchild because it is now in parent
        }
        parent.setSize(parent.getSize() + 1);                       //increase the size of parent by one
        writeNode(parent);                                          //write to disk
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
                node.incrementParentFrequancy(i - 1);
                writeNode(node);
                return;
            }
            while (i >= 1 && nextSequence <= node.getParentValue(i - 1)) {
                if (nextSequence == node.getParentValue(i - 1)) {
                    node.incrementParentFrequancy(i - 1);
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
            TreeObject temp = new TreeObject(nextSequence, 1);
            node.setParent(i, temp);
            node.setSize(node.getSize() + 1);
            writeNode(node);
        } else {

            while (i >= 1 && nextSequence <= node.getParentValue(i - 1)) {
                if (nextSequence == node.getParentValue(i - 1)) {
                    node.incrementParentFrequancy(i - 1);
                    writeNode(node);
                    return;
                }
                i--;
            }
            BTreeNode child = node.getChild(i);
            if (child == null)
                System.out.print("hi");
            //   BTreeNode Rchild = node.getChild(i+1);
            if (child.isFull()) {
                splitChild(node, (i), child);
                if (nextSequence == node.getParentValue(i)) {
                    node.incrementParentFrequancy(i);
                    writeNode(node);
                    return;
                }
                // compare the values at position i with the new value to find where to insert it
                if (nextSequence > node.getParentValue(i)) {
                    i++;
                    child = node.getChild(i);
                }
            }
            insertNonFull(child, nextSequence);
        }
    }

    public void inOrder(BTreeNode node) {
        if (node.getLeaf()) {
            writeNodeToDump(node);
            return;
        }
        for (int i = 0; i <= node.getSize(); i++) {
            if (i < node.getSize()) {
                if (node.getParentValue(i) > 0) {
                    if (node.getChild(i) != null) {
                        inOrder(node.getChild(i));
                    }
                    writer.print(node.getParentFrequancy(i) + "\t\t" + decoder(node.getParentValue(i)) + "\n");
                    writer.flush();
                }
            } else if (node.getChild(i) != null) {
                inOrder(node.getChild(i));
            } else {
                return;
            }
        }
    }

    private void createDumpFile() throws IOException {
        File dumpFile = new File("dump");
        writer = new PrintWriter(new FileWriter(dumpFile), false);
    }

    private void writeNodeToDump(BTreeNode node) {
        for (int i = 0; i < node.getSize(); i++) {
            if (node.getParentValue(i) > 0) {
                writer.print(node.getParentFrequancy(i) + "\t\t" + decoder(node.getParentValue(i)) + "\n");
            }
        }
        writer.flush();
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
            System.out.println("Generating dump file...");
            createDumpFile();
            inOrder(root);
            System.out.println("A debug file (dump) has been created.");
            writer.close();
        }
    }

    public String decoder(long sequence) {
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

