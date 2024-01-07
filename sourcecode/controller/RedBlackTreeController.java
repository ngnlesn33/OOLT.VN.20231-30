package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.redblacktree.*;
import view.RBTreeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class RedBlackTreeController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button backButton;

    @FXML
    TextField textField = new TextField();
    RedBlackTree<Integer> tree;
    RBTreeView<Integer> view;
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public RedBlackTreeController() {
        this.tree = new RedBlackTree<>();
        this.view = new RBTreeView<>(tree);
    }

    public void initialize() {
        borderPane.setCenter(view);
    }

    @FXML
    void handleBack() {
        // handle back action
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/main_menu.fxml"));
            Parent root = loader.load();

            // Get the MainMenuController and set the mainStage
            MainMenuController mainMenuController = loader.getController();
            Stage stage = (Stage) backButton.getScene().getWindow();
            mainMenuController.setMainStage(stage);
            stage.setScene(new Scene(root, 300, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDelete() {
        try {
            int key = Integer.parseInt(textField.getText());
            if (tree.isEmpty()) {
                view.displayTree();
                view.setText("The tree is empty");
            } else if (!tree.find(key)) {
                view.displayTree();
                view.setText(key + " is not in the tree");
            } else {
                Command command = new DeleteCommand(tree, key);
                command.execute();
                undoStack.push(command);
                redoStack.clear();
                view.displayTree();
                view.setText(key + " is deleted from the tree");
            }
        } catch (NumberFormatException e) {
            view.setText("You must enter an integer");
        }
        textField.setText("");
    }

    @FXML
    void handleInsert() {
        try {
            int key = Integer.parseInt(textField.getText());
            if (tree.isEmpty()) {
                Command command = new InsertCommand(tree, key);
                command.execute();
                undoStack.push(command);
                redoStack.clear();
                view.displayTree();
                view.setText(key + " is inserted into the tree");
            } else if (tree.find(key)) {
                view.displayTree();
                view.setText(key + " is already in the tree");
            } else {
                Command command = new InsertCommand(tree, key);
                command.execute();
                undoStack.push(command);
                redoStack.clear();
                view.displayTree();
                view.setText(key + " is inserted into the tree");
            }
        } catch (NumberFormatException e) {
            view.setText("You must enter an integer");
        }
        textField.setText("");
    }

    @FXML
    void handleSearch() {
        try {
            int key = Integer.parseInt(textField.getText());
            if (tree.isEmpty()) {
                view.displayTree();
                view.setText("The tree is empty");
            } else if (!tree.find(key)) {
                view.displayTree();
                view.setText(key + " is not in the tree");
            } else {
                view.displayTree();
                view.setText(key + " is in the tree");
            }
        } catch (NumberFormatException e) {
            view.setText("You must enter an integer");
        }
        textField.setText("");

    }

    @FXML
    public void handleUpdate() {
        // handle update action, the current element and the new element are in the text
        // field separated by a space character
        // for example, if the current element is 1 and the new element is 2, the text
        // field will contain "1 2"
        int key = Integer.parseInt(textField.getText().split(" ")[0]);
        int newKey = Integer.parseInt(textField.getText().split(" ")[1]);
        if (tree.isEmpty()) {
            view.displayTree();
            view.setText("The tree is empty");
        } else if (!tree.find(key)) {
            view.displayTree();
            view.setText(key + " is not in the tree");
        } else {
            tree.delete(key);
            tree.insert(newKey);
            view.displayTree();
            view.setText(key + " is updated to " + newKey);
        }
        textField.setText("");

    }

    Timeline timeline = new Timeline();

    public void handleTraverseBFS() {
        ArrayList<RBNode<Integer>> bfsResult = tree.breadthfirst(); // replace Integer with the type of elements in your
                                                                    // tree
        for (int i = 0; i < bfsResult.size(); i++) {
            RBNode<Integer> node = bfsResult.get(i);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(1000 + 1000 * i), e -> {
                view.displayTree();
                view.highlightNode(node);
                view.setText("BFS: " + node.getData());
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        // Add a delay at the end of the animation
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000 + 1000 * bfsResult.size()), e -> {
            view.displayTree();
            view.setText("BFS: Done");
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        // Unhighlight all nodes
        timeline.setOnFinished(e -> {
            view.displayTree();
        });
    }

    public void handleUndo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            view.displayTree();
            redoStack.push(command);
        }
    }

    public void handleRedo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            view.displayTree();
            undoStack.push(command);
        }
    }
}