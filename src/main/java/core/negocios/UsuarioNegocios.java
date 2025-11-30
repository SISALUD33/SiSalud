
package core.negocios;

import core.datos.dao.interfaces.IUsuarioDAO;
import core.datos.dao.mysql.UsuarioDAO_MySQL;


public class UsuarioNegocios {
    private final IUsuarioDAO usuarioDAO;

    public UsuarioNegocios() {
        this.usuarioDAO = new UsuarioDAO_MySQL();
    }

    public boolean cambiarClave(int idUsuario, String claveActual, String claveNueva) {
        return usuarioDAO.cambiarClave(idUsuario, claveActual, claveNueva);
    }
}
