
package core.datos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionPostgreSQL {
    private static final String URL = "jdbc:postgresql://localhost:5432/sisalud_pg";
    private static final String USER = "postgres";
    private static final String PASS = "Sisalud1234*";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            throw new SQLException("Error cargando driver PostgreSQL", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
