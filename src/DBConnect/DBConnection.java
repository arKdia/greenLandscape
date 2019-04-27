/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mario
 */
public class DBConnection {
    private static Connection conn;
    private static String url = "jdbc:postgresql://localhost:5432/greenLandscape";
    private static String user = "postgres";
    private static String pass = "";
    
    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            
        } catch(ClassNotFoundException cnfe) {
            System.err.println("Error: "+cnfe.getMessage() );
        }
        conn = DriverManager.getConnection(url, user, pass);
        System.out.println("Connection established");
        return conn;
    }
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        if(conn != null && !conn.isClosed()){
            System.out.println("Connection established");
            return conn;
        }
        
        connect();
        return conn;
    }
}
