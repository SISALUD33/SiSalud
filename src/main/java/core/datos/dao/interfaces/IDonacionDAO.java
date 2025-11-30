package core.datos.dao.interfaces;

import core.datos.dto.DonacionDTO;
import java.time.LocalDate;
import java.util.List;

public interface IDonacionDAO {

   boolean registrarDonacion(DonacionDTO donacion);

    List<DonacionDTO> listarPorDonante(int idUsuarioDonante);

    List<DonacionDTO> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    DonacionDTO obtenerComprobante(int idDonacion);
}
