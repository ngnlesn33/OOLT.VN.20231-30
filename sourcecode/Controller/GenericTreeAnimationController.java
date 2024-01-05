package Controller;

import Model.GenericTree;
import View.GenericTreeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.Stack;

public class GenericTreeAnimationController {
    @FXML
    private TextField tfKey;

    @FXML
    private Button backButton;

    // Add your BST and BTView instances here
    private final GenericTree<Integer> tree; /// Create a tree
    private final GenericTreeView view; // Create a View
    //    private final Stack<Action> undoStack = new Stack<>();
//    private final Stack<Action> redoStack = new Stack<>();
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
        if (tree.search(key)) { // key is in the tree already
            view.setStatus(key + " is already in the tree");
        } else {
            tree.insert(key); // Insert a new key
            view.displayTree();
            view.setStatus(key + " is inserted in the tree");
        }
    }
    @FXML

    public void handleDelete(ActionEvent event) {
        // Implement the code for deleting a key from the generic tree
        int key = Integer.parseInt(tfKey.getText());
        if (!tree.search(key)) { // key is not in the tree
            view.setStatus(key + " is not in the tree");
        } else {
            tree.delete(key); // Delete a key
            view.displayTree();
            view.setStatus(key + " is deleted from the tree");
        }

    }
    @FXML

    public void handleUpdate(ActionEvent event) {
    }
    @FXML

    public void handleTraverseBFS(ActionEvent event) {
    }
    @FXML

    public void handleSearch(ActionEvent event) {
    }
    @FXML

    public void handleUndo(ActionEvent event) {
    }
    @FXML

    public void handleRedo(ActionEvent event) {
    }
    @FXML

    public void handleBack(ActionEvent event) {
    }
}
