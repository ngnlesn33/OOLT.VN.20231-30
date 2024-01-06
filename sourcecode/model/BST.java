package model;

import java.util.LinkedList;
import java.util.Queue;

public class BST<E extends Comparable<E>> extends AbstractTree<E> {
    protected TreeNode<E> root;
    protected int size = 0;

    /**
     * This inner class is static, because it does not access any instance members defined in its
     * outer class
     */
    public static class TreeNode<E extends Comparable<E>> {
        protected E element;
        protected TreeNode<E> left;
        protected TreeNode<E> right;

        TreeNode(E e) {
            element = e;
        }

        public E getElement() {
            return element;
        }

        public TreeNode<E> getLeft() {
            return left;
        }

        public TreeNode<E> getRight() {
            return right;
        }
    }

    /**
     * Create a default binary tree
     */
    public BST() {}

    @Override
    public boolean search(E e) {
        TreeNode<E> current = root; // Start from the root
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                current = current.right;
            } else // element matches current.element
                return true; // Element is found
        }
        return false;
    }

    public TreeNode<E> searchNode(E element) {
        TreeNode<E> current = root; // Start from the root

        while (current != null) {
            if (element.compareTo(current.element) < 0) {
                current = current.left; // Go left
            } else if (element.compareTo(current.element) > 0) {
                current = current.right; // Go right
            } else { // element matches current.element
                return current; // Element is found
            }
        }

        return null; // Element is not in the tree
    }

    protected TreeNode<E> createNewNode(E e) {
        return new TreeNode<>(e);
    }

    @Override
    public boolean insert(E e) {
        if (root == null)
            root = createNewNode(e); // Create a new root
        else {
            // Locate the parent node
            TreeNode<E> parent = null;
            TreeNode<E> current = root;
            while (current != null)
                if (e.compareTo(current.element) < 0) {
                    parent = current;
                    current = current.left;
                } else if (e.compareTo(current.element) > 0) {
                    parent = current;
                    current = current.right;
                } else
                    return false; // Duplicate node not inserted
            // Create the new node and attach it to the parent node
            if (e.compareTo(parent.element) < 0)
                parent.left = createNewNode(e);
            else
                parent.right = createNewNode(e);
        }
        size++;
        return true; // Element inserted successfully
    }

    @Override
    public boolean delete(E e) {
        // Locate the node to be deleted and also locate its parent node
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                parent = current;
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                parent = current;
                current = current.right;
            } else
                break; // Element is in the tree pointed at by current
        }
        if (current == null)
            return false; // Element is not in the tree
        // Case 1: current has no left child
        if (current.left == null) {
            // Connect the parent with the right child of the current node
            if (parent == null) {
                root = current.right;
            } else {
                if (e.compareTo(parent.element) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;
            }
        } else {
            // Case 2: The current node has a left child
            // Locate the rightmost node in the left subtree of
            // the current node and also its parent
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;
            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; // Keep going to the right
            }
            // Replace the element in current by the element in rightMost
            current.element = rightMost.element;
            // Eliminate rightmost node
            if (parentOfRightMost.right == rightMost)
                parentOfRightMost.right = rightMost.left;
            else
                // Special case: parentOfRightMost == current
                parentOfRightMost.left = rightMost.left;
        }
        size--;
        return true; // Element deleted successfully
    }

    @Override
    /* Get the number of nodes in the tree */
    public int getSize() {
        return size;
    }

    /**
     * Returns the root of the tree
     */
    public TreeNode<E> getRoot() {
        return root;
    }

    public java.util.Iterator<E> iterator() {
        return new BFSIterator();
    }

    private class BFSIterator implements java.util.Iterator<E> {
        private final Queue<TreeNode<E>> queue = new LinkedList<>();

        public BFSIterator() {
            if (root != null) {
                queue.offer(root);
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
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
            return current.element;
        }
    }

}

