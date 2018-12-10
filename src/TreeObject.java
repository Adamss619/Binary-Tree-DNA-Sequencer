
public class TreeObject {

    //   private int degree;
    private long data;
    private int frequancy;



    public TreeObject(long data, int frequancy) {
        this.data = data;
        // this.degree = degree;
        this.frequancy = frequancy;
    }

    public void increaseFrequancy() {
        this.frequancy = this.frequancy + 1;
    }

    /*   public int getDegree() {
           return degree;
       }

       public void setDegree(int degree) {
           this.degree = degree;
       }
   */
    public int getFrequancy() {
        return this.frequancy;
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
        return "Key: " + data + "Frequency: " + frequancy;
    }
}