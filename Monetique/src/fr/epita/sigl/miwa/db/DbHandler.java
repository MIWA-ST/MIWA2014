package fr.epita.sigl.miwa.db;

import java.sql.*;

public class DbHandler {

    String URL = "jdbc:mysql://localhost:3306/monedb";
    String USER = "root";
    String PASSWORD = "root";

    Connection connection = null;

    public Connection open() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch ( SQLException e ) {
            System.out.println("ERROR : " + e.getMessage());
        }

        return connection;
    }

    public void close() {
        if ( connection != null ) {
            try {
                /* Fermeture de la connexion */
                connection.close();
            } catch ( SQLException ignore ) {
                /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
            }
        }
    }
}
