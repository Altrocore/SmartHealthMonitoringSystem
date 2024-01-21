package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // private static final String url = "jdbc:postgresql://localhost:5432/APJavaFinalSprint";
    private static final String url = "jdbc:postgresql://localhost:5432/healthMonitoring";
    private static final String user = "postgres";
    private static final String password = "password";

    public static Connection getCon(){
        Connection connection = null;
        try{
            Class.forName("org.postgresql.Driver");     // For Postgres
//            Class.forName("com.mysql.jdbc.Driver");  // For MySQL
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException  e) {
            e.printStackTrace();
        }
        return connection;
    }


}
