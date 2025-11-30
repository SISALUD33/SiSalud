package core.datos.dao.interfaces;

import core.datos.dto.CuidadorDTO;
import java.util.List;

public interface ICuidadorDAO {
    List<CuidadorDTO> listarTodos();   // para llenar combo
     boolean crearCuidador(int idUsuario);
}
