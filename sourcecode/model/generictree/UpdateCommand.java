package model.generictree;

import model.generictree.Command;
import model.generictree.GenericTree;

public class UpdateCommand implements Command {
    private final GenericTree<Integer> tree;
    private final int oldKey;
    private final int newKey;

    public UpdateCommand(GenericTree<Integer> tree, int oldKey, int newKey) {
        this.tree = tree;
        this.oldKey = oldKey;
        this.newKey = newKey;
    }

    @Override
    public void execute() {
        tree.updateKey(oldKey, newKey);
    }

    @Override
    public void execute2() {

    }

    @Override
    public void undo() {
        tree.updateKey(newKey, oldKey);
    }
}