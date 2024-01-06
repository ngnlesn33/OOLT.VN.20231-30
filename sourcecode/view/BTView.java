package view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.bstmodel.BST;
import model.bstmodel.BST.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class BTView extends Pane {
    private final BST<Integer> tree;
    private final double vGap = 50;
    private final Map<TreeNode<Integer>, Circle> nodeCircles = new HashMap<>();

    public BTView(BST<Integer> tree) {
        this.tree = tree;
        setStatus("Tree is empty");
    }

    public void setStatus(String msg) {
        Text statusText = new Text(20, 20, msg);
        statusText.setFont(new Font(20));
        getChildren().add(statusText);
    }

    public void displayTree() {
        this.getChildren().clear(); // Clear the pane
        if (tree.getRoot() != null) {
            // Display tree recursively
            displayTree(tree.getRoot(), getWidth() / 2, vGap, getWidth() / 4);
        }
    }

    /**
     * Display a subtree rooted at position (x, y)
     */
    private void displayTree(TreeNode<Integer> current, double x, double y, double hGap) {
        if (current.getLeft() != null) {
            // Draw a line to the left node
            getChildren().add(new Line(x - hGap, y + vGap, x, y));
            // Draw the left subtree recursively
            displayTree(current.getLeft(), x - hGap, y + vGap, hGap / 2);
        }
        if (current.getRight() != null) {
            // Draw a line to the right node
            getChildren().add(new Line(x + hGap, y + vGap, x, y));
            // Draw the right subtree recursively
            displayTree(current.getRight(), x + hGap, y + vGap, hGap / 2);
        }
        // Display the current node
        // Tree node radius
        double radius = 15;
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        getChildren().addAll(circle, new Text(x - 4, y + 4, current.getElement() + ""));
        // add the circle to the map
        nodeCircles.put(current, circle);
    }

    public void highlightNode(Integer element) {
        TreeNode<Integer> node = tree.searchNode(element);
        if (node != null) {
            Circle circle = nodeCircles.get(node);
            if (circle != null) {
                circle.setFill(Color.RED); // Change the color to highlight the node
            }
        }
    }

}
