
package core.datos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionMySQL {
    
    private static final String URL = "jdbc:mysql://localhost:3306/sisalud_mysql?useSSL=false&serverTimezone=America/Bogota";
    private static final String USER = "sisalud_user";
    private static final String PASS = "123456";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new SQLException("Error cargando driver MySQL", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
