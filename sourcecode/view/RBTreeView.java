package view;

import model.redblacktree.RBNode;
import model.redblacktree.RedBlackTree;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Red-Black Tree GUI.
 *
 * @author Richard Shu
 * @date Nov 9, 2017
 */
public class RBTreeView<E extends Comparable<E>> extends BorderPane {
    private final RedBlackTree<E> tree;
    private double radius = 30; // Tree node radius
    private double vGap = 75; // Gap between two levels in a tree
    private final Map<RBNode<E>, Circle> nodeCircles = new HashMap<>();

    /**
     * Default constructor.
     *
     * @param tree an empty tree
     */
    public RBTreeView(RedBlackTree<E> tree) {
        this.tree = tree;
    }

    /**
     * Displays the tree on the screen.
     */
    public void displayTree() {
        this.getChildren().clear();
        if (tree.getRoot() != null) {
            displayTree(tree.getRoot(), getWidth() / 2, vGap, getWidth() / 4);
        }
    }

    /**
     * Displays a subtree rooted at the position (x, y)
     *
     * @param root the root of the tree
     * @param x    the x coordinate
     * @param y    the y coordinate
     * @param hGap the gap
     */
    public void displayTree(RBNode<E> root, double x, double y, double hGap) {
        if (root.left != null) {
            getChildren().add(new Line(x - hGap, y + vGap, x, y));
            displayTree(root.left, x - hGap, y + vGap, hGap / 2);
        }
        if (root.right != null) {
            getChildren().add(new Line(x + hGap, y + vGap, x, y));
            displayTree(root.right, x + hGap, y + vGap, hGap / 2);
        }

        // Display the node
        Circle circle = new Circle(x, y, radius);
        Text text = new Text(x - 4, y + 4, root.getData() + "");
        text.setStroke(Color.WHITE);
        if (root.color.equals("B")) {
            circle.setFill(Color.BLACK);
        } else if (root.color.equals("R")) {
            circle.setFill(Color.RED);
        }
        getChildren().addAll(circle, text);
        nodeCircles.put(root, circle);
    }

    // Set text to the top of the pane
    public void setText(String msg) {
        Text statusText = new Text(20, 20, msg);
        statusText.setFont(new Font(20));
        getChildren().add(statusText);
    }

    public void highlightNode(RBNode<E> node) {
        if (node != null) {
            Circle circle = nodeCircles.get(node);
            if (circle != null) {
                circle.setFill(Color.AQUA); // Change the color to highlight the node
            }
        }
    }
}