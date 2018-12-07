

public class TreeObject implements Comparable<TreeObject> {

	private int degree;
	private long data;

	public TreeObject(long d, int degree) {
		this.degree = degree;
		data = d;
	}

	public TreeObject(long d) {
		degree = 1;
		data = d;
	}

	public void increaseDegree() {
		degree++;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
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