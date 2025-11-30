
package core.datos.dao.interfaces;


import core.datos.dto.ProfesionalSaludDTO;
import java.util.List;

public interface IProfesionalSaludDAO {
    List<ProfesionalSaludDTO> listarPorClinica(int idClinica);
    
     boolean crearProfesional(int idUsuario, int idClinica, 
                             String especialidad, String registroMedico);
}