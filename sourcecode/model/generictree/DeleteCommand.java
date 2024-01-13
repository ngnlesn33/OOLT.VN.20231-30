package model.generictree;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements Command {
    private final GenericTree<Integer> tree;
    private final int key;
    private final int parent;
    private List<TreeNode<Integer>> deletedChildren;

    public DeleteCommand(GenericTree<Integer> tree, int key, int parent) {
        this.tree = tree;
        this.key = key;
        this.parent = parent;
    }

    @Override
    public void execute() {
        TreeNode<Integer> nodeToDelete = tree.findNode(key);
        if (nodeToDelete != null) {
            deletedChildren = new ArrayList<>(nodeToDelete.getChildren());
            tree.delete(key);
        }
    }

    @Override
    public void execute2() {
    }

    @Override
    public void undo() {
        if (deletedChildren != null) {
            tree.insert(key, parent);
            for (TreeNode<Integer> child : deletedChildren) {
                tree.insert(child.element, key);
            }
        }
    }
}
