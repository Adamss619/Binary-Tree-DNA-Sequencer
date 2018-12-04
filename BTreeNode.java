import java.util.ArrayList;

/**
 * BTreeNode class creates and contains the values and keys of a BTreeNode used in BTree
 *
 * @param <T>
 */

public class BTreeNode<T> {

    int degree;          //the current element to make the node
    ArrayList<BTreeNode> children;  //pointer to children keys
    ArrayList<BTreeNode> keyArray;   //array of the keys of each BTreeNode in the B-Tree
    //  BTreeNode[] children;
    // BTreeNode[] keyArray;
    BTreeNode LNode;        //left node
    BTreeNode RNode;        //right node
    boolean isRoot;     //boolean if node is the main root node (only one)
    boolean isLeaf;     //boolean if node is a leaf
    int key;            //pointer to the values in the node
    int size;


    /**
     * constructor of BTreeNode
     *
     * @param degree
     */
    public BTreeNode(int degree) {
        this.degree = degree;
        children = new ArrayList<BTreeNode>(degree + 1);
        keyArray = new ArrayList<BTreeNode>(degree);
        // children=new BTreeNode[degree+1];
        // keyArray=new BTreeNode[degree];
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

    /*  public T getValue() {
          return value;
      }

      public void setValue(T value) {
          this.value = value;
      }
  */
    public ArrayList<BTreeNode> getKeys() {
        return this.keyArray;
    }

    public void setKeys(ArrayList<BTreeNode> arrayList) {
        this.keyArray = arrayList;
    }

    public ArrayList<BTreeNode> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<BTreeNode> arrayList) {
        this.children = arrayList;
    }

    public int getDegree() {
        return this.degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public BTreeNode getRightNode() {
        return children.get(key + 1);
    }

    public BTreeNode getLeftNode() {
        return children.get(key);
    }

    /*
        public BTreeNode getLeftNode() {
        return children[key];
    }

        public void setLeftNode(BTreeNode LNode) {
            children[key]= LNode;
        }

        public BTreeNode getRightNode() {
            return children[key + 1];
        }

        public void setRightNode(BTreeNode RNode) {
            children[key+1]= RNode;
        }
        */
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
    
    public boolean hasChild(){
        return (children.size > 0);
    }

}
