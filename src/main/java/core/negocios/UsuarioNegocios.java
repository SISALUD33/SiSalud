package core.negocios;

import core.datos.dao.interfaces.IUsuarioDAO;
import core.datos.dao.mysql.UsuarioDAO_MySQL;
import core.datos.dto.UsuarioDTO;

public class UsuarioNegocios {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioNegocios() {
        this.usuarioDAO = new UsuarioDAO_MySQL();
    }

    public boolean registrarUsuario(UsuarioDTO u) {
        if (u.getNombre() == null || u.getNombre().isBlank()
                || u.getCorreo() == null || u.getCorreo().isBlank()
                || u.getClave() == null || u.getClave().isBlank()) {
            return false;
        }

        if (u.getIdRol() <= 0) {
            return false;
        }

        return usuarioDAO.registrarUsuario(u);
    }

    public UsuarioDTO login(String correo, String clave) {
        if (correo == null || correo.isBlank()
                || clave == null || clave.isBlank()) {
            return null;
        }
        return usuarioDAO.login(correo, clave);
    }

    public boolean cambiarClave(int idUsuario, String claveActual, String claveNueva) {
        if (claveNueva == null || claveNueva.length() < 4) {
            return false;
        }
        return usuarioDAO.cambiarClave(idUsuario, claveActual, claveNueva);
    }
}
