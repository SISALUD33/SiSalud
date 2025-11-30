package core.negocios;

import core.datos.dao.interfaces.ICuidadorDAO;
import core.datos.dao.mysql.CuidadorDAO_MySQL;

public class CuidadorNegocios {

    private final ICuidadorDAO cuidadorDAO;

    public CuidadorNegocios() {
        this.cuidadorDAO = new CuidadorDAO_MySQL();
    }

    public boolean crearCuidador(int idUsuario) {
        return cuidadorDAO.crearCuidador(idUsuario);
    }

    public java.util.List<core.datos.dto.CuidadorDTO> listarTodos() {
        return cuidadorDAO.listarTodos();
    }
}
