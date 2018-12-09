
/**
 *
 */
public class BTreeNode<T> {
    private final BTreeNode[] child;
    private final TreeObject[] parent;
    private int degree;
    BTreeNode<T> parentNode;
    // protected long[] keyArray;
    // protected long[] children;
    // protected long[] frequency;
    //   private long currentOffset;
    private long parentOffset;
    private boolean isLeaf;
    private int size;
    // private int degree;
    //   private TreeObject object;

    /**
     * BTreeNode constructor
     *
     * @param degree
     * //@param offset
     */
    public BTreeNode(int degree) {
        // object = new TreeObject(offset, degree, 1);
        parent = new TreeObject[(2 * degree) - 1];
        child = new BTreeNode[2 * degree];
        this.degree = degree;
        this.parentNode = null;
        //  parent[0] = object;
        //this.currentOffset = offset;
        // keyArray = new long[(2 * degree) - 1];
        // children = new long[2 * degree];
        // frequency = new long[(2 * degree) - 1];
        isLeaf = false;
        size = 0;
        //child = new BTreeNode[2 * degree];
        //parent = new BTreeNode[(2 * degree) - 1];
        //this.degree = degree;

    }

    public void setParentNode(BTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public BTreeNode getParentNode() {
        return parentNode;
    }

    public void setLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getSize() {
        return this.size;
    }

    //   public long getOffset() {
    //       return object.getData();
    //   }

    //   public int getFrequancy() {
    //       return object.getFrequancy();
    //   }

    public int getParentFrequancy(int i) {
        return parent[i].getFrequancy();
    }

    /*  public int getChildFrequancy(int i) {
          return child[i]..getFrequancy();
      }
  */
    public void setFrequancy(int i) {
        parent[i].increaseFrequancy();
    }

    public void setParentFrequancy(int i, int freq) {
        parent[i].setFrequancy(freq);
    }

    public void setChildFrequancy(int i, int freq) {
        child[i].setFrequancy(freq);
    }

    /*   public long getParentOffset() {
           return parentOffset;
       }
   */
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
            if (parent[i].getData() > 0) {
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

    public void setParent(int i, TreeObject parentNode) {
        parent[i] = parentNode;
    }

    public void setChild(int i, BTreeNode childOffset) {
        child[i] = childOffset;
    }

    public BTreeNode getChild(int i) {
        return child[i];
    }

    public TreeObject getParent(int i) {
        return parent[i];
    }

    public long getParentValue(int i) {
        return parent[i].getData();
    }

    public boolean isFull() {
        return size == (2 * degree) - 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // public boolean equals(BTreeNode node) {
    //     return object.getData() == node.getOffset();
    // }

    //  public boolean equalsOffset(long offset) {
    //     return object.getData() == offset;
    // }

  /*  public String toString() {

        String retVal = "Node from memory \n";
        retVal += "NodeOffset: " + object.getData() + "\n";
        retVal += "Current Size: " + size + "\n";

        if (isLeaf) {
            retVal += "Leaf \n";
        } else {
            retVal += "Not Leaf \n";
        }

        retVal += "Keys in node: \n";
        for (int j = 0; j < (2 * object.getDegree()) - 1; j++) {
            retVal += "Key: " + parent[j].getData() + "\n";
        }
        retVal += "frequency of keys: \n";
        for (int j = 0; j < (2 * object.getDegree()) - 1; j++) {
            retVal += "Freq: " + parent[j].getFrequancy() + "\n";
        }
        retVal += "Children: \n";
        for (int j = 0; j < (2 * object.getDegree()); j++) {
            retVal += "Child: " + child[j].getData() + "\n";
        }
        retVal += "Parent Node: \n";
        retVal += parentOffset;
        return retVal;

    }*/

}
