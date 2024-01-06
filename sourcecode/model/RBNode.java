package model;
public class RBNode<E extends Comparable<E>> {
    protected E data;
    public String color; // Can be "R", "B", or "DB"
    public RBNode<E> left;
    public RBNode<E> right;
    protected RBNode<E> parent;

    public RBNode(E data, String color) {
        this.data = data;
        this.color = color;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String toString() {
        if (data == null) {
            return "NIL (" + color + ")";
        }
        return data + " (" + color + ")";
    }
}