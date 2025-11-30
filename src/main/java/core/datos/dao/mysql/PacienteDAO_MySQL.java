package core.datos.dao.mysql;

import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.IPacienteDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

public class PacienteDAO_MySQL implements IPacienteDAO {

    @Override
    public Integer obtenerIdPacientePorUsuario(int idUsuario) {
        String sql = "{ CALL sisalud_mysql.sp_paciente_obtener_por_usuario(?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuario);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    // Ajusta si tu PK tiene otro nombre
                    return rs.getInt("id_paciente");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer obtenerOCrearPacientePorUsuario(int idUsuario) {
        Integer idPac = obtenerIdPacientePorUsuario(idUsuario);
        if (idPac != null) {
            return idPac;
        }

        // Si no existe paciente, lo creamos con datos básicos
        String sql = "{ CALL sisalud_mysql.sp_paciente_crear(?, ?, ?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuario);

            // Si id_clinica permite NULL:
            cs.setNull(2, Types.INTEGER);

            cs.setString(3, "AUTO-HC-" + idUsuario);
            cs.setString(4, "Paciente creado automáticamente desde SI-SALUD.");

            cs.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return obtenerIdPacientePorUsuario(idUsuario);
    }
}
