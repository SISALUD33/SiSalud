package core.datos.dao.interfaces;

import core.datos.dto.UsuarioDTO;

public interface IUsuarioDAO {

    boolean registrarUsuario(UsuarioDTO usuario);

    UsuarioDTO login(String correo, String clave);
    
    boolean cambiarClave(int idUsuario, String claveActual, String claveNueva);

}
