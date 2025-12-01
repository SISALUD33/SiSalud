package core.negocios;

import core.datos.dao.interfaces.IProfesionalSaludDAO;
import core.datos.dao.mysql.ProfesionalSaludDAO_MySQL;

public class ProfesionalSaludNegocios {

    private final IProfesionalSaludDAO profesionalDAO;

    public ProfesionalSaludNegocios() {
        this.profesionalDAO = new ProfesionalSaludDAO_MySQL();
    }

    public boolean crearProfesionalBasico(int idUsuario) {
        if (idUsuario <= 0) {
            return false;
        }
        return profesionalDAO.crearProfesionalBasico(idUsuario);
    }

    public java.util.List<core.datos.dto.ProfesionalSaludDTO> listarPorClinica(int idClinica) {
        return profesionalDAO.listarPorClinica(idClinica);
    }
}
