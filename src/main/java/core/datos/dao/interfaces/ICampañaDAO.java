package core.datos.dao.interfaces;

import core.datos.dto.CampañaDTO;
import java.util.List;

public interface ICampañaDAO {

 
    boolean crearCampaña(CampañaDTO campaña);


    boolean actualizarEstado(int idCampania, String nuevoEstado);


    CampañaDTO obtenerPorId(int idCampania);


    List<CampañaDTO> listarAprobadas();
}
