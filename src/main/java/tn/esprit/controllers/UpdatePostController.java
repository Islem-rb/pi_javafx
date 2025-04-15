package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Post;
import tn.esprit.services.ServicePost;

import java.io.File;

public class UpdatePostController {

    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextField;
    @FXML
    private Label lblImagePath;
    @FXML
    private Button btnChooseImage;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;

    private String imagePath;
    private Post postToUpdate;
    private final ServicePost servicePost = new ServicePost();
    private GestionPost gestionPostController;

    public void setPostToUpdate(Post post) {
        this.postToUpdate = post;
        // Pré-remplir les champs avec les données du post
        titleTextField.setText(post.getTitle());
        descriptionTextField.setText(post.getDescription());
        lblImagePath.setText(post.getImagePath());
        imagePath = post.getImagePath();
    }

    public void setGestionPostController(GestionPost controller) {
        this.gestionPostController = controller;
    }

    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

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
    private void updatePost(ActionEvent event) {
        String newTitle = titleTextField.getText().trim();
        String newDescription = descriptionTextField.getText().trim();

        // Validation du titre
        if (newTitle.length() < 4) {
            showAlert("Erreur", "Le titre doit contenir au moins 4 caractères !");
            return;
        }

        // Validation de la description
        if (newDescription.length() < 10) {
            showAlert("Erreur", "La description doit contenir au moins 10 caractères !");
            return;
        }

        // Validation de l'image
        if (imagePath == null) {
            showAlert("Erreur", "Veuillez sélectionner une image !");
            return;
        }

        // Mettre à jour les données du post
        postToUpdate.setTitle(newTitle);
        postToUpdate.setDescription(newDescription);
        if (imagePath != null) {
            postToUpdate.setImagePath(imagePath);
        }

        // Mettre à jour dans la base de données
        servicePost.updatePost(postToUpdate);
        
        // Rafraîchir la liste des posts
        if (gestionPostController != null) {
            gestionPostController.refreshTableView();
        }
        
        showAlert("Succès", "Post mis à jour avec succès !");
        closeWindow();
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
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