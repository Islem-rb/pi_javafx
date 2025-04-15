package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Comment;
import tn.esprit.entities.Post;
import tn.esprit.services.ServiceComment;
import tn.esprit.services.ServicePost;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Comparator;

public class GestionPost implements Initializable {

    @FXML
    private FlowPane postsContainer;
    @FXML
    private Button btnAddPost;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> sortChoiceBox;

    private String imagePath;
    private Post currentPost;
    private final ServicePost servicePost = new ServicePost();
    private final ServiceComment serviceComment = new ServiceComment();
    private ObservableList<Post> postsList;
    private List<Post> allPosts = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) { //Configure l'affichage des posts dans un FlowPane (style cartes)
        // Configurer le FlowPane pour afficher 3 cartes par ligne
        postsContainer.setHgap(20);
        postsContainer.setVgap(20);
        postsContainer.setPadding(new Insets(20));
        
        // Initialiser le ChoiceBox pour le tri
        ObservableList<String> sortOptions = FXCollections.observableArrayList(
            "Plus récent",
            "Plus ancien"
        );
        sortChoiceBox.setItems(sortOptions);
        sortChoiceBox.setValue("Plus récent");
        
        // Ajouter un écouteur sur le ChoiceBox
        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortPosts();
        });
        
        // Ajouter un écouteur sur le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadPosts();
            } else {
                searchPosts();


            }
        });
        
        loadPosts();
    }

    private void sortPosts() {
        String sortOption = sortChoiceBox.getValue();
        List<Post> sortedPosts = new ArrayList<>(allPosts);
        
        if (sortOption.equals("Plus récent")) {
            sortedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
        } else if (sortOption.equals("Plus ancien")) {
            sortedPosts.sort(Comparator.comparing(Post::getCreatedAt));
        }
        
        displayPosts(sortedPosts);
    }

    private void loadPosts() {  //Récupère tous les posts depuis la base via ServicePost.
        postsContainer.getChildren().clear();
        allPosts = servicePost.getAllPosts();
        sortPosts(); // Appliquer le tri actuel
    }

    private void displayPosts(List<Post> posts) { //Vide le conteneur postsContainer  ,Pour chaque post, appelle createPostCard(post) pour générer une carte visuelle.


        postsContainer.getChildren().clear();
        for (Post post : posts) {
            postsContainer.getChildren().add(createPostCard(post));
        }
    }

    private Node createPostCard(Post post) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(300);
        card.setMaxWidth(300);

        // Conteneur pour centrer l'image
        HBox imageContainer = new HBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefWidth(300);

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(280);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
            try {
                File file = new File(post.getImagePath());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }

        imageContainer.getChildren().add(imageView);

        // Titre
        Label titleLabel = new Label(post.getTitle());
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(280);

        // Description avec bouton Read More
        VBox descriptionContainer = new VBox(5);
        Label descLabel = new Label(post.getDescription());
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #34495e;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(280);
        descLabel.setMaxHeight(40);

        // Vérifier si la description est trop longue
        if (post.getDescription().length() > 100) {
            Button readMoreButton = new Button("Read More");
            readMoreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-font-size: 12px; -fx-padding: 0;");
            readMoreButton.setOnAction(e -> showFullDescription(post));
            descriptionContainer.getChildren().addAll(descLabel, readMoreButton);
        } else {
            descriptionContainer.getChildren().add(descLabel);
        }

        // Date
        Label dateLabel = new Label("Posté le: " + post.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dateLabel.setStyle("-fx-font-size: 10px;");

        // Auteur
        Label authorLabel = new Label("Par: " + post.getUserPrenom() + " " + post.getUserNom());
        authorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #3498db;");

        // Boutons d'action
        HBox buttonBox = new HBox(8);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 12px;");
        updateButton.setPrefWidth(70);
        updateButton.setOnAction(e -> editPost(post));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px;");
        deleteButton.setPrefWidth(70);
        deleteButton.setOnAction(e -> deletePost(post));

        buttonBox.getChildren().addAll(updateButton, deleteButton);

        // Zone de commentaires
        VBox commentsBox = new VBox(5);
        commentsBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 8; -fx-background-radius: 5;");
        commentsBox.setMaxWidth(280);

        // Champ de commentaire
        HBox commentInputBox = new HBox(5);
        TextField commentField = new TextField();
        commentField.setPromptText("Ajouter un commentaire...");
        commentField.setPrefWidth(220);
        Button addCommentButton = new Button("Commenter");
        addCommentButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        addCommentButton.setOnAction(e -> addComment(post, commentField));
        commentInputBox.getChildren().addAll(commentField, addCommentButton);

        // Liste des commentaires
        VBox commentsList = new VBox(5);
        List<Comment> comments = serviceComment.getCommentsByPostId(post.getId());
        for (Comment comment : comments) {
            commentsList.getChildren().add(createCommentNode(comment));
        }

        commentsBox.getChildren().addAll(commentInputBox, commentsList);

        // Ajouter tous les éléments à la carte
        card.getChildren().addAll(
            imageContainer,
            titleLabel,
            descriptionContainer,
            dateLabel,
            authorLabel,
            buttonBox,
            commentsBox
        );

        return card;
    }

    private Node createCommentNode(Comment comment) {
        VBox commentBox = new VBox(5);
        commentBox.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-background-radius: 5;");
        
        // Auteur du commentaire 
        Label authorLabel = new Label(comment.getUserPrenom() + " " + comment.getUserNom());
        authorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Contenu du commentaire
        Label contentLabel = new Label(comment.getContent());
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(280);
        
        // Date du commentaire
        Label dateLabel = new Label(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
        
        // Boutons d'action
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        updateButton.setOnAction(e -> updateComment(comment));
        
        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteComment(comment));
        
        buttonBox.getChildren().addAll(updateButton, deleteButton);
        
        commentBox.getChildren().addAll(authorLabel, contentLabel, dateLabel, buttonBox);
        return commentBox;
    }

    private void updateComment(Comment comment) {
        TextInputDialog dialog = new TextInputDialog(comment.getContent());
        dialog.setTitle("Modifier le commentaire");
        dialog.setHeaderText("Modifier le contenu du commentaire");
        dialog.setContentText("Nouveau contenu:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newContent -> {
            comment.setContent(newContent);
            if (serviceComment.updateComment(comment)) {
                showAlert("Succès", "Commentaire modifié avec succès !");
                loadPosts();
            } else {
                showAlert("Erreur", "Erreur lors de la modification du commentaire !");
            }
        });
    }

    private void deleteComment(Comment comment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le commentaire");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce commentaire ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (serviceComment.deleteComment(comment.getId())) {
                showAlert("Succès", "Commentaire supprimé avec succès !");
                loadPosts();
            } else {
                showAlert("Erreur", "Erreur lors de la suppression du commentaire !");
            }
        }
    }

    private void addComment(Post post, TextField commentField) {
        String content = commentField.getText().trim();
        if (content.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un commentaire !");
            return;
        }

        Comment comment = new Comment(content, post.getId());
        if (serviceComment.addComment(comment)) {
            showAlert("Succès", "Commentaire ajouté avec succès !");
            loadPosts();
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout du commentaire !");
        }
    }

    private void editPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatepost.fxml"));
            Parent root = loader.load();
            
            UpdatePostController controller = loader.getController();
            controller.setPostToUpdate(post);
            controller.setGestionPostController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier le Post");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage());
        }
    }

    private void deletePost(Post post) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce post ?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            if (servicePost.deletePost(post)) {
                showAlert("Succès", "Le post a été supprimé avec succès !");
                loadPosts();
            } else {
                showAlert("Erreur", "Une erreur est survenue lors de la suppression du post.");
            }
        }
    }

    @FXML
    private void ajouterPost(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addpost.fxml"));
            Parent root = loader.load();
            AddPostController controller = loader.getController();
            controller.setGestionPostController(this);
            Stage stage = new Stage(); //// Nouvelle fenêtre
            stage.setTitle("Add New Post");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout de post : " + e.getMessage());
        }

    }










    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void refreshTableView() {
        loadPosts();
    }

    @FXML
    private void searchPosts() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            loadPosts();
            return;
        }

        List<Post> filteredPosts = allPosts.stream()
            .filter(post -> post.getTitle().toLowerCase().contains(searchText))
            .collect(Collectors.toList());

        displayPosts(filteredPosts);
    }

    @FXML
    private void resetSearch() {
        searchField.clear();
        sortChoiceBox.setValue("Plus récent");
        loadPosts();
    }

    private void showFullDescription(Post post) {
        Stage stage = new Stage();
        stage.setTitle("Description complète");

        VBox vbox = new VBox(15); // Augmenté l'espacement entre les éléments
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: white;");

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(500); // Largeur fixe pour l'image
        imageView.setFitHeight(300); // Hauteur fixe pour l'image
        imageView.setPreserveRatio(true);
        if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
            try {
                File file = new File(post.getImagePath());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }

        // Conteneur pour centrer l'image
        HBox imageContainer = new HBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getChildren().add(imageView);

        // Titre
        Label titleLabel = new Label(post.getTitle());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(500);

        // Description
        Label descLabel = new Label(post.getDescription());
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(500);

        // Auteur
        Label authorLabel = new Label("Par: " + post.getUserPrenom() + " " + post.getUserNom());
        authorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3498db;");

        // Bouton Fermer
        Button closeButton = new Button("Fermer");
        closeButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(e -> stage.close());

        // Conteneur pour centrer le bouton
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().add(closeButton);

        vbox.getChildren().addAll(imageContainer, titleLabel, descLabel, authorLabel, buttonContainer);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 600, 700); // Augmenté la hauteur pour accommoder l'image
        stage.setScene(scene);
        stage.show();
    }
}
