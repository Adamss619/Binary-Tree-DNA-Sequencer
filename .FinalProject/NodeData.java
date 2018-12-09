public class NodeData {

	final int MAX_NODE_SIZE = 4096;
	final int METADATA = 25;
	final int OBJECT_SIZE = 8; 
	
	final int PARENT_POINTER = 8;
	final int CHILDEREN_POINTERS = 8; 
	final int FREQUENCY = 8;
	
	private int degree;
	private int metadata;
	private int sizeOfChildPointers;
	
	private int sizeOfParentPointer;
	private int sizeOfObjects;
	private int frequency;


	/**
	 * Sets all values to defaulted constants
	 * 
	 */
	public NodeData() {

		this.metadata = METADATA;
		this.sizeOfObjects = OBJECT_SIZE;
		this.frequency = FREQUENCY;
		
		this.sizeOfChildPointers = CHILDEREN_POINTERS;
		this.sizeOfParentPointer = PARENT_POINTER;
	}

	/**
	 * This method returns the optimal degree
	 * @return
	 */
	public int CalculateBestDegree() {
		
		int metadata = this.getMetadata();

		
		int x0 = 0; 
		int c0 = 0; 

		if (this.getSizeOfObjects() == 0) {
			x0 = 0;
			c0 = 0;
		} else {

			x0 = (2 * this.getSizeOfObjects());
			c0 = (this.getSizeOfObjects());
		}

		
		int x1 = 0;
		int c1 = 0;
		

		if (this.frequency == 0) {
			x1 = 0;
			c1 = 0;
		} else {
			x1 = (2 * this.frequency);
			c1 = this.frequency;
		}
		
		
		int x2 = 0;
		int c2 = 0;
		
		if (this.getSizeOfChildPointers() == 0) {
			x2 = 0;
			c2 = 0;
			
		} else {
			x2 = (2 * this.getSizeOfChildPointers());
			
			c2 = this.getSizeOfChildPointers();
		}
		

		int coefOfT = x0 + x1 + x2;
		int con = metadata - c0 + c1 + c2;

		int optimalDegreeT = ((MAX_NODE_SIZE - con) / coefOfT);

		return optimalDegreeT;
	}

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

	public int getSizeOfChildPointers() {
		return sizeOfChildPointers;
	}

	public void setSizeOfChildPointers(int sizeOfChildPointers) {
		this.sizeOfChildPointers = sizeOfChildPointers;
	}

	public int getSizeOfParentPointers() {
		return sizeOfParentPointer;
	}

	public void setSizeOfParentPointers(int sizeOfParentPointers) {
		this.sizeOfParentPointer = sizeOfParentPointers;
	}

	public int getSizeOfBTreeNodePointer() {
		return frequency;
	}

	public void setSizeOfBTreeNode(int sizeOfBTreeNodePointer) {
		this.frequency = sizeOfBTreeNodePointer;
	}
	
	/**
	 * calculate byte size of each node
	 * @param t
	 * @return
	 */
	public int CalculateSize(int t) {

		int nodeSize = (this.metadata) + ((2 * t) - 1) * (this.sizeOfObjects) + ((2 * t) - 1) * (this.frequency)
				
				+ ((2 * t) * (this.sizeOfChildPointers)) + (sizeOfParentPointer);

		return nodeSize;
	}

}
