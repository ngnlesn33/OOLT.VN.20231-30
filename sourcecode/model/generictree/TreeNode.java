package model.generictree;

import java.util.ArrayList;
import java.util.List;

public  class TreeNode<E extends Comparable<E>> {
    protected E element;
    private final List<TreeNode<E>> children;

    public TreeNode(E e) {
        element = e;
        children = new ArrayList<>();
    }

    public E getElement() {
        return element;
    }

    public List<TreeNode<E>> getChildren() {
        return children;
    }

    public void addChild(TreeNode<E> child) {
        children.add(child);
    }

    public void removeChild(TreeNode<E> child) {
        children.remove(child);
    }
}