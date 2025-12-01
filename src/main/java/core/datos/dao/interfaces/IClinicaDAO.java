package core.datos.dao.interfaces;

import core.datos.dto.ClinicaDTO;
import core.datos.dto.UsuarioDTO;
import java.util.List;

public interface IClinicaDAO {

      boolean crearClinicaBasicaDesdeUsuario(UsuarioDTO usuario);


    List<ClinicaDTO> listarTodas();
}
