package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tn/esprit/views/GestionPost.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 467);
        primaryStage.setTitle("Gestion des Posts");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
