package Model;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GenericTree<E extends Comparable<E>> extends AbstractTree<E> {
    protected TreeNode<E> root;
    protected int size = 0;
    // Get root
    public TreeNode<E> getRoot() {
        return root;
    }

    public static class TreeNode<E extends Comparable<E>> {
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

    @Override
    public boolean search(E e) {
        if (root == null) {
            return false;
        }

        Queue<TreeNode<E>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode<E> current = queue.poll();

            if (current.element.compareTo(e) == 0) {
                return true;
            }

            queue.addAll(current.children);
        }
        return false;
    }

    @Override
    public boolean insert(E e) {
        if (root == null) {
            root = new TreeNode<>(e);
        } else {
            root.addChild(new TreeNode<>(e));
        }
        return true;
    }

    // Delete an element from the tree
    @Override
    public boolean delete(E e) {
        if (root == null) {
            return false;
        }

        if (root.element.compareTo(e) == 0) {
            root = null;
            return true;
        }

        return delete(root, e);
    }

    private boolean delete(TreeNode<E> node, E e) {
        for (TreeNode<E> child : node.children) {
            if (child.element.compareTo(e) == 0) {
                node.removeChild(child);
                return true;
            }

            if (delete(child, e)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new GenericTreeIterator();
    }

    private class GenericTreeIterator implements Iterator<E> {
        private final Queue<TreeNode<E>> queue = new LinkedList<>();

        public GenericTreeIterator() {
            if (root != null) {
                queue.add(root);
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public E next() {
            TreeNode<E> current = queue.poll();

            assert current != null;
            if (current.children != null) {
                queue.addAll(current.children);
            }

            return current.element;
        }
    }
}
