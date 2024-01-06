package model.redblacktree;

public interface Command {
    void execute();
    void undo();
}
