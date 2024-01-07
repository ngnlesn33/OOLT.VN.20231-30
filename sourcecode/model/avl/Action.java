package model.avl;

public class Action {
    private final int key;
    private final boolean isInsert;

    public Action(int key, boolean isInsert) {
        this.key = key;
        this.isInsert = isInsert;
    }

    public int getKey() {
        return key;
    }

    public boolean isInsert() {
        return isInsert;
    }
}



