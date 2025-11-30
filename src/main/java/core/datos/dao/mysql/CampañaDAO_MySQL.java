package core.datos.dao.mysql;

import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.ICampañaDAO;
import core.datos.dto.CampañaDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampañaDAO_MySQL implements ICampañaDAO {

    @Override
    public boolean crearCampaña(CampañaDTO c) {
        String sql = "{ CALL sisalud_mysql.sp_campaña_crear(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, c.getTitulo());
            cs.setString(2, c.getDescripcion());
            cs.setDouble(3, c.getMeta());

            // PACIENTE (obligatorio)
            if (c.getIdPaciente() != null) {
                cs.setInt(4, c.getIdPaciente());
            } else {
                throw new IllegalArgumentException("La campaña debe tener un paciente asociado (idPaciente no puede ser null).");
            }

            // CUIDADOR
            if (c.getIdCuidador() != null) {
                cs.setInt(5, c.getIdCuidador());
            } else {
                cs.setNull(5, java.sql.Types.INTEGER);
            }

            // PROFESIONAL
            if (c.getIdProfesionalSalud() != null) {
                cs.setInt(6, c.getIdProfesionalSalud());
            } else {
                cs.setNull(6, java.sql.Types.INTEGER);
            }

            // CLÍNICA
            if (c.getIdClinica() != null) {
                cs.setInt(7, c.getIdClinica());
            } else {
                cs.setNull(7, java.sql.Types.INTEGER);
            }

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarEstado(int idCampania, String nuevoEstado) {
        String sql = "{ CALL sisalud_mysql.sp_campaña_actualizar_estado(?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idCampania);
            cs.setString(2, nuevoEstado);

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CampañaDTO obtenerPorId(int idCampania) {
        String sql = "{ CALL sisalud_mysql.sp_campaña_obtener_por_id(?) }";
        CampañaDTO c = null;

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idCampania);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    c = new CampañaDTO();
                    c.setIdCampania(rs.getInt("id_campaña"));
                    c.setTitulo(rs.getString("titulo"));
                    c.setDescripcion(rs.getString("descripcion"));
                    c.setMeta(rs.getDouble("meta"));
                    c.setRecaudado(rs.getDouble("recaudado"));
                    c.setEstado(rs.getString("estado"));
                    c.setFechaCreacion(rs.getString("fecha_creacion"));

                    int idPac = rs.getInt("id_paciente");
                    c.setIdPaciente(rs.wasNull() ? null : idPac);

                    int idCui = rs.getInt("id_cuidador");
                    c.setIdCuidador(rs.wasNull() ? null : idCui);

                    int idProf = rs.getInt("id_profesional_salud");
                    c.setIdProfesionalSalud(rs.wasNull() ? null : idProf);

                    int idCli = rs.getInt("id_clinica");
                    c.setIdClinica(rs.wasNull() ? null : idCli);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    @Override
    public List<CampañaDTO> listarAprobadas() {
        List<CampañaDTO> lista = new ArrayList<>();
        String sql = "{ CALL sisalud_mysql.sp_campaña_listar_aprobadas() }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                CampañaDTO c = new CampañaDTO();
                c.setIdCampania(rs.getInt("id_campaña"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setMeta(rs.getDouble("meta"));
                c.setRecaudado(rs.getDouble("recaudado"));
                c.setEstado(rs.getString("estado"));
                c.setFechaCreacion(rs.getString("fecha_creacion"));

                int idPac = rs.getInt("id_paciente");
                c.setIdPaciente(rs.wasNull() ? null : idPac);

                int idCui = rs.getInt("id_cuidador");
                c.setIdCuidador(rs.wasNull() ? null : idCui);

                int idProf = rs.getInt("id_profesional_salud");
                c.setIdProfesionalSalud(rs.wasNull() ? null : idProf);

                int idCli = rs.getInt("id_clinica");
                c.setIdClinica(rs.wasNull() ? null : idCli);

                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
