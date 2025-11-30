package core.negocios;

import core.datos.dao.interfaces.IDonacionDAO;
import core.datos.dao.postgresql.DonacionDAO_PostgreSQL;
import core.datos.dto.DonacionDTO;
import core.microservicios.NotificacionService;

import java.time.LocalDate;
import java.util.List;
public class DonacionNegocios {

    private final IDonacionDAO donacionDAO;

    public DonacionNegocios() {
        this.donacionDAO = new DonacionDAO_PostgreSQL();
    }


    public boolean registrarDonacion(DonacionDTO donacion, String celularDonante) {
        boolean ok = donacionDAO.registrarDonacion(donacion);

        if (ok && celularDonante != null && !celularDonante.trim().isEmpty()) {
            String mensaje = "Gracias por tu donacion de $" + donacion.getMonto()
                    + " a la campaña #" + donacion.getIdCampana()
                    + ". Ref: " + (donacion.getReferenciaPago() != null ? donacion.getReferenciaPago() : "N/A");

            NotificacionService.enviarSMS(celularDonante, mensaje);
        }

        return ok;
    }


    public List<DonacionDTO> listarPorDonante(int idUsuarioDonante) {
        return donacionDAO.listarPorDonante(idUsuarioDonante);
    }

    public List<DonacionDTO> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return donacionDAO.listarPorRangoFechas(fechaInicio, fechaFin);
    }

    public DonacionDTO obtenerComprobante(int idDonacion) {
        return donacionDAO.obtenerComprobante(idDonacion);
    }

}
