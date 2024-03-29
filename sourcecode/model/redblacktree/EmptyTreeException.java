package model.redblacktree;

import java.io.Serial;

public class EmptyTreeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This constructor sets the error message.
     */
    public EmptyTreeException() {
        super("The tree is empty.");
    }
}