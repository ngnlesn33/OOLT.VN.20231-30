package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.MainMenuController;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/main_menu.fxml"));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setMainStage(primaryStage);

        primaryStage.setTitle("Tree Visualization App");
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.show();
    }
}