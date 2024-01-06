package view;

import model.GenericTree;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class GenericTreeView extends Pane {
    private final GenericTree<Integer> tree;

    private final Map<GenericTree.TreeNode<Integer>, Circle> nodeCircles = new HashMap<>();

    public void setStatus(String msg) {
        Text statusText = new Text(20, 20, msg);
        statusText.setFont(new Font(20));
        getChildren().add(statusText);
    }

    public GenericTreeView(GenericTree<Integer> tree) {
        this.tree = tree;
        setStatus("Tree is empty");
    }

    public void displayTree() {
        this.getChildren().clear(); // Clear the pane
        if (tree.getRoot() != null) {
            // Display generic tree recursively
            double vGap = 50;
            displayTree(tree.getRoot(), getWidth() / 2, 20, getWidth() / 8, vGap);
        }
    }

    // Display generic tree recursively
    public void displayTree(GenericTree.TreeNode<Integer> root, double x, double y, double hGap, double vGap) {
        if (root == null) {
            return;
        }

        Circle circle = new Circle(x, y, 15);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        getChildren().addAll(circle, new Text(x - 4, y + 4, root.getElement().toString()));
        nodeCircles.put(root, circle);

        if (root.getChildren() != null) {
            for (int i = 0; i < root.getChildren().size(); i++) {
                double childX = x - hGap * root.getChildren().size() / 2 + i * hGap;
                double childY = y + vGap;

                // Draw a line to the child node
                // Calculate the direction of the line
                double dx = childX - x;
                double dy = childY - y;
                double magnitude = Math.sqrt(dx * dx + dy * dy);

                // Normalize the direction
                dx /= magnitude;
                dy /= magnitude;

                // Move the starting point of the line by the radius of the circle
                double lineStartX = x + dx * circle.getRadius();
                double lineStartY = y + dy * circle.getRadius();

                // Create the line
                Line line = new Line(lineStartX, lineStartY, childX, childY);
                getChildren().add(line);

                // Recursively display the child node
                displayTree(root.getChildren().get(i), childX, childY, hGap / 2, vGap);
            }
        }
    }

    public void highlightNode(GenericTree.TreeNode<Integer> node) {
        if (node != null) {
            Circle circle = nodeCircles.get(node);
            if (circle != null) {
                circle.setFill(Color.AQUA); // Change the color to highlight the node
            }
        }
    }
}

