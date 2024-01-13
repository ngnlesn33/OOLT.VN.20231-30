package model.generictree;

import model.AbstractTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class GenericTree<E extends Comparable<E>> extends AbstractTree<E> {
    protected TreeNode<E> root;
    protected int size = 0;

    // Get root
    public TreeNode<E> getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    // Return parent of the node
    public E getParent(E key) {
        if (root == null) {
            return null;
        }
        return getParentHelper(root, key);
    }

    private E getParentHelper(TreeNode<E> root, E key) {
        if (root == null) {
            return null;
        }
        if (root.element.equals(key)) {
            return null;
        }
        for (TreeNode<E> child : root.getChildren()) {
            if (child.element.equals(key)) {
                return root.element;
            }
        }
        for (TreeNode<E> child : root.getChildren()) {
            E parent = getParentHelper(child, key);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }




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

            queue.addAll(current.getChildren());
        }
        return false;
    }

    public boolean insert(E e) {
        if (root == null) {
            // Tree is empty, create the root node
            root = new TreeNode<>(e);
            size++;
            return false;
        }
        return false;
    }

    public void insert(E e, E parent) {
        if (root == null) {
            // Tree is empty, create the root node
            root = new TreeNode<>(e);
            size++;
            return;
        }
        // if the node is already in the tree, do nothing
        if (search(e)) {
            return;
        }
        // Tree is not empty, find the parent node
        if (insertHelper(root, e, parent)) {
            size++;
        }
    }

    private boolean insertHelper(TreeNode<E> currentNode, E data, E parentData) {
        if (currentNode.element.equals(parentData)) {
            // Found the parent node, add the new node as its child
            currentNode.getChildren().add(new TreeNode<>(data));
            return true;
        } else {
            // Try to insert into children
            for (TreeNode<E> child : currentNode.getChildren()) {
                if (insertHelper(child, data, parentData)) {
                    return true;
                }
            }
            return false; // Parent not found in the current subtree
        }
    }

    public boolean delete(E data) {
        if (root == null) {
            System.out.println("Tree is empty. Cannot delete from an empty tree.");
            return false;
        }

        if (root.element.equals(data)) {
            // Delete root
            root = null;
        } else {
            deleteHelper(root, data);
        }
        return false;
    }

    @Override
    public int getSize() {
        return 0;
    }

    private void deleteHelper(TreeNode<E> currentNode, E data) {
        Iterator<TreeNode<E>> iterator = currentNode.getChildren().iterator();

        while (iterator.hasNext()) {
            TreeNode<E> child = iterator.next();

            if (child.element.equals(data)) {
                // Remove the node from children
                iterator.remove();
                return;
            } else {
                // Continue searching in the subtree
                deleteHelper(child, data);
            }
        }
    }

    public void updateKey(E oldKey, E newKey) {
        if (root == null) {
            System.out.println("Tree is empty. Cannot update in an empty tree.");
            return;
        }

        updateKeyHelper(root, oldKey, newKey);
    }

    private void updateKeyHelper(TreeNode<E> currentNode, E oldKey, E newKey) {
        if (currentNode.element.equals(oldKey)) {
            // Update the key
            currentNode.element = newKey;
        }

        // Update keys in children
        for (TreeNode<E> child : currentNode.getChildren()) {
            updateKeyHelper(child, oldKey, newKey);
        }
    }

    public ArrayList<TreeNode<E>> traverseBreadthFirst() {
        ArrayList<TreeNode<E>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode<E>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode<E> currentNode = queue.poll();
            result.add(currentNode);

            for (TreeNode<E> child : currentNode.getChildren()) {
                queue.offer(child);
            }
        }

        return result;
    }

    public TreeNode<E> findNode(E key) {
        if (root == null) {
            return null;
        }
        return findNodeHelper(root, key);
    }

    private TreeNode<E> findNodeHelper(TreeNode<E> root, E key) {
        if (root == null) {
            return null;
        }
        if (root.element.equals(key)) {
            return root;
        }
        for (TreeNode<E> child : root.getChildren()) {
            TreeNode<E> node = findNodeHelper(child, key);
            if (node != null) {
                return node;
            }
        }
        return null;
    }
}
