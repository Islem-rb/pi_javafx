package tn.esprit.services;

import tn.esprit.entities.Post;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePost {

    private Connection connection;
    private final ServiceComment serviceComment;

    public ServicePost() {
        connection = MyDatabase.getInstance().getConnection(); // ✅ Singleton appliqué
        serviceComment = new ServiceComment();
    }


    public boolean addPost(Post post) {
        System.out.println("Tentative d'ajout d'un post avec user_id: " + post.getUserId());
        
        // Si l'ID de l'utilisateur n'est pas défini, utiliser l'ID 2 par défaut
        int userId = post.getUserId() > 0 ? post.getUserId() : 2;
        System.out.println("Utilisation de l'ID utilisateur: " + userId);
        
        String query = "INSERT INTO post (title, description, image, created_at, user_id) VALUES (?, ?, ?, NOW(), ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, post.getTitle());
            pst.setString(2, post.getDescription());
            pst.setString(3, post.getImagePath());
            pst.setInt(4, userId);
            
            System.out.println("Titre: " + post.getTitle());
            System.out.println("Description: " + post.getDescription());
            System.out.println("Image: " + post.getImagePath());
            
            int rowsAffected = pst.executeUpdate();
            System.out.println("Post ajouté avec succès !");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de l'ajout du post: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT p.*, u.firstname, u.lastname FROM post p " +
                      "JOIN user u ON p.user_id = u.id " +
                      "ORDER BY p.created_at DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("image"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("user_id")
                );
                post.setUserPrenom(rs.getString("firstname"));
                post.setUserNom(rs.getString("lastname"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public void updatePost(Post post) {
        String query = "UPDATE post SET title = ?, description = ?, image = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, post.getTitle());
            pst.setString(2, post.getDescription());
            pst.setString(3, post.getImagePath());
            pst.setInt(4, post.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Post mis à jour !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletePost(Post post) {
        // D'abord supprimer tous les commentaires associés au post
        if (!serviceComment.deleteCommentsByPostId(post.getId())) {
            System.out.println("Erreur lors de la suppression des commentaires");
            return false;
        }

        // Ensuite supprimer le post
        String query = "DELETE FROM post WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, post.getId());
            int rowsAffected = pst.executeUpdate();
            System.out.println("Post supprimé !");
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
