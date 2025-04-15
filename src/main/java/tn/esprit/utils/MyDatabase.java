package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    // Configuration de ta base "sabeeltest"
    private static final String URL = "jdbc:mysql://localhost:3306/sabeeltest";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;        // Instance unique de Connection
    private static MyDatabase instance;   // Instance unique de MyDatabase
    //signifie que l'attribut appartient à la classe elle meme pas à un objet

    // Constructeur privé pour empêcher la création avec `new`
    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion à sabeeltest établie !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion : " + e.getMessage());
        }
    }

    // Méthode d'accès à l'instance unique
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    // Retourne l'objet Connection
    public Connection getConnection() {
        return connection;
    }
}
