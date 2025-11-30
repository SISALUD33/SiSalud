package core.datos.dao.mysql;

import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.IUsuarioDAO;
import core.datos.dto.UsuarioDTO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class UsuarioDAO_MySQL implements IUsuarioDAO {

    @Override
    public boolean registrarUsuario(UsuarioDTO u) {
        String sql = "{ CALL sp_usuario_registrar(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, u.getNombre());
            cs.setString(2, u.getCorreo());
            cs.setString(3, u.getTelefono());
            cs.setString(4, u.getClave());
            cs.setInt(5, u.getIdRol());

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UsuarioDTO login(String correo, String clave) {
        String sql = "{ CALL sp_usuario_login(?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, correo);
            cs.setString(2, clave);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    UsuarioDTO u = new UsuarioDTO();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setCorreo(rs.getString("correo"));
                    u.setTelefono(rs.getString("telefono"));   
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setActivo(rs.getBoolean("activo"));
                    u.setFechaRegistro(rs.getString("fecha_registro"));
                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // login fallido
    }

    @Override
    public boolean cambiarClave(int idUsuario, String claveActual, String claveNueva) {
        String sql = "{ CALL sp_usuario_cambiar_clave(?, ?, ?) }";

        try (Connection conn = ConexionMySQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuario);
            cs.setString(2, claveActual);
            cs.setString(3, claveNueva);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    String resultado = rs.getString("resultado");
                    return "OK".equalsIgnoreCase(resultado);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
