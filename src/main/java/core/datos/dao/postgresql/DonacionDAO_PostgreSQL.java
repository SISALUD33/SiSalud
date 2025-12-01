package core.datos.dao.postgresql;

import core.datos.conexion.ConexionPostgreSQL;
import core.datos.dao.interfaces.IDonacionDAO;
import core.datos.dto.DonacionDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DonacionDAO_PostgreSQL implements IDonacionDAO {

    @Override
    public boolean registrarDonacion(DonacionDTO d) {
        String sql = "{ CALL public.sp_registrar_donacion(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionPostgreSQL.getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, d.getIdCampana());
            cs.setInt(2, d.getIdUsuarioDonante());
            cs.setDouble(3, d.getMonto());
            cs.setString(4, d.getEntidadBancaria());
            cs.setString(5, d.getMedioPago());
            cs.setString(6, d.getDestinoRecursos());
            cs.setString(7, d.getReferenciaPago());

            cs.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DonacionDTO> listarPorDonante(int idUsuarioDonante) {
        List<DonacionDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM fn_reporte_donaciones_por_donante(?)";

        try (Connection conn = ConexionPostgreSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioDonante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonacionDTO d = new DonacionDTO();
                    d.setIdDonacion(rs.getInt("id_donacion"));
                    d.setIdCampana(rs.getInt("id_campana"));
                    d.setMonto(rs.getDouble("monto"));
                    Timestamp ts = rs.getTimestamp("fecha");
                    d.setFecha(ts != null ? ts.toString() : null);
                    d.setEntidadBancaria(rs.getString("entidad_bancaria"));
                    d.setMedioPago(rs.getString("medio_pago"));
                    d.setDestinoRecursos(rs.getString("destino"));
                    lista.add(d);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<DonacionDTO> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<DonacionDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM fn_reporte_donaciones_rango_fechas(?, ?)";

        try (Connection conn = ConexionPostgreSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonacionDTO d = new DonacionDTO();
                    d.setIdDonacion(rs.getInt("id_donacion"));
                    d.setIdCampana(rs.getInt("id_campana"));
                    d.setIdUsuarioDonante(rs.getInt("id_usuario_donante"));
                    d.setMonto(rs.getDouble("monto"));
                    Timestamp ts = rs.getTimestamp("fecha");
                    d.setFecha(ts != null ? ts.toString() : null);
                    d.setEntidadBancaria(rs.getString("entidad_bancaria"));
                    d.setDestinoRecursos(rs.getString("destino_recursos"));
                    lista.add(d);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public DonacionDTO obtenerComprobante(int idDonacion) {
        String sql = "SELECT * FROM fn_comprobante_donacion(?)";
        DonacionDTO d = null;

        try (Connection conn = ConexionPostgreSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDonacion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new DonacionDTO();
                    d.setIdDonacion(rs.getInt("id_donacion"));
                    d.setIdCampana(rs.getInt("id_campana"));
                    d.setIdUsuarioDonante(rs.getInt("id_usuario_donante"));
                    d.setMonto(rs.getDouble("monto"));
                    Timestamp ts = rs.getTimestamp("fecha");
                    d.setFecha(ts != null ? ts.toString() : null);
                    d.setEntidadBancaria(rs.getString("entidad_bancaria"));
                    d.setMedioPago(rs.getString("medio_pago"));
                    d.setReferenciaPago(rs.getString("referencia_pago"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }
}
