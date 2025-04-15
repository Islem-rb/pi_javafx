package tn.esprit.entities;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int postId;
    private String content;
    private LocalDateTime createdAt;
    private String userNom;
    private String userPrenom;

    public Comment() {
    }

    public Comment(String content, int postId) {
        this.content = content;
        this.postId = postId;
        this.createdAt = LocalDateTime.now();
    }

    public Comment(int id, int postId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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