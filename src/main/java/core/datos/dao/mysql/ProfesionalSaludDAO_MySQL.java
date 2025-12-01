package core.datos.dao.mysql;

import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.IProfesionalSaludDAO;
import core.datos.dto.ProfesionalSaludDTO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProfesionalSaludDAO_MySQL implements IProfesionalSaludDAO {

    @Override
    public List<ProfesionalSaludDTO> listarPorClinica(int idClinica) {
        List<ProfesionalSaludDTO> lista = new ArrayList<>();
        String sql = "{ CALL sisalud_mysql.sp_profesional_salud_listar_por_clinica(?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idClinica);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    ProfesionalSaludDTO p = new ProfesionalSaludDTO();
                    p.setIdProfesional(rs.getInt("id_profesional"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setIdClinica(rs.getInt("id_clinica"));
                    p.setEspecialidad(rs.getString("especialidad"));
                    p.setRegistroMedico(rs.getString("registro_medico"));
                    // campos que vienen del JOIN usuario en el SP
                    p.setNombre(rs.getString("nombre"));
                    p.setCorreo(rs.getString("correo"));
                    p.setTelefono(rs.getString("telefono"));
                    lista.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public boolean crearProfesionalBasico(int idUsuario) {
        String sql = "{ CALL sisalud_mysql.sp_profesional_salud_crear(?, ?, ?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuario);                  // usuario recién creado
            cs.setNull(2, Types.INTEGER);             // id_clinica (pendiente/asignar después)
            cs.setString(3, "GENERAL");               // especialidad por defecto
            cs.setString(4, "PENDIENTE");             // registro médico por defecto

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
