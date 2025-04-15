package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Post;
import tn.esprit.entities.Comment;
import tn.esprit.services.ServicePost;
import tn.esprit.services.ServiceComment;

import java.util.List;

public class AdminPostController {

    @FXML
    private VBox postsContainer;  // Conteneur pour les posts

    @FXML
    private Button btnCancel;

    private final ServicePost servicePost = new ServicePost();
    private final ServiceComment serviceComment = new ServiceComment();

    // Méthode pour afficher les posts dans l'interface admin
    @FXML
    private void showAdminDashboard() {
        // Effacer les anciens posts
        postsContainer.getChildren().clear();

        // Charger les nouveaux posts
        List<Post> posts = servicePost.getAllPosts();

        // Afficher chaque post dans le conteneur
        for (Post post : posts) {
            VBox postCard = new VBox(10);
            postCard.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #ccc;");

            // Titre du post
            Label titleLabel = new Label("📝 " + post.getTitle());
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // Description, date, auteur
            Label descLabel = new Label("📄 " + post.getDescription());
            Label dateLabel = new Label("📅 " + post.getCreatedAt().toString());
            Label authorLabel = new Label("👤 Par " + post.getUserNom() + " " + post.getUserPrenom());

            // Image du post
            if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
                try {
                    Image image = new Image("file:" + post.getImagePath());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(400);
                    imageView.setPreserveRatio(true);
                    postCard.getChildren().add(imageView);
                } catch (Exception e) {
                    postCard.getChildren().add(new Label("⚠️ Image non disponible."));
                }
            }

            postCard.getChildren().addAll(titleLabel, descLabel, dateLabel, authorLabel);

            // Commentaires
            Label commentsTitle = new Label("💬 Commentaires :");
            commentsTitle.setStyle("-fx-font-weight: bold;");
            postCard.getChildren().add(commentsTitle);

            List<Comment> comments = serviceComment.getCommentsByPostId(post.getId());

            if (comments.isEmpty()) {
                Label noComments = new Label("❌ Aucun commentaire.");
                postCard.getChildren().add(noComments);
            } else {
                for (Comment comment : comments) {
                    VBox commentBox = new VBox();
                    commentBox.setStyle("-fx-background-color: #f1f1f1; -fx-padding: 8; -fx-background-radius: 6;");
                    Label content = new Label("🗨 " + comment.getContent());
                    Label user = new Label("👤 " + comment.getUserPrenom() + " " + comment.getUserNom());
                    commentBox.getChildren().addAll(content, user);
                    postCard.getChildren().add(commentBox);
                }
            }

            // Bouton de suppression
            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 10;");
            deleteButton.setOnAction(e -> deletePost(e, post));  // Action de suppression pour ce post

            postCard.getChildren().add(deleteButton);

            postCard.getChildren().add(new Separator());
            postsContainer.getChildren().add(postCard);
        }
    }

    // Méthode pour supprimer un post
    @FXML
    private void deletePost(ActionEvent event, Post post) {
        boolean success = servicePost.deletePost(post);
        if (success) {
            showAlert("Succès", "Post supprimé avec succès.");
            refreshPostsList(); // Rafraîchit la liste des posts pour ne plus afficher celui supprimé
        } else {
            showAlert("Erreur", "Erreur lors de la suppression du post.");
        }
    }

    // Rafraîchit la liste des posts après suppression
    private void refreshPostsList() {
        postsContainer.getChildren().clear();  // Vide l'ancien contenu
        List<Post> posts = servicePost.getAllPosts();  // Récupère la liste mise à jour des posts
        for (Post post : posts) {
            VBox postCard = new VBox(10);
            postCard.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10;");
            postsContainer.getChildren().add(postCard);  // Ajoute le post au container
        }
    }

    // Affiche un message d'alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Fermeture de la fenêtre
    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
