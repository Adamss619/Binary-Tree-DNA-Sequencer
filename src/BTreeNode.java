
/**
 *
 */
public class BTreeNode<T> {
    private final BTreeNode[] child;
    private final BTreeNode[] parent;
    protected long[] keyArray;
    protected long[] children;
    protected long[] frequency;
    private long currentOffset;
    private long parentOffset;
    private boolean isLeaf;
    private int size;
    private int degree;

    /**
     * BTreeNode constructor
     *
     * @param degree
     * @param offset
     */
    public BTreeNode(int degree, long offset) {
        this.currentOffset = offset;
        keyArray = new long[(2 * degree) - 1];
        children = new long[2 * degree];
        frequency = new long[(2 * degree) - 1];
        isLeaf = false;
        size = 0;
        child = new BTreeNode[2 * degree];
        parent = new BTreeNode[(2 * degree) - 1];
        this.degree = degree;

    }


    public void setLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getSize() {
        return this.size;
    }

    public long getOffset() {
        return this.currentOffset;
    }

    public long getParentOffset() {
        return this.parentOffset;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public void generateCurrentSize() {
        Boolean noKey = false;
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
        this.size = size;
    }

    public int getChildLength() {

        return 0;
    }

    public void setParent(long parentOffset) {
        this.parentOffset = parentOffset;
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

    public boolean equals(BTreeNode node) {
        return currentOffset == node.getOffset();
    }

    public boolean equalsOffset(long offset) {
        return currentOffset == offset;
    }

    public String toString() {

        String retVal = "Node from memory \n";
        retVal += "NodeOffset: " + currentOffset + "\n";
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
        retVal += parentOffset;
        return retVal;

    }

}
