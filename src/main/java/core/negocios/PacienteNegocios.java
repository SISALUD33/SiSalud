package core.negocios;

import core.datos.dao.interfaces.IPacienteDAO;
import core.datos.dao.mysql.PacienteDAO_MySQL;

public class PacienteNegocios {

    private final IPacienteDAO pacienteDAO;

    public PacienteNegocios() {
        this.pacienteDAO = new PacienteDAO_MySQL();
    }

    public Integer obtenerOCrearPacientePorUsuario(int idUsuario) {
        return pacienteDAO.obtenerOCrearPacientePorUsuario(idUsuario);
    }
}
