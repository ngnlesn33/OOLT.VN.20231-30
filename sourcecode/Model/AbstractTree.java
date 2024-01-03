package Model;

public abstract class AbstractTree<E> implements Tree<E> {

    @Override
    /* Return true if the tree is empty */
    public boolean isEmpty() {
        return getSize() == 0;
    }
}