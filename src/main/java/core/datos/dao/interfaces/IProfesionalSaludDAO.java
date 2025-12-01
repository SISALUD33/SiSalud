
package core.datos.dao.interfaces;


import core.datos.dto.ProfesionalSaludDTO;
import java.util.List;

public interface IProfesionalSaludDAO {
    List<ProfesionalSaludDTO> listarPorClinica(int idClinica);
    
       boolean crearProfesionalBasico(int idUsuario);

}