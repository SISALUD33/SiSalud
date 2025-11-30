package core.datos.dao.mysql;

import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.IClinicaDAO;
import core.datos.dto.ClinicaDTO;

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
    public boolean crearClinica(String nombre, String nit, String direccion,
            String telefono, String correo, int idUsuario) {

        String sql = "{ CALL sisalud_mysql.sp_clinica_crear(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, nombre);
            cs.setString(2, nit);
            cs.setString(3, direccion);
            cs.setString(4, telefono);
            cs.setString(5, correo);

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
