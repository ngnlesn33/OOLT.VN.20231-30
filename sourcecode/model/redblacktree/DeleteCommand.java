package model.redblacktree;

public class DeleteCommand implements Command {
    private final RedBlackTree<Integer> tree;
    private final int key;

    public DeleteCommand(RedBlackTree<Integer> tree, int key) {
        this.tree = tree;
        this.key = key;
    }

    @Override
    public void execute() {
        tree.delete(key);
    }

    @Override
    public void undo() {
        tree.insert(key);
    }
}