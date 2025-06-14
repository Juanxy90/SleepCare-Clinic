package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static Connection getConnection() {
        Connection con = null;
        try {
            String myBD = "jdbc:mysql://localhost:3306/sleepcareclinic";
            con = DriverManager.getConnection(myBD, "root", "");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return con;
    }
}
