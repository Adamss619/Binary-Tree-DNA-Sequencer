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
    private int degree;         //the current element to make the node
    private int size;           //current node size
    private long key;       //the offset value of the the files key
    private long parentKey;     //the offset value of the the root files


    /**
     * constructor of BTreeNode
     *
     * @param object
     */

    /**
     * @param object
     */
    public BTreeNode(TreeObject object) {
        this.key = object.getKey(); // address of this node in the file.
        this.degree = object.getDegree();
        keyArray = new long[((2 * degree) - 1)];
        children = new long[2 * degree];
        frequency = new long[(2 * degree) - 1];
        isLeaf = false;
        size = 0;
    }

    /**
     *get the value of the children at the degree passed in
     * @param i
     * @return
     */
    public long getChild(int i) {
        return children[i];
    }

    /**
     *checks if the current node has any children
     * @return
     */
    public boolean hasChildren() {

        for (int i = 0; i < (2 * degree) - 1; i++) {
            if (children[i] != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     *checks if the B-TreeNode is full
     * @return
     */
    public boolean isFull() {
        return size == (2 * degree) - 1;
    }

    /**
     *checks if the B-TreeNode is empty
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *checks if the B-TreeNode is a leaf
     * @param isLeaf
     */
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     *returns the nodes size
     * @return
     */
    public int getCurrentSize() {
        return this.size;
    }

    /**
     *returns if the node is a leaf node or not
     * @return
     */
    public boolean getLeaf() {
        return isLeaf;
    }

    /**
     *set the size of the node
     * @param i
     */
    public void setCurrentSize(int i) {
        this.size = i;
    }

    /**
     *checks the current size then returns it
     */
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

    /**
     *compares the node with the current node
     * @param node
     * @return
     */
    public boolean equals(BTreeNode node) {
        return key == node.key;
    }

    /**
     *compares the offset of the
     * @param offset
     * @return
     */
    public boolean equalsKey(long offset) {
        return key == offset;
    }

    /**
     *
     * @return
     */
    public String toString() {

        String retVal = "Node from memory \n";
        retVal += "NodeOffset: " + key + "\n";
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
