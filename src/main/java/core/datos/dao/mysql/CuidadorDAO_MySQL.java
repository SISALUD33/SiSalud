
package core.datos.dao.mysql;


import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.ICuidadorDAO;
import core.datos.dto.CuidadorDTO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CuidadorDAO_MySQL implements ICuidadorDAO {

    @Override
    public List<CuidadorDTO> listarTodos() {
        List<CuidadorDTO> lista = new ArrayList<>();
        String sql = "{ CALL sisalud_mysql.sp_cuidador_listar() }";

        try (Connection conn = ConexionMySQL.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                CuidadorDTO c = new CuidadorDTO();
                c.setIdCuidador(rs.getInt("id_cuidador"));
                c.setIdUsuario(rs.getInt("id_usuario"));
                c.setNombre(rs.getString("nombre"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));
                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
        @Override
    public boolean crearCuidador(int idUsuario) {
        String sql = "{ CALL sp_cuidador_crear(?) }";

        try (Connection conn = ConexionMySQL.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuario);
            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
