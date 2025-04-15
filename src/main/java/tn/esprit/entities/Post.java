package tn.esprit.entities;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private String title;
    private String description;
    private String imagePath;
    private LocalDateTime createdAt;
    private int userId;
    private String userNom;
    private String userPrenom;

    public Post(int id, String title, String description, String imagePath, LocalDateTime createdAt, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Post(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.createdAt = LocalDateTime.now(); //  date et l'heure actuelles grâce à LocalDateTime.now()
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNom() {
        return userNom;
    }

    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }

    public String getUserPrenom() {
        return userPrenom;
    }

    public void setUserPrenom(String userPrenom) {
        this.userPrenom = userPrenom;
    }
}
