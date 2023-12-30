package Model;

/**
 * A record representing an action performed on a binary search tree. An action consists of a key
 * and a flag indicating whether the action was an insertion or not.
 *
 * @param key The key involved in the action.
 * @param isInsert A flag indicating whether the action was an insertion. If true, the action was an
 *        insertion. If false, the action was a deletion.
 */
public record Action(int key, boolean isInsert) {
}


