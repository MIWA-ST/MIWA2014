package fr.epita.sigl.miwa.db;

public class InitMysqlConnector {

    public static void init() {
        /* Chargement du driver JDBC pour MySQL */
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR : loading com.mysql.jdbc.Driver : " + e.getMessage());
        }
    }
}
