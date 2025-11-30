package core.negocios;

import core.datos.dao.interfaces.ICampañaDAO;
import core.datos.dao.mysql.CampañaDAO_MySQL;
import core.datos.dto.CampañaDTO;
import core.microservicios.NotificacionService;

import java.util.List;

public class CampañaNegocios {

    private final ICampañaDAO campañaDAO;

    public CampañaNegocios() {
        this.campañaDAO = new CampañaDAO_MySQL();
    }

    public boolean registrarCampaña(CampañaDTO c, String telefonoMedico) {
        boolean ok = campañaDAO.crearCampaña(c);

        if (ok) {
            NotificacionService.enviarSMS(
                    telefonoMedico,
                    "Nueva campaña creada: '" + c.getTitulo() + "'. Requiere verificación médica."
            );
        }

        return ok;
    }

    public boolean cambiarEstado(int idCampaña, String nuevoEstado, String telefonoDestino) {
        boolean ok = campañaDAO.actualizarEstado(idCampaña, nuevoEstado);

        if (ok && nuevoEstado.equalsIgnoreCase("APROBADA")) {
            NotificacionService.enviarSMS(
                    telefonoDestino,
                    "Tu campaña #" + idCampaña + " ha sido APROBADA. Gracias por usar SI-SALUD."
            );
        }

        return ok;
    }

    public List<CampañaDTO> obtenerCampañasAprobadas() {
        return campañaDAO.listarAprobadas();
    }
}
