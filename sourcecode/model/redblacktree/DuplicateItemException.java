package model.redblacktree;

import java.io.Serial;

public class DuplicateItemException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This constructor sets the error message.
     */
    public DuplicateItemException() {
        super("A duplicate item exists in the tree.");
    }
}