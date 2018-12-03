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

    private void BTree(BTree tree) {
        BTreeNode newNode = new BTreeNode(null);
        newNode.setIsLeaf(true);
        newNode.setSize(0);
        newNode.setIsRoot(true);
        tree.root = newNode;
    }

    private void insert(BTreeNode currentRoot, TreeObject value) {

        BTreeNode rootNode = currentRoot;
        if (currentRoot.size() == degree-1) {
            BTreeNode newNode = new BTreeNode(null);
            currentRoot = newNode;
            newNode.setIsLeaf(false);
            newNode.setIsRoot(true);
            newNode.children.add(0,rootNode);
            splitChild(newNode, 1, rootNode);
            insertNonFull(newNode,value);
        }
        else
            insertNonFull(rootNode,value);

    }

    private void insertNonFull(BTreeNode currentRoot, TreeObject value) {
        int i = currentRoot.size();
        if(currentRoot.isLeaf){
            while(i>=1 && value.getKey()>currentRoot.getKey()){
                currentRoot.keyArray.add(i+1,currentRoot.keyArray.get(i));
                i--;
            }
            currentRoot.keyArray.set(i+1,value.getKey());
            currentRoot.setSize(currentRoot.size()+1);
            //disk write currentRoot
        }
        else{
            while(i>=1 && value.getKey()<currentRoot.getKey()){
                i--;
            }
            i=i+1;
                  //disk read currentRoot.children{i}
            if(currentRoot.children.get(i).size()==degree-1){
                splitChild(currentRoot,i,(BTreeNode)currentRoot.children.get(i));
                if(value.getKey()>i){
                    i=i+1;
                }
            }
            insertNonFull((BTreeNode)currentRoot.children.get(i),value.getKey());
        }
    }

    private void splitChild(BTreeNode nonFullNode, int value, BTreeNode fullNode) {
        BTreeNode newNode = new BTreeNode(null);
        newNode.setIsLeaf(fullNode.isLeaf);
        newNode.setSize(degree-1);
        for(int i=1; i<degree-1; i++){
            newNode.keyArray.add(i,fullNode.keyArray.get(i+degree));
        }
        if(!fullNode.isLeaf){
            for(int i=1; i<degree; i++){
                newNode.children.add(i,fullNode.children.get(i+degree));
            }
        }
        fullNode.setSize(degree-1);
        for(int i=nonFullNode.size()+1; i>value+1; i--){
            nonFullNode.children.add(i+1,nonFullNode.children.get(i));
        }
        nonFullNode.children.add(value+1,newNode);
        for(int i=nonFullNode.size()+1; i>value+1; i--){
            nonFullNode.keyArray.add(i+1,nonFullNode.keyArray.get(i));
        }
        nonFullNode.keyArray.add(value+1,fullNode.keyArray.get(degree));
        nonFullNode.setSize(nonFullNode.size()+1);
        // no idea what diskwrite these nodes mean.... my assumption is store them in the cache.


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

    private boolean contains(BTreeNode current, TreeObject value) {
        if (current == null)
            return false;
        if (value == current.getValue())
            return true;
        if (current.getValue()>value.getValue())
            contains(current.LNode, value);
        else
            contains(current.RNode, value);
    }

    private BTreeNode delete(BTreeNode root, TreeObject value) {
        if (root == null) {
            return null;
        }
        if (value == root.value) {
            if (root.LNode == null && root.RNode == null) {
                return null;
            }

            if (root.RNode == null) {
                return root.LNode;
            }

            if (root.LNode == null) {
                return root.RNode;
            }

        }
        if (value < root.value) {
            return delete(root.LNode, value);
        }
        else
        return delete(root.RNode, value);

    }

    public void itteratorInOrder(BTreeNode root) {
        if (root != null) {
            itteratorInOrder(root.LNode);
            System.out.print(" " + root.value);
            itteratorInOrder(root.RNode);
        }
    }
}

