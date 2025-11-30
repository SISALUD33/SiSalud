package core.negocios;

import core.datos.conexion.ConexionMySQL;
import core.datos.dao.interfaces.ICampañaDAO;
import core.datos.dao.interfaces.IClinicaDAO;
import core.datos.dao.interfaces.IProfesionalSaludDAO;
import core.datos.dao.interfaces.ICuidadorDAO;
import core.datos.dao.mysql.CampañaDAO_MySQL;
import core.datos.dao.mysql.ClinicaDAO_MySQL;
import core.datos.dao.mysql.ProfesionalSaludDAO_MySQL;
import core.datos.dao.mysql.CuidadorDAO_MySQL;
import core.datos.dto.CampañaDTO;
import core.datos.dto.ClinicaDTO;
import core.datos.dto.ProfesionalSaludDTO;
import core.datos.dto.CuidadorDTO;
import core.microservicios.NotificacionService;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class CampañaNegocios {

    private final ICampañaDAO campañaDAO;
    private final IClinicaDAO clinicaDAO;
    private final IProfesionalSaludDAO profesionalDAO;
    private final ICuidadorDAO cuidadorDAO;

    public CampañaNegocios() {
        this.campañaDAO = new CampañaDAO_MySQL();
        this.clinicaDAO = new ClinicaDAO_MySQL();
        this.profesionalDAO = new ProfesionalSaludDAO_MySQL();
        this.cuidadorDAO = new CuidadorDAO_MySQL();
    }

    public boolean registrarCampaña(CampañaDTO c, String telefonoMedico) {
        boolean ok = campañaDAO.crearCampaña(c);

        if (ok && telefonoMedico != null && !telefonoMedico.trim().isEmpty()) {
            NotificacionService.enviarSMS(
                    telefonoMedico,
                    "Nueva campaña creada: '" + c.getTitulo() + "'. Requiere verificación médica."
            );
        }

        return ok;
    }

    public boolean cambiarEstadoCampania(int idCampania, String nuevoEstado) {
        boolean ok = campañaDAO.actualizarEstado(idCampania, nuevoEstado);

        if (!ok) {
            return false;
        }

        CampañaDTO campaña = campañaDAO.obtenerPorId(idCampania);
        if (campaña == null) {
            return true; // estado se cambió, aunque no podamos notificar
        }

        // Mensajes base
        String msgPacienteCuidador;
        String msgMedico;

        if ("APROBADA".equalsIgnoreCase(nuevoEstado)) {
            msgPacienteCuidador = "Tu campaña '" + campaña.getTitulo()
                    + "' ha sido APROBADA en SI-SALUD.";
            msgMedico = "La campaña '" + campaña.getTitulo()
                    + "' ha sido aprobada. Verifica la historia clinica del paciente.";
        } else if ("RECHAZADA".equalsIgnoreCase(nuevoEstado)) {
            msgPacienteCuidador = "Tu campaña '" + campaña.getTitulo()
                    + "' ha sido RECHAZADA. Revisa la informacion en el sistema.";
            msgMedico = null; // no es obligatorio notificar médico
        } else if ("CERRADA".equalsIgnoreCase(nuevoEstado)) {
            msgPacienteCuidador = "La campaña '" + campaña.getTitulo()
                    + "' ha sido CERRADA. Gracias por participar.";
            msgMedico = null;
        } else {
            // otros estados no envian nada especial
            msgPacienteCuidador = null;
            msgMedico = null;
        }

        if (msgPacienteCuidador != null) {
            notificarPacienteYCuidador(campaña, msgPacienteCuidador);
        }

        if (msgMedico != null) {
            notificarMedico(campaña, msgMedico);
        }

        return true;
    }

    // =================== Métodos privados de apoyo ===================
    private void notificarPacienteYCuidador(CampañaDTO campaña, String mensaje) {
        try (Connection conn = ConexionMySQL.getConnection()) {

            // PACIENTE
            if (campaña.getIdPaciente() != null) {
                String telPaciente = obtenerTelefonoPaciente(conn, campaña.getIdPaciente());
                if (telPaciente != null && !telPaciente.isEmpty()) {
                    NotificacionService.enviarSMS(telPaciente, mensaje);
                }
            }

            // CUIDADOR
            if (campaña.getIdCuidador() != null) {
                String telCuidador = obtenerTelefonoCuidador(conn, campaña.getIdCuidador());
                if (telCuidador != null && !telCuidador.isEmpty()) {
                    NotificacionService.enviarSMS(telCuidador, mensaje);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notificarMedico(CampañaDTO campaña, String mensaje) {
        if (campaña.getIdProfesionalSalud() == null) {
            return;
        }

        try (Connection conn = ConexionMySQL.getConnection()) {
            String telMedico = obtenerTelefonoProfesional(conn, campaña.getIdProfesionalSalud());
            if (telMedico != null && !telMedico.isEmpty()) {
                NotificacionService.enviarSMS(telMedico, mensaje);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------- helpers que llaman a los SP de contacto ---------
    private String obtenerTelefonoPaciente(Connection conn, int idPaciente) throws Exception {
        String sql = "{ CALL sisalud_mysql.sp_paciente_contacto(?) }";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idPaciente);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("telefono");
                }
            }
        }
        return null;
    }

    private String obtenerTelefonoCuidador(Connection conn, int idCuidador) throws Exception {
        String sql = "{ CALL sisalud_mysql.sp_cuidador_contacto(?) }";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idCuidador);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("telefono");
                }
            }
        }
        return null;
    }

    private String obtenerTelefonoProfesional(Connection conn, int idProfesional) throws Exception {
        String sql = "{ CALL sisalud_mysql.sp_profesional_salud_contacto(?) }";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idProfesional);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("telefono");
                }
            }
        }
        return null;
    }

    public List<CampañaDTO> obtenerCampañasAprobadas() {
        return campañaDAO.listarAprobadas();
    }

    public List<CampañaDTO> obtenerCampaniasPorPaciente(int idPaciente) {
        return campañaDAO.listarPorPaciente(idPaciente);
    }

    public List<ClinicaDTO> listarClinicas() {
        return clinicaDAO.listarTodas();
    }

    public List<ProfesionalSaludDTO> listarProfesionalesPorClinica(int idClinica) {
        return profesionalDAO.listarPorClinica(idClinica);
    }

    public List<CuidadorDTO> listarCuidadores() {
        return cuidadorDAO.listarTodos();
    }
}
