

import java.util.LinkedList;
import java.util.ListIterator;

public class Cache {
	private LinkedList<BTreeNode> cache;
	private int capacity;

	/**
	 * Constructs cache
	 * 
	 * @param capacity
	 */
	public Cache(int capacity) {
		cache = new LinkedList<BTreeNode>();
		this.capacity = capacity;

	}

	/**
	 * Returns capacity
	 * 
	 * @return capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Moves data to front of cache
	 * 
	 * @param o
	 */
	public void moveToFront(BTreeNode node) {
		cache.remove(node);
		cache.addFirst(node);
	}

	/**
	 * Adds data to front of cache
	 * 
	 * @param o
	 */
	public void addToFront(BTreeNode node) {
		cache.addFirst(node);

		if (cache.size() > this.capacity) {
			removeLast();
		}
	}

	/**
	 * Searches for node with matching fileOffset. Returns found node, or null
	 * if not in list. Updates found node and moves it to front of cache.
	 * 
	 * @param node
	 * @return node or null.
	 */
	public BTreeNode returnNode(BTreeNode node) {
		boolean found = false;
		BTreeNode tempNode;
		BTreeNode foundNode = null;
		ListIterator<BTreeNode> i = cache.listIterator();
		while (i.hasNext() && !found) {
			tempNode = i.next();
			if (tempNode.equals(node)) {
				found = true;
				foundNode = tempNode;
				cache.remove(tempNode);
				cache.addFirst(node);
			}
		}
		return foundNode;
	}
	
	public BTreeNode returnNodeFromOffset(long offset){
		boolean found = false;
		BTreeNode tempNode;
		BTreeNode foundNode = null;
		ListIterator<BTreeNode> i = cache.listIterator();
		while (i.hasNext() && !found) {
			tempNode = i.next();
			if (tempNode.equalsOffset(offset)) {
				found = true;
				foundNode = tempNode;
				moveToFront(tempNode);
			}
		}
		return foundNode;
	}

	public int getSize() {
		return cache.size();
	}

	/**
	 * Removes last element if cache is at capacity
	 */
	private void removeLast() {
		cache.removeLast();
	}
	
	public BTreeNode removeFirst(){
		return cache.removeFirst();
	}

	public BTreeNode getLast() {
		return cache.getLast();
	}
}