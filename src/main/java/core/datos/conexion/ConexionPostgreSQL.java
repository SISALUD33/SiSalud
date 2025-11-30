
package core.datos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionPostgreSQL {
    private static final String URL = "jdbc:postgresql://localhost:5432/sisalud_pg";
    private static final String USER = "sisalud_user";
    private static final String PASS = "123456";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            throw new SQLException("Error cargando driver PostgreSQL", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
