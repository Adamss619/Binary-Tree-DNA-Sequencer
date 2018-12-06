public class TreeObject {

	private long key;
	private int degree;

	// constructor 
	public TreeObject(long key, int degree) {
		setKey(key);
		setDegree(degree);
	}

	/**
	 * Sets key
	 * @param key Value to be set
	 */
	public void setKey(long key) {
		this.key = key;
	}
	
	/**
	 * Sets degree
	 * @param degree offSet to be set
	 */
	public void setDegree(int degree) {

		this.degree = degree;
	}
	
	/**
	 * Get the key
	 * @return Returns the key of an object
	 */
	public long getKey() {
		return this.key;
	}
	
	/**
	 * Get the degree
	 * @return Returns the degree of an object
	 */
	public int getDegree() {
		return this.degree;
	}

}
