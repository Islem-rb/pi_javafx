package tn.esprit.services;

import tn.esprit.entities.Comment;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceComment {
    private Connection connection;

    public ServiceComment() {
        connection = MyDatabase.getInstance().getConnection(); // ✅ version Singleton

    } //Initialise la connexion à la base

    private boolean userExists(int userId) {
        String query = "SELECT COUNT(*) FROM user WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getPatientUserId() {
        // Utiliser l'ID de l'utilisateur connecté ou un ID par défaut
        // Pour cet exemple, nous utiliserons l'ID 1 (l'employeur)
        return 1;
    }

    public boolean addComment(Comment comment) {
        int userId = getPatientUserId();
        if (userId == 0) {
            System.out.println("Aucun utilisateur trouvé");
            return false;
        }

        String query = "INSERT INTO comment (content, post_id, created_at, user_id) VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, comment.getContent());
            pst.setInt(2, comment.getPostId());
            pst.setInt(3, userId);
            pst.executeUpdate();

            // Get the user's full name and set it in the comment
            String userQuery = "SELECT firstname, lastname FROM user WHERE id = ?";
            try (PreparedStatement userPst = connection.prepareStatement(userQuery)) {
                userPst.setInt(1, userId);
                ResultSet rs = userPst.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                    comment.setUserNom(fullName);
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du commentaire: " + e.getMessage());
            return false;
        }
    }

    public List<Comment> getCommentsByPostId(int postId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT c.id, c.post_id, c.content, c.created_at, u.firstname, u.lastname " +
                      "FROM comment c " +
                      "INNER JOIN user u ON c.user_id = u.id " +
                      "WHERE c.post_id = ? " +
                      "ORDER BY c.created_at DESC";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, postId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Comment comment = new Comment(
                    rs.getInt("c.id"),
                    rs.getInt("c.post_id"),
                    rs.getString("c.content"),
                    rs.getTimestamp("c.created_at").toLocalDateTime()
                );
                comment.setUserPrenom(rs.getString("u.firstname"));
                comment.setUserNom(rs.getString("u.lastname"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commentaires: " + e.getMessage());
            e.printStackTrace();
        }
        return comments;
    }

    public boolean updateComment(Comment comment) {
        String query = "UPDATE comment SET content = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, comment.getContent());
            pst.setInt(2, comment.getId());
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteComment(int commentId) {
        String query = "DELETE FROM comment WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commentId);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCommentsByPostId(int postId) {
        String query = "DELETE FROM comment WHERE post_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, postId);
            pst.executeUpdate();
            System.out.println("Commentaires supprimés pour le post " + postId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 