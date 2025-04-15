package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Post;
import tn.esprit.services.ServicePost;

import java.io.File;

public class AddPostController {

    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextField;
    @FXML
    private Label lblImagePath;
    @FXML
    private Button btnChooseImage;
    @FXML
    private Button btnsave;
    @FXML
    private Button btncancel;
    @FXML
    private Button btnCancel;

    private String imagePath;
    private final ServicePost servicePost = new ServicePost();
    private GestionPost gestionPostController;

    public void setGestionPostController(GestionPost controller) {
        this.gestionPostController = controller;
    }

    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null); //Affiche la fenêtre de sélection de fichier et récupère le fichier sélectionné.

        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
                imagePath = selectedFile.getAbsolutePath();
                lblImagePath.setText(imagePath);
            } else {
                showAlert("Erreur", "Veuillez sélectionner une image au format JPG ou PNG !");
            }
        }
    }

    @FXML
    private void savePost(ActionEvent event) {
        String title = titleTextField.getText().trim();
        String description = descriptionTextField.getText().trim();

        // Validation du titre
        if (title.length() < 4) {
            showAlert("Erreur", "Le titre doit contenir au moins 4 caractères !");
            return;
        }

        // Validation de la description
        if (description.length() < 10) {
            showAlert("Erreur", "La description doit contenir au moins 10 caractères !");
            return;
        }

        // Validation de l'image
        if (imagePath == null) {
            showAlert("Erreur", "Veuillez sélectionner une image !");
            return;
        }

        Post post = new Post(title, description, imagePath); // Crée un nouvel objet Post avec le titre, la description et le chemin de l'image.
        if (servicePost.addPost(post)) {
            showAlert("Succès", "Post ajouté avec succès !");
            if (gestionPostController != null) { //Si le contrôleur GestionPost est défini, il rafraîchit la vue de gestion des posts.
                gestionPostController.refreshTableView();
            }
            closeWindow();
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout du post !");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btncancel.getScene().getWindow(); //Récupère la fenêtre actuelle.


        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}