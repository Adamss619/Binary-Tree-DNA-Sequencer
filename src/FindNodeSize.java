
public class FindNodeSize {

    private int degree; // variable t
    private int metadata;
    private int sizeOfChildOffset;
    private int sizeOfParentOffset;
    private int sizeOfObjects;
    private int frequency;
    final int DEFAULT_MAX_SIZE = 4096;
    final int DEFAULT_DATA = 25;
    final int DEFAULT_SIZE = 8; // 8(2t-1)
    final int DEFAULT_PARENT_OFFSET = 8;
    final int DEFAULT_CHILD_OFFSET = 8; // 8(2t)
    final int DEFAULT_FREQUENCY = 8; // 8(2t-1)

    // Default constructor

    /**
     * Default Constructor which sets metadata, size of all objects, frequency, child pointers, and the parent pointer
     * to the default constants.
     */
    public FindNodeSize() {

        this.metadata = DEFAULT_DATA;
        this.sizeOfObjects = DEFAULT_SIZE;
        this.frequency = DEFAULT_FREQUENCY;
        this.sizeOfChildOffset = DEFAULT_CHILD_OFFSET;
        this.sizeOfParentOffset = DEFAULT_PARENT_OFFSET;
    }

    /**
     * This method returns the optimal degree (t) to make the node size < 4096 bytes.
     *
     * @return
     */
    public int CalculateOptimalDegree() {

        // Constant for meta data
        int metadata = this.getMetadata();

        // Calculate coefficient of t along with constant when multiplied by object size

        int x0 = 0; // coefficient of t
        int c0 = 0; // constant

        if (this.getSizeOfObjects() == 0) {
            x0 = 0;
            c0 = 0;
        } else {

            x0 = (2 * this.getSizeOfObjects());
            c0 = (this.getSizeOfObjects());
        }

        // Calculate coefficient of t along with constant for frequency

        int x1 = 0;
        int c1 = 0;

        if (this.frequency == 0) {
            x1 = 0;
            c1 = 0;
        } else {
            x1 = (2 * this.frequency);
            c1 = this.frequency;
        }

        // Calculate coefficient of t along with constant for children and parent pointers

        int x2 = 0;
        int c2 = 0;

        if (this.getSizeOfChildOffset() == 0) {
            x2 = 0;
            c2 = 0;

        } else {
            x2 = (2 * this.getSizeOfChildOffset());
            c2 = this.getSizeOfChildOffset();
        }


        // Final computation of optimal degree
        int coefficientOfT = x0 + x1 + x2;
        int constants = metadata - c0 + c1 + c2;

        int optimalDegreeT = ((DEFAULT_MAX_SIZE - constants) / coefficientOfT);

        return optimalDegreeT;
    }

    // Calculate Node Size with given optimal degree t

    /**
     * Given the parameter t, this method will calculate the total byte size of 1 BTree Node.
     *
     * @param t
     * @return
     */
    public int CalculateSize(int t) {

        int nodeSize = (this.metadata) + ((2 * t) - 1) * (this.sizeOfObjects) + ((2 * t) - 1) * (this.frequency)
                + ((2 * t) * (this.sizeOfChildOffset)) + (sizeOfParentOffset);

        return nodeSize;
    }


    // ******************************************************
    // Getters and Setters for private variables
    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getMetadata() {
        return metadata;
    }

    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

    public int getSizeOfObjects() {
        return sizeOfObjects;
    }

    public void setSizeOfObjects(int sizeOfObjects) {
        this.sizeOfObjects = sizeOfObjects;
    }

    public int getSizeOfChildOffset() {
        return sizeOfChildOffset;
    }

    public void setSizeOfChildOffset(int sizeOfChildOffset) {
        this.sizeOfChildOffset = sizeOfChildOffset;
    }

    public int getSizeOfParentPointers() {
        return sizeOfParentOffset;
    }

    public void setSizeOfParentPointers(int sizeOfParentPointers) {
        this.sizeOfParentOffset = sizeOfParentPointers;
    }

    public int getSizeOfBTreeNodePointer() {
        return frequency;
    }

    public void setSizeOfBTreeNode(int sizeOfBTreeNodePointer) {
        this.frequency = sizeOfBTreeNodePointer;
    }
    // *******************************************************

}
