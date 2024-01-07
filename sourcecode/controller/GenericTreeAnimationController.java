package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.generictree.*;
import view.GenericTreeView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GenericTreeAnimationController {
    @FXML
    private TextField tfKey;
    @FXML
    private TextField tfParent;
    @FXML
    private Button backButton;

    // Add your BST and BTView instances here
    private final GenericTree<Integer> tree; /// Create a tree
    private final GenericTreeView view; // Create a View
    // private final Stack<Action> undoStack = new Stack<>();
    // private final Stack<Action> redoStack = new Stack<>();
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    @FXML
    private BorderPane borderPane = new BorderPane(); // Add a field for the BorderPane

    // This method will be called after the FXML file is loaded
    public void initialize() {
        borderPane.setCenter(view);
    }

    public GenericTreeAnimationController() {
        this.tree = new GenericTree<>();
        this.view = new GenericTreeView(this.tree);
    }

    @FXML
    public void handleInsert(ActionEvent event) {
        int key = Integer.parseInt(tfKey.getText());

        // if the root is null, insert the key as the root
        if (tree.getRoot() == null) {
            Command command = new InsertCommand(tree, key);
            command.execute();
            undoStack.push(command);
            redoStack.clear();
            view.displayTree();
            view.setStatus(key + " is inserted into the tree");
        } else if (tree.search(key)) {
            view.displayTree();
            view.setStatus(key + " is already in the tree");
        } else if (!tree.search(Integer.parseInt(tfParent.getText()))) {
            view.displayTree();
            view.setStatus(Integer.parseInt(tfParent.getText()) + " is not in the tree");
        } else if (tree.search(Integer.parseInt(tfParent.getText()))) {
            Command command = new InsertCommand(tree, key, Integer.parseInt(tfParent.getText()));
            command.execute2();
            undoStack.push(command);
            redoStack.clear();
            view.displayTree();
            view.setStatus(key + " is inserted into the tree");
        }
        tfKey.setText("");
    }

    @FXML

    public void handleDelete(ActionEvent event) {
        int key = Integer.parseInt(tfKey.getText());
        if (tree.isEmpty()) {
            view.displayTree();
            view.setStatus("The tree is empty");
        } else if (!tree.search(key)) {
            view.displayTree();
            view.setStatus(key + " is not in the tree");
        } else {
            int parent = tree.getParent(key);
            System.out.println(parent);
            Command command = new DeleteCommand(tree, key, parent);
            command.execute();
            undoStack.push(command);
            redoStack.clear();
            view.displayTree();
            view.setStatus(key + " is deleted from the tree");
        }
        tfKey.setText("");
    }

    @FXML

    public void handleUpdate(ActionEvent event) {
        // key and newKey are in the text fields separated by a space character
        String[] keys = tfKey.getText().split(" ");
        int key = Integer.parseInt(keys[0]);
        int newKey = Integer.parseInt(keys[1]);
        if (tree.isEmpty()) {
            view.displayTree();
            view.setStatus("The tree is empty");
        } else if (!tree.search(key)) {
            view.displayTree();
            view.setStatus(key + " is not in the tree");
        } else {
            if (tree.search(newKey)) {
                view.displayTree();
                view.setStatus(newKey + " is already in the tree");
                return;
            }
            Command command = new UpdateCommand(tree, key, newKey);
            command.execute();
            undoStack.push(command);
            redoStack.clear();
            view.displayTree();
            view.setStatus(key + " is updated to " + newKey);
        }
        tfKey.setText("");
    }

    Timeline timeline = new Timeline();

    @FXML
    public void handleTraverseBFS() {
        ArrayList<GenericTree.TreeNode<Integer>> bfsResult = tree.traverseBreadthFirst(); // replace Integer with the
                                                                                          // type of elements in your
        // tree
        for (int i = 0; i < bfsResult.size(); i++) {
            GenericTree.TreeNode<Integer> node = bfsResult.get(i);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(1000 + 1000 * i), e -> {
                view.displayTree();
                view.highlightNode(node);
                view.setStatus("BFS: " + node.getElement());
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        // Add a delay at the end of the animation
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000 + 1000 * bfsResult.size()), e -> {
            view.displayTree();
            view.setStatus("BFS: Done");
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        // Unhighlight all nodes
        timeline.setOnFinished(e -> {
            view.displayTree();
        });
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        int key = Integer.parseInt(tfKey.getText());
        if (tree.isEmpty()) {
            view.displayTree();
            view.setStatus("The tree is empty");
        } else if (!tree.search(key)) {
            view.displayTree();
            view.setStatus(key + " is not in the tree");
        } else {
            view.displayTree();
            view.setStatus(key + " is in the tree");
        }
    }

    @FXML
    public void handleUndo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            view.displayTree();
            redoStack.push(command);
        }
    }

    @FXML
    public void handleRedo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            view.displayTree();
            undoStack.push(command);
        }
    }

    @FXML

    public void handleBack(ActionEvent event) {
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
}
