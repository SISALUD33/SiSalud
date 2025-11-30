package core.datos.dao.interfaces;

import core.datos.dto.CampañaDTO;

public interface ICampañaDAO {

    boolean crearCampaña(CampañaDTO campaña);

    boolean actualizarEstado(int idCampaña, String nuevoEstado);

    java.util.List<CampañaDTO> listarAprobadas();
}
