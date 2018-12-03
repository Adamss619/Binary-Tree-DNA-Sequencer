import java.util.ArrayList;

/**
 * BTreeNode class creates and contains the values and keys of a BTreeNode used in BTree
 *
 * @param <T>
 */

public class BTreeNode<T> {

    T value;          //the current element to make the node
    ArrayList<BTreeNode> children;  //pointer to children keys
    ArrayList<BTreeNode> keyArray;   //array of the keys of each BTreeNode in the B-Tree
    BTreeNode LNode;        //left node
    BTreeNode RNode;        //right node
    boolean isRoot;     //boolean if node is the main root node (only one)
    boolean isLeaf;     //boolean if node is a leaf
    int key;            //pointer to the values in the node
    int size;


    /**
     * constructor of BTreeNode
     *
     * @param value
     */
    public BTreeNode(T value) {
        children = new ArrayList<BTreeNode>();
        keyArray = new ArrayList<BTreeNode>();
        this.value = value;
        this.key = 0;
        isLeaf = true;
        isRoot = true;
        size=0;

    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public BTreeNode getLeftNode() {
        return children.get(key);
    }

    public void setLeftNode(BTreeNode LNode) {
        children.set(key, LNode);
    }

    public BTreeNode getRightNode() {
        return children.get(key + 1);
    }

    public void setRightNode(BTreeNode RNode) {
        children.set(key + 1, RNode);
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int size() {
        return size;
    }
    public void setSize(int size) {
        this.size=size;
    }

}
