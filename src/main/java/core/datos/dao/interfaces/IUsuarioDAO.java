package core.datos.dao.interfaces;

import core.datos.dto.UsuarioDTO;
import java.util.List;

public interface IUsuarioDAO {

    boolean registrarUsuario(UsuarioDTO usuario);

    UsuarioDTO login(String correo, String clave);

    boolean cambiarClave(int idUsuario, String claveActual, String claveNueva);

    Integer registrarUsuarioRetornarID(UsuarioDTO usuario);

    List<UsuarioDTO> listarUsuarios();

    boolean cambiarEstadoUsuario(int idUsuario, boolean activo);
    
    
}
