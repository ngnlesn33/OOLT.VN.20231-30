package model;

public class InsertCommand implements Command {
    private final GenericTree<Integer> tree;
    private final int key;
    private final int parent;
    private final boolean hasParent; // new field

    public InsertCommand(GenericTree<Integer> tree, int key) {
        this.tree = tree;
        this.key = key;
        this.parent = -1;
        this.hasParent = false; // set to false
    }

    public InsertCommand(GenericTree<Integer> tree, int key, int parent) {
        this.tree = tree;
        this.key = key;
        this.parent = parent;
        this.hasParent = true; // set to true
    }

    @Override
    public void execute() {
        if (hasParent) {
            execute2();
        } else {
            tree.insert(key);
        }
    }

    public void execute2() {
        tree.insert(key, parent);
    }

    @Override
    public void undo() {
        tree.delete(key);
    }
}