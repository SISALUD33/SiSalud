package core.datos.dao.mysql;

import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.IClinicaDAO;
import core.datos.dto.ClinicaDTO;
import core.datos.dto.UsuarioDTO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClinicaDAO_MySQL implements IClinicaDAO {

    @Override
    public List<ClinicaDTO> listarTodas() {
        List<ClinicaDTO> lista = new ArrayList<>();
        String sql = "{ CALL sisalud_mysql.sp_clinica_listar() }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                ClinicaDTO c = new ClinicaDTO();
                c.setIdClinica(rs.getInt("id_clinica"));
                c.setNombre(rs.getString("nombre"));
                c.setNit(rs.getString("nit"));
                c.setDireccion(rs.getString("direccion"));
                c.setTelefono(rs.getString("telefono"));
                c.setCorreo(rs.getString("correo"));
                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public boolean crearClinicaBasicaDesdeUsuario(UsuarioDTO u) {
        String sql = "{ CALL sisalud_mysql.sp_clinica_crear(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, u.getNombre());           // nombre de la clínica (razón social)
            cs.setString(2, "PENDIENTE");             // NIT por defecto
            cs.setString(3, "");                      // dirección (vacío por ahora)
            cs.setString(4, u.getTelefono());         // teléfono desde el usuario
            cs.setString(5, u.getCorreo());           // correo desde el usuario

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
