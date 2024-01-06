package Controller;

import Model.Action;
import Model.BST;
import View.BTView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class BSTAnimationController {
    @FXML
    private TextField tfKey;

    @FXML
    private Button backButton;

    // Add your BST and BTView instances here
    private final BST<Integer> tree; /// Create a tree
    private final BTView view; // Create a View
    private final Stack<Action> undoStack = new Stack<>();
    private final Stack<Action> redoStack = new Stack<>();

    private void pushAction(int key, boolean isInsert) {
        undoStack.push(new Action(key, isInsert));
        // Clear the redo stack when a new action is performed
        // This is because we can't redo an action after a new action is performed
        redoStack.clear();
    }

    private Action popUndoAction() {
        if (!undoStack.isEmpty()) {
            return undoStack.pop();
        }
        return null;
    }

    private void pushRedoAction(Action action) {
        redoStack.push(action);
    }

    private Action popRedoAction() {
        if (!redoStack.isEmpty()) {
            return redoStack.pop();
        }
        return null;
    }

    @FXML
    private BorderPane borderPane = new BorderPane(); // Add a field for the BorderPane

    // This method will be called after the FXML file is loaded
    public void initialize() {
        borderPane.setCenter(view);
    }

    // Constructor
    public BSTAnimationController() {
        // Initialize your BST and BTView instances here
        tree = new BST<>();
        view = new BTView(tree);
    }

    @FXML
    void handleInsert(ActionEvent event) {
        // Implement the insert logic
        int key = Integer.parseInt(tfKey.getText());
        if (tree.search(key)) { // key is in the tree already
            view.displayTree();
            view.setStatus(key + " is already in the tree");
        } else {
            tree.insert(key); // Insert a new key
            view.displayTree();
            view.setStatus(key + " is inserted in the tree");
        }
        // After user clicks the Insert button, the text field should be cleared
        tfKey.setText("");
        // After inserting, push the action to the history stack
        pushAction(key, true);
        clearRedoStack(); // Clear the redo stack after a new action
    }

    @FXML
    void handleDelete(ActionEvent event) {
        // Implement the delete logic
        int key = Integer.parseInt(tfKey.getText());
        if (!tree.search(key)) { // key is not in the tree
            view.displayTree();
            view.setStatus(key + " is not in the tree");
        } else {
            tree.delete(key); // Delete a key
            view.displayTree();
            view.setStatus(key + " is deleted from the tree");
        }
        // After user clicks the Delete button, the text field should be cleared
        tfKey.setText("");
        pushAction(key, false);
        clearRedoStack(); // Clear the redo stack after a new action
    }

    // Change the node with current value to a new value
    // User input the current value and new value in the text fields respectively
    // and click the
    // Update button to change
    // the node value in the tree view accordingly.
    @FXML
    void handleUpdate(ActionEvent event) {
        String combinedText = tfKey.getText();
        String[] splitText = combinedText.split(",");

        if (splitText.length != 2) {
            view.displayTree();
            view.setStatus(
                    "Please enter two values separated by a comma.  Please use <currentValue>, <newValue>");
            return;
        }
        String currentText = splitText[0].trim();
        String newText = splitText[1].trim();

        int currentKey = Integer.parseInt(currentText);
        int newKey = Integer.parseInt(newText);
        if (!tree.search(currentKey)) {
            view.displayTree();
            view.setStatus(currentKey + " is not in the tree");
        } else if (tree.search(newKey)) {
            view.displayTree();
            view.setStatus(newKey + " is already in the tree");
        } else {
            tree.delete(currentKey);
            tree.insert(newKey);
            view.displayTree();
            view.setStatus(currentKey + " is updated to " + newKey);
        }
        // After user clicks the Update button, the text field should be cleared
        tfKey.setText("");
    }

    @FXML
    void handleSearch(ActionEvent event) {
        // Search for a node in the tree. Highlight the node if found.
        int key = Integer.parseInt(tfKey.getText());
        if (tree.search(key)) { // key is in the tree already
            view.displayTree();
            view.setStatus(key + " is found in the tree");
        } else {
            view.displayTree();
            view.setStatus(key + " is not found in the tree");
        }
        tfKey.setText("");
    }

    @FXML
    void handleUndo(ActionEvent event) {
        Action lastAction = popUndoAction();
        if (lastAction != null) {
            int key = lastAction.getKey();
            String action;
            if (lastAction.isInsert()) {
                tree.delete(key);
                action = "insertion";
            } else {
                tree.insert(key);
                action = "deletion";
            }
            view.displayTree();
            view.setStatus(key + " " + action + " undone");
            // Move the action to the redo stack
            pushRedoAction(lastAction);
        }
    }

    @FXML
    void handleRedo(ActionEvent event) {
        Action redoAction = popRedoAction();
        if (redoAction != null) {
            int key = redoAction.getKey();
            String action;
            if (redoAction.isInsert()) {
                tree.insert(key);
                action = "insertion";
            } else {
                tree.delete(key);
                action = "deletion";
            }
            view.displayTree();
            view.setStatus(key + " " + action + " redone");
            // Move the action back to the undo stack
            pushUndoAction(redoAction);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main_menu.fxml"));
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

    private void pushUndoAction(Action action) {
        undoStack.push(action);
    }

    private void clearRedoStack() {
        while (!redoStack.isEmpty()) {
            redoStack.pop();
        }
    }

    /**
     * Handles the action event triggered by the "Traverse BFS" button. This method
     * performs a
     * Breadth-First Search (BFS) traversal on the tree and highlights each node as
     * it is visited.
     * The nodes are highlighted in the order they are visited, with a delay between
     * each highlight.
     * After all nodes have been visited, the tree is redrawn without any
     * highlights.
     *
     * @param event The action event triggered by the button.
     */
    @FXML
    void handleTraverseBFS(ActionEvent event) {

        Iterator<Integer> iterator = tree.iterator();
        List<Integer> elements = new ArrayList<>();
        while (iterator.hasNext()) {
            elements.add(iterator.next());
        }
        // Create a timeline to schedule the animation, each element will be highlighted
        // for 2
        // seconds and the next element will be highlighted after 2 seconds
        // The last element will be highlighted for 2 seconds and then the   will be
        // unhighlighted after 2 seconds as well (total 4 seconds)
        // The total time for the animation is 2 * elements.size() + 2 seconds
        // The BFS traversal will block the UI thread, so we need to run it in a
        // separate thread to
        // avoid blocking the UI thread.
        Timeline timeline = new Timeline();
        for (int i = 0; i < elements.size(); i++) {
            Integer element = elements.get(i);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i * 2), e -> {
                view.highlightNode(element);
                System.out.println(element);
            }));
        }
        // Add a delay at the end
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(elements.size() * 2 + 2)));
        timeline.play();
        // Unhighlight the nodes after the animation is done
        timeline.setOnFinished(e -> {
            view.displayTree();
        });
    }
}
