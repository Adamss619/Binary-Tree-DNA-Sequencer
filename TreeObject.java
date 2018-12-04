public class TreeObject {

	private long value;
	private int key;
	private int frequency;
	
	// constructor 
	public TreeObject(long value, int key) {
		setValue(value);
        setFrequency(1);
		setKey(key);
	}
    
    //constructor that can set frequency
    public TreeObject(long value, int key, int freq) {
		setValue(value);
        setFrequency(freq);
		setKey(key);
	}

	/**
	 * Sets value
	 * @param val Value to be set
	 */
	public void setValue(long val) {
		this.value = val;
	}
	
	/**
	 * Sets key
	 * @param k key to be set
	 */
	public void setKey(int k) {
		
		this.key = k;
	}
	
	/**
	 * Get the value 
	 * @return Returns the value of an object
	 */
	public long getValue() {
		return this.value;
	}
	
	/**
	 * Get the key 
	 * @return Returns the key of an object
	 */
	public long getKey() {
		return this.key;
	}
	
	/**
	 * Get the frequency of an object
	 * @return Returns the frequency
	 */
	public int getFrequencty() {
		return this.frequency;
	}
    
    	public int setFrequencty(int f) {
		this.frequency = f;
	}
}
