package core.negocios;

import core.datos.dao.interfaces.IClinicaDAO;
import core.datos.dao.mysql.ClinicaDAO_MySQL;
import core.datos.dto.UsuarioDTO;

public class ClinicaNegocios {

    private final IClinicaDAO clinicaDAO;

    public ClinicaNegocios() {
        this.clinicaDAO = new ClinicaDAO_MySQL();
    }

    public boolean crearClinicaBasicaDesdeUsuario(UsuarioDTO u) {
        if (u == null) {
            return false;
        }
        return clinicaDAO.crearClinicaBasicaDesdeUsuario(u);
    }

    public java.util.List<core.datos.dto.ClinicaDTO> listarTodas() {
        return clinicaDAO.listarTodas();
    }
}
