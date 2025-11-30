
package core.negocios;

import core.datos.dao.interfaces.IProfesionalSaludDAO;
import core.datos.dao.mysql.ProfesionalSaludDAO_MySQL;


public class ProfesionalSaludNegocios {
    private final IProfesionalSaludDAO profesionalDAO;

    public ProfesionalSaludNegocios() {
        this.profesionalDAO = new ProfesionalSaludDAO_MySQL();
    }

    public boolean crearProfesional(Integer idUsuario,
                                    Integer idClinica,
                                    String especialidad,
                                    String registroMedico) {

        if (idUsuario == null) return false;
        if (idClinica == null) return false;

        return profesionalDAO.crearProfesional(
                idUsuario, idClinica, especialidad, registroMedico);
    }

    public java.util.List<core.datos.dto.ProfesionalSaludDTO> listarPorClinica(int idClinica) {
        return profesionalDAO.listarPorClinica(idClinica);
    }
}
