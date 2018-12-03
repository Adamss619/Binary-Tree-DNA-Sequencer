import java.lang.*;

/**
 * BTree class is the ADT of a B-Tree
 *
 * @param <T>
 */

public class BTree<T> {
    BTreeNode root;          //the main root of the b-tree
    TreeObject object;      //obect to store in the b-tree
    BTreeCache cache;       //cache to store the B-tree
    boolean useCache;       //determin if we use a chace or not (not sure on this one)
    int cacheSize;          //size of the cache
    int degree;        //the number of TreeObjects stored in each node (max is the degree for keys and +1 for children)

    private void create(T tree) {
        object = new TreeObject((Long) tree, 0);
        BTreeNode newNode = new BTreeNode(null);
    }

    private void insert(BTreeNode currentRoot, int value) {

        if (currentRoot.numKeys() == degree) {

        }
    }

    private void insertNonFull(BTreeNode currentRoot, int value) {
        if (currentRoot == null) {
            return new BTreeNode(value);
        }
        if (value < currentRoot.value) {
            currentRoot.LNode = insert(currentRoot.LNode, value);
        } else if (value > currentRoot.value) {
            currentRoot.RNode = insert(currentRoot.RNode, value);
        }

        root = currentRoot;
    }

    private void splitChild(BTreeNode currentRoot, int value) {

    }

    public boolean isEmpty() {
        return root == null;
    }

    private int getSize(BTreeNode root) {
        int size = 0;
        if (root == null)
            return size;
        else
            return size + getSize(root.LNode) + getSize(root.RNode);
    }

    private boolean contains(BTreeNode current, int value) {
        if (current == null)
            return false;
        if (value == current.getValue())
            return true;
        if (value < current.getValue())
            contains(current.LNode, value);
        else
            contains(current.RNode, value);
    }

    private BTreeNode delete(BTreeNode root, int value) {
        if (root == null) {
            return null;
        }
    }

    public void itteratorInOrder(BTreeNode root) {
        if (root != null) {
            itteratorInOrder(root.LNode);
            System.out.print(" " + root.value);
            itteratorInOrder(root.RNode);
        }
    }
}

