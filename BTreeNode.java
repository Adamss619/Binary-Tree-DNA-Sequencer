import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * BTreeNode class creates and contains the values and keys of a BTreeNode used in BTree
 *
 * @param <T>
 */

public class BTreeNode<T> {

    private BTreeNode[] child;
    protected long[] children;  //array with the pointer to children keys
    protected long[] keyArray;  //array of the keys of each BTreeNode in the B-Tree
    protected long[] frequency; //array with the frequencys of the B-Tree nodes
    private boolean isRoot;     //boolean if node is the main root node (only one)
    private boolean isLeaf;     //boolean if node is a leaf
    private int key;            //pointer to the values in the node
    private int degree;         //the current element to make the node
    private int size;           //current node size
    private long fileKey;       //the offset value of the the files key
    private long parentKey;     //the offset value of the the root files


    /**
     * constructor of BTreeNode
     *
     * @param object
     */


    public BTreeNode(TreeObject object) {
        this.fileKey = object.getOffSet(); // address of this node in the file.
        keyArray = new long[(2 * degree) - 1];
        children = new long[2 * degree];
        frequency = new long[(2 * degree) - 1];
        isLeaf = false;
        size = 0;
        this.degree = degree;
    }

    public void setParent(long parentOffset) {
        this.parentKey = parentOffset;
    }

    public long getChild(int i) {
        return children[i];
    }

    public boolean hasChildren() {

        for (int i = 0; i < (2 * degree) - 1; i++) {
            if (children[i] != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull() {
        return size == (2 * degree) - 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void setLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getCurrentSize() {
        return this.size;
    }

    public long getFileKey() {
        return this.fileKey;
    }

    public long getParentKey() {
        return this.parentKey;
    }

    public boolean getLeaf() {
        return isLeaf;
    }

    public void setCurrentSize(int i) {
        this.size = i;
    }

    public void generateCurrentSize() {
        boolean noKey = false;
        int size = 0;
        int i = 0;
        while (!noKey && i < ((2 * degree) - 1)) {
            if (keyArray[i] > 0) {
                size++;
                i++;
            } else {
                noKey = true;
            }
        }
        size = size;
    }

    public boolean equals(BTreeNode node) {
        return fileKey == node.getFileKey();
    }

    public boolean equalsKey(long offset) {
        return fileKey == offset;
    }

    public String toString() {

        String retVal = "Node from memory \n";
        retVal += "NodeOffset: " + fileKey + "\n";
        retVal += "Current Size: " + size + "\n";

        if (isLeaf) {
            retVal += "Leaf \n";
        } else {
            retVal += "Not Leaf \n";
        }

        retVal += "Keys in node: \n";
        for (int j = 0; j < (2 * degree) - 1; j++) {
            retVal += "Key: " + keyArray[j] + "\n";
        }
        retVal += "frequency of keys: \n";
        for (int j = 0; j < (2 * degree) - 1; j++) {
            retVal += "Freq: " + frequency[j] + "\n";
        }
        retVal += "Children: \n";
        for (int j = 0; j < (2 * degree); j++) {
            retVal += "Child: " + children[j] + "\n";
        }
        retVal += "Parent Node: \n";
        retVal += parentKey;
        return retVal;

    }

}
