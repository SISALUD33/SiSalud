package core.datos.dao.interfaces;

import core.datos.dto.ClinicaDTO;
import java.util.List;

public interface IClinicaDAO {

    boolean crearClinica(String nombre, String nit, String direccion,
            String telefono, String correo, int idUsuario);

    List<ClinicaDTO> listarTodas();
}
