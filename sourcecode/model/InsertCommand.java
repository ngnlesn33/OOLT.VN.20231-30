package model;

public class InsertCommand implements Command {
    private final RedBlackTree<Integer> tree;
    private final int key;

    public InsertCommand(RedBlackTree<Integer> tree, int key) {
        this.tree = tree;
        this.key = key;
    }

    @Override
    public void execute() {
        tree.insert(key);
    }

    @Override
    public void undo() {
        tree.delete(key);
    }
}