package tn.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger l'interface admin
        Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/views/AdminInterface.fxml"));
        
        // Configurer la scène
        Scene scene = new Scene(root);
        primaryStage.setTitle("Interface Administrateur");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 