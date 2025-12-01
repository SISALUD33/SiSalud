package core.ui;

import core.datos.dto.UsuarioDTO;
import core.microservicios.ReportesService;
import core.negocios.ClinicaNegocios;
import core.negocios.CuidadorNegocios;
import core.negocios.PacienteNegocios;
import core.negocios.ProfesionalSaludNegocios;
import core.negocios.UsuarioNegocios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FrmAdminUsuarios extends JFrame {

    private JTable tblUsuarios;
    private JButton btnCrear;
    private JButton btnActivarDesactivar;
    private JButton btnRefrescar;
    private JButton btnDescargarReporte;

    private final UsuarioNegocios usuarioNegocios = new UsuarioNegocios();
    private final PacienteNegocios pacienteNegocios = new PacienteNegocios();
    private final CuidadorNegocios cuidadorNegocios = new CuidadorNegocios();
    private final ProfesionalSaludNegocios profesionalSaludNegocios = new ProfesionalSaludNegocios();
    private final ClinicaNegocios clinicaNegocios = new ClinicaNegocios();

    public FrmAdminUsuarios() {

        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        setTitle("SI-SALUD - Administración de Usuarios");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tblUsuarios = new JTable();
        JScrollPane scroll = new JScrollPane(tblUsuarios);

        btnCrear = new JButton("Crear Usuario");
        btnActivarDesactivar = new JButton("Activar / Desactivar");
        btnRefrescar = new JButton("Refrescar");
        btnDescargarReporte = new JButton("Descargar reporte");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnActivarDesactivar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnDescargarReporte);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnRefrescar.addActionListener(e -> cargarUsuarios());
        btnCrear.addActionListener(e -> abrirDialogoCrearUsuario());
        btnDescargarReporte.addActionListener(e -> descargarReporteUsuarios()); // 👈
        btnActivarDesactivar.addActionListener(e -> cambiarEstadoUsuarioSeleccionado());
    }

    // ========================= CARGA DE DATA =============================
    private void cargarUsuarios() {
        List<UsuarioDTO> lista = usuarioNegocios.listarUsuarios();

        String[] columnas = {
            "ID", "Nombre", "Correo", "Teléfono", "Rol", "Activo", "Fecha Registro"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (UsuarioDTO u : lista) {
            Object[] fila = {
                u.getIdUsuario(),
                u.getNombre(),
                u.getCorreo(),
                u.getTelefono(),
                obtenerNombreRol(u.getIdRol()),
                u.isActivo() ? "Sí" : "No",
                u.getFechaRegistro()
            };
            modelo.addRow(fila);
        }

        tblUsuarios.setModel(modelo);
    }

    private void descargarReporteUsuarios() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte de usuarios");
        chooser.setSelectedFile(new File("reporte_usuarios_si_salud.xlsx"));

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File destino = chooser.getSelectedFile();

            boolean ok = ReportesService.generarReporteUsuariosExcel(destino);

            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Reporte generado correctamente en:\n" + destino.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo generar el reporte de usuarios.");
            }
        }
    }

    private String obtenerNombreRol(int idRol) {
        switch (idRol) {
            case 1:
                return "Paciente";
            case 2:
                return "Cuidador";
            case 3:
                return "Donante";
            case 4:
                return "Admin";
            case 5:
                return "Medico";
            case 6:
                return "Clinica";
            default:
                return "Rol desconocido";
        }
    }

    // ========================= ACCIONES =================================
    private void abrirDialogoCrearUsuario() {
        DlgCrearUsuario dlg = new DlgCrearUsuario(this, true);
        dlg.setVisible(true);

        if (dlg.fueCreado()) {
            cargarUsuarios();
        }
    }

    private void cambiarEstadoUsuarioSeleccionado() {
        int fila = tblUsuarios.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idUsuario = (int) tblUsuarios.getValueAt(fila, 0);
        boolean activo = "Sí".equals(tblUsuarios.getValueAt(fila, 5));

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de " + (activo ? "desactivar" : "activar") + " este usuario?",
                "Confirmar acción",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = usuarioNegocios.cambiarEstadoUsuario(idUsuario, !activo);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Usuario actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cambiar el estado del usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====================== MAIN PARA PRUEBAS ==========================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmAdminUsuarios().setVisible(true));
    }
}
