import java.util.ArrayList;

/**
 * BTreeNode class creates and contains the values and keys of a BTreeNode used in BTree
 *
 * @param <T>
 */
class BTreeNode<T> {

    private ArrayList<T> parents;
    private ArrayList<Integer> children;
    private int parentKey;
    private int keyCount;
    private int offset;
    private boolean isLeaf;


    public BTreeNode(TreeObject object) {
        parents = new ArrayList<T>();
        children = new ArrayList<Integer>();
        setKeyCount(0);
        setParentKey(-1);
    }

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int numKeys) {
        this.keyCount = numKeys;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getParentKey() {
        return parentKey;
    }

    public void setParentKey(int parentKey) {
        this.parentKey = parentKey;
    }

    public T getParents(int keyValue) {
        T object = parents.get(keyValue);
        return object;
    }

    public void addParent(T dataObject) {
        parents.add(dataObject);
    }

    public void addParent(T dataObject, int keyValue) {
        parents.add(keyValue, dataObject);
    }

    public T removeParent(int keyValue) {
        return parents.remove(keyValue);
    }

    public ArrayList<T> getParents() {
        return parents;
    }

    public int getChild(int childKey) {
        return children.get(childKey).intValue();
    }

    public void addChild(int childKey) {
        children.add(childKey);
    }

    public void addChild(Integer location, int childKey) {
        children.add(childKey, location);
    }

    public int removeChild(int childKey) {
        return children.remove(childKey);
    }

    public ArrayList<Integer> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String toString() {
        String s = new String();
        s += "Keys: ";
        for (int i = 0; i < parents.size(); i++) {
            s += (parents.get(i) + " ");
        }
        s += "\nchildren: ";
        for (int i = 0; i < children.size(); i++) {
            s += (children.get(i) + " ");
        }
        return s;
    }
}