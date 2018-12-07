

public class TreeObject implements Comparable<TreeObject> {

	private int degree;
	private long data;
	private int frequancy;


	public TreeObject(long data, int degree, int frequancy) {
		this.frequancy = frequancy;
		this.degree = degree;
		this.data = data;
	}

	public void increaseFrequancy() {
		frequancy++;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public int getFrequancy() {
		return frequancy;
	}

	public void setFrequancy(int frequancy) {
		this.frequancy = frequancy;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public int compareTo(TreeObject Tobj) {
		if (data < Tobj.data)
			return -1;
		if (data > Tobj.data)
			return 1;
		else
			return 0;
	}

	public String toString() {
		return "Key: " + data + "Frequency: " + degree;
	}
}