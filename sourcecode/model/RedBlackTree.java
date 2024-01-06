package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<E extends Comparable<E>> {
    protected RBNode<E> root;
    protected final RBNode<E> NIL = new RBNode<>(null, "B");

    public RedBlackTree() {
    }

    public RBNode<E> getRoot() {
        return root;
    }

    public void insert(E key) {
        RBNode<E> child = new RBNode<>(key, "R");
        child.left = NIL; // Default left child is set to NIL
        child.right = NIL; // Default right child is set to NIL
        if (isEmpty()) {
            root = child;
        } else {
            try {
                RBNode<E> parent = insertionPoint(key);
                if (key.compareTo(parent.getData()) < 0) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
                child.parent = parent;
            } catch (DuplicateItemException e) {
                throw new DuplicateItemException();
            }
        }
        insertionCleanup(child); // Update tree to have the properties of a Red-Black Tree
    }

    private RBNode<E> insertionPoint(E key) {
        RBNode<E> parent = root;
        RBNode<E> current = root;
        while (current != NIL) { // Stop traversing the tree when you get to NIL
            if (key.equals(current.getData())) {
                throw new DuplicateItemException();
            } else if (key.compareTo(current.getData()) < 0) {
                parent = current;
                current = current.left;
            } else if (key.compareTo(current.getData()) > 0) {
                parent = current;
                current = current.right;
            }
        }
        return parent;
    }

    private void insertionCleanup(RBNode<E> node) {

        // Case 1: The root is red
        if (root.color.equals("R")) {
            root.color = "B"; // Base case
        }

        // Case 2: The parent is black
        else if (node.parent.color.equals("B")) {
        } // Do nothing

        else {
            // Since cases 1 & 2 are skipped, the node is
            // guaranteed to have a parent, uncle, & grandparent
            RBNode<E> parent = node.parent;
            RBNode<E> uncle = uncle(node);
            RBNode<E> grandparent = grandparent(node);
            // Case 3: The parent and uncle are red
            if (isRed(parent) && isRed(uncle)) {
                parent.color = "B";            // Change parent to black
                uncle.color = "B";                // Change uncle to black
                grandparent.color = "R";        // Change grandparent to red
                insertionCleanup(grandparent);    // Recursively check grandparent for any violations
            }

            // Case 4: The parent is red and the uncle is black
            else if (parent.color.equals("R") && uncle.color.equals("B")) {

                // Case 4a: The node is a right child & the parent is a left child
                if (isRightChild(node) && isLeftChild(parent)) {
                    leftRotate(parent);

                    // Swap the node and the parent. Go to case 5.
                    RBNode<E> temp = node;
                    node = parent;
                    parent = temp;
                }

                // Case 4b: The node is a left child & the parent is a right child
                else if (isLeftChild(node) && isRightChild(parent)) {
                    rightRotate(parent);

                    // Swap the node and the parent. Go to case 5.
                    RBNode<E> temp = node;
                    node = parent;
                    parent = temp;
                }

                // Case 5a: The node and parent are left children
                if (isLeftChild(node) && isLeftChild(parent)) {
                    parent.color = "B";            // Change parent to black
                    grandparent.color = "R";    // Change grandparent to red
                    rightRotate(grandparent);    // Right rotate the grandparent
                }

                // Case 5b: The node and parent are right children
                else if (isRightChild(node) && isRightChild(parent)) {
                    parent.color = "B";            // Change the parent to black
                    grandparent.color = "R";    // Change the grandparent to red
                    leftRotate(grandparent);    // Left rotate the grandparent
                }
            }
        }
    }

    public void delete(E key) {
        if (isEmpty()) {
            throw new EmptyTreeException();
        }
        try {
            RBNode<E> node = nodeToDelete(key);
            RBNode<E> parent = node.parent;
            if (isLeaf(node)) { // Case 1: RBNode is a leaf
                if (node.equals(root)) { // Root is the only node in the tree
                    root = null;
                } else {
                    if (isLeftChild(node)) {
                        parent.left = NIL;
                    } else if (isRightChild(node)) {
                        parent.right = NIL;
                    }
                    NIL.parent = parent; // This step is necessary for the fixDoubleBlack method
                    if (node.color.equals("B")) {
                        NIL.color = "DB";
                        fixDoubleBlack(NIL);
                    }
                }
            } else if (numChildren(node) == 1) { // Case 2: RBNode has one child
                RBNode<E> child;
                if (node.left == NIL) {
                    child = node.right;
                } else {
                    child = node.left;
                }
                if (node.equals(root)) {
                    root = child;
                } else if (isLeftChild(node)) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
                child.parent = parent; // Update parent reference
                if (child.color.equals("R") || node.color.equals("R")) { // Note that both node & child cannot be red
                    child.color = "B";
                } else if (child.color.equals("B") && node.color.equals("B")) {
                    child.color = "DB";
                    fixDoubleBlack(child);
                }
            } else if (numChildren(node) == 2) { // Case 3: RBNode has two children
                if (node.equals(root) && node.left == NIL) {
                    root = node.right;
                    root.color = "B";
                } else {
                    E max = maxLeftSubtree(node);
                    delete(max); // Recursion
                    node.setData(max);
                }
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("The item cannot be found in the tree.");
        }
    }

    private RBNode<E> nodeToDelete(E key) {
        RBNode<E> current = root;
        while (current != NIL) {
            if (key.equals(current.getData())) {
                return current;
            } else if (key.compareTo(current.getData()) < 0) {
                current = current.left;
            } else if (key.compareTo(current.getData()) > 0) {
                current = current.right;
            }
        }
        throw new NullPointerException(); // Handle this exception in the delete method
    }

    private void fixDoubleBlack(RBNode<E> node) {

        // Case 1: The root is double black
        if (node.equals(root)) {
            node.color = "B"; // Base case
        } else {
            RBNode<E> sibling = sibling(node);
            RBNode<E> parent = node.parent;

            // Case 2: The sibling is red
            if (isRed(sibling)) {

                // Case 2a: The node is a right child
                if (isRightChild(node)) {
                    sibling.color = "B";    // Change sibling to black
                    parent.color = "R";    // Change parent to red
                    rightRotate(parent);    // Right rotate the parent
                }

                // Case 2b: The node is a left child
                else if (isLeftChild(node)) {
                    sibling.color = "B";    // Change sibling to black
                    parent.color = "R";    // Change parent to red
                    leftRotate(parent);    // Left rotate the parent
                }
                fixDoubleBlack(node); // Recursion
            }

            // Case 3: The sibling has at least one red child
            else if (hasRedChild(sibling)) {
                RBNode<E> RC; // Red child

                // Case 3a: The sibling is a left child
                if (isLeftChild(sibling)) {

                    // Case 3a.1: The right child of the sibling is red
                    if (isRed(sibling.right)) {
                        RC = sibling.right;
                        leftRotate(sibling);        // Left rotate the sibling
                        rightRotate(parent);        // Right rotate the parent
                        RC.color = parent.color;    // Change RC to the color of parent
                        sibling.color = "B";        // Change sibling to black
                        parent.color = "B";            // Change parent to black
                        node.color = "B";            // Change node to black
                    }

                    // Case 3a.2: The left child of the sibling is red
                    else if (isRed(sibling.left)) {
                        RC = sibling.left;
                        rightRotate(parent);            // Right rotate the parent
                        sibling.color = parent.color;    // Change sibling to the color of parent
                        RC.color = "B";                    // Change RC to black
                        parent.color = "B";                // Change parent to black
                        node.color = "B";                // Change node to black
                    }
                }

                // Case 3b: Sibling is a right child
                else if (isRightChild(sibling)) {

                    // Case 3b.1: The left child of the sibling is red
                    if (isRed(sibling.left)) {
                        RC = sibling.left;
                        rightRotate(sibling);        // Right rotate the sibling
                        leftRotate(parent);            // Left rotate the parent
                        RC.color = parent.color;    // Change RC to the color of parent
                        sibling.color = "B";        // Change sibling to black
                        parent.color = "B";            // Change parent to black
                        node.color = "B";            // Change node to black
                    }

                    // Case 3b.2: The right child of the sibling is red
                    else if (isRed(sibling.right)) {
                        RC = sibling.right;
                        leftRotate(parent);                // Left rotate the parent
                        sibling.color = parent.color;    // Change sibling to the color of parent
                        RC.color = "B";                    // Change RC to black
                        parent.color = "B";                // Change parent to black
                        node.color = "B";                // Change node to black
                    }
                }
            }

            // Case 4: The sibling and both of its children are black (or double black)
            else if ((isBlack(sibling) && isBlack(sibling.left) && isBlack(sibling.right)) ||
                    (isBlack(sibling) && isDoubleBlack(sibling.left) && isDoubleBlack(sibling.right))) {
                // 2nd condition is used when the double black node is NIL and the sibling's children are
                // NIL since changing NIL to double black changes all instances of NIL to double black

                // Case 4a: The parent of sibling is red
                if (isRed(parent)) {        // The sibling and node share the same parent
                    sibling.color = "R";    // Change sibling to red
                    parent.color = "B";        // Change parent to black
                    node.color = "B";        // Change node to black
                }

                // Case 4b: The parent of sibling is black
                else if (isBlack(sibling.parent)) {
                    sibling.color = "R";    // Change sibling to red
                    parent.color = "DB";    // Change parent to double black
                    node.color = "B";        // Change node to black
                    fixDoubleBlack(parent);    // Recursion
                }
            }
        }
    }

    private E maxLeftSubtree(RBNode<E> node) {
        RBNode<E> current = node.left;
        while (current.right != NIL) {
            current = current.right;
        }
        return current.getData();
    }

    public boolean find(E key) {
        RBNode<E> current = root;
        while (current != NIL) {
            if (key.equals(current.getData())) {
                return true;
            } else if (key.compareTo(current.getData()) < 0) {
                current = current.left;
            } else if (key.compareTo(current.getData()) > 0) {
                current = current.right;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean isLeaf(RBNode<E> node) {
        if (node == NIL) {
            return false;
        }
        return (node.left == NIL) && (node.right == NIL);
    }

    public boolean isLeftChild(RBNode<E> node) {
        if (node.equals(root) || node.parent.left == null) {
            return false;
        }
        return node.parent.left.equals(node);
    }

    public boolean isRightChild(RBNode<E> node) {
        if (node.equals(root) || node.parent.right == null) {
            return false;
        }
        return node.parent.right.equals(node);
    }

    public RBNode<E> sibling(RBNode<E> node) {
        if (node.equals(root)) {
            return null;
        } else if (isLeftChild(node)) {
            return node.parent.right;
        }
        return node.parent.left;
    }

    public RBNode<E> uncle(RBNode<E> node) {
        if (node.equals(root) || node.equals(root.left) || node.equals(root.right)) {
            return null;
        }
        return sibling(node.parent);
    }

    public RBNode<E> grandparent(RBNode<E> node) {
        if (node.equals(root) || node.equals(root.left) || node.equals(root.right)) {
            return null;
        }
        return node.parent.parent;
    }

    public ArrayList<RBNode<E>> breadthfirst() {
        ArrayList<RBNode<E>> list = new ArrayList<>();
        if (!isEmpty()) {
            Queue<RBNode<E>> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                RBNode<E> node = queue.remove();
                list.add(node);
                if (node.left != NIL) {
                    queue.add(node.left);
                }
                if (node.right != NIL) {
                    queue.add(node.right);
                }
            }
        }
        return list;
    }

    private int numChildren(RBNode<E> node) {
        int count = 0;
        if (node.left != NIL) {
            count++;
        }
        if (node.right != NIL) {
            count++;
        }
        return count;
    }

    private boolean isRed(RBNode<E> node) {
        return node.color.equals("R");
    }

    private boolean isBlack(RBNode<E> node) {
        return node.color.equals("B");
    }

    private boolean isDoubleBlack(RBNode<E> node) {
        return node.color.equals("DB");
    }

    private boolean hasRedChild(RBNode<E> node) {
        return isRed(node.left) || isRed(node.right);
    }

    private void leftRotate(RBNode<E> root) {
        RBNode<E> pivot = root.right;
        pivot.parent = root.parent; // Update parent reference

        // SPECIAL CASE: The root of the entire tree is used as the root of the rotation
        if (this.root.equals(root)) {
            this.root = pivot;
        } else if (isLeftChild(root)) {
            pivot.parent.left = pivot;
        } else if (isRightChild(root)) {
            pivot.parent.right = pivot;
        }
        root.right = pivot.left;
        root.right.parent = root; // Update parent reference
        pivot.left = root;
        pivot.left.parent = pivot; // Update parent reference
    }

    private void rightRotate(RBNode<E> root) {
        RBNode<E> pivot = root.left;
        pivot.parent = root.parent; // Update parent reference

        // SPECIAL CASE: The root of the entire tree is used as the root of the rotation
        if (this.root.equals(root)) {
            this.root = pivot;
        } else if (isLeftChild(root)) {
            pivot.parent.left = pivot;
        } else if (isRightChild(root)) {
            pivot.parent.right = pivot;
        }
        root.left = pivot.right;
        root.left.parent = root; // Update parent reference
        pivot.right = root;
        pivot.right.parent = pivot; // Update parent reference
    }

}