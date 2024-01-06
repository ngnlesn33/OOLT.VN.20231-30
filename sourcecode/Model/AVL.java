package Model;

import java.util.LinkedList;
import java.util.Queue;

public class AVL<E extends Comparable<E>> extends AbstractTree<E> {
    public AVLNode<E> root;
    public int size = 0;

    public AVL() { }
    public static class AVLNode<E extends Comparable<E>> {
        E element;
        AVLNode<E> left;
        AVLNode<E> right;
        int height;

        public AVLNode(E e) {
            element = e;
            left = null;
            right = null;
            height = 1;
        }

        public E getElement() {
            return element;
        }

        public AVLNode<E> getLeft() {
            return left;
        }

        public AVLNode<E> getRight() {
            return right;
        }

        public int getHeight() {
            return height;
        }
    }


    @Override
    public boolean search(E e) {
        AVLNode<E> current = root; // Start from the root
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

    public AVLNode<E> searchNode(E element) {
        AVLNode<E> current = root; // Start from the root

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

    @Override
    public boolean insert(E e) {
        if (root == null) {
            root = new AVLNode<>(e);
        } else {
            root = insert(e, root);
        }
        size++;
        return true;
    }


    public AVLNode<E> insert(E e, AVLNode<E> node) {
        if (node == null) {
            return new AVLNode<>(e);
        }

        int compareResult = e.compareTo(node.element);

        if (compareResult < 0) {
            node.left = insert(e, node.left);
        } else if (compareResult > 0) {
            node.right = insert(e, node.right);
        } else {
            return node;
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    public int height(AVLNode<E> node) {
        return (node == null) ? 0 : node.height;
    }

    public AVLNode<E> balance(AVLNode<E> node) {
        if (node == null) {
            return null;
        }
        int balanceFactor = getBalanceFactor(node);
        // Left heavy situation
        if (balanceFactor > 1) {
            // Left-Right case
            if (getBalanceFactor(node.left) < 0) {
                node.left = leftRotate(node.left);
            }
            // Left-Left case
            node = rightRotate(node);
        }
        // Right heavy situation
        else if (balanceFactor < -1) {
            // Right-Left case
            if (getBalanceFactor(node.right) > 0) {
                node.right = rightRotate(node.right);
            }
            // Right-Right case
            node = leftRotate(node);
        }

        // Return the balanced node
        return node;
    }

    public int getBalanceFactor(AVLNode<E> node) {
        if (node == null) {
            return 0;
        } else {
            return height(node.left) - height(node.right);
        }
    }


    public AVLNode<E> rightRotate(AVLNode<E> y) {
        AVLNode<E> x = y.left;
        AVLNode<E> T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    public AVLNode<E> leftRotate(AVLNode<E> x) {
        AVLNode<E> y = x.right;
        AVLNode<E> T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }


    @Override
    public boolean delete(E e) {
        if (root == null) {
            return false;
        }

        // A wrapper for boolean to check if deletion is successful
        BooleanWrapper deleted = new BooleanWrapper(false);
        root = delete(root, e, deleted);
        if (deleted.value) {
            size--; // Decrease size if deletion was successful
        }
        return deleted.value;
    }

    public AVLNode<E> delete(AVLNode<E> node, E e, BooleanWrapper deleted) {
        if (node == null) {
            return null;
        }

        int compareResult = e.compareTo(node.element);

        if (compareResult < 0) {
            // Go left if the element to delete is smaller than the current node's element
            node.left = delete(node.left, e, deleted);
        } else if (compareResult > 0) {
            // Go right if the element to delete is larger than the current node's element
            node.right = delete(node.right, e, deleted);
        } else {
            // Node to delete found
            deleted.value = true;

            // Node with only one child or no child
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                // Node with two children: Get the in-order successor
                AVLNode<E> successor = minValueNode(node.right);
                node.element = successor.element;
                node.right = delete(node.right, successor.element, deleted);
            }
        }

        if (node == null) {
            return null;
        }

        // Update height and balance the tree
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    public AVLNode<E> minValueNode(AVLNode<E> node) {
        AVLNode<E> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Auxiliary class to hold boolean as an object
    public static class BooleanWrapper {
        boolean value;
        BooleanWrapper(boolean value) {
            this.value = value;
        }
    }
    

    @Override
    public int getSize() {
        return size;
    }
    public AVLNode<E> getRoot() {
        return root;
    }

    public java.util.Iterator<E> iterator() {
        return new AVL.AVLIterator();
    }

    private class AVLIterator implements java.util.Iterator<E> {
        private final Queue<AVL.AVLNode<E>> queue = new LinkedList<>();

        public AVLIterator() {
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
            AVL.AVLNode<E> current = queue.poll();
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

