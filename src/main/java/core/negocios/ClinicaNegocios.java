package core.negocios;

import core.datos.dao.interfaces.IClinicaDAO;
import core.datos.dao.mysql.ClinicaDAO_MySQL;

public class ClinicaNegocios {

    private final IClinicaDAO clinicaDAO;

    public ClinicaNegocios() {
        this.clinicaDAO = new ClinicaDAO_MySQL();
    }

    public boolean crearClinica(String nombre, String nit, String direccion,
            String telefono, String correo, int idUsuario) {

        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        if (nit == null || nit.isBlank()) {
            return false;
        }

        return clinicaDAO.crearClinica(nombre, nit, direccion, telefono, correo, idUsuario);
    }

    public java.util.List<core.datos.dto.ClinicaDTO> listarTodas() {
        return clinicaDAO.listarTodas();
    }
}
